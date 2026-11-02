package com.etlforge.integration.server.license;

import org.etlforge.integration.server.exception.ServerConfigurationException;

public interface LicenseActivator {
   void activateLicense(String var1) throws ServerConfigurationException, ServerLicenseException;
}
