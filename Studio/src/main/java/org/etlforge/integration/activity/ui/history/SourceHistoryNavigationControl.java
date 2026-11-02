package org.etlforge.integration.activity.ui.history;

import org.etlforge.integration.activity.client.history.SourceHistoryEntry;
import org.etlforge.integration.activity.ui.component.filter.FilteredNavigationControl;

final class SourceHistoryNavigationControl extends FilteredNavigationControl<SourceHistoryEntry> {
   private final SourceHistoryModel model;
   private final SourceHistoryController controller;

   public SourceHistoryNavigationControl(SourceHistoryModel var1, SourceHistoryController var2) {
      this.model = var1;
      this.controller = var2;
      this.setFilters(new SourceHistoryFilters());
   }

   @Override
   public void filtersChanged() {
      this.applyFiltersToModel();
      this.model.filtersChanged();
   }

   public void applyFiltersToModel() {
      SourceHistoryFilters var1 = this.getFilters();
      this.model.setMaxEntriesFilter(var1.getMaxEntriesToGetFilter());
      this.model.setNameFilter(var1.getNameFilter());
      this.model.setTimeWindowFilter(var1.getTimeFilter());
   }

   public SourceHistoryFilters getFilters() {
      return (SourceHistoryFilters)super.getFilters();
   }

   @Override
   public void onRecordsRequested() {
      this.controller.downloadHistory();
   }

   @Override
   public String getTitle() {
      return PackageResources.SOURCE_TITLE;
   }
}
