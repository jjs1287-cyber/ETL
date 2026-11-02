package org.etlforge.integration.client.ui.interchange.cli;

import java.util.Set;
import org.etlforge.application.ui.Alert;
import org.etlforge.cli.CommandException;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.data.entity.IntegrationEntity;

final class OpenPathExecutor extends AbstractProjectPathCommandExecutor {
   private final EntityPageDisplayer pageDisplayer;

   public OpenPathExecutor(CurrentProjectService var1, EntityPageDisplayer var2) {
      super(var1);
      this.pageDisplayer = var2;
   }

   @Override
   protected void processMatchingItems(Set<IntegrationEntity> var1) {
      for (IntegrationEntity var3 : var1) {
         this.pageDisplayer.openPage(var3);
      }
   }

   @Override
   protected void noMatchingItems(String var1) throws CommandException {
      Alert.error("No items matching the path \"" + var1 + "\"", "Nothing to Open");
   }
}
