#include "AuditComparison.h"
#include <iostream>
#include <stdexcept>

using namespace etlforge::audit;
int checks = 0;
void require(bool ok, const char* message) { ++checks; if(!ok) throw std::runtime_error(message); }
int main() {
    try {
        std::vector<unsigned char> key(32, 0x31);
        Rules rules;
        rules.ignoredPaths = {"/savedAt", "/databaseId"};
        rules.unorderedArrays = {"/properties"};
        rules.secretPaths = {"/customCredential"};
        auto snapshot = [&](const char* text){ return prepareSnapshot(boost::json::parse(text), rules, key, "key-1"); };
        auto first = snapshot(R"({"name":"Source","directory":"C:/temp","savedAt":"old","databaseId":1})");
        auto same = snapshot(R"( { "databaseId":99, "savedAt":"new", "directory":"C:/temp", "name":"Source" } )");
        require(!compareSnapshots(first,same), "unchanged resave/redeploy must be ignored");
        require(first.fingerprint == same.fingerprint, "canonical field ordering mismatch");
        require(first.fingerprint.size() == 64, "SHA256 must be 64 hex characters");
        require(!compareSnapshots(std::nullopt,std::nullopt), "absent objects must not create event");
        require(compareSnapshots(std::nullopt,first)->action == "Created", "creation");
        require(compareSnapshots(first,std::nullopt)->action == "Deleted", "deletion");
        auto changed = snapshot(R"({"name":"Source","directory":"C:/temp2"})");
        auto difference = compareSnapshots(first,changed);
        require(difference->action == "Updated" && difference->paths == std::vector<std::string>{"/directory"}, "field diff");
        require(compareSnapshots(changed,first).has_value(), "revert must be audited");
        auto renamed = snapshot(R"({"name":"Renamed","directory":"C:/temp"})");
        require(compareSnapshots(first,renamed)->paths == std::vector<std::string>{"/name"}, "rename");
        require(compareSnapshots(snapshot(R"({"script":"x='a b'"})"),snapshot(R"({"script":"x='ab'"})")).has_value(), "meaningful script whitespace must be preserved");
        require(compareSnapshots(snapshot(R"({"xml":"<p>a b</p>"})"),snapshot(R"({"xml":"<p>ab</p>"})")).has_value(), "XML text whitespace must be preserved");
        require(compareSnapshots(snapshot(R"({"x":null})"),snapshot(R"({"x":""})")).has_value(), "null differs from empty");
        require(compareSnapshots(snapshot(R"({})"),snapshot(R"({"x":null})")).has_value(), "missing differs from null");
        require(!compareSnapshots(snapshot(R"({"properties":[{"a":1},{"b":2}]})"),snapshot(R"({"properties":[{"b":2},{"a":1}]})")), "unordered child row reorder");
        require(compareSnapshots(snapshot(R"({"steps":[1,2]})"),snapshot(R"({"steps":[2,1]})")).has_value(), "ordered steps must preserve sequence");
        require(compareSnapshots(snapshot(R"({"mapping":{"child":{"value":1}}})"),snapshot(R"({"mapping":{"child":{"value":2}}})"))->paths[0] == "/mapping/child/value", "nested mapping changes");
        auto secret = snapshot(R"({"password":"never-display","properties":[{"keyname":"client_secret","keyvalue":"also-secret"}],"customCredential":"private"})");
        auto visible = canonicalJson(secret.visible);
        require(visible.find("never-display") == std::string::npos && visible.find("also-secret") == std::string::npos && visible.find("private") == std::string::npos, "secret leaked into UI snapshot");
        require(secret.protectedCanonical.find("never-display") == std::string::npos && secret.protectedCanonical.find("also-secret") == std::string::npos, "plaintext secret leaked into comparison state");
        require(!compareSnapshots(secret,secret), "same secret must not trigger changes");
        auto newSecret = snapshot(R"({"password":"changed","properties":[{"keyname":"client_secret","keyvalue":"also-secret"}],"customCredential":"private"})");
        require(compareSnapshots(secret,newSecret)->paths == std::vector<std::string>{"/password"}, "secret-only change must be visible as a changed path");
        auto collision = changed; collision.fingerprint = first.fingerprint;
        require(compareSnapshots(first,collision).has_value(), "comparison must not rely on hash alone");
        auto version = rules; version.version = "2";
        bool refused = false;
        try { compareSnapshots(first,prepareSnapshot(boost::json::parse(R"({"name":"Source"})"),version,key,"key-1")); } catch(const std::runtime_error&) {refused=true;}
        require(refused,"rule version migration must require rebaseline");
        refused = false;
        try { compareSnapshots(first,prepareSnapshot(boost::json::parse(R"({"name":"Source"})"),rules,key,"key-2")); } catch(const std::runtime_error&) {refused=true;}
        require(refused,"key version migration must require rebaseline");
        refused = false;
        try { prepareSnapshot(nullptr,rules,std::vector<unsigned char>(4),"1"); } catch(const std::runtime_error&) {refused=true;}
        require(refused,"short key rejected");
        require(!compareSnapshots(snapshot(u8R"({"name":"客户 😀"})"),snapshot(u8R"({"name":"客户 😀"})")), "Unicode deterministic");
        require(compareSnapshots(snapshot(R"({"a/b~c":1})"),snapshot(R"({"a/b~c":2})"))->paths[0] == "/a~1b~0c", "JSON pointer escaping");
        std::cout << "PASS " << checks << " native audit comparison checks\n";
        return 0;
    } catch(const std::exception& error) { std::cerr << "FAIL " << error.what() << '\n'; return 1; }
}
