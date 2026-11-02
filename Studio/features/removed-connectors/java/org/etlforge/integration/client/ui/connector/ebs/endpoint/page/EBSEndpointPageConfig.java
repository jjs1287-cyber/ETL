package org.etlforge.integration.client.ui.connector.ebs.endpoint.page;

import javax.swing.Action;
import javax.swing.JButton;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.cli.ui.ActionCommand;
import org.etlforge.integration.client.ui.interchange.component.page.entity.ActiveObjectPageAction;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.entity.DefaultPageConfig;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class EBSEndpointPageConfig extends DefaultPageConfig {
   private final ApplicationAction testConnectionAction;

   public EBSEndpointPageConfig(ApplicationWindow var1, ProjectPersistor var2, ApplicationAction var3) {
      super(var1, var2);
      this.testConnectionAction = var3;
   }

   @Override
   protected void preConfiguration(IntegrationEntityPage var1) {
      if (this.testConnectionAction != null) {
         this.installTestAction((EBSEndpointPage)var1);
      }
   }

   private void installTestAction(EBSEndpointPage var1) {
      this.addToolbarButton(this.testConnectionButton(var1));
      this.addCommand(new ActionCommand("test", this.testConnectionAction));
      var1.setTestConnectionAction(this.getTestConnectionWrapper(var1));
   }

   private JButton testConnectionButton(EBSEndpointPage var1) {
      ActiveObjectPageAction var2 = this.createActiveObjectAction(var1, this.testConnectionAction);
      return this.createToolbarButton(var2);
   }

   private Action getTestConnectionWrapper(EBSEndpointPage var1) {
      ActiveObjectPageAction var2 = this.createActiveObjectAction(var1, this.testConnectionAction);
      var2.setSaveIfDirty(true);
      return var2;
   }
}
