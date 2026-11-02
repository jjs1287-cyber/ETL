package org.etlforge.ui.undo;

import javax.swing.Action;

public interface UndoRedoController {
   boolean canUndo();

   void undo();

   boolean canRedo();

   void redo();

   void dispose();

   Action getUndoAction();

   Action getRedoAction();

   void alwaysConsumeKeyboardTriggers();
}
