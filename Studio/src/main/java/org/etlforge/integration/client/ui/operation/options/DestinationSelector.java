package org.etlforge.integration.client.ui.operation.options;

import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.NewFileLocationCallback;
import org.etlforge.integration.client.ui.entity.wizard.ProjectItemSelector;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.operation.options.FileDestination;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.functors.FileBasedLocationFilter;
import org.etlforge.system.Disposable;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.label.KongaLabel;

final class DestinationSelector implements Disposable {
   private final KongaLabel targetLabel;
   private final ProjectItemSelector<Target> targetSelector;
   private final KongaLabel emailLabel;
   private final ProjectItemSelector<EmailMessage> emailSelector;

   public DestinationSelector(EntityExplorerSupport var1, FileDestination var2, String var3) {
      this.targetLabel = new KongaLabel("Write " + var3 + " records to:");
      this.targetSelector = this.createSelector(var1, EntityType.Target, Target.class, new FileBasedLocationFilter(true));
      this.targetSelector.setNewEntityCallback(new NewFileLocationCallback());
      this.targetSelector.setSelectedItemId(var2.getTargetId().orNull());
      this.emailLabel = new KongaLabel("Send " + var3 + " records to:");
      this.emailSelector = this.createSelector(var1, EntityType.EmailMessage, EmailMessage.class, EntityFilters.ACCEPTS_ALL);
      this.emailSelector.setSelectedItemId(var2.getEmailId().orNull());
   }

   private <T extends IntegrationEntity> ProjectItemSelector<T> createSelector(EntityExplorerSupport var1, EntityType var2, Class<T> var3, EntityFilter var4) {
      EditorPanel var5 = new EditorPanel(var1.getWindow());
      ProjectItemSelector var6 = new ProjectItemSelector(var3, var2, var1, var5, true);
      var6.setOptional(true);
      var6.setEditAutomaticallyOnSelect(false);
      var6.setFilter(var4);
      return var6;
   }

   @Override
   public void dispose() {
      this.targetSelector.dispose();
      this.emailSelector.dispose();
   }

   public void setEnabled(boolean var1) {
      this.targetLabel.setEnabled(var1);
      this.targetSelector.setEnabled(var1);
      this.emailLabel.setEnabled(var1);
      this.emailSelector.setEnabled(var1);
   }

   public void requestFocus() {
      this.targetSelector.requestFocus();
   }

   public void layoutIn(TwoColumnPanel var1) {
      var1.addRow(this.targetLabel.asUiProvider(), this.targetSelector);
      var1.addRow(this.emailLabel.asUiProvider(), this.emailSelector);
   }

   public void registerAsChangeSource(InputPanel var1) {
      var1.addInputField(this.targetSelector.asInputField("Target Selector"));
      var1.addInputField(this.emailSelector.asInputField("Email Selector"));
   }

   public FileDestination getDestination() {
      return new FileDestination(this.targetSelector.getSelectedItem(), this.emailSelector.getSelectedItem());
   }
}
