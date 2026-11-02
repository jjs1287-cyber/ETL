package org.etlforge.integration.client.server.webservice;

import com.google.common.base.Preconditions;
import java.rmi.RemoteException;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCall;

public abstract class WebServiceServerCall implements ServerCall {
   private final WebServiceConfiguration configuration;

   protected WebServiceServerCall(WebServiceConfiguration var1) {
      this.configuration = Preconditions.checkNotNull(var1, "config");
   }

   protected final WebServiceConfiguration getConfiguration() {
      return this.configuration;
   }

   protected final IntegrationServerException convert(RemoteException var1) {
      return this.configuration.convertToIntegrationServerException(var1);
   }
}
