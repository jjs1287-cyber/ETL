package org.etlforge.ui.style;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.util.FontUtils;
import org.etlforge.ui.widget.Label;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.label.LabelUtils;
import org.etlforge.ui.widget.text.TextDecorator;

public class TextStyle implements Cloneable {
   private Color color;
   private final String[] fontFamilies;
   private int fontStyle;
   private int fontSize;
   private Font font;
   private TextDecorator textDecorator;
   private LabelDecorator labelDecorator;

   public TextStyle(Component var1) {
      this.color = var1.getForeground();
      this.font = var1.getFont();
      this.fontFamilies = new String[]{this.font.getFamily()};
      this.fontStyle = this.font.getStyle();
      this.fontSize = this.font.getSize();
   }

   public TextStyle(String var1, int var2) {
      this(var1, var2, 0, defaultTextColor());
   }

   public static TextStyle newColor(TextStyle var0, Color var1) {
      TextStyle var2 = var0.clone();
      var2.color = var1;
      return var2;
   }

   public static TextStyle newFontFamily(TextStyle var0, String var1) {
      Font var2 = var0.font;
      return new TextStyle(var1, var2.getSize(), var2.getStyle(), var0.color);
   }

   public TextStyle(Font var1, Color var2) {
      this(var1.getFamily(), var1.getSize(), var1.getStyle(), var2);
   }

   public TextStyle(String var1, int var2, int var3) {
      this(var1, var2, var3, defaultTextColor());
   }

   private static Color defaultTextColor() {
      Color color = javax.swing.UIManager.getColor("Label.foreground");
      return color == null ? Color.BLACK : color;
   }

   public TextStyle(String var1, int var2, int var3, Color var4) {
      this.fontFamilies = var1.split(",");
      this.fontStyle = var3;
      this.fontSize = var2;
      this.color = var4;
      this.font = this.createFont();
   }

   public static TextStyle newSize(TextStyle var0, int var1) {
      TextStyle var2 = var0.clone();
      var2.fontSize = var1;
      var2.font = var2.createFont();
      return var2;
   }

   public static TextStyle adjustSize(TextStyle var0, int var1) {
      return newSize(var0, var0.fontSize + var1);
   }

   public static TextStyle newStyle(TextStyle var0, int var1) {
      TextStyle var2 = var0.clone();
      var2.fontStyle = var1;
      var2.font = var2.createFont();
      return var2;
   }

   public TextStyle withColor(Color var1) {
      return newColor(this, var1);
   }

   public TextStyle withStyle(int var1) {
      return newStyle(this, var1);
   }

   private Font createFont() {
      Font var1 = FontUtils.getMatchingFont(this.fontFamilies, this.fontSize, this.fontStyle);
      if (var1 == null) {
         var1 = new JLabel().getFont();
         var1 = var1.deriveFont(this.fontStyle, this.fontSize);
      }

      return var1;
   }

   public Font getFont() {
      return this.font;
   }

   public int getFontSize() {
      return this.fontSize;
   }

   public Color getColor() {
      return this.color;
   }

   public KongaLabel makeLabel(String var1) {
      KongaLabel var2 = new KongaLabel(var1);
      this.makeOver(var2);
      return var2;
   }

   public KongaLabel makeLabel(MnemonicString var1) {
      KongaLabel var2 = this.makeLabel(var1.getText());
      LabelUtils.applyMnemonics(var2, var1);
      return var2;
   }

   public <T extends JLabel> T makeOver(T var1) {
      var1.setFont(this.font);
      var1.setForeground(this.color);
      return (T)var1;
   }

   public void makeOver(JLabel var1, JLabel... var2) {
      this.makeOver(var1);

      for (JLabel var6 : var2) {
         this.makeOver(var6);
      }
   }

   public void makeOverAll(JLabel... var1) {
      for (JLabel var5 : var1) {
         this.makeOver(var5);
      }
   }

   public void makeOver(JTextComponent... var1) {
      for (JTextComponent var5 : var1) {
         var5.setFont(this.font);
         var5.setForeground(this.color);
      }
   }

   public void makeOver(JTable var1) {
      var1.setFont(this.font);
      var1.setForeground(this.color);
   }

   public void makeOver(AbstractButton... var1) {
      for (AbstractButton var5 : var1) {
         var5.setFont(this.font);
         var5.setForeground(this.color);
      }
   }

   public void makeOver(JComboBox var1) {
      var1.setFont(this.font);
      var1.setForeground(this.color);
   }

   public void makeOver(JList var1) {
      var1.setFont(this.font);
      var1.setForeground(this.color);
   }

   public void makeOver(JTabbedPane var1) {
      var1.setFont(this.font);
      var1.setForeground(this.color);
   }

   public void makeOver(JTree var1) {
      var1.setFont(this.font);
      var1.setForeground(this.color);
   }

   public void makeOver(Graphics var1) {
      var1.setFont(this.font);
      var1.setColor(this.color);
   }

   public TextDecorator asTextDecorator() {
      if (this.textDecorator == null) {
         this.textDecorator = new TextDecorator() {
            @Override
            public Font getFont() {
               return TextStyle.this.font;
            }

            @Override
            public Color getTextColor() {
               return TextStyle.this.color;
            }
         };
      }

      return this.textDecorator;
   }

   public LabelDecorator asLabelDecorator(final Icon var1) {
      if (this.labelDecorator == null) {
         this.labelDecorator = new LabelDecorator() {
            @Override
            public void decorate(Label var1x) {
               var1x.setFont(TextStyle.this.font);
               var1x.setTextColor(TextStyle.this.color);
               var1x.setIcon(var1);
            }
         };
      }

      return this.labelDecorator;
   }

   protected TextStyle clone() {
      try {
         TextStyle var1 = (TextStyle)super.clone();
         var1.textDecorator = null;
         var1.labelDecorator = null;
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }
}
