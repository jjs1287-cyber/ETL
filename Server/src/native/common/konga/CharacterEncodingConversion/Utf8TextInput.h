#pragma once
#include <string>
namespace etlforge {
std::wstring textPathWide(const std::string& path);
std::string textPathUtf8(const std::wstring& path);
std::string xmlDeclaredEncoding(const std::string& text,size_t* valueStart=0,size_t* valueEnd=0);
void xmlDeclarationToUtf8(std::string& text);
void requireUtf8Text(const char* bytes,size_t size);
void requireUtf8TextFile(const std::string& path);
class Utf8TextInputImpl;
class Utf8TextInput {
    Utf8TextInputImpl* implementation;
    Utf8TextInput(const Utf8TextInput&);
    Utf8TextInput& operator=(const Utf8TextInput&);
public:
    explicit Utf8TextInput(const std::string& path,const std::string& configured="");
    ~Utf8TextInput();
    const std::string& path()const;
};
}
