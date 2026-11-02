package org.etlforge.ui.util.time;

import java.awt.Component;
import java.awt.Insets;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public class DateTimeChooser {
   private DatePicker datePicker;
   private TimeOfTheDayControl timeOfTheDayControl;
   private Date initialDate;

   public DateTimeChooser() {
      this(null);
   }

   public DateTimeChooser(Date var1) {
      this.initialDate = getSafeDate(var1);
   }

   public void setCurrentDate(Date var1) {
      Date var2 = getSafeDate(var1);
      if (this.datePicker == null) {
         this.initialDate = var2;
      } else {
         this.datePicker.setDate(var2);
      }
   }

   public Date chooseDate() {
      return this.chooseDate(null);
   }

   public Date chooseDate(Component var1) {
      if (this.datePicker == null) {
         this.createDatePicker();
      }

      JPanel var2 = InvisiblePanel.of(this.datePicker);
      int var3 = JOptionPane.showOptionDialog(var1, var2, PackageResources.DateTimeChooser.CHOOSE_DATE, 2, -1, null, null, null);
      return var3 == 0 ? this.datePicker.getSelectedDate() : null;
   }

   public Date chooseDateAndTime() {
      return this.chooseDateAndTime(null);
   }

   public Date chooseDateAndTime(Component var1) {
      DateTimeChooser.DateTimeControl var2 = new DateTimeChooser.DateTimeControl();
      int var3 = JOptionPane.showOptionDialog(var1, var2.getDisplay(), PackageResources.DateTimeChooser.CHOOSE_DATE_TIME, 2, -1, null, null, null);
      return var3 == 0 ? var2.getDateAndTime() : null;
   }

   private void createDatePicker() {
      if (this.datePicker == null) {
         this.datePicker = new DatePicker(this.initialDate);
      }
   }

   private void createTimeOfTheDayControl() {
      if (this.timeOfTheDayControl == null) {
         this.timeOfTheDayControl = new TimeOfTheDayControl();
         this.timeOfTheDayControl.setDate(this.initialDate);
      }
   }

   private static Date getSafeDate(Date var0) {
      return var0 != null ? var0 : new Date();
   }

   private class DateTimeControl {
      private OneColumnPanel controls;

      public DateTimeControl() {
         if (DateTimeChooser.this.datePicker == null) {
            DateTimeChooser.this.createDatePicker();
         }

         if (DateTimeChooser.this.timeOfTheDayControl == null) {
            DateTimeChooser.this.createTimeOfTheDayControl();
         }

         this.layoutControls();
         this.setBorder();
      }

      public JComponent getDisplay() {
         JPanel var1 = InvisiblePanel.newPanel();
         this.controls.addToParent(var1, null);
         return var1;
      }

      private void layoutControls() {
         Insets var1 = new Insets(5, 5, 5, 5);
         Insets var2 = new Insets(5, 40, 15, 5);
         this.controls = new OneColumnPanel();
         this.controls.withInsets(var1).add(PackageResources.DateTimeChooser.CHOOSE_DATE + ":");
         this.controls.withInsets(var2).add(DateTimeChooser.this.datePicker);
         this.controls.withInsets(var1).add(PackageResources.DateTimeChooser.CHOOSE_TIME + ":");
         this.controls.withInsets(var2).add(DateTimeChooser.this.timeOfTheDayControl);
         this.controls.deeplyNonOpaque();
      }

      private void setBorder() {
         BorderBuilder var1 = new BorderBuilder().empty(10).etched().empty(10);
         this.controls.withBorder(var1);
      }

      public Date getDateAndTime() {
         Calendar var1 = Calendar.getInstance();
         var1.setTime(DateTimeChooser.this.datePicker.getSelectedDate());
         Calendar var2 = Calendar.getInstance();
         var2.setTime(DateTimeChooser.this.timeOfTheDayControl.getDate());
         var1.set(11, var2.get(11));
         var1.set(12, var2.get(12));
         var1.set(13, var2.get(13));
         return var1.getTime();
      }
   }
}
