#include "AuditObjectReader.h"
#include <algorithm>
#include <cctype>
#include <stdexcept>

namespace etlforge::audit {
namespace {
std::string identifier(const std::string& name) {
    auto dot=name.find('.');
    if(dot!=std::string::npos) {
        if(name.find('.',dot+1)!=std::string::npos)throw std::runtime_error("Invalid qualified audit database identifier");
        return identifier(name.substr(0,dot))+"."+identifier(name.substr(dot+1));
    }
    if(name.empty() || name.size()>63 || !std::all_of(name.begin(),name.end(),[](unsigned char c){return std::isalnum(c) || c=='_';}))
        throw std::runtime_error("Invalid audit snapshot database identifier");
    return name;
}
std::string valueText(const boost::json::value& value) {
    return value.is_string() ? std::string(value.as_string()) : canonicalJson(value);
}
std::string cell(const Row& row,std::size_t index) {
    if(index>=row.size() || !row[index]) throw std::runtime_error("Invalid audit snapshot database result");
    return *row[index];
}
}
ObjectReader::ObjectReader(Database& database,std::vector<EntityPlan> plans,Rules rules,
    std::vector<unsigned char> key,std::string keyVersion,FileSnapshot files,FileSnapshot supplement)
    : database_(database),plans_(std::move(plans)),rules_(std::move(rules)),key_(std::move(key)),keyVersion_(std::move(keyVersion)),files_(std::move(files)),supplement_(std::move(supplement)) {
    std::set<int> types;
    for(const auto& plan:plans_) {
        if(plan.type<1 || !types.insert(plan.type).second)throw std::runtime_error("Invalid or duplicate audit entity plan");
        identifier(plan.nameColumn);identifier(plan.guidTable);identifier(plan.membershipTable);identifier(plan.root.table);identifier(plan.root.primaryKey);
        for(const auto& child:plan.children) {
            identifier(child.table);identifier(child.ownerColumn);
            if(!child.primaryKey.empty())identifier(child.primaryKey);
        }
    }
}
boost::json::object ObjectReader::normalizeRow(boost::json::object row,const TablePlan& plan) {
    row.erase(plan.primaryKey);row.erase(plan.ownerColumn);
    if (plan.table == "projectvariablestab") {
        const auto* password = row.if_contains("is_password");
        if (password && password->is_int64() && password->as_int64() == 0) row.erase("is_password");
        const auto* description = row.if_contains("description");
        if (description && description->is_string() && description->as_string().empty()) row.erase("description");
    }
    row.erase("inserttime");row.erase("modtime");
    for(const auto& column:plan.ignoredColumns)row.erase(column);
    for(const auto& reference:plan.references) {
        auto* value=row.if_contains(reference.column);
        if(!value || value->is_null())continue;
        if(value->is_int64() && value->as_int64()<=0)continue;
        auto rows=database_.query("SELECT guid::text FROM "+identifier(reference.table)+" WHERE item_id=CAST(? AS integer)",{valueText(*value)});
        if(rows.size()!=1)throw std::runtime_error("Audit snapshot references an object without a stable GUID");
        *value=cell(rows[0],0);
    }
    return row;
}
Objects ObjectReader::readProject(const std::string& projectGuid) {
    database_.requireTransaction();
    auto projects=database_.query("SELECT item_id::text FROM guidsforinterchangeprojectstab WHERE CAST(guid AS uuid)=CAST(? AS uuid)",{projectGuid});
    if(projects.empty())return {};
    if(projects.size()!=1)throw std::runtime_error("Ambiguous audit project identity");
    std::string projectId=cell(projects[0],0);
    Objects result;
    for(const auto& plan:plans_) {
        const auto& root=plan.root;
        auto roots=database_.query("SELECT g.guid::text,to_jsonb(t)::text FROM "+identifier(root.table)+" t JOIN "+identifier(plan.guidTable)+" g ON g.item_id=t."+identifier(root.primaryKey)+" JOIN "+identifier(plan.membershipTable)+" p ON p.item_id=g.item_id WHERE p.interchangeproject_id=CAST(? AS integer)",{projectId});
        for(const auto& data:roots) {
            auto raw=boost::json::parse(cell(data,1)).as_object();
            auto* primary=raw.if_contains(root.primaryKey);auto* name=raw.if_contains(plan.nameColumn);
            if(!primary || !name || !name->is_string())throw std::runtime_error("Audit object missing its identity or name");
            std::string id=valueText(*primary),guid=cell(data,0),objectName(name->as_string());
            boost::json::object snapshot{{"fields",normalizeRow(raw,root)}};
            boost::json::object children;
            Rules rules=rules_;
            for(const auto& child:plan.children) {
                auto rows=database_.query("SELECT to_jsonb(t)::text FROM "+identifier(child.table)+" t WHERE t."+identifier(child.ownerColumn)+"=CAST(? AS integer)",{id});
                boost::json::array values;
                for(const auto& row:rows)values.push_back(normalizeRow(boost::json::parse(cell(row,0)).as_object(),child));
                if(children.contains(child.table))throw std::runtime_error("Duplicate audit child collection");
                children[child.table]=std::move(values);
                rules.unorderedArrays.insert("/children/"+child.table);
            }
            snapshot["children"]=std::move(children);
            if(supplement_)snapshot["configuration"]=supplement_(plan,raw);
            if(plan.requiresFileSnapshot) {
                if(!files_)throw std::runtime_error("Audit object requires a validated file-content snapshot adapter");
                snapshot["files"]=files_(plan,raw);
            }
            Object object{plan.type,guid,objectName,plan.typeName,prepareSnapshot(snapshot,rules,key_,keyVersion_)};
            if(!result.emplace(std::make_pair(plan.type,guid),std::move(object)).second)throw std::runtime_error("Duplicate audit object identity");
        }
    }
    return result;
}
}
