package org.etlforge.integration.activity.ui.log;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.CloseEditorAction;
import org.etlforge.application.ui.window.editor.EditorCategory;
import org.etlforge.application.ui.window.editor.EditorIdGenerator;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.serverapi.log.OperationLogProvider;
import org.etlforge.integration.activity.ui.component.filter.FilterDescriptionPanel;
import org.etlforge.integration.activity.ui.component.page.AbstractActivityPage;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.property.WaitStateBinding;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.focus.FocusUtils;

public final class OperationLogPage extends AbstractActivityPage {
   private final OperationLogController controller;
   private final OperationLogDisplayer logDisplayer;
   private final OperationLogModel model;
   private final OperationLogNavigationControl navigationControl;
   private final FilterDescriptionPanel filterDescription;
   private final LogMessageDisplayer messageDisplayer;

   public OperationLogPage(ActivitySubject var1, OperationLogProvider var2) {
      super(EditorIdGenerator.get(OperationLogPage.class), var1);
      this.model = new OperationLogModel(var1);
      this.model.setSortStatus(SortStatus.descending(OperationLogTableColumn.TIME_DONE));
      this.controller = new OperationLogController(this.model, var2);
      this.navigationControl = this.createNavigationControl();
      this.filterDescription = new FilterDescriptionPanel(this.navigationControl.getFilters());
      this.messageDisplayer = new HtmlLogMessageDisplayer();
      this.logDisplayer = this.createDisplayer();
      this.setIcon(PackageResources.Page.ICON);
      this.setCategory(new EditorCategory("Operation Logs", this.getSmallIcon()));
      this.addKeyBinding(KeyUtils.REFRESH, this.navigationControl.getRefreshAction());
   }

   private OperationLogNavigationControl createNavigationControl() {
      OperationLogNavigationControl var1 = new OperationLogNavigationControl(this.model, this.controller);
      var1.addControl(this.createHelpButton("activity.operation.log"), 2147483646);
      var1.setCloseAction(new CloseEditorAction(this));
      var1.applyFiltersToModel();
      var1.setDisplayedSubject(this.getObject());
      return var1;
   }

   private OperationLogDisplayer createDisplayer() {
      OperationLogDisplayer var1 = new OperationLogDisplayer(this.model, this.filterDescription, LogTableColumnsPreference.selectedColumns());
      var1.setSelectionListener(new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            OperationLogPage.this.showLogDetails();
         }
      });
      return var1;
   }

   private void showLogDetails() {
      OperationLogEntry var1 = this.logDisplayer.getSelectedLogEntry();
      if (var1 != null) {
         this.messageDisplayer.displayDetails(var1);
      }
   }

   @Override
   public String getTitle() {
      String var1 = this.navigationControl.getTitle();
      ActivitySubject var2 = this.getObject();
      if (var2 != null) {
         var1 = var1 + " " + var2.getName();
      } else {
         var1 = PackageResources.Page.getEmptyPageTitle(var1);
      }

      return var1;
   }

   @Override
   public void retrieveRecords() {
      this.controller.getLogEntries();
   }

   @Override
   protected void displayedEntityNameChanged() {
      this.navigationControl.setDisplayedSubject(this.getObject());
   }

   @Override
   protected String getMementoPathLeaf() {
      return "OperationLog";
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.navigationControl.setSubjectIcon(this.getSubjectIcon());
      this.bindProperties();
      this.layoutPage();
   }

   private void bindProperties() {
      WaitStateBinding var1 = new WaitStateBinding(this.model, "talkingToServer", this);
      var1.syncUi();
   }

   private void layoutPage() {
      JSplitPane var1 = this.createSplitPane();
      this.setDisplayPanel(var1);
   }

   private JSplitPane createSplitPane() {
      return Vertical.splitPane().of(this.entriesPanel(), this.detailsPanel()).withDividerSize(2).withResizeWeight(0.6);
   }

   private UiProvider entriesPanel() {
      JComponent var1 = this.navigationControl.getDisplayer();
      FocusUtils.makeComponentMouseFocusable(var1);
      return Vertical.borderLayout().of(var1, this.logDisplayer);
   }

   private UiProvider detailsPanel() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(this.messageDisplayer);
      DetailsToolBar var2 = new DetailsToolBar(this.messageDisplayer);
      return var1.north(var2.ui);
   }

   @Override
   public void requestFocus() {
      this.logDisplayer.requestFocus();
   }
}
