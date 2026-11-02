package org.etlforge.ui.widget.text;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.util.Interval;
import org.etlforge.util.IntervalFactory;

public class ColorTextPane implements UiProvider {
   private final JTextPane adaptee;
   private final TextComponentColorDecorator decorator;
   private Dimension preferredSize;

   public ColorTextPane() {
      this(new KongaTextPane());
   }

   public ColorTextPane(JTextPane var1) {
      this.adaptee = var1;
      this.decorator = new TextPaneColorDecorator(var1);
   }

   public void clear() {
      this.adaptee.setText("");
   }

   public void setText(String var1) {
      this.setText(var1, this.adaptee.getForeground());
   }

   public void setText(String var1, Color var2) {
      if (var1 == null) {
         var1 = "";
      }

      this.adaptee.setText(var1);
      Interval var3 = IntervalFactory.fromStartAndLength(0, var1.length());
      this.decorator.addColoredRegion(var3, var2);
   }

   public void append(String var1) {
      this.append(var1, this.adaptee.getForeground());
   }

   public void append(String var1, Color var2) {
      if (var1 == null) {
         var1 = "";
      }

      try {
         int var3 = this.adaptee.getDocument().getLength();
         this.adaptee.getDocument().insertString(var3, var1, null);
         Interval var4 = IntervalFactory.fromStartAndLength(var3, var1.length());
         this.decorator.addColoredRegion(var4, var2);
      } catch (BadLocationException var5) {
      }
   }

   public void setCaretPosition(int var1) {
      this.adaptee.setCaretPosition(var1);
   }

   public String getText() {
      return this.adaptee.getText();
   }

   public void setPreferredSize(Dimension var1) {
      this.preferredSize = var1;
   }

   public void setEditable(boolean var1) {
      this.adaptee.setEditable(var1);
   }

   public void setEnabled(boolean var1) {
      this.adaptee.setEnabled(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      JScrollPane var2 = new JScrollPane(this.adaptee);
      if (this.preferredSize != null) {
         var2.setPreferredSize(this.preferredSize);
      }

      var1.display(var2);
   }

   @Override
   public void requestFocus() {
      this.adaptee.requestFocusInWindow();
   }
}
