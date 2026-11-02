#pragma once
#include "NativeJavaScript.h"
#include <konga/DataElement/DataElement.h>
#include <memory>
#include <stdexcept>
#include <cmath>
namespace etlforge { namespace javascript {
struct NativeValueBudget {size_t nodes=0,bytes=0;void consume(size_t n){if(++nodes>100000||n>16*1024*1024-bytes)throw std::runtime_error("Operation value exceeds JavaScript transfer limit");bytes+=n;}};
inline bool equalValues(const Value& a,const Value& b){
    if(a.type!=b.type)return false;
    if(a.type==Value::Boolean)return a.boolean==b.boolean;
    if(a.type==Value::Number)return a.number==b.number;
    if(a.type==Value::String)return a.text==b.text;
    if(a.array.size()!=b.array.size()||a.object.size()!=b.object.size())return false;
    for(size_t i=0;i<a.array.size();i++)if(!equalValues(a.array[i],b.array[i]))return false;
    for(const auto& item:a.object){auto found=b.object.find(item.first);if(found==b.object.end()||!equalValues(item.second,found->second))return false;}return true;
}
inline std::unique_ptr<konga::DataElement> cloneNative(konga::DataElement* source,unsigned depth,NativeValueBudget& budget){
    using konga::DataElement;
    if(depth>64)throw std::runtime_error("Native overlay nesting limit exceeded");budget.consume(1);
    if(source->getType()==DataElement::t_binary)throw std::runtime_error("Binary globals cannot be overlaid in JavaScript connector calls");
    if(source->getType()==DataElement::t_string){int err=0;budget.consume(source->GetStringLength(err));}
    if(source->getType()!=DataElement::t_collection&&source->getType()!=DataElement::t_map)return std::unique_ptr<DataElement>(new DataElement(*source));
    std::unique_ptr<DataElement> copy(new DataElement(source->getType()));copy->setNull(source->IsNull());
    if(source->IsNull())return copy;
    if(source->isCollection())for(size_t i=0;i<source->getDECollectionSize();i++)copy->addToCollection(cloneNative(source->getDECollectionOf(i),depth+1,budget).release());
    else {int err=0;auto map=source->GetMap(err),target=copy->GetMap(err);for(const auto& item:*map){budget.consume(item.first.size());(*target)[item.first]=cloneNative(item.second,depth+1,budget).release();}}
    return copy;
}
inline Value fromDataElement(konga::DataElement* de,unsigned depth=0,NativeValueBudget* budget=nullptr){
    NativeValueBudget local;if(!budget)budget=&local;budget->consume(1);
    if(depth>64)throw std::runtime_error("Operation value nesting limit exceeded");
    Value v;if(!de||de->IsNull()){v.type=Value::Null;return v;}int err=0;
    switch(de->getType()){
    case konga::DataElement::t_collection:
        v.type=Value::Array;
        if(de->getDECollectionSize()>100000)throw std::runtime_error("Operation array limit exceeded");
        for(size_t i=0;i<de->getDECollectionSize();i++)v.array.push_back(fromDataElement(de->getDECollectionOf(i),depth+1,budget));break;
    case konga::DataElement::t_map:{
        v.type=Value::Object;auto map=de->GetMap(err);
        if(map){if(map->size()>100000)throw std::runtime_error("Operation map limit exceeded");for(const auto& item:*map){budget->consume(item.first.size());v.object.emplace(item.first,fromDataElement(item.second,depth+1,budget));}}break;}
    case konga::DataElement::t_bool:case konga::DataElement::t_bit:v.type=Value::Boolean;v.boolean=de->GetBool(err);break;
    case konga::DataElement::t_int:case konga::DataElement::t_long:case konga::DataElement::t_float:case konga::DataElement::t_double:
        v.type=Value::Number;v.number=de->GetDouble(err);if(!std::isfinite(v.number)||(std::floor(v.number)==v.number&&std::abs(v.number)>9007199254740991.0))throw std::runtime_error("Operation number outside JavaScript safe range");break;
    case konga::DataElement::t_string:{budget->consume(de->GetStringLength(err));auto str=de->GetString(err);v=Value::string(std::string((const char*)str,str.GetLength()));break;}
    default:throw std::runtime_error("Operation value type requires explicit conversion before JavaScript");
    }
    if(err)throw std::runtime_error("Operation value conversion failed");return v;
}
inline std::unique_ptr<konga::DataElement> toDataElement(const Value& v,unsigned depth=0){
    using konga::DataElement;
    if(depth>64)throw std::runtime_error("JavaScript value nesting limit exceeded");
    std::unique_ptr<DataElement> de;
    switch(v.type){
    case Value::Undefined:case Value::Null:de.reset(new DataElement);de->setNull();break;
    case Value::Boolean:de.reset(new DataElement(DataElement::t_bool,(int)v.boolean));break;
    case Value::Number:de.reset(new DataElement(DataElement::t_double,v.number));break;
    case Value::String:if(v.text.find('\0')!=std::string::npos)throw std::runtime_error("Legacy operation text cannot contain NUL");de.reset(new DataElement(DataElement::t_string,v.text.c_str()));break;
    case Value::Array:de.reset(new DataElement(DataElement::t_collection));de->setNull(false);for(const auto& item:v.array)de->addToCollection(toDataElement(item,depth+1).release());break;
    case Value::Object:{de.reset(new DataElement(DataElement::t_map));de->setNull(false);int err=0;auto map=de->GetMap(err);for(const auto& item:v.object){auto value=toDataElement(item.second,depth+1);map->emplace(item.first,value.get());value.release();}break;}
    }
    return de;
}
} }
