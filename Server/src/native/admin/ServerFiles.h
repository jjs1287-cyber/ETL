#pragma once
#include <cstdint>
#include <string>
#include <vector>

namespace etlforge {
namespace admin {
struct FileLocator {
  std::string path, modified;
  std::int64_t size;
};
std::wstring wide(const std::string &value);
std::string utf8(const std::wstring &value);
std::string pack(const std::string &bytes);
std::string unpackConfiguration(const std::string &encoded);
class ServerFiles {
  std::wstring home, debug, logs;

public:
  explicit ServerFiles(const std::wstring &home,
                       const std::wstring &debug = L"");
  std::vector<FileLocator> list(const std::string &kind,
                                const std::string &start,
                                const std::string &end) const;
  std::string configuration() const;
  bool saveConfiguration(const std::string &bytes) const;
  bool isStandardDownload(const std::wstring &path) const;
  std::string
  download(const std::wstring &path,
           const std::vector<std::wstring> &approvedBackups = {}) const;
};
} // namespace admin
} // namespace etlforge
