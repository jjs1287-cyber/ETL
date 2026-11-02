package com.etlforge.integration.server.license;

public interface LicenseKeyBaseGenerator {
   String getKeyBase() throws ServerLicenseException;
}
