package org.etlforge.integration.client.ui.connector.netsuite.search.record;

import com.google.common.base.Preconditions;
import java.util.List;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.SearchField;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchObjectName;

public class NetSuiteSearchObjectAllFields {
   private final NetSuiteSearchObject searchObject;
   private final NetSuiteSearchObjectCustomFields searchObjectCustomFields;

   public NetSuiteSearchObjectAllFields(NetSuiteSearchObject var1, NetSuiteSearchObjectSpecialField var2) {
      this(var1, var2, null);
   }

   NetSuiteSearchObjectAllFields(NetSuiteSearchObject var1, NetSuiteSearchObjectSpecialField var2, NetSuiteSearchObjectCustomFields var3) {
      this.searchObject = var1;
      this.searchObjectCustomFields = var3;
   }

   private boolean hasCustomFieldSupported() {
      return this.searchObjectCustomFields != null;
   }

   public String getSearchObjectLocalName() {
      return this.searchObject.searchObjectLocalName;
   }

   public String getSearchObjectNamespace() {
      return this.searchObject.getSearchObjectNamspaceName();
   }

   public String getNameOfObjectType() {
      return NetSuiteSearchObjectName.getObjectNameFromSearchBasic(this.getSearchObjectLocalName());
   }

   public Integer getFieldIndex(String var1) {
      Integer var2 = this.searchObject.getFieldIndex(var1);
      if (var2 == null && this.hasCustomFieldSupported()) {
         var2 = this.searchObjectCustomFields.getFieldIndex(var1);
      }

      Preconditions.checkArgument(var2 != null, "Unknown field index: " + var1);
      return var2;
   }

   public List<String> getFieldNames(String var1) {
      List var2 = this.searchObject.getFieldNames(var1);
      if (this.hasCustomFieldSupported()) {
         var2.addAll(this.searchObjectCustomFields.getFieldNames());
      }

      return var2;
   }

   public SearchField getSearchField(String var1) {
      SearchField var2 = this.searchObject.getSearchField(var1);
      if (var2 == null && this.hasCustomFieldSupported()) {
         var2 = this.searchObjectCustomFields.getSearchField(var1);
      }

      Preconditions.checkArgument(var2 != null, "Unknown field: " + var1);
      return var2;
   }

   public boolean isCustomField(String var1) {
      return this.searchObject.getSearchField(var1) == null;
   }

   public NetSuiteSearchObjectCustomFields getNetSuiteSearchObjectCustomFields() {
      return this.searchObjectCustomFields;
   }
}
