package org.etlforge.integration.client.ui.interchange.entity.operation.page;

import org.etlforge.integration.client.ui.entity.selector.combo.IntegrationEntityComboBox;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.operation.SchedulePolicy;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.button.KongaCheckBox;

public final class SchedulePanel extends InputPanel {
   private final IntegrationEntityComboBox scheduleSelector;
   private final CheckBoxInputField policyChoice;

   public SchedulePanel(OperationWrapper var1, IntegrationProject var2) {
      this.scheduleSelector = new IntegrationEntityComboBox(var2, EntityType.Schedule, null, true);
      this.scheduleSelector.addSelectionListener(new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            SchedulePanel.this.updatePolicyChoiceEnabledState();
         }
      });
      this.policyChoice = this.createSchedulePolicyChoice(var1);
      this.scheduleSelector.setSelectedEntity(var1.getScheduleId());
      this.updatePolicyChoiceEnabledState();
      this.layoutComponents();
      this.addInputFields();
   }

   public void setEnabled(boolean var1) {
      this.scheduleSelector.setEnabled(var1);
      this.policyChoice.setEnabled(var1 && this.isScheduleSelected());
   }

   @Override
   public void dispose() {
      this.scheduleSelector.dispose();
      super.dispose();
   }

   private boolean isScheduleSelected() {
      return this.getSelectedSchedule() != null;
   }

   public Schedule getSelectedSchedule() {
      return (Schedule)this.scheduleSelector.getSelectedEntity();
   }

   public SchedulePolicy getSelectedPolicy() {
      return this.policyChoice.isSelected() ? SchedulePolicy.STRICT : SchedulePolicy.YIELD_IF_STILL_RUNNING;
   }

   private CheckBoxInputField createSchedulePolicyChoice(OperationWrapper var1) {
      boolean var2 = getPolicyChoice(var1.getSchedulePolicy());
      KongaCheckBox var3 = new KongaCheckBox("Start even if previously scheduled operation is still running", var2);
      return new CheckBoxInputField("SchedulePolicy", var3);
   }

   private static boolean getPolicyChoice(SchedulePolicy var0) {
      return var0 == SchedulePolicy.STRICT;
   }

   private void updatePolicyChoiceEnabledState() {
      this.policyChoice.setEnabled(this.isScheduleSelected());
   }

   private void layoutComponents() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      var1.withLeftInsets(5, 0, 5, 10);
      var1.withRightInsets(5, 0, 5, 5);
      this.scheduleSelector.layoutIn(Strings.get("Page.Label.Schedule"), var1);
      var1.addRow(null, this.policyChoice.getInputComponent());
      this.wrap(var1);
   }

   private void addInputFields() {
      this.addInputField(this.scheduleSelector.asInputField("schedule", null));
      this.addInputField(this.policyChoice);
   }
}
