package org.etlforge.integration.client.ui.interchange.entity.schedule.page.occurrence;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import org.etlforge.integration.client.ui.interchange.entity.schedule.resources.ScheduleResources;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.time.DayOfTheWeekComboBox;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.util.time.DayOfTheWeek;

final class Monthly_B_Panel extends InputPanel {
   public static final String VALUE_CHANGED = "ValueChanged";
   private JComboBox dayNumBox;
   private DayOfTheWeekComboBox weekDayBox;
   private Every_X_Panel every_X_month_panel;
   private JLabel frontLabel;

   public Monthly_B_Panel() {
      this.setOpaque(false);
      this.createComponents();
      this.addInputComponents();
      this.layoutComponents();
   }

   public int getDayNum() {
      return this.dayNumBox.getSelectedIndex() + 1;
   }

   public void setDayNum(int var1) {
      if (var1 > 0 && var1 <= 5) {
         this.dayNumBox.setSelectedIndex(var1 - 1);
      } else {
         this.dayNumBox.setSelectedIndex(0);
      }
   }

   public DayOfTheWeek getDay() {
      return this.weekDayBox.getSelectedItem();
   }

   public void setDay(DayOfTheWeek var1) {
      if (var1 != null) {
         this.weekDayBox.setSelectedItem(var1);
      } else {
         this.weekDayBox.setSelectedItem(DayOfTheWeek.Sunday);
      }
   }

   public int get_Every_X_Month() {
      return this.every_X_month_panel.getValue();
   }

   public void set_Every_X_Month(int var1) {
      this.every_X_month_panel.setValue(var1);
   }

   public void clear_Every_X_Month() {
      this.every_X_month_panel.clearValue();
   }

   private void createComponents() {
      this.createDayNumBox();
      this.createWeekDayBox();
      this.createEvery_X_MonthPanel();
      this.createFrontLabel();
   }

   private void createDayNumBox() {
      Object[] var1 = new Object[]{
         ScheduleResources.getString("1st"),
         ScheduleResources.getString("2nd"),
         ScheduleResources.getString("3rd"),
         ScheduleResources.getString("4th"),
         ScheduleResources.getString("Last")
      };
      this.dayNumBox = new KongaComboBox(var1);
   }

   private void createWeekDayBox() {
      this.weekDayBox = new DayOfTheWeekComboBox();
   }

   private void createEvery_X_MonthPanel() {
      this.every_X_month_panel = new Every_X_Panel(
         ScheduleResources.getString("Label.Month"), ScheduleResources.getString("Label.ofEvery"), ScheduleResources.getString("Label.Month"), 1, 12
      );
   }

   private void createFrontLabel() {
      this.frontLabel = new JLabel(ScheduleResources.getString("The"));
   }

   private void addInputComponents() {
      this.addInputPanel(this.every_X_month_panel);
      this.addInputField(new ComboBoxInputField("", this.dayNumBox));
      this.addInputField(new ComboBoxInputField("", this.weekDayBox));
   }

   private void layoutComponents() {
      BoxBuilder var1 = BoxBuilder.horizontal(this.getUi());
      var1.add(this.frontLabel).addAllWithSpace(5, this.dayNumBox, this.weekDayBox, this.every_X_month_panel.getUi());
   }

   @Override
   public void setActive(boolean var1) {
      super.setActive(var1);
      this.frontLabel.setEnabled(var1);
      this.dayNumBox.setEnabled(var1);
      this.weekDayBox.setEnabled(var1);
   }
}
