package org.etlforge.ui.layout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.border.Border;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.panel.KongaPanel;

public final class Flow implements UiProvider {
   private final KongaPanel panel;

   private Flow(int var1, int var2, int var3) {
      this.panel = new KongaPanel(new FlowLayout(var1, var2, var3));
   }

   public static Flow invisible() {
      return leading(0, 0);
   }

   public static Flow leading() {
      return leading(5, 5);
   }

   public static Flow leading(int var0, int var1) {
      return new Flow(3, var0, var1);
   }

   public static Flow center() {
      return center(5, 5);
   }

   public static Flow center(int var0, int var1) {
      return new Flow(1, var0, var1);
   }

   public static Flow trailing() {
      return trailing(5, 5);
   }

   public static Flow trailing(int var0, int var1) {
      return new Flow(4, var0, var1);
   }

   public Flow add(Component var1) {
      this.panel.add(var1);
      return this;
   }

   public Flow add(UiProvider var1) {
      ContainerCanvas.decorate(var1, this.panel);
      return this;
   }

   public Flow add(Component... var1) {
      for (Component var5 : var1) {
         this.panel.add(var5);
      }

      return this;
   }

   public Flow add(UiProvider... var1) {
      for (UiProvider var5 : var1) {
         ContainerCanvas.decorate(var5, this.panel);
      }

      return this;
   }

   public Flow add(Iterable<? extends Component> var1) {
      for (Component var3 : var1) {
         this.panel.add(var3);
      }

      return this;
   }

   public Flow strut(int var1) {
      return this.add(Box.createHorizontalStrut(var1));
   }

   public Flow glue() {
      return this.add(Box.createHorizontalGlue());
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.panel);
   }

   public void addToParent(Container var1, Object var2) {
      var1.add(this.panel, var2);
   }

   public KongaPanel container() {
      return this.panel;
   }

   public Flow setBackground(Color var1) {
      this.panel.setBackground(var1);
      return this;
   }

   public Flow useEmptyBorder(int var1, int var2, int var3, int var4) {
      return this.setBorder(Empty.border(var1, var2, var3, var4));
   }

   public Flow setBorder(Border var1) {
      this.panel.setBorder(var1);
      return this;
   }

   public Flow setOpaque(boolean var1) {
      this.panel.setOpaque(var1);
      return this;
   }

   public Flow setOpaqueRecursively(boolean var1) {
      UiUtils.setOpaqueRecursivelySkipDefaults(this.panel, var1);
      return this;
   }

   @Override
   public void requestFocus() {
      this.panel.requestFocus();
   }
}
