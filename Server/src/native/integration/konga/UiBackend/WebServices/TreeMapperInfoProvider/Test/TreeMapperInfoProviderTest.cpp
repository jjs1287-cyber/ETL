                                                                                        
  
#include <konga/Utils/CrException.h>

#include <iostream>
#include <string>

#include <konga/TransformationEngine/CDTDParser.h>
#include <konga/UiBackend/WebServices/TreeMapperInfoProvider/Impl/TreeMapperInfoProviderImpl.h>

using namespace konga;
using namespace std;

void test1(){
	const char*dtd_files[]={
		"DataInterchange/XML_DTD/Custom/User_14/dev-heavy.dtd",
        "DataInterchange/xml_dtd/Custom/Admin/WebWorkflow.dtd",
        "DataInterchange/XML_DTD/Public/xCBL20/cbl.dtd",
		"DataInterchange/XML_DTD/Custom/User_8/ASN.xsd",
		"DataInterchange/XML_DTD/Custom/User_3/amzn-base.xsd"
	};
	TreeMapperInfoProviderImpl impl("Win_User", "****");

	std::string sJtr=impl.getJtrForStructure(dtd_files[1], "Process", true);
	FILE*out=fopen("C:/temp/WebWorkflow.jtr","w+");
	fprintf(out,"%s\n",sJtr.c_str());
	fclose(out);
}

void test2(){
	CDTDParser cDTDParser;
	cDTDParser.CreateScmForTextDocument(6,"C:/Temp/test_60s_5.scm");
}

void test3(){
	try{
		CDTDParser cDTDParser;
		if(cDTDParser.CreateScmForDB(358,1,"C:/Temp/tran358s.scm")!=S_OK)throw CrException(cDTDParser.get_LastError());
	}
	catch(CrException&e){
		printf("%s\n",e.GetErrorString().c_str());
	}
}

                           
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
  
                         
                                                 
                    
      
                                                     
                                                                         
                               
                                           
   
                       
                        
                                            
            
  
 
  

  
             
      
                                      
                                                                                                                            
                                                     
                                                                           
                               
                        
                                            
  
 
  

void test6() {
	try {
		TreeMapperInfoProviderImpl("emanuel", "****").testOperation("11", "12");
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorNumber() << endl;
	}
}

void test7() {
	try {
		TreeMapperInfoProviderImpl impl("emanuel", "****");
		impl.getJtrForWS("abc36a5f-8a64-409c-97d9-e32b947deec8",
						 "3656d8cb-7a5b-4f33-91f8-d534949d5f81",
						 "SoapBinding", "login", "document",
						 "literal", true, true);

                                                                                                                                                                

	}
	catch( const CrException& e ) {
		cout << e.GetErrorString() << endl;
	}
}

int main(int argc, char* argv[])
{
           
           
    test7();
	return 0;
}


