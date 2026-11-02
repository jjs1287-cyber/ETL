package org.etlforge.integration.activity.ui.log;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.integration.activity.client.filter.TimeWindowFilter;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.component.filter.FilteredNavigationControl;

final class OperationLogNavigationControl extends FilteredNavigationControl<OperationLogEntry> {
   private final OperationLogModel model;
   private final OperationLogController controller;
   private final OperationLogNavigationControl.GetNewLogsAction getNewLogsAction;

   public OperationLogNavigationControl(OperationLogModel var1, OperationLogController var2) {
      this.model = var1;
      this.controller = var2;
      this.getNewLogsAction = new OperationLogNavigationControl.GetNewLogsAction();
      this.addControl(this.getNewLogsAction, -1);
      this.setFilters(this.createLogFilters());
   }

   private LogFilters createLogFilters() {
      final LogFilters var1 = new LogFilters();
      var1.addTimeWindowFilterListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent var1x) {
            TimeWindowFilter var2 = var1.getTimeFilter();
            OperationLogNavigationControl.this.getNewLogsAction.update(var2);
         }
      });
      return var1;
   }

   @Override
   public void filtersChanged() {
      this.applyFiltersToModel();
      this.model.filtersChanged();
   }

   public void applyFiltersToModel() {
      LogFilters var1 = this.getFilters();
      this.model.setMaxEntriesFilter(var1.getMaxEntriesToGetFilter());
      this.model.setMessageFilter(var1.getMessageFilter());
      this.model.setNameFilter(var1.getNameFilter());
      this.model.setStatusFilter(var1.getStatusFilter());
      this.model.setTimeWindowFilter(var1.getTimeFilter());
   }

   @Override
   public void onRecordsRequested() {
      this.controller.getLogEntries();
   }

   @Override
   public String getTitle() {
      return PackageResources.TITLE;
   }

   public LogFilters getFilters() {
      return (LogFilters)super.getFilters();
   }

   private final class GetNewLogsAction extends AbstractAction {
      private boolean timeFilterIsSet;

      public GetNewLogsAction() {
         super("Get &New Logs");
         this.putValue("ShortDescription", "Download log entries newer than the newest one currently displayed.");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.timeFilterIsSet) {
            NonBlockingAlert.info(
               "The \"Get New Logs\" feature cannot be used when a time filter has been set.\n\nYou must remove the existing time filter first, by clicking the \"Filter...\" button or by right-clicking on the \"Time Reported\" column header.",
               "Not Applicable"
            );
         } else {
            OperationLogNavigationControl.this.controller.getNewLogEntries();
         }
      }

      public void update(TimeWindowFilter var1) {
         this.timeFilterIsSet = var1.getStartDate() != null || var1.getEndDate() != null;
      }
   }
}
