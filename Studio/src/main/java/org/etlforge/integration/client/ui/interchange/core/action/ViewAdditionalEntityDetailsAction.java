package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.event.ActionEvent;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.widget.BasicTwoColumnLabelDisplayer;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.UiProvider;

public final class ViewAdditionalEntityDetailsAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.AdditionalDetails";
   private final IntegrationEntity object;

   public ViewAdditionalEntityDetailsAction(IntegrationEntity var1) {
      super("action.ic.AdditionalDetails", InterchangeActionDictionary.DICTIONARY);
      this.object = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      Alert.info(this.createDisplayer(), "Additional Details");
   }

   private UiProvider createDisplayer() {
      BasicTwoColumnLabelDisplayer var1 = new BasicTwoColumnLabelDisplayer(BasicTwoColumnLabelDisplayer.Mode.TEXTFIELD);
      var1.setFieldWidth(30);
      var1.addRow("Type:", this.object.getEntityType().getDisplayName());
      var1.addRow("Name:", this.object.getName());
      var1.addRow("GUID:", this.object.getID());
      var1.addRow("Created on:", this.object.getCreatedAt());
      var1.addRow("Last modified on:", this.object.getModifiedAt());
      this.addBoolean(var1, "Deployed:", this.object.hasBeenDeployed());
      if (this.object.hasBeenDeployed()) {
         this.addBoolean(var1, "Changed since deployed:", this.object.isDeployDirty());
         this.addBoolean(var1, "Moved since deployed:", this.object.hasMoved());
      }

      return var1;
   }

   private void addBoolean(BasicTwoColumnLabelDisplayer var1, String var2, boolean var3) {
      var1.addRow(var2, var3 ? "Yes" : "No");
   }
}
