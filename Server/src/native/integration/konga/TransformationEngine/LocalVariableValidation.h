#ifndef ETLFORGE_LOCAL_VARIABLE_VALIDATION_H
#define ETLFORGE_LOCAL_VARIABLE_VALIDATION_H
#include <konga/DataElement/Token.h>
#include <konga/DataElement/TransException.h>
#include <cctype>
namespace etlforge {
inline bool isScriptArgument(const std::string& name){
    if(name.size()<2 || name[0]!='_') return false;
    for(size_t i=1;i<name.size();++i) if(name[i]<'0'||name[i]>'9') return false;
    return true;
}
inline void validateLocalVariables(const std::vector<konga::Token*>& input, std::set<std::string>& defined){
    using konga::Token;
    std::vector<Token*> tokens;
    for(size_t i=0;i<input.size();++i)
        if(input[i]->getType()!=Token::WhiteSpaces && input[i]->getType()!=Token::Comment) tokens.push_back(input[i]);
    struct Pending {std::string name; int depth; Pending(const std::string& n,int d):name(n),depth(d){}};
    std::vector<Pending> pending;
    std::vector<bool> arguments;
    int depth=0;
    for(size_t i=0;i<tokens.size();++i){
        Token* token=tokens[i];
        Token::Type type=token->getType();
        if(type==Token::Func || type==Token::LP || type==Token::LBRACE || type==Token::LBRACKET){
            arguments.push_back(type==Token::Func && token->GetName()=="ArgumentList(");
            ++depth;
            continue;
        }
        const bool closing=type==Token::RP || type==Token::RBRACE || type==Token::RBRACKET;
        if(closing || type==Token::Comma || type==Token::Semicolon){
            for(size_t j=0;j<pending.size();){
                if(pending[j].depth>=depth){defined.insert(pending[j].name);pending.erase(pending.begin()+j);}
                else ++j;
            }
            if(closing){if(depth>0){--depth;arguments.pop_back();}}
        }
        if(type!=Token::LocalDE) continue;
        const bool argumentName=!arguments.empty() && arguments.back();
        const bool target=argumentName || (i+1<tokens.size() && tokens[i+1]->getType()==Token::Set);
        token->localWriteTarget=target;
        if(target){pending.push_back(Pending(token->GetName(),depth));}
        else if(!defined.count(token->GetName()) && !isScriptArgument(token->GetName())){
            throw konga::TokenSyntaxException("Syntax error: local variable " + token->GetName() + " has not been assigned before use " + token->source_location());
        }
    }
    for(size_t i=0;i<pending.size();++i) defined.insert(pending[i].name);
}
}
#endif
