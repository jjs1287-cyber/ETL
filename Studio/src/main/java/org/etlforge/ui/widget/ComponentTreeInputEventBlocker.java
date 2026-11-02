package org.etlforge.ui.widget;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public final class ComponentTreeInputEventBlocker extends ComponentTreeInputEventHandler {
   public ComponentTreeInputEventBlocker(Container var1) {
      super(var1);
   }

   @Override
   public void handleKeyEvent(KeyEvent var1) {
      var1.consume();
   }

   @Override
   public void handleMouseEvent(MouseEvent var1) {
      var1.consume();
   }
}
