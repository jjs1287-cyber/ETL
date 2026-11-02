package com.etlforge.integration.license.client.ui;

import org.etlforge.application.config.ApplicationConfiguration;

public final class LicenseRequestEmail {
   public static final String EMAIL = ApplicationConfiguration.getString("License.Request.Email", "customersupport@etlforge.invalid");

   private LicenseRequestEmail() {
   }
}
