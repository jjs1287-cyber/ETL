package org.etlforge.application.ui.window.editor;

import javax.swing.Action;

public interface UndoService {
   UndoService.Activity getUndoActivity();

   UndoService.Activity getRedoActivity();

   void discardAllEdits();

   void addListener(UndoServiceListener var1);

   void removeListener(UndoServiceListener var1);

   interface Activity {
      boolean isAvailable();

      void invoke();

      String getRepresentationName();

      Action asAction();
   }
}
