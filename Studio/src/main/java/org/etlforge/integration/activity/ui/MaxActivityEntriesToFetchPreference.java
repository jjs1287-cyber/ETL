package org.etlforge.integration.activity.ui;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.IntPreference;

public abstract class MaxActivityEntriesToFetchPreference {
   private static final String[] PATH = new String[]{"Operations"};
   private final IntPreference pref;

   protected MaxActivityEntriesToFetchPreference(String var1, int var2, int var3) {
      this.pref = ApplicationConfiguration.getPreferenceFactory().newIntPreference(PATH, var1, var2, var3, 100);
   }

   public final void set(int var1) {
      this.pref.set(var1);
   }

   public final int get() {
      return this.pref.get();
   }

   public final IntPreference getPreference() {
      return this.pref;
   }
}
