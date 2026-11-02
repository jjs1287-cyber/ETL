package org.etlforge.integration.client.ui.admin.component.user2;

import javax.swing.Action;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.InputChangeListener;

public final class UserConsoleTreeView implements UserConsoleView {
   private final UserConsoleModel model;
   private final UsersAndGroupsViewer tree;
   private ServerItemDetailsViewer detailsViewer;
   private Action[] serverActions;
   private Action[] modelActions;
   private Action[] viewActions;

   public UserConsoleTreeView(UserConsoleModel var1) {
      this.model = var1;
      this.tree = new UsersAndGroupsTree(var1);
      this.detailsViewer = new StandardDetailsViewer(var1);
   }

   @Override
   public UserConsoleModel getModel() {
      return this.model;
   }

   @Override
   public void dispose() {
      this.tree.dispose();
      this.detailsViewer.dispose();
   }

   @Override
   public void decorate(UiCanvas var1) {
      SplitButtonsLayout2 var2 = new SplitButtonsLayout2(this, this.serverActions, this.modelActions, this.viewActions);
      var1.display(var2.getUi());
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocus();
   }

   @Override
   public void setDetailsViewer(ServerItemDetailsViewer var1) {
      this.detailsViewer = var1;
   }

   @Override
   public ServerItemDetailsViewer getDetailsViewer() {
      return this.detailsViewer;
   }

   @Override
   public UsersAndGroupsViewer getUsersAndGroupsViewer() {
      return this.tree;
   }

   @Override
   public void setModelActions(Action[] var1) {
      this.modelActions = (Action[])var1.clone();
   }

   @Override
   public void setServerActions(Action[] var1) {
      this.serverActions = (Action[])var1.clone();
   }

   @Override
   public void setViewActions(Action[] var1) {
      this.viewActions = (Action[])var1.clone();
   }

   @Override
   public void addChangeListener(InputChangeListener var1) {
      this.detailsViewer.addChangeListener(var1);
   }
}
