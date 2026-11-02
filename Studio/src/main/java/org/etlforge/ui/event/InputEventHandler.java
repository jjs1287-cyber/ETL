package org.etlforge.ui.event;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface InputEventHandler {
   void handleMouseEvent(MouseEvent var1);

   void handleKeyEvent(KeyEvent var1);
}
