package org.etlforge.integration.activity.ui;

public class MaxLogEntriesToFetchPreference extends MaxActivityEntriesToFetchPreference {
   public MaxLogEntriesToFetchPreference() {
      super("MaxLogEntries", 1, 10000);
   }
}
