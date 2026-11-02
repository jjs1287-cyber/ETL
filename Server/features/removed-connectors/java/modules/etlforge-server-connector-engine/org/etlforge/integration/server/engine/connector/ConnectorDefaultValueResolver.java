package org.etlforge.integration.server.engine.connector;

import com.google.common.base.Strings;
import org.etlforge.integration.dataelement.DataElementResolver;
import org.etlforge.integration.server.conf.PreconfiguredDataElements;

public class ConnectorDefaultValueResolver {
   private static ConnectorDefaultValueResolver instance = new ConnectorDefaultValueResolver();
   private final DataElementResolver deResolver = new DataElementResolver();

   public static String resolve(String var0) {
      return instance.resolveImpl(var0);
   }

   public ConnectorDefaultValueResolver() {
      this.deResolver.setContext(PreconfiguredDataElements.getContext());
   }

   public String resolveImpl(String var1) {
      return Strings.isNullOrEmpty(var1) ? var1 : this.deResolver.resolve(var1);
   }
}
