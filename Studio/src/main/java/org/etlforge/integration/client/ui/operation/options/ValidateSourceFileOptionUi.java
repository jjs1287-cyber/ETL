package org.etlforge.integration.client.ui.operation.options;

import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.entity.id.TextDocumentId;
import org.etlforge.integration.data.entity.operation.options.ValidateSourceFileOption;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;

public final class ValidateSourceFileOptionUi implements OperationOptionUi, Disposable {
   private final KongaCheckBox activator;
   private final EditValidationRulesAction editRulesAction;
   private final DestinationSelector validRecords;
   private final DestinationSelector invalidRecords;

   public ValidateSourceFileOptionUi(ValidateSourceFileOption var1, TextDocumentId var2, EntityExplorerSupport var3) {
      this.activator = new KongaCheckBox("Validate Source File", var1.isValidationEnabled());
      this.editRulesAction = new EditValidationRulesAction(var2, var3);
      this.validRecords = new DestinationSelector(var3, var1.getDestinationForValidRecords(), "valid");
      this.invalidRecords = new DestinationSelector(var3, var1.getDestinationForInvalidRecords(), "invalid");
      this.bindActivatorStateToDestinationSelectors();
   }

   private void bindActivatorStateToDestinationSelectors() {
      this.activator.addSelectionChangeListener(new Receiver<Boolean>() {
         public void handle(Boolean var1) {
            ValidateSourceFileOptionUi.this.updateEnabledState();
         }
      });
      this.updateEnabledState();
   }

   private void updateEnabledState() {
      boolean var1 = this.activator.isSelected();
      this.editRulesAction.setEnabled(var1);
      this.validRecords.setEnabled(var1);
      this.invalidRecords.setEnabled(var1);
   }

   @Override
   public void dispose() {
      this.validRecords.dispose();
      this.invalidRecords.dispose();
   }

   @Override
   public void decorate(UiCanvas var1) {
      TwoColumnPanel var2 = TwoColumnPanel.forFormEntry(5, 10);
      var2.addRow(this.activator, Horizontal.Align.east(this.editRulesAction).container());
      var2.withLeftInsets(5, 30, 5, 5).withRightInsets(5, 0, 5, 0);
      this.validRecords.layoutIn(var2);
      this.invalidRecords.layoutIn(var2);
      var1.display(var2.invisible());
   }

   @Override
   public void requestFocus() {
      this.activator.requestFocusInWindow();
   }

   public ValidateSourceFileOption getDefinedOption() {
      return this.activator.isSelected()
         ? ValidateSourceFileOption.validate(this.validRecords.getDestination(), this.invalidRecords.getDestination())
         : ValidateSourceFileOption.noValidation();
   }

   @Override
   public void registerAsChangeSource(InputPanel var1) {
      var1.addInputField(new CheckBoxInputField("Activator", this.activator));
      this.validRecords.registerAsChangeSource(var1);
      this.invalidRecords.registerAsChangeSource(var1);
   }
}
