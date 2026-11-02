package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import com.google.common.base.Strings;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchBasic;
import org.etlforge.integration.etlforgeconnector.XmlQName;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteSearchObjectValueHandler extends NetSuiteXmlSaxHandler {
   private final NetSuiteSearchBasic searchObject;
   private final NetSuiteSearchObjectValue searchObjectValue;
   private boolean isInsideSearchRecord;
   private int searchRecordLevel;
   private NetSuiteSearchObjectValueFieldsHandler fieldsHandler;

   NetSuiteSearchObjectValueHandler(NetSuiteSearchObjectValue var1) {
      this.searchObject = var1.getNetSuiteSearchBasic();
      this.searchObjectValue = var1;
      this.init();
   }

   private void init() {
      this.isInsideSearchRecord = false;
      this.searchRecordLevel = -1;
      this.fieldsHandler = null;
   }

   private void processSearchRecord(String var1, String var2, String var3, Attributes var4) {
      String var5 = var4.getValue("xsi:type");
      if (Strings.isNullOrEmpty(var5)) {
         throw new RuntimeException("derived type can not be empty.");
      }

      if (!var5.endsWith("SearchBasic")) {
         throw new RuntimeException(var5 + " is an unsupported derived type");
      }

      String var6 = XmlQName.getLocalName(var5);
      if (!this.searchObject.getSearchObjectLocalName().equals(var6)) {
         throw new RuntimeException(var6 + " is not same as " + this.searchObject.getSearchObjectLocalName());
      }

      this.searchRecordLevel = this.getLevel();
      this.isInsideSearchRecord = true;
      this.fieldsHandler = new NetSuiteSearchObjectValueFieldsHandler(this.searchRecordLevel + 1, this.searchObjectValue);
   }

   @Override
   public void startElementImpl(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if (this.isInsideSearchRecord) {
         this.fieldsHandler.startElementFieldImpl(this.getLevel(), var1, var2, var3, var4);
      } else if (NetSuiteSearchObjectValueAdvancedHandler.isSearchRecord(var1, var2)) {
         this.processSearchRecord(var1, var2, var3, var4);
      }
   }

   @Override
   public void endElementImpl(String var1, String var2, String var3) throws SAXException {
      if (this.isInsideSearchRecord) {
         if (this.searchRecordLevel >= this.getLevel()) {
            this.init();
         } else {
            this.fieldsHandler.endElementImpl(this.getLevel(), var1, var2, var3);
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
