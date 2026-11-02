package org.etlforge.ui.widget.text.undo;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.action.ActionDecorator;
import org.etlforge.ui.undo.RedoActionDecorator;
import org.etlforge.ui.undo.UndoActionDecorator;
import org.etlforge.ui.undo.UndoRedoController;

public class TextUndoRedoControllerFactory {
   private ActionDecorator undoDecorator = UndoActionDecorator.INSTANCE;
   private ActionDecorator redoDecorator = RedoActionDecorator.INSTANCE;

   public UndoRedoController newController(JComponent var1) {
      TextUndoRedoController var2 = new TextUndoRedoController(var1);
      this.customizeController(var2);
      return var2;
   }

   public UndoRedoController newController(JTextComponent var1) {
      TextUndoRedoController var2 = new TextUndoRedoController(var1);
      this.customizeController(var2);
      return var2;
   }

   public UndoRedoController newController(JTextComponent[] var1, InputMap var2, ActionMap var3) {
      TextUndoRedoController var4 = new TextUndoRedoController(var1, var2, var3);
      this.customizeController(var4);
      return var4;
   }

   public void setUndoActionDecorator(ActionDecorator var1) {
      if (var1 == null) {
         throw new NullPointerException("undo");
      }

      this.undoDecorator = var1;
   }

   public void setRedoActionDecorator(ActionDecorator var1) {
      if (var1 == null) {
         throw new NullPointerException("redo");
      }

      this.redoDecorator = var1;
   }

   private void customizeController(TextUndoRedoController var1) {
      if (this.undoDecorator != null) {
         var1.decorateUndoAction(this.undoDecorator);
      }

      if (this.redoDecorator != null) {
         var1.decorateRedoAction(this.redoDecorator);
      }
   }
}
