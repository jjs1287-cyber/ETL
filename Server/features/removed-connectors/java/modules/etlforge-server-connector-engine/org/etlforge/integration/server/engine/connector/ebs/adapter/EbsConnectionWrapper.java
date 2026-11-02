package org.etlforge.integration.server.engine.connector.ebs.adapter;

import com.ubs.core.adapter.ebs.connection.EbsDesignConnection;
import com.ubs.core.adapter.ebs.connection.EbsExecutionConnection;
import com.ubs.core.adapter.exception.AdapterResourceException;
import java.util.Properties;
import org.etlforge.integration.server.engine.connector.exception.ConnectorAdapterRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorEndpointRuntimeException;

public class EbsConnectionWrapper {
   public static EbsDesignConnection getOpenDesignConnection(Properties var0) {
      try {
         EbsDesignConnection var1 = new EbsDesignConnection(var0);
         var1.openConnection();
         return var1;
      } catch (Throwable var2) {
         throw new ConnectorEndpointRuntimeException(var2.getMessage());
      }
   }

   public static EbsExecutionConnection getOpenExecutionConnection(Properties var0) {
      try {
         EbsExecutionConnection var1 = new EbsExecutionConnection(var0);
         var1.openConnection();
         return var1;
      } catch (Throwable var2) {
         throw new ConnectorEndpointRuntimeException(var2.getMessage());
      }
   }

   public static void close(EbsDesignConnection var0) {
      if (var0 != null) {
         try {
            var0.closeConnection();
         } catch (AdapterResourceException var2) {
            throw new ConnectorAdapterRuntimeException(var2);
         }
      }
   }

   public static void close(EbsExecutionConnection var0) {
      if (var0 != null) {
         try {
            var0.closeConnection();
         } catch (AdapterResourceException var2) {
            throw new ConnectorAdapterRuntimeException(var2);
         }
      }
   }
}
