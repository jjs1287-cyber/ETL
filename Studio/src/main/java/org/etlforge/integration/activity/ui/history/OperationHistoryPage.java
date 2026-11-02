package org.etlforge.integration.activity.ui.history;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JSplitPane;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.CloseEditorAction;
import org.etlforge.application.ui.window.editor.EditorCategory;
import org.etlforge.application.ui.window.editor.EditorIdGenerator;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.serverapi.history.OperationHistoryProvider;
import org.etlforge.integration.activity.serverapi.history.SourceHistoryProvider;
import org.etlforge.integration.activity.ui.component.filter.FilteredNavigationControl;
import org.etlforge.integration.activity.ui.component.page.AbstractActivityPage;
import org.etlforge.integration.activity.ui.log.OperationLogTableColumn;
import org.etlforge.integration.activity.ui.log.SortStatus;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.property.ActionEnabledBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.WaitStateBinding;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.split.KongaSplitPane;

public final class OperationHistoryPage extends AbstractActivityPage {
   private final OperationHistoryModel opHistoryModel;
   private final OperationHistoryController opHistoryController;
   private final OperationHistoryDisplayer opHistoryUi;
   private final SourceHistoryModel sourceHistoryModel;
   private final SourceHistoryController sourceHistoryController;
   private final SourceHistoryTableDisplayer sourceHistoryUi;

   public OperationHistoryPage(ActivitySubject var1, OperationHistoryProvider var2, SourceHistoryProvider var3, boolean var4) {
      super(EditorIdGenerator.get(OperationHistoryPage.class), var1);
      this.opHistoryModel = new OperationHistoryModel(var1);
      this.opHistoryModel.setSortStatus(SortStatus.descending(OperationLogTableColumn.TIME_DONE));
      this.opHistoryController = new OperationHistoryController(this.opHistoryModel, var2);
      this.opHistoryUi = this.createOpHistoryUi();
      this.sourceHistoryModel = new SourceHistoryModel(var1, var4);
      this.sourceHistoryController = new SourceHistoryController(this.sourceHistoryModel, var3);
      this.sourceHistoryUi = this.createSourceHistoryUi();
      this.setIcon(PackageResources.OperationPage.ICON);
      this.setCategory(new EditorCategory("Operation History", this.getSmallIcon()));
      this.addKeyBinding(KeyUtils.F5, new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            OperationHistoryPage.this.retrieveRecords();
         }
      });
   }

   private OperationHistoryDisplayer createOpHistoryUi() {
      OperationHistoryNavigationControl var1 = this.createOperationHistoryNavigation();
      return new OperationHistoryDisplayer(this.opHistoryModel, var1);
   }

   private OperationHistoryNavigationControl createOperationHistoryNavigation() {
      OperationHistoryNavigationControl var1 = new OperationHistoryNavigationControl(this.opHistoryModel, this.opHistoryController);
      this.configureNavigationControl(var1);
      var1.applyFiltersToModel();
      return var1;
   }

   private void configureNavigationControl(FilteredNavigationControl<?> var1) {
      var1.addControl(this.createHelpButton("activity.operation.history"), 2147483646);
      var1.setCloseAction(new CloseEditorAction(this));
      var1.setDisplayedSubject(this.getObject());
   }

   private SourceHistoryTableDisplayer createSourceHistoryUi() {
      SourceHistoryNavigationControl var1 = this.createSourceHistoryNavigation();
      SourceHistoryTableDisplayer var2 = new SourceHistoryTableDisplayer(this.sourceHistoryModel, var1);
      if (this.sourceHistoryModel.isDeleteSupported()) {
         DeleteHistoryAction var3 = new DeleteHistoryAction(this.sourceHistoryController);
         DeleteSourceHistoryControlPanel var4 = new DeleteSourceHistoryControlPanel(var3);
         var2.setDeleteControls(var4);
         ActionEnabledBinding.forBoolean(var3, this.sourceHistoryModel, "deleteEnabled").syncUi();
      }

      var2.syncWithModel();
      return var2;
   }

   private SourceHistoryNavigationControl createSourceHistoryNavigation() {
      SourceHistoryNavigationControl var1 = new SourceHistoryNavigationControl(this.sourceHistoryModel, this.sourceHistoryController);
      this.configureNavigationControl(var1);
      var1.applyFiltersToModel();
      return var1;
   }

   @Override
   public String getTitle() {
      String var1 = this.opHistoryUi.getTitle();
      ActivitySubject var2 = this.getObject();
      if (var2 != null) {
         var1 = var1 + " " + var2.getName();
      } else {
         var1 = PackageResources.OperationPage.getEmptyPageTitle(var1);
      }

      return var1;
   }

   @Override
   public void retrieveRecords() {
      this.opHistoryController.downloadHistory();
      this.sourceHistoryController.downloadHistory();
   }

   @Override
   protected void displayedEntityNameChanged() {
      this.opHistoryUi.setDisplayedSubject(this.getObject());
      this.sourceHistoryUi.setDisplayedSubject(this.getObject());
   }

   @Override
   protected String getMementoPathLeaf() {
      return "OperationHistory";
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.opHistoryUi.setSubjectIcon(this.getSubjectIcon());
      this.sourceHistoryUi.setSubjectIcon(this.getSubjectIcon());
      this.bindProperties();
      this.layoutPage();
   }

   private void bindProperties() {
      Bindings.of(new WaitStateBinding(this.opHistoryModel, "talkingToServer", this), new WaitStateBinding(this.sourceHistoryModel, "talkingToServer", this))
         .syncUi();
   }

   private void layoutPage() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder();
      var1.center(this.createSplit());
      this.setDisplayPanel(var1.container());
   }

   private JSplitPane createSplit() {
      KongaSplitPane var1 = KongaSplitPane.verticalSplit(this.opHistoryUi, this.sourceHistoryUi);
      var1.setResizeWeight(0.5);
      return var1;
   }

   @Override
   public void requestFocus() {
      this.opHistoryUi.requestFocus();
   }
}
