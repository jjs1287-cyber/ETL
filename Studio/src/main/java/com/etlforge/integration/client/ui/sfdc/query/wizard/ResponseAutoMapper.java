package com.etlforge.integration.client.ui.sfdc.query.wizard;

import org.etlforge.integration.client.connect.mapper.AbstractAutoMappingJob;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;

final class ResponseAutoMapper extends AbstractAutoMappingJob {
   public ResponseAutoMapper(TransformationPage var1) {
      super(var1);
   }

   @Override
   protected Node getSourceNode(TreeMapper var1) {
      Node var2 = var1.m_sourceTreeComponent.getSalesForceNode();
      assert var2 != null : "The transformation source tree should have a query object node";
      return var2;
   }

   @Override
   protected Node getTargetNode(TreeMapper var1) {
      return this.getStructureRootNode(var1.m_targetTreeComponent);
   }
}
