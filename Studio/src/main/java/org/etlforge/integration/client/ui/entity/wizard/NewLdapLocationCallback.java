package org.etlforge.integration.client.ui.entity.wizard;

import org.etlforge.integration.client.ui.entity.factory.Result;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.location.DataLocationType;

public final class NewLdapLocationCallback extends UiEntityFactoryCallback.Adapter {
   @Override
   public Result preProjectInsertion(IntegrationEntity var1, Folder var2) {
      NewLocatableCallback var3 = new NewLocatableCallback(DataLocationType.LDAP);
      return var3.preProjectInsertion(var1, var2);
   }
}
