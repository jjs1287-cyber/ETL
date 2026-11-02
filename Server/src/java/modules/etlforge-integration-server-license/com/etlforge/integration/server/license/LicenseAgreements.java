package com.etlforge.integration.server.license;

import java.io.IOException;
import java.io.InputStream;
import org.etlforge.integration.server.exception.ServerConfigurationException;

public interface LicenseAgreements {
   InputStream getEvaluationLicense() throws ServerConfigurationException, IOException;

   InputStream getSubscriptionLicense() throws ServerConfigurationException, IOException;

   InputStream getPerpetualLicense() throws ServerConfigurationException, IOException;
}
