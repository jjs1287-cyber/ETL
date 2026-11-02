#include "ScriptBlocks.h"
#include "NativeJavaScript.h"
#include <stdexcept>
#include <cctype>
#include <cstring>
#include <chrono>

namespace etlforge { namespace javascript {
namespace {
bool at(const std::string& s,size_t p,const char* value){return s.compare(p,std::char_traits<char>::length(value),value)==0;}
bool first(unsigned char c){return std::isalpha(c)||c=='_'||c=='$'||c>=128;}
bool next(unsigned char c){return first(c)||std::isdigit(c);}
size_t spaceBytes(const std::string& s,size_t p){
    if(std::isspace((unsigned char)s[p]))return 1;
    if(s.compare(p,2,"\xc2\xa0")==0)return 2;
    if(s.compare(p,3,"\xef\xbb\xbf")==0||s.compare(p,3,"\xe1\x9a\x80")==0||s.compare(p,3,"\xe2\x81\x9f")==0||s.compare(p,3,"\xe3\x80\x80")==0)return 3;
    if(p+2<s.size()&&(unsigned char)s[p]==0xe2&&(unsigned char)s[p+1]==0x80){unsigned b=(unsigned char)s[p+2];if((b>=0x80&&b<=0x8a)||b==0xa8||b==0xa9||b==0xaf)return 3;}
    return 0;
}
bool lineEnd(const std::string& s,size_t p){return s[p]=='\r'||s[p]=='\n'||s.compare(p,3,"\xe2\x80\xa8")==0||s.compare(p,3,"\xe2\x80\xa9")==0;}
[[noreturn]] void error(const std::string& s,size_t p,const char* msg){
    size_t line=1,col=1;for(size_t i=0;i<p;i++){if(s[i]=='\n'){line++;col=1;}else col++;}
    throw std::runtime_error(std::string(msg)+" at line "+std::to_string(line)+", column "+std::to_string(col));
}
size_t scan(const std::string& s,size_t p,bool js,bool block,std::set<std::string>* globals,std::vector<std::string>* tokens=nullptr){
    bool expression=true, control=false,statement=true,pendingFunction=false,functionExpression=false;
    std::vector<bool> parens;
    std::vector<int> braces;
    while(p<s.size()){
        if(tokens&&tokens->size()>100000)error(s,p,"JavaScript token limit exceeded");
        char c=s[p];
        if(block&&(at(s,p,"</javascript>")||at(s,p,"</trans>"))){
            if((js&&at(s,p,"</javascript>"))||(!js&&at(s,p,"</trans>")))return p;
            error(s,p,"Mismatched script closing tag");
        }
        if(at(s,p,"<javascript>")||at(s,p,"<trans>"))error(s,p,"Nested script tags are unsupported");
        if(size_t spaces=spaceBytes(s,p)){p+=spaces;continue;}
        if(c=='\''||c=='"'){
            const char quote=c;size_t begin=p++;bool closed=false;
            while(p<s.size()){
                if(s[p]=='\\'){p+=2;continue;}
                if(s[p]==quote){p++;closed=true;break;}
                if(js&&(s[p]=='\r'||s[p]=='\n'))error(s,p,"Unterminated JavaScript string");p++;
            }
            if(!closed)error(s,begin,"Unterminated script string");if(tokens)tokens->push_back(s.substr(begin,p-begin));expression=false;continue;
        }
        if(js&&c=='`')error(s,p,"Template literals are outside the initial JavaScript profile; use quoted strings");
        if(c=='/'&&p+1<s.size()&&s[p+1]=='/'){p+=2;while(p<s.size()&&!lineEnd(s,p))p++;continue;}
        if(c=='/'&&p+1<s.size()&&s[p+1]=='*'){
            size_t begin=p;p=s.find("*/",p+2);if(p==std::string::npos)error(s,begin,"Unterminated block comment");p+=2;continue;
        }
        if(js&&c=='/'&&expression){
            size_t begin=p++;bool klass=false,closed=false;
            while(p<s.size()){
                char r=s[p++];if(r=='\\'){p++;continue;}
                if(r=='\n'||r=='\r'||lineEnd(s,p-1))error(s,begin,"Unterminated JavaScript regular expression");
                if(r=='[')klass=true;else if(r==']')klass=false;
                else if(r=='/'&&!klass){closed=true;break;}
            }
            if(!closed)error(s,begin,"Unterminated JavaScript regular expression");
            while(p<s.size()&&first((unsigned char)s[p])&&!spaceBytes(s,p))p++;expression=false;continue;
        }
        if(first((unsigned char)c)){
            size_t begin=p++;while(p<s.size()&&next((unsigned char)s[p])&&!spaceBytes(s,p))p++;
            const std::string word=s.substr(begin,p-begin);
            if(tokens)tokens->push_back(word);
            if(globals&&word.size()>1&&word[0]=='$'){
                if(word.size()>1025)throw std::runtime_error("Global variable name exceeds 1024 bytes");
                globals->insert(word);if(globals->size()>256)throw std::runtime_error("JavaScript global access limit exceeded");
            }
            if(word=="function"){pendingFunction=true;functionExpression=!statement;}
            control=word=="if"||word=="while"||word=="for"||word=="with"||word=="switch"||word=="catch";
            expression=control||word=="return"||word=="throw"||word=="case"||word=="delete"||word=="void"||word=="typeof"||word=="new"||word=="in"||word=="instanceof"||word=="else"||word=="do";
            statement=word=="else"||word=="do";
            continue;
        }
        if(std::isdigit((unsigned char)c)){
            p++;while(p<s.size()&&(std::isalnum((unsigned char)s[p])||s[p]=='.'||s[p]=='_'))p++;expression=false;continue;
        }
        if(tokens)tokens->push_back(std::string(1,c));
        if(c=='('){parens.push_back(control);control=false;expression=true;statement=false;}
        else if(c==')'){expression=!parens.empty()&&parens.back();statement=expression;if(!parens.empty())parens.pop_back();}
        else if(c=='{'){int kind=pendingFunction?(functionExpression?2:0):(!statement&&expression?1:0);braces.push_back(kind);pendingFunction=false;expression=true;statement=kind==0;}
        else if(c==']')expression=false;
        else if(c=='}'){int kind=braces.empty()?0:braces.back();if(!braces.empty())braces.pop_back();expression=kind==0;statement=kind==0;}
        else if(c=='.')expression=false;
        else if((c=='+'||c=='-')&&p+1<s.size()&&s[p+1]==c){p++;}
        else {expression=true;statement=c==';';}
        p++;
    }
    if(block)error(s,s.size(),js?"Missing </javascript>":"Missing </trans>");return s.size();
}
}
std::vector<Block> parseBlocks(const std::string& source){
    if(source.size()>16*1024*1024)throw std::runtime_error("Script template exceeds 16 MiB");
    if(source.find('\0')!=std::string::npos)throw std::runtime_error("Script source cannot contain NUL");
    std::vector<Block> blocks;size_t p=0;
    auto templateDeadline=std::chrono::steady_clock::now()+std::chrono::seconds(5);
    while(p<source.size()){
        if(blocks.size()>=1024||std::chrono::steady_clock::now()>templateDeadline)error(source,p,"Script template validation limit exceeded");
        size_t t=source.find("<trans>",p),j=source.find("<javascript>",p);
        size_t begin=t<j?t:j;if(begin==std::string::npos)break;
        const bool js=begin==j;size_t body=begin+(js?12:7),close=std::string::npos;
        if(js){
            size_t candidate=body;unsigned attempts=0;
            auto deadline=std::chrono::steady_clock::now()+std::chrono::seconds(5);
            std::string lastError;
            while(candidate<source.size()){
                size_t a=source.find("</javascript>",candidate),b=source.find("</trans>",candidate);candidate=a<b?a:b;
                if(candidate==std::string::npos)break;
                if(++attempts>256||std::chrono::steady_clock::now()>deadline)error(source,begin,"Script delimiter validation limit exceeded");
                std::string prefix=source.substr(body,candidate-body);
                bool valid=false;
                try{Invocation::validateSyntax(prefix);valid=true;}catch(const std::exception& e){lastError=e.what();}
                if(valid){
                    bool inLineComment=false;
                    try{Invocation::validateSyntax(prefix+" @");inLineComment=true;}catch(const std::exception&){}
                    if(!inLineComment){if(candidate==b)error(source,candidate,"Mismatched script closing tag");close=candidate;break;}
                }
                candidate++;
            }
            if(close==std::string::npos){if(!lastError.empty())error(source,begin,(lastError+"; no valid </javascript> boundary").c_str());error(source,begin,"Missing </javascript>");}
        }else close=scan(source,body,false,true,nullptr);
        size_t end=close+(js?13:8);
        blocks.push_back(Block{js?Block::JavaScript:Block::Trans,begin,body,close,end});p=end;
    }
    return blocks;
}
std::set<std::string> globalIdentifiers(const std::string& source){
    std::set<std::string> names;if(source.find('$')==std::string::npos)return names;
    std::vector<std::string> tokens;scan(source,0,true,false,&names,&tokens);
    int depth=0,parameters=-1,declarationDepth=-1;bool pendingParameters=false,binding=false;
    for(size_t i=0;i<tokens.size();i++){
        const auto& token=tokens[i];
        if(token=="function"||token=="catch")pendingParameters=true;
        if(token=="var"||token=="let"||token=="const"){declarationDepth=depth;binding=true;continue;}
        if(token=="("){depth++;if(pendingParameters){parameters=depth;pendingParameters=false;}continue;}
        if(token==")"){if(parameters==depth)parameters=-1;depth--;if(depth<declarationDepth){declarationDepth=-1;binding=false;}continue;}
        if(token==";"){declarationDepth=-1;binding=false;continue;}
        if(token==","&&depth==declarationDepth){binding=true;continue;}
        if(token=="="&&depth==declarationDepth){binding=false;continue;}
        if(!token.empty()&&token[0]=='$'){
            if(parameters>=0||pendingParameters||binding||(i&&tokens[i-1]==","&&depth==0))throw std::runtime_error("Dollar-prefixed names are operation globals, not JavaScript local bindings; use ordinary local names");
            if(i+2<tokens.size()&&tokens[i+1]=="="&&tokens[i+2]==">")throw std::runtime_error("Dollar-prefixed arrow parameters are unsupported; use ordinary local names");
        }
        if(binding&&depth==declarationDepth&&!token.empty()&&first((unsigned char)token[0]))binding=false;
    }
    return names;
}
std::vector<std::string> sourcePathLiterals(const std::string& source){
    std::vector<std::string> tokens,result;scan(source,0,true,false,nullptr,&tokens);
    for(size_t i=0;i+3<tokens.size();i++)if(tokens[i]=="ETLForge"&&tokens[i+1]=="."&&tokens[i+2]=="GetSource"){
        if(i+5>=tokens.size()||tokens[i+3]!="("||tokens[i+4].empty()||(tokens[i+4][0]!='\''&&tokens[i+4][0]!='"')||tokens[i+5]!=")")throw std::runtime_error("ETLForge.GetSource requires a literal source path");
        result.push_back(tokens[i+4]);
    }
    return result;
}
std::vector<std::pair<std::string,std::string>> hostReferenceLiterals(const std::string& source){
    std::vector<std::string> tokens;std::vector<std::pair<std::string,std::string>> result;scan(source,0,true,false,nullptr,&tokens);
    for(size_t i=0;i+4<tokens.size();i++)if(tokens[i]=="ETLForge"&&tokens[i+1]=="."&&tokens[i+3]=="("&&!tokens[i+4].empty()&&(tokens[i+4][0]=='\''||tokens[i+4][0]=='"')){
        const auto& name=tokens[i+2];if(name=="ReadFile"||name=="WriteFile"||name=="DbExecute"||name=="DbLookup"||name=="GetVar"||name=="SetVar")result.emplace_back(name,tokens[i+4]);
    }
    return result;
}
} }
