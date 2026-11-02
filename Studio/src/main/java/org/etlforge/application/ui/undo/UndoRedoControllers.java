package org.etlforge.application.ui.undo;

import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.ui.widget.combobox.undo.ComboBoxUndoRedoControllerFactory;
import org.etlforge.ui.widget.text.undo.TextUndoRedoControllerFactory;
import org.etlforge.ui.widget.toggle.ToggleButtonUndoRedoControllerFactory;

public final class UndoRedoControllers {
   private static final TextUndoRedoControllerFactory textFactory = createTextFactory();
   private static final ComboBoxUndoRedoControllerFactory comboFactory = createComboFactory();
   private static final ToggleButtonUndoRedoControllerFactory toggleFactory = createToggleFactory();

   public static TextUndoRedoControllerFactory textFactory() {
      return textFactory;
   }

   public static ComboBoxUndoRedoControllerFactory comboFactory() {
      return comboFactory;
   }

   public static ToggleButtonUndoRedoControllerFactory toggleButtonFactory() {
      return toggleFactory;
   }

   private static TextUndoRedoControllerFactory createTextFactory() {
      TextUndoRedoControllerFactory var0 = new TextUndoRedoControllerFactory();
      var0.setUndoActionDecorator(ActionDecorators.UNDO);
      var0.setRedoActionDecorator(ActionDecorators.REDO);
      return var0;
   }

   private static ComboBoxUndoRedoControllerFactory createComboFactory() {
      ComboBoxUndoRedoControllerFactory var0 = new ComboBoxUndoRedoControllerFactory();
      var0.setUndoActionDecorator(ActionDecorators.UNDO);
      var0.setRedoActionDecorator(ActionDecorators.REDO);
      return var0;
   }

   private static ToggleButtonUndoRedoControllerFactory createToggleFactory() {
      ToggleButtonUndoRedoControllerFactory var0 = new ToggleButtonUndoRedoControllerFactory();
      var0.setUndoActionDecorator(ActionDecorators.UNDO);
      var0.setRedoActionDecorator(ActionDecorators.REDO);
      return var0;
   }

   private UndoRedoControllers() {
   }
}
