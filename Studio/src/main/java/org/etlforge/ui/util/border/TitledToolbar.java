package org.etlforge.ui.util.border;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.util.List;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import org.etlforge.lang.Arrays2;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.widget.button.ButtonUtils;

public final class TitledToolbar implements UiProvider {
   private final JComponent component;
   private final String title;
   private final List<JComponent> buttons;
   private Border insideBorder;
   private Font titleFont;
   private Color titleColor;

   public TitledToolbar(JComponent var1, String var2) {
      this(var1, var2, new JComponent[0]);
   }

   public TitledToolbar(JComponent var1, String var2, Action... var3) {
      this(var1, var2, toButtons(var3));
   }

   private static JComponent[] toButtons(Action[] var0) {
      return Arrays2.transform(var0, JButton.class, ButtonUtils.ACTION_TO_BUTTON);
   }

   public TitledToolbar(JComponent var1, String var2, JComponent... var3) {
      this.component = var1;
      this.title = var2;
      this.buttons = Lists.newArrayList(var3);
   }

   public void addButton(JComponent var1) {
      this.buttons.add(var1);
   }

   public void setTitleFont(Font var1) {
      this.titleFont = var1;
   }

   public void setTitleColor(Color var1) {
      this.titleColor = var1;
   }

   public void setInsideBorder(Border var1) {
      this.insideBorder = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      UiProvider var2 = this.createToolbar();
      BorderLayoutBuilder var3 = this.doLayout(var2);
      var3.decorate(var1);
   }

   private UiProvider createToolbar() {
      JLabel var1 = this.createTitleLabel();
      int var2 = new TitledToolbar.Helper().getLeftMargin(var1);
      return Horizontal.box().bottomAligned().nonOpaque().rigid(var2).add(var1).glue().addAllWithSpace(10, this.buttons);
   }

   private JLabel createTitleLabel() {
      JLabel var1 = new JLabel(this.title);
      if (this.titleFont != null) {
         var1.setFont(this.titleFont);
      }

      if (this.titleColor != null) {
         var1.setForeground(this.titleColor);
      }

      return var1;
   }

   private BorderLayoutBuilder doLayout(UiProvider var1) {
      Border var2 = this.getComponentBorder();
      this.component.setBorder(var2);
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 2);
      var3.north(var1).center(this.component).nonOpaque();
      return var3;
   }

   private Border getComponentBorder() {
      BorderBuilder var1 = new BorderBuilder();
      var1.titled(null);
      var1.add(this.insideBorder);
      return var1;
   }

   @Override
   public void requestFocus() {
      this.component.requestFocusInWindow();
   }

   private static final class Helper extends TitledBorder {
      public Helper() {
         super("");
      }

      public int getLeftMargin(JComponent var1) {
         int var2 = 7;
         Border var3 = this.getBorder();
         if (var3 != null) {
            Insets var4 = var3.getBorderInsets(var1);
            var2 += var4.left;
         }

         return var2;
      }
   }
}
