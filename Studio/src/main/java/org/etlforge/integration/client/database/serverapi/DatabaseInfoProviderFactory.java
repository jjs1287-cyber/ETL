package org.etlforge.integration.client.database.serverapi;

import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.DriverDescriptorVisitor;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.database.driver.OdbcDriverDescriptor;

public final class DatabaseInfoProviderFactory {
   public static DatabaseInfoProvider createProvider(ConnectionParams var0) {
      DriverDescriptor var1 = var0.getDriver();
      DatabaseInfoProviderFactory.Visitor var2 = new DatabaseInfoProviderFactory.Visitor();
      var1.accept(var2);
      return var2.provider;
   }

   private DatabaseInfoProviderFactory() {
   }

   private static final class Visitor implements DriverDescriptorVisitor {
      private DatabaseInfoProvider provider;

      private Visitor() {
      }

      @Override
      public void visit(JdbcDriverDescriptor var1) {
         IntegrationServer var2 = IntegrationServer.getInstance();
         this.provider = var2.getServerCall(JdbcInfoProvider.class);
      }

      @Override
      public void visit(OdbcDriverDescriptor var1) {
         IntegrationServer var2 = IntegrationServer.getInstance();
         this.provider = var2.getServerCall(OdbcInfoProvider.class);
      }
   }
}
