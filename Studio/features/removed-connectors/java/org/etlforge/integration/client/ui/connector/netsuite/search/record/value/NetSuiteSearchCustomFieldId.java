package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import java.util.Map;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchCustomField;

public class NetSuiteSearchCustomFieldId {
   private final boolean usingScriptId;

   public NetSuiteSearchCustomFieldId(boolean var1) {
      this.usingScriptId = var1;
   }

   public void put(Map<String, String> var1, NetSuiteSearchCustomField var2) {
      String var3 = this.usingScriptId ? "scriptId" : "internalId";
      String var4 = var2.getScriptID();
      var1.put(var3, var4);
   }
}
