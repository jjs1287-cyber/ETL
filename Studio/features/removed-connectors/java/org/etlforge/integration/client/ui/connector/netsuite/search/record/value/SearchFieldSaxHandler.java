package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteConstants;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.NetSuiteRecordRef;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValueString;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValueStringTwo;
import org.xml.sax.Attributes;

public abstract class SearchFieldSaxHandler {
   protected NetSuiteSearchOperator selectedOperator = null;
   protected SearchFieldValue fieldValue = null;

   public SearchFieldValue getSearchFieldValue() {
      return this.fieldValue;
   }

   public void startElementImpl(boolean var1, String var2, String var3, String var4, Attributes var5) {
   }

   public void endElementImpl(boolean var1, String var2, String var3, String var4, String var5) {
   }

   protected void processOperator(Attributes var1) {
      if (this.selectedOperator == null) {
         String var2 = var1.getValue("operator");
         if (!Strings.isNullOrEmpty(var2)) {
            this.selectedOperator = NetSuiteSearchOperator.valueOf(var2);
         }
      }
   }

   protected final void setSearchFieldValueStringTwo(String var1, String var2) {
      if (var1.equals("searchValue")) {
         this.fieldValue = new SearchFieldValueStringTwo(this.selectedOperator, var2);
      } else if (var1.equals("searchValue2")) {
         if (this.fieldValue == null) {
            this.fieldValue = new SearchFieldValueStringTwo(this.selectedOperator, null, var2);
         } else {
            SearchFieldValueStringTwo var3 = (SearchFieldValueStringTwo)this.fieldValue;
            this.fieldValue = new SearchFieldValueStringTwo(this.selectedOperator, var3.getSearchValue(), var2);
         }
      }
   }

   protected final void setSearchFieldValueString(String var1, String var2) {
      if (var1.equals("searchValue")) {
         this.fieldValue = new SearchFieldValueString(this.selectedOperator, var2);
      }
   }

   protected final NetSuiteRecordRef setNetSuiteRecordRef(Attributes var1, boolean var2) {
      NetSuiteRecordRef var3 = new NetSuiteRecordRef();
      String var4 = var1.getValue("internalId");
      String var5 = var1.getValue("externalId");
      String var6 = var1.getValue(NetSuiteConstants.getTypeElementName(var2));
      if (!Strings.isNullOrEmpty(var4)) {
         var3.setInternalId(var4);
      }

      if (!Strings.isNullOrEmpty(var5)) {
         var3.setExternalId(var5);
      }

      if (!Strings.isNullOrEmpty(var6)) {
         var3.setType(var6);
      }

      return var3;
   }

   protected final void setNetSuiteRecordRefName(String var1, String var2, NetSuiteRecordRef var3) {
      if (var1.equals("name")) {
         Preconditions.checkNotNull(var3, "The xml is incompatible to expected structure of RecordRef!");
         var3.setName(var2);
      }
   }
}
