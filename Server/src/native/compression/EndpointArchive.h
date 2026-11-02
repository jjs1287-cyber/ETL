#pragma once
#include <string>
#include <cstddef>
#include <vector>
namespace etlforge { namespace compression {
struct File { std::string path; std::string name; };
std::vector<File> extract(const std::string& archivePath, const std::string& directory, const std::string& password, const std::string& sourceName = "", unsigned long long byteLimit = 2ULL*1024*1024*1024, size_t entryLimit = 10000);
void pack(const std::vector<File>& files, const std::string& archivePath, const std::string& format, const std::string& password);
std::string extension(const std::string& format);
}}
