#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#include <winsvc.h>
#include <algorithm>
#include <chrono>
#include <filesystem>
#include <fstream>
#include <iomanip>
#include <map>
#include <sstream>
#include <string>
#include <vector>

namespace fs = std::filesystem;

namespace {
HANDLE stopEvent = nullptr;
SERVICE_STATUS_HANDLE statusHandle = nullptr;
SERVICE_STATUS status{};
std::wstring serviceName = L"ETLForge.Dev.Watchdog";
fs::path configuration;
fs::path logPath;

struct Handle {
    HANDLE value = nullptr;
    explicit Handle(HANDLE input = nullptr) : value(input) {}
    ~Handle() { if (value && value != INVALID_HANDLE_VALUE) CloseHandle(value); }
    Handle(const Handle&) = delete;
    Handle& operator=(const Handle&) = delete;
};

std::wstring readSetting(const std::wstring& section, const wchar_t* key, const wchar_t* fallback = L"") {
    wchar_t buffer[32768]{};
    DWORD length = GetPrivateProfileStringW(section.c_str(), key, fallback, buffer, 32768, configuration.c_str());
    if (length >= 32766) throw std::runtime_error("Watchdog configuration value is too long");
    return buffer;
}

DWORD readNumber(const std::wstring& section, const wchar_t* key, DWORD fallback, DWORD minimum, DWORD maximum) {
    auto text = readSetting(section, key);
    if (text.empty()) return fallback;
    wchar_t* end = nullptr;
    unsigned long value = std::wcstoul(text.c_str(), &end, 10);
    if (!end || *end || value < minimum || value > maximum) throw std::runtime_error("Invalid numeric watchdog setting");
    return static_cast<DWORD>(value);
}

void log(const std::wstring& message) {
    SYSTEMTIME now{}; GetLocalTime(&now);
    fs::create_directories(logPath.parent_path());
    std::wofstream output(logPath, std::ios::app);
    output << std::setfill(L'0') << now.wYear << L'-' << std::setw(2) << now.wMonth << L'-' << std::setw(2) << now.wDay
           << L' ' << std::setw(2) << now.wHour << L':' << std::setw(2) << now.wMinute << L':' << std::setw(2) << now.wSecond
           << L' ' << message << L'\n';
}

void report(DWORD state, DWORD error = NO_ERROR) {
    if (!statusHandle) return;
    status.dwServiceType = SERVICE_WIN32_OWN_PROCESS;
    status.dwCurrentState = state;
    status.dwControlsAccepted = state == SERVICE_RUNNING ? SERVICE_ACCEPT_STOP | SERVICE_ACCEPT_SHUTDOWN : 0;
    status.dwWin32ExitCode = error;
    status.dwWaitHint = state == SERVICE_STOP_PENDING ? 30000 : 0;
    SetServiceStatus(statusHandle, &status);
}

DWORD WINAPI control(DWORD code, DWORD, void*, void*) {
    if (code == SERVICE_CONTROL_STOP || code == SERVICE_CONTROL_SHUTDOWN) { report(SERVICE_STOP_PENDING); SetEvent(stopEvent); }
    return NO_ERROR;
}

bool serviceRunning(SC_HANDLE manager, const std::wstring& name) {
    SC_HANDLE raw = OpenServiceW(manager, name.c_str(), SERVICE_QUERY_STATUS);
    if (!raw) return false;
    SERVICE_STATUS_PROCESS serviceStatus{}; DWORD needed = 0;
    bool running = QueryServiceStatusEx(raw, SC_STATUS_PROCESS_INFO, reinterpret_cast<BYTE*>(&serviceStatus), sizeof(serviceStatus), &needed)
        && serviceStatus.dwCurrentState == SERVICE_RUNNING;
    CloseServiceHandle(raw);
    return running;
}

bool waitService(SC_HANDLE service, DWORD desired, DWORD timeoutMs) {
    auto deadline = GetTickCount64() + timeoutMs;
    do {
        SERVICE_STATUS_PROCESS current{}; DWORD needed = 0;
        if (!QueryServiceStatusEx(service, SC_STATUS_PROCESS_INFO, reinterpret_cast<BYTE*>(&current), sizeof(current), &needed)) return false;
        if (current.dwCurrentState == desired) return true;
        if (WaitForSingleObject(stopEvent, 250) != WAIT_TIMEOUT) return false;
    } while (GetTickCount64() < deadline);
    return false;
}

bool restartService(SC_HANDLE manager, const std::wstring& name, DWORD timeoutMs) {
    SC_HANDLE raw = OpenServiceW(manager, name.c_str(), SERVICE_STOP | SERVICE_START | SERVICE_QUERY_STATUS);
    if (!raw) return false;
    SERVICE_STATUS_PROCESS current{}; DWORD needed = 0;
    QueryServiceStatusEx(raw, SC_STATUS_PROCESS_INFO, reinterpret_cast<BYTE*>(&current), sizeof(current), &needed);
    if (current.dwCurrentState != SERVICE_STOPPED) {
        SERVICE_STATUS ignored{}; ControlService(raw, SERVICE_CONTROL_STOP, &ignored);
        if (!waitService(raw, SERVICE_STOPPED, timeoutMs)) { CloseServiceHandle(raw); return false; }
    }
    bool started = StartServiceW(raw, 0, nullptr) != FALSE && waitService(raw, SERVICE_RUNNING, timeoutMs);
    CloseServiceHandle(raw);
    return started;
}

bool runCommand(const std::wstring& command, DWORD timeoutMs) {
    std::vector<wchar_t> writable(command.begin(), command.end()); writable.push_back(0);
    STARTUPINFOW startup{}; startup.cb = sizeof(startup); startup.dwFlags = STARTF_USESHOWWINDOW; startup.wShowWindow = SW_HIDE;
    PROCESS_INFORMATION process{};
    if (!CreateProcessW(nullptr, writable.data(), nullptr, nullptr, FALSE, CREATE_NO_WINDOW, nullptr, nullptr, &startup, &process)) return false;
    Handle processHandle(process.hProcess), threadHandle(process.hThread);
    DWORD wait = WaitForSingleObject(process.hProcess, timeoutMs);
    if (wait == WAIT_TIMEOUT) { TerminateProcess(process.hProcess, ERROR_TIMEOUT); WaitForSingleObject(process.hProcess, 5000); return false; }
    DWORD exitCode = 1; return wait == WAIT_OBJECT_0 && GetExitCodeProcess(process.hProcess, &exitCode) && exitCode == 0;
}

struct Probe {
    std::wstring section;
    std::wstring service;
    std::wstring command;
    std::wstring dependency;
    DWORD failures = 0;
    ULONGLONG lastRestart = 0;
};

std::vector<Probe> loadProbes() {
    std::vector<Probe> probes;
    for (unsigned index = 1; index <= 64; ++index) {
        auto section = L"Probe." + std::to_wstring(index);
        auto service = readSetting(section, L"Service");
        if (service.empty()) break;
        probes.push_back({section, service, readSetting(section, L"Command"), readSetting(section, L"Dependency")});
    }
    if (probes.empty()) throw std::runtime_error("No watchdog probes are configured");
    return probes;
}

int run(bool once, bool noRestart) {
    DWORD interval = readNumber(L"Watchdog", L"IntervalSeconds", 20, 5, 3600);
    DWORD threshold = readNumber(L"Watchdog", L"FailureThreshold", 3, 1, 100);
    DWORD commandTimeout = readNumber(L"Watchdog", L"ProbeTimeoutSeconds", 10, 1, 300) * 1000;
    DWORD restartTimeout = readNumber(L"Watchdog", L"RestartTimeoutSeconds", 60, 5, 600) * 1000;
    DWORD cooldown = readNumber(L"Watchdog", L"RestartCooldownSeconds", 120, 10, 86400) * 1000;
    auto configuredLog = readSetting(L"Watchdog", L"Log");
    logPath = configuredLog.empty() ? configuration.parent_path() / L"Watchdog.log" : fs::path(configuredLog);
    auto probes = loadProbes();
    SC_HANDLE manager = OpenSCManagerW(nullptr, nullptr, SC_MANAGER_CONNECT);
    if (!manager) throw std::runtime_error("Cannot open Windows Service Control Manager");
    do {
        for (auto& probe : probes) {
            if (WaitForSingleObject(stopEvent, 0) == WAIT_OBJECT_0) break;
            bool dependencyHealthy = probe.dependency.empty() || serviceRunning(manager, probe.dependency);
            bool healthy = dependencyHealthy && serviceRunning(manager, probe.service)
                && (probe.command.empty() || runCommand(probe.command, commandTimeout));
            if (healthy) { if (probe.failures) log(probe.service + L" recovered before restart"); probe.failures = 0; continue; }
            if (!dependencyHealthy) { probe.failures = 0; continue; }
            ++probe.failures;
            log(probe.service + L" health probe failed (" + std::to_wstring(probe.failures) + L"/" + std::to_wstring(threshold) + L")");
            auto now = GetTickCount64();
            if (probe.failures >= threshold && !noRestart && (probe.lastRestart == 0 || now - probe.lastRestart >= cooldown)) {
                bool restarted = restartService(manager, probe.service, restartTimeout);
                log(probe.service + (restarted ? L" restarted successfully" : L" restart failed"));
                probe.lastRestart = now; probe.failures = 0;
            }
        }
        if (once) break;
    } while (WaitForSingleObject(stopEvent, interval * 1000) == WAIT_TIMEOUT);
    CloseServiceHandle(manager);
    return 0;
}

void WINAPI serviceMain(DWORD, wchar_t**) {
    statusHandle = RegisterServiceCtrlHandlerExW(serviceName.c_str(), control, nullptr);
    if (!statusHandle) return;
    try { report(SERVICE_START_PENDING); report(SERVICE_RUNNING); int result = run(false, false); report(SERVICE_STOPPED, result); }
    catch (const std::exception&) { report(SERVICE_STOPPED, ERROR_SERVICE_SPECIFIC_ERROR); }
}
}

int wmain(int argc, wchar_t** argv) {
    bool console = false, once = false, noRestart = false;
    for (int i = 1; i < argc; ++i) {
        std::wstring argument = argv[i];
        if (argument == L"--config" && i + 1 < argc) configuration = argv[++i];
        else if (argument == L"--name" && i + 1 < argc) serviceName = argv[++i];
        else if (argument == L"--console") console = true;
        else if (argument == L"--once") { console = true; once = true; }
        else if (argument == L"--no-restart") noRestart = true;
        else return ERROR_INVALID_PARAMETER;
    }
    if (configuration.empty() || GetFileAttributesW(configuration.c_str()) == INVALID_FILE_ATTRIBUTES) return ERROR_FILE_NOT_FOUND;
    stopEvent = CreateEventW(nullptr, TRUE, FALSE, nullptr); if (!stopEvent) return static_cast<int>(GetLastError());
    int result = 0;
    if (console) { try { result = run(once, noRestart); } catch (const std::exception&) { result = 1; } }
    else { SERVICE_TABLE_ENTRYW table[] = {{serviceName.data(), serviceMain}, {nullptr, nullptr}}; if (!StartServiceCtrlDispatcherW(table)) result = static_cast<int>(GetLastError()); }
    CloseHandle(stopEvent); return result;
}
