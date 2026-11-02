package org.etlforge.integration.server.engine.connector.sap.adapter;

import com.ubs.core.adapter.exception.AdapterResourceException;
import com.ubs.core.adapter.sap.idoc.connection.SapIDocDesignConnection;
import com.ubs.core.adapter.sap.idoc.connection.SapIDocExecutionConnection;
import com.ubs.core.adapter.sap.idoc.factory.SapIDocDesignConnectionFactory;
import com.ubs.core.adapter.sap.idoc.factory.SapIDocExecutionConnectionFactory;
import com.ubs.core.adapter.sap.rfc.connection.SapDesignConnection;
import com.ubs.core.adapter.sap.rfc.connection.SapExecutionConnection;
import com.ubs.core.adapter.sap.rfc.factory.SapDesignConnectionFactory;
import com.ubs.core.adapter.sap.rfc.factory.SapExecutionConnectionFactory;
import java.util.Properties;
import org.etlforge.integration.server.engine.connector.exception.ConnectorAdapterRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorEndpointRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerAccessRuntimeException;

public class SapConnectorConnectionAdapter {
   static void processSpecialException(Throwable var0) {
      if (var0 instanceof Error) {
         String var1 = "";
         Throwable var2 = var0.getCause();
         if (var2 != null && var2 instanceof ClassNotFoundException) {
            var1 = "Please confirm the required jar files (sapidoc3, sapjco3) in under the folder tomcat/webapps/axis/WEB-INF/lib. They are not included in the ETLForge installer.\nThe reason: "
               + var2.getMessage();
         } else if (var0 instanceof ExceptionInInitializerError) {
            var1 = "Please confirm the right sapjco3.dll in the bin folder of the jittebit server. \nThe reason: " + var0.getMessage();
         } else if (var0 instanceof NoClassDefFoundError) {
            var1 = "You should restart tomcat server and try again since the reason may hide the real cause at second time!\nThe reason: " + var0.getMessage();
         } else {
            var1 = "The reason: " + var0.getMessage();
         }

         var1 = var1 + "\n if required libs do not exist in the ETLForge server, copy them and restart the tomcat server!";
         throw new ConnectorServerAccessRuntimeException(var1);
      }
   }

   public static SapExecutionConnection getOpenExecutionConnection(Properties var0) {
      SapExecutionConnectionFactory var1 = SapExecutionConnectionFactory.getInstance();

      try {
         SapExecutionConnection var2 = var1.createConnection(var0);
         var2.openConnection();
         return var2;
      } catch (Throwable var3) {
         processSpecialException(var3);
         throw new ConnectorEndpointRuntimeException(var3);
      }
   }

   public static void closeConnection(SapExecutionConnection var0) {
      try {
         if (var0 != null) {
            var0.closeConnection();
         }
      } catch (AdapterResourceException var2) {
         throw new ConnectorAdapterRuntimeException(var2);
      }
   }

   public static SapDesignConnection getOpenDesignConnection(Properties var0) {
      SapDesignConnectionFactory var1 = SapDesignConnectionFactory.getInstance();

      try {
         SapDesignConnection var2 = var1.createConnection(var0);
         var2.openConnection();
         return var2;
      } catch (Throwable var3) {
         processSpecialException(var3);
         throw new ConnectorEndpointRuntimeException(var3);
      }
   }

   public static void closeConnection(SapDesignConnection var0) {
      if (var0 != null) {
         try {
            var0.closeConnection();
         } catch (AdapterResourceException var2) {
            throw new ConnectorAdapterRuntimeException(var2);
         }
      }
   }

   public static SapIDocExecutionConnection getOpenSapIDocExecutionConnection(Properties var0) {
      SapIDocExecutionConnectionFactory var1 = SapIDocExecutionConnectionFactory.getInstance();

      try {
         SapIDocExecutionConnection var2 = var1.createConnection(var0);
         var2.openConnection();
         return var2;
      } catch (Throwable var3) {
         processSpecialException(var3);
         throw new ConnectorEndpointRuntimeException(var3);
      }
   }

   public static void closeConnection(SapIDocExecutionConnection var0) {
      try {
         if (var0 != null) {
            var0.closeConnection();
         }
      } catch (AdapterResourceException var2) {
         throw new ConnectorAdapterRuntimeException(var2);
      }
   }

   public static SapIDocDesignConnection getOpenSapIDocDesignConnection(Properties var0) {
      SapIDocDesignConnectionFactory var1 = SapIDocDesignConnectionFactory.getInstance();

      try {
         SapIDocDesignConnection var2 = var1.createConnection(var0);
         var2.openConnection();
         return var2;
      } catch (Throwable var3) {
         processSpecialException(var3);
         throw new ConnectorEndpointRuntimeException(var3);
      }
   }

   public static void closeConnection(SapIDocDesignConnection var0) {
      if (var0 != null) {
         try {
            var0.closeConnection();
         } catch (AdapterResourceException var2) {
            throw new ConnectorAdapterRuntimeException(var2);
         }
      }
   }
}
