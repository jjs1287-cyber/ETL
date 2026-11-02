package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.NoStructure;

public final class ShowMappedSourceNodesAction extends AbstractShowMappedNodesAction {
   public static final String ID = "action.ic.tx.ShowMappedSourceNodes";

   public ShowMappedSourceNodesAction(TransformationPage var1) {
      super("action.ic.tx.ShowMappedSourceNodes", var1);
      this.setEnabled(this.checkEnabled());
   }

   private boolean checkEnabled() {
      DataStructure var1 = this.page.getObject().getInputStructure();
      return !(var1 instanceof NoStructure);
   }

   @Override
   protected TreeComponent getTreeComponent(TransformationPage var1) {
      return var1.getTreeMapper().m_sourceTreeComponent;
   }
}
