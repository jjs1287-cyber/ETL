package org.etlforge.integration.client.ui.entity.wizard;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.entity.factory.Result;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.LocatableBuilder;
import org.etlforge.integration.data.location.DataLocationType;

public final class NewLocatableCallback extends UiEntityFactoryCallback.Adapter {
   private final DataLocationType type;

   public NewLocatableCallback(DataLocationType var1) {
      this.type = Preconditions.checkNotNull(var1);
   }

   @Override
   public Result preProjectInsertion(IntegrationEntity var1, Folder var2) {
      LocatableBuilder var3 = new LocatableBuilder(this.type);
      Folder var4 = var3.configure((Locatable)var1, var2);
      return Result.continueProcess(var4);
   }
}
