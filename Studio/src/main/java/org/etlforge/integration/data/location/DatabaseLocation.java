package org.etlforge.integration.data.location;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.db.TransactionIsolationLevel;
import org.etlforge.integration.data.ExportableProperty;
import org.etlforge.integration.data.ExportableStringProperty;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.PasswordUtils;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.driver.DriverDescriptorFactory;
import org.etlforge.integration.data.location.driver.ExportableDriverProperty;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.DriverDescriptorVisitor;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.database.driver.OdbcDriverDescriptor;
import org.etlforge.util.net.Port;
import org.etlforge.util.net.PortAccessible;
import org.etlforge.util.persist.Persistor;

public final class DatabaseLocation extends DataLocation implements LoginProtectedResource, PortAccessible {
   public static final int DEFAULT_TIMEOUT = 300;
   public static final int DEFAULT_FETCH_SIZE = 0;
   public static final String DRIVER_PROPERTY = "driver";
   public static final String DRIVER_CLASS_PROPERTY = "driver_class";
   public static final String JDBC_SUBPROTOCOL = "jdbc_subprotocol";
   public static final String SERVER_PROPERTY = "locator";
   public static final String DATABASE_PROPERTY = "dbname";
   public static final String TIMEOUT_PROPERTY = "timeout";
   public static final String LOGIN_PROPERTY = "login";
   private static final String PASSWORD_PROPERTY = "password";
   public static final String CONNECT_PARAMETERS_PROPERTY = "optional_connectstring";
   public static final String CONNECT_STRING_PROPERTY = "connectstring";
   public static final String PORT_PROPERTY = "port";
   public static final String TRANSACTION_ISOLATION_LEVEL = "transactionIsolationLevel";
   public static final String FETCH_SIZE = "fetch_size";
   public static final String MANUAL_MODE_PROPERTY = "manual_mode";
   @Deprecated
   private static final String BEGIN_QUOTE_PROPERTY = "begin_quote";
   @Deprecated
   private static final String END_QUOTE_PROPERTY = "end_quote";
   public static final String DRIVER_BEGIN_QUOTE = "driver_begin_quote";
   public static final String DRIVER_END_QUOTE = "driver_end_quote";
   public static final String DRIVER_QUOTES = "driver_quotes";
   public static final String DB_BEGIN_QUOTE = "db_begin_quote";
   public static final String DB_END_QUOTE = "db_end_quote";
   public static final String DB_QUOTES = "db_quotes";
   public static final String BEGIN_END_QUOTE = "begin_end_quote";
   public static final String DRIVER_TYPE = "drivertype";
   private transient PortAccessibleSupport portSupport = new PortAccessibleSupport(this, "port");

   public DatabaseLocation() {
      super(DataLocationType.Database);
   }

   public DatabaseLocation(DatabaseLocation var1) {
      super(DataLocationType.Database);
      StringKeyValuePairs var2 = var1.getData();

      for (String var4 : var2.getKeys()) {
         this.setProperty(var4, var2.get(var4));
      }
   }

   public static String[] getPropertyKeys() {
      return new String[]{
         "driver",
         "drivertype",
         "driver_class",
         "jdbc_subprotocol",
         "locator",
         "dbname",
         "timeout",
         "login",
         "password",
         "optional_connectstring",
         "connectstring",
         "port",
         "transactionIsolationLevel",
         "fetch_size",
         "manual_mode",
         "driver_begin_quote",
         "driver_end_quote",
         "db_begin_quote",
         "db_end_quote"
      };
   }

   @Override
   public String getName() {
      String var1 = this.getDatabase();
      return var1 != null && var1.length() != 0 ? LocationMessages.getFormattedMessage("DatabaseLocation.Name", var1, this.getServer()) : this.getServer();
   }

   public String getConnectionString() {
      if (this.isManualMode()) {
         String var1 = this.getManualConnectionString();
         return this.addBackPasswordIfNeeded(var1);
      } else {
         return this.getConnectionStringFromDriver();
      }
   }

   private String addBackPasswordIfNeeded(String var1) {
      if (this.getDriver() instanceof OdbcDriverDescriptor && !var1.toLowerCase().contains("pwd")) {
         String var2 = this.getClearTextPassword();
         if (StringUtils.isNotBlank(var2)) {
            if (!var1.endsWith(";")) {
               var1 = var1 + ";";
            }

            var1 = var1 + "PWD=" + var2 + ";";
         }
      }

      return var1;
   }

   private String getClearTextPassword() {
      return PasswordUtils.getClearTextPassword(this);
   }

   private String getConnectionStringFromDriver() {
      DriverDescriptor var1 = this.getDriver();
      if (var1 == null) {
         return null;
      }

      ConnectionParams var2 = this.getConnectionParams();
      return var1.getConnectionString(var2);
   }

   public void setDriverBeginEndQuote(BeginEndQuote var1) {
      this.updateBeginEndQuote(var1, "driver_begin_quote", "driver_end_quote", "driver_quotes");
   }

   public void setDatabaseBeginEndQuote(BeginEndQuote var1) {
      this.updateBeginEndQuote(var1, "db_begin_quote", "db_end_quote", "db_quotes");
   }

   private void updateBeginEndQuote(BeginEndQuote var1, String var2, String var3, String var4) {
      BeginEndQuote var5;
      BeginEndQuote var6;
      BeginEndQuote var7;
      synchronized (this.getDataLock()) {
         var5 = this.getBeginEndQuoteImpl(var2, var3);
         var6 = this.getBeginEndQuote();
         String var9 = var1 != null ? var1.begin : null;
         String var10 = var1 != null ? var1.end : null;
         this.setProperty(var2, var9);
         this.setProperty(var3, var10);
         var7 = this.getBeginEndQuote();
      }

      this.firePropertyChange(var4, var5, var1);
      this.firePropertyChange("begin_end_quote", var6, var7);
   }

   public BeginEndQuote getBeginEndQuote() {
      synchronized (this.getDataLock()) {
         BeginEndQuote var2 = this.getDriverBeginEndQuote();
         if (var2 == null) {
            var2 = this.getDbBeginEndQuote();
            if (var2 == null) {
               var2 = BeginEndQuote.BLANK;
            }
         }

         return var2;
      }
   }

   private BeginEndQuote getDriverBeginEndQuote() {
      return this.getBeginEndQuoteImpl("driver_begin_quote", "driver_end_quote");
   }

   private BeginEndQuote getDbBeginEndQuote() {
      return this.getBeginEndQuoteImpl("db_begin_quote", "db_end_quote");
   }

   private BeginEndQuote getBeginEndQuoteImpl(String var1, String var2) {
      String var3 = this.getProperty(var1);
      String var4 = this.getProperty(var2);
      return BeginEndQuote.fromStrings(var3, var4);
   }

   public DriverDescriptor getDriver() {
      synchronized (this.getDataLock()) {
         return DriverDescriptorFactory.createDriver(this);
      }
   }

   public void setDriver(DriverDescriptor var1) {
      DriverDescriptor var2;
      BeginEndQuote var3;
      BeginEndQuote var4;
      synchronized (this.getDataLock()) {
         var2 = this.getDriver();
         var3 = this.getDriverBeginEndQuote();
         if (var1 != null) {
            var1.accept(new DatabaseLocation.DriverPropertySetter());
            var4 = var1.getBeginEndQuotes();
            this.setProperty("drivertype", var1.getType().getId());
         } else {
            this.setProperty("driver", "");
            this.setProperty("driver_begin_quote", (String)null);
            this.setProperty("driver_end_quote", (String)null);
            this.setProperty("driver_class", (String)null);
            var4 = null;
         }
      }

      this.firePropertyChange("driver", var2, var1);
      this.firePropertyChange("driver_quotes", var3, var4);
   }

   public String getDriverName() {
      return this.getProperty("driver");
   }

   public int getTimeout() {
      return this.getIntProperty("timeout", 300);
   }

   public void setTimeout(int var1) {
      int var2;
      synchronized (this.getDataLock()) {
         var2 = this.getTimeout();
         this.setProperty("timeout", String.valueOf(var1));
      }

      if (var1 != var2) {
         this.firePropertyChange("timeout", (int)var2, (int)var1);
      }
   }

   public String getServer() {
      return this.getProperty("locator");
   }

   public void setServer(String var1) {
      this.setStringProperty("locator", var1);
   }

   public String getDatabase() {
      return this.getProperty("dbname");
   }

   public void setDatabase(String var1) {
      this.setStringProperty("dbname", var1);
   }

   @Override
   public String getLogin() {
      return this.getProperty("login");
   }

   public void setLogin(String var1) {
      this.setStringProperty("login", var1);
   }

   @Override
   public Optional<Password> getPassword() {
      return PasswordUtils.getPassword(this, "password");
   }

   public void setPassword(Optional<Password> var1) {
      PasswordUtils.setPassword(this, "password", var1);
   }

   public String getAdditionalConnectionParameters() {
      return this.getProperty("optional_connectstring");
   }

   public void setAdditionalConnectionParameters(String var1) {
      this.setStringProperty("optional_connectstring", var1);
   }

   public boolean isManualMode() {
      return this.getBooleanProperty("manual_mode", false);
   }

   public void setManualMode(boolean var1) {
      this.setBooleanProperty("manual_mode", var1);
   }

   public String getManualConnectionString() {
      String var1 = this.getProperty("connectstring");
      if (StringUtils.isEmpty(var1)) {
         return var1;
      }

      if (this.getDriver() instanceof OdbcDriverDescriptor && !var1.endsWith(";")) {
         var1 = var1 + ";";
      }

      return var1;
   }

   public void setManualConnectionString(String var1) {
      this.setStringProperty("connectstring", var1);
   }

   @Override
   public Port getPort() {
      synchronized (this.getDataLock()) {
         return this.portSupport.getPort();
      }
   }

   @Override
   public void setPort(Port var1) {
      Port var2;
      synchronized (this.getDataLock()) {
         var2 = this.portSupport.setPort(var1);
      }

      this.firePropertyChange("port", var2, var1);
   }

   public TransactionIsolationLevel getTransactionIsolationLevel() {
      synchronized (this.getDataLock()) {
         int var2 = this.getIntProperty("transactionIsolationLevel", TransactionIsolationLevel.DEFAULT.getCode());
         return TransactionIsolationLevel.fromCode(var2);
      }
   }

   public void setTransactionIsolactionLevel(TransactionIsolationLevel var1) {
      TransactionIsolationLevel var2;
      synchronized (this.getDataLock()) {
         var2 = this.getTransactionIsolationLevel();
         this.getData().putInt("transactionIsolationLevel", var1.getCode());
      }

      if (var1 != var2) {
         this.firePropertyChange("transactionIsolationLevel", var2, var1);
      }
   }

   public int getFetchSize() {
      return this.getIntProperty("fetch_size", 0);
   }

   public void setFetchSize(int var1) {
      this.setIntProperty("fetch_size", var1);
   }

   @Override
   public void collectValidationMessages(ValidationMessageCollector var1) {
      super.collectValidationMessages(var1);
      new DatabaseLocationValidator(this, var1).validate();
   }

   @Override
   public String toString() {
      String var1 = super.toString();
      return var1 + ";Password is NULL=" + (this.getPassword() == null);
   }

   @Override
   public StringKeyValuePairs getPropertiesForSource() {
      synchronized (this.getDataLock()) {
         StringKeyValuePairs var2 = super.getPropertiesForSource();
         if (this.isManualMode()) {
            var2.put("connectstring", this.getManualConnectionString());
         } else {
            var2.put("connectstring", "");
         }

         this.addAdditionalPropertiesForDeploy(var2);
         return var2;
      }
   }

   private void addAdditionalPropertiesForDeploy(StringKeyValuePairs var1) {
      var1.put("driver_class", this.getProperty("driver_class"));
      var1.put("jdbc_subprotocol", this.getProperty("jdbc_subprotocol"));
   }

   @Override
   public StringKeyValuePairs getPropertiesForTarget() {
      synchronized (this.getDataLock()) {
         StringKeyValuePairs var2 = super.getPropertiesForSource();
         if (this.isManualMode()) {
            var2.put("connectstring", this.getManualConnectionString());
         } else {
            var2.put("connectstring", "");
         }

         this.addAdditionalPropertiesForDeploy(var2);
         return var2;
      }
   }

   @Override
   public ImmutableList<? extends ExportableProperty> getExportableProperties() {
      return ImmutableList.of(
         new ExportableDriverProperty(this),
         new ExportableStringProperty(this, "locator", LocationMessages.getString("Property.Server"), true),
         new ExportableStringProperty(this, "dbname", LocationMessages.getString("DatabaseLocation.Property.Database"), true),
         new ExportableStringProperty(this, "port", LocationMessages.getString("DatabaseLocation.Property.Port"), true),
         new ExportableStringProperty(this, "login", LocationMessages.getString("Property.Login"), true),
         new ExportableStringProperty(this, "password", LocationMessages.getString("Property.Password"), true),
         new ExportableStringProperty(this, "optional_connectstring", LocationMessages.getString("DatabaseLocation.Property.AdditionalParameters"), false),
         new ExportableManualConnectionString(this)
      );
   }

   @Override
   public DataLocation getCopyForSourceOrTarget(SourceTarget var1) {
      Preconditions.checkNotNull(var1);
      DatabaseLocation var2 = this.clone();
      if (var1 == SourceTarget.Target) {
         var2.setProperty("fetch_size", (String)null);
      }

      return var2;
   }

   public DatabaseLocation clone() {
      synchronized (this.getDataLock()) {
         DatabaseLocation var2 = (DatabaseLocation)super.clone();
         var2.portSupport = new PortAccessibleSupport(var2, "port");
         return var2;
      }
   }

   public ConnectionParams getConnectionParams() {
      synchronized (this.getDataLock()) {
         ConnectionParams var2 = new ConnectionParams();
         var2.setServer(this.getServer())
            .setPort(this.getPort())
            .setDatabase(this.getDatabase())
            .setDriverName(this.getDriverName())
            .setDriver(this.getDriver())
            .setUser(this.getLogin())
            .setPassword(this.getClearTextPassword())
            .setAdditionalParams(this.getAdditionalConnectionParameters())
            .setBeginEndQuotes(this.getBeginEndQuote())
            .setTransactionIsolationLevel(this.getTransactionIsolationLevel());
         if (this.isManualMode()) {
            var2.setManualConnectionString(this.getManualConnectionString());
         }

         return var2;
      }
   }

   @Override
   public void restoreFrom(Persistor var1) {
      super.restoreFrom(var1);
      String var2 = this.getProperty("begin_quote");
      if (var2 != null) {
         this.setProperty("begin_quote", (String)null);
         this.setProperty("driver_begin_quote", var2);
      }

      String var3 = this.getProperty("end_quote");
      if (var3 != null) {
         this.setProperty("end_quote", (String)null);
         this.setProperty("driver_end_quote", var3);
      }
   }

   @Override
   public String getPasswordPropertyName() {
      return "password";
   }

   @Override
   public ImmutableSet<String> getNamesOfPropertiesToCache() {
      return ImmutableSet.of("locator", "dbname", "login");
   }

   private class DriverPropertySetter implements DriverDescriptorVisitor {
      private DriverPropertySetter() {
      }

      @Override
      public void visit(JdbcDriverDescriptor var1) {
         synchronized (DatabaseLocation.this.getDataLock()) {
            this.transferCommonProperties(var1);
            this.setDriverClass(var1.getDriverClass());
            this.setSubprotocol(var1.getSubprotocol());
         }
      }

      @Override
      public void visit(OdbcDriverDescriptor var1) {
         synchronized (DatabaseLocation.this.getDataLock()) {
            this.transferCommonProperties(var1);
            this.setDriverClass(null);
            this.setSubprotocol(null);
         }
      }

      private void setDriverClass(String var1) {
         DatabaseLocation.this.setProperty("driver_class", var1);
      }

      private void setSubprotocol(String var1) {
         DatabaseLocation.this.setProperty("jdbc_subprotocol", var1);
      }

      private void transferCommonProperties(DriverDescriptor var1) {
         DatabaseLocation.this.setProperty("driver", var1.getName());
         DatabaseLocation.this.setDriverBeginEndQuote(var1.getBeginEndQuotes());
      }
   }
}
