#ifndef ETLFORGE_XSLT_ASSETS_H
#define ETLFORGE_XSLT_ASSETS_H

#include <konga/Db/DbUtils/DbConnectionHolder.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>

namespace etlforge {
class XsltAssetStore : private konga::DbConnectionHolder {
public:
    XsltAssetStore() { SetInternalDbName("TranDb"); }
    std::string loadStylesheet(const std::string& guid) {
        const std::string sql = "SELECT x.xslt FROM xslttransformstab x JOIN guidsforxslttransformstab g ON g.item_id=x.xslttransform_id WHERE g.guid='"
            + konga::SqlEscape(guid) + "'";
        konga::SmartDbPtr<konga::DynamicDbRecordSet> records(konga::DynamicDbRecordSet::MakeInstance(&GetDbConnection(), sql));
        records->Open();
        if (!records->Next()) throw konga::CrException("The XSLT transform has not been deployed: " + guid);
        return records->GetFieldValue(0);
    }
    std::string loadOperationStylesheet(long operationId) {
        const std::string sql = "SELECT x.xslt FROM xslttransformstab x JOIN operationpipelineactivitiestab a ON a.activity_id=x.xslttransform_id WHERE a.activitytype_id=8 AND a.operation_id="
            + konga::toString(operationId);
        konga::SmartDbPtr<konga::DynamicDbRecordSet> records(konga::DynamicDbRecordSet::MakeInstance(&GetDbConnection(), sql));
        records->Open();
        if (!records->Next()) throw konga::CrException("The operation has no deployed XSLT transform.");
        const std::string stylesheet = records->GetFieldValue(0);
        if (records->Next()) throw konga::CrException("An XSLT operation must have exactly one transform.");
        return stylesheet;
    }
};
}
#endif
