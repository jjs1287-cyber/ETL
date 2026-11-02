package org.etlforge.application.ui.text.editor;

import java.awt.event.FocusEvent;
import javax.swing.text.DefaultCaret;
import org.etlforge.ui.UIManager2;

class ShowSelectionWhenNotFocusedCaret extends DefaultCaret {
   public ShowSelectionWhenNotFocusedCaret() {
      this.setBlinkRate(UIManager2.getInt("TextPane.caretBlinkRate", 500));
   }

   @Override
   public void focusLost(FocusEvent var1) {
      this.setVisible(false);
   }
}
