package org.etlforge.application.ui.window.editor;

import java.awt.event.MouseEvent;
import javax.swing.KeyStroke;

public abstract class EditorControllerAdapter implements EditorController {
   @Override
   public boolean processKeyStroke(Editor var1, KeyStroke var2) {
      return false;
   }

   @Override
   public boolean processMouseEvent(Editor var1, MouseEvent var2) {
      return false;
   }
}
