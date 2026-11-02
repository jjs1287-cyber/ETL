package org.etlforge.ui.layout;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.split.KongaSplitPane;

public final class Vertical {
   public static Vertical.VerticalBorderLayoutBuilder borderLayout() {
      return new Vertical.VerticalBorderLayoutBuilder();
   }

   public static Vertical.VerticalBorderLayoutBuilder borderLayout(int var0) {
      return new Vertical.VerticalBorderLayoutBuilder(var0);
   }

   public static Vertical.VerticalSplitPaneBuilder splitPane() {
      return new Vertical.VerticalSplitPaneBuilder();
   }

   public static BoxBuilder box() {
      return BoxBuilder.vertical();
   }

   public static Grid grid() {
      return Grid.oneColumn();
   }

   public static Grid grid(int var0) {
      return Grid.oneColumn(var0);
   }

   public static OneColumnPanel column() {
      return new OneColumnPanel();
   }

   private Vertical() {
   }

   public static final class Anchor {
      public static BorderLayoutBuilder north(Object var0) {
         return new BorderLayoutBuilder().nonOpaque().north(var0);
      }

      private Anchor() {
      }
   }

   public static final class VerticalBorderLayoutBuilder implements UiProvider {
      private final BorderLayoutBuilder layout;

      public VerticalBorderLayoutBuilder() {
         this(0);
      }

      public VerticalBorderLayoutBuilder(int var1) {
         this.layout = new BorderLayoutBuilder(0, var1);
      }

      public BorderLayoutBuilder layout() {
         return this.layout;
      }

      public JPanel container() {
         return (JPanel)this.layout.container();
      }

      public JPanel invisible() {
         return this.layout.invisible();
      }

      public Vertical.VerticalBorderLayoutBuilder of(Object... var1) {
         ArgCheck.checkInRange(var1.length, 1, 3);
         this.layout.north(var1[0]);
         if (var1.length > 1) {
            this.layout.center(var1[1]);
            if (var1.length == 3) {
               this.layout.south(var1[2]);
            }
         }

         return this;
      }

      public Vertical.VerticalBorderLayoutBuilder withBorder(Border var1) {
         this.layout.withBorder(var1);
         return this;
      }

      public Vertical.VerticalBorderLayoutBuilder withEmptyBorder(int var1) {
         this.layout.withEmptyBorder(var1);
         return this;
      }

      public Vertical.VerticalBorderLayoutBuilder withBackground(Color var1) {
         this.layout.withBackground(var1);
         return this;
      }

      public Vertical.VerticalBorderLayoutBuilder withGap(int var1) {
         this.layout.withVGap(var1);
         return this;
      }

      public Vertical.VerticalBorderLayoutBuilder deeplyNonOpaque() {
         this.layout.deeplyNonOpaque();
         return this;
      }

      public Vertical.VerticalBorderLayoutBuilder withCaption(Object var1, String var2) {
         return this.of(var2, var1).withGap(3);
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.layout.requestFocus();
      }
   }

   public static final class VerticalSplitPaneBuilder {
      public KongaSplitPane of(JComponent var1, JComponent var2) {
         return KongaSplitPane.verticalSplit(var1, var2);
      }

      public KongaSplitPane of(UiProvider var1, UiProvider var2) {
         return KongaSplitPane.verticalSplit(var1, var2);
      }
   }
}
