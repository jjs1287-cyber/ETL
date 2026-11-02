package org.etlforge.application.ui.resources;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Set;
import org.etlforge.application.config.FatalConfigurationError;
import org.etlforge.ui.IconProvider;
import org.etlforge.ui.MnemonicStringFactory;
import org.etlforge.ui.PropertiesBasedIconProvider;
import org.etlforge.ui.ResourceBundleIconProvider;
import org.etlforge.ui.ResourceBundleStringResources;
import org.etlforge.ui.StringResources;

public final class ResourceLoader {
   public static final MnemonicStringFactory MNEMONIC_FACTORY = MnemonicStringFactory.newCharMarkerFactory('&');

   public ResourceBundle load(String var1, Set<Object> var2) {
      try {
         ResourceBundle var3 = ResourceBundle.getBundle(var1);
         if (var2 != null) {
            Enumeration var4 = var3.getKeys();

            while (var4.hasMoreElements()) {
               var2.add(var4.nextElement());
            }
         }

         return var3;
      } catch (Exception var5) {
         this.handleBundleException(var1, var5);
         return null;
      }
   }

   public ResourceBundle load(String var1) {
      return this.load(var1, null);
   }

   public StringResources loadStringsFromResourceBundle(String var1) {
      try {
         return new ResourceBundleStringResources(var1, MNEMONIC_FACTORY);
      } catch (Exception var3) {
         this.handleBundleException(var1, var3);
         return null;
      }
   }

   public IconProvider loadIconsFromResourceBundle(String var1) {
      try {
         return new ResourceBundleIconProvider(var1);
      } catch (Exception var3) {
         this.handleBundleException(var1, var3);
         return null;
      }
   }

   public IconProvider loadIconsFromPropertiesFile(String var1, String var2) {
      try {
         return new PropertiesBasedIconProvider(var1, var2);
      } catch (Exception var4) {
         this.handleBundleException(var1, var4);
         return null;
      }
   }

   private void handleBundleException(String var1, Exception var2) {
      String var3 = "Failed to load the resource bundle " + var1;
      FatalConfigurationError.throwError(5, ResourceLoader.class, var2, var3);
   }
}
