package org.etlforge.integration.activity.ui;

public class MaxQueueEntriesToFetchPreference extends MaxActivityEntriesToFetchPreference {
   public MaxQueueEntriesToFetchPreference() {
      super("MaxQueueEntries", 1, 10000);
   }
}
