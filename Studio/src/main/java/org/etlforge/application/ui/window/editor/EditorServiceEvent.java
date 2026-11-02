package org.etlforge.application.ui.window.editor;

import java.util.EventObject;

public class EditorServiceEvent extends EventObject {
   private final Editor editor;

   public EditorServiceEvent(EditorService var1, Editor var2) {
      super(var1);
      this.editor = var2;
   }

   public EditorService getSource() {
      return (EditorService)super.getSource();
   }

   public Editor getEditor() {
      return this.editor;
   }
}
