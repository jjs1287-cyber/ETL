#include <konga/Xml/XmlStrings.h>
#include <iostream>
#include <string>
#include <vector>
// Test-only UTF-8 configuration; the converter and Xerces runtime are production code.
const XMLCh* konga::get_xml_enc() {
  static const XMLCh utf8[] = {'U', 'T', 'F', '-', '8', 0};
  return utf8;
}
int main() {
  XERCES_CPP_NAMESPACE::XMLPlatformUtils::Initialize();
  int result = 0;
  {
    const std::vector<std::string> cases{"", "ASCII", u8"H\u00e9llo", u8"\u6771\u4eac", u8"\U0001f600", std::string(8192, 'x')};
    for (const auto& input : cases) {
      konga::xml::StringConverter wide(input);
      if (konga::xml::XMLChToString(wide.getXMLCh()) != input) { result = 1; break; }
    }
  }
  XERCES_CPP_NAMESPACE::XMLPlatformUtils::Terminate();
  if (!result) std::cout << "PASS: empty, ASCII, Latin, Japanese, supplementary Unicode, large input\n";
  return result;
}

