package org.etlforge.integration.client.ui.core.window;

import java.awt.event.KeyEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowKeyDispatcher;
import org.etlforge.application.ui.window.editor.EditorService;

final class CloseAllEditorsExecutor implements WindowKeyDispatcher {
   @Override
   public boolean dispatch(KeyEvent var1, ApplicationWindow var2) {
      EditorService var3 = var2.getEditorService();
      var3.closeAllEditors();
      return true;
   }

   @Override
   public String getId() {
      return this.getClass().getName();
   }
}
