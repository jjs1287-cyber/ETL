                                                              
#include "mod_konga_ws.h"
#include <konga/Utils/CrException.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
extern "C" module AP_MODULE_DECLARE_DATA etlforge_hosted_module;
APLOG_USE_MODULE(etlforge_hosted);
namespace {
int handle(request_rec* r) {
    if (!r->handler || strcmp(r->handler, "konga_ws") != 0) return DECLINED;
    try {
        int result = DECLINED;
        if(r->uri && strncmp(r->uri,"/etlforge/soap",14)==0)
            konga::HandleHostedWebService(r, result);
        if(result==DECLINED) {
            konga::SmartDbPtr<konga::DbConnection> database(konga::GetBackEndDbConnection("TranDb"));
            konga::HandleHttpTrigger(r,result,*database);
        }
        return result;
    } catch (const konga::CrException& ex) {
        ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r, "%s", ex.GetErrorString().c_str());
        return HTTP_INTERNAL_SERVER_ERROR;
    } catch (...) {
        ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r, "%s", "Unexpected hosted SOAP execution error");
        return HTTP_INTERNAL_SERVER_ERROR;
    }
}
void hooks(apr_pool_t*) { ap_hook_handler(handle, NULL, NULL, APR_HOOK_MIDDLE); }
}
extern "C" {
__declspec(dllexport) module AP_MODULE_DECLARE_DATA etlforge_hosted_module = {
    STANDARD20_MODULE_STUFF, NULL, NULL, NULL, NULL, NULL, hooks
};
}
