package com.etlforge.integration.client.ui.sfdc.delete.wizard;

import org.etlforge.integration.client.connect.mapper.AbstractAutoMappingJob;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;

final class RequestAutoMapper extends AbstractAutoMappingJob {
   public RequestAutoMapper(TransformationPage var1) {
      super(var1);
   }

   @Override
   protected Node getSourceNode(TreeMapper var1) {
      return this.getStructureRootNode(var1.m_sourceTreeComponent);
   }

   @Override
   protected Node getTargetNode(TreeMapper var1) {
      return var1.m_targetTreeComponent.findNode("root$transaction.body$delete$ids.");
   }
}
