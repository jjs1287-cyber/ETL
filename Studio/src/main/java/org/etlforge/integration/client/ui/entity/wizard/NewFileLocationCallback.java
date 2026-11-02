package org.etlforge.integration.client.ui.entity.wizard;

import org.etlforge.integration.client.ui.entity.factory.Result;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.FileBasedLocatableBuilder;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class NewFileLocationCallback extends UiEntityFactoryCallback.Adapter {
   @Override
   public Result preProjectInsertion(IntegrationEntity var1, Folder var2) {
      FileBasedLocatableBuilder var3 = new FileBasedLocatableBuilder();
      Folder var4 = var3.configure((Locatable)var1, var2);
      return Result.continueProcess(var4);
   }
}
