package org.etlforge.integration.client.ui.interchange.location;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.datalocation.connection.serverapi.TestSourceConnectionApi;
import org.etlforge.integration.client.datalocation.connection.serverapi.TestTargetConnectionApi;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.client.ui.interchange.testconnection.ConnectionTester;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.location.FileShareLocation;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.location.LocalFileLocation;
import org.etlforge.integration.data.location.TemporaryStorageLocation;
import org.etlforge.integration.data.test.TestConnectionResult;
import org.etlforge.integration.data.test.TestResultException;

public abstract class LocationConnectionTester<T extends DataLocation> {
   protected final T location;

   public static LocationConnectionTester<?> forLocation(DataLocation var0) {
      switch (var0.getDataLocationType()) {
         case AmazonS3:
            return new S3ConnectionTester((org.etlforge.integration.data.location.S3Location)var0);
         case GlobalVariable:
            return new GlobalVariableConnectionTester((org.etlforge.integration.data.location.GlobalVariableLocation)var0);
         case Database:
            return new DatabaseConnectionTester((DatabaseLocation)var0);
         case FTP:
            return new FtpConnectionTester((FtpLocation)var0);
         case FileShare:
            return new FileShareConnectionTester((FileShareLocation)var0);
         case TemporaryStorage:
            return new TemporaryStorageConnectionTester((TemporaryStorageLocation)var0);
         case LocalFile:
            return new LocalFileConnectionTester((LocalFileLocation)var0);
         case HTTP:
            return new HttpConnectionTester((HttpLocation)var0);
         case LDAP:
            return new LdapConnectionTester((LdapLocation)var0);
         default:
            throw new RuntimeException("Unexpected location type: " + var0.getDataLocationType());
      }
   }

   protected LocationConnectionTester(T var1) {
      this.location = Preconditions.checkNotNull((T)var1, "location");
   }

   public final ConnectionTester asSourceConnectionTester(final Source var1, final TestSourceConnectionApi var2) {
      return new ConnectionTester() {
         @Override
         public TestConnectionResult testConnection() {
            return LocationConnectionTester.this.testSource(var1, var2);
         }
      };
   }

   private TestConnectionResult testSource(Source var1, TestSourceConnectionApi var2) {
      ResultProducingServerCallback var3 = this.makeServerCall(var1, var2);
      return this.waitForResult(var1, var3);
   }

   protected abstract ResultProducingServerCallback<TestConnectionResult> makeServerCall(Source var1, TestSourceConnectionApi var2);

   public final ConnectionTester asTargetConnectionTester(final Target var1, final TestTargetConnectionApi var2) {
      return new ConnectionTester() {
         @Override
         public TestConnectionResult testConnection() {
            return LocationConnectionTester.this.testTarget(var1, var2);
         }
      };
   }

   private TestConnectionResult testTarget(Target var1, TestTargetConnectionApi var2) {
      ResultProducingServerCallback var3 = this.makeServerCall(var1, var2);
      return this.waitForResult(var1, var3);
   }

   protected abstract ResultProducingServerCallback<TestConnectionResult> makeServerCall(Target var1, TestTargetConnectionApi var2);

   private TestConnectionResult waitForResult(IntegrationEntity var1, ResultProducingServerCallback<TestConnectionResult> var2) {
      Object var3 = null;

      try {
         return (TestConnectionResult)var2.awaitResult().orNull();
      } catch (Exception var6) {
         TestResultException var5 = new TestResultException(var6);
         return TestConnectionResult.error(var1, var5);
      }
   }
}
