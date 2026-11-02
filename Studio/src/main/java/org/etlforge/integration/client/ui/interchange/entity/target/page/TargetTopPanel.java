package org.etlforge.integration.client.ui.interchange.entity.target.page;

import org.etlforge.integration.client.ui.interchange.entity.LocatableTopPanel;
import org.etlforge.integration.client.ui.interchange.entity.target.resources.TargetResources;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.DataLocationType;

final class TargetTopPanel extends LocatableTopPanel {
   private TargetPanel targetPanel;

   public TargetTopPanel(Target var1, TargetPanel var2) {
      super(var1, SourceTarget.Target);
      this.targetPanel = var2;
   }

   @Override
   protected String getMissingTypeErrorMessage() {
      return TargetResources.getString("Target.TypeMissing");
   }

   @Override
   protected void handleTypeSelection(DataLocationType var1) {
      this.targetPanel.onLocationTypeSelected(var1);
   }
}
