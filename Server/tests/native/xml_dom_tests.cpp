#include <konga/Xml/DOMDocumentPtr.h>
#include <konga/Xml/DOMElementPtr.h>
#include <konga/Xml/DOMNodeListPtr.h>
#include <konga/Xml/CrXmlException.h>
#include <iostream>
#include <stdexcept>
using namespace konga::xml;
static void check(bool value,const char* message){if(!value)throw std::runtime_error(message);}
int main(){
 try {
  const std::string text="\xe6\x97\xa5\xe6\x9c\xac";
  const std::string input="<?xml version=\"1.0\" encoding=\"UTF-8\"?><root xmlns=\"urn:default\" xmlns:p=\"urn:test\"><p:value id=\"1\">"+text+"</p:value><p:value id=\"2\">two</p:value></root>";
  DOMNodePtr retained;DOMNodeListPtr retainedList;
  {
   DOMDocumentPtr doc;check(doc.loadString(input),"Parse UTF-8 document");
   doc.addSelectionNamespace("r","urn:default");doc.addSelectionNamespace("p","urn:test");
   retained=doc.selectSingleNode("/r:root/p:value[@id='1']");check(!!retained && retained.getTextContent()==text,"Namespace XPath selection");
   retainedList=doc.selectNodes("/r:root/p:value");check(retainedList.getLength()==2,"XPath list");
   check(retained.lookupNamespacePrefix("urn:test",false)=="p","Namespace prefix lookup");
   check(retained.lookupNamespacePrefix("urn:default",true).empty(),"Default namespace prefix lookup");
   const std::string serialized=doc.getXml();check(serialized.find(text)!=std::string::npos && serialized.find("UTF-8")!=std::string::npos,"UTF-8 serialization");
   DOMDocumentPtr reloaded;check(reloaded.loadString(serialized),"Parse serialized UTF-8 document");
   check(doc.save("etlforge-xml-smoke.xml"),"Save document");
   DOMDocumentPtr file;check(file.loadFile("etlforge-xml-smoke.xml"),"Reload saved document");
  }
  check(retained.getTextContent()==text && retainedList.getItem(1).getTextContent()=="two","XPath result lifetime after document handle release");
  retained=0;retainedList=0;
  std::cout<<"PASS: XML UTF-8 parse/serialization/file round trip, namespace XPath, and retained query-result ownership\n";
 }catch(const konga::CrException& error){std::cerr<<error.GetErrorString()<<"\n";return 1;}
 catch(const std::exception& error){std::cerr<<error.what()<<"\n";return 2;}
}
