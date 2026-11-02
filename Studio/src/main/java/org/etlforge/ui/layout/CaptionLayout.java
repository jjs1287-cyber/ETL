package org.etlforge.ui.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.border.Border;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

public final class CaptionLayout implements UiProvider {
   private final BorderLayoutBuilder impl = new BorderLayoutBuilder(0, 5);

   public static CaptionLayout of(Object var0, Object var1) {
      return new CaptionLayout(var0, var1);
   }

   public CaptionLayout(Object var1, Object var2) {
      this.impl.north(var1).center(var2);
   }

   public CaptionLayout withGap(int var1) {
      this.impl.withVGap(var1);
      return this;
   }

   public CaptionLayout withBorder(Border var1) {
      this.impl.withBorder(var1);
      return this;
   }

   public CaptionLayout withBackground(Color var1) {
      this.impl.withBackground(var1);
      return this;
   }

   public CaptionLayout nonOpaque() {
      this.impl.nonOpaque();
      return this;
   }

   public CaptionLayout deeplyNonOpaque() {
      this.impl.deeplyNonOpaque();
      return this;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.impl.decorate(var1);
   }

   @Override
   public void requestFocus() {
      BorderLayout var1 = (BorderLayout)this.impl.container().getLayout();
      Component var2 = var1.getLayoutComponent("Center");
      var2.requestFocusInWindow();
   }

   public CaptionLayout addToParent(Container var1, Object var2) {
      this.impl.addToParent(var1, var2);
      return this;
   }

   public JComponent container() {
      return this.impl.container();
   }
}
