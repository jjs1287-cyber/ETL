package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.EnumPreference;
import org.etlforge.application.prefs.PreferenceFactory;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.EdgeType;

public final class EdgeTypePreference {
   private static final EnumPreference<EdgeType> INSTANCE = createPref();

   public static EnumPreference<EdgeType> instance() {
      return INSTANCE;
   }

   public static EdgeType get() {
      return instance().get();
   }

   private static EnumPreference<EdgeType> createPref() {
      PreferenceFactory var0 = ApplicationConfiguration.getPreferenceFactory();
      String[] var1 = OperationGraphPreferencesPage.PATH();
      String var2 = "EdgeType";
      return var0.newEnumPreference(var1, var2, EdgeType.STRAIGHT_LINE, EdgeType.class);
   }

   private EdgeTypePreference() {
   }
}
