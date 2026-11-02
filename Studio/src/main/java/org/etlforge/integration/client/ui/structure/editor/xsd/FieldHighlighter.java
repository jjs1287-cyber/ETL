package org.etlforge.integration.client.ui.structure.editor.xsd;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import org.etlforge.ui.widget.DocumentContentChangeListener;

final class FieldHighlighter {
   private final JTextField field;

   public FieldHighlighter(JTextField var1) {
      this.field = var1;
   }

   public void highlight() {
      this.field.requestFocusInWindow();
      this.field.setBackground(new Color(255, 255, 0, 192));
      FieldHighlighter.Disposer var1 = new FieldHighlighter.Disposer();
      this.field.addFocusListener(var1);
      this.field.getDocument().addDocumentListener(var1);
   }

   private class Disposer extends DocumentContentChangeListener implements FocusListener {
      private Disposer() {
      }

      @Override
      public void contentChanged(DocumentEvent var1) {
         this.restoreField();
      }

      private void restoreField() {
         FieldHighlighter.this.field.setBackground(javax.swing.UIManager.getColor("Panel.background"));
         FieldHighlighter.this.field.removeFocusListener(this);
         FieldHighlighter.this.field.getDocument().removeDocumentListener(this);
      }

      @Override
      public void focusLost(FocusEvent var1) {
         this.restoreField();
      }

      @Override
      public void focusGained(FocusEvent var1) {
      }
   }
}

