#include "EtlForgeXsltRunner.h"
#include <iostream>
#include <future>
int main(){
 try{
  std::ifstream s("C:/ETL/Samples/XSLT/ExpandEmbeddedXml/transform.xsl",std::ios::binary),i("C:/ETL/Samples/XSLT/ExpandEmbeddedXml/input.xml",std::ios::binary);
  std::ostringstream style,input;style<<s.rdbuf();input<<i.rdbuf();
  auto output=etlforge::executeXslt("C:/ETL/Tools/jdk8u504-b01/bin/java.exe","C:/ETL/Server/dist/lib/xslt/*",style.str(),{input.str()});
  if(output.find("<outbox>")==std::string::npos || output.find("&lt;?xml")!=std::string::npos)return 2;
  std::vector<std::future<std::string>> calls;
  for(int n=0;n<4;++n)calls.push_back(std::async(std::launch::async,[&](){return etlforge::executeXslt("C:/missing-java.exe","",style.str(),{input.str()});}));
  for(auto& call:calls)if(call.get()!=output)return 3;
  bool timeout=false;
  try {etlforge::executeXslt("","","<xsl:stylesheet version='3.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'><xsl:template match='/'><xsl:call-template name='forever'/></xsl:template><xsl:template name='forever'><xsl:call-template name='forever'/></xsl:template></xsl:stylesheet>",{"<root/>"},"C:/ETL/Server/runtime/xslt-native/ETLForgeXsltWorker.exe",false,200);}
  catch(const std::exception& e){timeout=std::string(e.what()).find("timed out")!=std::string::npos;}
  if(!timeout)return 4;
  std::cout<<"PASS: Windows x64 native SaxonC XSLT execution\n";
  return 0;
 }catch(const std::exception& e){std::cerr<<e.what()<<"\n";return 1;}
}
