package org.etlforge.integration.client.server.ui.info;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JToolBar;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.PopupMenuRequestHandlers;
import org.etlforge.ui.widget.button.KongaButton;

public final class ServerTableActions {
   private final SelectionAction removeAction;
   private final Action refreshAction;
   private final SelectionAction loginAction;
   private final SelectionAction viewDetailsAction;

   public ServerTableActions(KnownServersViewController var1, KnownServersTable var2) {
      this.removeAction = new ServerTableActions.RemoveAction(var1);
      this.refreshAction = new ServerTableActions.RefreshAction(var1);
      this.loginAction = new ServerTableActions.LoginAction(var1);
      this.viewDetailsAction = new ServerTableActions.ViewDetailsAction(var2);
      this.removeAction.setSelectionSource(var2);
      this.loginAction.setSelectionSource(var2);
      var2.setDefaultAction(this.viewDetailsAction);
      var2.setPopupMenuRequestHandler(PopupMenuRequestHandlers.with(this.loginAction, this.viewDetailsAction, null, this.removeAction));
      var2.installKeyBinding(this.removeAction);
   }

   public void addToToolBar(JToolBar var1) {
      var1.add(KongaButton.forToolbar(this.loginAction));
      var1.add(KongaButton.forToolbar(this.viewDetailsAction));
      var1.add(KongaButton.forToolbar(this.refreshAction));
      var1.add(KongaButton.forToolbar(this.removeAction));
   }

   public Action refresh() {
      return this.refreshAction;
   }

   private static class LoginAction extends AbstractSingleObjectSelectionAction<KnownServer> {
      private final KnownServersViewController controller;

      public LoginAction(KnownServersViewController var1) {
         super(KnownServer.class);
         this.controller = var1;
         ActionDecorators.LOGIN.decorate(this);
         this.putValue("ShortDescription", PackageResources.Actions.Login.TOOLTIP);
      }

      protected void run(final KnownServer var1, ActionEvent var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               LoginAction.this.controller.login(var1);
            }
         });
      }
   }

   private static class RefreshAction extends AbstractAction {
      private final KnownServersViewController controller;

      public RefreshAction(KnownServersViewController var1) {
         this.controller = var1;
         ActionDecorators.REFRESH.decorate(this);
         this.putValue("ShortDescription", PackageResources.Actions.Refresh.TOOLTIP);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.controller.updateListOfServers();
      }
   }

   private static class RemoveAction extends AbstractSingleTypeSelectionAction<KnownServer> {
      private final KnownServersViewController controller;

      public RemoveAction(KnownServersViewController var1) {
         super(KnownServer.class, PackageResources.Actions.Remove.NAME, PackageResources.Actions.Remove.ICON);
         this.controller = var1;
         this.putValue("AcceleratorKey", KeyUtils.DELETE);
         this.putValue("ShortDescription", PackageResources.Actions.Remove.TOOLTIP);
      }

      @Override
      protected void run(List<KnownServer> var1) {
         this.controller.removeServers(var1);
      }
   }

   private static class ViewDetailsAction extends AbstractSingleObjectSelectionAction<KnownServer> {
      public ViewDetailsAction(KnownServersTable var1) {
         super(KnownServer.class);
         ActionDecorators.VIEW_DETAILS.decorate(this);
         this.putValue("ShortDescription", PackageResources.Actions.ViewDetails.TOOLTIP);
         this.setSelectionSource(var1);
      }

      public void run(final KnownServer var1, ActionEvent var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ViewDetailsAction.this.viewDetails(var1);
            }
         });
      }

      private void viewDetails(KnownServer var1) {
         ServerDetailsViewer var2 = new ServerDetailsViewer(var1);
         KongaDialog var3 = new KongaDialog(ApplicationUi.getAppWinFrame(), PackageResources.Viewer.TITLE, true);
         var3.standardLayout(var2, KongaDialog.CLOSE_ONLY);
         var3.setCloseButtonAsDefault();
         var3.setFocusedComponent(var2);
         var3.setLocationRelativeTo(null);
         var3.setResizable(false);
         var3.setVisible(true);
      }
   }
}
