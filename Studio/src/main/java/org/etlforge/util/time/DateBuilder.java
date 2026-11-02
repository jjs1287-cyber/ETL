package org.etlforge.util.time;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateBuilder {
   private final Calendar cal = Calendar.getInstance();

   public DateBuilder now() {
      this.cal.setTimeInMillis(System.currentTimeMillis());
      return this;
   }

   public Date getDate() {
      return this.cal.getTime();
   }

   public DateBuilder timezone(TimeZone var1) {
      this.cal.setTimeZone(var1);
      return this;
   }

   public DateBuilder year(int var1) {
      this.cal.set(1, var1);
      return this;
   }

   public DateBuilder month(int var1) {
      this.cal.set(2, var1);
      return this;
   }

   public DateBuilder dayOfMonth(int var1) {
      this.cal.set(5, var1);
      return this;
   }

   public DateBuilder hourOfDay(int var1) {
      this.cal.set(11, var1);
      return this;
   }

   public DateBuilder minute(int var1) {
      this.cal.set(12, var1);
      return this;
   }

   public DateBuilder second(int var1) {
      this.cal.set(13, var1);
      return this;
   }

   public DateBuilder millisecond(int var1) {
      this.cal.set(14, var1);
      return this;
   }

   public DateBuilder timeOfDay(int var1, int var2) {
      return this.hourOfDay(var1).minute(var2);
   }

   public DateBuilder timeOfDay(int var1, int var2, int var3) {
      return this.hourOfDay(var1).minute(var2).second(var3);
   }

   public DateBuilder timeOfDay(int var1, int var2, int var3, int var4) {
      return this.hourOfDay(var1).minute(var2).second(var3).millisecond(var4);
   }

   public DateBuilder january(int var1) {
      return this.month(0).dayOfMonth(var1);
   }

   public DateBuilder february(int var1) {
      return this.month(1).dayOfMonth(var1);
   }

   public DateBuilder march(int var1) {
      return this.month(2).dayOfMonth(var1);
   }

   public DateBuilder april(int var1) {
      return this.month(3).dayOfMonth(var1);
   }

   public DateBuilder may(int var1) {
      return this.month(4).dayOfMonth(var1);
   }

   public DateBuilder june(int var1) {
      return this.month(5).dayOfMonth(var1);
   }

   public DateBuilder july(int var1) {
      return this.month(6).dayOfMonth(var1);
   }

   public DateBuilder august(int var1) {
      return this.month(7).dayOfMonth(var1);
   }

   public DateBuilder september(int var1) {
      return this.month(8).dayOfMonth(var1);
   }

   public DateBuilder october(int var1) {
      return this.month(9).dayOfMonth(var1);
   }

   public DateBuilder november(int var1) {
      return this.month(10).dayOfMonth(var1);
   }

   public DateBuilder december(int var1) {
      return this.month(11).dayOfMonth(var1);
   }

   public DateBuilder today() {
      Calendar var1 = Calendar.getInstance();
      return this.year(var1.get(1)).month(var1.get(2)).dayOfMonth(var1.get(5));
   }

   public DateBuilder midnight() {
      return this.hourOfDay(0).minute(0).second(0).millisecond(0);
   }

   public DateBuilder yesterday() {
      return this.daysFromToday(-1);
   }

   public DateBuilder tomorrow() {
      return this.daysFromToday(1);
   }

   public DateBuilder daysFromToday(int var1) {
      return this.today().addDays(var1);
   }

   public DateBuilder addDays(int var1) {
      this.cal.add(6, var1);
      return this;
   }

   public DateBuilder addHours(int var1) {
      this.cal.add(11, var1);
      return this;
   }

   @Override
   public String toString() {
      return this.cal.getTime().toString();
   }
}
