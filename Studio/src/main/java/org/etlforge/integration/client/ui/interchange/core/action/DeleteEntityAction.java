package org.etlforge.integration.client.ui.interchange.core.action;

import java.util.List;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.DeleteRequestHandler;

public final class DeleteEntityAction extends AbstractDeleteEntityAction {
   public static final String ID = "action.ic.Delete";

   public DeleteEntityAction(ActiveObjectService var1, DeleteRequestHandler var2) {
      super("action.ic.Delete", var1, var2);
   }

   @Override
   public boolean accepts(Object var1) {
      return var1 instanceof IntegrationEntity && ((IntegrationEntity)var1).isDeletable();
   }

   @Override
   protected List<IntegrationEntity> getItemsToDelete() {
      return this.getActiveObjects(IntegrationEntity.class);
   }

   @Override
   protected void updateEnabledState(List<? extends Object> var1) {
      this.setEnabled(var1 != null && !var1.isEmpty());
   }
}
