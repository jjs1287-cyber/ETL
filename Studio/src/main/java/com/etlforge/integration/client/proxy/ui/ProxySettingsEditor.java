package com.etlforge.integration.client.proxy.ui;

import javax.swing.Action;
import javax.swing.event.UndoableEditListener;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.WaitStateBinding;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.util.Property;
import org.etlforge.util.net.ProxySettings;

public final class ProxySettingsEditor implements UiProvider {
   private final ProxySettingsEditorModel model;
   private final ProxySettingsEditorController controller;
   private final ProxySettingsForm inputForm = new ProxySettingsForm();
   private final WaitStateBinding waitStateBinding;

   public ProxySettingsEditor(ProxySettingsEditorModel var1, ProxySettingsEditorController var2) {
      this.model = var1;
      this.controller = var2;
      this.inputForm.set(var1.getProxySettings());
      this.waitStateBinding = new WaitStateBinding(var1, "busy");
      this.bindSettings();
      this.installValidationListener();
   }

   private void bindSettings() {
      ProxySettingsEditor.SettingsBinding var1 = new ProxySettingsEditor.SettingsBinding();
      var1.syncUi();
   }

   private void installValidationListener() {
      this.inputForm.setValidStateReceiver(new Receiver<Boolean>() {
         public void handle(Boolean var1) {
            ProxySettingsEditor.this.model.setValid(var1);
         }
      });
   }

   public ProxySettingsEditorModel getModel() {
      return this.model;
   }

   public void reset() {
      Property var1 = this.model.getProperty("px");
      var1.setSilently(null);
      this.controller.getSettings();
   }

   public void clear() {
      this.model.setProxySettings(ProxySettings.NONE);
   }

   public void save() {
      ProxySettings var1 = this.inputForm.get();
      this.model.setProxySettings(var1);
      this.controller.saveSettings();
   }

   public void setSaveAction(Action var1) {
      this.inputForm.setSaveAction(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.inputForm.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.inputForm.requestFocus();
   }

   public void setWaitService(WaitService var1) {
      this.waitStateBinding.setWaitService(var1);
   }

   public void setEnabled(boolean var1) {
      this.inputForm.setEnabled(var1);
   }

   public void addInputChangeListener(InputChangeListener var1) {
      this.inputForm.addInputChangeListener(var1);
   }

   public void removeInputChangeListener(InputChangeListener var1) {
      this.inputForm.removeInputChangeListener(var1);
   }

   public void addUndoableEditListener(UndoableEditListener var1) {
      this.inputForm.addUndoableEditListener(var1);
   }

   public void removeUndoableEditListener(UndoableEditListener var1) {
      this.inputForm.removeUndoableEditListener(var1);
   }

   private class SettingsBinding extends AbstractBinding<ProxySettings> {
      public SettingsBinding() {
         super(ProxySettingsEditor.this.model, "px");
      }

      protected void updateUiWithValue(ProxySettings var1) {
         ProxySettingsEditor.this.inputForm.set(var1);
      }
   }
}
