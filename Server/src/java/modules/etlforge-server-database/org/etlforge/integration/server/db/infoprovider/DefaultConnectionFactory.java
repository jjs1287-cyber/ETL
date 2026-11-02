package org.etlforge.integration.server.db.infoprovider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.database.info.InformixUtils;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.conf.JdbcDriversConfig;
import org.etlforge.integration.server.db.LocationPasswordLookup;
import org.etlforge.integration.server.db.ServerDbException;
import org.etlforge.integration.server.exception.ServerConfigurationException;

public final class DefaultConnectionFactory implements ConnectionFactory {
   private LocationPasswordLookup pwdLookup;
   private final ConnectionParams connectionParams;
   private final SourceId sourceGuid;
   private final TargetId targetGuid;
   private JdbcDriverDescriptor driver;

   public DefaultConnectionFactory(ConnectionParams var1, SourceId var2, TargetId var3) {
      this.connectionParams = var1;
      this.sourceGuid = var2;
      this.targetGuid = var3;
   }

   public void setLocationPasswordLookup(LocationPasswordLookup var1) {
      this.pwdLookup = var1;
   }

   @Override
   public Connection newConnection() throws ServerDbException, ServerAccessException, SQLException {
      try {
         JdbcDriverDescriptor var1 = this.getDriver();
         var1.loadDriverClass();
         String var2 = var1.getConnectionString(this.connectionParams);
         String var3 = this.connectionParams.getUser();
         if (StringUtils.isEmpty(var3)) {
            return DriverManager.getConnection(var2);
         }

         String var4 = this.getPassword();
         return DriverManager.getConnection(var2, var3, var4);
      } catch (ServerConfigurationException var5) {
         throw new ServerDbException("The ETLForge server configuration is incorrect: " + var5.getMessage(), var5);
      } catch (ClassNotFoundException var6) {
         throw new ServerDbException("The driver could not be found. Make sure the driver is on the classpath: " + var6.getMessage(), var6);
      } catch (NoClassDefFoundError var7) {
         throw new ServerDbException(
            "The driver was found, but one of its required components could not be accessed correctly. Make sure all required driver libraries (JAR files) are installed correctly. The reported error message was: "
               + var7.toString(),
            var7
         );
      } catch (ServerDbException var8) {
         throw new ServerDbException("An error occurred while accessing the ETLForge backend database: " + var8.getMessage(), var8);
      }
   }

   private JdbcDriverDescriptor getDriver() throws ServerConfigurationException {
      if (this.driver == null) {
         this.driver = JdbcDriversConfig.getConfig().getDriverDescriptor(this.connectionParams.getDriverName());
      }

      return this.driver;
   }

   public boolean isInformixConnection() throws ServerConfigurationException {
      return InformixUtils.isInformix(this.getDriver());
   }

   private String getPassword() throws ServerDbException, ServerAccessException {
      String var1 = this.connectionParams.getPassword();
      if (var1 == null && this.pwdLookup != null) {
         var1 = this.pwdLookup.lookupPassword(this.sourceGuid, this.targetGuid);
      }

      return var1;
   }
}
