package org.etlforge.integration.server.implementation.webservice.interchange.connection.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.xml.rpc.ServiceException;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.client.datalocation.connection.serverapi.DatabaseConnectionCallback;
import org.etlforge.integration.client.datalocation.connection.serverapi.FileConnectionCallback;
import org.etlforge.integration.client.datalocation.connection.serverapi.LdapConnectionCallback;
import org.etlforge.integration.client.datalocation.connection.serverapi.TestSourceConnectionApi;
import org.etlforge.integration.client.datalocation.connection.serverapi.TestTargetConnectionApi;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.location.FileShareLocation;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.location.LocalFileLocation;
import org.etlforge.integration.data.location.LoginProtectedResource;
import org.etlforge.integration.data.location.TemporaryStorageLocation;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.ClientServerDeployKeyMappings;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.DeployKeyMappings;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.ServerPropertyKey;
import org.etlforge.integration.server.implementation.webservice.interchange.connection.client.ConnectionInfo;
import org.etlforge.integration.server.implementation.webservice.interchange.connection.client.FileList;
import org.etlforge.integration.server.implementation.webservice.interchange.connection.client.TestConnection;
import org.etlforge.integration.server.implementation.webservice.interchange.connection.client.TestConnectionServiceLocator;
import org.etlforge.integration.server.implementation.webservice.interchange.db.impl.JdbcInfoProviderWsImpl;

public final class ConnectionTesterWsImpl extends CppWebServiceServerCall implements TestSourceConnectionApi, TestTargetConnectionApi {
   private static String SERVICE_NAME = "konga_test_connection";
   private String userName;
   private String password;
   private final JdbcInfoProviderWsImpl jdbcTester;

   public ConnectionTesterWsImpl(CppWebServiceConfiguration var1, JdbcInfoProviderWsImpl var2) {
      super(var1);
      this.jdbcTester = var2;
   }

   private TestConnection getTestConnection(ServerCallback var1) throws IntegrationServerException {
      TestConnectionServiceLocator var2 = new TestConnectionServiceLocator();

      try {
         StringBuilder var3 = new StringBuilder();
         if (!this.getConfiguration().configureWebservice(var2, SERVICE_NAME, var3, var1)) {
            return null;
         }

         this.userName = this.getConfiguration().getUserName();
         this.password = this.getConfiguration().getPasswordHash();
         var2.setkonga_test_connectionEndpointAddress(var3.toString());
         return var2.getkonga_test_connection();
      } catch (ServiceException var4) {
         throw new IntegrationServerException("Failed to call web service: " + var4.getMessage(), var4);
      }
   }

   private ConnectionInfo getConnectionInfo(DataLocation var1, boolean var2) {
      StringKeyValuePairs var3 = var2 ? var1.getPropertiesForSource() : var1.getPropertiesForTarget();
      DeployKeyMappings var4 = ClientServerDeployKeyMappings.getClientServerKeyMappings(var2 ? EntityType.Source : EntityType.Target);
      int var5 = var1.getDataLocationType().getId();
      String var6 = var4.get("source_type_id").getServerKeyName();
      var3.putInt(var6, var5);
      Set var7 = var3.getKeys();
      ArrayList var8 = Lists.newArrayList();
      ArrayList var9 = Lists.newArrayList();
      String var10 = var1 instanceof LoginProtectedResource ? ((LoginProtectedResource)var1).getPasswordPropertyName() : null;

      for (Object __etlItem86 : var7) {
         String var12 = (String) __etlItem86;
         ServerPropertyKey var13 = var4.get(var12);
         if (var13 != null) {
            String var14 = null;
            if (var12.equals(var10)) {
               Optional var15 = ((LoginProtectedResource)var1).getPassword();
               if (var15.isPresent()) {
                  var14 = ((Password)var15.get()).clearText();
               }
            } else {
               var14 = var3.get(var12);
            }

            var8.add(var13.getServerKeyName());
            var9.add(var14);
         }
      }

      ConnectionInfo var16 = new ConnectionInfo();
      var16.setKeys((String[]) var8.toArray(new String[var8.size()]));
      var16.setKeyValues((String[]) var9.toArray(new String[var9.size()]));
      return var16;
   }

   private List<String> toList(String[] var1) {
      if (var1 != null && var1.length != 0) {
         ArrayList var2 = Lists.newArrayList();
         Collections.addAll(var2, var1);
         return var2;
      } else {
         return Lists.newArrayList();
      }
   }

   private List<String> testFileSource(Source var1, DataLocation var2, ServerCallback var3) throws IntegrationServerException, RemoteException {
      TestConnection var4 = this.getTestConnection(var3);
      if (var4 == null) {
         return null;
      }

      ConnectionInfo var5 = this.getConnectionInfo(var2, true);
      FileList var6 = var4.testFileTypeConnection(this.userName, this.password, var5, var1.getID().toString(), true);
      return this.toList(var6.getFiles());
   }

   private boolean testFileTarget(Target var1, DataLocation var2, ServerCallback var3) throws IntegrationServerException, RemoteException {
      TestConnection var4 = this.getTestConnection(var3);
      if (var4 == null) {
         return false;
      }

      ConnectionInfo var5 = this.getConnectionInfo(var2, false);
      var4.testFileTypeConnection(this.userName, this.password, var5, var1.getID().toString(), false);
      return true;
   }

   private boolean testDatabaseSource(Source var1, DatabaseLocation var2, ServerCallback var3) throws IntegrationServerException, RemoteException {
      TestConnection var4 = this.getTestConnection(var3);
      if (var4 == null) {
         return false;
      }

      ConnectionInfo var5 = this.getConnectionInfo(var2, true);
      var4.testDatabaseConnection(this.userName, this.password, var5, var1.getID().toString(), true);
      return true;
   }

   private boolean testDatabaseTarget(Target var1, DatabaseLocation var2, ServerCallback var3) throws IntegrationServerException, RemoteException {
      TestConnection var4 = this.getTestConnection(var3);
      if (var4 == null) {
         return false;
      }

      ConnectionInfo var5 = this.getConnectionInfo(var2, false);
      var4.testDatabaseConnection(this.userName, this.password, var5, var1.getID().toString(), false);
      return true;
   }

   @Override
   public void testDatabase(Source var1, DatabaseLocation var2, DatabaseConnectionCallback var3) {
      try {
         DriverDescriptor var4 = var2.getDriver();
         if (var4 instanceof JdbcDriverDescriptor) {
            this.testJdbcSource(var1, var2, var3);
         } else {
            this.testOdbcSource(var1, var2, var3);
         }
      } catch (RemoteException var5) {
         var3.caught(this.convert(var5));
      } catch (IntegrationServerException var6) {
         var3.caught(new IntegrationServerException("Server exception: " + var6.getMessage(), var6));
      }
   }

   private void testJdbcSource(Source var1, DatabaseLocation var2, DatabaseConnectionCallback var3) {
      this.jdbcTester.testConnection(var2, var1.getID(), null, var3);
   }

   private void testOdbcSource(Source var1, DatabaseLocation var2, DatabaseConnectionCallback var3) throws IntegrationServerException, RemoteException {
      if (this.testDatabaseSource(var1, var2, var3)) {
         var3.connected(null);
      }
   }

   @Override
   public void testFileshare(Source var1, FileShareLocation var2, FileConnectionCallback var3) {
      this.testFileBasedSourceLocation(var1, var2, var3);
   }

   private void testFileBasedSourceLocation(Source var1, DataLocation var2, FileConnectionCallback var3) {
      try {
         List var4 = this.testFileSource(var1, var2, var3);
         if (var4 == null) {
            return;
         }

         var3.connected(var4);
      } catch (RemoteException var5) {
         var3.caught(this.convert(var5));
      } catch (IntegrationServerException var6) {
         var3.caught(new IntegrationServerException("Server exception: " + var6.getMessage(), var6));
      }
   }

   @Override
   public void testTemporaryStorage(Source var1, TemporaryStorageLocation var2, FileConnectionCallback var3) {
      this.testFileBasedSourceLocation(var1, var2, var3);
   }

   @Override
   public void testFtp(Source var1, FtpLocation var2, FileConnectionCallback var3) {
      this.testFileBasedSourceLocation(var1, var2, var3);
   }

   @Override
   public void testLocalFile(Source var1, LocalFileLocation var2, FileConnectionCallback var3) {
      this.testFileBasedSourceLocation(var1, var2, var3);
   }

   @Override
   public void testHttp(Source var1, HttpLocation var2, FileConnectionCallback var3) {
      this.testFileBasedSourceLocation(var1, var2, var3);
   }

   @Override
   public void testDatabase(Target var1, DatabaseLocation var2, DatabaseConnectionCallback var3) {
      try {
         DriverDescriptor var4 = var2.getDriver();
         if (var4 instanceof JdbcDriverDescriptor) {
            this.testJdbcTarget(var1, var2, var3);
         } else {
            this.testOdbcTarget(var1, var2, var3);
         }
      } catch (RemoteException var5) {
         var3.caught(this.convert(var5));
      } catch (IntegrationServerException var6) {
         var3.caught(new IntegrationServerException("Server exception: " + var6.getMessage(), var6));
      }
   }

   private void testJdbcTarget(Target var1, DatabaseLocation var2, DatabaseConnectionCallback var3) {
      this.jdbcTester.testConnection(var2, null, var1.getID(), var3);
   }

   private void testOdbcTarget(Target var1, DatabaseLocation var2, DatabaseConnectionCallback var3) throws IntegrationServerException, RemoteException {
      if (this.testDatabaseTarget(var1, var2, var3)) {
         var3.connected(null);
      }
   }

   @Override
   public void testFileshare(Target var1, FileShareLocation var2, FileConnectionCallback var3) {
      this.testFileBasedTargetLocation(var1, var2, var3);
   }

   private void testFileBasedTargetLocation(Target var1, DataLocation var2, FileConnectionCallback var3) {
      try {
         if (this.testFileTarget(var1, var2, var3)) {
            var3.connected(null);
         }
      } catch (RemoteException var5) {
         var3.caught(this.convert(var5));
      } catch (IntegrationServerException var6) {
         var3.caught(new IntegrationServerException("Server exception: " + var6.getMessage(), var6));
      }
   }

   @Override
   public void testTemporaryStorage(Target var1, TemporaryStorageLocation var2, FileConnectionCallback var3) {
      this.testFileBasedTargetLocation(var1, var2, var3);
   }

   @Override
   public void testFtp(Target var1, FtpLocation var2, FileConnectionCallback var3) {
      this.testFileBasedTargetLocation(var1, var2, var3);
   }

   @Override
   public void testLocalFile(Target var1, LocalFileLocation var2, FileConnectionCallback var3) {
      this.testFileBasedTargetLocation(var1, var2, var3);
   }

   @Override
   public void testHttp(Target var1, HttpLocation var2, FileConnectionCallback var3) {
      this.testFileBasedTargetLocation(var1, var2, var3);
   }

   @Override
   public void testLdap(Target var1, LdapLocation var2, LdapConnectionCallback var3) {
      this.testLdapImpl(var1, var2, var3);
   }

   private void testLdapImpl(Locatable var1, LdapLocation var2, LdapConnectionCallback var3) {
      try {
         TestConnection var4 = this.getTestConnection(var3);
         if (var4 == null) {
            return;
         }

         ConnectionInfo var5 = this.getConnectionInfo(var2, false);
         var4.testLdapConnection(this.userName, this.password, var5, var1.getID().toString(), false);
         var3.connected();
      } catch (RemoteException var6) {
         var3.caught(this.convert(var6));
      } catch (IntegrationServerException var7) {
         var3.caught(new IntegrationServerException("Server exception: " + var7.getMessage(), var7));
      }
   }

   @Override
   public void testLdap(Source var1, LdapLocation var2, LdapConnectionCallback var3) {
      this.testLdapImpl(var1, var2, var3);
   }
}
