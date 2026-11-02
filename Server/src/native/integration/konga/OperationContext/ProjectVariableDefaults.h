#ifndef ETLFORGE_PROJECT_VARIABLE_DEFAULTS_H
#define ETLFORGE_PROJECT_VARIABLE_DEFAULTS_H
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/OperationContext/OperationDataElementHolder.h>
#include <konga/DataElement/DataElement.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>
#include <konga/CharacterEncodingConversion/Utf8TextInput.h>
#include <konga/Crypto/Crypto.h>
#include <memory>
#include <regex>
#include <set>
namespace etlforge {
inline void validateProjectVariable(const std::string& name, const std::string& value) {
    if (!std::regex_match(name, std::regex("[A-Za-z_][A-Za-z0-9_.]{0,255}")) || name.find("__")==0
        || konga::EqualsIgnoreCase(name.substr(0,9), "etlforge.") || konga::EqualsIgnoreCase(name.substr(0,9), "jitterbit."))
        throw konga::CrException("Invalid project variable name: " + name);
    etlforge::requireUtf8Text(value.data(), value.size());
    if (value.size() > 1048576 || value.find('\0') != std::string::npos)
        throw konga::CrException("Project variable values must be text of at most 1 MiB without NUL.");
}
inline void validateProjectVariableNames(konga::DbConnection& db, long project) {
    konga::SmartDbPtr<konga::DynamicDbRecordSet> rows(konga::DynamicDbRecordSet::MakeInstance(&db,
        "SELECT v.projectvariable_name FROM projectvariablestab v JOIN interchangeprojectprojectvariablestab p ON p.item_id=v.projectvariable_id WHERE p.interchangeproject_id="
        + konga::toString(project) + " GROUP BY v.projectvariable_name HAVING count(*)>1"));
    rows->Open();
    if (rows->Next()) throw konga::CrException("Duplicate project variable name: " + rows->GetFieldValue(0));
}
inline void loadProjectVariableDefaults(konga::DbConnection& db, long operation, konga::OperationDataElementHolder& holder) {
    konga::SmartDbPtr<konga::DynamicDbRecordSet> rows(konga::DynamicDbRecordSet::MakeInstance(&db,
        "SELECT DISTINCT v.projectvariable_name,encode(convert_to(v.default_value,'UTF8'),'base64') FROM projectvariablestab v JOIN interchangeprojectprojectvariablestab p ON p.item_id=v.projectvariable_id "
        "JOIN interchangeprojectoperationstab o ON o.interchangeproject_id=p.interchangeproject_id WHERE o.item_id=" + konga::toString(operation) + " ORDER BY v.projectvariable_name"));
    rows->Open();
    std::set<std::string> seen;
    while (rows->Next()) {
        const std::string name=rows->GetFieldValue(0);
        int size=0; std::unique_ptr<unsigned char[]> decoded(konga::DecodeBase64(rows->GetFieldValue(1), size));
        const std::string value(reinterpret_cast<const char*>(decoded.get()), size);
        validateProjectVariable(name,value);
        if (!seen.insert(name).second) throw konga::CrException("Ambiguous project variable name: " + name);
        if (holder.isOperationDataElementDefined(name)) continue;
        konga::DataElement* variable=holder.getOperationDataElement(name);
        variable->setType(konga::DataElement::t_string);
        variable->setValue(value.c_str());
    }
}
}
#endif
