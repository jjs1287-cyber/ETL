package org.etlforge.integration.server.engine.connector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.server.engine.connector.endpoint.ConnectorEndpointTypeDef;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;

public class ConnectorEndpointTab {
   public static String getEndpointPassword(String var0, String var1, Connection var2) {
      String var3 = ConnectorEndpointTypeDef.getEndpointPasswordSql(var1);
      if (var3 == null) {
         return null;
      }

      String var4 = var3 + " where guid='" + var0 + "' ";
      return lookupEndpointPasswordFromDb(var4, var2);
   }

   private static String lookupEndpointPasswordFromDb(String var0, Connection var1) {
      String var2 = null;
      Statement var3 = null;
      ResultSet var4 = null;

      try {
         var3 = var1.createStatement();
         var4 = var3.executeQuery(var0);
         if (var4.next()) {
            var2 = var4.getString(1);
         }

         return var2;
      } catch (SQLException var9) {
         throw new ConnectorServerRuntimeException(var9);
      } finally {
         KongaDbUtils.close(var4);
         KongaDbUtils.close(var3);
      }
   }

   private ConnectorEndpointTab() {
   }

   public static void main(String[] var0) {
      System.out.println("start");
      String var1 = "12345";
      System.out.println(var1);
      System.out.println("end");
   }
}
