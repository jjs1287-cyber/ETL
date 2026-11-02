package org.etlforge.integration.client.plugin.ui.pipeline.applied;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;

public final class ApplyPluginSettingsAction extends AbstractAction {
   private final AppliedPipelinePluginsController controller;

   public ApplyPluginSettingsAction(AppliedPipelinePluginsModel var1, AppliedPipelinePluginsController var2) {
      super("Apply");
      this.controller = var2;
      this.bindEnabledState(var1);
   }

   private void bindEnabledState(AppliedPipelinePluginsModel var1) {
      ApplyPluginSettingsAction.EnabledBinding var2 = new ApplyPluginSettingsAction.EnabledBinding(var1);
      var2.syncUi();
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ApplyPluginSettingsAction.this.controller.applyCurrentSettings();
         }
      });
   }

   private class EnabledBinding implements PropertyChangeListener {
      private final AppliedPipelinePluginsModel model;

      public EnabledBinding(AppliedPipelinePluginsModel var2) {
         this.model = var2;
         var2.addPropertyChangeListener("busy", this);
         var2.addPropertyChangeListener("dirty", this);
         this.syncUi();
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         this.syncUi();
      }

      private void syncUi() {
         ApplyPluginSettingsAction.this.setEnabled(this.model.isDirty() && !this.model.isBusy());
      }
   }
}
