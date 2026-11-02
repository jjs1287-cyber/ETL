package org.etlforge.ui.widget.text;

import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.etlforge.util.Interval;

public class TextPaneColorDecorator implements TextComponentColorDecorator {
   private final JTextPane textPane;
   private final StyledDocument document;
   private final Style defaultStyle;

   public TextPaneColorDecorator(JTextPane var1) {
      this.textPane = var1;
      this.document = var1.getStyledDocument();
      this.defaultStyle = this.document.getStyle("default");
   }

   @Override
   public void addColoredRegion(final Interval var1, Color var2) {
      StyleContext var3 = StyleContext.getDefaultStyleContext();
      final AttributeSet var4 = var3.addAttribute(this.defaultStyle, StyleConstants.Foreground, var2);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TextPaneColorDecorator.this.document.setCharacterAttributes(var1.startsAt(), var1.length(), var4, true);
         }
      });
   }

   @Override
   public void clearColoredRegions() {
      EventQueue.invokeLater(
         new Runnable() {
            @Override
            public void run() {
               TextPaneColorDecorator.this.document
                  .setCharacterAttributes(0, TextPaneColorDecorator.this.document.getLength(), TextPaneColorDecorator.this.defaultStyle, true);
            }
         }
      );
   }

   @Override
   public void dispose() {
      this.clearColoredRegions();
   }

   @Override
   public void repaint() {
      this.textPane.repaint();
   }
}
