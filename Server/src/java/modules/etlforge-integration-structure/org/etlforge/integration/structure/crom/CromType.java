package org.etlforge.integration.structure.crom;

public enum CromType {
   CROM_t_Unknown("Unknown", 0),
   CROM_t_Text("Text", 1),
   CROM_t_DB("DB", 2),
   CROM_t_Ldap("Ldap", 3),
   CROM_t_Xml("Xml", 4);

   public static final int t_Unknown = 0;
   public static final int t_Text = 1;
   public static final int t_DB = 2;
   public static final int t_Ldap = 3;
   public static final int t_Xml = 4;
   private static CromType[] a = new CromType[]{CROM_t_Unknown, CROM_t_Text, CROM_t_DB, CROM_t_Ldap, CROM_t_Xml};
   public final String name;
   public final int ordinal;

   CromType(String var3, int var4) {
      this.name = var3;
      this.ordinal = var4;
   }

   public static CromType fromName(String var0) {
      for (CromType var4 : values()) {
         if (var4.name.equals(var0)) {
            return var4;
         }
      }

      return CROM_t_Unknown;
   }

   public static CromType fromIndex(int var0) {
      return var0 > 0 && var0 < 5 ? a[var0] : CROM_t_Unknown;
   }

   public CROM_ext createExtension() {
      switch (this) {
         case CROM_t_Text:
            return new CROM_Text();
         case CROM_t_Xml:
            return new CROM_Xml();
         case CROM_t_DB:
            return new CROM_DB();
         case CROM_t_Ldap:
            return new CROM_Ldap();
         case CROM_t_Unknown:
            return NullExtension.INSTANCE;
         default:
            throw new RuntimeException("Unexpected CROMType: " + this);
      }
   }
}
