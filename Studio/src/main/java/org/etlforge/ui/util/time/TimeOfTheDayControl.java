package org.etlforge.ui.util.time;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.UiResources;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.util.time.AM_PM;

public final class TimeOfTheDayControl extends JPanel {
   public static final String DISPLAYED_TIME = "Displayed_Time";
   public static final String FONT_RESOURCE_KEY = TimeOfTheDayControl.class.getName() + "#font";
   public static final int TWELVE_HOUR_MODE = 12;
   public static final int TWENTYFOUR_HOUR_MODE = 24;
   private static final int DEFAULT_MODE;
   private static final NumberFormat FORMAT;
   private static final String[] HOURS_12;
   private static final String[] HOURS_24;
   private static final String[] MINUTES;
   private static final String[] SECONDS;
   private final int mode;
   private final boolean showSeconds;
   private JComboBox hourBox;
   private JComboBox minuteBox;
   private JComboBox secondBox;
   private AM_PM_ComboBox amPmBox;
   private Date previousDate;

   public TimeOfTheDayControl() {
      this(DEFAULT_MODE, false);
   }

   public TimeOfTheDayControl(int var1) {
      this(var1, false);
   }

   public TimeOfTheDayControl(boolean var1) {
      this(DEFAULT_MODE, var1);
   }

   public TimeOfTheDayControl(int var1, boolean var2) {
      if (var1 != 12 && var1 != 24) {
         throw new IllegalArgumentException("mode must be one of TWELVE_HOUR_MODE or TWENTYFOUR_HOUR_MODE.");
      }

      this.mode = var1;
      this.showSeconds = var2;
      this.createComponents();
      this.layoutComponents();
   }

   public void setEditable(boolean var1) {
      this.hourBox.setEnabled(var1);
      this.minuteBox.setEnabled(var1);
      if (this.secondBox != null) {
         this.secondBox.setEnabled(var1);
      }

      if (this.amPmBox != null) {
         this.amPmBox.setEnabled(var1);
      }
   }

   private void createComponents() {
      ActionListener var1 = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            TimeOfTheDayControl.this.handleChange();
         }
      };
      this.hourBox = this.mode == 12 ? new JComboBox<>(HOURS_12) : new JComboBox<>(HOURS_24);
      this.hourBox.setMaximumRowCount(12);
      this.hourBox.addActionListener(var1);
      this.minuteBox = new JComboBox<>(MINUTES);
      this.minuteBox.setMaximumRowCount(12);
      this.minuteBox.addActionListener(var1);
      if (this.showSeconds) {
         this.secondBox = new JComboBox<>(SECONDS);
         this.secondBox.setMaximumRowCount(12);
         this.secondBox.addActionListener(var1);
      }

      if (this.mode == 12) {
         this.amPmBox = new AM_PM_ComboBox();
         this.amPmBox.addActionListener(var1);
      }

      Font var2 = UiResources.getDefault().getFont(FONT_RESOURCE_KEY);
      if (var2 != null) {
         this.hourBox.setFont(var2);
         this.minuteBox.setFont(var2);
         if (this.secondBox != null) {
            this.secondBox.setFont(var2);
         }

         if (this.amPmBox != null) {
            this.amPmBox.setFont(var2);
         }
      }
   }

   private void layoutComponents() {
      BoxBuilder var1 = BoxBuilder.horizontal(this);
      var1.glue().addAllWithSpace(2, this.hourBox, ":", this.minuteBox);
      if (this.showSeconds) {
         var1.strut(2).addAllWithSpace(2, ":", this.secondBox);
      }

      if (this.mode == 12) {
         var1.strut(5).add(this.amPmBox);
      }

      var1.glue();
   }

   public int getHour() {
      switch (this.mode) {
         case 12:
            return this.hourBox.getSelectedIndex() + 1;
         case 24:
            return this.hourBox.getSelectedIndex();
         default:
            return -1;
      }
   }

   public int getHourOfTheDay() {
      if (this.mode == 24) {
         return this.hourBox.getSelectedIndex();
      }

      int var1 = this.hourBox.getSelectedIndex() + 1;
      AM_PM var2 = this.getAM_PM();
      if (var2 == AM_PM.AM && var1 == 12) {
         var1 = 0;
      } else if (var2 == AM_PM.PM && var1 != 12) {
         var1 += 12;
      }

      return var1;
   }

   public int getMinute() {
      return this.minuteBox.getSelectedIndex();
   }

   public int getSecond() {
      return this.secondBox != null ? this.secondBox.getSelectedIndex() : 0;
   }

   public AM_PM getAM_PM() {
      return this.amPmBox != null ? this.amPmBox.getSelectedItem() : null;
   }

   public Date getDate() {
      return this.getDate(TimeZone.getDefault());
   }

   public Date getDate(TimeZone var1) {
      Calendar var2 = Calendar.getInstance(getSafeTimeZone(var1));
      var2.set(11, this.getHourOfTheDay());
      var2.set(12, this.getMinute());
      var2.set(13, this.getSecond());
      return var2.getTime();
   }

   private static TimeZone getSafeTimeZone(TimeZone var0) {
      return var0 != null ? var0 : TimeZone.getDefault();
   }

   public void setMinute(int var1) {
      ArgCheck.checkInRange(var1, 0, 59, "minute");
      this.minuteBox.setSelectedIndex(var1);
   }

   public void setSecond(int var1) {
      if (this.showSeconds) {
         ArgCheck.checkInRange(var1, 0, 59, "second");
         this.secondBox.setSelectedIndex(var1);
      }
   }

   public void setAM_PM(AM_PM var1) {
      if (this.mode == 12) {
         Preconditions.checkNotNull(var1);
         this.amPmBox.setSelectedItem(var1);
      }
   }

   public void setHour(int var1) {
      if (this.mode == 12) {
         ArgCheck.checkInRange(var1, 1, 12, "hour");
         this.hourBox.setSelectedIndex(var1 - 1);
      } else {
         ArgCheck.checkInRange(var1, 0, 23, "hour");
         this.hourBox.setSelectedIndex(var1);
      }
   }

   public void setDate(Date var1) {
      this.setDate(var1, TimeZone.getDefault());
   }

   public void setDate(Date var1, TimeZone var2) {
      Calendar var3 = Calendar.getInstance(getSafeTimeZone(var2));
      var3.setTime(var1);
      int var4 = var3.get(11);
      if (this.mode == 12) {
         if (var4 < 12) {
            this.setAM_PM(AM_PM.AM);
            if (var4 == 0) {
               var4 = 12;
            }
         } else {
            this.setAM_PM(AM_PM.PM);
            if (var4 != 12) {
               var4 -= 12;
            }
         }
      }

      this.setHour(var4);
      this.setMinute(var3.get(12));
      this.setSecond(var3.get(13));
   }

   public void setToStartOfDay() {
      int var1 = this.hourBox.getItemCount();
      if (var1 == 12) {
         this.hourBox.setSelectedIndex(11);
      } else {
         this.hourBox.setSelectedIndex(0);
      }

      this.minuteBox.setSelectedIndex(0);
      if (this.showSeconds) {
         this.secondBox.setSelectedIndex(0);
      }

      if (this.amPmBox != null) {
         this.amPmBox.setSelectedItem(AM_PM.AM);
      }
   }

   public void setToEndOfDay() {
      int var1 = this.hourBox.getItemCount();
      if (var1 == 12) {
         this.hourBox.setSelectedIndex(this.hourBox.getItemCount() - 2);
      } else {
         this.hourBox.setSelectedIndex(this.hourBox.getItemCount() - 1);
      }

      this.minuteBox.setSelectedIndex(this.minuteBox.getItemCount() - 1);
      if (this.showSeconds) {
         this.secondBox.setSelectedIndex(this.secondBox.getItemCount() - 1);
      }

      if (this.amPmBox != null) {
         this.amPmBox.setSelectedItem(AM_PM.PM);
      }
   }

   private void handleChange() {
      Date var1 = this.getDate();
      if (!Objects.equal(var1, this.previousDate)) {
         this.firePropertyChange("Displayed_Time", this.previousDate, var1);
         this.previousDate = var1;
      }
   }

   @Override
   public void setEnabled(boolean var1) {
      super.setEnabled(var1);
      UiUtils.setEnabled(var1, this.hourBox, this.minuteBox, this.secondBox, this.amPmBox);
   }

   private static final String[] createArray(int var0, boolean var1) {
      String[] var2 = new String[var0];
      fill(var2, var1);
      return var2;
   }

   private static final void fill(String[] var0, boolean var1) {
      for (int var2 = 0; var2 < var0.length; var2++) {
         int var3 = var1 ? var2 : var2 + 1;
         var0[var2] = FORMAT.format(var3);
      }
   }

   static {
      SimpleDateFormat var0 = (SimpleDateFormat)DateFormat.getTimeInstance();
      String var1 = var0.toLocalizedPattern();
      if (var1.indexOf("HH:") >= 0) {
         DEFAULT_MODE = 24;
      } else if (var1.indexOf("h:") >= 0) {
         DEFAULT_MODE = 12;
      } else {
         DEFAULT_MODE = 24;
      }

      FORMAT = new DecimalFormat("00");
      HOURS_12 = createArray(12, false);
      HOURS_24 = createArray(24, true);
      MINUTES = createArray(60, true);
      SECONDS = createArray(60, true);
   }
}
