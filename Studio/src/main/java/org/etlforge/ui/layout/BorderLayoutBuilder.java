package org.etlforge.ui.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.RootPaneContainer;
import javax.swing.border.Border;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.panel.KongaPanel;

public final class BorderLayoutBuilder extends AbstractLayoutBuilder implements UiProvider, UiCanvas {
   private final JComponent panel;

   public static BorderLayoutBuilder from(Container var0) {
      return from(var0, 0, 0);
   }

   public static BorderLayoutBuilder from(Container var0, int var1, int var2) {
      if (!(var0 instanceof JComponent)) {
         throw new IllegalArgumentException("c must be a JComponent (was " + var0 + ")");
      } else {
         return new BorderLayoutBuilder((JComponent)var0, var1, var2);
      }
   }

   public BorderLayoutBuilder() {
      this(0, 0);
   }

   public BorderLayoutBuilder(Object var1) {
      this(0, 0);
      this.center(var1);
   }

   public BorderLayoutBuilder(int var1, int var2) {
      this.panel = new KongaPanel(new BorderLayout(var1, var2));
   }

   private BorderLayoutBuilder(JComponent var1, int var2, int var3) {
      this.panel = var1;
      var1.setLayout(new BorderLayout(var2, var3));
   }

   public static JPanel fillWith(Object var0) {
      return (JPanel)new BorderLayoutBuilder().center(var0).setOpaque(false).container();
   }

   public static JPanel fillWith(Object var0, int var1, int var2) {
      return (JPanel)new BorderLayoutBuilder(var1, var2).center(var0).setOpaque(false).container();
   }

   public BorderLayoutBuilder north(Object var1) {
      this.add(var1, "North");
      return this;
   }

   public BorderLayoutBuilder west(Object var1) {
      this.add(var1, "West");
      return this;
   }

   public BorderLayoutBuilder east(Object var1) {
      this.add(var1, "East");
      return this;
   }

   public BorderLayoutBuilder center(Object var1) {
      this.add(var1, "Center");
      return this;
   }

   public BorderLayoutBuilder south(Object var1) {
      this.add(var1, "South");
      return this;
   }

   public BorderLayoutBuilder northToSouth(Object var1, Object var2, Object var3) {
      this.north(var1);
      this.center(var2);
      this.south(var3);
      return this;
   }

   public BorderLayoutBuilder removeAll() {
      this.panel.removeAll();
      return this;
   }

   public BorderLayoutBuilder removeNorth() {
      return this.removeComponent("North");
   }

   public BorderLayoutBuilder removeCenter() {
      return this.removeComponent("Center");
   }

   public BorderLayoutBuilder removeSouth() {
      return this.removeComponent("South");
   }

   public BorderLayoutBuilder removeWest() {
      return this.removeComponent("West");
   }

   public BorderLayoutBuilder removeEast() {
      return this.removeComponent("East");
   }

   private BorderLayoutBuilder removeComponent(String var1) {
      BorderLayout var2 = (BorderLayout)this.panel.getLayout();
      Component var3 = var2.getLayoutComponent(var1);
      if (var3 != null) {
         this.panel.remove(var3);
      }

      return this;
   }

   @Override
   public JComponent container() {
      return this.panel;
   }

   public BorderLayoutBuilder withEmptyBorder(int var1, int var2, int var3, int var4) {
      return this.withBorder(Empty.border(var1, var2, var3, var4));
   }

   public BorderLayoutBuilder withEmptyBorder(int var1) {
      return this.withBorder(Empty.border(var1));
   }

   public BorderLayoutBuilder withBorder(Border var1) {
      this.panel.setBorder(var1);
      return this;
   }

   public BorderLayoutBuilder withBackground(Color var1) {
      this.panel.setBackground(var1);
      return this;
   }

   public BorderLayoutBuilder opaque() {
      return this.setOpaque(true);
   }

   public BorderLayoutBuilder setOpaque(boolean var1) {
      this.panel.setOpaque(var1);
      return this;
   }

   public BorderLayoutBuilder nonOpaque() {
      return this.setOpaque(false);
   }

   public BorderLayoutBuilder deeplyNonOpaque() {
      UiUtils.setOpaqueRecursivelySkipDefaults(this.panel, false);
      return this;
   }

   public BorderLayoutBuilder withPreferredSize(Dimension var1) {
      this.panel.setPreferredSize(var1);
      return this;
   }

   public BorderLayoutBuilder withHGap(int var1) {
      ((BorderLayout)this.panel.getLayout()).setHgap(var1);
      return this;
   }

   public BorderLayoutBuilder withVGap(int var1) {
      ((BorderLayout)this.panel.getLayout()).setVgap(var1);
      return this;
   }

   public void addToParent(Container var1, Object var2) {
      var1.add(this.panel, var2);
   }

   @Override
   public void display(Component var1) {
      this.center(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.panel);
   }

   @Override
   public void requestFocus() {
      this.panel.requestFocusInWindow();
   }

   public void remove(Component var1) {
      this.panel.remove(var1);
   }

   public void asContentPaneFor(RootPaneContainer var1) {
      var1.setContentPane(this.panel);
   }

   private void add(Object var1, Object var2) {
      if (var1 instanceof Component) {
         this.panel.add(this.forComponent((Component)var1), var2);
      } else if (var1 instanceof UiProvider) {
         ContainerCanvas.decorate((UiProvider)var1, this.panel, var2);
      } else if (var1 instanceof String) {
         this.panel.add(this.forString((String)var1), var2);
      } else if (var1 instanceof Action) {
         this.panel.add(this.forAction((Action)var1), var2);
      } else if (var1 instanceof Icon) {
         this.panel.add(this.forIcon((Icon)var1), var2);
      } else if (var1 != null) {
         this.panel.add(this.forObject(var1), var2);
      }
   }

   @Override
   protected JComponent forAction(Action var1) {
      return InvisiblePanel.of(var1);
   }

   @Override
   protected Component forComponent(Component var1) {
      return var1 instanceof JButton ? InvisiblePanel.of(var1) : super.forComponent(var1);
   }

   public JScrollPane inScrollPane() {
      return new KongaScrollPane(this.container());
   }

   public void removePosition(Object var1) {
      Component var2 = ((BorderLayout)this.panel.getLayout()).getLayoutComponent(var1);
      if (var2 != null) {
         this.panel.remove(var2);
         this.refreshLayout();
      }
   }

   public void refreshLayout() {
      UiUtils.revalidate(this.panel);
   }

   public final boolean isEmpty() {
      return this.panel.getComponentCount() == 0;
   }
}
