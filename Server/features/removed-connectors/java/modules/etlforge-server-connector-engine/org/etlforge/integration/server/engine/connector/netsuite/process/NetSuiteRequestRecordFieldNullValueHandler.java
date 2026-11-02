package org.etlforge.integration.server.engine.connector.netsuite.process;

import com.google.common.base.Strings;
import org.xml.sax.Attributes;

public class NetSuiteRequestRecordFieldNullValueHandler {
   private static final String internalIdElementName = "internalId";
   private static final String externalIdElementName = "externalId";
   private static final String nameElementName = "name";
   private static final String typeElementName = "type";
   private static final String typeIDElementName = "typeId";
   private boolean isNullField;
   private final int valueLevel;
   private boolean hasChildElement;
   private final boolean isCustomField;

   public NetSuiteRequestRecordFieldNullValueHandler(int var1, boolean var2) {
      this.valueLevel = var1;
      this.isCustomField = var2;
      this.isNullField = true;
      this.hasChildElement = false;
   }

   public boolean isNullField() {
      return this.isNullField;
   }

   void throwExceptionOnLevel(int var1) {
      if (var1 < this.valueLevel) {
         throw new RuntimeException("Fatal Error: This is not a child of a field value. Please save the debug files");
      }
   }

   public void startElementValue(Attributes var1) {
      if (!isNullFieldAttributes(this.isCustomField, var1)) {
         this.isNullField = false;
      }
   }

   private static boolean isNullFieldAttributes(boolean var0, Attributes var1) {
      if (var1.getLength() > 3) {
         return false;
      }

      int var2 = 0;

      while (true) {
         if (var2 >= var1.getLength()) {
            return true;
         }

         String var3 = var1.getLocalName(var2);
         if (!var3.equals("internalId") && !var3.equals("externalId")) {
            if (var0 ? !var3.equals("typeId") : !var3.equals("type")) {
               break;
            }
         } else {
            String var4 = var1.getValue(var2);
            if (!Strings.isNullOrEmpty(var4)) {
               break;
            }
         }

         var2++;
      }

      return false;
   }

   public void startElement(int var1, String var2, String var3, String var4, Attributes var5) {
      this.throwExceptionOnLevel(var1);
      if (this.isNullField) {
         if (this.valueLevel == var1) {
            this.hasChildElement = false;
            this.startElementValue(var5);
         } else {
            if (var5.getLength() > 0) {
               this.isNullField = false;
            }

            this.hasChildElement = true;
         }
      }
   }

   public void endElement(boolean var1, int var2, String var3, String var4, String var5) {
      this.throwExceptionOnLevel(var2);
      if (this.isNullField) {
         if (this.valueLevel == var2) {
            if (!this.hasChildElement) {
               this.isNullField = var1;
            }
         } else {
            if (this.valueLevel + 1 == var2) {
               this.isNullField = var1;
               if (this.isNullField) {
                  this.isNullField = var4.equalsIgnoreCase("name");
                  return;
               }
            }

            this.isNullField = false;
         }
      }
   }
}
