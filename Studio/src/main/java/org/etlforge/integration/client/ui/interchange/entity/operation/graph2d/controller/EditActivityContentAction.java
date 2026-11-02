package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.operation.pipeline.EntityActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

final class EditActivityContentAction extends AbstractAction {
   private final PipelineActivityNode node;

   public EditActivityContentAction(PipelineActivityNode var1) {
      this.node = var1;
      this.putValue("Name", this.createName());
      this.setEnabled(this.isEditable());
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            EntityEditorLauncher.openEditor(EditActivityContentAction.this.node);
         }
      });
   }

   private String createName() {
      PipelineActivity var1 = this.node.getDataObject();
      if (var1 instanceof EntityActivity && !var1.isEmpty()) {
         EntityActivity var2 = (EntityActivity)var1;
         return "Edit " + ((IntegrationEntity)var2.getContent()).getName() + "...";
      } else {
         return "Edit...";
      }
   }

   private boolean isEditable() {
      PipelineActivity var1 = this.node.getDataObject();
      Object var2 = var1.getContent();
      return var2 instanceof IntegrationEntity && ((IntegrationEntity)var2).isEditable();
   }
}
