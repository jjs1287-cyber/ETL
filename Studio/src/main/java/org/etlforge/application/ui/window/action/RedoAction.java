package org.etlforge.application.ui.window.action;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.UndoService;
import org.etlforge.ui.util.KeyUtils;

public final class RedoAction extends AbstractUndoRedoAction {
   public static final String ID = "action.Redo";

   public RedoAction(ApplicationWindow var1) {
      super(var1, "action.Redo", "Redo");
      this.putValue("AcceleratorKey", KeyUtils.REDO);
   }

   @Override
   protected UndoService.Activity getActivity(UndoService var1) {
      return var1.getRedoActivity();
   }
}
