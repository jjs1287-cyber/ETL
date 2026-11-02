package org.etlforge.integration.client.ui.interchange.entity.script;

import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.editor.EditorDisplayer;

public final class ScriptPadEditorDisplayer implements ScriptPadDisplayer {
   private final EditorDisplayer delegate;
   private ScriptPad pad;
   private ScriptPadEditor editor;

   public ScriptPadEditorDisplayer(EditorDisplayer var1) {
      this.delegate = var1;
   }

   @Override
   public void close() {
      if (this.editor != null) {
         this.editor.close();
         this.editor = null;
         this.pad = null;
      }
   }

   @Override
   public void open(ScriptPad var1) {
      if (var1 != this.pad) {
         this.close();
      }

      if (this.pad == null) {
         this.pad = var1;
         this.editor = this.createEditor(var1);
      }

      this.delegate.openEditor(this.editor);
   }

   private ScriptPadEditor createEditor(ScriptPad var1) {
      assert this.editor == null : "There is already a Script Pad editor open.";
      ScriptPadEditor var2 = new ScriptPadEditor(var1);
      var2.addResource(new AbstractWindowElementResource() {
         @Override
         public void dispose(WindowElement var1) {
            ScriptPadEditorDisplayer.this.pad.onPadClosed();
            ScriptPadEditorDisplayer.this.close();
         }
      });
      return var2;
   }

   @Override
   public void requestAttention() {
      if (this.editor != null) {
         this.delegate.openEditor(this.editor);
      }
   }
}
