package org.etlforge.integration.client.datalocation.connection.serverapi;

import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.location.FileShareLocation;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.location.LocalFileLocation;
import org.etlforge.integration.data.location.TemporaryStorageLocation;

public interface TestSourceConnectionApi extends ServerCall {
   void testDatabase(Source var1, DatabaseLocation var2, DatabaseConnectionCallback var3);

   void testFileshare(Source var1, FileShareLocation var2, FileConnectionCallback var3);

   void testTemporaryStorage(Source var1, TemporaryStorageLocation var2, FileConnectionCallback var3);

   void testFtp(Source var1, FtpLocation var2, FileConnectionCallback var3);

   void testLocalFile(Source var1, LocalFileLocation var2, FileConnectionCallback var3);

   void testHttp(Source var1, HttpLocation var2, FileConnectionCallback var3);

   void testLdap(Source var1, LdapLocation var2, LdapConnectionCallback var3);
}
