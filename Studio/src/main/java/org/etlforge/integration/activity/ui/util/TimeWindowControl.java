package org.etlforge.integration.activity.ui.util;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;
import javax.annotation.Nullable;
import javax.swing.JButton;
import javax.swing.JLabel;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.application.ui.widget.TextFieldInput;
import org.etlforge.integration.activity.ui.ActivityViewUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.DateInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.time.DateTimeChooser;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.util.time.DateBuilder;

public final class TimeWindowControl implements UiProvider {
   private static final DateFormat DATE_FORMAT = ActivityViewUtils.getTableDateFormat();
   private DateTimeChooser timeChooser;
   private final InputPanel inputPanel = new InputPanel();
   private final DateInputField startField;
   private final DateInputField endField;
   private final JButton todayButton;
   private final JButton lastDaysButton;
   private final JButton lastHoursButton;
   private final JButton startButton;
   private final JButton endButton;

   public TimeWindowControl() {
      this(null, null);
   }

   public TimeWindowControl(Date var1, Date var2) {
      this.startField = this.createDateField(PackageResources.TimeWindowControl.LABEL_START, var1);
      this.endField = this.createDateField(PackageResources.TimeWindowControl.LABEL_END, var2);
      this.startButton = this.createDateButton();
      this.endButton = this.createDateButton();
      this.todayButton = new JButton("From Today");
      this.lastDaysButton = new JButton("From Last [N] Days");
      this.lastHoursButton = new JButton("From Last [N] Hours");
      ButtonUtils.addActionListener(
         new TimeWindowControl.ButtonListener(), this.startButton, this.endButton, this.todayButton, this.lastDaysButton, this.lastHoursButton
      );
      this.layoutComponents();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.inputPanel.getUi());
   }

   @Override
   public void requestFocus() {
      this.startField.requestFocus();
   }

   private DateInputField createDateField(String var1, Date var2) {
      KongaTextField var3 = new KongaTextField(20);
      var3.setSelectAllWhenFocused(true);
      DateInputField var4 = new DateInputField(var1, DATE_FORMAT, var3, new JLabel(var1));
      var4.setDate(var2);
      return var4;
   }

   private JButton createDateButton() {
      return new KongaButton(PackageResources.TimeWindowControl.CALENDAR_ICON).makeToolbarButton();
   }

   private void layoutComponents() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      var1.withRightAnchor(TwoColumnPanel.SOUTHWEST);
      var1.withLeftWeights(1.0, 0.0).withLeftFill(GridBagLayoutPanel.Fill.HORIZONTAL);
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.addAllWithSpace(5, this.todayButton, this.lastDaysButton, this.lastHoursButton);
      var1.addRow(InvisiblePanel.of(var2));
      var1.addRow(InputFieldFactory.getCapsule(this.startField), this.startButton);
      var1.addRow(InputFieldFactory.getCapsule(this.endField), this.endButton);
      this.inputPanel.wrap(var1);
   }

   public Date getStart() {
      return this.startField.getValue();
   }

   public Date getEnd() {
      return this.endField.getValue();
   }

   private void setTimestamp(DateInputField var1) {
      if (this.timeChooser == null) {
         this.timeChooser = new DateTimeChooser();
      }

      Date var2 = this.timeChooser.chooseDateAndTime(this.inputPanel.getUi());
      if (var2 != null) {
         var1.setDate(var2);
      }
   }

   public void setFieldLabels(String var1, String var2) {
      this.startField.getLabel().setText(var1);
      this.endField.getLabel().setText(var2);
   }

   public void setStartAndEndDates(Date var1, Date var2) {
      this.startField.setDate(var1);
      this.endField.setDate(var2);
   }

   private class ButtonListener implements ActionListener {
      private ButtonListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (var1.getSource() == TimeWindowControl.this.startButton) {
            TimeWindowControl.this.setTimestamp(TimeWindowControl.this.startField);
         } else if (var1.getSource() == TimeWindowControl.this.endButton) {
            TimeWindowControl.this.setTimestamp(TimeWindowControl.this.endField);
         } else if (var1.getSource() == TimeWindowControl.this.todayButton) {
            this.setToday();
         } else if (var1.getSource() == TimeWindowControl.this.lastDaysButton) {
            this.setLastNDays();
         } else if (var1.getSource() == TimeWindowControl.this.lastHoursButton) {
            Window var2 = UiUtils.getWindowAncestor(var1).orNull();
            this.setLastNHours(var2);
         }
      }

      private void setToday() {
         Date var1 = new DateBuilder().midnight().getDate();
         TimeWindowControl.this.setStartAndEndDates(var1, null);
      }

      private void setLastNDays() {
         Integer var1 = this.getDaysBack();
         if (var1 != null) {
            Date var2 = new DateBuilder().addDays(-(var1 - 1)).midnight().getDate();
            TimeWindowControl.this.setStartAndEndDates(var2, null);
         }
      }

      private Integer getDaysBack() {
         return TextFieldInput.askForIntegerInDialog(null, "Number of days back", "Number of days back:", 1, Integer.MAX_VALUE, null);
      }

      private void setLastNHours(@Nullable Window var1) {
         Integer var2 = this.getHoursBack(var1);
         if (var2 != null) {
            Date var3 = new DateBuilder().addHours(-var2).getDate();
            TimeWindowControl.this.setStartAndEndDates(var3, null);
         }
      }

      private Integer getHoursBack(@Nullable Window var1) {
         return TextFieldInput.askForIntegerInDialog(var1, "Number of hours back", "Number of hours back:", 1, Integer.MAX_VALUE, null);
      }
   }
}
