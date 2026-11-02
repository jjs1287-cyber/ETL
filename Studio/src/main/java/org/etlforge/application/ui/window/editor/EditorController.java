package org.etlforge.application.ui.window.editor;

import java.awt.event.MouseEvent;
import javax.swing.KeyStroke;

public interface EditorController {
   boolean processKeyStroke(Editor var1, KeyStroke var2);

   boolean processMouseEvent(Editor var1, MouseEvent var2);
}
