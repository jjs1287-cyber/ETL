package org.etlforge.integration.data.structure.edi;

public final class EdiOptions implements Cloneable {
   public static final EdiOptions X12_DEFAULTS = new EdiOptions(false, "~", "*", "!", "\\");
   public static final EdiOptions EDIFACT_DEFAULTS = new EdiOptions(false, "'", "+", ":", "?");
   private final boolean dataIsFormattedWithCLRF;
   private final String segmentTerminator;
   private final String elementTerminator;
   private final String compositeElementTerminator;
   private final String releaseIndicator;

   public EdiOptions(boolean var1, String var2, String var3, String var4, String var5) {
      this.dataIsFormattedWithCLRF = var1;
      this.segmentTerminator = checkString(var2);
      this.elementTerminator = checkString(var3);
      this.compositeElementTerminator = checkString(var4);
      this.releaseIndicator = checkString(var5);
   }

   private static String checkString(String var0) {
      if (var0 != null && var0.trim().length() != 0) {
         return var0;
      } else {
         throw new IllegalArgumentException("No blank strings allowed");
      }
   }

   public boolean isDataFormattedWithCLRF() {
      return this.dataIsFormattedWithCLRF;
   }

   public String getSegmentTerminator() {
      return this.segmentTerminator;
   }

   public String getElementTerminator() {
      return this.elementTerminator;
   }

   public String getCompositeElementTerminator() {
      return this.compositeElementTerminator;
   }

   public String getReleaseIndicator() {
      return this.releaseIndicator;
   }

   public EdiOptions clone() {
      try {
         return (EdiOptions)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new AssertionError();
      }
   }

   @Override
   public boolean equals(Object var1) {
      if (!(var1 instanceof EdiOptions)) {
         return false;
      }

      EdiOptions var2 = (EdiOptions)var1;
      return this.dataIsFormattedWithCLRF == var2.dataIsFormattedWithCLRF
         && this.segmentTerminator.equals(var2.segmentTerminator)
         && this.elementTerminator.equals(var2.elementTerminator)
         && this.compositeElementTerminator.equals(var2.compositeElementTerminator)
         && this.releaseIndicator.equals(var2.releaseIndicator);
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 37 * var1 + (this.dataIsFormattedWithCLRF ? 1 : 0);
      var1 = 37 * var1 + this.segmentTerminator.hashCode();
      var1 = 37 * var1 + this.elementTerminator.hashCode();
      var1 = 37 * var1 + this.compositeElementTerminator.hashCode();
      return 37 * var1 + this.releaseIndicator.hashCode();
   }

   @Override
   public String toString() {
      return "EDI Options: [clrf = "
         + this.dataIsFormattedWithCLRF
         + "], [segment terminator = "
         + this.segmentTerminator
         + "], [element terminator = "
         + this.elementTerminator
         + "], [composite element terminator = "
         + this.compositeElementTerminator
         + "], [release indicator = "
         + this.releaseIndicator
         + "]";
   }
}
