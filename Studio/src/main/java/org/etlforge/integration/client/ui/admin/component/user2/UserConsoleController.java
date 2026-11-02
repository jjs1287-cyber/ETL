package org.etlforge.integration.client.ui.admin.component.user2;

import com.google.common.base.Supplier;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.ServerConnectionListener;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ServerInformationStore;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.useradmin.UserAdmin;
import org.etlforge.integration.client.ui.admin.prefs.DownloadUsersAndGroupsAutomaticallyPreference;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.Role;
import org.etlforge.integration.user.User;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.CallbackResult;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public final class UserConsoleController implements Disposable {
   private final ServerManager serverManager;
   private final UserAdmin serverApi;
   private final List<UserConsoleAction> optionalActions;
   private UserConsoleModel model;
   private UserConsoleModelListener modelListener;
   private UserConsoleView view;
   private ServerConnectionListener connectionListener;
   private UserConsoleActions actions;
   private CallbackResult successfulSaveCallback;
   private DeleteSelectionItemsAction deleteSelectedItemsAction;

   public UserConsoleController(ServerManager var1, UserAdmin var2) {
      this(var1, var2, new ArrayList<>());
   }

   public UserConsoleController(ServerManager var1, UserAdmin var2, List<UserConsoleAction> var3) {
      this.serverManager = var1;
      this.serverApi = var2;
      this.optionalActions = var3;
   }

   public void setView(UserConsoleView var1) {
      this.view = var1;
      this.model = var1.getModel();
      this.model.addModelListener(this.modelListener = new UserConsoleController.ModelListener());
      this.connectionListener = new UserConsoleController.CurrentServerListener();
      this.actions = new UserConsoleActions(var1, this, this.optionalActions);
      this.deleteSelectedItemsAction = new DeleteSelectionItemsAction(this.model, var1.getUsersAndGroupsViewer());
      this.configureView();
   }

   private void configureView() {
      this.view.setModelActions(this.getModelActions());
      this.view.setServerActions(this.getServerActions());
      this.view.setViewActions(this.getViewActions());
      this.addKeyBindings(this.view.getUsersAndGroupsViewer());
      this.view.getUsersAndGroupsViewer().setDefaultAction(new UserConsoleController.DefaultAction());
      this.view.getUsersAndGroupsViewer().setContextMenuProvider(new UserConsoleController.ContextMenuProvider());
   }

   @Override
   public void dispose() {
      this.serverManager.removeServerConnectionListener(this.connectionListener);
      this.model.removeModelListener(this.modelListener);
      this.actions.dispose();
   }

   public UserAdmin getServerApi() {
      return this.serverApi;
   }

   public ServerInformationStore getServerInfoStore() {
      return this.serverManager.getServerInfoStore();
   }

   public void activate() {
      this.serverManager.addServerConnectionListener(this.connectionListener);
      this.setInitialState();
   }

   public void setSuccessfulSaveCallback(CallbackResult var1) {
      this.successfulSaveCallback = var1;
   }

   private void setInitialState() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            UserConsoleController.this.model.setCurrentServer(UserConsoleController.this.serverManager.getCurrentServerInfo());
            User var1 = UserConsoleController.this.serverManager.getCurrentUser();
            UserConsoleController.this.checkIfUserHasWriteAccess(var1);
            if (DownloadUsersAndGroupsAutomaticallyPreference.get()) {
               UserConsoleController.this.downloadUsersAndGroups();
            }
         }
      });
   }

   private void downloadUsersAndGroups() {
      ActionUtils.run(this.actions.getDownloadAction(), this, "download");
   }

   private void checkIfUserHasWriteAccess(User var1) {
      if (var1 != null) {
         this.model.setReadOnly(!var1.isAdmin());
      }
   }

   public void save() throws InvalidUserDataException {
      this.view.getDetailsViewer().apply();
      this.model.validate();
      new UploadJob(this.serverApi, this.model, this.successfulSaveCallback).submit();
   }

   public Action[] getServerActions() {
      return this.actions.getServerActions();
   }

   public Action[] getModelActions() {
      return this.actions.getModelActions();
   }

   public Action[] getViewActions() {
      return this.actions.getViewActions();
   }

   public void displayDetails(Role var1) {
      if (var1 instanceof User) {
         this.displayUser((User)var1);
      } else if (var1 instanceof Group) {
         this.displayGroup((Group)var1);
      } else if (var1 instanceof GroupMember) {
         this.displayUser(((GroupMember)var1).getUser());
      }
   }

   public void displayUser(User var1) {
      this.view.getDetailsViewer().displayUser(var1);
   }

   public void displayGroup(Group var1) {
      this.view.getDetailsViewer().displayGroup(var1);
   }

   private void addKeyBindings(UsersAndGroupsViewer var1) {
      var1.addKeyBinding("delete", this.deleteSelectedItemsAction, null);
   }

   private class ContextMenuProvider implements Supplier<JPopupMenu> {
      private ContextMenuProvider() {
      }

      public JPopupMenu get() {
         KongaPopupMenu var1 = null;
         OrderedSelection var2 = (OrderedSelection)UserConsoleController.this.view.getUsersAndGroupsViewer().getSelection();

         for (Action var6 : UserConsoleController.this.actions.getActionsForSelection(var2)) {
            if (var1 == null) {
               var1 = new KongaPopupMenu();
            }

            var1.add(var6);
         }

         return var1;
      }
   }

   private class CurrentServerListener implements ServerConnectionListener {
      private CurrentServerListener() {
      }

      @Override
      public void serverConnectionChanged(final ServerConnectionEvent var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               ServerInfo var1x = UserConsoleController.this.model.getCurrentServer();
               ServerInfo var2 = var1.getServerInfo();
               UserConsoleController.this.model.setCurrentServer(var2);
               this.checkReadOnly(var1);
               if (CurrentServerListener.this.loggedInToNewServer(var1x, var2)) {
                  UserConsoleController.this.downloadUsersAndGroups();
               }
            }

            private void checkReadOnly(ServerConnectionEvent var1x) {
               User var2 = var1x.getUser();
               UserConsoleController.this.checkIfUserHasWriteAccess(var2);
            }
         });
      }

      private boolean loggedInToNewServer(ServerInfo var1, ServerInfo var2) {
         if (var2 == null) {
            return false;
         } else {
            return var1 == null ? true : !var2.getGuid().equals(var1.getGuid());
         }
      }
   }

   private class DefaultAction extends AbstractSingleObjectSelectionAction<Object> {
      public DefaultAction() {
         super(Object.class);
      }

      @Override
      protected void run(Object var1, ActionEvent var2) {
         if (var1 instanceof ServerInfo || var1 instanceof DisconnectedMarker) {
            UserConsoleController.this.actions.getDownloadAction().actionPerformed(var2);
         } else if (var1 instanceof Role) {
            UserConsoleController.this.displayDetails((Role)var1);
         }
      }
   }

   private class ModelListener extends UserConsoleModelListener.Adapter {
      private ModelListener() {
      }

      @Override
      public void groupAdded(UserConsoleModel var1, Group var2) {
         UserConsoleController.this.displayGroup(var2);
      }

      @Override
      public void groupRemoved(UserConsoleModel var1, Group var2) {
         if (var2 == UserConsoleController.this.view.getDetailsViewer().getDisplayedGroup()) {
            UserConsoleController.this.displayGroup(null);
         }
      }

      @Override
      public void userAdded(UserConsoleModel var1, User var2) {
         UserConsoleController.this.displayUser(var2);
      }

      @Override
      public void userRemoved(UserConsoleModel var1, User var2) {
         if (var2 == UserConsoleController.this.view.getDetailsViewer().getDisplayedUser()) {
            UserConsoleController.this.displayUser(null);
         }
      }

      @Override
      public void activeStatusChanged(UserConsoleModel var1, boolean var2) {
         if (!var2) {
            UserConsoleController.this.view.getDetailsViewer().clear();
         }
      }

      @Override
      public void readOnlyStateChanged(UserConsoleModel var1, boolean var2) {
         UserConsoleController.this.deleteSelectedItemsAction.setReadOnly(var2);
      }
   }
}
