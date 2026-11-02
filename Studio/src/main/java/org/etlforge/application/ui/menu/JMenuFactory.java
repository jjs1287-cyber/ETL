package org.etlforge.application.ui.menu;

import javax.swing.Icon;
import javax.swing.JMenu;

public final class JMenuFactory {
   private final String menuId;
   private final MenuDictionary dict;

   public JMenuFactory(String var1, MenuDictionary var2) {
      this.menuId = var1;
      this.dict = var2;
   }

   public JMenu createEmptyMenu() {
      JMenu var1 = new JMenu(this.dict.getName(this.menuId));
      this.setMnemonic(var1);
      this.setMnemonicIndex(var1);
      this.setIcon(var1);
      return var1;
   }

   private void setMnemonic(JMenu var1) {
      Integer var2 = this.dict.getMnemonic(this.menuId);
      if (var2 != null) {
         var1.setMnemonic(var2);
      }
   }

   private void setMnemonicIndex(JMenu var1) {
      Integer var2 = this.dict.getDisplayedMnemonicIndex(this.menuId);
      if (var2 != null) {
         var1.setDisplayedMnemonicIndex(var2);
      }
   }

   private void setIcon(JMenu var1) {
      Icon var2 = this.dict.getIcon(this.menuId);
      if (var2 != null) {
         var1.setIcon(var2);
      }
   }
}
