package org.etlforge.integration.data.test;

import org.etlforge.application.config.ApplicationConfiguration;

public final class PersistTestResultsProperty {
   public static final boolean ENABLED = ApplicationConfiguration.getBoolean("PersistTestResultsLocally", false);

   private PersistTestResultsProperty() {
   }
}
