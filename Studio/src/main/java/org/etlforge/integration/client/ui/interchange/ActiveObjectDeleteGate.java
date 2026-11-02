package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Predicate;
import java.util.List;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class ActiveObjectDeleteGate implements Predicate<List<IntegrationEntity>> {
   public boolean apply(List<IntegrationEntity> var1) {
      DeleteVetoFinder var2 = new DeleteVetoFinder();
      IntegrationEntity var3 = var2.findVeto(var1);
      if (var3 == null) {
         return true;
      }

      this.showVetoMessageToUser(var3);
      return false;
   }

   private void showVetoMessageToUser(IntegrationEntity var1) {
      String var2 = String.format("The %s \"%s\" is currently in use. It cannot be deleted.", var1.getEntityType(), var1.getName());
      Alert.error(ApplicationUi.getAppWinFrame(), var2, "Active Item");
   }
}
