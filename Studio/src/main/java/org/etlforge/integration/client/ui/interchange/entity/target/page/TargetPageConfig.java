package org.etlforge.integration.client.ui.interchange.entity.target.page;

import javax.swing.Action;
import javax.swing.KeyStroke;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.DefaultEditorController;
import org.etlforge.cli.ui.ActionCommand;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.ui.interchange.component.page.entity.ActiveObjectPageAction;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageControllerBuilder;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.entity.DefaultPageConfig;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.ui.util.KeyUtils;

public final class TargetPageConfig extends DefaultPageConfig {
   private static final KeyStroke TEST_CONNECTION_KEY = KeyUtils.getCtrlDown(122);
   private final ServerManager serverManager;
   private ApplicationAction testConnectionAction;
   private Action assignPluginAction;

   public TargetPageConfig(ApplicationWindow var1, ProjectPersistor var2, ServerManager var3) {
      super(var1, var2);
      this.serverManager = var3;
   }

   public void setTestConnectionAction(ApplicationAction var1) {
      this.testConnectionAction = var1;
   }

   public void setAssignPluginAction(Action var1) {
      this.assignPluginAction = var1;
   }

   @Override
   protected void preConfiguration(IntegrationEntityPage var1) {
      ((TargetPage)var1).setServerManager(this.serverManager);
      if (this.testConnectionAction != null) {
         this.installTestConnection(var1);
      }

      if (this.assignPluginAction != null) {
         this.addCommand(new ActionCommand("plugins", this.assignPluginAction));
      }
   }

   private void installTestConnection(IntegrationEntityPage var1) {
      Action var2 = this.createTestConnectionWrapper(var1);
      ((TargetPage)var1).setTestConnectionAction(var2);
      this.addToolbarButton(this.createToolbarButton(var2));
      this.installController(var1);
      this.addCommand(new ActionCommand("test connection", this.testConnectionAction));
   }

   private Action createTestConnectionWrapper(IntegrationEntityPage var1) {
      ActiveObjectPageAction var2 = this.createActiveObjectAction(var1, this.testConnectionAction);
      var2.setSaveIfDirty(true);
      return var2;
   }

   private void installController(IntegrationEntityPage var1) {
      DefaultEditorController var2 = EntityPageControllerBuilder.buildController(var1);
      var2.registerAction(TEST_CONNECTION_KEY, this.testConnectionAction);
      this.setController(var2);
   }
}
