package com.etlforge.integration.server.license;

import com.google.common.base.Preconditions;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.server.ServerGuidLookup;
import org.etlforge.integration.server.exception.ServerConfigurationException;

public final class MaskedServerGuidKeyBaseGenerator extends MaskingLicenseKeyBaseGenerator {
   private final ServerGuidLookup guidLookup;

   public MaskedServerGuidKeyBaseGenerator(ServerGuidLookup var1) {
      Preconditions.checkNotNull(var1, "guidLookup");
      this.guidLookup = var1;
   }

   @Override
   protected String getValueToMask() throws ServerLicenseException {
      try {
         ServerGuid var1 = this.guidLookup.lookupGuid();
         return var1.toString().toLowerCase();
      } catch (ServerConfigurationException var2) {
         throw new ServerLicenseException(
            "Failed to retrieve license related information from the ETLForge server. The server reported the following errror: " + var2.getMessage(), var2
         );
      }
   }
}
