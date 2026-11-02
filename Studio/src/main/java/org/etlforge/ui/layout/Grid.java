package org.etlforge.ui.layout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.panel.KongaPanel;

public final class Grid extends AbstractLayoutBuilder implements UiProvider, UiCanvas {
   private final JPanel panel;

   public static Grid oneRow() {
      return oneRow(0);
   }

   public static Grid oneRow(int var0) {
      return new Grid(new KongaPanel(), new GridLayout(1, 0, var0, 0));
   }

   public static Grid oneColumn() {
      return oneColumn(0);
   }

   public static Grid oneColumn(int var0) {
      return new Grid(new KongaPanel(), new GridLayout(0, 1, 0, var0));
   }

   public Grid() {
      this(new KongaPanel());
   }

   public Grid(int var1, int var2) {
      this(new KongaPanel(), new GridLayout(var1, var2));
   }

   public Grid(int var1, int var2, int var3, int var4) {
      this(new KongaPanel(), new GridLayout(var1, var2, var3, var4));
   }

   public Grid(JPanel var1) {
      this(var1, new GridLayout());
   }

   public Grid(JPanel var1, GridLayout var2) {
      this.panel = var1;
      var1.setLayout(var2);
   }

   public Grid add(Component var1) {
      if (var1 != null) {
         this.panel.add(var1);
      }

      return this;
   }

   public Grid add(Object var1) {
      if (var1 instanceof Component) {
         this.add(this.forComponent((Component)var1));
      } else if (var1 instanceof UiProvider) {
         ContainerCanvas.decorate((UiProvider)var1, this.panel);
      } else if (var1 instanceof String) {
         this.add(this.forString((String)var1));
      } else if (var1 instanceof Action) {
         this.add(this.forAction((Action)var1));
      } else if (var1 instanceof Icon) {
         this.add(this.forIcon((Icon)var1));
      } else if (var1 != null) {
         this.add(this.forObject(var1));
      }

      return this;
   }

   public Grid addAll(Component... var1) {
      for (Component var5 : var1) {
         this.add(var5);
      }

      return this;
   }

   public Grid addAll(UiProvider... var1) {
      for (UiProvider var5 : var1) {
         this.add(var5);
      }

      return this;
   }

   public Grid addAll(Action... var1) {
      for (Action var5 : var1) {
         this.add(var5);
      }

      return this;
   }

   public Grid addAll(String... var1) {
      for (String var5 : var1) {
         this.add(var5);
      }

      return this;
   }

   public Grid addAll(Object... var1) {
      for (Object var5 : var1) {
         this.add(var5);
      }

      return this;
   }

   public Grid of(Object... var1) {
      return this.addAll(var1);
   }

   public Grid of(Iterable<?> var1) {
      return this.addAll(var1);
   }

   public Grid addAll(Iterable<?> var1) {
      for (Object var3 : var1) {
         this.add(var3);
      }

      return this;
   }

   public Grid withVGap(int var1) {
      ((GridLayout)this.panel.getLayout()).setVgap(var1);
      return this;
   }

   public Grid withHGap(int var1) {
      ((GridLayout)this.panel.getLayout()).setHgap(var1);
      return this;
   }

   public Grid withBorder(Border var1) {
      this.panel.setBorder(var1);
      return this;
   }

   public Grid withBackground(Color var1) {
      this.panel.setBackground(var1);
      return this;
   }

   public Grid opaque() {
      return this.setOpaque(true);
   }

   public Grid nonOpaque() {
      return this.setOpaque(false);
   }

   private Grid setOpaque(boolean var1) {
      this.panel.setOpaque(var1);
      return this;
   }

   public Grid deeplyNonOpaque() {
      UiUtils.setOpaqueRecursivelySkipDefaults(this.panel, false);
      return this;
   }

   public JPanel container() {
      return this.panel;
   }

   @Override
   public void display(Component var1) {
      this.add(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.panel);
   }

   @Override
   public void requestFocus() {
      int var1 = this.panel.getComponentCount();
      if (var1 == 0) {
         this.panel.requestFocusInWindow();
      } else {
         this.panel.getComponent(0).requestFocusInWindow();
      }
   }

   public Grid withPreferredSize(Dimension var1) {
      this.panel.setPreferredSize(var1);
      return this;
   }

   public Grid setAutomaticallyWrapScrollables(boolean var1) {
      super.setAutomaticallyWrapScrollables(var1);
      return this;
   }

   public Grid removeAll() {
      this.panel.removeAll();
      this.refresh();
      return this;
   }

   public Grid refresh() {
      this.panel.revalidate();
      this.panel.repaint();
      return this;
   }

   public boolean isEmpty() {
      return this.panel.getComponentCount() == 0;
   }
}
