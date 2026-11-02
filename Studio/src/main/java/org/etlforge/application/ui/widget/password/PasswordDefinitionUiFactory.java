package org.etlforge.application.ui.widget.password;

import org.apache.commons.lang.SystemUtils;

public final class PasswordDefinitionUiFactory {
   private PasswordDefinitionUiFactory() {
   }

   public static PasswordDefinitionUi getUi() {
      return getUi(null);
   }

   public static PasswordDefinitionUi getUi(String var0) {
      if (var0 == null || var0.length() == 0) {
         var0 = PackageResources.DEFAULT_LABEL;
      }

      return SystemUtils.IS_OS_WINDOWS ? new WindowsPasswordDefinitionUi(var0) : new StandardPasswordDefinitionUi(var0);
   }
}
