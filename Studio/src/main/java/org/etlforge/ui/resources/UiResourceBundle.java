package org.etlforge.ui.resources;

import java.text.MessageFormat;
import javax.swing.Icon;
import org.etlforge.ui.IconProvider;
import org.etlforge.ui.MnemonicStringFactory;
import org.etlforge.ui.PropertiesBasedIconProvider;
import org.etlforge.ui.ResourceBundleStringResources;
import org.etlforge.ui.StringResources;

public final class UiResourceBundle {
   public static final MnemonicStringFactory MNEMONIC_FACTORY = MnemonicStringFactory.newCharMarkerFactory('&');
   private static final StringResources strings = new ResourceBundleStringResources("org.etlforge.ui.resources.Strings", MNEMONIC_FACTORY);
   private static IconProvider icons;

   public static String getString(String var0) {
      return strings.getString(var0);
   }

   public static String getFormattedMessage(String var0, Object[] var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   public static Icon getIcon(String var0) {
      return icons != null ? icons.getIcon(var0) : null;
   }

   public static Icon getCloseIcon16() {
      return getIcon("CloseX");
   }

   public static Icon getExpandArrow16() {
      return getIcon("ExpandArrow");
   }

   public static Icon getCollapseArrow16() {
      return getIcon("CollapseArrow");
   }

   public static Icon getOptionButtonArrow16() {
      return getIcon("OptionButtonArrow");
   }

   private UiResourceBundle() {
   }

   static {
      try {
         icons = new PropertiesBasedIconProvider("org/etlforge/ui/resources/Icons.properties", "org.etlforge.ui.icons");
      } catch (Exception var1) {
         icons = null;
      }
   }
}
