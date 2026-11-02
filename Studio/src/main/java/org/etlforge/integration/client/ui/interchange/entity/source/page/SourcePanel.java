package org.etlforge.integration.client.ui.interchange.entity.source.page;

import javax.swing.Action;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.client.ui.interchange.entity.LocatablePanel;
import org.etlforge.integration.client.ui.interchange.entity.LocatableTopPanel;
import org.etlforge.integration.client.ui.interchange.entity.source.resources.SourceResources;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Source;

public class SourcePanel extends LocatablePanel {
   public SourcePanel(ApplicationWindow var1, Source var2, Action var3) {
      super(var1, var2, SourceTarget.Source, var3);
   }

   @Override
   protected LocatableTopPanel createTopPanel(Locatable var1) {
      return new SourceTopPanel((Source)var1, this);
   }

   public Source getDisplayedObject() {
      return (Source)this.getLocatable();
   }

   @Override
   protected void validate() throws IntegrationDataPanelException {
      LocatableTopPanel var1 = this.getTopPanel();
      if (var1.getType() == null) {
         throw new IntegrationDataPanelException(SourceResources.getString("Source.TypeMissing"));
      }
   }

   @Override
   protected void applyOtherPropertiesThanTheName() throws IntegrationDataPanelException {
      this.applyLocation();
      this.getDisplayedObject().setLocation(this.getDisplayedLocation());
   }
}
