package com.etlforge.integration.salesforce.wsdl;

import java.util.List;
import org.etlforge.integration.salesforce.LoginCredentials;

public final class WsdlWithCredentials {
   public final String wsdl;
   public final List<LoginCredentials> credentials;

   public WsdlWithCredentials(String var1, List<LoginCredentials> var2) {
      this.wsdl = var1;
      this.credentials = var2;
   }
}
