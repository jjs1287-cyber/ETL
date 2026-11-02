package org.etlforge.integration.activity.ui.history;

import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.component.filter.FilteredNavigationControl;

final class OperationHistoryNavigationControl extends FilteredNavigationControl<OperationLogEntry> {
   private final OperationHistoryModel model;
   private final OperationHistoryController controller;

   public OperationHistoryNavigationControl(OperationHistoryModel var1, OperationHistoryController var2) {
      this.model = var1;
      this.controller = var2;
      this.setFilters(new OperationHistoryFilters());
   }

   @Override
   public void filtersChanged() {
      this.applyFiltersToModel();
      this.model.filtersChanged();
   }

   public void applyFiltersToModel() {
      OperationHistoryFilters var1 = this.getFilters();
      this.model.setMaxEntriesFilter(var1.getMaxEntriesToGetFilter());
      this.model.setNameFilter(var1.getNameFilter());
      this.model.setTimeWindowFilter(var1.getTimeFilter());
   }

   public OperationHistoryFilters getFilters() {
      return (OperationHistoryFilters)super.getFilters();
   }

   @Override
   public void onRecordsRequested() {
      this.controller.downloadHistory();
   }

   @Override
   public String getTitle() {
      return PackageResources.OPERATION_TITLE;
   }
}
