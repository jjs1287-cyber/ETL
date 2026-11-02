package org.etlforge.integration.client.ui;

import com.google.common.collect.Sets;
import java.util.ResourceBundle;
import java.util.Set;
import org.etlforge.application.ui.resources.ResourceLoader;

public final class Resources {
   private static final Set<Object> stringKeys = Sets.newHashSet();
   public static final ResourceBundle CommonStrings = loadStrings();

   private static ResourceBundle loadStrings() {
      ResourceLoader var0 = new ResourceLoader();
      return var0.load("org.etlforge.integration.client.ui.resources.CommonStrings", stringKeys);
   }

   public static boolean stringExists(String var0) {
      return stringKeys.contains(var0);
   }

   public static String getCommonString(String var0) {
      return stringExists(var0) ? CommonStrings.getString(var0) : "";
   }

   private Resources() {
   }
}
