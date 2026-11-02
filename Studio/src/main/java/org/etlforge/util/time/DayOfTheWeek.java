package org.etlforge.util.time;

public enum DayOfTheWeek {
   Sunday(1),
   Monday(2),
   Tuesday(3),
   Wednesday(4),
   Thursday(5),
   Friday(6),
   Saturday(7);

   private String name;
   private String shortName;

   public String getShortName() {
      return this.shortName;
   }

   @Override
   public String toString() {
      return this.name;
   }

   public String getLocaleName() {
      return this.name;
   }

   DayOfTheWeek(int var3) {
      this.name = DateUtils.getWeekDayName(var3);
      this.shortName = DateUtils.getWeekDayShortName(var3);
   }
}
