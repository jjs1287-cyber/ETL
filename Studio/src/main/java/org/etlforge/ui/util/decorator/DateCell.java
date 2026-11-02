package org.etlforge.ui.util.decorator;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import javax.swing.Icon;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.widget.text.TextDecorator;
import org.etlforge.util.text.ThreadSafeDateFormat;
import org.etlforge.util.time.DateUtils;

public final class DateCell implements Comparable<DateCell>, ListDecorator {
   private static final ThreadSafeDateFormat DETAILED_FMT = new ThreadSafeDateFormat(DateFormat.getDateTimeInstance(2, 2));
   private static final ThreadSafeDateFormat DEFAULT_FMT = new ThreadSafeDateFormat(DateFormat.getDateTimeInstance(2, 3));
   private static final ThreadSafeDateFormat DATE_FMT = new ThreadSafeDateFormat(DateFormat.getDateInstance(2));
   private static final ThreadSafeDateFormat TIME_FMT = new ThreadSafeDateFormat(DateFormat.getTimeInstance(3));
   private final Date date;
   private final DateCell.Mode mode;
   private final String detailedDisplay;
   private Icon icon;
   private TextDecorator style;

   public DateCell(Date var1) {
      this(var1, DateCell.Mode.NORMAL);
   }

   public DateCell(Date var1, DateCell.Mode var2) {
      this.mode = var2;
      if (var1 != null) {
         this.detailedDisplay = var2 == DateCell.Mode.DETAILED ? DETAILED_FMT.format(var1) : null;
         this.date = new Date(var1.getTime());
      } else {
         this.detailedDisplay = "";
         this.date = null;
      }
   }

   public final Date getDate() {
      return this.date == null ? null : new Date(this.date.getTime());
   }

   public final int compareTo(DateCell var1) {
      return this.date != null && var1.date != null ? this.date.compareTo(var1.date) : 0;
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof DateCell && equals(this.date, ((DateCell)var1).date);
   }

   @Override
   public int hashCode() {
      return this.date == null ? 0 : this.date.hashCode();
   }

   @Override
   public void decorate(ListDecor var1, boolean var2) {
      String var3 = this.getDisplayString();
      var1.setText(var3);
      var1.setToolTip(this.getToolTip(var3));
      var1.setIcon(this.icon);
      if (this.style != null) {
         var1.setFont(this.style.getFont());
         if (!var2) {
            var1.setTextColor(this.style.getTextColor());
         }
      } else {
         var1.setFont(var1.getListFont());
         if (!var2) {
            var1.setTextColor(var1.getTextColor());
         }
      }
   }

   public void setIcon(Icon var1) {
      this.icon = var1;
   }

   public void setTextStyle(TextDecorator var1) {
      this.style = var1;
   }

   protected String getToolTip(String var1) {
      return this.date == null ? null : DateFormat.getDateTimeInstance().format(this.date);
   }

   public String getDisplayString() {
      if (this.mode == DateCell.Mode.DETAILED) {
         return this.detailedDisplay;
      }

      if (this.date == null) {
         return "";
      }

      if (this.mode == DateCell.Mode.DATE_ONLY) {
         return DATE_FMT.format(this.date);
      }

      if (this.mode == DateCell.Mode.TIME_ONLY) {
         return TIME_FMT.format(this.date);
      }

      if (this.date.before(new Date())) {
         String var1 = null;
         int var2 = DateUtils.getDaysAgo(this.date);
         switch (var2) {
            case 0:
               var1 = UiResourceBundle.getString("Today");
               break;
            case 1:
               var1 = UiResourceBundle.getString("Yesterday");
               break;
            case 2:
               var1 = MessageFormat.format(UiResourceBundle.getString("NDaysAgo"), var2);
         }

         if (var1 != null) {
            return var1 + ", " + TIME_FMT.format(this.date);
         }
      }

      if (DateUtils.isToday(this.date)) {
         return MessageFormat.format("{0}, {1}", UiResourceBundle.getString("Today"), TIME_FMT.format(this.date));
      } else {
         return DateUtils.isYesterday(this.date)
            ? MessageFormat.format("{0}, {1}", UiResourceBundle.getString("Yesterday"), TIME_FMT.format(this.date))
            : DEFAULT_FMT.format(this.date);
      }
   }

   @Override
   public String toString() {
      return this.getDisplayString();
   }

   private static boolean equals(Date var0, Date var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }

   public enum Mode {
      NORMAL,
      TIME_ONLY,
      DATE_ONLY,
      DETAILED;
   }
}
