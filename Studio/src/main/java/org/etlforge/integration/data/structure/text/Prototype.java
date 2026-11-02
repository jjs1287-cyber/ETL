package org.etlforge.integration.data.structure.text;

public enum Prototype {
   SampleFlatFileWithHeader("Sample flat file with column names"),
   SampleFlatFile("Sample flat file w/o column names"),
   SimpleTextDef("Simple text definition");

   private final String display_name;

   Prototype(String var3) {
      this.display_name = var3;
   }

   public String getDisplayName() {
      return this.display_name;
   }
}
