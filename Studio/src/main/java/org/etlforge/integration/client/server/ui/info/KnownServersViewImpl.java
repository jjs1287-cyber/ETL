package org.etlforge.integration.client.server.ui.info;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import org.etlforge.application.ui.help.HelpButtonResource;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.AbstractWindowSectionContent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.layout.BorderLayoutBuilder;

class KnownServersViewImpl extends AbstractWindowSectionContent implements KnownServersView {
   private final ServerManager serverManager;
   private final KnownServersViewModel model;
   private final KnownServersViewController controller;
   private final KnownServersTable table;
   private final ServerTableActions actions;
   private final JButton helpButton;

   public KnownServersViewImpl(ServerManager var1, WindowSection var2) {
      super(CONTENT_ID, var2);
      this.serverManager = var1;
      this.model = new KnownServersViewModel();
      this.controller = new KnownServersViewController(var1, this.model);
      this.table = new KnownServersTable(this.model);
      this.actions = new ServerTableActions(this.controller, this.table);
      this.helpButton = HelpButtonResource.forToolbar(this, "Known_Servers");
      this.addResource(new KnownServersViewImpl.ConnectionChangeResource());
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.controller.updateListOfServers();
   }

   @Override
   protected void populateToolBar(JToolBar var1) {
      this.actions.addToToolBar(var1);
      var1.add(this.helpButton);
      super.populateToolBar(var1);
   }

   @Override
   public void decorate(JComponent var1) {
      BorderLayoutBuilder.from(var1).north(this.getTitlePanel()).center(this.table).withBackground(StyleSheet.GenericEditorPanelClass.getBackground());
   }

   @Override
   public String getTitle() {
      return PackageResources.View.TITLE;
   }

   @Override
   public Icon getIcon() {
      return ClientIcons.SERVER_16;
   }

   private class ConnectionChangeResource extends AbstractWindowElementResource {
      private CurrentServerListener currentServerListener;

      private ConnectionChangeResource() {
      }

      @Override
      public void create(WindowElement var1) {
         this.currentServerListener = new CurrentServerListener(
            KnownServersViewImpl.this.serverManager, KnownServersViewImpl.this.model, KnownServersViewImpl.this.controller
         );
         this.currentServerListener.activate();
      }

      @Override
      public void dispose(WindowElement var1) {
         this.currentServerListener.dispose();
      }
   }
}
