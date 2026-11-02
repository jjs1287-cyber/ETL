package org.etlforge.integration.client.ui.entity;

import java.awt.event.ActionEvent;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;

public final class EditEntityAction extends AbstractSingleObjectSelectionAction<IntegrationEntity> {
   public EditEntityAction() {
      super(IntegrationEntity.class, ApplicationResources.STRINGS.getString("Edit.Mnemonic"));
   }

   public void setPageDisplayer(EntityPageDisplayer var1) {
      IntegrationEntity var2 = this.getSelectedObject();
      this.setEnabled(var2 != null && this.isEnabledFor(var2));
   }

   protected boolean isEnabledFor(IntegrationEntity var1) {
      return !(var1 instanceof Folder);
   }

   protected void run(IntegrationEntity var1, ActionEvent var2) {
      UiCompanion.of(var1).openPage();
   }
}
