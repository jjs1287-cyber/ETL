package com.etlforge.integration.server.license;

public interface LicenseKeyStore {
   String readLicenseKey() throws ServerLicenseException;

   void writeLicenseKey(String var1) throws ServerLicenseException;

   String readCookie() throws ServerLicenseException;

   void removeCookie() throws ServerLicenseException;
}
