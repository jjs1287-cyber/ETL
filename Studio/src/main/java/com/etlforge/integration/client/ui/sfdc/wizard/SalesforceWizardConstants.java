package com.etlforge.integration.client.ui.sfdc.wizard;

import org.etlforge.application.config.ApplicationConfiguration;

public final class SalesforceWizardConstants {
   public static final String SALESFORCE_OPERATION_VARIABLE = "wizard.salesforce.operation";
   public static final int CHUNK_SIZE = 200;
   public static final int THREADS = ApplicationConfiguration.getInt("Sfdc.Soap.Threads", 2);

   private SalesforceWizardConstants() {
   }
}
