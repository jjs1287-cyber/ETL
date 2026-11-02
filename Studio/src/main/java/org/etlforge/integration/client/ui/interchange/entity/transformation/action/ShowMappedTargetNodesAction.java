package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;

public final class ShowMappedTargetNodesAction extends AbstractShowMappedNodesAction {
   public static final String ID = "action.ic.tx.ShowMappedTargetNodes";

   public ShowMappedTargetNodesAction(TransformationPage var1) {
      super("action.ic.tx.ShowMappedTargetNodes", var1);
   }

   @Override
   protected TreeComponent getTreeComponent(TransformationPage var1) {
      return var1.getTreeMapper().m_targetTreeComponent;
   }
}
