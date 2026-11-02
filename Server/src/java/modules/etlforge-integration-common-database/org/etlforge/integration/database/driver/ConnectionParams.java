package org.etlforge.integration.database.driver;

import java.util.Arrays;
import org.apache.commons.lang.StringUtils;
import org.etlforge.db.TransactionIsolationLevel;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.util.net.Port;

public final class ConnectionParams implements Cloneable {
   private String server;
   private Port port = Port.DEFAULT;
   private String database;
   private String user;
   private String password;
   private String manualConnectionString;
   private String additionalParams;
   private String driverName;
   private DriverDescriptor driver;
   private BeginEndQuote beginEndQuotes;
   private TransactionIsolationLevel transactionIsolationLevel = TransactionIsolationLevel.DEFAULT;

   public String getServer() {
      return this.server;
   }

   public ConnectionParams setServer(String var1) {
      this.server = var1;
      return this;
   }

   public Port getPort() {
      return this.port;
   }

   public ConnectionParams setPort(Port var1) {
      this.port = var1;
      return this;
   }

   public String getDatabase() {
      return this.database;
   }

   public ConnectionParams setDatabase(String var1) {
      this.database = var1;
      return this;
   }

   public String getUser() {
      return this.user;
   }

   public ConnectionParams setUser(String var1) {
      this.user = var1;
      return this;
   }

   public String getPassword() {
      return this.password;
   }

   public ConnectionParams setPassword(String var1) {
      this.password = var1;
      return this;
   }

   public String getManualConnectionString() {
      return this.manualConnectionString;
   }

   public ConnectionParams setManualConnectionString(String var1) {
      this.manualConnectionString = var1;
      return this;
   }

   public String getAdditionalParams() {
      return this.additionalParams;
   }

   public ConnectionParams setAdditionalParams(String var1) {
      this.additionalParams = var1;
      return this;
   }

   public String getDriverName() {
      return this.driverName;
   }

   public ConnectionParams setDriverName(String var1) {
      this.driverName = var1;
      return this;
   }

   public DriverDescriptor getDriver() {
      return this.driver;
   }

   public ConnectionParams setDriver(DriverDescriptor var1) {
      this.driver = var1;
      return this;
   }

   public TransactionIsolationLevel getTransactionIsolationLevel() {
      return this.transactionIsolationLevel;
   }

   public ConnectionParams setTransactionIsolationLevel(TransactionIsolationLevel var1) {
      this.transactionIsolationLevel = var1;
      return this;
   }

   public BeginEndQuote getBeginEndQuotes() {
      return this.beginEndQuotes;
   }

   public ConnectionParams setBeginEndQuotes(BeginEndQuote var1) {
      this.beginEndQuotes = var1;
      return this;
   }

   public String getConnectionString() {
      return StringUtils.isNotEmpty(this.manualConnectionString) && this.cannotAskDriverForMoreInfoAboutManualConnection()
         ? this.manualConnectionString
         : this.driver.getConnectionString(this);
   }

   private boolean cannotAskDriverForMoreInfoAboutManualConnection() {
      return this.driver == null || StringUtils.isEmpty(this.password);
   }

   public ConnectionParams clone() {
      try {
         return (ConnectionParams)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof ConnectionParams ? this.equals((ConnectionParams)var1) : false;
      }
   }

   private boolean equals(ConnectionParams var1) {
      return Arrays.equals(this.objectsForComparison(), var1.objectsForComparison());
   }

   @Override
   public int hashCode() {
      int var1 = 17;

      for (Object var5 : this.objectsForComparison()) {
         var1 = 31 * var1 + (var5 != null ? var5.hashCode() : 0);
      }

      return var1;
   }

   private Object[] objectsForComparison() {
      return new Object[]{
         this.server,
         this.database,
         this.driverName,
         this.driver,
         this.port,
         this.user,
         this.password,
         this.manualConnectionString,
         this.additionalParams,
         this.transactionIsolationLevel,
         this.beginEndQuotes
      };
   }
}
