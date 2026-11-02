#include "../../src/native/ssh/SshKeys.h"

#include <filesystem>
#include <fstream>
#include <iostream>
#include <stdexcept>
#include <string>

namespace fs = std::filesystem;

namespace {
int checks = 0;

void check(bool condition, const char* label) {
    if (!condition) throw std::runtime_error(label);
    std::cout << "PASS " << label << '\n';
    ++checks;
}

std::string readFile(const fs::path& path) {
    std::ifstream input(path, std::ios::binary);
    return {std::istreambuf_iterator<char>(input), {}};
}
}

int main(int argc, char** argv) {
    try {
        if (argc != 4) throw std::runtime_error("Expected fixture root, private key, public key");
        const fs::path root = argv[1];
        const std::string publicKey = readFile(argv[3]);

        check(!etlforge::ssh::fingerprint(publicKey).empty(), "public key fingerprint generated");
        check(etlforge::ssh::validateHostFingerprint(
                  "SHA256:AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA").size() == 43,
              "host SHA256 accepted");

        bool rejected = false;
        try { etlforge::ssh::validateHostFingerprint(""); }
        catch (...) { rejected = true; }
        check(rejected, "missing host fingerprint rejected");

        rejected = false;
        try { etlforge::ssh::fingerprint("not a public key"); }
        catch (...) { rejected = true; }
        check(rejected, "invalid public key rejected");

        fs::create_directories(root);
        const fs::path orphan = root / "orphan.pem";
        {
            std::ofstream output(orphan, std::ios::binary);
            output << "temporary private material";
        }
        etlforge::ssh::cleanup(root.string());
        check(!fs::exists(orphan), "orphan transient key removed after crash");

        std::cout << checks << " checks passed\n";
        return 0;
    } catch (const std::exception& error) {
        std::cerr << error.what() << '\n';
        return 1;
    }
}
