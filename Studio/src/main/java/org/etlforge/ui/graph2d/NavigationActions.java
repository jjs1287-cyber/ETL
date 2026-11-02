package org.etlforge.ui.graph2d;

import java.awt.event.ActionEvent;
import java.util.Collections;
import javax.swing.Action;
import org.etlforge.ui.graph.NavigationOrder;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;

final class NavigationActions {
   public static Action forward(Graph2D var0) {
      return new NavigationActions.NavigationAction(var0, 1);
   }

   public static Action backward(Graph2D var0) {
      return new NavigationActions.NavigationAction(var0, -1);
   }

   private NavigationActions() {
   }

   private static final class NavigationAction extends AbstractSingleObjectSelectionAction<Object> {
      private final Graph2D graph;
      private final int direction;

      private NavigationAction(Graph2D var1, int var2) {
         super(Object.class);
         this.graph = var1;
         this.direction = var2;
      }

      @Override
      protected void run(Object var1, ActionEvent var2) {
         NavigationOrder var3 = this.graph.getNavigationOrder();
         if (var3 != null) {
            Object var4 = this.direction > 0 ? var3.next(var1) : var3.previous(var1);
            if (var4 != null && var4 != var1) {
               this.graph.getSelectionModel().setSelection(Collections.singletonList(var4));
               this.graph.getLayout().moveToTop(var4);
               this.graph.ensureNodeIsVisible(var4);
            }
         }
      }
   }
}
