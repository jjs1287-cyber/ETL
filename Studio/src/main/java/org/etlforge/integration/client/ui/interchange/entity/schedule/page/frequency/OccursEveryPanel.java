package org.etlforge.integration.client.ui.interchange.entity.schedule.page.frequency;

import com.google.common.collect.Maps;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import java.util.EnumMap;
import java.util.TimeZone;
import javax.swing.JLabel;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.client.ui.interchange.entity.schedule.resources.ScheduleResources;
import org.etlforge.integration.data.entity.schedule.OccursEvery;
import org.etlforge.integration.data.entity.schedule.ScheduleDailyFrequency;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.IntegerInputField;
import org.etlforge.ui.input.TimeOfTheDayInputField;
import org.etlforge.ui.layout.OneRowPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.time.TimeOfTheDayControl;
import org.etlforge.ui.widget.combobox.EnumComboBox;

class OccursEveryPanel extends InputPanel {
   static final String OCCURS_EVERY = "Occurs_Every";
   private final IntegerInputField everyNumField;
   private final EnumComboBox<OccursEvery> everyHMSBox;
   private final EnumMap<OccursEvery, String> hmsLabels = createOccursEveryDisplayStrings();
   private final TimeOfTheDayControl startTimeSelector;
   private final TimeOfTheDayControl endTimeSelector;

   public OccursEveryPanel() {
      this.everyNumField = InputFieldFactory.getIntegerInputField(this.hmsLabels.get(OccursEvery.MINUTE), "", 1, OccursEvery.MINUTE.getMaxAllowedValue(), 4);
      this.everyNumField.setSelectAllWhenFocused(true);
      this.everyHMSBox = new EnumComboBox<>(OccursEvery.class);
      this.everyHMSBox.setDisplayStrings(this.hmsLabels);
      this.everyHMSBox.addItemListener(new OccursEveryPanel.EveryHMSListener());
      this.everyHMSBox.setSelectedItem(OccursEvery.MINUTE);
      this.startTimeSelector = new TimeOfTheDayControl();
      this.endTimeSelector = new TimeOfTheDayControl();
      this.resetStartTime();
      this.resetEndTime();
      this.addInputFields();
      this.layoutComponents();
   }

   private void resetStartTime() {
      this.startTimeSelector.setToStartOfDay();
   }

   private void resetEndTime() {
      this.endTimeSelector.setToEndOfDay();
   }

   private static EnumMap<OccursEvery, String> createOccursEveryDisplayStrings() {
      EnumMap var0 = Maps.newEnumMap(OccursEvery.class);
      var0.put(OccursEvery.SECOND, ScheduleResources.getString("Label.Seconds"));
      var0.put(OccursEvery.MINUTE, ScheduleResources.getString("Label.Minutes"));
      var0.put(OccursEvery.HOUR, ScheduleResources.getString("Label.Hours"));
      return var0;
   }

   private void addInputFields() {
      this.addInputField(this.everyNumField, this.everyNumField, this.everyNumField);
      this.addInputField(new ComboBoxInputField(ScheduleResources.getString("Frequency.TimeUnit.Name"), this.everyHMSBox));
      this.addInputField(new TimeOfTheDayInputField(ScheduleResources.getString("Frequency.StartTime.Name"), this.startTimeSelector));
      this.addInputField(new TimeOfTheDayInputField(ScheduleResources.getString("Frequency.EndTime.Name"), this.endTimeSelector));
   }

   private void layoutComponents() {
      OneRowPanel var1 = new OneRowPanel();
      var1.withInsets(3, 5, 10, 10);
      var1.add(InputFieldFactory.getCapsule(this.everyNumField));
      var1.add(this.everyHMSBox);
      var1.add(this.getTimePanel());
      this.wrap(var1);
   }

   private TwoColumnPanel getTimePanel() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      var1.withAnchor(TwoColumnPanel.NORTHWEST);
      var1.addRow(new JLabel(ScheduleResources.getString("Label.StartingAt")), this.startTimeSelector);
      var1.addRow(new JLabel(ScheduleResources.getString("Label.EndingAt")), this.endTimeSelector);
      return var1;
   }

   public int getEveryNum() {
      return this.everyNumField.getIntValue(0);
   }

   public void setEveryNum(int var1) {
      this.everyNumField.setValue(var1);
   }

   public OccursEvery getEveryHMS() {
      return this.everyHMSBox.getSelectedItem();
   }

   public void setEveryHMS(OccursEvery var1) {
      this.everyHMSBox.setSelectedItem(var1);
   }

   public Date getStartTime(TimeZone var1) {
      return this.startTimeSelector.getDate(var1);
   }

   public void setStartTime(Date var1, TimeZone var2) {
      this.startTimeSelector.setDate(var1, var2);
   }

   public Date getEndTime(TimeZone var1) {
      return this.endTimeSelector.getDate(var1);
   }

   public void setEndTime(Date var1, TimeZone var2) {
      this.endTimeSelector.setDate(var1, var2);
   }

   public void setDailyFrequency(ScheduleDailyFrequency var1, TimeZone var2) {
      if (var1 == null) {
         this.everyNumField.setValue("");
         this.everyHMSBox.setSelectedItem(OccursEvery.MINUTE);
         this.resetStartTime();
         this.resetEndTime();
      } else {
         this.setEveryNum(var1.get_Every_Num());
         this.setEveryHMS(var1.get_Every_HMS());
         Date var3 = var1.getStartTime();
         if (var3 != null) {
            this.setStartTime(var1.getStartTime(), var2);
         }

         Date var4 = var1.getEndTime();
         if (var4 != null) {
            this.setEndTime(var1.getEndTime(), var2);
         }
      }
   }

   private class EveryHMSListener implements ItemListener {
      private EveryHMSListener() {
      }

      @Override
      public void itemStateChanged(ItemEvent var1) {
         if (var1.getStateChange() == 1) {
            OccursEvery var2 = OccursEveryPanel.this.everyHMSBox.getSelectedItem();
            OccursEveryPanel.this.everyNumField.setName(OccursEveryPanel.this.hmsLabels.get(var2));
            OccursEveryPanel.this.everyNumField.setMaximumValue(var2.getMaxAllowedValue());
            OccursEveryPanel.this.decorateFields();
         }
      }
   }
}
