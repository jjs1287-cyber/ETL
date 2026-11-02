#include <boost/filesystem.hpp>
#include <boost/regex.hpp>
#include <boost/thread.hpp>
#include <iostream>
int main() {
 static_assert(sizeof(void*)==8,"x64 required");
 boost::filesystem::path root("C:/ETL/Server");
 if(root.filename().string()!="Server" || !boost::filesystem::is_directory(root))return 1;
 boost::regex pattern("ETLForge-([0-9]+)");boost::smatch match;std::string value="ETLForge-64";
 if(!boost::regex_match(value,match,pattern)||match[1]!="64")return 2;
 int result=0;boost::thread worker([&result](){result=64;});worker.join();
 if(result!=64)return 3;
 std::cout << "PASS: Boost filesystem, regex and thread libraries linked and executed on x64\n";
}
