#define wmain etlforgeCloudLoggingEntryPoint
#include "../../src/native/cloudlogging/Main.cpp"
#undef wmain
#include <iostream>
int wmain() {
 int checks=0;auto require=[&](bool ok){if(!ok)throw std::runtime_error("Cloud logging regression check failed");++checks;};
 std::string unicode=u8"café € 中文 😀";require(utf8(wide(unicode))==unicode);
 require(limited(std::string(1048575,'x')+u8"😀").size()==1048575);
 require(limited(std::string(1048572,'x')+u8"😀").size()==1048576);
 require(limited(unicode)==unicode);
 boost::json::object payload{{"Operation Name","A,\"B\""},{"Operation messages",u8"line1\r\n中文 😀"}};
 auto output=csv(payload);require(output.find("\"A,\"\"B\"\"\"")!=std::string::npos);require(output.find(u8"line1\r\n中文 😀")!=std::string::npos);
 std::string master(64,'a');auto encrypted=protect(unicode,master);require(encrypted!=unicode);require(protect(encrypted,master,true)==unicode);
 bool authenticationRejected=false;try{protect(encrypted,std::string(64,'b'),true);}catch(...){authenticationRejected=true;}require(authenticationRejected);
 auto tampered=encrypted;tampered.back()=tampered.back()=='A'?'B':'A';authenticationRejected=false;try{protect(tampered,master,true);}catch(...){authenticationRejected=true;}require(authenticationRejected);
 auto safe=publicSettings({{"passwordProtected",encrypted},{"bearerProtected",encrypted}});require(!safe.if_contains("passwordProtected")&&!safe.if_contains("bearerProtected"));require(safe.at("hasPassword").as_bool());
 auto id=guid();require(id.size()==36);require(id!=guid());
 boost::json::object config{{"enabled",false},{"type","sftp"}};validate(config);++checks;
 config["enabled"]=true;bool rejected=false;try{validate(config);}catch(...){rejected=true;}require(rejected);
 config={{"enabled",true},{"type","http"},{"environment","dev"},{"url","http://127.0.0.1:47100/etlforge/cloud-logging"}};validate(config);++checks;
 config["url"]="http://user:secret@localhost";rejected=false;try{validate(config);}catch(...){rejected=true;}require(rejected);
 config["url"]="http://localhost";config["excludeOperationIds"]="1,2,3";validate(config);++checks;
 config["excludeOperationIds"]="1,,3";rejected=false;try{validate(config);}catch(...){rejected=true;}require(rejected);
 bool sftp=false;auto* version=curl_version_info(CURLVERSION_NOW);for(auto p=version->protocols;*p;++p)if(std::string(*p)=="sftp")sftp=true;require(sftp);
 std::cout<<"PASS: "<<checks<<" cloud logging contract checks\n";return 0;
}
