package org.etlforge.application.ui.action;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.KeyStroke;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.ui.IconProvider;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.MnemonicStringFactory;
import org.etlforge.ui.ResourceBundleIconProvider;
import org.etlforge.ui.ResourceBundleStringResources;
import org.etlforge.ui.StringResources;

public class NewResourceBasedActionDictionary implements ActionDictionary {
   private ActionDictionary parent;
   private final StringResources strings;
   private final IconProvider icons;
   private final Map<String, MnemonicString> mnemonicStrings;

   public NewResourceBasedActionDictionary(StringResources var1, IconProvider var2, ActionDictionary var3) {
      this.strings = var1;
      this.icons = var2;
      this.parent = var3;
      this.mnemonicStrings = Maps.newHashMap();
   }

   public static NewResourceBasedActionDictionary newDictionary(String var0, MnemonicStringFactory var1, String var2, ActionDictionary var3) {
      ResourceBundleStringResources var4 = new ResourceBundleStringResources(var0, var1);
      ResourceBundleIconProvider var5 = new ResourceBundleIconProvider(var2);
      return new NewResourceBasedActionDictionary(var4, var5, var3);
   }

   public void setParent(ActionDictionary var1) {
      this.parent = var1;
   }

   @Override
   public Icon getLargeIcon(String var1) {
      Icon var2 = this.icons.getIcon(var1 + ".Icon.Large");
      if (var2 == null && this.parent != null) {
         var2 = this.parent.getLargeIcon(var1);
      }

      return var2;
   }

   @Override
   public Icon getMediumIcon(String var1) {
      Icon var2 = this.icons.getIcon(var1 + ".Icon.Medium");
      if (var2 == null && this.parent != null) {
         var2 = this.parent.getLargeIcon(var1);
      }

      return var2;
   }

   @Override
   public Icon getSmallIcon(String var1) {
      Icon var2 = this.icons.getIcon(var1 + ".Icon.Small");
      if (var2 == null && this.parent != null) {
         var2 = this.parent.getLargeIcon(var1);
      }

      return var2;
   }

   @Override
   public MnemonicString getName(String var1) {
      String var2 = var1 + ".Name";
      MnemonicString var3 = null;
      if (this.mnemonicStrings.containsKey(var2)) {
         var3 = this.mnemonicStrings.get(var2);
      } else if (this.strings.contains(var2)) {
         var3 = this.strings.getMnemonicString(var2);
         this.mnemonicStrings.put(var2, var3);
      } else if (this.parent != null) {
         var3 = this.parent.getName(var1);
      }

      return var3;
   }

   @Override
   public ActionDictionary getParent() {
      return this.parent;
   }

   @Override
   public String getToolTip(String var1) {
      String var2 = this.strings.getString(var1 + ".ToolTip", null);
      if (var2 == null && this.parent != null) {
         var2 = this.parent.getToolTip(var1);
      }

      return var2;
   }

   @Override
   public KeyStroke getAccelerator(String var1) {
      KeyStroke var2 = null;
      String var3 = var1 + ".Accelerator";
      if (this.strings.contains(var3)) {
         String var4 = translateAcceleratorForPlatoform(this.strings.getString(var3));
         var2 = KeyStroke.getKeyStroke(var4);
      }

      if (var2 == null && this.parent != null) {
         var2 = this.parent.getAccelerator(var1);
      }

      return var2;
   }

   private static String translateAcceleratorForPlatoform(String var0) {
      return SystemUtils.IS_OS_MAC ? var0.replace("ctrl pressed", "meta pressed") : var0;
   }
}
