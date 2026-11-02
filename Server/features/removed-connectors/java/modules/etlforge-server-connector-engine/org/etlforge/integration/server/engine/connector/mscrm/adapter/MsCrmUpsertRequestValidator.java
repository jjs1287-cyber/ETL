package org.etlforge.integration.server.engine.connector.mscrm.adapter;

import java.io.File;
import org.etlforge.integration.server.engine.connector.exception.ConnectorRuntimeException;
import org.etlforge.xml.KongaXmlUtils;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MsCrmUpsertRequestValidator {
   public static String validateXml(String var0) {
      return parseXml(var0, false);
   }

   public static String validateXmlFile(String var0) {
      return parseXml(var0, true);
   }

   private static String parseXml(String var0, boolean var1) {
      MsCrmUpsertRequestValidator.MsCrmUpsertRequestValidatorHandler var2 = new MsCrmUpsertRequestValidator.MsCrmUpsertRequestValidatorHandler();

      try {
         if (var1) {
            SaxParsers.parseFile(new File(var0), var2, false);
         } else {
            SaxParsers.parseString(var0, var2, false);
         }
      } catch (Exception var4) {
         throw new ConnectorRuntimeException("Failed to parse the upsert request: " + var0 + "\n error message:" + var4.getMessage(), var4);
      }

      return var2.getValidationMessage();
   }

   private MsCrmUpsertRequestValidator() {
   }

   public static void main(String[] var0) {
      String var1 = "Z:/etlforge/trunk/dev/ETLForgeConnectorProject/MSCRM/Data/Upsert - Request.xml";
      String var2 = validateXmlFile(var1);
      System.out.println("message=" + var2);
   }

   private static class MsCrmUpsertRequestValidatorHandler extends DefaultHandler {
      private boolean hasUpserkey = true;
      private boolean hasUpsertkeyRecord;
      private int currentLevel;
      private final int upsertObjectLevel;
      private final int upsertObjectFieldLevel;
      private int recordNumber = 0;
      private int recordNumberNoUpsertKey = 0;
      private final int recordNumberNoUpsertKeyMax;
      private final StringBuilder errorMessage;

      MsCrmUpsertRequestValidatorHandler() {
         this.hasUpserkey = true;
         this.upsertObjectLevel = 2;
         this.upsertObjectFieldLevel = this.upsertObjectLevel + 1;
         this.currentLevel = 0;
         this.errorMessage = new StringBuilder();
         this.recordNumberNoUpsertKeyMax = 50;
      }

      String getValidationMessage() {
         if (this.recordNumber == 0) {
            return "No records to upsert";
         } else {
            return this.hasUpserkey ? null : this.errorMessage.toString();
         }
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         this.currentLevel++;
         boolean var5 = this.recordNumberNoUpsertKey < this.recordNumberNoUpsertKeyMax;
         if (var5) {
            if (this.upsertObjectLevel == this.currentLevel) {
               this.recordNumber++;
               this.hasUpsertkeyRecord = false;
            } else if (!this.hasUpsertkeyRecord && this.upsertObjectFieldLevel == this.currentLevel && this.hasUpsertKey(var4)) {
               this.hasUpsertkeyRecord = true;
            }
         }
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         boolean var4 = this.recordNumberNoUpsertKey < this.recordNumberNoUpsertKeyMax;
         if (var4 && this.upsertObjectLevel == this.currentLevel && !this.hasUpsertkeyRecord) {
            this.hasUpserkey = false;
            this.recordNumberNoUpsertKey++;
            this.errorMessage.append("Record ").append(this.recordNumber).append(" has no upsert keys. ");
         }

         this.currentLevel--;
      }

      private boolean hasUpsertKey(Attributes var1) {
         String var2 = var1.getValue("upsertkey");
         return var2 == null ? false : KongaXmlUtils.isTrue(var2.toLowerCase());
      }
   }
}
