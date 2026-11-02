package org.etlforge.ui.util.time;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;
import org.etlforge.ui.widget.overlay.OverlayPositioner;

public final class DatePicker implements UiProvider {
   private final JComboBox monthCombo;
   private final JSpinner yearSpinner;
   private final DatePicker.DaysPanel daysPanel;
   private final Calendar calendar;
   private final DatePicker.DaysPanelUpdater daysPanelUpdater;
   private final JComponent displayer;
   private int minYear = 1900;
   private int maxYear = 3000;

   public DatePicker() {
      this(new Date());
   }

   public DatePicker(Date var1) {
      this.calendar = Calendar.getInstance();
      this.calendar.setTime(getSafeDate(var1));
      this.daysPanelUpdater = new DatePicker.DaysPanelUpdater();
      this.monthCombo = this.createMonthCombo();
      this.yearSpinner = this.createYearSpinner();
      this.daysPanel = new DatePicker.DaysPanel(this.calendar);
      this.displayer = this.createDisplayer();
   }

   private static Date getSafeDate(Date var0) {
      return var0 != null ? var0 : new Date();
   }

   private JComboBox createMonthCombo() {
      ArrayList var1 = new ArrayList();

      for (String var5 : new DateFormatSymbols().getMonths()) {
         if (var5 != null && var5.trim().length() > 0) {
            var1.add(var5);
         }
      }

      JComboBox var6 = new JComboBox<>(var1.toArray());
      var6.setSelectedIndex(this.calendar.get(2));
      var6.addActionListener(this.daysPanelUpdater);
      return var6;
   }

   private JSpinner createYearSpinner() {
      SpinnerModel var1 = this.createYearSpinnerModel();
      JSpinner var2 = new JSpinner(var1);
      var2.setEditor(new NumberEditor(var2, "#"));
      var2.addChangeListener(this.daysPanelUpdater);
      return var2;
   }

   private JComponent createDisplayer() {
      OneColumnPanel var1 = new OneColumnPanel().withInsets(0, 0, 5, 0);
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.addAllWithSpace(5, this.monthCombo, this.yearSpinner);
      var1.add(var2);
      var1.add(this.daysPanel);
      return var1.deeplyNonOpaque().invisible();
   }

   public static Date selectFromDialog(Date var0, Window var1, String var2) {
      DatePicker var3 = new DatePicker(var0);
      KongaDialog var4 = UiProviderDialogDisplayer.createDialog(var1, var3, var2, ModalityType.APPLICATION_MODAL, KongaDialog.OK_CANCEL, null);
      var4.setVisible(true);
      return var4.wasOkPressed() ? var3.getSelectedDate() : null;
   }

   public static void selectFromOverlay(final Receiver<Date> var0, OverlayContainer var1, Date var2, OverlayPositioner var3, String var4) {
      final DatePicker var5 = new DatePicker(var2);
      var5.displayer.setBorder(Empty.border(10));
      OverlayDecorator var6 = new OverlayDecorator(var5, var4) {
         @Override
         protected void onCancel() {
            var0.handle(null);
         }

         @Override
         protected void onOk() {
            var0.handle(var5.getSelectedDate());
         }
      };
      var6.setBlocking(true);
      var6.useOkAndCancel();
      var6.show(var1, var3 != null ? var3 : new OverlayCenterPositioner());
   }

   public Date getSelectedDate() {
      Calendar var1 = Calendar.getInstance();
      var1.set(1, (Integer)this.yearSpinner.getValue());
      var1.set(2, this.monthCombo.getSelectedIndex());
      var1.set(5, this.daysPanel.getSelectedDay());
      return var1.getTime();
   }

   public void setDate(Date var1) {
      this.calendar.setTime(getSafeDate(var1));
      this.monthCombo.setSelectedIndex(this.calendar.get(2));
      this.yearSpinner.setValue(this.calendar.get(1));
      this.daysPanel.setCalendar(this.calendar);
   }

   public void setAllowedYearRange(int var1, int var2) {
      this.minYear = var1;
      this.maxYear = var2;
      SpinnerModel var3 = this.createYearSpinnerModel();
      this.yearSpinner.setModel(var3);
   }

   private SpinnerModel createYearSpinnerModel() {
      int var1 = this.calendar.get(1);
      return new SpinnerNumberModel(var1, this.minYear, this.maxYear, 1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.displayer);
   }

   @Override
   public void requestFocus() {
      this.yearSpinner.requestFocusInWindow();
   }

   private static class DaysPanel extends JPanel implements ActionListener {
      private Calendar calendar;
      private int day;
      private JLabel[] labels;
      private DatePicker.DaysPanel.DayButton[] buttons;
      private final Calendar currentTime = Calendar.getInstance();

      public DaysPanel(Calendar var1) {
         super(new GridLayout(0, 7, 2, 2));
         this.createLabels();
         this.day = var1.get(5);
         this.setCalendar(var1);
      }

      public int getSelectedDay() {
         return this.day;
      }

      public void setCalendar(Calendar var1) {
         boolean var2 = this.currentTime.get(1) == var1.get(1) && this.currentTime.get(2) == var1.get(2);
         this.calendar = var1;
         this.createButtons(var2);
         this.layoutComponents();
      }

      private void createLabels() {
         this.labels = new JLabel[7];
         String[] var1 = new DateFormatSymbols().getShortWeekdays();

         for (int var2 = 0; var2 < 7; var2++) {
            this.labels[var2] = new JLabel(var1[var2 + 1], 0);
         }
      }

      private void createButtons(boolean var1) {
         int var2 = this.calendar.getActualMaximum(5);
         this.buttons = new DatePicker.DaysPanel.DayButton[var2];

         for (int var3 = 1; var3 <= var2; var3++) {
            DatePicker.DaysPanel.DayButton var4 = new DatePicker.DaysPanel.DayButton(var3);
            var4.addActionListener(this);
            if (var3 == this.day) {
               var4.setSelected(true);
               if (var1) {
                  var4.setForeground(Color.RED);
               }
            }

            this.buttons[var3 - 1] = var4;
         }

         ButtonUtils.makeMutuallyExclusive(this.buttons);
      }

      private void layoutComponents() {
         this.removeAll();

         for (int var1 = 0; var1 < 7; var1++) {
            this.add(this.labels[var1]);
         }

         int var8 = this.calendar.get(5);
         this.calendar.set(5, 1);
         int var2 = this.calendar.get(7) - 1;
         this.calendar.set(5, var8);
         int var3 = 0;

         for (var3 = 0; var3 < var2; var3++) {
            this.add(new JLabel(" "));
         }

         for (DatePicker.DaysPanel.DayButton var7 : this.buttons) {
            var3++;
            this.add(var7);
         }

         while (var3 < 42) {
            this.add(new JLabel(" "));
            var3++;
         }

         this.validate();
         this.repaint();
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.day = ((DatePicker.DaysPanel.DayButton)var1.getSource()).getDay();
      }

      private static class DayButton extends JToggleButton {
         private final int day;

         public DayButton(int var1) {
            super(String.valueOf(var1));
            this.day = var1;
         }

         public int getDay() {
            return this.day;
         }
      }
   }

   private class DaysPanelUpdater implements ActionListener, ChangeListener {
      private DaysPanelUpdater() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.update();
      }

      @Override
      public void stateChanged(ChangeEvent var1) {
         this.update();
      }

      private void update() {
         DatePicker.this.calendar.set(2, DatePicker.this.monthCombo.getSelectedIndex());
         DatePicker.this.calendar.set(1, (Integer)DatePicker.this.yearSpinner.getValue());
         DatePicker.this.daysPanel.setCalendar(DatePicker.this.calendar);
      }
   }
}
