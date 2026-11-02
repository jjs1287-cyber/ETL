package org.etlforge.integration.client.ui.project.browser;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.RecentProjects;
import org.etlforge.integration.client.ui.project.location.ProjectLocationBrowser;
import org.etlforge.integration.client.ui.project.location.ProjectLocationsTableModel;
import org.etlforge.integration.client.ui.project.recent.CopyProjectLocationAction;
import org.etlforge.integration.client.ui.project.recent.DeleteProjectFromDiskAction;
import org.etlforge.integration.client.ui.project.recent.RecentProjectsTableViewer;
import org.etlforge.integration.client.ui.project.recent.RecentProjectsViewer;
import org.etlforge.integration.client.ui.project.recent.RecentProjectsViewerAction;
import org.etlforge.integration.client.ui.project.recent.RemoveProjectFromHistoryAction;
import org.etlforge.integration.client.ui.project.resources.ProjectUiResources;
import org.etlforge.integration.client.ui.project.search.ProjectSearchController;
import org.etlforge.integration.client.ui.project.search.ProjectSearchUi;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.event.ActionListeners;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class ProjectBrowser implements UiProvider {
   private final RecentProjectsViewer recentProjectsViewer;
   private final ProjectBrowser.OpenAction openFromTableAction = new ProjectBrowser.OpenAction();
   private final boolean includeCancelButton;
   private Receiver<ProjectLocation> openHandler;
   private final ActionListeners cancelListeners;
   private File currentProjectLocation;
   private File defaultSearchFolder;
   private KongaDialog searchDialog;
   private JComponent ui;

   public ProjectBrowser(RecentProjects var1, boolean var2) {
      this(var1, var2, getDefaultActions());
   }

   public ProjectBrowser(RecentProjects var1, boolean var2, Iterable<RecentProjectsViewerAction> var3) {
      this.recentProjectsViewer = this.createViewer(var1, var3);
      this.includeCancelButton = var2;
      this.openHandler = NoReceiver.create();
      this.cancelListeners = new ActionListeners();
   }

   public void refresh() {
      this.recentProjectsViewer.update();
   }

   public void setOpenHandler(Receiver<ProjectLocation> var1) {
      this.openHandler = var1;
   }

   public void setCurrentProjectLocation(File var1) {
      this.currentProjectLocation = var1;
   }

   public void setDefaultSearchFolder(File var1) {
      this.defaultSearchFolder = var1;
   }

   public void addCancelListener(ActionListener var1) {
      this.cancelListeners.add(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 5);
      var2.center(this.recentProjectsViewer);
      BoxBuilder var3 = BoxBuilder.horizontal().nonOpaque();
      var3.add(this.layoutLeftButtons()).glue();
      if (this.includeCancelButton) {
         var3.add(new KongaButton(new ProjectBrowser.CancelHandler()));
      }

      var2.south(var3).nonOpaque();
      this.ui = var2.container();
      var1.display(this.ui);
   }

   private JComponent layoutLeftButtons() {
      Grid var1 = Grid.oneRow(10);
      var1.setButtonStyle(WizardStyleSheet.SMALL_CAPTION_STYLE);
      var1.setUseMediumSizedIconsForActions(true);
      var1.addAll(this.openFromTableAction, new ProjectBrowser.BrowseAction(), new ProjectBrowser.SearchAction()).nonOpaque();
      return InvisiblePanel.of(var1);
   }

   @Override
   public void requestFocus() {
      this.recentProjectsViewer.requestFocus();
   }

   private RecentProjectsViewer createViewer(RecentProjects var1, Iterable<RecentProjectsViewerAction> var2) {
      RecentProjectsTableViewer var3 = new RecentProjectsTableViewer(var1);
      var3.setActions(Iterables.concat(Collections.singletonList(this.openFromTableAction), var2));
      return var3;
   }

   public static Iterable<RecentProjectsViewerAction> getDefaultActions() {
      ArrayList var0 = Lists.newArrayList();
      Collections.addAll(var0, new CopyProjectLocationAction(), new RemoveProjectFromHistoryAction(), null, new DeleteProjectFromDiskAction());
      return var0;
   }

   private class BrowseAction extends AbstractAction {
      public BrowseAction() {
         super(ApplicationResources.STRINGS.getString("Browse.Action"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ProjectLocation var2 = ProjectLocationBrowser.browseExistingProjectLocation(ProjectBrowser.this.currentProjectLocation, null);
         if (var2 != null) {
            ProjectBrowser.this.openHandler.handle(var2);
         }
      }

      @Override
      public String toString() {
         return (String)this.getValue("Name");
      }
   }

   private class CancelHandler extends AbstractAction {
      public CancelHandler() {
         super("Cancel");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ProjectBrowser.this.cancelListeners.call().actionPerformed(var1);
      }
   }

   private class OpenAction extends AbstractSingleObjectSelectionAction<ProjectLocation> implements RecentProjectsViewerAction {
      public OpenAction() {
         super(ProjectLocation.class, ApplicationResources.STRINGS.getString("Open.Action"), CommonIcons.OPEN_16);
         this.putValue("KongaMediumIconKey", CommonIcons.OPEN_24);
         this.putValue("ShortDescription", ProjectUiResources.getString("Browser.Open.ToolTip"));
         this.putValue("AcceleratorKey", KeyUtils.ENTER);
         this.setEnabled(false);
      }

      protected void run(ProjectLocation var1, ActionEvent var2) {
         this.closeSearchDialog();
         ProjectBrowser.this.openHandler.handle(var1);
      }

      private void closeSearchDialog() {
         if (ProjectBrowser.this.searchDialog != null) {
            ProjectBrowser.this.searchDialog.dispose();
            ProjectBrowser.this.searchDialog = null;
         }
      }

      @Override
      public void setViewer(RecentProjectsViewer var1) {
         this.setSelectionSource(var1);
         var1.setDefaultAction(this);
      }
   }

   private class SearchAction extends AbstractAction {
      private ProjectSearchUi searchUi;

      public SearchAction() {
         super(ProjectUiResources.getString("Browser.Search.Name"), CommonIcons.FIND_16);
         this.putValue("KongaMediumIconKey", CommonIcons.FIND_24);
         this.putValue("ShortDescription", ProjectUiResources.getString("Browser.Search.ToolTip"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               SearchAction.this.openSearchDialog();
            }
         });
      }

      private void openSearchDialog() {
         if (this.searchUi == null) {
            this.searchUi = this.createSearchUi();
         }

         if (ProjectBrowser.this.searchDialog == null) {
            ProjectBrowser.this.searchDialog = new WizardStyleDialog(
               this.getDialogOwner(), ProjectUiResources.getString("Browser.Search.Title"), this.searchUi, KongaDialog.CLOSE_ONLY, true
            );
            ProjectBrowser.this.searchDialog.manageLocation(ProjectSearchUi.class);
         }

         ProjectBrowser.this.searchDialog.getRootPane().setDefaultButton(null);
         ProjectBrowser.this.searchDialog.setVisible(true);
      }

      private ProjectSearchUi createSearchUi() {
         ProjectLocationsTableModel var1 = new ProjectLocationsTableModel();
         ProjectSearchController var2 = new ProjectSearchController(var1);
         ProjectSearchUi var3 = new ProjectSearchUi(var1, var2);
         var3.setDefaultSearchFolder(ProjectBrowser.this.defaultSearchFolder);
         var3.setDefaultAction(ProjectBrowser.this.new OpenAction());
         return var3;
      }

      private Window getDialogOwner() {
         return ProjectBrowser.this.ui == null ? UiUtils.getActiveWindow() : SwingUtilities.getWindowAncestor(ProjectBrowser.this.ui);
      }
   }
}
