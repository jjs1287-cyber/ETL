package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchBasicRow;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchCustomField;
import org.xml.sax.Attributes;

public abstract class NetSuiteSearchObjectRowFieldsHandler {
   protected final Logger log = Logger.getLogger("NetSuiteSearchObjectValueParser");
   private final NetSuiteSearchBasicRow searchObjectRow;
   private final NetSuiteSearchCustomFieldIdHandler customFieldIdHandler;
   private final int searchFieldLevel;
   private boolean isCustomFieldList = false;

   NetSuiteSearchObjectRowFieldsHandler(int var1, NetSuiteSearchBasicRow var2) {
      this.searchFieldLevel = var1;
      this.searchObjectRow = var2;
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
      if (this.isCustomFieldList(var1, var2)) {
         this.isCustomFieldList = true;
      } else {
         String var5 = var2;
         boolean var6 = this.searchObjectRow.isCustomField(var5);
         if (var6) {
            String var7 = "No definition for field" + var5;
            this.log.log(Level.SEVERE, var7);
         } else {
            this.add(var5);
         }
      }
   }

   protected abstract void add(String var1);

   private void processSearchCustomField(String var1, String var2, String var3, Attributes var4) {
      NetSuiteSearchCustomField var5 = this.customFieldIdHandler.processId(var4);
      if (var5 != null) {
         this.add(var5.getSearchFieldName());
      }
   }

   public void startElementFieldImpl(int var1, String var2, String var3, String var4, Attributes var5) {
      if (this.isSearchField(var1)) {
         this.processSearchField(var2, var3, var4, var5);
      } else if (this.isSearchCustomField(var1)) {
         this.processSearchCustomField(var2, var3, var4, var5);
      }
   }

   public void endElementImpl(int var1, String var2, String var3, String var4) {
      if (this.isSearchField(var1)) {
         this.isCustomFieldList = false;
      }
   }
}
