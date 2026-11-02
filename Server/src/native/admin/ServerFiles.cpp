#include "ServerFiles.h"
#include <algorithm>
#include <filesystem>
#include <memory>
#include <mutex>
#include <objbase.h>
#include <shared_mutex>
#include <sstream>
#include <stdexcept>
#include <wincrypt.h>
#include <windows.h>
#include <zlib.h>

namespace fs = std::filesystem;
namespace etlforge {
namespace admin {
namespace {
std::shared_mutex configurationMutex;
constexpr size_t maxDownload = 64 * 1024 * 1024,
                 maxConfiguration = 4 * 1024 * 1024, maxEntries = 100000;
struct Handle {
  HANDLE value = INVALID_HANDLE_VALUE;
  explicit Handle(HANDLE v) : value(v) {}
  ~Handle() {
    if (value != INVALID_HANDLE_VALUE)
      CloseHandle(value);
  }
  Handle(const Handle &) = delete;
};
void require(bool ok, const char *message) {
  if (!ok)
    throw std::runtime_error(message);
}
std::wstring lower(std::wstring value) {
  std::transform(value.begin(), value.end(), value.begin(),
                 [](wchar_t c) { return towlower(c); });
  return value;
}
fs::path clean(const fs::path &path) {
  auto value = path.wstring();
  require(value.size() > 2 && value[1] == L':' && path.is_absolute(),
          "A local absolute server path is required.");
  require(value.find(L':', 2) == std::wstring::npos &&
              value.find(L'\0') == std::wstring::npos,
          "Device paths and alternate data streams are not permitted.");
  for (const auto &part : path) {
    auto name = part.wstring();
    require(name != L".." && name != L".",
            "Relative traversal is not permitted.");
    require(name.empty() || (name.back() != L' ' && name.back() != L'.'),
            "Ambiguous Windows paths are not permitted.");
  }
  return path.lexically_normal();
}
bool same(const fs::path &a, const fs::path &b) {
  return lower(a.wstring()) == lower(b.wstring());
}
bool beneath(const fs::path &path, const fs::path &root) {
  auto p = lower(path.wstring()), r = lower(root.wstring());
  if (!r.empty() && r.back() != L'\\')
    r += L'\\';
  return p.size() > r.size() && p.compare(0, r.size(), r) == 0;
}
void noReparse(const fs::path &path) {
  fs::path current;
  for (const auto &part : path) {
    current /= part;
    if (!current.has_relative_path())
      continue;
    DWORD attributes = GetFileAttributesW(current.c_str());
    require(attributes != INVALID_FILE_ATTRIBUTES,
            "The server path does not exist or cannot be read.");
    require(!(attributes & FILE_ATTRIBUTE_REPARSE_POINT),
            "Links and junctions are not permitted for server administration "
            "files.");
  }
}
fs::path finalPath(HANDLE file) {
  std::wstring name(32768, 0);
  DWORD size =
      GetFinalPathNameByHandleW(file, &name[0], static_cast<DWORD>(name.size()),
                                FILE_NAME_NORMALIZED | VOLUME_NAME_DOS);
  require(size && size < name.size(), "Cannot resolve the opened server file.");
  name.resize(size);
  if (name.compare(0, 4, L"\\\\?\\") == 0)
    name.erase(0, 4);
  return clean(fs::path(name));
}
std::string read(const fs::path &path, size_t maximum) {
  noReparse(path);
  Handle file(
      CreateFileW(path.c_str(), GENERIC_READ,
                  FILE_SHARE_READ | FILE_SHARE_WRITE | FILE_SHARE_DELETE, 0,
                  OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, 0));
  require(file.value != INVALID_HANDLE_VALUE,
          "The server file cannot be read.");
  require(same(finalPath(file.value), path),
          "The opened server file resolved outside its approved path.");
  BY_HANDLE_FILE_INFORMATION info{};
  require(GetFileInformationByHandle(file.value, &info) &&
              !(info.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY),
          "The server path is not a regular file.");
  LARGE_INTEGER size{};
  require(GetFileSizeEx(file.value, &size) && size.QuadPart >= 0 &&
              size.QuadPart <= maximum,
          "Server file exceeds the supported download size.");
  std::string result(static_cast<size_t>(size.QuadPart), 0);
  size_t position = 0;
  while (position < result.size()) {
    DWORD count = 0;
    require(ReadFile(file.value, &result[position],
                     static_cast<DWORD>(
                         std::min<size_t>(result.size() - position, 65536)),
                     &count, 0) &&
                count,
            "Cannot complete server file download.");
    position += count;
  }
  return result;
}
std::string trim(const std::string &value) {
  auto a = value.find_first_not_of(" \t\r"),
       b = value.find_last_not_of(" \t\r");
  return a == std::string::npos ? "" : value.substr(a, b - a + 1);
}
void validateConfiguration(const std::string &bytes) {
  require(bytes.size() <= maxConfiguration && !bytes.empty(),
          "Configuration must contain between 1 byte and 4 MiB.");
  wide(bytes);
  require(bytes.find('\0') == std::string::npos,
          "Configuration must not contain NUL bytes.");
  std::string text = bytes;
  if (text.compare(0, 3, "\xEF\xBB\xBF") == 0)
    text.erase(0, 3);
  std::istringstream lines(text);
  std::string line;
  bool section = false, entry = false;
  while (std::getline(lines, line)) {
    line = trim(line);
    if (line.empty() || line[0] == '#' || line[0] == ';')
      continue;
    if (line[0] == '[') {
      require(line.size() > 2 && line.back() == ']',
              "Configuration contains an invalid section header.");
      section = true;
      continue;
    }
    auto equal = line.find('=');
    require(equal != std::string::npos && !trim(line.substr(0, equal)).empty(),
            "Configuration contains an invalid key/value line.");
    bool single = false, doubled = false, escape = false;
    for (char c : line.substr(equal + 1)) {
      if (escape) {
        escape = false;
        continue;
      }
      if (c == '\\') {
        escape = true;
        continue;
      }
      if (c == '#' && !single && !doubled)
        break;
      if (c == '\'' && !doubled)
        single = !single;
      if (c == '"' && !single)
        doubled = !doubled;
    }
    require(!single && !doubled,
            "Configuration contains an unterminated quoted value.");
    entry = true;
  }
  require(section && entry,
          "Configuration must contain sections and settings.");
}
std::uint64_t ticks(const FILETIME &value) {
  ULARGE_INTEGER n{};
  n.LowPart = value.dwLowDateTime;
  n.HighPart = value.dwHighDateTime;
  return n.QuadPart;
}
std::uint64_t parseDate(const std::string &text) {
  if (text.empty())
    return 0;
  SYSTEMTIME date{};
  int year, month, day, hour, minute, second;
  char extra;
  require(text.size() == 19 &&
              sscanf(text.c_str(), "%4d-%2d-%2d %2d:%2d:%2d%c", &year, &month,
                     &day, &hour, &minute, &second, &extra) == 6,
          "Dates must use UTC yyyy-MM-dd HH:mm:ss.");
  date.wYear = static_cast<WORD>(year);
  date.wMonth = static_cast<WORD>(month);
  date.wDay = static_cast<WORD>(day);
  date.wHour = static_cast<WORD>(hour);
  date.wMinute = static_cast<WORD>(minute);
  date.wSecond = static_cast<WORD>(second);
  FILETIME file{};
  require(year >= 1601 && SystemTimeToFileTime(&date, &file),
          "Date is outside the supported UTC range.");
  SYSTEMTIME round{};
  FileTimeToSystemTime(&file, &round);
  char normalized[32];
  sprintf_s(normalized, "%04u-%02u-%02u %02u:%02u:%02u", round.wYear,
            round.wMonth, round.wDay, round.wHour, round.wMinute,
            round.wSecond);
  require(text == normalized, "Date is not a valid UTC date.");
  return ticks(file);
}
std::string dateText(const FILETIME &file) {
  SYSTEMTIME date{};
  require(FileTimeToSystemTime(&file, &date),
          "Cannot format server file timestamp.");
  char result[32];
  sprintf_s(result, "%04u-%02u-%02u %02u:%02u:%02u", date.wYear, date.wMonth,
            date.wDay, date.wHour, date.wMinute, date.wSecond);
  return result;
}
void writeNew(const fs::path &path, const std::string &bytes) {
  Handle file(CreateFileW(path.c_str(), GENERIC_WRITE, 0, 0, CREATE_NEW,
                          FILE_ATTRIBUTE_NORMAL, 0));
  require(file.value != INVALID_HANDLE_VALUE,
          "Cannot create configuration replacement file.");
  size_t position = 0;
  while (position < bytes.size()) {
    DWORD count = 0;
    require(WriteFile(file.value, bytes.data() + position,
                      static_cast<DWORD>(
                          std::min<size_t>(bytes.size() - position, 65536)),
                      &count, 0) &&
                count,
            "Cannot write configuration replacement file.");
    position += count;
  }
  require(FlushFileBuffers(file.value),
          "Cannot flush configuration replacement file.");
}
} // namespace
std::wstring wide(const std::string &value) {
  require(value.size() <= INT_MAX, "UTF-8 value is too long.");
  int size = MultiByteToWideChar(CP_UTF8, MB_ERR_INVALID_CHARS, value.data(),
                                 static_cast<int>(value.size()), 0, 0);
  require(size || value.empty(), "Text is not valid UTF-8.");
  std::wstring result(size, 0);
  if (size)
    MultiByteToWideChar(CP_UTF8, MB_ERR_INVALID_CHARS, value.data(),
                        static_cast<int>(value.size()), &result[0], size);
  return result;
}
std::string utf8(const std::wstring &value) {
  int size = WideCharToMultiByte(CP_UTF8, WC_ERR_INVALID_CHARS, value.data(),
                                 static_cast<int>(value.size()), 0, 0, 0, 0);
  require(size || value.empty(), "Path is not valid Unicode.");
  std::string result(size, 0);
  if (size)
    WideCharToMultiByte(CP_UTF8, WC_ERR_INVALID_CHARS, value.data(),
                        static_cast<int>(value.size()), &result[0], size, 0, 0);
  return result;
}
std::string pack(const std::string &bytes) {
  require(bytes.size() <= maxDownload,
          "Server file exceeds 64 MiB download limit.");
  uLongf size = compressBound(static_cast<uLong>(bytes.size()));
  std::string compressed(size, 0);
  require(compress2(reinterpret_cast<Bytef *>(&compressed[0]), &size,
                    reinterpret_cast<const Bytef *>(bytes.data()),
                    static_cast<uLong>(bytes.size()), 9) == Z_OK,
          "Cannot compress server file.");
  compressed.resize(size);
  DWORD encodedSize = 0;
  require(CryptBinaryToStringA(
              reinterpret_cast<const BYTE *>(compressed.data()),
              static_cast<DWORD>(compressed.size()),
              CRYPT_STRING_BASE64 | CRYPT_STRING_NOCRLF, 0, &encodedSize),
          "Cannot encode server file.");
  std::string encoded(encodedSize, 0);
  require(
      CryptBinaryToStringA(reinterpret_cast<const BYTE *>(compressed.data()),
                           static_cast<DWORD>(compressed.size()),
                           CRYPT_STRING_BASE64 | CRYPT_STRING_NOCRLF,
                           &encoded[0], &encodedSize),
      "Cannot encode server file.");
  encoded.resize(encodedSize);
  return encoded;
}
std::string unpackConfiguration(const std::string &wire) {
  require(wire.size() <= 8 * 1024 * 1024, "Oversized encoded configuration.");
  std::string encoded;
  encoded.reserve(wire.size());
  for (char c : wire)
    if (c != ' ' && c != '\t' && c != '\r' && c != '\n')
      encoded.push_back(c);
  require(!encoded.empty() && encoded.size() <= 8 * 1024 * 1024 &&
              encoded.size() % 4 == 0,
          "Invalid or oversized encoded configuration.");
  size_t padding = 0;
  for (size_t i = 0; i < encoded.size(); ++i) {
    char c = encoded[i];
    if (c == '=') {
      ++padding;
      require(i >= encoded.size() - 2 && padding <= 2,
              "Invalid base64 padding.");
    } else
      require(!padding && ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||
                           (c >= '0' && c <= '9') || c == '+' || c == '/'),
              "Configuration is not strict base64.");
  }
  DWORD count = 0;
  require(CryptStringToBinaryA(encoded.data(),
                               static_cast<DWORD>(encoded.size()),
                               CRYPT_STRING_BASE64, 0, &count, 0, 0),
          "Invalid base64 configuration.");
  std::string compressed(count, 0);
  require(CryptStringToBinaryA(
              encoded.data(), static_cast<DWORD>(encoded.size()),
              CRYPT_STRING_BASE64, reinterpret_cast<BYTE *>(&compressed[0]),
              &count, 0, 0),
          "Invalid base64 configuration.");
  compressed.resize(count);
  z_stream stream{};
  require(inflateInit(&stream) == Z_OK,
          "Cannot initialize configuration decompression.");
  struct End {
    z_stream *s;
    ~End() { inflateEnd(s); }
  } end{&stream};
  stream.next_in = reinterpret_cast<Bytef *>(&compressed[0]);
  stream.avail_in = static_cast<uInt>(compressed.size());
  std::string bytes;
  int result = Z_OK;
  while (result == Z_OK) {
    char block[65536];
    stream.next_out = reinterpret_cast<Bytef *>(block);
    stream.avail_out = sizeof(block);
    result = inflate(&stream, Z_NO_FLUSH);
    size_t produced = sizeof(block) - stream.avail_out;
    require(bytes.size() + produced <= maxConfiguration,
            "Inflated configuration exceeds 4 MiB.");
    bytes.append(block, produced);
  }
  require(result == Z_STREAM_END && stream.avail_in == 0,
          "Configuration is a truncated, trailing or invalid zlib stream.");
  validateConfiguration(bytes);
  return bytes;
}
ServerFiles::ServerFiles(const std::wstring &root,
                         const std::wstring &debugDirectory)
    : home(clean(fs::path(root)).wstring()),
      debug(debugDirectory.empty()
                ? (fs::path(home) / L"DataInterchange/Temp/Debug").wstring()
                : clean(fs::path(debugDirectory)).wstring()) {
  const auto configured = clean(fs::path(home) / L"log");
  std::error_code error;
  const auto resolved = fs::canonical(configured, error);
  if (error) {
    require(error == std::errc::no_such_file_or_directory,
            "Cannot resolve the configured server log directory.");
    logs = configured.wstring();
  } else {
    logs = clean(resolved).wstring();
    noReparse(fs::path(logs));
  }
}
std::string ServerFiles::configuration() const {
  std::shared_lock<std::shared_mutex> guard(configurationMutex);
  return read(fs::path(home) / L"etlforge.conf", maxConfiguration);
}
bool ServerFiles::isStandardDownload(const std::wstring &requested) const {
  auto path = clean(fs::path(requested));
  if (same(path, fs::path(home) / L"etlforge.conf"))
    return true;
  for (const auto &root :
       {fs::path(home) / L"log", fs::path(logs), fs::path(home) / L"DataInterchange/Temp/LOG",
        fs::path(debug)})
    if (beneath(path, clean(root)))
      return true;
  return false;
}
std::string
ServerFiles::download(const std::wstring &requested,
                      const std::vector<std::wstring> &backups) const {
  auto path = clean(fs::path(requested));
  if (same(path, fs::path(home) / L"etlforge.conf"))
    return configuration();
  bool allowed = isStandardDownload(requested);
  for (const auto &backup : backups)
    allowed = allowed || same(path, clean(fs::path(backup)));
  require(allowed, "Download is outside approved server log, configuration and "
                   "registered backup locations.");
  const auto alias = clean(fs::path(home) / L"log");
  if (beneath(path, alias))
    path = clean(fs::path(logs) / path.lexically_relative(alias));
  return read(path, maxDownload);
}
std::vector<FileLocator> ServerFiles::list(const std::string &kind,
                                           const std::string &start,
                                           const std::string &end) const {
  bool logOnly = kind == "server" || kind == "plugin";
  fs::path root = kind == "server"   ? fs::path(logs)
                  : kind == "plugin" ? fs::path(logs) / L"plugin"
                  : kind == "operation"
                      ? fs::path(home) / L"DataInterchange/Temp/LOG"
                  : kind == "debug" ? fs::path(debug)
                                    : fs::path();
  require(!root.empty(), "Unknown server log category.");
  root = clean(root);
  auto first = parseDate(start), last = parseDate(end);
  require(!first || !last || first < last,
          "Start date must be before end date.");
  DWORD attributes = GetFileAttributesW(root.c_str());
  if (attributes == INVALID_FILE_ATTRIBUTES) {
    require(GetLastError() == ERROR_FILE_NOT_FOUND ||
                GetLastError() == ERROR_PATH_NOT_FOUND,
            "Cannot access server log directory.");
    return {};
  }
  noReparse(root);
  require(attributes & FILE_ATTRIBUTE_DIRECTORY,
          "Server log location is not a directory.");
  WIN32_FIND_DATAW entry{};
  HANDLE search = FindFirstFileW((root / L"*").c_str(), &entry);
  if (search == INVALID_HANDLE_VALUE) {
    require(GetLastError() == ERROR_FILE_NOT_FOUND,
            "Cannot list server log directory.");
    return {};
  }
  struct End {
    HANDLE h;
    ~End() { FindClose(h); }
  } closer{search};
  std::vector<FileLocator> files;
  do {
    if (entry.dwFileAttributes &
        (FILE_ATTRIBUTE_DIRECTORY | FILE_ATTRIBUTE_REPARSE_POINT))
      continue;
    std::wstring name(entry.cFileName);
    auto extension = lower(fs::path(name).extension().wstring());
    if (logOnly && extension != L".log")
      continue;
    auto modified = ticks(entry.ftLastWriteTime);
    if ((first && modified < first) || (last && modified > last))
      continue;
    require(files.size() < maxEntries,
            "Too many server log files; narrow the date range.");
    ULARGE_INTEGER size{};
    size.LowPart = entry.nFileSizeLow;
    size.HighPart = entry.nFileSizeHigh;
    files.push_back({utf8(root / name), dateText(entry.ftLastWriteTime),
                     static_cast<std::int64_t>(size.QuadPart)});
  } while (FindNextFileW(search, &entry));
  require(GetLastError() == ERROR_NO_MORE_FILES,
          "Server log enumeration failed.");
  std::sort(files.begin(), files.end(),
            [](const FileLocator &a, const FileLocator &b) {
              return a.path < b.path;
            });
  return files;
}
bool ServerFiles::saveConfiguration(const std::string &bytes) const {
  validateConfiguration(bytes);
  std::unique_lock<std::shared_mutex> guard(configurationMutex);
  auto target = clean(fs::path(home) / L"etlforge.conf");
  noReparse(target);
  Handle lock(CreateFileW((fs::path(home) / L".etlforge-config.lock").c_str(),
                          GENERIC_READ | GENERIC_WRITE, 0, 0, OPEN_ALWAYS,
                          FILE_ATTRIBUTE_HIDDEN | FILE_FLAG_OPEN_REPARSE_POINT,
                          0));
  require(lock.value != INVALID_HANDLE_VALUE,
          "Another administrator is saving the configuration; reload and try "
          "again.");
  BY_HANDLE_FILE_INFORMATION info{};
  require(GetFileInformationByHandle(lock.value, &info) &&
              !(info.dwFileAttributes & FILE_ATTRIBUTE_REPARSE_POINT),
          "Unsafe configuration lock file.");
  auto original = read(target, maxConfiguration);
  if (original == bytes)
    return false;
  GUID guid{};
  require(SUCCEEDED(CoCreateGuid(&guid)),
          "Cannot create configuration update identifier.");
  wchar_t id[40];
  StringFromGUID2(guid, id, 40);
  auto temporary = target;
  temporary += L"." + std::wstring(id) + L".part";
  auto backup = target;
  backup += L".previous";
  try {
    writeNew(temporary, bytes);
    if (GetFileAttributesW(backup.c_str()) != INVALID_FILE_ATTRIBUTES)
      noReparse(backup);
    require(read(target, maxConfiguration) == original,
            "Configuration changed while saving; reload and try again.");
    require(ReplaceFileW(target.c_str(), temporary.c_str(), backup.c_str(), 0,
                         0, 0),
            "Cannot replace configuration atomically; original file remains "
            "available.");
  } catch (...) {
    DeleteFileW(temporary.c_str());
    throw;
  }
  return true;
}
} // namespace admin
} // namespace etlforge
