package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.graph2d.GraphNodeEditor;

final class PipelineActivityRenamer extends AbstractNodeRenamer<PipelineActivityNode> {
   private final IntegrationEntity entity;

   public PipelineActivityRenamer(OperationGraphController var1, PipelineActivityNode var2) {
      super(var1, var2);
      Object var3 = var2.getDataObject().getContent();
      this.entity = var3 instanceof IntegrationEntity ? (IntegrationEntity)var3 : null;
   }

   @Override
   public void rename() {
      if (this.entity != null && this.entity.isRenameable()) {
         super.rename();
      }
   }

   @Override
   protected void processNewValue(GraphNodeEditor var1) {
      String var2 = this.entity.getName();
      String var3 = (String)var1.getCellEditorValue();
      if (!var2.equals(var3)) {
         this.applyNewName(this.entity, var3);
      }
   }
}
