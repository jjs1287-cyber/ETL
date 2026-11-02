package org.etlforge.integration.client.ui.script.builder.layout;

public enum LayoutType {
   TABS("Tab-based"),
   STACK("Stack-based"),
   BUTTONS("Button-based");

   private final String string;

   LayoutType(String var3) {
      this.string = var3;
   }

   @Override
   public String toString() {
      return this.string;
   }

   public boolean isSupported() {
      return this != STACK;
   }
}
