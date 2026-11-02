package org.etlforge.integration.client.ui.interchange.entity.message.page;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.integration.client.ui.interchange.util.InterchangeViewUtils;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.data.entity.Message;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.util.name.IllegalNameException;

public class MessageTopPanel extends InputPanel {
   private final TextInputField2 nameField;

   public MessageTopPanel(Message var1) {
      this.nameField = this.createNameField(var1);
      this.setMessage(var1);
      this.addInputFields();
      this.layoutComponents();
   }

   private TextInputField2 createNameField(Message var1) {
      String var2 = Strings.get("MessagePage.Label.Name");
      JLabel var3 = new JLabel(var2);
      return InterchangeViewUtils.getIntegrationEntityNameInputField(var1, var2, var3, 30);
   }

   BoundProperty[] getBoundPropertyControls() {
      return new BoundProperty[]{BoundPropertyFactory.nameProperty(this.nameField)};
   }

   private void addInputFields() {
      this.addInputField(this.nameField, this.nameField, this.nameField);
   }

   private void layoutComponents() {
      JPanel var1 = new JPanel();
      this.nameField.layoutVertically(var1);
      this.setLayout(new FlowLayout(3, 5, 0));
      this.getUi().add(var1);
   }

   public void setMessage(Message var1) {
      if (var1 != null) {
         this.nameField.setValue(var1.getName());
         this.nameField.getInputComponent().setCaretPosition(0);
      } else {
         this.nameField.setValue("");
      }
   }

   public void applyTo(Message var1) throws IllegalNameException {
      var1.setName(this.getMessageName());
   }

   private String getMessageName() {
      return this.nameField.getValue().trim();
   }

   void setInitialFocus() {
      this.nameField.requestFocus();
   }
}
