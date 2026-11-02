package org.etlforge.integration.client.ui.interchange.location.panel;
import javax.swing.JLabel;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.GlobalVariableLocation;
import org.etlforge.ui.Size;
import org.etlforge.ui.layout.OneColumnPanel;
public final class GlobalVariableLocationPanel extends DataLocationPanel {
   private final TextInputField2 variable;
   public GlobalVariableLocationPanel(GlobalVariableLocation location, SourceTarget direction) {
      super(location, direction);
      variable = new TextInputField2("Variable", new KongaTextField(Size.columns(40)), new JLabel("Variable:")) {
         @Override public boolean isInputValid(String value) { return GlobalVariableLocation.isValidVariableName(value.trim()); }
         @Override public String getInvalidMessage(String value) { return "Enter a global variable name, for example Payload or $Payload."; }
      };
      variable.setAutoTrim(true);
      variable.setRequired(true);
      addInputField(variable, variable, variable);
      reset();
   }
   @Override public void apply() { ((GlobalVariableLocation)getDisplayedObject()).setVariableName(variable.getValue()); }
   @Override public void reset() { variable.setValue(((GlobalVariableLocation)getDisplayedObject()).getVariableName()); }
   @Override public void display() {
      super.display();
      OneColumnPanel panel = new OneColumnPanel(); panel.withInsets(3,5,10,10);
      panel.add(new JLabel("Global Variable"));
      panel.add(InputFieldFactory.getCapsule(variable));
      panel.add(new JLabel(getSourceTarget() == SourceTarget.Source ? "Reads the variable's UTF-8 text. The variable must be set before use." : "Replaces the variable with the output text. Multiple output files are rejected."));
      wrap(panel);
   }
   @Override public void setInitialFocus() { variable.requestFocus(); }
}
