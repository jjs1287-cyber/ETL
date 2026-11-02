#include "ServerFiles.h"
#include <atomic>
#include <filesystem>
#include <fstream>
#include <functional>
#include <iostream>
#include <thread>
#include <windows.h>
#include <winioctl.h>
namespace fs = std::filesystem;
using etlforge::admin::pack;
using etlforge::admin::ServerFiles;
using etlforge::admin::unpackConfiguration;
int checks = 0;
void check(bool ok, const char *message) {
  if (!ok)
    throw std::runtime_error(message);
  ++checks;
}
void rejects(const std::function<void()> &action, const char *message) {
  bool failed = false;
  try {
    action();
  } catch (const std::exception &) {
    failed = true;
  }
  check(failed, message);
}
void write(const fs::path &path, const std::string &text) {
  std::ofstream output(path, std::ios::binary | std::ios::trunc);
  output << text;
  output.close();
  if (!output)
    throw std::runtime_error("Test fixture write failed.");
}
std::string read(const fs::path &path) {
  std::ifstream input(path, std::ios::binary);
  return std::string(std::istreambuf_iterator<char>(input), {});
}
int wmain(int argc, wchar_t **argv) {
  try {
    if (argc != 2)
      throw std::runtime_error("Pass a new isolated test directory.");
    fs::path root = fs::path(argv[1]) / L"admin-\u00e9\u4e2d\u6587-\U0001F680";
    check(!fs::exists(root), "Use a fresh isolated root.");
    fs::create_directories(root / L"log/plugin");
    fs::create_directories(root / L"DataInterchange/Temp/LOG");
    fs::create_directories(root / L"DataInterchange/Temp/Debug");
    std::string original = "[Test]\r\nValue = old\r\n",
                changed =
                    u8"[Test]\nValue = caf\u00e9 \u4e2d\u6587 \U0001F680\n";
    write(root / L"etlforge.conf", original);
    ServerFiles files(root.wstring());
    check(files.list("server", "", "").empty(), "Empty directory");
    check(files.configuration() == original, "Configuration bytes");
    check(!files.saveConfiguration(original), "Identical config is a no-op");
    check(!fs::exists(root / L"etlforge.conf.previous"),
          "No-op created backup");
    check(files.saveConfiguration(changed), "Changed config save");
    check(files.configuration() == changed, "UTF8 changed config");
    check(read(root / L"etlforge.conf.previous") == original,
          "Recoverable original backup");
    check(unpackConfiguration(pack(changed)) == changed,
          "Wire codec round-trip");
    files.saveConfiguration(original);
    check(files.configuration() == original, "Restore config");
    for (const auto &bad :
         std::vector<std::string>{"", "[Broken\nValue\n", "[Test]\n=x\n",
                                  "[Test]\nValue=\"unterminated\n",
                                  std::string("[Test]\nValue=\xC3\x28\n"),
                                  std::string("[Test]\nValue=a\0b\n", 18)}) {
      rejects([&] { files.saveConfiguration(bad); }, "Invalid config accepted");
      check(files.configuration() == original, "Invalid config changed file");
    }
    rejects([&] { unpackConfiguration("bad"); }, "Invalid base64");
    rejects([&] { unpackConfiguration("eA=="); }, "Invalid zlib");
    auto encoded = pack(changed);
    rejects([&] { unpackConfiguration(encoded.substr(0, encoded.size() - 4)); },
            "Truncated zlib");
    rejects([&] { unpackConfiguration(pack(changed) + "AAAA"); },
            "Trailing encoded data");
    rejects(
        [&] {
          unpackConfiguration(pack(std::string(4 * 1024 * 1024 + 1, 'x')));
        },
        "Inflation limit");
    std::string binary;
    for (int i = 0; i < 256; i++)
      binary.push_back(static_cast<char>(i));
    write(root / L"log/a.log", binary);
    write(root / L"log/empty.LOG", "");
    write(root / L"log/not-log.bin", binary);
    fs::create_directories(root / L"log/directory.log");
    check(files.list("server", "", "").size() == 2,
          "Log extension / directory filtering");
    check(files.download((root / L"log/a.log").wstring()) == binary,
          "Binary bytes preserved");
    check(files.download((root / L"log/empty.LOG").wstring()).empty(),
          "Empty file download");
    check(files.download((root / L"etlforge.conf").wstring()) == original,
          "Config download");
    write(root / L"private.txt", "private");
    rejects([&] { files.download((root / L"private.txt").wstring()); },
            "Private file allowed");
    rejects([&] { files.download((root / L"log/../etlforge.conf").wstring()); },
            "Traversal allowed");
    rejects([&] { files.download((root / L"etlforge.conf:ads").wstring()); },
            "ADS allowed");
    rejects([&] { files.download(L"\\\\server\\share\\file"); }, "UNC allowed");
    rejects([&] { files.download((root / L"log/missing.log").wstring()); },
            "Missing file allowed");
    rejects([&] { files.download((root / L"log/directory.log").wstring()); },
            "Directory allowed");
    fs::create_directories(root / L"backup");
    write(root / L"backup/a.jpk", binary);
    check(files.download((root / L"backup/a.jpk").wstring(),
                         {(root / L"backup/a.jpk").wstring()}) == binary,
          "Registered backup");
    rejects([&] { files.download((root / L"backup/a.jpk").wstring()); },
            "Unregistered backup allowed");
    auto link = root / L"log/link.log";
    check(CreateSymbolicLinkW(link.c_str(), (root / L"private.txt").c_str(),
                              SYMBOLIC_LINK_FLAG_ALLOW_UNPRIVILEGED_CREATE) !=
              0,
          "Cannot create test symlink");
    rejects([&] { files.download(link.wstring()); }, "Symlink escape allowed");
    check(files.list("server", "", "").size() == 2, "Listed symlink");
    rejects([&] { files.list("debug", "2026-02-30 00:00:00", ""); },
            "Invalid date");
    rejects(
        [&] {
          files.list("debug", "2026-01-02 00:00:00", "2026-01-01 00:00:00");
        },
        "Reversed date");
    rejects(
        [&] {
          files.list("debug", "2026-01-01 00:00:00", "2026-01-01 00:00:00");
        },
        "Equal dates");
    HANDLE lock = CreateFileW((root / L".etlforge-config.lock").c_str(),
                              GENERIC_READ | GENERIC_WRITE, 0, 0, OPEN_EXISTING,
                              FILE_ATTRIBUTE_NORMAL, 0);
    check(lock != INVALID_HANDLE_VALUE, "Test lock");
    rejects([&] { files.saveConfiguration(changed); },
            "Concurrent writer lock");
    CloseHandle(lock);
    check(files.configuration() == original, "Lock failure modified config");
    HANDLE blocked =
        CreateFileW((root / L"etlforge.conf").c_str(), GENERIC_READ,
                    FILE_SHARE_READ | FILE_SHARE_WRITE, 0, OPEN_EXISTING,
                    FILE_ATTRIBUTE_NORMAL, 0);
    check(blocked != INVALID_HANDLE_VALUE,
          "Hold config without delete sharing");
    rejects([&] { files.saveConfiguration(changed); },
            "Replacement write failure");
    CloseHandle(blocked);
    check(files.configuration() == original, "Write failure changed original");
    for (const auto &item : fs::directory_iterator(root))
      check(item.path().extension() != L".part", "Leaked partial replacement");
    std::atomic<int> failures{0};
    std::vector<std::thread> readers;
    for (int i = 0; i < 4; i++)
      readers.emplace_back([&] {
        for (int j = 0; j < 30; j++) {
          try {
            auto text = files.configuration();
            if (text != original && text != changed)
              ++failures;
          } catch (...) {
            ++failures;
          }
        }
      });
    for (int j = 0; j < 20; j++)
      files.saveConfiguration(j % 2 ? original : changed);
    for (auto &thread : readers)
      thread.join();
    check(!failures, "Concurrent readers saw partial config");
    check(files.configuration() == original, "Concurrent test restore");
    fs::path large = root / L"log/large.log";
    HANDLE largeFile = CreateFileW(large.c_str(), GENERIC_WRITE, 0, 0,
                                   CREATE_NEW, FILE_ATTRIBUTE_NORMAL, 0);
    check(largeFile != INVALID_HANDLE_VALUE, "Large fixture");
    LARGE_INTEGER length{};
    length.QuadPart = static_cast<LONGLONG>(3) * 1024 * 1024 * 1024;
    DWORD ignored = 0;
    DeviceIoControl(largeFile, FSCTL_SET_SPARSE, 0, 0, 0, 0, &ignored, 0);
    check(SetFilePointerEx(largeFile, length, 0, FILE_BEGIN) &&
              SetEndOfFile(largeFile),
          "Large sparse size");
    CloseHandle(largeFile);
    bool seen = false;
    for (const auto &item : files.list("server", "", ""))
      if (item.size == length.QuadPart)
        seen = true;
    check(seen, "64-bit locator size");
    rejects([&] { files.download(large.wstring()); },
            "Oversized file download");
    fs::remove(large);
    auto migrated = root / L"migrated-home";
    auto central = root / L"central-logs";
    fs::create_directories(migrated);
    fs::create_directories(central / L"plugin");
    write(central / L"central.log", binary);
    write(central / L"plugin/plugin.log", "plugin");
    check(CreateSymbolicLinkW((migrated / L"log").c_str(), central.c_str(),
              SYMBOLIC_LINK_FLAG_DIRECTORY | SYMBOLIC_LINK_FLAG_ALLOW_UNPRIVILEGED_CREATE) != 0,
          "Cannot create migrated log root fixture");
    ServerFiles moved(migrated.wstring());
    check(moved.list("server", "", "").size() == 1, "Migrated server log list");
    check(moved.list("plugin", "", "").size() == 1, "Migrated plugin log list");
    check(moved.download((central / L"central.log").wstring()) == binary, "Canonical log download");
    check(moved.download((migrated / L"log/central.log").wstring()) == binary, "Compatibility log alias download");
    rejects([&] { moved.download((root / L"private.txt").wstring()); }, "Migration exposed private file");
    check(CreateSymbolicLinkW((central / L"escape.log").c_str(), (root / L"private.txt").c_str(),
              SYMBOLIC_LINK_FLAG_ALLOW_UNPRIVILEGED_CREATE) != 0, "Cannot create migrated file link fixture");
    rejects([&] { moved.download((central / L"escape.log").wstring()); }, "Migrated file symlink allowed");
    rejects([&] { moved.download((migrated / L"log/escape.log").wstring()); }, "Alias file symlink allowed");
    check(moved.list("server", "", "").size() == 1, "Migrated list included file symlink");
    std::cout << "PASS: " << checks
              << " isolated native server administration checks.\n";
    return 0;
  } catch (const std::exception &error) {
    std::cerr << "FAIL: " << error.what() << "\n";
    return 1;
  }
}
