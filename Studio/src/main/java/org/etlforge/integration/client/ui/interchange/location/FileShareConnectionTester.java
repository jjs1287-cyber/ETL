package org.etlforge.integration.client.ui.interchange.location;

import org.etlforge.integration.client.datalocation.connection.serverapi.TestSourceConnectionApi;
import org.etlforge.integration.client.datalocation.connection.serverapi.TestTargetConnectionApi;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.FileShareLocation;
import org.etlforge.integration.data.test.TestConnectionResult;

public final class FileShareConnectionTester extends LocationConnectionTester<FileShareLocation> {
   public FileShareConnectionTester(FileShareLocation var1) {
      super(var1);
   }

   @Override
   protected ResultProducingServerCallback<TestConnectionResult> makeServerCall(Source var1, TestSourceConnectionApi var2) {
      TestFileLocationCallback var3 = new TestFileLocationCallback(var1);
      var2.testFileshare(var1, this.location, var3);
      return var3;
   }

   @Override
   protected ResultProducingServerCallback<TestConnectionResult> makeServerCall(Target var1, TestTargetConnectionApi var2) {
      TestFileLocationCallback var3 = new TestFileLocationCallback(var1);
      var2.testFileshare(var1, this.location, var3);
      return var3;
   }
}
