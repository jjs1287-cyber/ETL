package org.etlforge.integration.client.ui.project;

import java.awt.event.ActionListener;
import javax.swing.JLabel;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.widget.text.KongaTextField;

public final class ProjectNameInputField extends TextInputField2 {
   private final ProjectNameInputFieldValidator validator = new ProjectNameInputFieldValidator();

   public ProjectNameInputField() {
      this(null);
   }

   public ProjectNameInputField(String var1) {
      super("Project Name", new KongaTextField(30), var1);
      this.setSelectAllWhenFocused(true);
      this.setAutoTrim(true);
   }

   @Override
   public boolean isInputValid(String var1) {
      return this.validator.validate(var1);
   }

   @Override
   public String getInvalidMessage(String var1) {
      String var2 = this.validator.getErrorMessage(var1);
      return var2 != null ? var2 : super.getInvalidMessage(var1);
   }

   public void addActionListener(ActionListener var1) {
      this.getInputComponent().addActionListener(var1);
   }

   public void removeActionListener(ActionListener var1) {
      this.getInputComponent().removeActionListener(var1);
   }

   public UiProvider asUiProvider() {
      JLabel var1 = this.getLabel();
      this.getInputComponent().setLabel(var1);
      return this.getInputComponent().asUiProvider();
   }

   public KongaTextField getInputComponent() {
      return (KongaTextField)super.getInputComponent();
   }
}
