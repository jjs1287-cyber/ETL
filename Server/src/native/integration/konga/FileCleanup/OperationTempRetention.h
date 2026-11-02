#pragma once
#include <windows.h>
#include <functional>
#include <regex>
#include <string>
#include <vector>
#include <stdexcept>

namespace etlforge {
inline std::wstring cleanupWidePath(const std::string& path) {
    int n = MultiByteToWideChar(CP_UTF8, MB_ERR_INVALID_CHARS, path.data(), static_cast<int>(path.size()), 0, 0);
    if (!n || path.find('\0') != std::string::npos) throw std::runtime_error("Invalid UTF-8 cleanup path");
    std::wstring result(n, L'\0');
    MultiByteToWideChar(CP_UTF8, MB_ERR_INVALID_CHARS, path.data(), static_cast<int>(path.size()), &result[0], n);
    return result;
}
inline bool cleanupPlainPath(const std::wstring& path) {
    if (path.size() < 4 || path[1] != L':' || (path[2] != L'\\' && path[2] != L'/')) return false;
    if (path.find(L':', 2) != std::wstring::npos) return false;
    size_t component = 3;
    for (size_t i = 3; i <= path.size(); ++i) {
        if (i != path.size() && path[i] != L'\\' && path[i] != L'/') continue;
        std::wstring part = path.substr(component, i - component);
        if (part == L"." || part == L".." || (!part.empty() && (part.back() == L'.' || part.back() == L' '))) return false;
        component = i + 1;
        DWORD a = GetFileAttributesW(path.substr(0, i).c_str());
        if (a == INVALID_FILE_ATTRIBUTES || (a & FILE_ATTRIBUTE_REPARSE_POINT)) return false;
    }
    return true;
}
inline bool cleanupDeleteFile(const std::wstring& path) {
    if (!cleanupPlainPath(path)) return GetFileAttributesW(path.c_str()) == INVALID_FILE_ATTRIBUTES && GetLastError() == ERROR_FILE_NOT_FOUND;
    for (int i = 0; i < 5; ++i) {
        if (DeleteFileW(path.c_str())) return true;
        DWORD error = GetLastError();
        if (error == ERROR_FILE_NOT_FOUND || error == ERROR_PATH_NOT_FOUND) return true;
        if (error != ERROR_SHARING_VIOLATION && error != ERROR_ACCESS_DENIED) return false;
        if (i < 4) Sleep(100);
    }
    return false;
}
inline unsigned long long cleanupTime(const FILETIME& time) {
    ULARGE_INTEGER n; n.LowPart = time.dwLowDateTime; n.HighPart = time.dwHighDateTime; return n.QuadPart;
}
inline bool cleanupCollectOld(const std::wstring& path, unsigned long long cutoff,
                              std::vector<std::wstring>& files, std::vector<std::wstring>& dirs, unsigned depth = 0) {
    if (depth > 64 || files.size() + dirs.size() > 100000 || !cleanupPlainPath(path)) return false;
    WIN32_FILE_ATTRIBUTE_DATA info;
    if (!GetFileAttributesExW(path.c_str(), GetFileExInfoStandard, &info) || cleanupTime(info.ftLastWriteTime) >= cutoff) return false;
    if (!(info.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY)) { files.push_back(path); return true; }
    WIN32_FIND_DATAW item;
    HANDLE find = FindFirstFileW((path + L"\\*").c_str(), &item);
    if (find == INVALID_HANDLE_VALUE) return false;
    bool valid = true;
    do {
        if (!wcscmp(item.cFileName, L".") || !wcscmp(item.cFileName, L"..")) continue;
        if (!cleanupCollectOld(path + L"\\" + item.cFileName, cutoff, files, dirs, depth + 1)) { valid = false; break; }
    } while (FindNextFileW(find, &item));
    if (valid && GetLastError() != ERROR_NO_MORE_FILES) valid = false;
    FindClose(find);
    if (valid) dirs.push_back(path);
    return valid;
}
inline size_t cleanupExpiredOperationFiles(const std::wstring& root, int days, bool workingDirectories,
                                          const std::function<bool(long)>& active, size_t& failures) {
    if (days < 1 || !cleanupPlainPath(root)) return 0;
    FILETIME now; GetSystemTimeAsFileTime(&now);
    const auto cutoff = cleanupTime(now) - static_cast<unsigned long long>(days) * 864000000000ULL;
    const std::wstring guid = L"[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
    const std::wregex pattern(workingDirectories ? L"(?:OpId_|op_)(-?[0-9]+)_" + guid :
        L"(?:op_|RunScript_)?([0-9]+)_(?:" + guid + L"|[0-9a-fA-F]{8})(?:_[0-9]+)?\\.(?:txt|log|prf\\.xml)");
    WIN32_FIND_DATAW item; HANDLE find = FindFirstFileW((root + L"\\*").c_str(), &item);
    if (find == INVALID_HANDLE_VALUE) return 0;
    size_t removed = 0;
    try {
        do {
            std::wcmatch match;
            if (!std::regex_match(item.cFileName, match, pattern)) continue;
            if (workingDirectories != !!(item.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY)) continue;
            long id = std::stol(match[1].str());
            std::vector<std::wstring> files, dirs;
            if (active(id) || !cleanupCollectOld(root + L"\\" + item.cFileName, cutoff, files, dirs) || active(id)) continue;
            bool okay = true;
            for (const auto& file : files) if (!cleanupDeleteFile(file)) { ++failures; okay = false; }
            for (const auto& dir : dirs) if (!RemoveDirectoryW(dir.c_str())) { ++failures; okay = false; }
            if (okay) ++removed;
        } while (FindNextFileW(find, &item));
    } catch (...) { FindClose(find); throw; }
    FindClose(find);
    return removed;
}
inline bool cleanupOwnedOperationDirectory(std::wstring path) {
    while (!path.empty() && (path.back() == L'/' || path.back() == L'\\')) path.pop_back();
    const auto name = path.substr(path.find_last_of(L"/\\") + 1);
    if (!std::regex_match(name, std::wregex(L"(?:OpId_|op_)-?[0-9]+_[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"))) return false;
    if (GetFileAttributesW(path.c_str()) == INVALID_FILE_ATTRIBUTES && GetLastError() == ERROR_FILE_NOT_FOUND) return true;
    std::vector<std::wstring> files, dirs;
    if (!cleanupCollectOld(path, ~0ULL, files, dirs)) return false;
    bool okay = true;
    for (const auto& file : files) if (!cleanupDeleteFile(file)) okay = false;
    for (const auto& dir : dirs) if (!RemoveDirectoryW(dir.c_str())) okay = false;
    return okay;
}
}
