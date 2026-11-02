package org.etlforge.ui.input;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.etlforge.ui.resources.UiResourceBundle;

public class InputFieldUndoableEdit implements UndoableEdit {
   private final InputField m_inputField;
   private final UndoableEdit m_wrappee;

   public InputFieldUndoableEdit(InputField var1, UndoableEdit var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("field cannot be null");
      }

      if (var2 == null) {
         throw new IllegalArgumentException("e cannot be null");
      }

      this.m_inputField = var1;
      this.m_wrappee = var2;
   }

   @Override
   public boolean addEdit(UndoableEdit var1) {
      return this.m_wrappee.addEdit(var1);
   }

   @Override
   public boolean canRedo() {
      return this.m_wrappee.canRedo();
   }

   @Override
   public boolean canUndo() {
      return this.m_wrappee.canUndo();
   }

   @Override
   public void die() {
      this.m_wrappee.die();
   }

   @Override
   public String getPresentationName() {
      return this.m_inputField.getUndoRedoPresentation();
   }

   @Override
   public String getRedoPresentationName() {
      return this.getUndoRedoPresentationNameImpl(UiResourceBundle.getString("Redo"));
   }

   @Override
   public String getUndoPresentationName() {
      return this.getUndoRedoPresentationNameImpl(UiResourceBundle.getString("Undo"));
   }

   private String getUndoRedoPresentationNameImpl(String var1) {
      String var2 = this.getPresentationName();
      return isEmpty(var2) ? var1 : var1 + " " + var2;
   }

   @Override
   public boolean isSignificant() {
      return this.m_wrappee.isSignificant();
   }

   @Override
   public void redo() throws CannotRedoException {
      this.m_wrappee.redo();
   }

   @Override
   public boolean replaceEdit(UndoableEdit var1) {
      return this.m_wrappee.replaceEdit(var1);
   }

   @Override
   public void undo() throws CannotUndoException {
      this.m_wrappee.undo();
   }

   private static boolean isEmpty(String var0) {
      return var0 == null || var0.length() == 0;
   }
}
