package org.etlforge.ui.text.contentassist;

public enum AutoCompletionMode {
   AUTOMATIC("Automatic"),
   CODE_EDITOR("Code Editor");

   private final String stringRep;

   AutoCompletionMode(String var3) {
      this.stringRep = var3;
   }

   @Override
   public String toString() {
      return this.stringRep;
   }
}
