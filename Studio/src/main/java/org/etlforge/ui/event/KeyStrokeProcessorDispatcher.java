package org.etlforge.ui.event;

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

public class KeyStrokeProcessorDispatcher implements KeyEventDispatcher {
   @Override
   public boolean dispatchKeyEvent(KeyEvent var1) {
      KeyStroke var2 = KeyStroke.getKeyStrokeForEvent(var1);
      Object var3 = var1.getSource();
      Component var4 = var1.getComponent();

      while (!(var4 instanceof KeyStrokeProcessor) || !((KeyStrokeProcessor)var4).process(var2, var3)) {
         var4 = var4.getParent();
         if (var4 == null) {
            return false;
         }
      }

      return true;
   }
}
