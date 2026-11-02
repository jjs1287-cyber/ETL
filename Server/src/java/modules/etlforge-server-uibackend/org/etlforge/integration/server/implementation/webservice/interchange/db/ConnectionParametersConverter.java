package org.etlforge.integration.server.implementation.webservice.interchange.db;

import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.dataelement.DataElementContext;
import org.etlforge.integration.dataelement.DataElementResolver;
import org.etlforge.util.net.Port;

final class ConnectionParametersConverter {
   private final DataElementResolver deResolver = new DataElementResolver();

   public ConnectionParametersConverter(DataElementContext var1) {
      this.deResolver.setContext(var1);
   }

   public ConnectionParams convert(String var1, WsConnectionParams var2) {
      ConnectionParams var3 = new ConnectionParams();
      var3.setServer(this.convert(var2.getServer()))
         .setPort(Port.valueOf(var2.getPort()))
         .setDatabase(this.convert(var2.getDatabase()))
         .setUser(this.convert(var2.getUser()))
         .setPassword(this.convert(var2.getPassword()))
         .setManualConnectionString(this.convert(var2.getManualConnection()))
         .setAdditionalParams(this.convert(var2.getAdditionalParams()))
         .setDriverName(var1);
      return var3;
   }

   private String convert(String var1) {
      return this.deResolver.resolve(var1);
   }
}
