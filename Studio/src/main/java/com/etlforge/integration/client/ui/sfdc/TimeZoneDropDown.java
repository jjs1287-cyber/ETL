package com.etlforge.integration.client.ui.sfdc;

import com.google.common.collect.Maps;
import java.awt.Cursor;
import java.awt.event.FocusListener;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.salesforce.bulkload.TimeZoneId;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.util.time.TimeZones;

final class TimeZoneDropDown implements UiProvider, TimeZoneSelectionWidget {
   private final KongaComboBox dropdown = this.createDropdown();
   private boolean ready;
   private TimeZoneId idToSelectWhenReady;

   public TimeZoneDropDown() {
      this.populateComboBox();
   }

   private KongaComboBox createDropdown() {
      KongaComboBox var1 = new KongaComboBox();
      var1.getModel().setSelectedItem("Fetching list of available Time Zones...");
      TimeZoneListAppearance var2 = new TimeZoneListAppearance();
      var2.installIn(var1);
      var1.setCursor(Cursor.getPredefinedCursor(3));
      return var1;
   }

   private void populateComboBox() {
      Computation var1 = Computation.create(new TimeZoneDropDown.ComboBoxModelProducer(), new TimeZoneDropDown.ComboBoxModelConsumer());
      var1.swingWorkerBuilder().buildAndExecute();
   }

   @Override
   public void setSelectedTimeZoneId(TimeZoneId var1) {
      if (this.ready) {
         this.dropdown.setSelectedItem(var1.getTimeZone());
      } else {
         this.idToSelectWhenReady = var1;
      }
   }

   @Override
   public TimeZoneId getSelectedTimeZoneId() {
      if (this.ready) {
         Object var1 = this.dropdown.getSelectedItem();
         if (var1 instanceof TimeZone) {
            return TimeZoneId.forTimeZone((TimeZone)var1);
         }
      } else if (this.idToSelectWhenReady != null) {
         return this.idToSelectWhenReady;
      }

      return TimeZoneId.SERVER_LOCAL;
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.dropdown);
   }

   @Override
   public void requestFocus() {
      this.dropdown.requestFocusInWindow();
   }

   @Override
   public void addFocusListener(FocusListener var1) {
      this.dropdown.addFocusListener(var1);
   }

   @Override
   public void setEnabled(boolean var1) {
      this.dropdown.setEnabled(var1);
   }

   @Override
   public void registerWithParent(InputPanel var1) {
      var1.addInputField(new ComboBoxInputField("Time Zone", this.dropdown));
   }

   private class ComboBoxModelConsumer implements Receiver<ComboBoxModel> {
      private ComboBoxModelConsumer() {
      }

      public void handle(ComboBoxModel var1) {
         TimeZoneDropDown.this.dropdown.setModel(var1);
         if (TimeZoneDropDown.this.idToSelectWhenReady != null) {
            this.select(TimeZoneDropDown.this.idToSelectWhenReady.getTimeZone());
            TimeZoneDropDown.this.idToSelectWhenReady = null;
         } else {
            this.select(TimeZone.getDefault());
         }

         TimeZoneDropDown.this.ready = true;
         TimeZoneDropDown.this.dropdown.setCursor(Cursor.getDefaultCursor());
      }

      private void select(TimeZone var1) {
         TimeZoneDropDown.this.dropdown.setSelectedItem(var1);
      }
   }

   private static class ComboBoxModelProducer implements Callable<ComboBoxModel> {
      private ComboBoxModelProducer() {
      }

      public ComboBoxModel call() throws Exception {
         DefaultComboBoxModel var1 = new DefaultComboBoxModel();
         var1.addElement(TimeZones.DEFAULT);
         var1.addElement(TimeZones.GMT);
         TreeMap var2 = Maps.newTreeMap();

         for (String var6 : TimeZone.getAvailableIDs()) {
            var2.put(var6, TimeZone.getTimeZone(var6));
         }

         var2.remove(TimeZones.DEFAULT.getID());
         var2.remove(TimeZones.GMT.getID());

         for (Object __etlItem131 : var2.values()) {
            TimeZone var8 = (TimeZone) __etlItem131;
            var1.addElement(var8);
         }

         return var1;
      }
   }
}
