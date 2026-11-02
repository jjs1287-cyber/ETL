package org.etlforge.integration.client.database;

import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.DriverDescriptorVisitor;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.database.driver.JdbcDriverDesriptorBuilder;
import org.etlforge.integration.database.driver.OdbcDriverDescriptor;
import org.etlforge.integration.database.driver.OdbcDriverDescriptorFactory;
import org.etlforge.util.persist.Persistor;

final class DriverDescriptorPersistor {
   private static final int ODBC_TYPE = 0;
   private static final int JDBC_TYPE = 1;
   private static final String TYPE = "Type";
   private static final String NAME = "Name";
   private static final String BEGIN_QUOTE = "BeginQuote";
   private static final String END_QUOTE = "EndQuote";
   private static final String CLASS_NAME = "Class";
   private static final String SUBPROTOCOL = "subprotocol";

   public static void persist(DriverDescriptor var0, Persistor var1) {
      DriverDescriptorPersistor.PersistorVisitor var2 = new DriverDescriptorPersistor.PersistorVisitor(var1);
      var0.accept(var2);
   }

   public static DriverDescriptor restore(Persistor var0) {
      int var1 = var0.getInteger("Type");
      String var2 = var0.getString("Name");
      String var3 = var0.getString("BeginQuote");
      String var4 = var0.getString("EndQuote");
      BeginEndQuote var5 = BeginEndQuote.fromStrings(var3, var4);
      DriverDescriptor var6 = null;
      if (var1 == 0) {
         var6 = OdbcDriverDescriptorFactory.createDescriptor(var2, var5);
      } else {
         if (var1 != 1) {
            throw new IllegalArgumentException("Invalid type value: " + var1);
         }

         var6 = new JdbcDriverDesriptorBuilder()
            .name(var2)
            .subprotocol(var0.getString("subprotocol"))
            .driverClass(var0.getString("Class"))
            .quotes(var5)
            .build();
      }

      return var6;
   }

   private DriverDescriptorPersistor() {
   }

   private static class PersistorVisitor implements DriverDescriptorVisitor {
      private final Persistor persistor;

      public PersistorVisitor(Persistor var1) {
         this.persistor = var1;
      }

      @Override
      public void visit(JdbcDriverDescriptor var1) {
         this.persistCommonData(var1);
         this.persistor.putString("Class", var1.getDriverClass());
         this.persistor.putString("subprotocol", var1.getSubprotocol());
         this.persistor.putInteger("Type", 1);
      }

      @Override
      public void visit(OdbcDriverDescriptor var1) {
         this.persistCommonData(var1);
         this.persistor.putInteger("Type", 0);
      }

      private void persistCommonData(DriverDescriptor var1) {
         this.persistor.putString("Name", var1.getName());
         BeginEndQuote var2 = var1.getBeginEndQuotes();
         if (var2 != null) {
            this.persistor.putString("BeginQuote", var2.begin);
            this.persistor.putString("EndQuote", var2.end);
         }
      }
   }
}
