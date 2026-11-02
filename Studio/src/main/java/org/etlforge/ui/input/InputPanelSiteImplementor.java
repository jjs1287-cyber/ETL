package org.etlforge.ui.input;

import javax.swing.event.UndoableEditListener;

public final class InputPanelSiteImplementor implements InputPanelSite {
   private final InputPanel inputPanel;

   public InputPanelSiteImplementor(InputPanel var1) {
      this.inputPanel = var1;
   }

   @Override
   public void dispose() {
      this.inputPanel.dispose();
   }

   @Override
   public void setSendInputChangeEvents(boolean var1) {
      this.inputPanel.setSendInputChangeEvents(var1);
   }

   @Override
   public boolean isSendingInputChangeEvents() {
      return this.inputPanel.isSendingInputChangeEvents();
   }

   @Override
   public void addInputChangeListener(InputChangeListener var1) {
      this.inputPanel.addInputChangeListener(var1);
   }

   @Override
   public void removeInputChangeListener(InputChangeListener var1) {
      this.inputPanel.removeInputChangeListener(var1);
   }

   @Override
   public void setSendUndoEvents(boolean var1) {
      this.inputPanel.setSendUndoEvents(var1);
   }

   @Override
   public boolean isSendingUndoEvents() {
      return this.inputPanel.isSendingUndoEvents();
   }

   @Override
   public void addUndoableEditListener(UndoableEditListener var1) {
      this.inputPanel.addUndoableEditListener(var1);
   }

   @Override
   public void removeUndoableEditListener(UndoableEditListener var1) {
      this.inputPanel.removeUndoableEditListener(var1);
   }

   @Override
   public void validateInput() throws InputPanelException {
      this.inputPanel.validateInput();
   }

   @Override
   public void setActive(boolean var1) {
      this.inputPanel.setActive(var1);
   }

   @Override
   public void decorateFields() {
      this.inputPanel.decorateFields();
   }

   @Override
   public boolean isInputValid() {
      return this.inputPanel.isInputValid();
   }

   @Override
   public void visitAllFields(InputFieldVisitor var1) {
      this.inputPanel.visitAllFields(var1);
   }
}
