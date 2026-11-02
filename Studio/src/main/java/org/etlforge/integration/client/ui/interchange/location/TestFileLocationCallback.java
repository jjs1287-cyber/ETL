package org.etlforge.integration.client.ui.interchange.location;

import java.util.List;
import javax.annotation.Nullable;
import org.etlforge.integration.client.datalocation.connection.MatchingFileList;
import org.etlforge.integration.client.datalocation.connection.serverapi.FileConnectionCallback;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.test.TestConnectionResult;

final class TestFileLocationCallback extends ResultProducingServerCallback<TestConnectionResult> implements FileConnectionCallback {
   private final IntegrationEntity locatable;

   public TestFileLocationCallback(IntegrationEntity var1) {
      this.locatable = var1;
   }

   @Override
   public void connected(@Nullable List<String> var1) {
      TestConnectionResult var2 = this.createResult(var1);
      this.set(var2);
   }

   private TestConnectionResult createResult(List<String> var1) {
      if (var1 == null) {
         return TestConnectionResult.success(this.locatable);
      }

      MatchingFileList var2 = new MatchingFileList(var1);
      return TestConnectionResult.success(this.locatable, var2);
   }
}
