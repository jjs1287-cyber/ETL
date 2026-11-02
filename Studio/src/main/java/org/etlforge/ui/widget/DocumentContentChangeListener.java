package org.etlforge.ui.widget;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent.EventType;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public abstract class DocumentContentChangeListener implements DocumentListener {
   @Override
   public void changedUpdate(DocumentEvent var1) {
   }

   @Override
   public void insertUpdate(DocumentEvent var1) {
      this.contentChanged(var1);
   }

   @Override
   public void removeUpdate(DocumentEvent var1) {
      this.contentChanged(var1);
   }

   public abstract void contentChanged(DocumentEvent var1);

   protected String getInsertedString(DocumentEvent var1) {
      if (var1.getType() == EventType.INSERT) {
         Document var2 = var1.getDocument();

         try {
            return var2.getText(var1.getOffset(), var1.getLength());
         } catch (BadLocationException var4) {
         }
      }

      return null;
   }
}
