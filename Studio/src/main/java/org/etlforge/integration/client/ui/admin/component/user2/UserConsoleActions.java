package org.etlforge.integration.client.ui.admin.component.user2;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.Role;
import org.etlforge.integration.user.User;
import org.etlforge.system.Disposable;
import org.etlforge.system.DisposableCollection;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionSource;

final class UserConsoleActions implements Disposable {
   private final UserConsoleModel model;
   private final UserConsoleActions.ModelListener modelListener;
   private final UserConsoleController controller;
   private final DownloadAction downloadAction;
   private final UploadAction uploadAction;
   private final ImmutableList<UserConsoleAction> additionalServerActions;
   private final AddNewUserAction addNewUserAction;
   private final AddNewGroupAction addNewGroupAction;
   private final DeleteUserAction deleteUserAction;
   private final DeleteGroupAction deleteGroupAction;
   private final RemoveGroupMemberAction removeGroupMemberAction;
   private final UserConsoleActions.ViewDetailsAction viewDetailsAction;
   private final DisposableCollection disposableElements;

   public UserConsoleActions(UserConsoleView var1, UserConsoleController var2, List<UserConsoleAction> var3) {
      this.model = var1.getModel();
      this.model.addModelListener(this.modelListener = new UserConsoleActions.ModelListener());
      this.controller = var2;
      this.disposableElements = new DisposableCollection();
      this.downloadAction = new DownloadAction(this.model, var2.getServerApi());
      this.uploadAction = new UploadAction(this.model, var2);
      this.disposableElements.add(this.uploadAction);
      this.additionalServerActions = ImmutableList.copyOf(var3);
      this.initOptionalActions();
      this.addNewUserAction = new AddNewUserAction(this.model);
      this.addNewGroupAction = new AddNewGroupAction(this.model);
      this.deleteUserAction = new DeleteUserAction(this.model, var1.getUsersAndGroupsViewer());
      this.deleteGroupAction = new DeleteGroupAction(this.model, var1.getUsersAndGroupsViewer());
      this.removeGroupMemberAction = new RemoveGroupMemberAction(var1.getUsersAndGroupsViewer());
      this.viewDetailsAction = new UserConsoleActions.ViewDetailsAction(var1.getUsersAndGroupsViewer());
   }

   private void initOptionalActions() {
      for (UserConsoleAction var2 : this.additionalServerActions) {
         var2.setController(this.controller);
         var2.setModel(this.model);
         if (var2 instanceof Disposable) {
            this.disposableElements.add((Disposable)var2);
         }
      }
   }

   @Override
   public void dispose() {
      this.model.removeModelListener(this.modelListener);
      this.disposableElements.dispose();
   }

   public Action getDownloadAction() {
      return this.downloadAction;
   }

   public Action[] getServerActions() {
      ArrayList var1 = Lists.newArrayList();
      var1.add(this.downloadAction);
      var1.add(this.uploadAction);
      var1.addAll(this.additionalServerActions);
      return (Action[]) var1.toArray(new Action[var1.size()]);
   }

   public Action[] getModelActions() {
      return new Action[]{this.addNewUserAction, this.deleteUserAction, this.addNewGroupAction, this.deleteGroupAction, this.removeGroupMemberAction};
   }

   public Action[] getViewActions() {
      return new Action[]{this.viewDetailsAction};
   }

   public Action[] getActionsForSelection(OrderedSelection var1) {
      ArrayList var2 = Lists.newArrayList();
      if (var1.size() == 1) {
         Object var3 = var1.head();
         if (var3 instanceof ServerInfo) {
            Collections.addAll(var2, this.getServerActions());
         } else if (var3 instanceof DisconnectedMarker) {
            var2.add(this.downloadAction);
         } else if (var3 instanceof UserRoot) {
            var2.add(this.addNewUserAction);
         } else if (var3 instanceof User) {
            var2.add(this.viewDetailsAction);
            var2.add(this.deleteUserAction);
         } else if (var3 instanceof GroupRoot) {
            var2.add(this.addNewGroupAction);
         } else if (var3 instanceof Group) {
            var2.add(this.viewDetailsAction);
            var2.add(this.deleteGroupAction);
         } else if (var3 instanceof GroupMember) {
            var2.add(this.removeGroupMemberAction);
         }
      } else if (var1.size() > 1) {
      }

      return (Action[]) var2.toArray(new Action[var2.size()]);
   }

   private class ModelListener extends UserConsoleModelListener.Adapter {
      private ModelListener() {
      }

      @Override
      public void readOnlyStateChanged(UserConsoleModel var1, boolean var2) {
         UserConsoleActions.this.deleteUserAction.setReadOnly(var2);
         UserConsoleActions.this.deleteGroupAction.setReadOnly(var2);
      }
   }

   private class ViewDetailsAction extends AbstractSingleObjectSelectionAction<Role> {
      public ViewDetailsAction(SelectionSource var2) {
         super(Role.class, "View Details", CommonIcons.VIEW_DETAILS_16);
         this.putValue("SwingLargeIconKey", CommonIcons.VIEW_DETAILS_32);
         this.putValue("ShortDescription", "View/Update the details of the selected user or group");
         this.setSelectionSource(var2);
      }

      protected void run(Role var1, ActionEvent var2) {
         UserConsoleActions.this.controller.displayDetails(var1);
      }

      protected boolean isEnabledFor(Role var1) {
         this.putValue("ShortDescription", "View/Update the details of \"" + var1.getName() + "\"");
         return super.isEnabledFor(var1);
      }

      @Override
      protected void onInvalidSelection() {
         this.putValue("ShortDescription", "View/Update the details of the selected user or group");
      }
   }
}
