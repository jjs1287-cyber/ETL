package org.etlforge.integration.client.ui.admin.component.jms;

import javax.swing.JPanel;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorIdGenerator;
import org.etlforge.integration.client.admin.Admin;
import org.etlforge.integration.client.admin.engine.GetEngineStatusJob;
import org.etlforge.integration.client.admin.engine.JmsEngine;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.ui.admin.core.console.AbstractConsolePage;
import org.etlforge.integration.client.ui.admin.engine.EngineActionPanel;
import org.etlforge.integration.client.ui.admin.engine.EngineController;
import org.etlforge.integration.client.ui.admin.engine.EngineStatusPanel;
import org.etlforge.integration.client.ui.admin.resources.AdminViewResources;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public final class JmsEngineConsolePage extends AbstractConsolePage {
   private final JmsEngine engine = new JmsEngine();
   private final EngineController controller;
   private EngineStatusPanel statusPanel;
   private EngineActionPanel actionPanel;
   private JPanel displayer;

   public JmsEngineConsolePage(Admin var1) {
      super(EditorIdGenerator.get(JmsEngineConsolePage.class), AdminViewResources.getString("Jms.Console.Title"));
      this.controller = new EngineController(this.engine, var1);
      this.setIcon(AdminViewResources.getIcon("Jms.Console"));
      this.setHelpId("administration.consoles.jms");
      this.setMementoPath(new String[]{"Admin", "JmsConsole"});
   }

   @Override
   protected void populatePopupMenu(KongaPopupMenu var1) {
      this.statusPanel.addItemsToPopupMenu(var1);
      var1.addSeparator();
      this.actionPanel.addItemsToPopupMenu(var1);
   }

   private void createComponents() {
      this.createStatusPanel();
      this.createActionPanel();
   }

   private void createStatusPanel() {
      this.statusPanel = new EngineStatusPanel(
         this.engine, this.controller, AdminViewResources.getString("Jms.StatusPanel.Title"), AdminViewResources.getIcon("Jms.StatusPanel")
      );
   }

   private void createActionPanel() {
      this.controller.getStartEngineAction().setIcon(AdminViewResources.getIcon("Jms.ActionPanel.Start"));
      this.controller.getStopEngineAction().setIcon(AdminViewResources.getIcon("Jms.ActionPanel.Stop"));
      this.actionPanel = new EngineActionPanel(
         AdminViewResources.getString("Jms.ActionPanel.Title"), AdminViewResources.getIcon("Jms.ActionPanel"), this.controller
      );
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.createComponents();
      Admin var2 = IntegrationServer.getInstance().getServerCall(Admin.class);
      GetEngineStatusJob var3 = new GetEngineStatusJob(this.engine, var2);
      var3.submit();
      this.createTitlePanel();
      this.setToolBar(this.createToolBar());
      this.displayer = this.createDisplayer();
      this.setMainPanel(this.displayer);
   }

   private JPanel createDisplayer() {
      OneColumnPanel var1 = new OneColumnPanel(OneColumnPanel.WEST, OneColumnPanel.NONE);
      var1.withInsets(5, 5, 25, 5);
      var1.add(this.statusPanel);
      var1.setFill(OneColumnPanel.HORIZONTAL);
      var1.add(this.actionPanel);
      JPanel var2 = InvisiblePanel.newPanel();
      var1.addToParent(var2, null);
      UiUtils.setOpaqueRecursivelySkipDefaults(var2, false);
      return var2;
   }
}
