package org.etlforge.tools.xml.xmlviewer.ui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

public class CopyAction extends AbstractAction {
   private final JTextComponent textComponent;

   public CopyAction(JTextComponent var1) {
      super("Copy");
      this.textComponent = var1;
      this.putValue("ShortDescription", "Copy the selected text");
      this.putValue("MnemonicKey", 67);
      this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(67, 2));
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      if (this.textComponent != null) {
         this.textComponent.copy();
      }
   }
}
