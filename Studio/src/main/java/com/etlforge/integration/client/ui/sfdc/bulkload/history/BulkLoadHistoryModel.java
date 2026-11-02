package com.etlforge.integration.client.ui.sfdc.bulkload.history;

import com.google.common.base.Preconditions;
import java.util.List;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstance;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstanceFilter;
import org.etlforge.integration.salesforce.bulkload.backend.RecordOfSentBatch;
import org.etlforge.integration.salesforce.bulkload.backend.TimeWindowFilter;
import org.etlforge.ui.property.BooleanPropertyWaitService;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;

public final class BulkLoadHistoryModel extends AbstractPropertyBasedModel {
   public static final String TALKING_TO_SERVER = "busy";
   public static final String INSTANCES = "instances";
   public static final String BATCHES = "batches";
   public static final String MAX_RECORDS = "maxRecords";
   public static final String TIME_FILTER = "timeFilter";
   public static final String TIME_ZONE = "timeZone";
   private final SalesforceBulkLoad bulkLoad;
   private final Property<Boolean> talkingToServer;
   private final ListProperty<BulkLoadInstance> history;
   private final Property<Integer> maxRecords;
   private final Property<TimeWindowFilter> timeFilter;
   private final Property<TimeZoneMode> timeZone;

   public BulkLoadHistoryModel(SalesforceBulkLoad var1) {
      this.bulkLoad = Preconditions.checkNotNull(var1);
      this.talkingToServer = this.newProperty("busy", Boolean.FALSE);
      this.history = this.newListProperty("instances");
      this.maxRecords = this.newProperty("maxRecords", 100);
      this.timeFilter = this.newProperty("timeFilter", TimeWindowFilter.NONE);
      this.timeZone = this.newProperty("timeZone", TimeZoneMode.LOCAL);
   }

   public SalesforceBulkLoad getBulkLoad() {
      return this.bulkLoad;
   }

   public boolean isTalkingToServer() {
      return this.talkingToServer.get();
   }

   public void setTalkingToServer(boolean var1) {
      this.talkingToServer.set(var1);
   }

   public List<BulkLoadInstance> getInstances() {
      return this.history.get();
   }

   public void setInstances(List<BulkLoadInstance> var1) {
      this.history.set(Preconditions.checkNotNull(var1, "instances"));
   }

   public void setBatches(BulkLoadInstance var1, List<RecordOfSentBatch> var2) {
      Preconditions.checkNotNull(var1, "instance");
      Preconditions.checkNotNull(var2, "batches");
      var1.setBatches(var2);
      this.sendChange("batches", null, var1);
   }

   public void setMaxRecords(int var1) {
      this.maxRecords.set(var1);
   }

   public void setTimeFilter(TimeWindowFilter var1) {
      Preconditions.checkNotNull(var1, "filter");
      this.timeFilter.set(var1);
   }

   public BulkLoadInstanceFilter getInstanceFilter() {
      return new BulkLoadInstanceFilter(this.bulkLoad.getID(), this.maxRecords.get(), this.timeFilter.get());
   }

   public TimeZoneMode getTimeZone() {
      return this.timeZone.get();
   }

   public void setTimeZone(TimeZoneMode var1) {
      Preconditions.checkNotNull(var1, "zone");
      this.timeZone.set(var1);
   }

   public WaitService asWaitService() {
      return new BooleanPropertyWaitService(this.talkingToServer);
   }
}
