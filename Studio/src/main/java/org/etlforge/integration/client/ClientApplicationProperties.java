package org.etlforge.integration.client;

import java.io.IOException;
import org.etlforge.application.config.FatalConfigurationError;
import org.etlforge.util.properties.ReadOnlyProperties;
import org.etlforge.util.properties.ReadOnlyPropertiesFactory;
import org.etlforge.util.properties.SystemPropertiesDecorator;

public final class ClientApplicationProperties {
   private static ClientApplicationProperties INSTANCE;
   private final ReadOnlyProperties props;

   private ClientApplicationProperties() throws IOException {
      ReadOnlyPropertiesFactory var1 = ReadOnlyPropertiesFactory.newInstance();
      this.props = new SystemPropertiesDecorator(var1.load("client.properties"));
   }

   public static ReadOnlyProperties getInstance() {
      return INSTANCE.props;
   }

   static {
      try {
         INSTANCE = new ClientApplicationProperties();
      } catch (IOException var1) {
         FatalConfigurationError.throwError(9, "ClientApplicationPropertes", var1, "Failed to load the file client.propertes");
      }
   }
}
