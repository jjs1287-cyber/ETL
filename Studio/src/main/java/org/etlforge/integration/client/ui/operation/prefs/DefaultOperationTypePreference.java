package org.etlforge.integration.client.ui.operation.prefs;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.FixedSetPreference;
import org.etlforge.application.prefs.PreferenceFactory;
import org.etlforge.integration.data.entity.operation.OperationType;

public final class DefaultOperationTypePreference {
   private static final FixedSetPreference<OperationType> INSTANCE = createPref();

   public static FixedSetPreference<OperationType> instance() {
      return INSTANCE;
   }

   public static OperationType get() {
      return instance().get();
   }

   private static FixedSetPreference<OperationType> createPref() {
      PreferenceFactory var0 = ApplicationConfiguration.getPreferenceFactory();
      String[] var1 = new String[]{"Operations"};
      String var2 = "OperationType";
      return var0.newFixedSetPreference(var1, var2, OperationType.getSelectableTypes(), OperationType.getDefaultType(), "name");
   }

   private DefaultOperationTypePreference() {
   }
}
