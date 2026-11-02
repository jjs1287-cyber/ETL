package org.etlforge.integration.activity.ui.queue;

import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.component.filter.FilteredNavigationControl;

final class QueueNavigationControl extends FilteredNavigationControl<OperationLogEntry> {
   private final OperationQueueModel model;
   private final OperationQueueController controller;

   public QueueNavigationControl(OperationQueueModel var1, OperationQueueController var2) {
      this.model = var1;
      this.controller = var2;
      this.setFilters(new QueueFilters());
   }

   @Override
   public void filtersChanged() {
      this.applyFiltersToModel();
      this.model.filtersChanged();
   }

   public void applyFiltersToModel() {
      QueueFilters var1 = this.getFilters();
      this.model.setMaxEntriesFilter(var1.getMaxEntriesToGetFilter());
      this.model.setNameFilter(var1.getNameFilter());
      this.model.setStatusFilter(var1.getStatusFilter());
   }

   public QueueFilters getFilters() {
      return (QueueFilters)super.getFilters();
   }

   @Override
   public void onRecordsRequested() {
      this.controller.downloadQueue();
   }

   @Override
   public String getTitle() {
      return PackageResources.TITLE;
   }
}
