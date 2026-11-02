#include "../../src/native/integration/konga/FileCleanup/OperationTempRetention.h"
#include "../../src/native/integration/konga/FileCleanup/OperationHistoryRetentionSql.h"
#include <fstream>
#include <iostream>

static int checks;
static void check(bool pass, const char* message) { if (!pass) throw std::runtime_error(message); ++checks; }
static void file(const std::wstring& path) {
    HANDLE h = CreateFileW(path.c_str(), GENERIC_WRITE, FILE_SHARE_READ, 0, CREATE_NEW, 0, 0);
    check(h != INVALID_HANDLE_VALUE, "create fixture");
    const char bytes[] = "owned UTF-8 \xc3\xa9 \xe4\xb8\xad\xe6\x96\x87"; DWORD n;
    WriteFile(h, bytes, sizeof(bytes), &n, 0); CloseHandle(h);
}
static void age(const std::wstring& path, int days) {
    FILETIME now; GetSystemTimeAsFileTime(&now); ULARGE_INTEGER n;
    n.QuadPart = etlforge::cleanupTime(now) - static_cast<unsigned long long>(days) * 864000000000ULL;
    FILETIME before = {n.LowPart, n.HighPart};
    HANDLE h = CreateFileW(path.c_str(), FILE_WRITE_ATTRIBUTES, FILE_SHARE_READ | FILE_SHARE_WRITE | FILE_SHARE_DELETE, 0, OPEN_EXISTING, FILE_FLAG_BACKUP_SEMANTICS, 0);
    check(h != INVALID_HANDLE_VALUE && SetFileTime(h, 0, 0, &before), "age fixture"); CloseHandle(h);
}
int main(int argc, char** argv) {
    try {
        if (argc != 2) throw std::runtime_error("Specify new isolated root");
        std::wstring root = etlforge::cleanupWidePath(argv[1]);
        check(CreateDirectoryW(root.c_str(), 0) != 0, "new root required");
        const std::wstring guid = L"12345678-1234-1234-1234-123456789abc";
        const auto old = root + L"\\OpId_42_" + guid;
        check(CreateDirectoryW(old.c_str(), 0) != 0, "create old operation");
        file(old + L"\\\u4e2d\u6587-\xd83d\xde80.tmp"); age(old + L"\\\u4e2d\u6587-\xd83d\xde80.tmp", 8); age(old, 8);
        const auto active = root + L"\\OpId_43_" + guid;
        check(CreateDirectoryW(active.c_str(), 0) != 0, "create active operation"); file(active + L"\\live.tmp"); age(active + L"\\live.tmp", 8); age(active, 8);
        const auto fresh = root + L"\\OpId_44_" + guid;
        check(CreateDirectoryW(fresh.c_str(), 0) != 0, "create fresh operation"); file(fresh + L"\\fresh.tmp"); age(fresh, 8);
        file(root + L"\\unrelated.txt"); age(root + L"\\unrelated.txt", 8);
        size_t failures = 0;
        check(etlforge::cleanupExpiredOperationFiles(root, 7, true, [](long id) { return id == 43; }, failures) == 1 && failures == 0, "expire only inactive old tree");
        check(GetFileAttributesW(old.c_str()) == INVALID_FILE_ATTRIBUTES, "old tree removed");
        check(GetFileAttributesW((active + L"\\live.tmp").c_str()) != INVALID_FILE_ATTRIBUTES, "active tree preserved");
        check(GetFileAttributesW((fresh + L"\\fresh.tmp").c_str()) != INVALID_FILE_ATTRIBUTES, "recent child preserved");
        file(root + L"\\42_" + guid + L"_0.txt"); age(root + L"\\42_" + guid + L"_0.txt", 8);
        file(root + L"\\43_" + guid + L"_0.txt"); age(root + L"\\43_" + guid + L"_0.txt", 8);
        file(root + L"\\44_" + guid + L"_0.txt");
        check(etlforge::cleanupExpiredOperationFiles(root, 7, false, [](long id) { return id == 43; }, failures) == 1, "expire old owned log only");
        check(GetFileAttributesW((root + L"\\unrelated.txt").c_str()) != INVALID_FILE_ATTRIBUTES, "unrelated file preserved");
        check(!etlforge::cleanupPlainPath(root + L"\\.."), "reject traversal");
        check(!etlforge::cleanupPlainPath(root + L":stream"), "reject alternate stream");
        check(!etlforge::cleanupPlainPath(L"\\\\localhost\\share"), "reject UNC");
        check(!etlforge::cleanupOwnedOperationDirectory(root), "refuse shared root removal");
        HANDLE held = CreateFileW((active + L"\\live.tmp").c_str(), GENERIC_READ, FILE_SHARE_READ, 0, OPEN_EXISTING, 0, 0);
        check(held != INVALID_HANDLE_VALUE, "hold temp file");
        check(!etlforge::cleanupOwnedOperationDirectory(active), "locked file reports cleanup failure"); CloseHandle(held);
        check(etlforge::cleanupOwnedOperationDirectory(active), "retry after unlocking succeeds");
        check(etlforge::cleanupOwnedOperationDirectory(fresh), "success removes young working files");
        const auto nested = root + L"\\OpId_-1_" + guid;
        check(CreateDirectoryW(nested.c_str(), 0) != 0, "create nested script context"); file(nested + L"\\nested.tmp");
        check(etlforge::cleanupOwnedOperationDirectory(nested), "nested script context cleaned");
        const auto link = root + L"\\OpId_45_" + guid;
        check(CreateSymbolicLinkW(link.c_str(), root.c_str(), SYMBOLIC_LINK_FLAG_DIRECTORY | SYMBOLIC_LINK_FLAG_ALLOW_UNPRIVILEGED_CREATE) != 0, "create reparse fixture");
        check(!etlforge::cleanupOwnedOperationDirectory(link), "refuse reparse tree"); RemoveDirectoryW(link.c_str());
        std::ofstream sql("C:/ETL/Reports/operation-history-retention-statement.sql", std::ios::binary);
        sql << etlforge::operationHistoryRetentionSql(30) << ";\n"; sql.close();
        std::ofstream archives("C:/ETL/Reports/archive-history-retention-statement.sql", std::ios::binary);
        archives << etlforge::archiveHistoryRetentionSql(30) << ";\n";
        archives.close();
        check(etlforge::archiveHistoryRetentionSql(0) == "SELECT 0", "invalid archive retention disabled");
        check(etlforge::operationHistoryRetentionSql(0) == "SELECT 0", "invalid history retention disabled");
        std::cout << "PASS: " << checks << " native operation cleanup checks.\n";
        return 0;
    } catch (const std::exception& ex) { std::cerr << "FAIL: " << ex.what() << "\n"; return 1; }
}
