package org.etlforge.integration.client.ui.interchange.core.action;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.DeleteRequestHandler;

public final class DeleteRootFolderContentAction extends AbstractDeleteEntityAction {
   public static final String ID = "action.ic.DeleteRootFolder";

   public DeleteRootFolderContentAction(ActiveObjectService var1, DeleteRequestHandler var2) {
      super("action.ic.DeleteRootFolder", var1, var2);
   }

   @Override
   public boolean accepts(Object var1) {
      return var1 instanceof RootFolder;
   }

   @Override
   protected List<IntegrationEntity> getItemsToDelete() {
      List var1 = this.getActiveObjects(RootFolder.class);
      if (var1.size() != 1) {
         return null;
      }

      ArrayList var2 = Lists.newArrayList();
      populateItemList(var2, (IntegrationEntity)var1.get(0));
      return var2;
   }

   private static void populateItemList(List<IntegrationEntity> var0, IntegrationEntity var1) {
      for (IntegrationEntity var3 : var1.getAllChildren()) {
         if (var3.isDeletable()) {
            var0.add(var3);
         }

         populateItemList(var0, var3);
      }
   }
}
