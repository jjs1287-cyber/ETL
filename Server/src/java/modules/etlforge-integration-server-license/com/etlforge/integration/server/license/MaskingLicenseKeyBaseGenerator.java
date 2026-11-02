package com.etlforge.integration.server.license;

abstract class MaskingLicenseKeyBaseGenerator implements LicenseKeyBaseGenerator {
   protected abstract String getValueToMask() throws ServerLicenseException;

   @Override
   public final String getKeyBase() throws ServerLicenseException {
      return MaskingStrategy.mask(this.getValueToMask());
   }
}
