package com.etlforge.integration.client.ui.sfdc.wizard.prefs;

import com.etlforge.integration.client.ui.sfdc.TimeZoneSelector;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.prefs.PreferenceUi;
import org.etlforge.integration.salesforce.bulkload.TimeZoneId;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.event.ChangeListeners;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class TimeZonePreferenceUi implements PreferenceUi {
   private final TimeZoneSelector selector = new TimeZoneSelector();
   private final TimeZonePreference pref;
   private final InputPanel inputPanel = new InputPanel();
   private final TimeZonePreferenceUi.ChangeEventDispatcher changeEventDispatcher = new TimeZonePreferenceUi.ChangeEventDispatcher();

   public TimeZonePreferenceUi(TimeZonePreference var1) {
      this.pref = var1;
      this.selector.setCaption("Specify the time zone when reading source data:");
      this.selector.setSelectedTimeZoneId(var1.getTimeZoneId());
      this.selector.registerWithParent(this.inputPanel);
      this.inputPanel.setSendInputChangeEvents(true);
      this.inputPanel.addInputChangeListener(this.changeEventDispatcher);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(InvisiblePanel.of(this.selector));
   }

   @Override
   public void requestFocus() {
      this.selector.requestFocus();
   }

   @Override
   public void apply() {
      TimeZoneId var1 = this.selector.getSelectedTimeZoneId();
      this.pref.setTimeZoneId(var1);
   }

   @Override
   public void restore() {
      TimeZoneId var1 = this.pref.getTimeZoneId();
      this.selector.setSelectedTimeZoneId(var1);
   }

   @Override
   public void restoreDefault() {
      TimeZoneId var1 = this.pref.getDefaultValue();
      this.selector.setSelectedTimeZoneId(var1);
   }

   @Override
   public boolean isDirty() {
      return !this.selector.getSelectedTimeZoneId().equals(this.pref.getTimeZoneId());
   }

   @Override
   public void addChangeListener(ChangeListener var1) {
      this.changeEventDispatcher.addReceiver(var1);
   }

   @Override
   public void share() {
   }

   private static class ChangeEventDispatcher implements InputChangeListener {
      private final ChangeListeners receivers = new ChangeListeners();

      private ChangeEventDispatcher() {
      }

      @Override
      public void inputChanged(InputChangeEvent var1) {
         ChangeEvent var2 = new ChangeEvent(var1.getSource());
         this.receivers.call().stateChanged(var2);
      }

      public void addReceiver(ChangeListener var1) {
         this.receivers.add(var1);
      }
   }
}
