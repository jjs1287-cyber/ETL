package org.etlforge.integration.activity.ui.files;

import org.etlforge.integration.common.server.file.FileCategory;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.property.WaitStateBinding;
import org.etlforge.ui.wait.WaitService;

public final class StatusHistoryView implements UiProvider {
   private final StatusHistoryController controller;
   private final ResultTableWidget table;
   private final ViewOperationFileAction viewSuccessFileAction;
   private final ViewOperationFileAction viewFailureFileAction;
   private final WaitStateBinding waitStateBinding;

   public StatusHistoryView(StatusHistoryModel var1, StatusHistoryController var2) {
      this.controller = var2;
      this.table = new ResultTableWidget(var1);
      this.waitStateBinding = this.bindWaitState(var1);
      this.viewSuccessFileAction = this.createViewFileAction(FileCategory.SFDC_SUCCESS);
      this.viewFailureFileAction = this.createViewFileAction(FileCategory.SFDC_FAILURE);
   }

   private WaitStateBinding bindWaitState(StatusHistoryModel var1) {
      WaitStateBinding var2 = new WaitStateBinding(var1, "busy");
      var2.syncUi();
      return var2;
   }

   private ViewOperationFileAction createViewFileAction(FileCategory var1) {
      ViewOperationFileAction var2 = new ViewOperationFileAction(this.controller, var1);
      var2.setSelectionSource(this.table);
      return var2;
   }

   public void setWaitService(WaitService var1) {
      this.waitStateBinding.setWaitService(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      String var2 = "Select a result to access its success and failure files.";
      UiProvider var3 = this.layoutButtons();
      Vertical.borderLayout(5).of(var2, this.table, var3).decorate(var1);
   }

   private UiProvider layoutButtons() {
      Grid var1 = Horizontal.grid().of(this.viewSuccessFileAction, this.viewFailureFileAction).withHGap(10);
      return Horizontal.Align.east(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }
}
