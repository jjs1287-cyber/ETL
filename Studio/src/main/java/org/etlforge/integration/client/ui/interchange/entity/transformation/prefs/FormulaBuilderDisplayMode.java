package org.etlforge.integration.client.ui.interchange.entity.transformation.prefs;

public enum FormulaBuilderDisplayMode {
   DIALOG,
   OVERLAY;

   @Override
   public String toString() {
      switch (this) {
         case DIALOG:
            return "Dialog";
         case OVERLAY:
            return "Overlay";
         default:
            throw new RuntimeException("Unexpected display mode: " + this.name());
      }
   }
}
