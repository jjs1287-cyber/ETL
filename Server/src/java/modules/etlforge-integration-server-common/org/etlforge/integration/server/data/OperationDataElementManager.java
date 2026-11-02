package org.etlforge.integration.server.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.db.KongaDbUtils;

public class OperationDataElementManager {
   private static final String PREPARED_INSERT_SQL = "INSERT INTO OperationDataElementTab (SessionId, DataElementName, StringValue) VALUES (?,?,?)";
   private Connection connection;
   private PreparedStatement preparedStatement;

   public OperationDataElementManager(Connection var1) throws SQLException {
      KongaDbUtils.isValidForWriting(var1);
      this.connection = var1;
   }

   public synchronized int addDataElements(Map<String, DataElement> var1) throws SQLException {
      int var2 = -1;
      this.preparedStatement = null;
      if (var1.isEmpty()) {
         return -1;
      }

      boolean var3 = this.connection.getAutoCommit();

      try {
         if (var3) {
            this.connection.setAutoCommit(false);
         }

         for (Entry var5 : var1.entrySet()) {
            var2 = this.addDataElement((String)var5.getKey(), (DataElement)var5.getValue(), var2, this.connection);
         }
      } finally {
         KongaDbUtils.close(this.preparedStatement);
         this.preparedStatement = null;
         if (var3) {
            this.connection.setAutoCommit(var3);
         }
      }

      return var2;
   }

   private int addDataElement(String var1, DataElement var2, int var3, Connection var4) throws SQLException {
      if (var3 < 0) {
         var3 = KongaDbUtils.getNextId("OperationDataElementTab", "SessionId", var4);
      }

      if (this.preparedStatement == null) {
         this.preparedStatement = var4.prepareStatement("INSERT INTO OperationDataElementTab (SessionId, DataElementName, StringValue) VALUES (?,?,?)");
      }

      this.preparedStatement.setLong(1, var3);
      this.preparedStatement.setString(2, var1);
      this.preparedStatement.setString(3, var2.getType().toString() + "." + var2.toString());
      this.preparedStatement.executeUpdate();
      return var3;
   }
}
