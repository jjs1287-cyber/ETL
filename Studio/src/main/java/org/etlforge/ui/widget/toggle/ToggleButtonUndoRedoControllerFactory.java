package org.etlforge.ui.widget.toggle;

import javax.swing.JToggleButton;
import org.etlforge.ui.action.ActionDecorator;
import org.etlforge.ui.undo.RedoActionDecorator;
import org.etlforge.ui.undo.UndoActionDecorator;
import org.etlforge.ui.undo.UndoRedoController;

public class ToggleButtonUndoRedoControllerFactory {
   private ActionDecorator undoDecorator = UndoActionDecorator.INSTANCE;
   private ActionDecorator redoDecorator = RedoActionDecorator.INSTANCE;

   public UndoRedoController newController(JToggleButton var1) {
      ToggleButtonUndoRedoController var2 = new ToggleButtonUndoRedoController(var1);
      this.customizeController(var2);
      return var2;
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

   private void customizeController(UndoRedoController var1) {
      if (this.undoDecorator != null) {
         this.undoDecorator.decorate(var1.getUndoAction());
      }

      if (this.redoDecorator != null) {
         this.redoDecorator.decorate(var1.getRedoAction());
      }
   }
}
