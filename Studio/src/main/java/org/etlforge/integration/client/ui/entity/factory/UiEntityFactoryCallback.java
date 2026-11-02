package org.etlforge.integration.client.ui.entity.factory;

import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface UiEntityFactoryCallback {
   Result preProjectInsertion(IntegrationEntity var1, Folder var2);

   void postProjectInsertion(IntegrationEntity var1);

   class Adapter implements UiEntityFactoryCallback {
      @Override
      public Result preProjectInsertion(IntegrationEntity var1, Folder var2) {
         return Result.continueProcess(var2);
      }

      @Override
      public void postProjectInsertion(IntegrationEntity var1) {
      }
   }
}
