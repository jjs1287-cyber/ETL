package org.etlforge.ui.layout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.panel.KongaPanel;

public abstract class BoxBuilder implements UiProvider, UiCanvas {
   private final JPanel panel;
   private Float alignmentX;
   private Float alignmentY;
   private boolean useSmallSizedIconsForActions;

   protected BoxBuilder() {
      this.panel = new KongaPanel();
      this.panel.setLayout(this.createLayout(this.panel));
   }

   protected BoxBuilder(JPanel var1) {
      this.panel = var1;
      var1.setLayout(this.createLayout(var1));
   }

   public static BoxBuilder horizontal() {
      return new BoxBuilder.HorizontalBoxBuilder();
   }

   public static BoxBuilder horizontal(JPanel var0) {
      return new BoxBuilder.HorizontalBoxBuilder(var0);
   }

   public static BoxBuilder vertical() {
      return new BoxBuilder.VerticalBoxBuilder();
   }

   public static BoxBuilder vertical(JPanel var0) {
      return new BoxBuilder.VerticalBoxBuilder(var0);
   }

   public BoxBuilder add(Component var1) {
      if (var1 != null) {
         if (var1 instanceof JComponent) {
            if (this.alignmentX != null) {
               ((JComponent)var1).setAlignmentX(this.alignmentX);
            }

            if (this.alignmentY != null) {
               ((JComponent)var1).setAlignmentY(this.alignmentY);
            }
         }

         this.panel.add(var1);
      }

      return this;
   }

   public BoxBuilder add(String var1) {
      if (var1 != null) {
         this.add(new JLabel(var1));
      }

      return this;
   }

   public BoxBuilder add(Action var1) {
      if (var1 != null) {
         KongaButton var2 = new KongaButton(var1);
         if (this.useSmallSizedIconsForActions) {
            var2.setUseSmallActionIcon(true);
         }

         this.add(var2);
      }

      return this;
   }

   public BoxBuilder add(Icon var1) {
      if (var1 != null) {
         this.add(new JLabel(var1));
      }

      return this;
   }

   public BoxBuilder add(Object var1) {
      if (var1 instanceof Component) {
         this.add((Component)var1);
      } else if (var1 instanceof UiProvider) {
         this.add((UiProvider)var1);
      } else if (var1 instanceof Action) {
         this.add((Action)var1);
      } else if (var1 instanceof String) {
         this.add((String)var1);
      } else if (var1 instanceof Icon) {
         this.add((Icon)var1);
      } else if (var1 instanceof Iterable) {
         for (Object var3 : (Iterable)var1) {
            this.add(var3);
         }
      } else if (var1 != null) {
         throw new IllegalArgumentException("I do not know how to add a " + var1.getClass());
      }

      return this;
   }

   public BoxBuilder addAll(Iterable<?> var1) {
      for (Object var3 : var1) {
         this.add(var3);
      }

      return this;
   }

   public BoxBuilder of(Object... var1) {
      return this.addAll(var1);
   }

   public BoxBuilder addAll(Object... var1) {
      for (Object var5 : var1) {
         this.add(var5);
      }

      return this;
   }

   public BoxBuilder of(Component... var1) {
      return this.addAll(var1);
   }

   public BoxBuilder addAll(Component... var1) {
      for (Component var5 : var1) {
         this.add(var5);
      }

      return this;
   }

   public BoxBuilder addAllWithSpace(int var1, Component... var2) {
      return this.addAllWithSpace(var1, Arrays.asList(var2));
   }

   public BoxBuilder addAllWithSpace(int var1, Object... var2) {
      return this.addAllWithSpace(var1, Arrays.asList(var2));
   }

   public BoxBuilder addAllWithSpace(int var1, Collection<?> var2) {
      if (var1 <= 0) {
         return this.addAll(var2.toArray(new Object[var2.size()]));
      }

      boolean var3 = true;

      for (Object var5 : var2) {
         if (var5 != null) {
            if (!var3) {
               this.strut(var1);
            }

            this.add(var5);
            var3 = false;
         }
      }

      return this;
   }

   public BoxBuilder add(UiProvider var1) {
      if (var1 != null) {
         var1.decorate(new UiCanvas() {
            @Override
            public void display(Component var1) {
               BoxBuilder.this.add(var1);
            }
         });
      }

      return this;
   }

   public BoxBuilder addAll(UiProvider... var1) {
      for (UiProvider var5 : var1) {
         this.add(var5);
      }

      return this;
   }

   public BoxBuilder strut(int var1) {
      return this.add(this.createStrut(var1));
   }

   public BoxBuilder glue() {
      return this.add(this.createGlue());
   }

   public BoxBuilder rigid(int var1) {
      return this.add(Box.createRigidArea(this.createRigidArea(var1)));
   }

   public BoxBuilder rigid(int var1, int var2) {
      return this.add(Box.createRigidArea(new Dimension(var1, var2)));
   }

   public BoxBuilder removeAll() {
      this.panel.removeAll();
      return this;
   }

   public JPanel container() {
      return this.panel;
   }

   public BoxBuilder withEmptyBorder(int var1, int var2, int var3, int var4) {
      return this.withBorder(Empty.border(var1, var2, var3, var4));
   }

   public BoxBuilder withEmptyBorder(int var1) {
      return this.withBorder(Empty.border(var1));
   }

   public BoxBuilder withBorder(Border var1) {
      this.panel.setBorder(var1);
      return this;
   }

   public BoxBuilder withBackground(Color var1) {
      this.panel.setBackground(var1);
      return this;
   }

   public BoxBuilder nonOpaque() {
      return this.setOpaque(false);
   }

   private BoxBuilder setOpaque(boolean var1) {
      this.panel.setOpaque(var1);
      return this;
   }

   public BoxBuilder deeplyNonOpaque() {
      UiUtils.setOpaqueRecursivelySkipDefaults(this.panel, false);
      return this;
   }

   public void addToParent(Container var1, Object var2) {
      var1.add(this.panel, var2);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.panel);
   }

   @Override
   public void requestFocus() {
      this.panel.requestFocusInWindow();
   }

   @Override
   public void display(Component var1) {
      this.add(var1);
   }

   public BoxBuilder leftAligned() {
      return this.setAlignmentX(0.0F);
   }

   private BoxBuilder setAlignmentX(float var1) {
      this.alignmentX = var1;
      return this;
   }

   public BoxBuilder rightAligned() {
      return this.setAlignmentX(1.0F);
   }

   public BoxBuilder topAligned() {
      return this.setAlignmentY(0.0F);
   }

   private BoxBuilder setAlignmentY(float var1) {
      this.alignmentY = var1;
      return this;
   }

   public BoxBuilder bottomAligned() {
      return this.setAlignmentY(1.0F);
   }

   public BoxBuilder centerAligned() {
      return this instanceof BoxBuilder.HorizontalBoxBuilder ? this.setAlignmentY(0.5F) : this.setAlignmentX(0.5F);
   }

   public BoxBuilder withSmallSizedIconsForActions() {
      this.useSmallSizedIconsForActions = true;
      return this;
   }

   public final JPanel invisible() {
      return InvisiblePanel.of(this.panel);
   }

   public final KongaScrollPane inScroll() {
      return new KongaScrollPane(this.panel);
   }

   protected abstract BoxLayout createLayout(JPanel var1);

   protected abstract Component createStrut(int var1);

   protected abstract Component createGlue();

   protected abstract Dimension createRigidArea(int var1);

   private static class HorizontalBoxBuilder extends BoxBuilder {
      public HorizontalBoxBuilder() {
      }

      public HorizontalBoxBuilder(JPanel var1) {
         super(var1);
      }

      @Override
      protected BoxLayout createLayout(JPanel var1) {
         return new BoxLayout(var1, 2);
      }

      @Override
      protected Component createGlue() {
         return Box.createHorizontalGlue();
      }

      @Override
      protected Component createStrut(int var1) {
         return Box.createHorizontalStrut(var1);
      }

      @Override
      protected Dimension createRigidArea(int var1) {
         return new Dimension(var1, 0);
      }
   }

   private static class VerticalBoxBuilder extends BoxBuilder {
      public VerticalBoxBuilder() {
      }

      public VerticalBoxBuilder(JPanel var1) {
         super(var1);
      }

      @Override
      protected BoxLayout createLayout(JPanel var1) {
         return new BoxLayout(var1, 3);
      }

      @Override
      protected Component createGlue() {
         return Box.createVerticalGlue();
      }

      @Override
      protected Component createStrut(int var1) {
         return Box.createVerticalStrut(var1);
      }

      @Override
      protected Dimension createRigidArea(int var1) {
         return new Dimension(0, var1);
      }
   }
}
