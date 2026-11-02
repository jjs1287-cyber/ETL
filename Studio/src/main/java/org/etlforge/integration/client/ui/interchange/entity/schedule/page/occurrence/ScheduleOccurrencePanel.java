package org.etlforge.integration.client.ui.interchange.entity.schedule.page.occurrence;

import java.awt.CardLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.etlforge.integration.client.ui.interchange.entity.schedule.resources.ScheduleResources;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.schedule.ScheduleOccurrence;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.time.DayOfTheWeekCheckBoxPanel;

public class ScheduleOccurrencePanel extends InputPanel {
   private TypeSelectionPanel typeSelectionPanel;
   private JPanel displayPanel;
   private Every_X_Panel every_X_days_panel;
   private InputPanel dailyPanel;
   private Every_X_Panel every_X_weeks_panel;
   private DayOfTheWeekCheckBoxPanel dayOfTheWeekCheckBoxPanel;
   private WeeklyPanel weeklyPanel;
   private Monthly_A_Panel monthly_A_panel;
   private Monthly_B_Panel monthly_B_panel;
   private MonthlyPanel monthlyPanel;

   public ScheduleOccurrencePanel() {
      this((ScheduleOccurrence)null);
   }

   public ScheduleOccurrencePanel(Schedule var1) {
      this(var1.getOccurrence());
   }

   public ScheduleOccurrencePanel(ScheduleOccurrence var1) {
      this.createComponents();
      this.addInputComponents();
      this.layoutComponents();
      this.setOccurrence(var1);
      UiUtils.setOpaqueRecursivelySkipDefaults(this.getUi(), false);
   }

   public void setOccurrence(ScheduleOccurrence var1) {
      if (var1 != null) {
         this.typeSelectionPanel.setType(var1.getType());
         this.every_X_days_panel.setValue(var1.get_Every_X_Days());
         this.every_X_weeks_panel.setValue(var1.get_Every_X_Weeks());
         this.dayOfTheWeekCheckBoxPanel.setSelection(var1.get_Weekly_On_These_Days());
         this.monthly_A_panel.setDay(var1.get_Monthly_Day_In_Month());
         this.monthly_A_panel.setMonth(var1.get_Monthly_of_Every_X_Months());
         this.monthly_B_panel.setDayNum(var1.get_Monthly_B_Num());
         this.monthly_B_panel.setDay(var1.get_Monthly_B_Day_Of_The_Week());
         this.monthly_B_panel.set_Every_X_Month(var1.get_Monthly_B_Every_X_Month());
         this.monthlyPanel.setMonthlyType(var1.getMonthlyType());
      } else {
         this.typeSelectionPanel.setType(1);
         this.setOccurrenceType(1);
         this.every_X_days_panel.clearValue();
         this.every_X_weeks_panel.clearValue();
         this.dayOfTheWeekCheckBoxPanel.clearSelection();
         this.monthly_A_panel.clearDay();
         this.monthly_A_panel.clearMonth();
         this.monthly_B_panel.setDayNum(1);
         this.monthly_B_panel.setDay(null);
         this.monthly_B_panel.clear_Every_X_Month();
         this.monthlyPanel.setMonthlyType(0);
      }
   }

   void setOccurrenceType(int var1) {
      CardLayout var2 = (CardLayout)this.displayPanel.getLayout();
      switch (var1) {
         case 1:
            this.setDailyOccurrence(var2);
            break;
         case 2:
            this.setWeeklyOccurrence(var2);
            break;
         case 3:
            this.setMonthlyOccurrence(var2);
      }
   }

   private void setDailyOccurrence(CardLayout var1) {
      this.dailyPanel.setActive(true);
      this.weeklyPanel.setActive(false);
      this.monthlyPanel.setActive(false);
      var1.show(this.displayPanel, "DAILY");
   }

   private void setWeeklyOccurrence(CardLayout var1) {
      this.dailyPanel.setActive(false);
      this.weeklyPanel.setActive(true);
      this.monthlyPanel.setActive(false);
      var1.show(this.displayPanel, "WEEKLY");
   }

   private void setMonthlyOccurrence(CardLayout var1) {
      this.dailyPanel.setActive(false);
      this.weeklyPanel.setActive(false);
      this.monthlyPanel.setActive(true);
      var1.show(this.displayPanel, "MONTHLY");
   }

   private void createComponents() {
      this.createTypeSelectionPanel();
      this.createInputPanel();
      this.createEvery_X_DaysPanel();
      this.createDailyPanel();
      this.createEvery_X_WeeksPanel();
      this.createDayOfTheWeekCheckBoxPanel();
      this.createWeeklyPanel();
      this.createMonthlyPanel();
   }

   private void createTypeSelectionPanel() {
      this.typeSelectionPanel = new TypeSelectionPanel(this);
   }

   private void createInputPanel() {
      this.displayPanel = new JPanel();
      this.displayPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
      this.displayPanel.setLayout(new CardLayout());
      this.displayPanel.setOpaque(false);
   }

   private void createEvery_X_DaysPanel() {
      this.every_X_days_panel = new Every_X_Panel("Days", ScheduleResources.getString("Label.Every"), ScheduleResources.getString("Label.Days"), 1, 365);
   }

   private void createDailyPanel() {
      this.dailyPanel = new InputPanel();
      this.dailyPanel.setOpaque(false);
   }

   private void createEvery_X_WeeksPanel() {
      this.every_X_weeks_panel = new Every_X_Panel("Weeks", ScheduleResources.getString("Label.Every"), ScheduleResources.getString("Label.Weeks"), 1, 99);
   }

   private void createDayOfTheWeekCheckBoxPanel() {
      this.dayOfTheWeekCheckBoxPanel = new DayOfTheWeekCheckBoxPanel();
      this.dayOfTheWeekCheckBoxPanel.setOpaque(false);
   }

   private void createWeeklyPanel() {
      this.weeklyPanel = new WeeklyPanel(this.every_X_weeks_panel, this.dayOfTheWeekCheckBoxPanel);
   }

   private void createMonthlyPanel() {
      this.createMonthly_A_Panel();
      this.createMonthly_B_Panel();
      this.monthlyPanel = new MonthlyPanel(this.monthly_A_panel, this.monthly_B_panel);
   }

   private void createMonthly_A_Panel() {
      this.monthly_A_panel = new Monthly_A_Panel(ScheduleResources.getString("Label.Day"), ScheduleResources.getString("Label.Month"));
   }

   private void createMonthly_B_Panel() {
      this.monthly_B_panel = new Monthly_B_Panel();
   }

   private void addInputComponents() {
      this.dailyPanel.addInputPanel(this.every_X_days_panel);
      this.addInputPanel(this.typeSelectionPanel);
      this.addInputPanel(this.dailyPanel);
      this.addInputPanel(this.weeklyPanel);
      this.addInputPanel(this.monthlyPanel);
      this.addInputPanel(this.every_X_weeks_panel);
      this.addInputPanel(this.monthly_A_panel);
      this.addInputPanel(this.monthly_B_panel);
   }

   private void layoutComponents() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      var1.withAnchor(TwoColumnPanel.NORTHWEST);
      var1.addRow(this.typeSelectionPanel.getUi(), this.displayPanel);
      this.wrap(var1);
      this.dailyPanel.setLayout(new BoxLayout(this.dailyPanel.getUi(), 3));
      JPanel var2 = new JPanel();
      var2.setOpaque(false);
      var2.setLayout(new BoxLayout(var2, 2));
      var2.add(this.every_X_days_panel.getUi());
      var2.add(Box.createHorizontalGlue());
      this.dailyPanel.add(var2);
      this.dailyPanel.add(Box.createVerticalGlue());
      this.displayPanel.add(this.dailyPanel.getUi(), "DAILY");
      this.displayPanel.add(this.weeklyPanel.getUi(), "WEEKLY");
      this.displayPanel.add(this.monthlyPanel.getUi(), "MONTHLY");
   }

   public ScheduleOccurrence getOccurrence() {
      ScheduleOccurrence var1 = new ScheduleOccurrence();
      int var2 = this.typeSelectionPanel.getSelectedType();
      if (var2 == 1) {
         var1.setDailyOccurrence(this.every_X_days_panel.getValue());
      } else if (var2 == 2) {
         var1.setWeeklyOccurrence(this.every_X_weeks_panel.getValue(), this.dayOfTheWeekCheckBoxPanel.getSelectedDays());
      } else if (var2 == 3) {
         if (this.monthlyPanel.getMonthlyType() == 0) {
            var1.setMonthlyOccurrence(this.monthly_A_panel.getDay(), this.monthly_A_panel.getMonth());
         } else {
            var1.setMonthlyOccurence(this.monthly_B_panel.getDayNum(), this.monthly_B_panel.getDay(), this.monthly_B_panel.get_Every_X_Month());
         }
      }

      return var1;
   }
}
