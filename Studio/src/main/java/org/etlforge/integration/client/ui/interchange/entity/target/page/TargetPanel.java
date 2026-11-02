package org.etlforge.integration.client.ui.interchange.entity.target.page;

import javax.swing.Action;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.client.ui.interchange.entity.LocatablePanel;
import org.etlforge.integration.client.ui.interchange.entity.LocatableTopPanel;
import org.etlforge.integration.client.ui.interchange.entity.target.resources.TargetResources;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Target;

public class TargetPanel extends LocatablePanel {
   public TargetPanel(ApplicationWindow var1, Target var2, Action var3) {
      super(var1, var2, SourceTarget.Target, var3);
   }

   @Override
   protected LocatableTopPanel createTopPanel(Locatable var1) {
      return new TargetTopPanel((Target)var1, this);
   }

   public Target getDisplayedObject() {
      return (Target)this.getLocatable();
   }

   @Override
   protected void validate() throws IntegrationDataPanelException {
      LocatableTopPanel var1 = this.getTopPanel();
      if (var1.getType() == null) {
         throw new IntegrationDataPanelException(TargetResources.getString("Target.TypeMissing"));
      }
   }

   @Override
   protected void applyOtherPropertiesThanTheName() throws IntegrationDataPanelException {
      this.applyLocation();
      this.getDisplayedObject().setLocation(this.getDisplayedLocation());
   }
}
