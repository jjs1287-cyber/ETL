package org.etlforge.ui.widget.text;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;

public abstract class ElementClickDetector {
   private JTextComponent m_text;
   private MouseListener m_listener;

   public ElementClickDetector(JTextComponent var1) {
      this.m_text = var1;
      this.m_listener = new ElementClickDetector.ListenerImpl();
      this.m_text.addMouseListener(this.m_listener);
   }

   public final void shutDown() {
      this.m_text.removeMouseListener(this.m_listener);
   }

   public void handleElementClick(Element var1) {
   }

   public void handleElementClick(String var1) {
   }

   public void handleBadLocationException(BadLocationException var1) {
      var1.printStackTrace();
   }

   private class ListenerImpl extends MouseAdapter {
      private ListenerImpl() {
      }

      @Override
      public void mouseReleased(MouseEvent var1) {
         Element var2 = this.getElementClickedOn(var1.getPoint());
         if (var2 != null) {
            this.process(var2);
         }
      }

      private void process(Element var1) {
         ElementClickDetector.this.handleElementClick(var1);

         try {
            String var2 = this.getElementContens(var1);
            ElementClickDetector.this.handleElementClick(var2);
         } catch (BadLocationException var3) {
            ElementClickDetector.this.handleBadLocationException(var3);
         }
      }

      private Element getElementClickedOn(Point var1) {
         int var2 = ElementClickDetector.this.m_text.viewToModel(var1);
         Element var3 = Utilities.getParagraphElement(ElementClickDetector.this.m_text, var2);
         int var4 = var3.getElementIndex(var2);
         return var4 >= 0 && var4 < var3.getElementCount() ? var3.getElement(var4) : null;
      }

      private String getElementContens(Element var1) throws BadLocationException {
         int var2 = var1.getStartOffset();
         int var3 = var1.getEndOffset();
         return ElementClickDetector.this.m_text.getDocument().getText(var2, var3 - var2);
      }
   }
}
