package org.etlforge.integration.client.ui.interchange.entity.schedule.page.frequency;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.interchange.entity.schedule.resources.ScheduleResources;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.schedule.OccursEvery;
import org.etlforge.integration.data.entity.schedule.ScheduleDailyFrequency;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TimeOfTheDayInputField;
import org.etlforge.ui.input.undo.UndoableButtonGroup;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.time.TimeOfTheDayControl;
import org.etlforge.ui.widget.button.KongaRadioButton;

public class ScheduleDailyFrequencyPanel extends InputPanel {
   private UndoableButtonGroup buttonGroup;
   private JRadioButton occursOnceSelector;
   private JRadioButton occursEverySelector;
   private TimeOfTheDayControl onceAtTimeControl;
   private OccursEveryPanel occursEveryPanel;
   private JLabel captionLabel;

   public ScheduleDailyFrequencyPanel(Schedule var1) {
      this(var1.getDailyFrequency(), var1.getTimeZone());
   }

   public ScheduleDailyFrequencyPanel(ScheduleDailyFrequency var1, TimeZone var2) {
      this.createComponents();
      this.addInputComponents();
      this.layoutComponents();
      this.setDailyFrequency(var1, var2);
      UiUtils.setOpaqueRecursivelySkipDefaults(this.getUi(), false);
   }

   public void setDailyFrequency(ScheduleDailyFrequency var1, TimeZone var2) {
      if (var1 == null) {
         this.setOccursType(1);
      } else if (var1.getFrequencyType() == 1) {
         this.occursOnceSelector.setSelected(true);
         Date var3 = var1.getOccursOnceTime();
         if (var3 != null) {
            this.onceAtTimeControl.setDate(var1.getOccursOnceTime(), var2);
         }
      } else {
         this.occursEverySelector.setSelected(true);
      }

      this.occursEveryPanel.setDailyFrequency(var1, var2);
   }

   public ScheduleDailyFrequency getDailyFrequency(TimeZone var1) {
      ScheduleDailyFrequency var2 = new ScheduleDailyFrequency();
      if (this.getOccursType() == 1) {
         var2.setOccursOnce(this.onceAtTimeControl.getDate(var1));
      } else {
         int var3 = this.occursEveryPanel.getEveryNum();
         OccursEvery var4 = this.occursEveryPanel.getEveryHMS();
         Date var5 = this.occursEveryPanel.getStartTime(var1);
         Date var6 = this.occursEveryPanel.getEndTime(var1);
         var2.setOccursEvery(var3, var4, var5, var6);
      }

      return var2;
   }

   public Calendar getStartTime(TimeZone var1) {
      Calendar var2 = Calendar.getInstance(var1);
      if (this.getOccursType() == 1) {
         var2.setTime(this.onceAtTimeControl.getDate(var1));
      } else {
         var2.setTime(this.occursEveryPanel.getStartTime(var1));
      }

      return var2;
   }

   public Calendar getEndTime(TimeZone var1) {
      Calendar var2 = Calendar.getInstance(var1);
      if (this.getOccursType() == 1) {
         var2.setTime(this.onceAtTimeControl.getDate(var1));
      } else {
         var2.setTime(this.occursEveryPanel.getEndTime(var1));
      }

      return var2;
   }

   private int getOccursType() {
      if (this.occursOnceSelector.isSelected()) {
         return 1;
      } else if (this.occursEverySelector.isSelected()) {
         return 0;
      } else {
         throw new IllegalStateException("No type has been selected.");
      }
   }

   private void createComponents() {
      this.createSelectors();
      this.createOnceAtField();
      this.createOccursEveryPanel();
      this.createCaption();
   }

   private void createSelectors() {
      this.buttonGroup = this.createButtonGroup();
      ScheduleDailyFrequencyPanel.SelectorListener var1 = new ScheduleDailyFrequencyPanel.SelectorListener();
      this.occursOnceSelector = createSelector(ScheduleResources.getString("Label.OccursOnce"), var1, this.buttonGroup);
      this.occursEverySelector = createSelector(ScheduleResources.getString("Label.OccursEvery"), var1, this.buttonGroup);
   }

   private UndoableButtonGroup createButtonGroup() {
      UndoableButtonGroup var1 = new UndoableButtonGroup();
      var1.setUndoRedoPresentationName(ScheduleResources.getString("FrequencyType.Edit"));
      this.addChangeSource(var1);
      return var1;
   }

   private void createOnceAtField() {
      this.onceAtTimeControl = new TimeOfTheDayControl();
   }

   private void createOccursEveryPanel() {
      this.occursEveryPanel = new OccursEveryPanel();
   }

   private void createCaption() {
      String var1 = ScheduleResources.getString("Caption.HowAreTheTimesInterpreted");
      this.captionLabel = TextStyles.AdminHint.makeLabel(var1);
   }

   private static JRadioButton createSelector(String var0, ItemListener var1, ButtonGroup var2) {
      KongaRadioButton var3 = new KongaRadioButton(var0, false);
      var2.add(var3);
      var3.addItemListener(var1);
      return var3;
   }

   private void addInputComponents() {
      this.addInputField(new TimeOfTheDayInputField(ScheduleResources.getString("Frequency.OneAtTime.Name"), this.onceAtTimeControl));
      this.addInputField(this.buttonGroup.createInputField(this.occursOnceSelector, "", null));
      this.addInputField(this.buttonGroup.createInputField(this.occursEverySelector, "", null));
      this.addInputPanel(this.occursEveryPanel);
   }

   private void layoutComponents() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      var1.withAnchor(TwoColumnPanel.WEST);
      var1.addRow(this.captionLabel);
      var1.addRow(this.occursOnceSelector, this.onceAtTimeControl);
      var1.withAnchor(TwoColumnPanel.NORTHWEST);
      var1.addRow(this.occursEverySelector, this.occursEveryPanel.getUi());
      this.wrap(var1);
   }

   private void setOccursType(int var1) {
      switch (var1) {
         case 0:
            this.occursEverySelector.setSelected(true);
            break;
         case 1:
            this.occursOnceSelector.setSelected(true);
            break;
         default:
            throw new IllegalArgumentException("type must be one of ScheduleDailyFrequency.OCCURS_ONCE_AT and ScheduleDailyFrequency.OCCURS_EVERY_X.");
      }
   }

   private class SelectorListener implements ItemListener {
      private SelectorListener() {
      }

      @Override
      public void itemStateChanged(ItemEvent var1) {
         if (ScheduleDailyFrequencyPanel.this.occursOnceSelector.isSelected()) {
            ScheduleDailyFrequencyPanel.this.onceAtTimeControl.setEnabled(true);
            ScheduleDailyFrequencyPanel.this.occursEveryPanel.setActive(false);
         } else if (ScheduleDailyFrequencyPanel.this.occursEverySelector.isSelected()) {
            ScheduleDailyFrequencyPanel.this.onceAtTimeControl.setEnabled(false);
            ScheduleDailyFrequencyPanel.this.occursEveryPanel.setActive(true);
         }
      }
   }
}
