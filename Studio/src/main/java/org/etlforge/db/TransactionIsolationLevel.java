package org.etlforge.db;

import java.sql.Connection;
import java.sql.SQLException;

public enum TransactionIsolationLevel {
   DEFAULT(0),
   READ_UNCOMMITTED(1),
   READ_COMMITTED(2),
   REPEATABLE_READ(4),
   SERIALIZABLE(8);

   private final int code;

   TransactionIsolationLevel(int var3) {
      this.code = var3;
   }

   public int getCode() {
      return this.code;
   }

   public void apply(Connection var1) throws SQLException {
      var1.setTransactionIsolation(this.code);
   }

   public static TransactionIsolationLevel fromCode(int var0) {
      for (TransactionIsolationLevel var4 : values()) {
         if (var4.code == var0) {
            return var4;
         }
      }

      return DEFAULT;
   }
}
