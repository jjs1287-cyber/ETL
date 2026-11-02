package org.etlforge.ui.graph2d.node;

import java.awt.Color;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.border.BorderBuilder;

public final class NodeStyle implements Cloneable {
   public static final TextStyle EMPTY_ACTIVITY_TEXT = TextStyle.newColor(TextStyles.DefaultItalic, Color.GRAY);
   private Border border;
   private TextStyle contentStyle;
   private Color background;
   private boolean hasDropShadow = true;
   private boolean isSelected;

   public static NodeStyle getDefaultStyle(boolean var0) {
      return var0 ? createDefaultSelectedStyle() : createDefaultUnselectedStyle();
   }

   public NodeStyle() {
   }

   public NodeStyle(Border var1, TextStyle var2, Color var3) {
      this.border = var1;
      this.contentStyle = var2;
      this.background = var3;
   }

   public NodeStyle clone() {
      NodeStyle var1 = new NodeStyle(this.getBorder(), this.getContentStyle(), this.getBackgroundColor());
      var1.setSelected(this.isSelected);
      var1.enableDropShadow(this.hasDropShadow);
      return var1;
   }

   public Border getBorder() {
      if (this.border == null) {
         this.border = this.createDefaultBorder();
      }

      return this.border;
   }

   public TextStyle getContentStyle() {
      if (this.contentStyle == null) {
         this.contentStyle = this.createDefaultContentStyle();
      }

      return this.contentStyle;
   }

   public Color getBackgroundColor() {
      if (this.background == null) {
         this.background = this.createDefaultBackgroundColor();
      }

      return this.background;
   }

   public void setBackgroundColor(Color var1) {
      this.background = var1;
   }

   public void enableDropShadow(boolean var1) {
      this.hasDropShadow = var1;
      if (this.hasDropShadow) {
         this.border = new CompoundBorder(new DropShadowBorder(this.isSelected ? Color.GREEN : Color.BLACK, 10), this.createDefaultBorder());
      } else {
         this.border = this.createDefaultBorder();
      }
   }

   public void setSelected(boolean var1) {
      this.isSelected = var1;
   }

   private Border createDefaultBorder() {
      return new BorderBuilder().line(this.isSelected ? Color.BLACK : Color.GRAY).empty(10);
   }

   private TextStyle createDefaultContentStyle() {
      return TextStyle.newColor(TextStyles.DefaultText, Color.BLACK);
   }

   private Color createDefaultBackgroundColor() {
      return new Color(16119285);
   }

   private static NodeStyle createDefaultSelectedStyle() {
      NodeStyle var0 = new NodeStyle();
      var0.setSelected(true);
      var0.enableDropShadow(true);
      return var0;
   }

   private static NodeStyle createDefaultUnselectedStyle() {
      NodeStyle var0 = new NodeStyle();
      var0.enableDropShadow(true);
      return var0;
   }
}
