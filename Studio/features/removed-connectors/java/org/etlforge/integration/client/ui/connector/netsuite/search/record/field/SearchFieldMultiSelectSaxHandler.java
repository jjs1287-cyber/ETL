package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.SearchFieldSaxHandler;
import org.xml.sax.Attributes;

public class SearchFieldMultiSelectSaxHandler extends SearchFieldSaxHandler {
   private NetSuiteRecordRef recordRefValue = null;
   private final boolean isCustomField;

   SearchFieldMultiSelectSaxHandler(boolean var1) {
      this.isCustomField = var1;
   }

   @Override
   public void startElementImpl(boolean var1, String var2, String var3, String var4, Attributes var5) {
      if (var1) {
         this.processOperator(var5);
         this.fieldValue = new SearchFieldValueMultiSelect(this.selectedOperator, this.isCustomField);
      } else if (var3.equals("searchValue")) {
         this.recordRefValue = this.setNetSuiteRecordRef(var5, this.isCustomField);
      }
   }

   @Override
   public void endElementImpl(boolean var1, String var2, String var3, String var4, String var5) {
      if (!var1) {
         if (var3.equals("searchValue")) {
            SearchFieldValueMultiSelect var6 = (SearchFieldValueMultiSelect)this.fieldValue;
            var6.add(this.recordRefValue);
         } else {
            this.setNetSuiteRecordRefName(var3, var5, this.recordRefValue);
         }
      }
   }
}
