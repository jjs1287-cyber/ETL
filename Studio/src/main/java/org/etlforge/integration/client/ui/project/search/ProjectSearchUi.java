package org.etlforge.integration.client.ui.project.search;

import com.google.common.base.Predicate;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.search.string.SearchRuleOption;
import org.etlforge.application.ui.search.string.StringFilterField;
import org.etlforge.application.ui.widget.FolderSelectorPanelBuilder;
import org.etlforge.integration.client.ui.project.location.ProjectLocationsTable;
import org.etlforge.integration.client.ui.project.location.ProjectLocationsTableModel;
import org.etlforge.integration.client.ui.project.resources.ProjectUiResources;
import org.etlforge.io.FileUtils;
import org.etlforge.io.Folder;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.table.SortStatusPreferencesMemento;
import org.etlforge.util.preference.PreferencesKey;

public class ProjectSearchUi implements UiProvider {
   private final ProjectSearchController controller;
   private final FileSelectorPanel rootSelector;
   private final StringFilterField nameFilterField;
   private final ProjectLocationsTableModel resultModel;
   private final ProjectLocationsTable resultTable;
   private final ProjectSearchUi.StartSearchAction startAction;
   private final ProjectSearchUi.StopSearchAction stopAction;
   private final ProjectSearchUi.StatusPanel statusPanel;
   private File defaultSearchFolder;

   public ProjectSearchUi(ProjectLocationsTableModel var1, ProjectSearchController var2) {
      this.resultModel = var1;
      this.controller = var2;
      var2.setView(this);
      this.rootSelector = this.createRootSelector();
      StringFilterField var3 = this.createNameFilterField();
      this.nameFilterField = var3;
      this.resultTable = this.createResultTable();
      this.startAction = new ProjectSearchUi.StartSearchAction();
      this.rootSelector.addActionListener(this.startAction);
      var3.addActionListener(this.startAction);
      this.stopAction = new ProjectSearchUi.StopSearchAction();
      this.statusPanel = new ProjectSearchUi.StatusPanel();
   }

   private FileSelectorPanel createRootSelector() {
      FolderSelectorPanelBuilder var1 = new FolderSelectorPanelBuilder();
      var1.setPreferenceKey(new PreferencesKey(ProjectSearchUi.class, "Root"))
         .setDefaultFolder(this.defaultSearchFolder)
         .setTitle(ProjectUiResources.getString("Search.RootSelector"));
      return var1.buildPanel(null);
   }

   private StringFilterField createNameFilterField() {
      StringFilterField var1 = new StringFilterField(
         SearchRuleOption.CONTAINS_IGNORE_CASE, SearchRuleOption.STARTS_WITH_IGNORE_CASE, SearchRuleOption.WILDCARD_IGNORE_CASE
      );
      var1.setLabel("(Optional) &Name filter:");
      var1.setLabelLayout(StringFilterField.LabelLayout.VERTICAL);
      var1.setColumns(20);
      return var1;
   }

   private ProjectLocationsTable createResultTable() {
      ProjectLocationsTable var1 = new ProjectLocationsTable(this.resultModel);
      var1.setVisibleRows(6);
      var1.setSortStatusMemento(new SortStatusPreferencesMemento(this.getClass()));
      return var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      ProjectSearchUi.Layout var2 = new ProjectSearchUi.Layout();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.rootSelector.requestFocus();
   }

   public void setDefaultSearchFolder(File var1) {
      this.defaultSearchFolder = var1;
   }

   public void setDefaultAction(SelectionAction var1) {
      this.resultTable.setDefaultAction(var1);
   }

   public void showStatus(File var1) {
      this.statusPanel.setCurrentFolder(var1);
   }

   public void setSearching(boolean var1) {
      this.startAction.setEnabled(!var1);
      this.stopAction.setEnabled(var1);
   }

   public void searchIsDone() {
      this.resultTable.selectFirst();
      this.resultTable.requestFocus();
      this.statusPanel.publishProjectCount();
      this.resultTable.selectFirst();
      this.resultTable.requestFocus();
   }

   private class Layout {
      private final BorderLayoutBuilder layout = new BorderLayoutBuilder(0, 10);

      public Layout() {
         this.layoutNorth();
         this.layoutTable();
         this.layoutSouth();
      }

      private void layoutNorth() {
         this.layout.north(Vertical.grid().of(ProjectSearchUi.this.rootSelector, InvisiblePanel.of(ProjectSearchUi.this.nameFilterField)).withVGap(10));
      }

      private void layoutTable() {
         BoxBuilder var1 = BoxBuilder.horizontal();
         var1.addAllWithSpace(10, ProjectSearchUi.this.startAction, ProjectSearchUi.this.stopAction, ProjectSearchUi.this.statusPanel);
         this.layout.center(CaptionLayout.of(var1, ProjectSearchUi.this.resultTable));
      }

      private void layoutSouth() {
         BoxBuilder var1 = BoxBuilder.horizontal();
         var1.addAllWithSpace(20, ProjectSearchUi.this.resultTable.getDefaultAction(), this.createHideIncompatibleProjectsSwitch()).glue();
         this.layout.south(var1);
      }

      private UiProvider createHideIncompatibleProjectsSwitch() {
         return null;
      }

      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }
   }

   private class StartSearchAction extends AbstractAction {
      public StartSearchAction() {
         super(ProjectUiResources.getString("Search.Start"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         File var2 = ProjectSearchUi.this.rootSelector.getSelectedFile();
         if (this.isLocationOk(var2)) {
            Predicate var3 = ProjectSearchUi.this.nameFilterField.getFilter();
            ProjectSearchUi.this.controller.startSearch(new Folder(var2), var3);
         }
      }

      private boolean isLocationOk(File var1) {
         if (var1 == null) {
            return false;
         } else if (!var1.isDirectory()) {
            Alert.error(
               ProjectUiResources.formatMessage("Search.InvalidRoot", var1.getAbsolutePath()), ProjectUiResources.getString("Search.InvalidRoot.Title")
            );
            return false;
         } else {
            return true;
         }
      }
   }

   private class StatusPanel implements UiProvider {
      private static final int MAX_PATH_LEN = 100;
      private final JLabel label = new JLabel(" ");

      private StatusPanel() {
      }

      @Override
      public void decorate(UiCanvas var1) {
         var1.display(this.label);
      }

      @Override
      public void requestFocus() {
      }

      public void setCurrentFolder(File var1) {
         if (var1 != null) {
            this.label.setText(ProjectUiResources.formatMessage("Search.Status", FileUtils.leftTrimPath(var1, 100)));
         } else {
            this.label.setText(" ");
         }
      }

      public void publishProjectCount() {
         this.label.setText(ProjectSearchUi.this.resultModel.getRowCount() + " project(s) found");
      }
   }

   private class StopSearchAction extends AbstractAction {
      public StopSearchAction() {
         super(ProjectUiResources.getString("Search.Stop"));
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ProjectSearchUi.this.controller.stopSearch();
      }
   }
}
