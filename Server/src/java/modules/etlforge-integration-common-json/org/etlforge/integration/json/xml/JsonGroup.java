package org.etlforge.integration.json.xml;

enum JsonGroup {
   ARRAY("[", "]"),
   OBJECT("{", "}");

   public final String open;
   public final String close;

   JsonGroup(String var3, String var4) {
      this.open = var3;
      this.close = var4;
   }
}
