package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.SearchFieldSaxHandler;
import org.xml.sax.Attributes;

public class SearchFieldRecordRefSaxHandler extends SearchFieldSaxHandler {
   private NetSuiteRecordRef recordRefValue = null;

   @Override
   public void startElementImpl(boolean var1, String var2, String var3, String var4, Attributes var5) {
      if (var1) {
         this.recordRefValue = this.setNetSuiteRecordRef(var5, false);
      }
   }

   @Override
   public void endElementImpl(boolean var1, String var2, String var3, String var4, String var5) {
      if (var1) {
         this.fieldValue = new SearchFieldValueRecordRef(this.selectedOperator, this.recordRefValue);
         this.recordRefValue = null;
      } else {
         this.setNetSuiteRecordRefName(var3, var5, this.recordRefValue);
      }
   }
}
