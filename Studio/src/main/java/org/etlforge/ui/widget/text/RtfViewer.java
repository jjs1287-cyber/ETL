package org.etlforge.ui.widget.text;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JScrollPane;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

public final class RtfViewer implements UiProvider {
   private final KongaEditorPane editor;

   public RtfViewer() {
      this(null);
   }

   public RtfViewer(String var1) {
      this.editor = new KongaEditorPane("text/rtf", var1);
      this.editor.setEditable(false);
      this.goToBeginning();
   }

   public void goToBeginning() {
      this.editor.setCaretPosition(0);
   }

   public void setPreferredSize(Dimension var1) {
      this.editor.setPreferredSize(var1);
   }

   public void openFile(File var1) throws IOException {
      FileInputStream var2 = new FileInputStream(var1);

      try {
         this.editor.read(var2, null);
         this.goToBeginning();
      } finally {
         KongaIoUtils.close(var2);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(new JScrollPane(this.editor));
   }

   @Override
   public void requestFocus() {
      this.editor.requestFocusInWindow();
   }
}
