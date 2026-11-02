package org.etlforge.integration.data.entity;

import java.util.TimeZone;
import org.etlforge.application.VersionFactory;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.id.ScheduleId;
import org.etlforge.integration.data.entity.schedule.ScheduleDailyFrequency;
import org.etlforge.integration.data.entity.schedule.ScheduleDuration;
import org.etlforge.integration.data.entity.schedule.ScheduleOccurrence;
import org.etlforge.integration.data.entity.schedule.SchedulePriority;
import org.etlforge.integration.data.entity.schedule.ScheduleValidator;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.system.EnumWithIdLookup;
import org.etlforge.util.version.Version;

public final class Schedule extends IntegrationEntity {
   @IdProperty
   public static final String SCHEDULE_ID = "schedule_id";
   @NameProperty
   public static final String SCHEDULE_NAME = "schedule_name";
   public static final String OCCURRENCE_PROPERTY = "occurrence";
   public static final String FREQUENCY_PROPERTY = "frequency";
   public static final String DURATION_PROPERTY = "duration";
   public static final String PRIORITY_PROPERTY = "priority";
   public static final String TIMEZONE_ID = "timezone_id";
   public static final String ENABLED = "enabled";
   private static final Version VERSION_WITH_SECONDS_RESOLUTION = VersionFactory.newVersion(3, 1, 3, 8);
   private static final String[] WEEKDAY_PROPERTIES = ScheduleOccurrence.getWeekDayProperties();
   private static String[] PROPERTIES = new String[]{
      "schedule_id",
      "schedule_name",
      "occurs",
      "daily",
      "weekly",
      WEEKDAY_PROPERTIES[1],
      WEEKDAY_PROPERTIES[2],
      WEEKDAY_PROPERTIES[3],
      WEEKDAY_PROPERTIES[4],
      WEEKDAY_PROPERTIES[5],
      WEEKDAY_PROPERTIES[6],
      WEEKDAY_PROPERTIES[0],
      "monthly",
      "monthly_a_day",
      "monthly_a_of_every_x_month",
      "monthly_b_num",
      "monthly_b_day",
      "monthly_b_of_every_x_month",
      "frequency",
      "frequency_once_time",
      "frequency_every_num",
      "frequency_every_hm",
      "frequency_start_time",
      "frequency_end_time",
      "duration_start_date",
      "duration_end_date",
      "duration_yesno_end",
      "priority"
   };

   public Schedule(String var1) {
      this(null, var1);
   }

   public Schedule(ScheduleId var1, String var2) {
      super(EntityType.Schedule, var1, var2);
   }

   @Override
   public String[] getPropertyKeys() {
      return (String[])PROPERTIES.clone();
   }

   public ScheduleId getID() {
      return (ScheduleId)super.getID();
   }

   public SchedulePriority getPriority() {
      SchedulePriority var1 = SchedulePriority.Medium;
      int var2 = this.getIntProperty("priority", var1.getId());
      return EnumWithIdLookup.lookupOr(var2, var1);
   }

   public void setPriority(SchedulePriority var1) {
      SchedulePriority var2 = this.getPriority();
      this.setProperty("priority", String.valueOf(var1.getId()));
      if (var1 != var2) {
         this.firePropertyChange("priority", var2, var1);
      }
   }

   public ScheduleOccurrence getOccurrence() {
      return new ScheduleOccurrence(this.getData());
   }

   public void setOccurrence(ScheduleOccurrence var1) {
      ScheduleOccurrence var2 = this.getOccurrence();
      var1.appendToData(this.getData());
      if (!var2.equals(var1)) {
         this.firePropertyChange("occurrence", var2, var1);
      }
   }

   public ScheduleDailyFrequency getDailyFrequency() {
      return new ScheduleDailyFrequency(this.getData());
   }

   public void setDailyFrequency(ScheduleDailyFrequency var1) {
      ScheduleDailyFrequency var2 = this.getDailyFrequency();
      var1.appendToData(this.getData());
      if (!var2.equals(var1)) {
         this.firePropertyChange("frequency", var2, var1);
      }
   }

   public ScheduleDuration getDuration() {
      return new ScheduleDuration(this.getData());
   }

   public void setDuration(ScheduleDuration var1) {
      ScheduleDuration var2 = this.getDuration();
      var1.appendToData(this.getData());
      if (!var2.equals(var1)) {
         this.firePropertyChange("duration", var2, var1);
      }
   }

   public TimeZone getTimeZone() {
      String var1 = this.getProperty("timezone_id", "");
      return var1.length() == 0 ? TimeZone.getDefault() : TimeZone.getTimeZone(var1);
   }

   public void setTimeZone(TimeZone var1) {
      if (var1 == null) {
         var1 = TimeZone.getDefault();
      }

      String var2 = var1.getID();
      String var3 = this.getProperty("timezone_id");
      this.setProperty("timezone_id", var2);
      if (!var2.equals(var3)) {
         this.firePropertyChange("timezone_id", var3, var2);
      }
   }

   public boolean isEnabled() {
      return this.getBooleanProperty("enabled", true);
   }

   public void setEnabled(boolean var1) {
      this.setBooleanProperty("enabled", var1, true);
   }

   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      new ScheduleValidator(this, var1).run();
   }

   @Override
   public StringKeyValuePairs getCopyOfKeyValues() {
      StringKeyValuePairs var1 = super.getCopyOfKeyValues();
      StringKeyValuePairs var2 = new StringKeyValuePairs();
      var2.putAll(var1);
      var2.put("schedule_id", this.getID().toString());
      var2.put("schedule_name", this.getName());
      return var2;
   }

   @Override
   public Version getRequiredVersion() {
      return this.isUsingSubSecondResolution() ? VERSION_WITH_SECONDS_RESOLUTION : super.getRequiredVersion();
   }

   private boolean isUsingSubSecondResolution() {
      synchronized (this.getDataLock()) {
         return ScheduleDailyFrequency.isDefiningSubMinuteResolution(this.getData());
      }
   }
}
