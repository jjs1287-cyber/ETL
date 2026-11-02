package org.etlforge.integration.client.datalocation.connection.serverapi;

import java.util.ArrayList;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.location.FileShareLocation;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.location.LocalFileLocation;
import org.etlforge.integration.data.location.TemporaryStorageLocation;
import org.etlforge.integration.database.BeginEndQuote;

final class MockTestSourceConnectionApi implements TestSourceConnectionApi {
   @Override
   public void testDatabase(Source var1, DatabaseLocation var2, DatabaseConnectionCallback var3) {
      try {
         Thread.sleep(2000L);
         var3.connected(new BeginEndQuote("\""));
      } catch (InterruptedException var5) {
         Thread.currentThread().interrupt();
         var3.caught(var5);
      }
   }

   @Override
   public void testFileshare(Source var1, FileShareLocation var2, FileConnectionCallback var3) {
      this.testFile(var3);
   }

   private void testFile(FileConnectionCallback var1) {
      try {
         Thread.sleep(2000L);
         ArrayList var2 = new ArrayList();
         var2.add("abc.txt");
         var2.add("123.txt");
         var1.connected(var2);
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
         var1.caught(var3);
      }
   }

   @Override
   public void testFtp(Source var1, FtpLocation var2, FileConnectionCallback var3) {
      this.testFile(var3);
   }

   @Override
   public void testHttp(Source var1, HttpLocation var2, FileConnectionCallback var3) {
      this.testFile(var3);
   }

   @Override
   public void testLdap(Source var1, LdapLocation var2, LdapConnectionCallback var3) {
      try {
         Thread.sleep(2000L);
         var3.connected();
      } catch (InterruptedException var5) {
         Thread.currentThread().interrupt();
         var3.caught(var5);
      }
   }

   @Override
   public void testTemporaryStorage(Source var1, TemporaryStorageLocation var2, FileConnectionCallback var3) {
      this.testFile(var3);
   }

   @Override
   public void testLocalFile(Source var1, LocalFileLocation var2, FileConnectionCallback var3) {
      this.testFile(var3);
   }
}
