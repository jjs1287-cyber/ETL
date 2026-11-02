package org.etlforge.integration.data;

import org.apache.commons.lang.SystemUtils;
import org.etlforge.util.file.WindowsFileNameValidator;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.name.NameValidator;

public final class ProjectNameValidator implements NameValidator {
   private final DefaultIntegrationNameValidator defaultValidation = new DefaultIntegrationNameValidator();

   @Override
   public void validateName(String var1) throws IllegalNameException {
      String var2 = var1.trim();
      this.doDefaultValidation(var2);
      WindowsFileNameValidator var3 = new WindowsFileNameValidator();
      var3.setIllegalCharactersMessagePattern("A project name may not contain any of the following characters: {0}");
      var3.setReservedNameMessage(getReservedNameMessage(var2));
      var3.validate(var2);
      doDotValidation(var2);
   }

   private void doDefaultValidation(String var1) throws IllegalNameException {
      this.defaultValidation.validateName(var1);
   }

   private static String getReservedNameMessage(String var0) {
      StringBuilder var1 = new StringBuilder("The project name \"");
      var1.append(var0).append("\" is not allowed due to restrictions on valid folder names on Windows");
      if (!SystemUtils.IS_OS_WINDOWS) {
         var1.append(
            "\nThe name is disallowed even when running on a non-Windows platform, to make sure that a project can be shared between client platforms."
         );
      }

      return var1.toString();
   }

   private static void doDotValidation(String var0) {
      if (var0.endsWith(".")) {
         throw new IllegalNameException("The project name cannot end with '.'");
      }
   }
}
