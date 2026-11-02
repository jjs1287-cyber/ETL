#include "EtlForgeJsonConversion.h"
#include <boost/json.hpp>
#include <boost/json/basic_parser_impl.hpp>
#include <boost/multiprecision/cpp_int.hpp>
#include <xercesc/parsers/XercesDOMParser.hpp>
#include <xercesc/dom/DOM.hpp>
#include <xercesc/framework/MemBufInputSource.hpp>
#include <xercesc/sax/HandlerBase.hpp>
#include <xercesc/sax2/XMLReaderFactory.hpp>
#include <xercesc/sax2/DefaultHandler.hpp>
#include <xercesc/sax2/Attributes.hpp>
#include <xercesc/util/SecurityManager.hpp>
#include <windows.h>
#include <objbase.h>
#include <charconv>
#include <algorithm>
#include <cmath>
#include <cstring>
#include <filesystem>
#include <fstream>
#include <map>
#include <memory>
#include <set>
#include <sstream>
#include <stdexcept>
#include <vector>

namespace etlforge {
namespace {
using Error = boost::system::error_code;
using View = boost::json::string_view;
struct JsonNode {
    char kind = 'n';
    std::string name, value;
    std::vector<JsonNode> children;
};
using BigInteger = boost::multiprecision::cpp_int;
BigInteger binaryUnits(double value) {
    std::uint64_t bits = 0; std::memcpy(&bits, &value, sizeof(bits));
    unsigned exponent = static_cast<unsigned>((bits >> 52) & 2047);
    BigInteger significand = bits & ((std::uint64_t(1) << 52) - 1);
    if (exponent) { significand += std::uint64_t(1) << 52; significand <<= exponent - 1; }
    return significand;
}
bool insideRoundingInterval(const std::string& scientific, const BigInteger& low, const BigInteger& high) {
    std::size_t e = scientific.find('e');
    std::string digits = scientific.substr(0, e);
    auto dot = digits.find('.');
    int decimalPower = std::stoi(scientific.substr(e + 1));
    if (dot != std::string::npos) { decimalPower -= static_cast<int>(digits.size() - dot - 1); digits.erase(dot, 1); }
    BigInteger value = 0;
    for (char digit : digits) value = value * 10 + (digit - '0');
    value <<= 1075;
    BigInteger scale = 1;
    for (int i = 0; i < std::abs(decimalPower); ++i) scale *= 10;
    if (decimalPower >= 0) value *= scale;
    else return value > low * scale && value < high * scale;
    return value > low && value < high;
}
std::string javaNumber(std::string raw, double number) {
    if (raw.find_first_of(".eE") == std::string::npos) {
        if (raw == "-0") return "0";
        return raw;
    }
    if (!std::isfinite(number)) throw std::runtime_error("JSON floating-point overflow");
    if (number == 0) return std::signbit(number) ? "-0.0" : "0.0";
    bool negative = number < 0;
    double magnitude = std::abs(number);
    BigInteger units = binaryUnits(magnitude);
    BigInteger low = units + binaryUnits(std::nextafter(magnitude, 0.0));
    double next = std::nextafter(magnitude, INFINITY);
    BigInteger high = std::isfinite(next) ? units + binaryUnits(next) : units * 2 + (units - binaryUnits(std::nextafter(magnitude, 0.0)));
    char buffer[128];
    auto result = std::to_chars(buffer, buffer + sizeof(buffer), magnitude, std::chars_format::scientific);
    if (result.ec != std::errc()) throw std::runtime_error("Cannot format JSON number");
    std::string scientific(buffer, result.ptr);
    for (int precision = 1; precision <= 17; ++precision) {
        result = std::to_chars(buffer, buffer + sizeof(buffer), magnitude, std::chars_format::scientific, precision - 1);
        scientific.assign(buffer, result.ptr);
        int candidateExponent = std::stoi(scientific.substr(scientific.find('e') + 1));
        if (precision == 1 && (candidateExponent < -3 || candidateExponent >= 7)) continue;
        if (insideRoundingInterval(scientific, low, high)) break;
    }
    int binaryExponent = std::ilogb(magnitude);
    if (binaryExponent >= 0 && binaryExponent <= 62 && std::floor(magnitude) == magnitude) {
        std::uint64_t integer = static_cast<std::uint64_t>(magnitude), rounding = 1;
        if (binaryExponent > 53) {
            std::uint64_t uncertainty = std::uint64_t(1) << (binaryExponent - 54);
            while (uncertainty >= 10) { uncertainty /= 10; rounding *= 10; }
        }
        integer = (integer + rounding / 2) / rounding * rounding;
        std::string digits = std::to_string(integer);
        int decimalExponent = static_cast<int>(digits.size()) - 1;
        while (digits.size() > 1 && digits.back() == '0') digits.pop_back();
        if (digits.size() > 1) digits.insert(1, ".");
        scientific = digits + "e" + std::to_string(decimalExponent);
    }
    if (negative) scientific.insert(0, "-");
    auto e = scientific.find('e');
    int exponent = std::stoi(scientific.substr(e + 1));
    std::string mantissa = scientific.substr(0, e);
    if (exponent < -3 || exponent >= 7) {
        if (mantissa.find('.') == std::string::npos) mantissa += ".0";
        return mantissa + "E" + std::to_string(exponent);
    }
    bool sign = mantissa[0] == '-';
    std::string digits = sign ? mantissa.substr(1) : mantissa;
    auto dot = digits.find('.'); if (dot != std::string::npos) digits.erase(dot, 1);
    int position = exponent + 1;
    std::string fixed;
    if (position <= 0) fixed = "0." + std::string(-position, '0') + digits;
    else if (position >= static_cast<int>(digits.size())) fixed = digits + std::string(position - digits.size(), '0');
    else { fixed = digits; fixed.insert(position, "."); }
    if (sign) fixed.insert(0, "-");
    if (fixed.find('.') == std::string::npos) fixed += ".0";
    return fixed;
}
struct JsonEvents {
    static constexpr std::size_t max_array_size = SIZE_MAX, max_object_size = SIZE_MAX,
        max_string_size = SIZE_MAX, max_key_size = SIZE_MAX;
    JsonNode root;
    std::vector<JsonNode*> stack;
    std::string key, partial, numeric;
    std::map<std::string,std::string> numericCache;
    JsonNode* add(char kind, const std::string& value = "") {
        JsonNode node; node.kind = kind; node.name = key; node.value = value; key.clear();
        if (stack.empty()) { root = std::move(node); return &root; }
        stack.back()->children.push_back(std::move(node));
        return &stack.back()->children.back();
    }
    bool on_document_begin(Error&) { return true; }
    bool on_document_end(Error&) { return true; }
    bool on_object_begin(Error&) { stack.push_back(add('o')); return true; }
    bool on_array_begin(Error&) { stack.push_back(add('a')); return true; }
    bool on_object_end(std::size_t, Error&) { stack.pop_back(); return true; }
    bool on_array_end(std::size_t, Error&) { stack.pop_back(); return true; }
    bool on_string_part(View s, std::size_t, Error&) { partial.append(s.data(), s.size()); return true; }
    bool on_string(View s, std::size_t, Error&) { partial.append(s.data(), s.size()); add('s', partial); partial.clear(); return true; }
    bool on_key_part(View s, std::size_t, Error&) { partial.append(s.data(), s.size()); return true; }
    bool on_key(View s, std::size_t, Error&) { partial.append(s.data(), s.size()); key.swap(partial); partial.clear(); return true; }
    bool on_number_part(View s, Error&) { numeric.append(s.data(), s.size()); return true; }
    bool number(View s, double value) {
        numeric.append(s.data(),s.size());
        auto found=numericCache.find(numeric);
        if(found!=numericCache.end())add('d',found->second);
        else {auto formatted=javaNumber(numeric,value);add('d',formatted);if(numericCache.size()<256 && numeric.size()<=128)numericCache.emplace(numeric,std::move(formatted));}
        numeric.clear();return true;
    }
    bool on_int64(std::int64_t v, View s, Error&) { return number(s, static_cast<double>(v)); }
    bool on_uint64(std::uint64_t v, View s, Error&) { return number(s, static_cast<double>(v)); }
    bool on_double(double v, View s, Error&) { return number(s, v); }
    bool on_bool(bool b, Error&) { add('b', b ? "true" : "false"); return true; }
    bool on_null(Error&) { add('n'); return true; }
    bool on_comment_part(View, Error&) { return true; }
    bool on_comment(View, Error&) { return true; }
};
std::string readFile(const std::wstring& file) {
    std::ifstream stream(std::filesystem::path(file), std::ios::binary);
    if (!stream) throw std::runtime_error("Cannot open conversion input file");
    stream.seekg(0,std::ios::end);const auto length=stream.tellg();
    if(length<0 || static_cast<unsigned long long>(length)>std::string().max_size())throw std::runtime_error("Cannot determine conversion input size");
    std::string text(static_cast<size_t>(length),'\0');stream.seekg(0,std::ios::beg);
    if(length && !stream.read(&text[0],static_cast<std::streamsize>(length)))throw std::runtime_error("Cannot read conversion input file");
    if(stream.peek()!=std::char_traits<char>::eof() || stream.bad())throw std::runtime_error("Conversion input changed while reading");
    return text;
}
void writeFile(const std::wstring& file, const std::string& text) {
    std::ofstream stream(std::filesystem::path(file), std::ios::binary | std::ios::trunc);
    if (!stream) throw std::runtime_error("Cannot open conversion output file");
    stream.write(text.data(), text.size()); stream.close();
    if (!stream) throw std::runtime_error("Cannot write conversion output file");
}
std::string utf8(const XMLCh* text) {
    if (!text) return "";
    std::string value;
    for(size_t i=0;text[i];++i){
        unsigned cp=text[i];
        if(cp>=0xD800 && cp<=0xDBFF){unsigned low=text[++i];if(low<0xDC00 || low>0xDFFF)throw std::runtime_error("Invalid XML Unicode");cp=0x10000+((cp-0xD800)<<10)+(low-0xDC00);}
        else if(cp>=0xDC00 && cp<=0xDFFF)throw std::runtime_error("Invalid XML Unicode");
        if(cp<0x80)value+=static_cast<char>(cp);
        else if(cp<0x800){value+=static_cast<char>(0xC0|(cp>>6));value+=static_cast<char>(0x80|(cp&63));}
        else if(cp<0x10000){value+=static_cast<char>(0xE0|(cp>>12));value+=static_cast<char>(0x80|((cp>>6)&63));value+=static_cast<char>(0x80|(cp&63));}
        else {value+=static_cast<char>(0xF0|(cp>>18));value+=static_cast<char>(0x80|((cp>>12)&63));value+=static_cast<char>(0x80|((cp>>6)&63));value+=static_cast<char>(0x80|(cp&63));}
    }
    return value;
}
struct XmlNode {
    std::string name, text;
    std::map<std::string, std::string> attributes;
    std::vector<XmlNode> children;
    std::string attr(const std::string& key) const { auto it = attributes.find(key); return it == attributes.end() ? "" : it->second; }
};
XmlNode copyXml(xercesc::DOMElement* element,size_t depth=0) {
    if(depth>512)throw std::runtime_error("Conversion XML/XSD nesting exceeds 512 levels.");
    XmlNode node; node.name = utf8(element->getLocalName());
    auto attrs = element->getAttributes();
    for (XMLSize_t i = 0; i < attrs->getLength(); ++i) {
        auto a = attrs->item(i); node.attributes[utf8(a->getNodeName())] = utf8(a->getNodeValue());
    }
    for (auto c = element->getFirstChild(); c; c = c->getNextSibling()) {
        if (c->getNodeType() == xercesc::DOMNode::ELEMENT_NODE) node.children.push_back(copyXml(static_cast<xercesc::DOMElement*>(c),depth+1));
        else if (c->getNodeType() == xercesc::DOMNode::TEXT_NODE || c->getNodeType() == xercesc::DOMNode::CDATA_SECTION_NODE) node.text += utf8(c->getNodeValue());
    }
    return node;
}
XmlNode parseXml(const std::string& text) {
    static const bool initialized = [] { xercesc::XMLPlatformUtils::Initialize(); return true; }();
    (void)initialized;
    if (text.find("<!DOCTYPE") != std::string::npos || text.find("<!ENTITY") != std::string::npos)
        throw std::runtime_error("DTD/entity declarations are disabled");
    xercesc::SecurityManager security;security.setEntityExpansionLimit(16);
    xercesc::XercesDOMParser parser;parser.setSecurityManager(&security);
    xercesc::HandlerBase errors;
    parser.setErrorHandler(&errors); parser.setDoNamespaces(true);
    parser.setValidationScheme(xercesc::XercesDOMParser::Val_Never);
    parser.setLoadExternalDTD(false); parser.setDisableDefaultEntityResolution(true);
    xercesc::MemBufInputSource source(reinterpret_cast<const XMLByte*>(text.data()), text.size(), "ETLForge conversion input", false);
    try { parser.parse(source); }
    catch (const xercesc::XMLException&) { throw std::runtime_error("Malformed conversion XML/XSD"); }
    catch (const xercesc::SAXException&) { throw std::runtime_error("Malformed conversion XML/XSD"); }
    if (parser.getErrorCount() || !parser.getDocument() || !parser.getDocument()->getDocumentElement())
        throw std::runtime_error("Malformed conversion XML/XSD");
    if(parser.getDocument()->getDoctype())throw std::runtime_error("DTD/entity declarations are disabled");
    return copyXml(parser.getDocument()->getDocumentElement());
}
struct Schema {
    std::map<std::string, std::string> types;
    std::set<std::string> simple;
    void visit(const XmlNode& node, std::string path) {
        if (node.name == "element") {
            path += (path.empty() ? "" : "/") + node.attr("name");
            std::string type = node.attr("type");
            auto colon = type.find(':'); if (colon != std::string::npos) type.erase(0, colon + 1);
            if (!type.empty()) types[path] = type;
        } else if (node.name == "attribute" && node.attr("name") == "simple") simple.insert(path);
        for (auto& child : node.children) visit(child, path);
    }
    explicit Schema(const std::wstring& file) { visit(parseXml(readFile(file)), ""); }
};
std::string safeName(const std::string& name) {
    if(std::all_of(name.begin(),name.end(),[](unsigned char c){return c<128;})){
        bool numeric=true;for(unsigned char c:name)if(c<'0' || c>'9')numeric=false;
        if(numeric)return "_"+name;
        std::string result=name;
        for(size_t i=0;i<result.size();++i){unsigned char c=result[i];bool letter=(c>='a'&&c<='z')||(c>='A'&&c<='Z');if(!(letter||c=='_'||(i&&((c>='0'&&c<='9')||c=='.'||c=='-'))))result[i]='_';}
        if(result.size()>=3 && (result[0]=='x'||result[0]=='X') && (result[1]=='m'||result[1]=='M') && (result[2]=='l'||result[2]=='L'))result.replace(0,3,"___");
        return result;
    }
    int count = MultiByteToWideChar(CP_UTF8, MB_ERR_INVALID_CHARS, name.data(), static_cast<int>(name.size()), nullptr, 0);
    std::wstring wide(count, L'\0');
    if (count) MultiByteToWideChar(CP_UTF8, MB_ERR_INVALID_CHARS, name.data(), static_cast<int>(name.size()), &wide[0], count);
    bool numeric = true;
    for (wchar_t ch : wide) { WORD type = 0; GetStringTypeW(CT_CTYPE1, &ch, 1, &type); if (!(type & C1_DIGIT)) numeric = false; }
    if (numeric) return "_" + name;
    for (std::size_t i = 0; i < wide.size(); ++i) {
        WORD type = 0; GetStringTypeW(CT_CTYPE1, &wide[i], 1, &type);
        bool valid = (type & C1_ALPHA) || wide[i] == '_' || (i && ((type & C1_DIGIT) || wide[i] == '.' || wide[i] == '-'));
        if (!valid) wide[i] = '_';
    }
    wide.push_back(0);
    std::string result = utf8(reinterpret_cast<const XMLCh*>(wide.c_str()));
    if (result.size() >= 3 && (result[0] == 'x' || result[0] == 'X') && (result[1] == 'm' || result[1] == 'M') && (result[2] == 'l' || result[2] == 'L')) result.replace(0, 3, "___");
    return result;
}
std::string escapeXml(const std::string& value) {
    std::string out;
    for (size_t i=0;i<value.size();++i) {
      const unsigned char ch=static_cast<unsigned char>(value[i]);
      if((ch<32 && ch!=9 && ch!=10 && ch!=13) || (ch==0xEF && i+2<value.size() && static_cast<unsigned char>(value[i+1])==0xBF && static_cast<unsigned char>(value[i+2])>=0xBE))throw std::runtime_error("JSON text contains a character not representable in XML 1.0.");
      char c=value[i];switch(c) {
        case '&': out += "&amp;"; break; case '<': out += "&lt;"; break; case '>': out += "&gt;"; break;
        case '"': out += "&quot;"; break; case '\'': out += "&apos;"; break; default: out += c;
      }
    }
    return out;
}
void emitXml(const JsonNode& node, const std::string& name, const std::string& path, const Schema& schema, std::string& out, bool root = false, bool flattenObject = false) {
    bool group = node.kind == 'o' || node.kind == 'a';
    bool simple = node.kind == 'a' && schema.simple.count(path);
    if (!flattenObject) {
        out += '<' + safeName(name);
        if (group) out += node.kind == 'o' ? " object=\"true\"" : " array=\"true\"";
        if (simple) out += " simple=\"true\"";
        if (root && group) out += " xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
        if (node.kind == 'n') { out += " xsi:nil=\"true\"/>\n"; return; }
        out += '>';
    }
    if (group) {
        for (auto& child : node.children) {
            std::string childName = node.kind == 'o' ? child.name : child.kind == 'a' ? "array" : "item";
            if (simple && child.kind == 'o') {
                for (auto& field : child.children) emitXml(field, field.name, path + '/' + field.name, schema, out);
            } else emitXml(child, childName, path + '/' + childName, schema, out);
        }
    } else out += escapeXml(node.value);
    if (!flattenObject) out += "</" + safeName(name) + ">\n";
}
std::string quote(const std::string& text) { return boost::json::serialize(boost::json::value(text)); }
bool isTrue(const std::string& text) { return text == "true" || text == "1"; }
class JsonXmlStream final : public xercesc::DefaultHandler {
    struct Frame {
        std::string path,text,type;
        bool array=false,object=false,nil=false,simple=false,suppressed=false,wrapper=false;
        size_t children=0;
    };
    const Schema& schema;
    std::vector<Frame> frames;
public:
    std::string output;
    explicit JsonXmlStream(const Schema& schema):schema(schema){}
    void startElement(const XMLCh*,const XMLCh* local,const XMLCh*,const xercesc::Attributes& attrs) override {
        if(frames.size()>512)throw std::runtime_error("Conversion XML nesting exceeds 512 levels.");
        const auto name=utf8(local);Frame frame;frame.path=frames.empty()?name:frames.back().path+'/'+name;
        if(!frames.empty()){
            auto& parent=frames.back();frame.suppressed=parent.suppressed || (!parent.array && !parent.object);
            if(!frame.suppressed){
                if(parent.children++)output+=',';
                frame.wrapper=parent.array && parent.simple;
                if(frame.wrapper)output+='{';
                if(parent.object || frame.wrapper)output+=quote(name)+':';
            }
        }
        for(XMLSize_t i=0;i<attrs.getLength();++i){
            const auto key=utf8(attrs.getQName(i));
            if(key=="array")frame.array=isTrue(utf8(attrs.getValue(i)));
            else if(key=="object")frame.object=isTrue(utf8(attrs.getValue(i)));
            else if(key=="xsi:nil")frame.nil=isTrue(utf8(attrs.getValue(i)));
        }
        auto type=schema.types.find(frame.path);frame.type=type==schema.types.end()?"string":type->second;
        frame.simple=schema.simple.count(frame.path)!=0;
        if(!frame.suppressed && (frame.array || frame.object))output+=frame.array?'[':'{';
        frames.push_back(std::move(frame));
    }
    void characters(const XMLCh* text,XMLSize_t length) override {
        if(frames.empty() || frames.back().suppressed || frames.back().array || frames.back().object)return;
        auto& frame=frames.back();
        frame.text.append(reinterpret_cast<const char*>(text),length*sizeof(XMLCh));
    }
    void endElement(const XMLCh*,const XMLCh*,const XMLCh*) override {
        auto& frame=frames.back();
        if(!frame.suppressed){
            if(frame.array || frame.object)output+=frame.array?']':'}';
            else if(frame.nil)output+="null";
            else {
                std::vector<XMLCh> text(frame.text.size()/sizeof(XMLCh)+1,0);
                if(!frame.text.empty())std::memcpy(text.data(),frame.text.data(),frame.text.size());
                auto value=utf8(text.data());
                if(frame.type=="boolean")output+=isTrue(value)?"true":"false";
                else if(frame.type=="integer" || frame.type=="long" || frame.type=="double")output+=value.empty()?"null":value;
                else output+=quote(value);
            }
            if(frame.wrapper)output+='}';
        }
        frames.pop_back();
    }
    void error(const xercesc::SAXParseException& e) override {throw e;}
    void fatalError(const xercesc::SAXParseException& e) override {throw e;}
};
std::string streamXmlToJson(const std::string& text,const Schema& schema){
    if(text.find("<!DOCTYPE")!=std::string::npos || text.find("<!ENTITY")!=std::string::npos)throw std::runtime_error("DTD/entity declarations are disabled");
    std::unique_ptr<xercesc::SAX2XMLReader> reader(xercesc::XMLReaderFactory::createXMLReader());
    reader->setFeature(xercesc::XMLUni::fgSAX2CoreNameSpaces,true);
    reader->setFeature(xercesc::XMLUni::fgXercesDisallowDoctype,true);
    reader->setFeature(xercesc::XMLUni::fgXercesLoadExternalDTD,false);
    reader->setFeature(xercesc::XMLUni::fgXercesDisableDefaultEntityResolution,true);
    JsonXmlStream handler(schema);reader->setContentHandler(&handler);reader->setErrorHandler(&handler);
    xercesc::MemBufInputSource input(reinterpret_cast<const XMLByte*>(text.data()),text.size(),"ETLForge JSON conversion input",false);
    try {reader->parse(input);}
    catch(const xercesc::SAXException&){throw std::runtime_error("Malformed conversion XML");}
    catch(const xercesc::XMLException&){throw std::runtime_error("Malformed conversion XML");}
    if(handler.output.empty())throw std::runtime_error("Conversion XML has no root element.");
    return handler.output;
}
}
std::wstring resolveJsonSchemaPath(const std::wstring& home,const std::wstring& relative) {
    try {
    if(relative.empty() || relative.find(L'\0')!=std::wstring::npos)throw std::runtime_error("Missing or invalid JSON schema path.");
    std::filesystem::path path(relative);
    if(path.is_absolute() || path.has_root_name() || path.has_root_directory())throw std::runtime_error("JSON schema must be relative to the instance home.");
    const auto root=std::filesystem::canonical(std::filesystem::path(home));
    const auto resolved=std::filesystem::canonical(root/path);
    auto ri=root.begin(), pi=resolved.begin();
    for(;ri!=root.end();++ri,++pi)if(pi==resolved.end() || _wcsicmp(ri->c_str(),pi->c_str()))throw std::runtime_error("JSON schema is outside the instance home.");
    if(!std::filesystem::is_regular_file(resolved))throw std::runtime_error("JSON schema is not a file.");
    return resolved.wstring();
    } catch(const std::filesystem::filesystem_error&) {throw std::runtime_error("Cannot resolve deployed JSON XSD file.");}
}
void convertJsonAtomically(const std::wstring& input,const std::wstring& schema,const std::wstring& output,bool jsonToXml) {
    GUID id;wchar_t guid[40];
    if(FAILED(CoCreateGuid(&id)) || !StringFromGUID2(id,guid,40))throw std::runtime_error("Cannot allocate JSON output ID.");
    const auto temporary=output+L"."+guid+L".part";
    struct Cleanup {const std::wstring& path;~Cleanup(){DeleteFileW(path.c_str());}} cleanup{temporary};
    if(jsonToXml)convertJsonFileToXml(input,schema,temporary);else convertXmlFileToJson(input,schema,temporary);
    if(!MoveFileExW(temporary.c_str(),output.c_str(),MOVEFILE_REPLACE_EXISTING))throw std::runtime_error("Cannot publish JSON conversion output.");
}
void convertJsonFileToXml(const std::wstring& jsonFile, const std::wstring& xsdFile, const std::wstring& xmlFile) {
    Schema schema(xsdFile);
    std::string input = readFile(jsonFile);
    if (input.compare(0, 3, "\xEF\xBB\xBF") == 0) input.erase(0, 3);
    boost::json::parse_options options; options.max_depth = 512; options.numbers = boost::json::number_precision::precise;
    boost::json::basic_parser<JsonEvents> parser(options);
    Error error;
    std::size_t used = parser.write_some(false, input.data(), input.size(), error);
    if (error) throw std::runtime_error("Malformed JSON: " + error.message());
    if (used != input.size() || !parser.done()) throw std::runtime_error("Malformed JSON: trailing content or incomplete document");
    auto& root = parser.handler().root;
    std::string output = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    emitXml(root, root.kind == 'o' || root.kind == 'a' ? "json" : "item", "json", schema, output, true);
    writeFile(xmlFile, output);
}
void convertXmlFileToJson(const std::wstring& xmlFile, const std::wstring& xsdFile, const std::wstring& jsonFile) {
    Schema schema(xsdFile);
    writeFile(jsonFile,streamXmlToJson(readFile(xmlFile),schema));
}
}
