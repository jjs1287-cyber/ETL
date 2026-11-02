package org.etlforge.ui.graph2d;

import java.awt.event.ActionEvent;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.util.KeyUtils;

public class ToggleNodeExpansionAction extends AbstractSelectionAction {
   private final Graph2D graph;

   public ToggleNodeExpansionAction(Graph2D var1) {
      this.graph = var1;
      this.putValue("AcceleratorKey", KeyUtils.SPACE);
   }

   public ToggleNodeExpansionAction(Graph2D var1, Object var2) {
      this(var1);
      this.setSelection(SelectionFactory.newOrderedSelection(var2));
   }

   @Override
   public final void actionPerformed(ActionEvent var1) {
      Object var2 = this.getNodeToToggle();
      if (var2 != null) {
         this.toggle(var2);
      }
   }

   protected void toggle(Object var1) {
      this.graph.toggleExpansion(var1, true);
   }

   @Override
   protected final void handleSelectionChange() {
      Object var1 = this.getNodeToToggle();
      if (var1 != null) {
         this.enableMe(var1);
      } else {
         this.disableMe();
      }
   }

   private void enableMe(Object var1) {
      this.setEnabled(true);
      if (this.graph.getLayout().isExpanded(var1)) {
         this.putValue("Name", "Collapse");
      } else {
         this.putValue("Name", "Expand");
      }
   }

   private void disableMe() {
      this.putValue("Name", "Expand/Collapse");
      this.setEnabled(false);
   }

   private Object getSelectedNode() {
      return ((OrderedSelection)this.getSelection()).head();
   }

   private Object getNodeToToggle() {
      Object var1 = this.getSelectedNode();

      while (!this.graph.getModel().isGroup(var1) && var1 != null) {
         var1 = this.graph.getModel().getParentOf(var1);
      }

      return var1;
   }
}
