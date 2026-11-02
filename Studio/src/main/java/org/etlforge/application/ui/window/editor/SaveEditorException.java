package org.etlforge.application.ui.window.editor;

public class SaveEditorException extends Exception {
   private final Editor editor;

   public SaveEditorException(Editor var1, String var2) {
      super(var2);
      this.editor = var1;
   }

   public SaveEditorException(Editor var1, String var2, Throwable var3) {
      super(var2, var3);
      this.editor = var1;
   }

   public final Editor getEditor() {
      return this.editor;
   }
}
