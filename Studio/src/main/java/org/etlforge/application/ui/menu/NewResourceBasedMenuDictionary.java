package org.etlforge.application.ui.menu;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.swing.Icon;
import org.etlforge.ui.IconProvider;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.MnemonicStringFactory;
import org.etlforge.ui.ResourceBundleIconProvider;
import org.etlforge.ui.ResourceBundleStringResources;
import org.etlforge.ui.StringResources;

public final class NewResourceBasedMenuDictionary implements MenuDictionary {
   private final StringResources strings;
   private final IconProvider icons;
   private MenuDictionary parent;
   private final Map<String, MnemonicString> mnemonicStrings;

   public NewResourceBasedMenuDictionary(StringResources var1, IconProvider var2, MenuDictionary var3) {
      this.strings = var1;
      this.icons = var2;
      this.parent = var3;
      this.mnemonicStrings = Maps.newHashMap();
   }

   public static NewResourceBasedMenuDictionary newDictionary(String var0, MnemonicStringFactory var1, String var2, MenuDictionary var3) {
      ResourceBundleStringResources var4 = new ResourceBundleStringResources(var0, var1);
      ResourceBundleIconProvider var5 = new ResourceBundleIconProvider(var2);
      return new NewResourceBasedMenuDictionary(var4, var5, var3);
   }

   public void setParent(MenuDictionary var1) {
      this.parent = var1;
   }

   @Override
   public Integer getDisplayedMnemonicIndex(String var1) {
      MnemonicString var2 = this.getMnemonicString(var1);
      Integer var3 = null;
      if (var2 != null) {
         int var4 = var2.getDisplayedMnemonicIndex();
         var3 = var4 >= 0 ? var4 : null;
      } else if (this.parent != null) {
         var3 = this.parent.getDisplayedMnemonicIndex(var1);
      }

      return var3;
   }

   @Override
   public Icon getIcon(String var1) {
      Icon var2 = this.icons.getIcon(var1 + ".Icon");
      if (var2 == null && this.parent != null) {
         var2 = this.parent.getIcon(var1);
      }

      return var2;
   }

   @Override
   public Integer getMnemonic(String var1) {
      Integer var2 = null;
      MnemonicString var3 = this.getMnemonicString(var1);
      if (var3 != null) {
         char var4 = var3.getMnemonic();
         if (var4 != 0) {
            var2 = Integer.valueOf(var4);
         }
      } else if (this.parent != null) {
         var2 = this.parent.getMnemonic(var1);
      }

      return var2;
   }

   @Override
   public String getName(String var1) {
      String var2 = null;
      MnemonicString var3 = this.getMnemonicString(var1);
      if (var3 != null) {
         var2 = var3.getText();
      } else if (this.parent != null) {
         var2 = this.parent.getName(var1);
      }

      return var2;
   }

   @Override
   public MenuDictionary getParent() {
      return this.parent;
   }

   private MnemonicString getMnemonicString(String var1) {
      String var2 = var1 + ".Name";
      MnemonicString var3 = null;
      if (this.mnemonicStrings.containsKey(var2)) {
         var3 = this.mnemonicStrings.get(var2);
      } else {
         var3 = this.strings.getMnemonicString(var2);
         this.mnemonicStrings.put(var2, var3);
      }

      return var3;
   }
}
