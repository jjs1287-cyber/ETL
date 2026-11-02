package org.etlforge.ui.input;

public interface InputPanelSite extends ChangeSource {
   void setActive(boolean var1);

   void decorateFields();

   boolean isInputValid();

   void validateInput() throws InputPanelException;

   void dispose();

   void visitAllFields(InputFieldVisitor var1);
}
