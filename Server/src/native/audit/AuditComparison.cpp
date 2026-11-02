#undef _WIN32_WINNT
#define _WIN32_WINNT 0x0A00
#include "AuditComparison.h"
#include <windows.h>
#include <bcrypt.h>
#include <algorithm>
#include <cctype>
#include <limits>
#include <stdexcept>
#include <string_view>

namespace etlforge::audit {
namespace {
std::string digest(const std::string& input, const std::vector<unsigned char>* key = nullptr) {
    BCRYPT_ALG_HANDLE algorithm = nullptr;
    if (BCryptOpenAlgorithmProvider(&algorithm, BCRYPT_SHA256_ALGORITHM, nullptr,
        key ? BCRYPT_ALG_HANDLE_HMAC_FLAG : 0) < 0) throw std::runtime_error("Audit hash provider unavailable");
    struct Close { BCRYPT_ALG_HANDLE a; ~Close(){ BCryptCloseAlgorithmProvider(a, 0); } } close{algorithm};
    if (input.size() > std::numeric_limits<ULONG>::max()) throw std::runtime_error("Audit snapshot too large");
    unsigned char output[32];
    if (BCryptHash(algorithm, key ? const_cast<PUCHAR>(key->data()) : nullptr,
        key ? static_cast<ULONG>(key->size()) : 0,
        reinterpret_cast<PUCHAR>(const_cast<char*>(input.data())), static_cast<ULONG>(input.size()),
        output, sizeof(output)) < 0) throw std::runtime_error("Audit hash failed");
    const char* hex = "0123456789abcdef";
    std::string result;
    for (unsigned char byte : output) { result += hex[byte >> 4]; result += hex[byte & 15]; }
    return result;
}
std::string escaped(const std::string& value) {
    std::string result;
    for (char c : value) { if(c == '~') result += "~0"; else if(c == '/') result += "~1"; else result += c; }
    return result;
}
bool secretName(std::string name) {
    name.erase(std::remove_if(name.begin(), name.end(), [](unsigned char c){ return !std::isalnum(c); }), name.end());
    std::transform(name.begin(), name.end(), name.begin(), [](unsigned char c){return static_cast<char>(std::tolower(c));});
    return name.find("password") != std::string::npos || name.find("secret") != std::string::npos ||
        name.find("token") != std::string::npos || name == "authorization" || name == "privatekey" ||
        name == "apikey" || name.find("accesskey") != std::string::npos || name == "credential" || name == "credentials" ||
        name == "connectstring" || name == "optionalconnectstring";
}
struct Pair { boost::json::value secured, visible; };
Pair normalize(const boost::json::value& input, const std::string& path, const Rules& rules,
               const std::vector<unsigned char>& key, const std::string& keyVersion, bool secret, unsigned depth) {
    if (depth > 128) throw std::runtime_error("Audit object nesting exceeds 128");
    if (secret || rules.secretPaths.count(path)) {
        if (input.is_null()) return {nullptr, nullptr};
        return {boost::json::object{{"$secretHmac", digest(canonicalJson(input), &key)}, {"$keyVersion", keyVersion}}, "[REDACTED]"};
    }
    if (input.is_object()) {
        boost::json::object secured, visible;
        bool secretProperty = false;
        for (const char* property : {"keyname", "propertyName"}) {
            const auto* name = input.as_object().if_contains(property);
            if (name && name->is_string()) secretProperty |= secretName(std::string(name->as_string()));
        }
        for (const auto& item : input.as_object()) {
            std::string name(item.key());
            std::string childPath = path + "/" + escaped(name);
            if (rules.ignoredPaths.count(childPath)) continue;
            Pair child = normalize(item.value(), childPath, rules, key, keyVersion,
                secretName(name) || (secretProperty && (name == "keyvalue" || name == "value")), depth + 1);
            secured[name] = std::move(child.secured);
            visible[name] = std::move(child.visible);
        }
        return {std::move(secured), std::move(visible)};
    }
    if (input.is_array()) {
        std::vector<Pair> children;
        for (std::size_t i = 0; i < input.as_array().size(); ++i)
            children.push_back(normalize(input.as_array()[i], path + "/" + std::to_string(i), rules, key, keyVersion, false, depth + 1));
        if (rules.unorderedArrays.count(path)) std::sort(children.begin(), children.end(), [](const Pair& a, const Pair& b){
            return canonicalJson(a.secured) < canonicalJson(b.secured);
        });
        boost::json::array secured, visible;
        for (auto& child : children) { secured.push_back(std::move(child.secured)); visible.push_back(std::move(child.visible)); }
        return {std::move(secured), std::move(visible)};
    }
    return {input, input};
}
void differences(const boost::json::value& a, const boost::json::value& b, const std::string& path, std::vector<std::string>& paths) {
    if (canonicalJson(a) == canonicalJson(b)) return;
    if (a.is_object() && b.is_object() && !a.as_object().contains("$secretHmac") && !b.as_object().contains("$secretHmac")) {
        std::set<std::string> names;
        for(const auto& item : a.as_object()) names.insert(std::string(item.key()));
        for(const auto& item : b.as_object()) names.insert(std::string(item.key()));
        for(const auto& name : names) {
            auto* left = a.as_object().if_contains(name); auto* right = b.as_object().if_contains(name);
            std::string child = path + "/" + escaped(name);
            if (!left || !right) paths.push_back(child); else differences(*left, *right, child, paths);
        }
    } else if (a.is_array() && b.is_array() && a.as_array().size() == b.as_array().size()) {
        for (std::size_t i = 0; i < a.as_array().size(); ++i) differences(a.as_array()[i], b.as_array()[i], path + "/" + std::to_string(i), paths);
    } else paths.push_back(path.empty() ? "/" : path);
}
}
std::string canonicalJson(const boost::json::value& value) {
    if(value.is_object()) {
        std::vector<std::string> keys;
        for(const auto& item : value.as_object()) keys.emplace_back(item.key());
        std::sort(keys.begin(), keys.end());
        std::string result = "{";
        for (const auto& key : keys) { if(result.size() > 1) result += ','; result += boost::json::serialize(boost::json::value(key)) + ":" + canonicalJson(value.as_object().at(key)); }
        return result + "}";
    }
    if(value.is_array()) {
        std::string result = "[";
        for(const auto& child : value.as_array()) { if(result.size() > 1) result += ','; result += canonicalJson(child); }
        return result + "]";
    }
    return boost::json::serialize(value);
}
Snapshot prepareSnapshot(const boost::json::value& value, const Rules& rules,
                         const std::vector<unsigned char>& secretKey, const std::string& keyVersion) {
    if(secretKey.size() < 32 || keyVersion.empty() || rules.version.empty()) throw std::runtime_error("Audit requires a versioned key of at least 32 bytes and versioned normalization rules");
    auto normalized = normalize(value, "", rules, secretKey, keyVersion, false, 0);
    auto canonical = canonicalJson(boost::json::object{{"rulesVersion", rules.version}, {"keyVersion", keyVersion}, {"object", std::move(normalized.secured)}});
    return {digest(canonical), std::move(canonical), std::move(normalized.visible)};
}
Snapshot restoreProtectedSnapshot(const boost::json::value& envelope,const boost::json::value& visible) {
    if(!envelope.is_object() || !envelope.as_object().contains("object") || !envelope.at("rulesVersion").is_string() || !envelope.at("keyVersion").is_string())
        throw std::runtime_error("Invalid protected audit snapshot envelope");
    auto canonical=canonicalJson(envelope);
    return {digest(canonical),std::move(canonical),visible};
}
std::optional<Change> compareSnapshots(const std::optional<Snapshot>& previous, const std::optional<Snapshot>& current) {
    if(!previous && !current) return std::nullopt;
    if(!previous) return Change{"Created", {"/"}};
    if(!current) return Change{"Deleted", {"/"}};
    if(previous->protectedCanonical == current->protectedCanonical) return std::nullopt;
    auto before = boost::json::parse(previous->protectedCanonical);
    auto after = boost::json::parse(current->protectedCanonical);
    if(before.at("rulesVersion") != after.at("rulesVersion") || before.at("keyVersion") != after.at("keyVersion"))
        throw std::runtime_error("Audit normalization/key version changed; explicit rebaseline is required");
    Change result{"Updated", {}};
    differences(before.at("object"), after.at("object"), "", result.paths);
    return result;
}
}
