package org.etlforge.integration.client.ui.connector.netsuite.search.record;

import java.util.List;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchCustomObject;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;

public class NetSuiteSearchObjectSpecialField {
   private final NetSuiteCustomSearchObject customSearchObject;
   private final NetSuiteSearchCustomObject customObject;

   NetSuiteSearchObjectSpecialField(NetSuiteCustomSearchObject var1, NetSuiteSearchCustomObject var2) {
      this.customSearchObject = var1;
      this.customObject = var2;
   }

   public String getFieldName() {
      if (this.customSearchObject != null) {
         return "type";
      } else {
         return this.customObject != null ? "recType" : null;
      }
   }

   public SearchFieldValue getSearchFieldValue() {
      if (this.customSearchObject != null) {
         return this.customSearchObject.getTypeFieldValue();
      } else {
         return this.customObject != null ? this.customObject.getCustomObjectValue() : null;
      }
   }

   public boolean requireSpecialField() {
      return this.customSearchObject != null || this.customObject != null;
   }

   public void setCustomSearchSubtypes(List<String> var1) {
      this.customSearchObject.setCustomSearchSubtypes(var1);
   }
}
