#pragma once
#include <memory>
#include <string>
namespace etlforge { namespace ssh {
struct KeyLease {
    std::string privateFile, publicFile, passphrase, fingerprint;
    std::shared_ptr<void> lifetime;
    ~KeyLease();
};
std::shared_ptr<KeyLease> lease(void* database, const std::string& root, const std::string& vaultKey, const std::string& id, const std::string& type="ssh");
std::string request(void* database, const std::string& root, const std::string& vaultKey, const std::string& json, bool administrator);
std::string fingerprint(const std::string& publicKey);
std::string validateHostFingerprint(const std::string& input);
std::string pgpRuntime(const std::string& root);
void cleanup(const std::string& root);
}}
