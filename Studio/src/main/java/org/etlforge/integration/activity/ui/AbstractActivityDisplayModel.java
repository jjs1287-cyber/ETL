package org.etlforge.integration.activity.ui;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.client.ActivityEntry;
import org.etlforge.integration.activity.client.filter.MaxEntriesToGetFilter;
import org.etlforge.integration.activity.client.filter.OperationNameFilter;
import org.etlforge.integration.activity.client.filter.TimeWindowFilter;
import org.etlforge.integration.activity.serverapi.ActivitySubjectId;
import org.etlforge.integration.activity.ui.log.SortStatus;
import org.etlforge.ui.property.BooleanPropertyWaitService;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public abstract class AbstractActivityDisplayModel<T extends ActivityEntry> extends AbstractPropertyBasedModel {
   public static final String TALKING_TO_SERVER = "talkingToServer";
   public static final String ENTRIES = "entries";
   public static final String SORT_STATUS = "sortStatus";
   private final ActivitySubject subject;
   private final Property<List<T>> allEntries;
   private TimeWindowFilter timeFilter;
   private MaxEntriesToGetFilter maxEntriesFilter;
   private OperationNameFilter nameFilter;
   private final Property<Boolean> talkingToServer;
   private final Property<SortStatus> sortStatus;

   protected AbstractActivityDisplayModel(ActivitySubject var1) {
      this.subject = var1;
      this.allEntries = this.newProperty("entries");
      this.allEntries.set(new ArrayList<>());
      this.allEntries.setAllowsNull(false);
      this.talkingToServer = this.newProperty("talkingToServer", Boolean.FALSE);
      this.timeFilter = new TimeWindowFilter(null, null);
      this.maxEntriesFilter = new MaxEntriesToGetFilter(new MaxLogEntriesToFetchPreference().get());
      this.nameFilter = new OperationNameFilter(null);
      this.sortStatus = this.newProperty("sortStatus", null);
   }

   public final ActivitySubject getSubject() {
      return this.subject;
   }

   public final ActivitySubjectId getSubjectId() {
      return this.subject.getId();
   }

   public final synchronized void setActivityEntries(List<T> var1) {
      this.allEntries.set(Lists.newArrayList(var1));
      this.newEntriesReceived(var1);
   }

   protected void newEntriesReceived(List<T> var1) {
   }

   public final synchronized List<T> getFilteredList() {
      Predicate var1 = this.createCompositeFilter();
      List var2 = KongaListUtils.filter(this.allEntries.get(), var1);
      return this.maxEntriesFilter.getFilteredList(var2);
   }

   protected abstract Predicate<ActivityEntry> createCompositeFilter();

   public final boolean isTalkingToServer() {
      return this.talkingToServer.get();
   }

   public final void setTalkingToServer(boolean var1) {
      this.talkingToServer.set(var1);
   }

   public final WaitService getTalkingToServerWaitService() {
      return new BooleanPropertyWaitService(this.talkingToServer);
   }

   public final TimeWindowFilter getTimeFilter() {
      return this.timeFilter;
   }

   public final void setTimeWindowFilter(TimeWindowFilter var1) {
      Preconditions.checkNotNull(var1, "filter");
      this.timeFilter = var1;
   }

   public final MaxEntriesToGetFilter getMaxEntriesFilter() {
      return this.maxEntriesFilter;
   }

   public final void setMaxEntriesFilter(MaxEntriesToGetFilter var1) {
      Preconditions.checkNotNull(var1, "filter");
      this.maxEntriesFilter = var1;
   }

   public final OperationNameFilter getNameFilter() {
      return this.nameFilter;
   }

   public final void setNameFilter(OperationNameFilter var1) {
      Preconditions.checkNotNull(var1, "filter");
      this.nameFilter = var1;
   }

   public final SortStatus getSortStatus() {
      return this.sortStatus.get();
   }

   public final void setSortStatus(SortStatus var1) {
      this.sortStatus.set(var1);
   }

   public final void filtersChanged() {
      this.sendChange("entries", null, null);
   }
}
