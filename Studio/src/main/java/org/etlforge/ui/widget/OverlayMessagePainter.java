package org.etlforge.ui.widget;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.UiUtils;

public class OverlayMessagePainter extends PostRenderingComponentPainter {
   private String message;
   private Font font;
   private Color color;
   private Point2D location;

   public OverlayMessagePainter(String var1) {
      this.setMessage(var1);
   }

   public final void setMessage(String var1) {
      this.message = Preconditions.checkNotNull(var1);
   }

   public final String getMessage() {
      return this.message;
   }

   public void setFont(Font var1) {
      this.font = var1;
   }

   public void setColor(Color var1) {
      this.color = var1;
   }

   public void setTextStyle(TextStyle var1) {
      if (var1 != null) {
         this.font = var1.getFont();
         this.color = var1.getColor();
      } else {
         this.font = null;
         this.color = null;
      }
   }

   public void setLocation(Point2D var1) {
      this.location = var1;
   }

   @Override
   public void postRendering(Component var1, Graphics2D var2) {
      Font var3 = var2.getFont();
      Color var4 = var2.getColor();
      RenderingHints var5 = var2.getRenderingHints();
      var2.setFont(this.font != null ? this.font : var1.getFont());
      var2.setColor(this.color != null ? this.color : var1.getForeground());
      UiUtils.makeAntiAliased(var2);
      Point2D var6 = this.getLocation(var1, var2);
      var2.drawString(this.message, (float)var6.getX(), (float)var6.getY());
      var2.setFont(var3);
      var2.setColor(var4);
      var2.setRenderingHints(var5);
   }

   protected Point2D getLocation(Component var1, Graphics2D var2) {
      if (this.location != null) {
         return this.location;
      }

      Rectangle2D var3 = var2.getFontMetrics().getStringBounds(this.message, var2);
      return new Double((var1.getWidth() - var3.getWidth()) / 2.0, (var1.getHeight() - var3.getHeight()) / 2.0);
   }
}
