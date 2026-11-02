package org.etlforge.integration.client.ui.project;

import org.etlforge.application.ui.widget.TextFieldInput;

public final class ProjectNameInput extends TextFieldInput {
   public ProjectNameInput(String var1, String var2) {
      super(var1, var2);
      this.setValidator(new ProjectNameInputFieldValidator());
      this.setFieldWidth(30);
      this.setSelectAllWhenFocused(true);
   }
}
