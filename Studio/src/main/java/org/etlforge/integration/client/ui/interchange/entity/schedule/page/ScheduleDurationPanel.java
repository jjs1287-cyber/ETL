package org.etlforge.integration.client.ui.interchange.entity.schedule.page;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.client.ui.interchange.entity.schedule.resources.ScheduleResources;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.schedule.ScheduleDuration;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.DateInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.time.DatePicker;
import org.etlforge.ui.widget.WidgetUtils;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.overlay.OverlayContainer;

final class ScheduleDurationPanel extends InputPanel {
   static final DateFormat DATE_FORMAT = DateFormat.getDateInstance();
   private static final Icon CALENDAR_ICON = ApplicationResources.ICONS.getIcon("Calendar.24");
   private final DateInputField startDateField = this.createStartDateField();
   private final DateInputField endDateField = this.createEndDateField();
   private final JCheckBox endDateActivator = this.createEndDateActivator();
   private final JButton startDateButton = this.createDateButton();
   private final JButton endDateButton = this.createDateButton();

   public ScheduleDurationPanel(Schedule var1) {
      this(var1.getDuration());
   }

   public ScheduleDurationPanel(ScheduleDuration var1) {
      ButtonUtils.addActionListener(new ScheduleDurationPanel.StartEndActionListener(), this.startDateButton, this.endDateButton);
      this.setDuration(var1);
      this.layoutComponents();
      UiUtils.setOpaqueRecursivelySkipDefaults(this.getUi(), false);
   }

   public void setDuration(ScheduleDuration var1) {
      if (var1 == null) {
         this.startDateField.setDate(new Date());
         this.setNoEndDate();
      } else {
         this.startDateField.setDate(var1.getStartDate());
         if (var1.hasEndDate()) {
            this.setDurationEndDate(var1.getEndDate());
         } else {
            this.setNoEndDate();
         }
      }
   }

   public ScheduleDuration getDuration() {
      ScheduleDuration var1 = new ScheduleDuration();
      Date var2 = getDateFromInputField(this.startDateField);
      Date var3 = this.endDateActivator.isSelected() ? getDateFromInputField(this.endDateField) : null;
      var1.setDuration(var2, var3);
      return var1;
   }

   public ScheduleDuration getDuration(Calendar var1, Calendar var2) {
      ScheduleDuration var3 = new ScheduleDuration();
      Date var4 = getPointInTime(this.startDateField, var1).getTime();
      Date var5 = null;
      if (this.endDateActivator.isSelected()) {
         var5 = getPointInTime(this.endDateField, var2).getTime();
      }

      var3.setDuration(var4, var5);
      return var3;
   }

   private DateInputField createStartDateField() {
      JLabel var1 = new JLabel(ScheduleResources.getString("Label.StartingAt") + " ");
      DateInputField var2 = new DateInputField(ScheduleResources.getString("Label.StartDate"), DATE_FORMAT, InputFieldFactory.getTextField(12, true), var1);
      this.configureDateField(var2);
      return var2;
   }

   private DateInputField createEndDateField() {
      JLabel var1 = new JLabel(ScheduleResources.getString("Label.EndingAt") + " ");
      ScheduleDurationPanel.EndDateInputField var2 = new ScheduleDurationPanel.EndDateInputField(
         ScheduleResources.getString("Label.EndDate"), DATE_FORMAT, InputFieldFactory.getTextField(12, true), var1
      );
      this.configureDateField(var2);
      return var2;
   }

   private void configureDateField(DateInputField var1) {
      var1.getInputComponent().setEditable(false);
      var1.getInputComponent().setBackground(javax.swing.UIManager.getColor("Panel.background"));
      this.addInputField(var1, var1, var1);
   }

   private JCheckBox createEndDateActivator() {
      KongaCheckBox var1 = new KongaCheckBox();
      var1.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent var1) {
            ScheduleDurationPanel.this.applyEndDateActivator();
         }
      });
      this.addInputField(new CheckBoxInputField(ScheduleResources.getString("Label.EndDate"), var1));
      return var1;
   }

   private JButton createDateButton() {
      return new KongaButton(CALENDAR_ICON).makeToolbarButton();
   }

   private void layoutComponents() {
      NColumnPanel var1 = new NColumnPanel(3);
      var1.setInsets(3, 5, 10, 10);
      var1.addRow(this.startDateField.getLabel(), this.startDateField.getInputComponent(), this.startDateButton);
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.addAllWithSpace(5, this.endDateActivator, this.endDateField.getLabel());
      var1.addRow(var2, this.endDateField.getInputComponent(), this.endDateButton);
      this.wrap(var1);
   }

   private void setDurationEndDate(Date var1) {
      this.endDateActivator.setSelected(true);
      this.endDateField.setDate(var1);
      this.setEndDateActive(true);
   }

   private void setNoEndDate() {
      this.endDateActivator.setSelected(false);
      this.endDateField.setDate(null);
      this.setEndDateActive(false);
   }

   private static Date getDateFromInputField(DateInputField var0) {
      Date var1 = null;

      try {
         var1 = var0.getDate();
      } catch (ParseException var3) {
      }

      return var1;
   }

   private static Calendar getPointInTime(DateInputField var0, Calendar var1) {
      Date var2 = null;

      try {
         var2 = var0.getDate();
         return appendTimeOfDay(var2, var1);
      } catch (ParseException var4) {
         throw new RuntimeException(var4);
      }
   }

   private static Calendar appendTimeOfDay(Date var0, Calendar var1) {
      Calendar var2 = Calendar.getInstance();
      var2.setTime(var0);
      var2.set(11, var1.get(11));
      var2.set(12, var1.get(12));
      return var2;
   }

   private void setEndDateActive(boolean var1) {
      this.endDateField.setActive(var1);
      this.endDateButton.setEnabled(var1);
   }

   private void applyEndDateActivator() {
      this.setEndDateActive(this.endDateActivator.isSelected());
   }

   private void selectDate(Receiver<Date> var1, Date var2, String var3) {
      OverlayContainer var4 = WidgetUtils.getOverlayContainerAncestor(this.getUi());
      if (var4 != null) {
         DatePicker.selectFromOverlay(var1, var4, var2, null, var3);
      } else {
         Date var5 = DatePicker.selectFromDialog(var2, UiUtils.getActiveWindow(), var3);
         var1.handle(var5);
      }
   }

   private void selectStartDate() {
      Receiver var1 = new Receiver<Date>() {
         public void handle(Date var1) {
            if (var1 != null) {
               ScheduleDurationPanel.this.setNewStartDate(var1);
            }
         }
      };
      this.selectDate(var1, this.startDateField.getValue(), "Select Start Date");
   }

   private void selectEndDate() {
      Receiver var1 = new Receiver<Date>() {
         public void handle(Date var1) {
            if (var1 != null) {
               ScheduleDurationPanel.this.setNewEndDate(var1);
            }
         }
      };
      this.selectDate(var1, this.endDateField.getValue(), "Select End Date");
   }

   private void setNewStartDate(final Date var1) {
      this.startDateField.requestFocus();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ScheduleDurationPanel.this.startDateField.setDate(var1);
            ScheduleDurationPanel.this.endDateField.decorateField();
         }
      });
   }

   private void setNewEndDate(final Date var1) {
      this.endDateField.requestFocus();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ScheduleDurationPanel.this.endDateField.setDate(var1);
         }
      });
   }

   private final class EndDateInputField extends DateInputField {
      public EndDateInputField(String var2, DateFormat var3, JTextComponent var4, JLabel var5) {
         super(var2, var3, var4, var5);
      }

      @Override
      public String getInvalidMessage(String var1) {
         return super.isInputValid(var1) ? this.getName() + " must be later than the start date." : super.getInvalidMessage(var1);
      }

      @Override
      public boolean isInputValid(String var1) {
         if (super.isInputValid(var1)) {
            try {
               return !this.getDate().before(ScheduleDurationPanel.this.startDateField.getDate());
            } catch (ParseException var3) {
            }
         }

         return false;
      }
   }

   private final class StartEndActionListener implements ActionListener {
      private StartEndActionListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         Object var2 = var1.getSource();
         if (var2 == ScheduleDurationPanel.this.endDateActivator) {
            ScheduleDurationPanel.this.applyEndDateActivator();
         } else if (var2 == ScheduleDurationPanel.this.startDateButton) {
            ScheduleDurationPanel.this.selectStartDate();
         } else if (var2 == ScheduleDurationPanel.this.endDateButton) {
            ScheduleDurationPanel.this.selectEndDate();
         }
      }
   }
}

