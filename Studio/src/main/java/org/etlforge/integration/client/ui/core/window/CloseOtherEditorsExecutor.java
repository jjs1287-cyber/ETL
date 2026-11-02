package org.etlforge.integration.client.ui.core.window;

import java.awt.event.KeyEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowKeyDispatcher;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;

final class CloseOtherEditorsExecutor implements WindowKeyDispatcher {
   @Override
   public boolean dispatch(KeyEvent var1, ApplicationWindow var2) {
      EditorService var3 = var2.getEditorService();
      Editor var4 = var3.getActiveEditor();
      if (var4 != null) {
         var3.closeOtherEditors(var4);
      }

      return true;
   }

   @Override
   public String getId() {
      return this.getClass().getName();
   }
}
