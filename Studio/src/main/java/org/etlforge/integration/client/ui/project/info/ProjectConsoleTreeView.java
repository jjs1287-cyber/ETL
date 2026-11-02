package org.etlforge.integration.client.ui.project.info;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.widget.HideableSplitPane;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class ProjectConsoleTreeView implements ProjectConsoleView {
   private final ApplicationWindow appWin;
   private final ProjectConsoleModel model;
   private final ServerProjectsTree tree;
   private final ProjectConsoleSubViewContainer subViews;
   private final HideableSplitPane splitPane;
   private Action downloadAction;
   private ImmutableList<ProjectConsoleAction> consoleActions = ImmutableList.of();
   private ActionControlPanel controlPanel;
   private WaitService waitService = WaitService.NONE;

   public ProjectConsoleTreeView(ApplicationWindow var1) {
      this(var1, new ProjectConsoleModel());
   }

   public ProjectConsoleTreeView(ApplicationWindow var1, ProjectConsoleModel var2) {
      this.appWin = var1;
      this.model = var2;
      this.tree = new ServerProjectsTree(var2);
      this.subViews = new ProjectConsoleSubViewTabContainer();
      this.splitPane = new HideableSplitPane(0, 1);
      this.splitPane.setDividerHidden(true);
   }

   @Override
   public ApplicationWindow getAppWin() {
      return this.appWin;
   }

   @Override
   public ProjectConsoleModel getModel() {
      return this.model;
   }

   @Override
   public void dispose() {
      this.tree.dispose();
   }

   public ServerProjectsTree getProjectsViewer() {
      return this.tree;
   }

   @Override
   public void setDownloadAction(Action var1) {
      this.downloadAction = var1;
      this.tree.setRefreshAction(var1);
   }

   @Override
   public void setConsoleActions(List<ProjectConsoleAction> var1) {
      this.consoleActions = ImmutableList.copyOf(var1);
   }

   @Override
   public WaitService getWaitService() {
      return this.waitService;
   }

   @Override
   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.controlPanel = new ActionControlPanel(this.downloadAction, this.consoleActions);
      this.splitPane.setTopComponent(this.tree);
      this.splitPane.setBottomComponent(this.subViews);
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(20, 0);
      var2.center(this.splitPane).east(this.controlPanel);
      var2.deeplyNonOpaque().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocus();
   }

   public void setTitle(String var1) {
      this.tree.setTitle(var1);
   }

   @Override
   public ProjectConsoleSubView getSubView(String var1) {
      return this.subViews.getSubView(var1);
   }

   @Override
   public void showSubView(ProjectConsoleSubView var1) {
      boolean var2 = this.subViews.getViewCount() == 0;
      this.subViews.showSubView(var1);
      if (var2) {
         this.splitPane.setDividerHidden(false);
         this.splitPane.setDividerLocation(0.5);
      }
   }

   @Override
   public void closeSubView(String var1) {
      this.subViews.removeSubView(var1);
      boolean var2 = this.subViews.getViewCount() == 0;
      if (var2) {
         this.splitPane.setDividerHidden(true);
      }
   }

   public void setFilterControl(TreeFilterControl var1) {
      this.tree.setFilterControl(var1);
   }

   public void setExpansionFilter(Predicate<? super KongaTreeNode> var1) {
      this.tree.setExpansionFilter(var1);
   }
}
