package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import com.google.common.collect.Sets;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import javax.swing.KeyStroke;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.util.KeyUtils;

public final class ResetLayoutAction extends AbstractSelectionAction {
   public static final KeyStroke SHORTCUT = KeyUtils.getCtrlDown(116);
   private final OperationGraphController controller;

   public ResetLayoutAction(OperationGraphController var1) {
      super(Strings.get("ResetLayout.Name"));
      this.controller = var1;
      this.putValue("AcceleratorKey", SHORTCUT);
      this.putValue("ShortDescription", Strings.get("ResetLayout.ToolTip"));
      this.setEnabled(true);
   }

   @Override
   protected void handleSelectionChange() {
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      Selection var2 = this.getSelection();
      if (var2.isEmpty()) {
         this.resetGraphLayout();
      } else {
         this.resetNodeLayouts(var2);
      }
   }

   private void resetGraphLayout() {
      this.controller.resetLayout();
   }

   private void resetNodeLayouts(Selection var1) {
      HashSet var2 = Sets.newHashSet();

      for (Object var4 : var1.items()) {
         OperationWrapperNode var5 = null;
         if (var4 instanceof OperationWrapperNode) {
            var5 = (OperationWrapperNode)var4;
         } else if (var4 instanceof PipelineActivityNode) {
            var5 = ((PipelineActivityNode)var4).getParent();
         }

         if (var5 != null) {
            var2.add(var5);
         }
      }

      for (Object __etlItem59 : var2) {
         OperationWrapperNode var7 = (OperationWrapperNode) __etlItem59;
         this.controller.resetOperationNodeLayout(var7);
      }
   }
}
