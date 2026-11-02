package org.etlforge.application.ui.widget.password;

import java.text.MessageFormat;
import javax.swing.Icon;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.CommonIcons;

class PackageResources {
   static final String DEFAULT_LABEL = getString("Password.DefaultLabel");
   static final String CAPS_LOCK_WARNING = getString("Password.CapsLock.Warning");
   static final Icon CAPS_LOCK_ICON = CommonIcons.WARNING_16;
   static final String DIALOG_TITLE = getString("Password.Dialog.Title");

   private static String getString(String var0) {
      return ApplicationResources.STRINGS.getString(var0);
   }

   private static String getFormattedMessage(String var0, Object[] var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   private PackageResources() {
   }

   static class Empty {
      static final String TITLE = PackageResources.getString("Password.Empty.Title");
      static final String MESSAGE = PackageResources.getString("Password.Empty.Message");
   }

   static class Labels {
      static String getEnter(String var0) {
         return PackageResources.getFormattedMessage("Password.Label.Enter", new Object[]{var0});
      }

      static String getReEnter(String var0) {
         return PackageResources.getFormattedMessage("Password.Label.Re-enter", new Object[]{var0});
      }
   }

   static class MisMatch {
      static final String TITLE = PackageResources.getString("Password.MisMatch.Title");
      static final String MESSAGE = PackageResources.getString("Password.MisMatch.Message");
   }
}
