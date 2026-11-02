package org.etlforge.integration.client.ui.interchange.entity.schedule;

import org.etlforge.integration.client.entity.search.AbstractSearchableProperty;
import org.etlforge.integration.client.entity.search.ReplaceableProperty;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.schedule.DailyFrequencyType;
import org.etlforge.integration.data.entity.schedule.ScheduleOccurrenceType;
import org.etlforge.integration.data.entity.schedule.SchedulePriority;

final class ScheduleSearchAndReplaceProperties extends SearchAndReplaceMetaData {
   public ScheduleSearchAndReplaceProperties() {
      super(
         new ScheduleSearchAndReplaceProperties.PriorityProperty(),
         new ScheduleSearchAndReplaceProperties.OccurrenceTypeProperty(),
         new ScheduleSearchAndReplaceProperties.DailyFrequencyTypeProperty()
      );
   }

   private static final class DailyFrequencyTypeProperty extends AbstractSearchableProperty<DailyFrequencyType> {
      public DailyFrequencyTypeProperty() {
         super("Daily frequency type", DailyFrequencyType.class);
      }

      public DailyFrequencyType getValue(IntegrationEntity var1) {
         Schedule var2 = (Schedule)var1;
         return DailyFrequencyType.fromId(var2.getDailyFrequency().getFrequencyType());
      }
   }

   private static final class OccurrenceTypeProperty extends AbstractSearchableProperty<ScheduleOccurrenceType> {
      public OccurrenceTypeProperty() {
         super("Occurrence type", ScheduleOccurrenceType.class);
      }

      public ScheduleOccurrenceType getValue(IntegrationEntity var1) {
         Schedule var2 = (Schedule)var1;
         return ScheduleOccurrenceType.fromId(var2.getOccurrence().getType());
      }
   }

   private static final class PriorityProperty extends AbstractSearchableProperty<SchedulePriority> implements ReplaceableProperty<SchedulePriority> {
      public PriorityProperty() {
         super("Priority", SchedulePriority.class);
      }

      public SchedulePriority getValue(IntegrationEntity var1) {
         return ((Schedule)var1).getPriority();
      }

      public void setValue(IntegrationEntity var1, SchedulePriority var2) {
         ((Schedule)var1).setPriority(var2);
      }
   }
}
