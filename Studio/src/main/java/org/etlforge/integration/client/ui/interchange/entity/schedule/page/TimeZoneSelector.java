package org.etlforge.integration.client.ui.interchange.entity.schedule.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.InputFieldLayoutUtils;
import org.etlforge.integration.client.ui.interchange.entity.schedule.resources.ScheduleResources;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.time.CurrentDateTimeDisplayer;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.ui.widget.label.KongaLabel;

final class TimeZoneSelector extends InputPanel {
   private static List<TimeZone> TIME_ZONES;
   private JLabel caption;
   private ComboBoxInputField timeZones;
   private CurrentDateTimeDisplayer dateTimeDisplayer;

   public TimeZoneSelector(TimeZone var1) {
      this.createComponents();
      this.layoutComponents();
      this.setSelectedTimeZone(var1);
      this.setSendInputChangeEvents(true);
      this.setSendUndoEvents(true);
   }

   public void display() {
      this.dateTimeDisplayer.start();
   }

   public void remove() {
      this.dateTimeDisplayer.stop();
   }

   public TimeZone getSelectedTimeZone() {
      TimeZoneSelector.TimeZoneItem var1 = (TimeZoneSelector.TimeZoneItem)this.timeZones.getSelectedItem();
      return var1.getTimeZone();
   }

   public void setSelectedTimeZone(TimeZone var1) {
      if (var1 == null) {
         var1 = TimeZone.getDefault();
      }

      this.timeZones.setSelectedItem(new TimeZoneSelector.TimeZoneItem(var1));
      this.dateTimeDisplayer.setTimeZone(var1);
   }

   public void decorate(UiCanvas var1) {
      var1.display(this.getUi());
   }

   private void createComponents() {
      this.createTimeZoneField();
      this.createDateTimeDisplayer();
      this.createCaption();
   }

   private void createTimeZoneField() {
      this.timeZones = this.createTimeZoneCombo();
      this.addInputField(this.timeZones);
   }

   private void createDateTimeDisplayer() {
      this.dateTimeDisplayer = new CurrentDateTimeDisplayer(2, 3);
   }

   private void createCaption() {
      this.caption = TextStyles.AdminHint.makeLabel(ScheduleResources.getString("Caption.TimeZone"));
   }

   private ComboBoxInputField createTimeZoneCombo() {
      ComboBoxModel var1 = createComboModel();
      KongaComboBox var2 = new KongaComboBox(var1);
      var2.setSelectionReceiver(new Receiver<Object>() {
         @Override
         public void handle(Object var1) {
            TimeZoneSelector.TimeZoneItem var2x = (TimeZoneSelector.TimeZoneItem)var1;
            TimeZoneSelector.this.dateTimeDisplayer.setTimeZone(var2x.getTimeZone());
         }
      });
      String var3 = ScheduleResources.getString("Label.SelectTimeZone");
      return new ComboBoxInputField(var3, var2, new JLabel(var3));
   }

   private static ComboBoxModel createComboModel() {
      ArrayList var0 = new ArrayList();

      for (TimeZone var2 : getAllTimeZones()) {
         var0.add(new TimeZoneSelector.TimeZoneItem(var2));
      }

      Collections.sort(var0);
      return new DefaultComboBoxModel<>(var0.toArray());
   }

   private void layoutComponents() {
      InputPanel var1 = new InputPanel();
      InputField[] var2 = new InputField[]{this.timeZones, this.getDateTimeDisplayerAsInputField()};
      InputFieldLayoutUtils.layoutOneColumn(var1, var2, false, false);
      BorderLayoutBuilder.from(this.getUi(), 0, 5).north(this.caption).center(var1.getUi());
      UiUtils.setOpaqueRecursively(this.getUi(), false);
   }

   private InputField getDateTimeDisplayerAsInputField() {
      String var1 = ScheduleResources.getString("Label.CurrentTimeInSelectedTimeZone");
      TextStyle var2 = TextStyles.InputLabelText;
      KongaLabel var3 = var2.makeLabel(var1);
      this.dateTimeDisplayer.setAppearance(var2.asLabelDecorator(null));
      return new InputField(var1, BorderLayoutBuilder.fillWith(this.dateTimeDisplayer), var3) {
         @Override
         public void makeReadOnly() {
            TimeZoneSelector.this.dateTimeDisplayer.setEditable(false);
         }
      };
   }

   private static synchronized List<TimeZone> getAllTimeZones() {
      if (TIME_ZONES == null) {
         TIME_ZONES = new ArrayList<>(100);

         for (String var3 : TimeZone.getAvailableIDs()) {
            TIME_ZONES.add(TimeZone.getTimeZone(var3));
         }
      }

      return TIME_ZONES;
   }

   private static class TimeZoneItem implements Comparable<TimeZoneSelector.TimeZoneItem> {
      private final TimeZone timeZone;
      private final String stringRep;

      public TimeZoneItem(TimeZone var1) {
         this.timeZone = var1;
         this.stringRep = var1.getDisplayName() + "( " + var1.getID() + " )";
      }

      @Override
      public String toString() {
         return this.stringRep;
      }

      public TimeZone getTimeZone() {
         return this.timeZone;
      }

      public int compareTo(TimeZoneSelector.TimeZoneItem var1) {
         return this.toString().compareTo(var1.toString());
      }

      @Override
      public boolean equals(Object var1) {
         return var1 instanceof TimeZoneSelector.TimeZoneItem && this.timeZone.equals(((TimeZoneSelector.TimeZoneItem)var1).timeZone);
      }

      @Override
      public int hashCode() {
         return this.timeZone.hashCode();
      }
   }
}
