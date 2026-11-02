package org.etlforge.ui.canvas;

import java.awt.Component;
import javax.swing.JSplitPane;
import org.etlforge.ui.UiCanvas;

public final class SplitPaneCanvas implements UiCanvas {
   private final JSplitPane splitPane;
   private final SplitPaneCanvas.Position position;

   public static UiCanvas left(JSplitPane var0) {
      return new SplitPaneCanvas(var0, SplitPaneCanvas.Position.LEFT);
   }

   public static UiCanvas top(JSplitPane var0) {
      return new SplitPaneCanvas(var0, SplitPaneCanvas.Position.TOP);
   }

   public static UiCanvas right(JSplitPane var0) {
      return new SplitPaneCanvas(var0, SplitPaneCanvas.Position.RIGHT);
   }

   public static UiCanvas bottom(JSplitPane var0) {
      return new SplitPaneCanvas(var0, SplitPaneCanvas.Position.BOTTOM);
   }

   private SplitPaneCanvas(JSplitPane var1, SplitPaneCanvas.Position var2) {
      this.position = var2;
      this.splitPane = var1;
   }

   @Override
   public void display(Component var1) {
      this.position.add(this.splitPane, var1);
   }

   private enum Position {
      LEFT,
      RIGHT,
      TOP,
      BOTTOM;

      public void add(JSplitPane var1, Component var2) {
         switch (this) {
            case LEFT:
               var1.setLeftComponent(var2);
               break;
            case RIGHT:
               var1.setRightComponent(var2);
               break;
            case TOP:
               var1.setTopComponent(var2);
               break;
            case BOTTOM:
               var1.setBottomComponent(var2);
         }
      }
   }
}
