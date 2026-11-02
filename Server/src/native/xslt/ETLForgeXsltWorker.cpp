#include <windows.h>
#include <saxonc/SaxonProcessor.h>
#include <saxonc/Xslt30Processor.h>
#include <saxonc/XsltExecutable.h>
#include <saxonc/SaxonApiException.h>
#include <saxonc/XdmNode.h>
#include <saxonc/DocumentBuilder.h>
#include <saxonc/XPathProcessor.h>
#include <filesystem>
#include <fstream>
#include <iostream>
#include <memory>
#include <string>
#include <vector>
#include <iterator>
#include <stdexcept>

namespace {
std::string readFile(const std::filesystem::path& path) {
    std::ifstream in(path,std::ios::binary);
    if(!in) throw std::runtime_error("Cannot open XSLT input file.");
    std::string text((std::istreambuf_iterator<char>(in)),{});
    if(in.bad()) throw std::runtime_error("Cannot read XSLT input file.");
    if(text.find('\0')!=std::string::npos) throw std::runtime_error("XSLT expects UTF-8 input without NUL characters.");
    return text;
}
void writeFile(const std::filesystem::path& path,const std::string& text) {
    std::ofstream out(path,std::ios::binary|std::ios::trunc);
    out.write(text.data(),text.size()); out.close();
    if(!out) throw std::runtime_error("Cannot write XSLT output file.");
}
std::string fileUri(const std::filesystem::path& path) {
    const std::string raw=std::filesystem::absolute(path).generic_u8string();
    const char* hex="0123456789ABCDEF"; std::string uri="file:///";
    for(unsigned char c:raw) {
        if((c>='a'&&c<='z')||(c>='A'&&c<='Z')||(c>='0'&&c<='9')||c=='/'||c==':'||c=='-'||c=='_'||c=='.'||c=='~') uri+=c;
        else {uri+='%';uri+=hex[c>>4];uri+=hex[c&15];}
    }
    return uri;
}
struct CatalogFile {
    std::filesystem::path path;
    ~CatalogFile(){std::error_code error;std::filesystem::remove(path,error);}
};
void execute(int argc,wchar_t** argv) {
    if(argc<5) throw std::runtime_error("Usage: ETLForgeXsltWorker stylesheet input [input ...] output error");
    const int outputIndex=argc-2;
    std::string style=readFile(argv[1]);
    std::vector<std::string> inputs;
    CatalogFile catalog{std::filesystem::path(argv[outputIndex]).concat(L".catalog.xml")};
    std::string catalogXml="<catalog xmlns='urn:oasis:names:tc:entity:xmlns:xml:catalog'>";
    for(int i=2;i<outputIndex;++i) {
        inputs.push_back(readFile(argv[i]));
        const std::string number=std::to_string(i-1),uri=fileUri(argv[i]);
        catalogXml+="<uri name='urn:etlforge:xslt:param"+number+"' uri='"+uri+"'/>";
        catalogXml+="<uri name='urn:/etlforge/xslt/param"+number+"' uri='"+uri+"'/>";
    }
    writeFile(catalog.path,catalogXml+"</catalog>");
    SaxonProcessor processor(false);
    std::string uri=fileUri(catalog.path);const char* catalogName=uri.c_str();
    processor.setCatalogFiles(&catalogName,1);
    processor.setConfigurationProperty("http://saxon.sf.net/feature/allowedProtocols","urn");
    processor.setConfigurationProperty("http://saxon.sf.net/feature/allow-external-functions","false");
    processor.setConfigurationProperty("http://saxon.sf.net/feature/parserFeature?uri=http%3A//apache.org/xml/features/disallow-doctype-decl","true");
    std::unique_ptr<DocumentBuilder> builder(processor.newDocumentBuilder());
    builder->setBaseUri("urn:/etlforge/xslt/stylesheet");
    std::unique_ptr<XdmNode> styleNode(builder->parseXmlFromString(style.c_str(),"UTF-8"));
    std::unique_ptr<XPathProcessor> inspect(processor.newXPathProcessor());
    inspect->declareNamespace("xsl","http://www.w3.org/1999/XSL/Transform");
    inspect->setContextItem(styleNode.get());
    if(inspect->effectiveBooleanValue("exists(//xsl:include | //xsl:import)"))
        throw std::runtime_error("Stylesheet imports must be deployed project assets.");
    std::unique_ptr<Xslt30Processor> compiler(processor.newXslt30Processor());
    std::unique_ptr<XsltExecutable> executable(compiler->compileFromXdmNode(styleNode.get()));
    executable->setProperty("!encoding","UTF-8");
    builder->setBaseUri("urn:etlforge:xslt:param1");
    std::unique_ptr<XdmNode> input(builder->parseXmlFromString(inputs[0].c_str(),"UTF-8"));
    executable->setGlobalContextItem(input.get());
    const char* result=inputs.size()>1 ? executable->callTemplateReturningString("start_here") : executable->transformToString(input.get());
    if(!result) throw std::runtime_error("XSLT returned no serialized result.");
    std::unique_ptr<const char,void(*)(const char*)> owned(result,[](const char* p){::operator delete(const_cast<char*>(p));});
    std::string output(owned.get());
    writeFile(argv[outputIndex],output);
}
}
int wmain(int argc,wchar_t** argv) {
    std::string message;
    try {execute(argc,argv);return 0;}
    catch(SaxonApiException& failure){message=failure.what();}
    catch(const std::exception& failure){message=failure.what();}
    try {if(argc>=5)writeFile(argv[argc-1],message);}catch(...){}
    std::cerr<<message<<std::endl;return 1;
}
