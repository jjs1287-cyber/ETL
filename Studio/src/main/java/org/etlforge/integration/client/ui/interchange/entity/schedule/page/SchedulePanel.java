package org.etlforge.integration.client.ui.interchange.entity.schedule.page;

import com.google.common.collect.ImmutableList;
import java.util.TimeZone;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanel;
import org.etlforge.integration.client.ui.interchange.entity.schedule.page.frequency.ScheduleDailyFrequencyPanel;
import org.etlforge.integration.client.ui.interchange.entity.schedule.page.occurrence.ScheduleOccurrencePanel;
import org.etlforge.integration.client.ui.interchange.entity.schedule.resources.ScheduleResources;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.schedule.SchedulePriority;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.DropDownPanel;

public final class SchedulePanel extends IntegrationEntityPanel {
   private final ComboBoxInputField prioritySelector;
   private final ScheduleOccurrencePanel occurrencePanel;
   private final ScheduleDailyFrequencyPanel dailyFrequencyPanel;
   private final ScheduleDurationPanel durationPanel;

   public SchedulePanel(ApplicationWindow var1, Schedule var2) {
      super(var1, var2);
      this.prioritySelector = this.createPriorityField(var2);
      this.occurrencePanel = new ScheduleOccurrencePanel(var2);
      this.dailyFrequencyPanel = new ScheduleDailyFrequencyPanel(var2);
      this.durationPanel = new ScheduleDurationPanel(var2);
      this.addInputFields();
   }

   private ComboBoxInputField createPriorityField(Schedule var1) {
      String var2 = ScheduleResources.getString("Label.Priority");
      JComboBox var3 = new JComboBox<>(SchedulePriority.values());
      var3.setSelectedItem(var1.getPriority());
      return new ComboBoxInputField(var2, var3, new JLabel(var2));
   }

   public ImmutableList<BoundProperty> getBoundProperties() {
      BoundProperty var1 = BoundPropertyFactory.comboProperty("priority", this.prioritySelector);
      return ImmutableList.of(var1);
   }

   @Override
   public void display() {
      super.display();
      this.layoutComponents();
      UiUtils.setOpaqueRecursivelySkipDefaults(this.getUi(), false);
   }

   private void layoutComponents() {
      IntegrationDataPanel.Layout var1 = this.getDefaultLayout();
      var1.addToTopGrid(this.prioritySelector);
      this.layoutOccurencePanel(var1);
      this.layoutFrequencyPanel(var1);
      this.layoutDurationPanel(var1);
      var1.glue();
   }

   private void layoutOccurencePanel(IntegrationDataPanel.Layout var1) {
      String var2 = ScheduleResources.getString("Label.Occurrence");
      this.layoutDropDownPanel(var1, this.occurrencePanel.getUi(), var2, true);
   }

   private void layoutFrequencyPanel(IntegrationDataPanel.Layout var1) {
      String var2 = ScheduleResources.getString("Label.DailyFrequency");
      this.layoutDropDownPanel(var1, this.dailyFrequencyPanel.getUi(), var2, false);
   }

   private void layoutDurationPanel(IntegrationDataPanel.Layout var1) {
      String var2 = ScheduleResources.getString("Label.Duration");
      this.layoutDropDownPanel(var1, this.durationPanel.getUi(), var2, false);
   }

   private void layoutDropDownPanel(IntegrationDataPanel.Layout var1, JComponent var2, String var3, boolean var4) {
      DropDownPanel var5 = new DropDownPanel(var2, var3, false);
      if (var4) {
         var5.expand();
      }

      JComponent var6 = var5.getDisplayer();
      var1.add(var6);
   }

   private void addInputFields() {
      this.addInputField(this.prioritySelector);
      this.addInputPanel(this.occurrencePanel);
      this.addInputPanel(this.dailyFrequencyPanel);
      this.addInputPanel(this.durationPanel);
   }

   @Override
   protected void applyOtherPropertiesThanTheName() {
      this.applyThePriority();
      this.applyTheOccurrence();
      TimeZone var1 = TimeZone.getDefault();
      this.applyTheDailyFrequency(var1);
      this.applyTimeZoneAndDuration(var1);
   }

   private void applyThePriority() {
      this.getDisplayedObject().setPriority((SchedulePriority)this.prioritySelector.getSelectedItem());
   }

   private void applyTheOccurrence() {
      this.getDisplayedObject().setOccurrence(this.occurrencePanel.getOccurrence());
   }

   private void applyTheDailyFrequency(TimeZone var1) {
      this.getDisplayedObject().setDailyFrequency(this.dailyFrequencyPanel.getDailyFrequency(var1));
   }

   private void applyTimeZoneAndDuration(TimeZone var1) {
      this.getDisplayedObject().setTimeZone(var1);
      this.getDisplayedObject().setDuration(this.durationPanel.getDuration());
   }

   public Schedule getDisplayedObject() {
      return (Schedule)super.getDisplayedObject();
   }
}
