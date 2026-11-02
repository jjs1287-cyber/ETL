package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import java.util.logging.Logger;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.SearchField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchBasic;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchCustomField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchObjectSpecialField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;
import org.xml.sax.Attributes;

public abstract class NetSuiteSearchObjectFieldsHandler {
   protected final Logger log = Logger.getLogger("NetSuiteSearchObjectValueParser");
   private final NetSuiteSearchBasic searchObject;
   private final NetSuiteSearchCustomFieldIdHandler customFieldIdHandler;
   private final int searchFieldLevel;
   private boolean isCustomFieldList = false;
   private StringBuilder sb = null;
   private String searchFieldName = null;
   private final String skipSearchFieldName;
   private SearchFieldSaxHandler searchFieldValueSaxHandler = null;

   NetSuiteSearchObjectFieldsHandler(int var1, NetSuiteSearchBasic var2) {
      this.searchFieldLevel = var1;
      this.searchObject = var2;
      if (var2.requireSpecialField()) {
         NetSuiteSearchObjectSpecialField var3 = var2.getNetSuiteSearchObjectSpecialField();
         this.skipSearchFieldName = var3.getFieldName();
      } else {
         this.skipSearchFieldName = null;
      }

      this.customFieldIdHandler = new NetSuiteSearchCustomFieldIdHandler(var2.getNetSuiteSearchObjectCustomFields(), this.log);
   }

   private boolean isCustomFieldList(String var1, String var2) {
      return var2.equals("customFieldList");
   }

   private boolean isSearchCustomField(int var1) {
      return this.isCustomFieldList && this.searchFieldLevel + 1 == var1;
   }

   private boolean isSearchField(int var1) {
      return this.searchFieldLevel == var1;
   }

   private void processSearchField(String var1, String var2, String var3, Attributes var4) {
      this.searchFieldValueSaxHandler = null;
      if (this.isCustomFieldList(var1, var2)) {
         this.isCustomFieldList = true;
      } else if (this.skipSearchFieldName == null || !var2.equals(this.skipSearchFieldName)) {
         this.searchFieldName = var2;
         SearchField var5 = this.searchObject.getSearchField(this.searchFieldName);
         this.searchFieldValueSaxHandler = var5.newSearchFieldValueSaxHandler();
      }
   }

   private void processSearchCustomField(String var1, String var2, String var3, Attributes var4) {
      this.searchFieldValueSaxHandler = null;
      NetSuiteSearchCustomField var5 = this.customFieldIdHandler.processId(var4);
      if (var5 != null) {
         this.searchFieldName = var5.getSearchFieldName();
         SearchField var6 = this.searchObject.getNetSuiteSearchObjectCustomFields().getSearchField(this.searchFieldName);
         if (var6 != null) {
            this.searchFieldValueSaxHandler = var6.newSearchFieldValueSaxHandler();
         }
      }
   }

   public void startElementFieldImpl(int var1, String var2, String var3, String var4, Attributes var5) {
      if (this.isSearchField(var1)) {
         this.processSearchField(var2, var3, var4, var5);
      } else if (this.isSearchCustomField(var1)) {
         this.processSearchCustomField(var2, var3, var4, var5);
      }

      if (this.searchFieldValueSaxHandler != null) {
         this.sb = new StringBuilder();
         boolean var6 = this.isSearchField(var1) || this.isSearchCustomField(var1);
         this.searchFieldValueSaxHandler.startElementImpl(var6, var2, var3, var4, var5);
      }
   }

   public void endElementImpl(int var1, String var2, String var3, String var4) {
      if (this.isSearchField(var1)) {
         this.isCustomFieldList = false;
      }

      if (this.searchFieldValueSaxHandler != null) {
         String var5 = this.sb == null ? null : this.sb.toString();
         boolean var6 = this.isSearchField(var1) || this.isSearchCustomField(var1);
         this.searchFieldValueSaxHandler.endElementImpl(var6, var2, var3, var4, var5);
         this.sb = null;
         if (var6) {
            SearchFieldValue var7 = this.searchFieldValueSaxHandler.getSearchFieldValue();
            if (var7 != null) {
               this.put(this.searchFieldName, var7);
            }

            this.searchFieldValueSaxHandler = null;
         }
      }
   }

   protected abstract void put(String var1, SearchFieldValue var2);

   public void characters(char[] var1, int var2, int var3) {
      if (this.sb != null) {
         this.sb.append(var1, var2, var3);
      }
   }
}
