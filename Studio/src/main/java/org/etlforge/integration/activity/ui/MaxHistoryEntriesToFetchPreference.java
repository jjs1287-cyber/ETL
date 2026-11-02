package org.etlforge.integration.activity.ui;

public final class MaxHistoryEntriesToFetchPreference extends MaxActivityEntriesToFetchPreference {
   public MaxHistoryEntriesToFetchPreference() {
      super("MaxHistoryEntries", 1, 10000);
   }
}
