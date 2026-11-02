package org.etlforge.integration.client.ui.core.window;

import java.awt.event.KeyEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowKeyDispatcher;
import org.etlforge.application.ui.window.editor.Editor;

final class CurrentEditorCloser implements WindowKeyDispatcher {
   @Override
   public boolean dispatch(KeyEvent var1, ApplicationWindow var2) {
      Editor var3 = var2.getEditorService().getActiveEditor();
      if (var3 != null) {
         var3.close();
      }

      return true;
   }

   @Override
   public String getId() {
      return this.getClass().getName();
   }
}
