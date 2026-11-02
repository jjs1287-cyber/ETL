package org.etlforge.db;

public class ConnectionStringPasswordExtractor {
   private static final String KEY = "pwd=";
   private final String connection;
   private final String password;

   public ConnectionStringPasswordExtractor(String var1) {
      if (var1 == null) {
         throw new NullPointerException("connection");
      }

      this.connection = var1;
      this.password = this.extractPassword();
   }

   public boolean isDefiningPassword() {
      return this.password != null;
   }

   public String getPassword() {
      return this.password;
   }

   public String removePassword() {
      if (this.password == null) {
         return this.connection;
      }

      int var1 = this.connection.toLowerCase().indexOf("pwd=");
      int var2 = var1 + "pwd=".length() + this.password.length();
      String var3 = this.connection.substring(0, var1);
      if (var2 < this.connection.length()) {
         if (this.connection.charAt(var2) == ';' && (var3.length() == 0 || var3.endsWith(";"))) {
            var2++;
         }

         if (var2 < this.connection.length()) {
            var3 = var3 + this.connection.substring(var2);
         }
      }

      return var3;
   }

   private String extractPassword() {
      int var1 = this.findPwdKeyStartPos();
      if (var1 < 0) {
         return null;
      } else {
         int var2 = var1 + "pwd=".length();
         if (var2 < this.connection.length() && this.connection.charAt(var2) != ';') {
            int var3 = this.connection.indexOf(59, var2);
            return var3 < 0 ? this.connection.substring(var2) : this.connection.substring(var2, var3);
         } else {
            return null;
         }
      }
   }

   private int findPwdKeyStartPos() {
      String var1 = this.connection.toLowerCase();
      if (var1.startsWith("pwd=")) {
         return 0;
      }

      int var2 = var1.indexOf(";pwd=");
      return var2 >= 0 ? var2 + 1 : -1;
   }
}
