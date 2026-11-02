package org.etlforge.integration.data.entity.schedule;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

public final class ScheduleValidator {
   private final Schedule schedule;
   private final ValidationMessageCollector collector;

   public ScheduleValidator(Schedule var1, ValidationMessageCollector var2) {
      Preconditions.checkNotNull(var1, "schedule");
      Preconditions.checkNotNull(var2, "collector");
      this.schedule = var1;
      this.collector = var2;
   }

   public void run() {
      this.validateOccurrence();
      this.validateDailyFrequency();
      this.validateDuration();
   }

   private void validateOccurrence() {
      if (!this.schedule.getOccurrence().isValid()) {
         this.fatal(ScheduleMessages.getString("ScheduleValidator.InvalidOccurrence"));
      }
   }

   private void validateDailyFrequency() {
      if (!this.schedule.getDailyFrequency().isValid()) {
         this.fatal(ScheduleMessages.getString("ScheduleValidator.InvalidDailyFrequency"));
      }
   }

   private void validateDuration() {
      if (!this.schedule.getDuration().isValid()) {
         this.fatal(ScheduleMessages.getString("ScheduleValidator.InvalidDuration"));
      }
   }

   private void fatal(String var1) {
      this.collector.addMessage(var1, ValidationStatus.INVALID);
   }
}
