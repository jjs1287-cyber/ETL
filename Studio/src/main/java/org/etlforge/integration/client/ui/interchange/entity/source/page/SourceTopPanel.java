package org.etlforge.integration.client.ui.interchange.entity.source.page;

import org.etlforge.integration.client.ui.interchange.entity.LocatableTopPanel;
import org.etlforge.integration.client.ui.interchange.entity.source.resources.SourceResources;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.location.DataLocationType;

final class SourceTopPanel extends LocatableTopPanel {
   private SourcePanel sourcePanel;

   public SourceTopPanel(Source var1, SourcePanel var2) {
      super(var1, SourceTarget.Source);
      this.sourcePanel = var2;
   }

   @Override
   protected String getMissingTypeErrorMessage() {
      return SourceResources.getString("Source.TypeMissing");
   }

   @Override
   protected void handleTypeSelection(DataLocationType var1) {
      this.sourcePanel.onLocationTypeSelected(var1);
   }
}
