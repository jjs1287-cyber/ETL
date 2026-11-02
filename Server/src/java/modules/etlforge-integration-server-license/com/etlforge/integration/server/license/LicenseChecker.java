package com.etlforge.integration.server.license;

public interface LicenseChecker {
   LicenseInfo checkLicenseKey(String var1, String var2) throws ServerLicenseException;
}
