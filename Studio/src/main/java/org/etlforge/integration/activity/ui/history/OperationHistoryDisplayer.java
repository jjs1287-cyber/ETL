package org.etlforge.integration.activity.ui.history;

import javax.swing.Icon;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.ui.component.filter.FilterDescriptionPanel;
import org.etlforge.integration.activity.ui.log.OperationLogDisplayer;
import org.etlforge.integration.activity.ui.log.OperationLogTableColumn;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;

final class OperationHistoryDisplayer implements UiProvider {
   private final OperationHistoryNavigationControl toolbar;
   private final FilterDescriptionPanel filterDescriptionPanel;
   private final OperationLogDisplayer displayer;

   public OperationHistoryDisplayer(OperationHistoryModel var1, OperationHistoryNavigationControl var2) {
      this.toolbar = var2;
      this.filterDescriptionPanel = new FilterDescriptionPanel(var2.getFilters());
      this.displayer = new OperationLogDisplayer(var1, this.filterDescriptionPanel, getColumnsToInclude());
   }

   private static OperationLogTableColumn[] getColumnsToInclude() {
      return new OperationLogTableColumn[]{
         OperationLogTableColumn.OPERATION,
         OperationLogTableColumn.TIME_STARTED,
         OperationLogTableColumn.TIME_DONE,
         OperationLogTableColumn.DURATION,
         OperationLogTableColumn.SOURCE_RECORDS,
         OperationLogTableColumn.TARGET_RECORDS
      };
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.northToSouth(this.toolbar.getDisplayer(), this.displayer, this.filterDescriptionPanel);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.displayer.requestFocus();
   }

   public String getTitle() {
      return this.toolbar.getTitle();
   }

   public void setDisplayedSubject(ActivitySubject var1) {
      this.toolbar.setDisplayedSubject(var1);
   }

   public void setSubjectIcon(Icon var1) {
      this.toolbar.setSubjectIcon(var1);
   }
}
