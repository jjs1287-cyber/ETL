#include "NativeJavaScript.h"
#include "ScriptBlocks.h"
#include <jni.h>
#include <stdexcept>
#include <vector>
#include <Windows.h>
namespace {
jstring javaString(JNIEnv* env,const std::string& s){
    int n=MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,s.data(),(int)s.size(),nullptr,0);
    if(!n&&!s.empty())throw std::runtime_error("Invalid UTF-8 in JavaScript reference");
    std::wstring out(n,L'\0');if(n)MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,s.data(),(int)s.size(),&out[0],n);
    return env->NewString(reinterpret_cast<const jchar*>(out.data()),n);
}
void throwJava(JNIEnv* env,const std::string& message){
    try{
        jclass type=env->FindClass("java/lang/IllegalArgumentException");if(!type)return;
        jmethodID constructor=env->GetMethodID(type,"<init>","(Ljava/lang/String;)V");if(!constructor)return;
        jstring text=javaString(env,message);if(!text)return;
        jobject error=env->NewObject(type,constructor,text);if(error)env->Throw(static_cast<jthrowable>(error));
        env->DeleteLocalRef(text);if(error)env->DeleteLocalRef(error);env->DeleteLocalRef(type);
    }catch(...){if(!env->ExceptionCheck())env->ThrowNew(env->FindClass("java/lang/IllegalArgumentException"),"Native diagnostic is not valid UTF-8");}
}
}
extern "C" JNIEXPORT jobjectArray JNICALL Java_org_etlforge_integration_data_script_NativeJavaScript_referencesNative(JNIEnv* env,jclass,jbyteArray input){
    try{
        jsize length=env->GetArrayLength(input);if(length>16*1024*1024)throw std::runtime_error("Script source exceeds 16 MiB");
        std::string source(length,'\0');if(length)env->GetByteArrayRegion(input,0,length,reinterpret_cast<jbyte*>(&source[0]));if(env->ExceptionCheck())return nullptr;
        std::set<std::string> refs;
        for(const auto& block:etlforge::javascript::parseBlocks(source))if(block.kind==etlforge::javascript::Block::JavaScript){
            std::string code=source.substr(block.body,block.close-block.body);
            for(const auto& global:etlforge::javascript::globalIdentifiers(code))refs.insert("G:"+global);
            for(const auto& literal:etlforge::javascript::sourcePathLiterals(code)){
                etlforge::javascript::Invocation decoder;refs.insert("S:"+decoder.evaluate("SetScriptResult("+literal+");").text);
            }
            for(const auto& literal:etlforge::javascript::hostReferenceLiterals(code)){
                etlforge::javascript::Invocation decoder;std::string value=decoder.evaluate("SetScriptResult("+literal.second+");").text;
                if(literal.first=="GetVar"||literal.first=="SetVar")refs.insert("G:"+(value.empty()||value[0]!='$'?"$"+value:value));
                else refs.insert("P:"+literal.first+":"+value);
            }
        }
        jobjectArray array=env->NewObjectArray((jsize)refs.size(),env->FindClass("java/lang/String"),nullptr);if(!array)return nullptr;
        int i=0;for(const auto& ref:refs){jstring item=javaString(env,ref);if(!item)return nullptr;env->SetObjectArrayElement(array,i++,item);env->DeleteLocalRef(item);}return array;
    }catch(const std::exception& e){env->ThrowNew(env->FindClass("java/lang/IllegalArgumentException"),"Cannot extract JavaScript source/global references");return nullptr;}
    catch(...){env->ThrowNew(env->FindClass("java/lang/IllegalArgumentException"),"Native JavaScript reference extraction failed");return nullptr;}
}
extern "C" JNIEXPORT jintArray JNICALL Java_org_etlforge_integration_data_script_NativeJavaScript_inspectNative(JNIEnv* env,jclass,jbyteArray input){
    try{
        if(!input)throw std::runtime_error("Script source is missing");
        jsize length=env->GetArrayLength(input);if(length>16*1024*1024)throw std::runtime_error("Script source exceeds 16 MiB");
        std::string source((size_t)length,'\0');if(length)env->GetByteArrayRegion(input,0,length,reinterpret_cast<jbyte*>(&source[0]));if(env->ExceptionCheck())return nullptr;
        std::vector<jint> spans;
        for(const auto& block:etlforge::javascript::parseBlocks(source)){
            if(block.kind==etlforge::javascript::Block::JavaScript)etlforge::javascript::Invocation::validate(source.substr(block.body,block.close-block.body));
            spans.push_back(block.kind==etlforge::javascript::Block::JavaScript?1:0);
            spans.push_back((jint)block.start);spans.push_back((jint)block.body);spans.push_back((jint)block.close);spans.push_back((jint)block.end);
        }
        jintArray result=env->NewIntArray((jsize)spans.size());if(result&&!spans.empty())env->SetIntArrayRegion(result,0,(jsize)spans.size(),spans.data());return result;
    }catch(const std::exception& e){
        throwJava(env,e.what());return nullptr;
    }catch(...){env->ThrowNew(env->FindClass("java/lang/IllegalArgumentException"),"Native JavaScript validation failed");return nullptr;}
}
