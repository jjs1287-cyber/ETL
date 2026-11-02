package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import com.google.common.base.Strings;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchRecord;
import org.etlforge.integration.design.netsuite.definition.ConnectorCoreNamespaces;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchObjectName;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteSearchObjectValueAdvancedHandler extends NetSuiteXmlSaxHandler {
   private static final String searchRecordLocalName = "searchRecord";
   private static final String savedSearchIdLocalName = "savedSearchId";
   private final NetSuiteSearchRecordValue searchRecordValue;
   private boolean isInsideSearchRecord;
   private boolean isSavedSearch;
   private boolean isSearchBasic;
   private boolean isCriteria;
   private int searchRecordLevel;
   private int searchFieldLevel;
   private NetSuiteSearchObjectValueFieldsAdvancedHandler fieldsHandler;
   private NetSuiteSearchObjectValueRowFieldsHandler fieldsHandlerRow;

   NetSuiteSearchObjectValueAdvancedHandler(NetSuiteSearchRecord var1) {
      this.searchRecordValue = new NetSuiteSearchRecordValue(var1);
      this.initRecord();
   }

   public NetSuiteSearchRecordValue getSearchRecordValue() {
      return this.searchRecordValue;
   }

   private void initFields() {
      this.fieldsHandler = null;
      this.fieldsHandlerRow = null;
   }

   private void initRecord() {
      this.isInsideSearchRecord = false;
      this.searchRecordLevel = -1;
      this.searchFieldLevel = Integer.MAX_VALUE;
      this.isSearchBasic = false;
      this.isSavedSearch = false;
      this.isCriteria = false;
      this.initFields();
   }

   static boolean isSearchRecord(String var0, String var1) {
      return var1.equals("searchRecord") && ConnectorCoreNamespaces.isMessageNamespace(var0);
   }

   private void processSearchRecord(String var1, String var2, String var3, Attributes var4) {
      this.initRecord();
      String var5 = var4.getValue("savedSearchId");
      this.isSavedSearch = !Strings.isNullOrEmpty(var5);
      if (this.isSavedSearch) {
         this.searchRecordValue.setSavedSearchId(var5);
      } else {
         String var6 = var4.getValue("xsi:type");
         if (Strings.isNullOrEmpty(var6)) {
            throw new RuntimeException("derived type can not be empty.");
         }

         this.isSearchBasic = NetSuiteSearchObjectName.isSearchBasic(var6);
         this.searchRecordLevel = this.getLevel();
         this.isInsideSearchRecord = true;
         this.searchFieldLevel = this.isSearchBasic ? this.searchRecordLevel + 1 : this.searchRecordLevel + 3;
         if (this.isSearchBasic) {
            String var7 = this.searchRecordValue.getNetSuiteSearchRecord().getBasicObjectKey();
            this.fieldsHandler = new NetSuiteSearchObjectValueFieldsAdvancedHandler(this.searchFieldLevel, var7, this.searchRecordValue);
         }
      }
   }

   private void processSearchRecordAdvanced(String var1, String var2, String var3) {
      if (this.searchRecordLevel + 1 == this.getLevel()) {
         this.isCriteria = var2.equals("criteria");
         this.initFields();
      } else {
         String var4 = var2;
         if (this.isCriteria) {
            this.fieldsHandler = new NetSuiteSearchObjectValueFieldsAdvancedHandler(this.searchFieldLevel, var4, this.searchRecordValue);
         } else {
            this.fieldsHandlerRow = new NetSuiteSearchObjectValueRowFieldsHandler(this.searchFieldLevel, var4, this.searchRecordValue);
         }
      }
   }

   private boolean isInFields(int var1) {
      return !this.isInsideSearchRecord ? false : var1 >= this.searchFieldLevel;
   }

   @Override
   public void startElementImpl(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if (!this.isSavedSearch) {
         if (this.isInsideSearchRecord) {
            if (this.isInFields(this.getLevel())) {
               if (this.fieldsHandler == null) {
                  this.fieldsHandlerRow.startElementFieldImpl(this.getLevel(), var1, var2, var3, var4);
               } else {
                  this.fieldsHandler.startElementFieldImpl(this.getLevel(), var1, var2, var3, var4);
               }
            } else {
               this.processSearchRecordAdvanced(var1, var2, var3);
            }
         } else if (isSearchRecord(var1, var2)) {
            this.processSearchRecord(var1, var2, var3, var4);
         }
      }
   }

   @Override
   public void endElementImpl(String var1, String var2, String var3) throws SAXException {
      if (!this.isSavedSearch) {
         if (this.isInsideSearchRecord) {
            if (this.searchRecordLevel >= this.getLevel()) {
               this.initRecord();
            } else if (this.isInFields(this.getLevel())) {
               if (this.fieldsHandler == null) {
                  this.fieldsHandlerRow.endElementImpl(this.getLevel(), var1, var2, var3);
               } else {
                  this.fieldsHandler.endElementImpl(this.getLevel(), var1, var2, var3);
               }
            } else {
               this.initFields();
            }
         }
      }
   }

   @Override
   public void characters(char[] var1, int var2, int var3) throws SAXException {
      if (this.fieldsHandler != null) {
         this.fieldsHandler.characters(var1, var2, var3);
      }
   }
}
