package com.etlforge.integration.client.ui.sfdc.ucu.wizard;

import org.etlforge.integration.client.connect.mapper.AbstractAutoMappingJob;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;

public final class RequestAutoMapper extends AbstractAutoMappingJob {
   public RequestAutoMapper(TransformationPage var1) {
      super(var1);
   }

   @Override
   protected Node getSourceNode(TreeMapper var1) {
      return this.getStructureRootNode(var1.m_sourceTreeComponent);
   }

   @Override
   protected Node getTargetNode(TreeMapper var1) {
      Node var2 = var1.m_targetTreeComponent.getSalesForceNode();
      assert var2 != null : "The transformation target tree should have an SFDC object node";
      return var2;
   }
}
