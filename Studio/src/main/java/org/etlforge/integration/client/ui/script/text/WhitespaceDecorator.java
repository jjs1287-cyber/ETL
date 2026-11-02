package org.etlforge.integration.client.ui.script.text;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;
import javax.swing.text.BadLocationException;
import org.etlforge.integration.client.ui.script.builder.ExpressionArea;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.util.Interval;

public class WhitespaceDecorator implements ComponentPainter {
   private static final Color COLOR = new Color(255, 255, 0, 75);
   private ExpressionArea area;

   public void install(ExpressionArea var1) {
      this.area = var1;
      var1.setPainter(this);
   }

   @Override
   public Dimension getPreferredSize(Dimension var1) {
      return var1;
   }

   @Override
   public void postRendering(Component var1, Graphics2D var2) {
      if (this.area.getDocument().getLength() != 0) {
         Set var3 = this.collectRowsToHighlight(var2);
         if (!var3.isEmpty()) {
            Color var4 = var2.getColor();
            var2.setColor(COLOR);

            for (Object __etlItem38 : var3) {
               Rectangle var6 = (Rectangle) __etlItem38;
               var2.fill(new Rectangle(0, var6.y, this.area.getTextPane().getWidth(), var6.height));
            }

            var2.setColor(var4);
         }
      }
   }

   private Set<Rectangle> collectRowsToHighlight(Graphics2D var1) {
      HashSet var2 = new HashSet();
      ScriptDocument var3 = this.area.getDocument();
      int var4 = DocumentUtils.getNumberOfRows(var3);
      int var5 = 1;

      while (var5 <= var4 && this.checkRow(var3, var5, var2)) {
         var5++;
      }

      if (var5 < var4) {
         int var6 = var4;

         while (var6 > var5 && this.checkRow(var3, var6, var2)) {
            var6--;
         }
      }

      return var2;
   }

   private boolean checkRow(ScriptDocument var1, int var2, Set<Rectangle> var3) {
      try {
         Interval var4 = DocumentUtils.rowAsInterval(var1, var2);
         Rectangle var5 = this.area.getTextPane().modelToView(var4.startsAt());
         if (this.shouldRowBeHighlighted(var1, var4)) {
            var3.add(var5);
            return true;
         }
      } catch (BadLocationException var6) {
         var6.printStackTrace();
      }

      return false;
   }

   private boolean shouldRowBeHighlighted(ScriptDocument var1, Interval var2) {
      if (!this.area.getPartitioner().isWhitespaceDecoratingRegion(var2.startsAt())) {
         return false;
      }

      String var3 = DocumentUtils.getText(var1, var2);
      return var3 != null && var3.trim().isEmpty();
   }

   @Override
   public boolean preRendering(Component var1, Graphics2D var2) {
      return false;
   }
}
