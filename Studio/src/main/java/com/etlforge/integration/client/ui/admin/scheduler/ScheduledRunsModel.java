package com.etlforge.integration.client.ui.admin.scheduler;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.client.scheduler.ScheduledRun;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class ScheduledRunsModel extends AbstractPropertyBasedModel {
   public static final String DAY = "day";
   public static final String SCHEDULED_RUNS = "scheduledRuns";
   public static final String IS_DOWNLOADING = "isDownloading";
   public static final String EMPTY = "empty";
   private final DateFormat dateFormat = DateFormat.getDateInstance();
   private final Property<Date> day = this.newProperty("day");
   private final Property<List<ScheduledRun>> scheduledRuns = this.newProperty("scheduledRuns");
   private final Property<Boolean> downloading = this.newProperty("isDownloading", Boolean.FALSE);
   private final Property<Boolean> empty = this.newProperty("empty", Boolean.FALSE);
   private final Map<String, List<ScheduledRun>> cache = Maps.newHashMap();

   public void setDownloading(boolean var1) {
      this.empty.set(false);
      this.downloading.set(var1);
   }

   public Date getDay() {
      Date var1 = this.day.get();
      return var1 != null ? new Date(var1.getTime()) : null;
   }

   public void setDay(Date var1) {
      Date var2 = this.getDay();
      this.day.set(var1 != null ? new Date(var1.getTime()) : null);
      if (!Objects.equal(var2, var1)) {
         List var3 = this.cache.get(this.dateFormat.format(var1));
         if (var3 == null) {
            this.scheduledRuns.set(new ArrayList<>());
            this.empty.set(false);
         } else {
            this.scheduledRuns.set(var3);
            this.empty.set(var3.isEmpty());
         }
      }
   }

   public void setDayAndScheduledRuns(Date var1, List<ScheduledRun> var2) {
      this.day.set(new Date(var1.getTime()));
      ArrayList var3 = Lists.newArrayList(var2);
      Collections.sort(var3, new Comparator<ScheduledRun>() {
         public int compare(ScheduledRun var1, ScheduledRun var2x) {
            return var1.getTimeOfRun().compareTo(var2x.getTimeOfRun());
         }
      });
      this.scheduledRuns.set(var3);
      this.empty.set(var2.isEmpty());
      this.cache.put(this.dateFormat.format(var1), var3);
   }

   public boolean isCached(Date var1) {
      return var1 != null && this.cache.containsKey(this.dateFormat.format(var1));
   }
}
