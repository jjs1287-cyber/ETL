#include "NativeJavaScript.h"
#include "ScriptBlocks.h"
#include <iostream>
#include <stdexcept>
#include <chrono>
using namespace etlforge::javascript;
static int count=0;
void require(bool ok,const char* name){if(!ok)throw std::runtime_error(name);++count;std::cout<<"PASS "<<name<<"\n";}
void rejects(const std::string& source,const char* name,Limits limits=Limits()){
 bool rejected=false;try{Invocation i(Host(),limits);i.evaluate(source);}catch(const std::exception&){rejected=true;}require(rejected,name);
}
int main(){try{
 Invocation i;
 require(i.evaluate("SetScriptResult('hello');").text=="hello","string");
 require(i.evaluate("SetScriptOutput(2+3);").number==5,"number and output alias");
 require(i.evaluate("42;").type==Value::Undefined,"no implicit result");
 require(i.evaluate("SetScriptResult(null);").type==Value::Null,"null");
 require(i.evaluate("var shared=7;").type==Value::Undefined,"declaration");
 require(i.evaluate("SetScriptResult(shared);").number==7,"same invocation shares scope");
 require(i.evaluate("SetScriptResult(JSON.parse('{\"x\":[true,1]}'));").object.at("x").array.size()==2,"typed object array");
 require(i.evaluate("SetScriptResult('Gr\\u00fc\\u00dfe \\u4e16\\u754c \\ud83d\\ude00');").text==u8"Grüße 世界 😀","Unicode");
 require(i.evaluate("SetScriptResult('a\\0b');").text.size()==3,"embedded NUL value");
 require(i.evaluate("SetScriptResult(1);SetScriptResult(2);").number==2,"last setter wins");
 require(i.evaluate("SetScriptResult(eval('Math.sqrt(81)'));").number==9,"eval");
 require(i.evaluate("SetScriptResult(Function('return 4')());").number==4,"Function");
 require(i.evaluate("SetScriptResult(typeof std+typeof os+typeof require+typeof Java+typeof Packages+typeof fetch+typeof load);").text=="undefinedundefinedundefinedundefinedundefinedundefinedundefined","no privileged globals");
 rejects("SetScriptResult(shared);","fresh invocation isolation");
 rejects("accidental=2;","strict assignment");
 rejects("SetScriptResult(1/0);","infinity");
 rejects("SetScriptResult(9007199254740992);","unsafe integer");
 rejects("var x={};x.x=x;SetScriptResult(x);","cycle");
 rejects("SetScriptResult(function(){});","function result");
 rejects("SetScriptResult(new Date());","date explicit rejection");
 rejects("ETLForge.GetVar();","arity");
 rejects("ETLForge.ReadFile('arbitrary');","unbound host");
 rejects("SetScriptResult(1n);","BigInt");
 Limits fast;fast.deadlineMs=50;
 rejects("while(true){}","loop deadline",fast);
 rejects("eval('while(true){}');","eval deadline",fast);
 rejects("function f(){return f()}f();","stack limit");
 Limits small;small.heapBytes=4*1024*1024;
 rejects("var a=[];while(true)a.push(new Array(10000).fill(1));","heap limit",small);
 std::atomic<bool> cancelled(true);Limits cancel;cancel.cancelled=&cancelled;
 rejects("while(true){}","cancellation",cancel);
 int calls=0;Invocation host([&](const std::string& name,const std::vector<Value>& args){if(name=="GetVar"){++calls;require(args[0].text=="x","host argument conversion");}return Value::string("value");});
 require(host.evaluate("SetScriptResult(ETLForge.GetVar('$x'));").text=="value"&&calls==1,"host return conversion");
 Invocation throwing([](const std::string&,const std::vector<Value>&)->Value{throw std::runtime_error("callback failure");});
 bool caught=false;try{throwing.evaluate("ETLForge.GetVar('$x');");}catch(const std::exception& e){caught=std::string(e.what()).find("callback failure")!=std::string::npos;}require(caught,"host exception boundary");
 Invocation::validate("while(true){};ETLForge.WriteFile('x','y');");require(true,"compile only has no effects");
 bool syntax=false;try{Invocation::validate("var = ;");}catch(...){syntax=true;}require(syntax,"compile syntax rejection");
 auto blocks=parseBlocks("before<trans>1</trans>between<javascript>SetScriptResult('</javascript>');</javascript>after");
 require(blocks.size()==2&&blocks[0].kind==Block::Trans&&blocks[1].kind==Block::JavaScript,"mixed blocks and quoted delimiter");
 require(parseBlocks("<javascript>/* </javascript> */ // </javascript>\nSetScriptResult(/<\\/javascript>/.test('x'));</javascript>").size()==1,"comment and regex delimiters");
 require(parseBlocks("<javascript>if(true) /<\\/javascript>/.test('x');</javascript>").size()==1,"regex after control parenthesis");
 require(parseBlocks("<javascript>var x=10/2;SetScriptResult(x);</javascript>").size()==1,"division token");
 require(parseBlocks("<TRANS>unchanged</TRANS>").empty(),"uppercase literal preserved");
 bool mismatch=false;try{parseBlocks("<javascript>1</trans>");}catch(...){mismatch=true;}require(mismatch,"mismatched tag rejected");
 bool nested=false;try{parseBlocks("<javascript><trans>1</trans></javascript>");}catch(...){nested=true;}require(nested,"nested tag rejected");
 require(globalIdentifiers("$global=1; '$quoted'; /*$comment*/ $other.x").size()==2,"lexical global identifiers");
 std::map<std::string,Value> store;Value start;start.type=Value::Number;start.number=2;store["counter"]=start;
 Host globals=[&](const std::string& name,const std::vector<Value>& args){if(name=="GetVar")return store[args[0].text];store[args[0].text]=args[1];return args[1];};
 Invocation mixed(globals);require(mixed.evaluate("$counter+=3;SetScriptResult($counter);").number==5&&store["counter"].number==5,"typed dollar global accessor");
 require(mixed.evaluate("ETLForge.SetVar('obj',{nested:{value:1}});").type==Value::Undefined,"global object assignment");
 mixed.evaluate("ETLForge.GetVar('obj').nested.value=3;");require(store["obj"].object["nested"].object["value"].number==3,"nested global mutation");
 Invocation failure(globals);try{failure.evaluate("$counter=99;throw Error('fail');");}catch(...){}require(store["counter"].number==5,"block failure rolls back staged globals");
 rejects("SetScriptResult('\\ud800');","unpaired surrogate rejected");
 require(parseBlocks("<javascript>var ratio={x:4}.x/2;SetScriptResult(ratio);</javascript>").size()==1,"object literal division delimiter");
 require(parseBlocks("<javascript>var f=function(){return 1;}/2;SetScriptResult(f);</javascript>").size()==1,"function expression division delimiter");
 require(parseBlocks("<javascript>var regex=/[//</javascript>]/;SetScriptResult(1);</javascript>").size()==1,"regular expression character class delimiter");
 require(sourcePathLiterals("ETLForge.GetSource('rows$row$name$'); /* ETLForge.GetSource(no) */").size()==1,"literal source binding metadata");
 require(globalIdentifiers("// comment\xe2\x80\xa8 $unicode=1;").count("$unicode")==1,"Unicode line separator ends comments");
 require(sourcePathLiterals("// comment\xe2\x80\xa9 ETLForge.GetSource('id');").size()==1,"Unicode paragraph separator source metadata");
 bool dynamic=false;try{Invocation::validate("ETLForge.GetSource(path);");}catch(...){dynamic=true;}require(dynamic,"dynamic source path rejected");
 rejects("var $global=1;SetScriptResult($global);","dollar local declaration rejected");
 rejects("function f($parameter){return $parameter;}SetScriptResult(f(1));","dollar parameter rejected");
 rejects("Promise.resolve(1);","Promise unavailable");
 rejects("SetScriptResult((async function(){return 1;})());","async result rejected");
 rejects("var o={};Object.defineProperty(o,'x',{enumerable:true,get:function(){return 1;}});SetScriptResult(o);","object accessor transfer rejected");
 rejects("var a=[1];Object.defineProperty(a,'0',{get:function(){return 2;}});SetScriptResult(a);","array accessor transfer rejected");
 rejects("ETLForge.ReadFile('s.id','../private.txt');","file override traversal rejected");
 rejects("ETLForge.WriteFile('t.id','payload','C:\\\\private.txt');","absolute file override rejected");
 rejects("ETLForge.GetVar(Array(2000).join('x'));","global name length bounded");
 int writes=0;Value nativeCounter;nativeCounter.type=Value::Number;nativeCounter.number=5;
 Invocation readonly([&](const std::string& name,const std::vector<Value>& args){if(name=="GetVar")return nativeCounter;if(name=="SetVar"){writes++;nativeCounter=args[1];return args[1];}if(name=="ReadFile"){nativeCounter.number=7;return Value::string("file");}throw std::runtime_error("unexpected callback");});
 require(readonly.evaluate("ETLForge.GetVar('counter');ETLForge.ReadFile('s.fixture');SetScriptResult(ETLForge.GetVar('counter'));").number==7&&writes==0,"native side effects refresh read-only globals without rewriting them");
 bool overlaySeen=false;nativeCounter.number=5;
 Invocation overlay([&](const std::string& name,const std::vector<Value>& args){
  if(name=="GetVar")return nativeCounter;
  if(name=="SetVar"){nativeCounter=args[1];return args[1];}
  if(name=="__ConnectorOverlay"){
   overlaySeen=nativeCounter.number==5&&args[2].object.at("counter").number==9;
   Value result;result.type=Value::Object;result.object["result"]=Value::string("overlay");result.object["changes"].type=Value::Object;return result;
  }throw std::runtime_error("unexpected callback");
 });
 require(overlay.evaluate("ETLForge.SetVar('counter',9);ETLForge.ReadFile('s.fixture');SetScriptResult(ETLForge.GetVar('counter'));").number==9&&nativeCounter.number==9&&overlaySeen,"connector receives uncommitted global overlay");
 require(Invocation().evaluate("SetScriptResult(({value:4}).value/2);").number==2,"object division evaluation");
 require(Invocation().evaluate("var f=function(){return 1;}/2;SetScriptResult(isNaN(f));").boolean,"function expression division evaluation");
 for(int n=0;n<100;n++){Invocation fresh;require(fresh.evaluate("var isolated=0;isolated++;SetScriptResult(isolated);").number==1,"cached bytecode fresh realm");}
 std::cout<<"Passed "<<count<<" checks\n";return 0;
 }catch(const std::exception& e){std::cerr<<"FAIL "<<e.what()<<"\n";return 1;}}
