package org.etlforge.integration.client.ui.interchange.component.tree;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.BooleanPreference;

public final class ShowOperationContentsInTreePreference {
   public static final BooleanPreference INSTANCE = create();

   public static boolean get() {
      return INSTANCE.get();
   }

   private static BooleanPreference create() {
      String[] var0 = new String[]{"Project"};
      String var1 = "ShowOperationContentsInTree";
      Boolean var2 = Boolean.FALSE;
      return ApplicationConfiguration.getPreferenceFactory().newBooleanPreference(var0, var1, var2);
   }

   private ShowOperationContentsInTreePreference() {
   }
}
