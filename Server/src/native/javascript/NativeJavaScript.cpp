#include "NativeJavaScript.h"
#include "ScriptBlocks.h"
#include "JavaScriptPolicy.generated.h"
#include <quickjs.h>
#include <chrono>
#include <stdexcept>
#include <cmath>
#include <set>
#include <cstring>
#include <deque>

namespace etlforge { namespace javascript {
static_assert(Limits{}.heapBytes==policy::heapBytes&&Limits{}.stackBytes==policy::stackBytes&&Limits{}.valueBytes==policy::valueBytes&&Limits{}.deadlineMs==policy::deadlineMs,"JavaScript defaults must match the policy manifest");
namespace {
bool same(const Value& a,const Value& b){
    if(a.type!=b.type)return false;
    switch(a.type){
    case Value::Boolean:return a.boolean==b.boolean;
    case Value::Number:return a.number==b.number;
    case Value::String:return a.text==b.text;
    case Value::Array:if(a.array.size()!=b.array.size())return false;for(size_t i=0;i<a.array.size();i++)if(!same(a.array[i],b.array[i]))return false;return true;
    case Value::Object:if(a.object.size()!=b.object.size())return false;for(const auto& item:a.object){auto found=b.object.find(item.first);if(found==b.object.end()||!same(item.second,found->second))return false;}return true;
    default:return true;
    }
}
void checkOperationText(const Value& value){
    if(value.type==Value::String&&value.text.find('\0')!=std::string::npos)throw std::runtime_error("Operation globals cannot contain NUL text");
    for(const auto& item:value.array)checkOperationText(item);
    for(const auto& item:value.object)checkOperationText(item.second);
}
struct CachedCode {std::string source;std::vector<uint8_t> bytes;std::set<std::string> globals;bool known=false;};
thread_local std::deque<CachedCode> codeCache;
JSValue compile(JSContext* ctx,const std::string& source,const std::string& filename){
    for(const auto& item:codeCache)if(item.source==source)return JS_ReadObject(ctx,item.bytes.data(),item.bytes.size(),JS_READ_OBJ_BYTECODE);
    JSValue value=JS_Eval(ctx,source.c_str(),source.size(),filename.c_str(),JS_EVAL_TYPE_GLOBAL|JS_EVAL_FLAG_STRICT|JS_EVAL_FLAG_COMPILE_ONLY);
    if(JS_IsException(value))return value;
    size_t n=0;uint8_t* data=JS_WriteObject(ctx,&n,value,JS_WRITE_OBJ_BYTECODE);
    if(data){
        if(n<=128*1024&&source.size()<=128*1024){
            if(codeCache.size()>=32)codeCache.pop_front();
            codeCache.push_back(CachedCode{source,std::vector<uint8_t>(data,data+n),{},false});
        }
        js_free(ctx,data);
    }else {JSValue e=JS_GetException(ctx);JS_FreeValue(ctx,e);}
    return value;
}
struct Owned {
    JSContext* ctx; JSValue value;
    Owned(JSContext* c, JSValue v):ctx(c),value(v){}
    ~Owned(){JS_FreeValue(ctx,value);}
    Owned(const Owned&)=delete;
};
std::string stringValue(JSContext* ctx, JSValueConst v) {
    if(JS_IsString(v)){
        size_t units=0;const uint16_t* u=JS_ToCStringLenUTF16(ctx,&units,v);
        if(!u)throw std::runtime_error("JavaScript UTF-16 conversion failed");
        bool valid=true;
        for(size_t i=0;i<units;i++){
            if(u[i]>=0xd800&&u[i]<=0xdbff){if(i+1>=units||u[i+1]<0xdc00||u[i+1]>0xdfff){valid=false;break;}i++;}
            else if(u[i]>=0xdc00&&u[i]<=0xdfff){valid=false;break;}
        }
        JS_FreeCStringUTF16(ctx,u);
        if(!valid)throw std::runtime_error("JavaScript text contains an unpaired Unicode surrogate");
    }
    size_t n=0; const char* p=JS_ToCStringLen(ctx,&n,v);
    if(!p) throw std::runtime_error("JavaScript string conversion failed");
    std::string s(p,n); JS_FreeCString(ctx,p); return s;
}
void check(JSContext* ctx, JSValueConst v) {
    if(JS_IsException(v)) {
        Owned e(ctx,JS_GetException(ctx));
        std::string message=stringValue(ctx,e.value);
        Owned stack(ctx,JS_GetPropertyStr(ctx,e.value,"stack"));
        if(JS_IsString(stack.value)) message += "\n"+stringValue(ctx,stack.value);
        throw std::runtime_error(message);
    }
}
bool utf8(const std::string& s) {
    size_t i=0;
    while(i<s.size()) {
        unsigned c=(unsigned char)s[i++], n=0, cp=0, min=0;
        if(c<128) continue;
        if(c>=194&&c<=223){n=1;cp=c&31;min=128;}
        else if(c>=224&&c<=239){n=2;cp=c&15;min=2048;}
        else if(c>=240&&c<=244){n=3;cp=c&7;min=65536;}
        else return false;
        if(i+n>s.size()) return false;
        while(n--){unsigned b=(unsigned char)s[i++];if((b&192)!=128)return false;cp=(cp<<6)|(b&63);}
        if(cp<min||cp>0x10ffff||(cp>=0xd800&&cp<=0xdfff)) return false;
    }
    return true;
}
}
struct Invocation::Impl {
    JSRuntime* runtime=nullptr;
    JSContext* ctx=nullptr;
    Limits limits; Host host;
    Value result;
    JSValue objectPrototype=JS_UNDEFINED;
    std::map<std::string,JSValue> globals;
    std::map<std::string,Value> snapshots;
    std::set<std::string> dirty;
    std::vector<std::string> identifiers;
    size_t globalNameBytes=0;
    bool failed=false;
    std::chrono::steady_clock::time_point deadline;
    size_t bytes=0, nodes=0;
    explicit Impl(Host h,Limits l):limits(l),host(std::move(h)) {
        deadline=std::chrono::steady_clock::now()+std::chrono::milliseconds(l.deadlineMs);
        runtime=JS_NewRuntime();
        if(!runtime)throw std::runtime_error("Cannot allocate JavaScript runtime");
        JS_SetMemoryLimit(runtime,l.heapBytes);
        JS_SetMaxStackSize(runtime,l.stackBytes);
        JS_SetInterruptHandler(runtime,interrupt,this);
        ctx=JS_NewContextRaw(runtime);
        if(!ctx){JS_FreeRuntime(runtime);runtime=nullptr;throw std::runtime_error("Cannot allocate JavaScript context");}
        if(JS_AddIntrinsicBaseObjects(ctx)||JS_AddIntrinsicDate(ctx)||JS_AddIntrinsicEval(ctx)||JS_AddIntrinsicRegExp(ctx)||JS_AddIntrinsicJSON(ctx)){
            JS_FreeContext(ctx);JS_FreeRuntime(runtime);ctx=nullptr;runtime=nullptr;throw std::runtime_error("Cannot initialize JavaScript intrinsics");
        }
        JS_SetContextOpaque(ctx,this);
        try {
            Owned global(ctx,JS_GetGlobalObject(ctx));
            Owned obj(ctx,JS_GetPropertyStr(ctx,global.value,"Object"));
            objectPrototype=JS_GetPropertyStr(ctx,obj.value,"prototype");
            Owned api(ctx,JS_NewObjectProto(ctx,JS_NULL));
            for(int i=0;i<10;i++){
                const auto& definition=policy::Hosts[i];
                JSValue function=JS_NewCFunctionMagic(ctx,call,definition.name,definition.minimum,JS_CFUNC_generic_magic,i);
                int status=definition.api?JS_SetPropertyStr(ctx,api.value,definition.name,function):JS_DefinePropertyValueStr(ctx,global.value,definition.name,function,0);
                if(status<0)throw std::runtime_error("Cannot install JavaScript host policy");
            }
            JS_DefinePropertyValueStr(ctx,global.value,"ETLForge",JS_DupValue(ctx,api.value),0);
        } catch(...) { JS_FreeValue(ctx,objectPrototype);JS_FreeContext(ctx);JS_FreeRuntime(runtime);throw; }
    }
    ~Impl(){if(ctx){for(auto& item:globals)JS_FreeValue(ctx,item.second);JS_FreeValue(ctx,objectPrototype);JS_FreeContext(ctx);}if(runtime)JS_FreeRuntime(runtime);}
    static int interrupt(JSRuntime*,void* p) {
        Impl* s=static_cast<Impl*>(p);
        return (s->limits.cancelled&&s->limits.cancelled->load())||std::chrono::steady_clock::now()>=s->deadline;
    }
    void budget(size_t n) {
        if(++nodes>100000||n>limits.valueBytes-bytes)throw std::runtime_error("JavaScript value limit exceeded");
        bytes+=n;
        if(interrupt(runtime,this))throw std::runtime_error("JavaScript cancelled or deadline exceeded");
    }
    Value read(JSValueConst v,unsigned depth=0,std::set<void*>* parents=nullptr) {
        if(depth>64)throw std::runtime_error("JavaScript value nesting limit exceeded");
        budget(1); Value out;
        if(JS_IsUndefined(v))return out;
        if(JS_IsNull(v)){out.type=Value::Null;return out;}
        if(JS_IsBool(v)){out.type=Value::Boolean;out.boolean=JS_ToBool(ctx,v)!=0;return out;}
        if(JS_IsNumber(v)){
            out.type=Value::Number;JS_ToFloat64(ctx,&out.number,v);
            if(!std::isfinite(out.number)||(std::floor(out.number)==out.number&&std::abs(out.number)>9007199254740991.0))throw std::runtime_error("JavaScript number is non-finite or outside safe integer range");
            return out;
        }
        if(JS_IsString(v)){out=Value::string(stringValue(ctx,v));budget(out.text.size());return out;}
        if(!JS_IsObject(v)||JS_IsFunction(ctx,v))throw std::runtime_error("Unsupported JavaScript value type");
        std::set<void*> local; if(!parents)parents=&local;
        void* ptr=JS_VALUE_GET_PTR(v);
        if(!parents->insert(ptr).second)throw std::runtime_error("Cyclic JavaScript value");
        struct Remove {std::set<void*>* p;void* v;~Remove(){p->erase(v);}} remove{parents,ptr};
        if(JS_IsArray(v)) {
            out.type=Value::Array;Owned length(ctx,JS_GetPropertyStr(ctx,v,"length"));check(ctx,length.value);
            uint32_t n=0;if(JS_ToUint32(ctx,&n,length.value)<0||n>100000)throw std::runtime_error("JavaScript array limit exceeded");
            for(uint32_t i=0;i<n;i++){
                JSAtom atom=JS_NewAtomUInt32(ctx,i);
                try{out.array.push_back(readOwn(v,atom,depth+1,parents));}catch(...){JS_FreeAtom(ctx,atom);throw;}
                JS_FreeAtom(ctx,atom);
            }
        } else {
            Owned proto(ctx,JS_GetPrototype(ctx,v));check(ctx,proto.value);
            if(!JS_IsNull(proto.value)&&JS_VALUE_GET_PTR(proto.value)!=JS_VALUE_GET_PTR(objectPrototype))throw std::runtime_error("Only plain objects may cross the JavaScript boundary");
            out.type=Value::Object;JSPropertyEnum* props=nullptr;uint32_t n=0;
            if(JS_GetOwnPropertyNames(ctx,&props,&n,v,JS_GPN_STRING_MASK|JS_GPN_ENUM_ONLY)<0)throw std::runtime_error("JavaScript property enumeration failed");
            struct FreeProps{JSContext* c;JSPropertyEnum* p;uint32_t n;~FreeProps(){for(uint32_t i=0;i<n;i++)JS_FreeAtom(c,p[i].atom);js_free(c,p);}} freeProps{ctx,props,n};
            for(uint32_t i=0;i<n;i++){
                Owned name(ctx,JS_AtomToString(ctx,props[i].atom));auto key=stringValue(ctx,name.value);budget(key.size());
                if(key.find('\0')!=std::string::npos)throw std::runtime_error("Object keys cannot contain NUL");
                out.object.emplace(key,readOwn(v,props[i].atom,depth+1,parents));
            }
        }
        return out;
    }
    Value readOwn(JSValueConst object,JSAtom atom,unsigned depth,std::set<void*>* parents){
        JSPropertyDescriptor descriptor;
        int status=JS_GetOwnProperty(ctx,&descriptor,object,atom);
        if(status<0)throw std::runtime_error("Cannot inspect JavaScript property");
        if(!status)return Value();
        Owned value(ctx,descriptor.value),getter(ctx,descriptor.getter),setter(ctx,descriptor.setter);
        if(!JS_IsUndefined(getter.value)||!JS_IsUndefined(setter.value))throw std::runtime_error("Accessor properties cannot cross the JavaScript boundary");
        return read(value.value,depth,parents);
    }
    JSValue write(const Value& v,unsigned depth=0) {
        if(depth>64)throw std::runtime_error("Host value nesting limit exceeded");budget(1);
        switch(v.type){
        case Value::Undefined:return JS_UNDEFINED;
        case Value::Null:return JS_NULL;
        case Value::Boolean:return JS_NewBool(ctx,v.boolean);
        case Value::Number:if(!std::isfinite(v.number)|| (std::floor(v.number)==v.number&&std::abs(v.number)>9007199254740991.0))throw std::runtime_error("Host number outside JavaScript safe range");return JS_NewFloat64(ctx,v.number);
        case Value::String:budget(v.text.size());if(!utf8(v.text))throw std::runtime_error("Host text is not UTF-8");return JS_NewStringLen(ctx,v.text.data(),v.text.size());
        default:break;
        }
        Owned obj(ctx,v.type==Value::Array?JS_NewArray(ctx):JS_NewObjectProto(ctx,JS_NULL));check(ctx,obj.value);
        if(v.type==Value::Array){for(size_t i=0;i<v.array.size();i++)if(JS_SetPropertyUint32(ctx,obj.value,(uint32_t)i,write(v.array[i],depth+1))<0)throw std::runtime_error("Cannot set JavaScript array element");}
        else {for(const auto& item:v.object){if(item.first.find('\0')!=std::string::npos||!utf8(item.first))throw std::runtime_error("Invalid host property name");if(JS_SetPropertyStr(ctx,obj.value,item.first.c_str(),write(item.second,depth+1))<0)throw std::runtime_error("Cannot set JavaScript object property");}}
        return JS_DupValue(ctx,obj.value);
    }
    static JSValue call(JSContext* c,JSValueConst,int argc,JSValueConst* argv,int magic) {
        Impl* s=static_cast<Impl*>(JS_GetContextOpaque(c));
        try {
            s->bytes=0;s->nodes=0;
            if(magic==7||magic==8){if(argc!=1)throw std::runtime_error("Result setter requires one argument");s->result=s->read(argv[0]);return JS_UNDEFINED;}
            const auto& definition=policy::Hosts[magic];
            if(argc<definition.minimum||argc>definition.maximum)throw std::runtime_error("Invalid host function argument count");
            std::vector<Value> args;for(int i=0;i<argc;i++)args.push_back(s->read(argv[i]));
            if(magic==0||magic==1||magic==2||magic==3||magic==9){
                if(args[0].type!=Value::String)throw std::runtime_error("Connector/source reference must be a string");
                if((magic==2||magic==3)&&args[1].type!=Value::String)throw std::runtime_error("SQL must be a string");
                if(magic==0&&argc==2&&args[1].type!=Value::String)throw std::runtime_error("File name must be a string");
                if(magic==1&&argc==3&&args[2].type!=Value::String)throw std::runtime_error("File name must be a string");
                if((magic==0&&argc==2)||(magic==1&&argc==3)){
                    const auto& file=args[magic==0?1:2].text;
                    if(file.find_first_of("/\\:\0",0,4)!=std::string::npos||file=="..")throw std::runtime_error("Override file name must be a file name inside the configured connector directory");
                }
            }
            if(!s->host)throw std::runtime_error("Host function unavailable in this context");
            if(magic==4||magic==5){
                if(args[0].type!=Value::String||args[0].text.empty())throw std::runtime_error("Global variable name must be a nonempty string");
                std::string name=args[0].text;if(name[0]=='$')name.erase(0,1);
                if(name.empty())throw std::runtime_error("Global variable name is empty");
                if(name.size()>1024)throw std::runtime_error("Global variable name exceeds 1024 bytes");
                if(!s->globals.count(name)){
                    if(s->globals.size()>=256||name.size()>65536-s->globalNameBytes)throw std::runtime_error("JavaScript global access limit exceeded");
                    s->globalNameBytes+=name.size();
                }
                if(magic==5){auto found=s->globals.find(name);if(found!=s->globals.end())JS_FreeValue(c,found->second);s->globals[name]=JS_DupValue(c,argv[1]);s->dirty.insert(name);return JS_DupValue(c,argv[1]);}
                auto found=s->globals.find(name);
                if(found==s->globals.end()){Value initial=s->host("GetVar",{Value::string(name)});JSValue value=s->write(initial);check(c,value);s->snapshots[name]=std::move(initial);s->globals[name]=value;found=s->globals.find(name);}
                return JS_DupValue(c,found->second);
            }
            Value returned;
            if(magic<=3||magic==6){
                auto overlay=s->changedGlobals();
                if(!overlay.empty()){
                    Value arguments;arguments.type=Value::Array;arguments.array=std::move(args);
                    Value changes;changes.type=Value::Object;changes.object=std::move(overlay);
                    Value packet=s->host("__ConnectorOverlay",{Value::string(definition.name),arguments,changes});
                    if(packet.type!=Value::Object||!packet.object.count("result")||!packet.object.count("changes"))throw std::runtime_error("Invalid native connector overlay result");
                    for(const auto& item:packet.object.at("changes").object){
                        auto found=s->globals.find(item.first);if(found!=s->globals.end())JS_FreeValue(c,found->second);
                        s->globals[item.first]=s->write(item.second);s->dirty.insert(item.first);
                    }
                    returned=std::move(packet.object.at("result"));
                }else returned=s->host(definition.name,args);
                s->invalidateUnchanged();
            }else returned=s->host(definition.name,args);
            return s->write(returned);
        }catch(const std::exception& e){return JS_ThrowInternalError(c,"%s",e.what());}
        catch(...){return JS_ThrowInternalError(c,"Native host callback failed");}
    }
    static JSValue access(JSContext* c,JSValueConst,int argc,JSValueConst* argv,int magic){
        Impl* s=static_cast<Impl*>(JS_GetContextOpaque(c));
        JSValue name=JS_NewString(c,s->identifiers.at(magic).c_str());
        JSValue args[]={name,argc?argv[0]:JS_UNDEFINED};
        JSValue ret=call(c,JS_UNDEFINED,argc?2:1,args,argc?5:4);JS_FreeValue(c,name);return ret;
    }
    void bindGlobals(const std::string& source){
        Owned global(ctx,JS_GetGlobalObject(ctx));
        const std::set<std::string>* names=nullptr;std::set<std::string> uncached;
        for(auto& item:codeCache)if(item.source==source){if(!item.known){item.globals=globalIdentifiers(source);item.known=true;}names=&item.globals;break;}
        if(!names){uncached=globalIdentifiers(source);names=&uncached;}
        for(const auto& name:*names){
            bool exists=false;for(const auto& old:identifiers)if(old==name)exists=true;if(exists)continue;
            int id=(int)identifiers.size();identifiers.push_back(name);
            JSAtom atom=JS_NewAtom(ctx,name.c_str());
            int status=JS_DefinePropertyGetSet(ctx,global.value,atom,JS_NewCFunctionMagic(ctx,access,"globalRead",0,JS_CFUNC_generic_magic,id),JS_NewCFunctionMagic(ctx,access,"globalWrite",1,JS_CFUNC_generic_magic,id),0);
            JS_FreeAtom(ctx,atom);if(status<0)throw std::runtime_error("Cannot bind operation global");
        }
    }
    void commit(){
        bytes=0;nodes=0;
        auto values=changedGlobals();
        for(const auto& item:values)checkOperationText(item.second);
        for(const auto& item:values)host("SetVar",{Value::string(item.first),item.second});
        for(auto& item:values)snapshots[item.first]=std::move(item.second);
        dirty.clear();
    }
    std::map<std::string,Value> changedGlobals(){
        std::map<std::string,Value> values;
        for(const auto& item:globals){Value current=read(item.second);auto previous=snapshots.find(item.first);if(dirty.count(item.first)||previous==snapshots.end()||!same(current,previous->second)){dirty.insert(item.first);values.emplace(item.first,std::move(current));}}
        return values;
    }
    void invalidateUnchanged(){
        for(auto it=globals.begin();it!=globals.end();){
            auto previous=snapshots.find(it->first);
            if(!dirty.count(it->first)&&previous!=snapshots.end()){
                globalNameBytes-=it->first.size();snapshots.erase(previous);JS_FreeValue(ctx,it->second);it=globals.erase(it);
            }else ++it;
        }
    }
};
Invocation::Invocation(Host h,Limits l):impl(new Impl(std::move(h),l)){}
Invocation::~Invocation()=default;
Value Invocation::evaluate(const std::string& source,const std::string& filename) {
    if(impl->failed)throw std::runtime_error("JavaScript invocation already failed");
    if(source.size()>16*1024*1024||!utf8(source)||source.find('\0')!=std::string::npos)throw std::runtime_error("JavaScript source must be UTF-8 without NUL and at most 16 MiB");
    impl->result=Value();
    try {
        impl->bindGlobals(source);
        Owned compiled(impl->ctx,compile(impl->ctx,source,filename));check(impl->ctx,compiled.value);
        Owned evaluated(impl->ctx,JS_EvalFunction(impl->ctx,JS_DupValue(impl->ctx,compiled.value)));check(impl->ctx,evaluated.value);
        if(JS_IsPromise(evaluated.value)||JS_IsJobPending(impl->runtime))throw std::runtime_error("Asynchronous JavaScript is unsupported");
        impl->commit();
        return impl->result;
    }catch(...){impl->failed=true;throw;}
}
void Invocation::validate(const std::string& source) {
    validateSyntax(source);
    globalIdentifiers(source);
    sourcePathLiterals(source);
}
void Invocation::validateSyntax(const std::string& source) {
    Invocation invocation;
    if(source.size()>16*1024*1024||!utf8(source)||source.find('\0')!=std::string::npos)throw std::runtime_error("JavaScript source must be UTF-8 without NUL and at most 16 MiB");
    Owned compiled(invocation.impl->ctx,compile(invocation.impl->ctx,source,"javascript-block"));check(invocation.impl->ctx,compiled.value);
}
void Invocation::refreshGlobals(){for(auto& item:impl->globals)JS_FreeValue(impl->ctx,item.second);impl->globals.clear();impl->snapshots.clear();impl->dirty.clear();impl->globalNameBytes=0;}
} }
