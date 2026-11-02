package org.etlforge.integration.client.ui.interchange.entity.httpendpoint.page;

import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.data.entity.HttpEndpoint;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.widget.label.KongaLabel;

class RequestPanel implements UiProvider {
   private final InputPanel panel = new InputPanel();
   private final TextInputField2 requestVariableField = this.createVariableField();

   public RequestPanel(HttpEndpoint var1, InputPanel var2) {
      this.panel.setSendInputChangeEvents(true);
      this.panel.setSendUndoEvents(true);
      this.reset(var1);
      if (var2 != null) {
         var2.addInputPanel(this.panel);
      }
   }

   private TextInputField2 createVariableField() {
      TextInputField2 var1 = InputFieldFactory.getTextInputField2(
         "Request Variable Name", KongaLabel.withMnemonic("Global variable name for request"), 24, true
      );
      this.panel.addInputField(var1, var1, var1);
      return var1;
   }

   public void apply(HttpEndpoint var1) {
      var1.setRequestVariable(this.requestVariableField.getValue().trim());
   }

   public void reset(HttpEndpoint var1) {
      this.requestVariableField.setValue(var1.getRequestVariable());
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.doLayout();
      var1.display(this.panel.getUi());
   }

   private void doLayout() {
      this.panel.wrap(UiProviderAdapter.of(InputFieldFactory.getCapsule(this.requestVariableField)));
      this.panel.setBorder(StyleSheet.getDefaultTitleBorder("Request Settings"));
   }

   @Override
   public void requestFocus() {
      this.requestVariableField.requestFocus();
   }

   public BoundProperty asBoundProperty() {
      return BoundPropertyFactory.textProperty("request_variable", this.requestVariableField);
   }
}
