package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import com.google.common.base.Strings;
import java.util.logging.Logger;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchCustomField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchObjectCustomFields;
import org.xml.sax.Attributes;

public class NetSuiteSearchCustomFieldIdHandler {
   private final Logger log;
   private final NetSuiteSearchObjectCustomFields customFields;
   private String customFieldIdName;

   NetSuiteSearchCustomFieldIdHandler(NetSuiteSearchObjectCustomFields var1, Logger var2) {
      this.customFields = var1;
      this.log = var2;
      this.customFieldIdName = null;
   }

   public NetSuiteSearchCustomField processId(Attributes var1) {
      if (this.customFieldIdName == null) {
         String var2 = var1.getValue("scriptId");
         boolean var3 = Strings.isNullOrEmpty(var2);
         this.customFieldIdName = var3 ? "internalId" : "scriptId";
      }

      String var5 = var1.getValue(this.customFieldIdName);
      if (Strings.isNullOrEmpty(var5)) {
         throw new RuntimeException("custom field id can not be empty");
      }

      NetSuiteSearchCustomField var6 = this.customFields.getNetSuiteSearchCustomFieldByScriptID(var5);
      if (var6 == null) {
         String var4 = "Meta data may be outdated : No definition for custom field id: " + var5;
         this.log.warning(var4);
      }

      return var6;
   }
}
