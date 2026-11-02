package org.etlforge.integration.structure.crom;

public class CROMHeader {
   public static final CROMHeader DEFAULT_HEADER = new CROMHeader("");
   public static final CROMHeader XML = new CROMHeader("Xml");
   private final String typeString;
   private String beginQuote = "";
   private String endQuote = "";

   public CROMHeader(String var1) {
      this.typeString = var1;
   }

   public CROMHeader(String var1, String var2, String var3) {
      this.typeString = var1;
      this.beginQuote = var2;
      this.endQuote = var3;
   }

   public String getTypeString() {
      return this.typeString;
   }

   public int getVersion() {
      if (this.typeString.equals("DB")) {
         return 2;
      } else {
         return this.typeString.equals("Xml") ? 2 : 0;
      }
   }

   public String getBeginQuote() {
      return this.beginQuote;
   }

   public String getEndQuote() {
      return this.endQuote;
   }
}
