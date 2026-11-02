package org.etlforge.ui.text;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.util.mouse.MouseHoverer;
import org.etlforge.ui.widget.DocumentContentChangeListener;

public abstract class TextComponentMouseHoverer extends MouseHoverer {
   private DocumentListener docListener;

   public TextComponentMouseHoverer(JTextComponent var1, int var2) {
      super(var1, var2);
   }

   @Override
   public void dispose() {
      if (!this.isDisposed()) {
         super.dispose();
         if (this.docListener != null) {
            ((JTextComponent)this.getTarget()).getDocument().removeDocumentListener(this.docListener);
            this.docListener = null;
         }
      }
   }

   @Override
   public void install() throws IllegalStateException {
      super.install();
      if (this.docListener == null) {
         this.docListener = new DocumentContentChangeListener() {
            @Override
            public void contentChanged(DocumentEvent var1) {
               TextComponentMouseHoverer.this.handleDocumentChange(var1);
            }
         };
         ((JTextComponent)this.getTarget()).getDocument().addDocumentListener(this.docListener);
      }
   }

   protected void handleDocumentChange(DocumentEvent var1) {
   }
}
