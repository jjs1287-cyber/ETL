package org.etlforge.integration.server.db.msgdb;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.server.data.DataElement;

public class MsgDb {
   private Connection msgDbConnection;

   public MsgDb(Connection var1) throws SQLException {
      KongaDbUtils.isValidForWriting(var1);
      this.msgDbConnection = var1;
   }

   public Statement createStatement() throws SQLException {
      return this.msgDbConnection.createStatement();
   }

   public void setAutoCommit(boolean var1) throws SQLException {
      this.msgDbConnection.setAutoCommit(var1);
   }

   public void commit() throws SQLException {
      this.msgDbConnection.commit();
   }

   private void insertDataElementTextTab(long var1, String var3, String var4) throws SQLException {
      PreparedStatement var5 = null;

      try {
         String var6 = "INSERT INTO DataElementTextTab (MsgId, DataElementName, stringvalue) VALUES (?,?,?)";
         var5 = this.msgDbConnection.prepareStatement(var6);
         var5.setInt(1, (int)var1);
         var5.setString(2, var3);
         var5.setString(3, var4);
         var5.execute();
      } finally {
         KongaDbUtils.close(var5);
      }
   }

   private void insertDataElementTab(long var1, String var3, DataElement var4) throws SQLException {
      String var5 = "INSERT INTO DataElementTab (MsgId, DataElementName, DataType, Intvalue, DoubleValue, StringValue) VALUES ("
         + Long.toString(var1)
         + ", '"
         + var3
         + "', '"
         + var4.getType().toString()
         + "', ";
      switch (var4.getType()) {
         case BOOL:
         case BIT:
         case INT:
         case LONG:
            var5 = var5 + var4.toString() + ", null, null";
            break;
         case FLOAT:
         case DOUBLE:
            var5 = var5 + "null, " + var4.toString() + ", null";
            break;
         default:
            var5 = var5 + "null, null, '" + var4.toString() + "'";
      }

      var5 = var5 + ")";
      Statement var6 = null;

      try {
         var6 = this.msgDbConnection.createStatement();
         var6.executeUpdate(var5);
      } finally {
         KongaDbUtils.close(var6);
      }
   }

   public void insertProcessDataElement(long var1, String var3, DataElement var4) throws SQLException {
      if (var4.getType() == DataElement.Type.STRING) {
         String var5 = var4.toString();
         if (var5.length() > 7000) {
            this.insertDataElementTextTab(var1, var3, var5);
            return;
         }
      }

      this.insertDataElementTab(var1, var3, var4);
   }

   public long insertIncomingProcessMsg(String var1, String var2, String var3, String var4, String var5, int var6, int var7) throws SQLException {
      if (this.msgDbConnection.isReadOnly()) {
         throw new IllegalStateException("The connection is read-only!");
      }

      CallableStatement var8 = null;

      try {
         String var9 = "{? = CALL SpInsertIncomingMsgTab(?,?,?,?,?,?,?,?,?)}";
         var8 = this.msgDbConnection.prepareCall(var9);
         var8.registerOutParameter(1, 4);
         var8.setString(2, var1);
         var8.setString(3, var2);
         var8.setString(4, var3);
         var8.setString(5, "{" + var4 + ";0;" + var5 + ";0;}");
         long var10 = System.currentTimeMillis() / 1000L;
         var8.setInt(6, (int)var10);
         var8.setInt(7, -1);
         var8.setString(8, "");
         var8.setInt(9, var6);
         var8.setInt(10, var7);
         var8.execute();
         return var8.getInt(1);
      } finally {
         KongaDbUtils.close(var8);
      }
   }

   public void flagMessageForExecution(long var1) throws SQLException {
      Statement var3 = null;

      try {
         var3 = this.msgDbConnection.createStatement();
         var3.execute("UPDATE IncomingMsgTab SET IsExecuting = 0 WHERE MsgId = " + Long.toString(var1));
      } finally {
         KongaDbUtils.close(var3);
         Object var7 = null;
      }
   }

   public enum SendingProgressStatus {
      ERROR(-1),
      NOT_SENT(0),
      SENDING(1),
      SENT(2);

      private int progressStatus;

      SendingProgressStatus(int var3) {
         this.progressStatus = var3;
      }

      public int toInt() {
         return this.progressStatus;
      }
   }
}
