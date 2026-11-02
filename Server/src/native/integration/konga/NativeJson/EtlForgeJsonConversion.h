#pragma once
#include <string>

namespace etlforge {
std::wstring resolveJsonSchemaPath(const std::wstring& home, const std::wstring& relative);
void convertJsonAtomically(const std::wstring& input, const std::wstring& schema, const std::wstring& output, bool jsonToXml);
void convertJsonFileToXml(const std::wstring& jsonFile, const std::wstring& xsdFile, const std::wstring& xmlFile);
void convertXmlFileToJson(const std::wstring& xmlFile, const std::wstring& xsdFile, const std::wstring& jsonFile);
}
