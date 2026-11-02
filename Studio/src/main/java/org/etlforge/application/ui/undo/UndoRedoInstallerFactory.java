package org.etlforge.application.ui.undo;

import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.ui.undo.UndoRedoInstaller;

public final class UndoRedoInstallerFactory {
   public static UndoRedoInstaller newInstaller() {
      UndoRedoInstaller var0 = new UndoRedoInstaller();
      var0.decorateUndoAction(ActionDecorators.UNDO);
      var0.decorateRedoAction(ActionDecorators.REDO);
      return var0;
   }

   private UndoRedoInstallerFactory() {
   }
}
