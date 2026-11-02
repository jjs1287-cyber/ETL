package org.etlforge.integration.client.connect.text;

import org.etlforge.integration.client.ui.entity.factory.Result;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;

public final class NewFileStructureCallback extends UiEntityFactoryCallback.Adapter {
   @Override
   public Result preProjectInsertion(IntegrationEntity var1, Folder var2) {
      Folder var3 = this.getParent(var2);
      return Result.continueProcess(var3);
   }

   private Folder getParent(Folder var1) {
      if (var1 instanceof RootFolder) {
         Folder var2 = ((RootFolder)var1).getEtlforgeConnectRoot();
         if (var2 != null) {
            return var2;
         }
      }

      return var1;
   }
}
