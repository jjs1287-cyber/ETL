package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EntityActivityNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.graph.node.AbstractGraphNode;

public final class LocateInTreeAction extends AbstractAction {
   private final IntegrationEntity entity;

   public LocateInTreeAction(AbstractGraphNode var1) {
      super(Strings.get("LocateInTree.Name"));
      this.entity = this.getEntity(var1);
      this.setEnabled(this.entity != null && this.entity.isEditable());
      this.putValue("ShortDescription", Strings.get("LocateInTree.ToolTip"));
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      if (this.entity != null) {
         this.locateInTree();
      }
   }

   private void locateInTree() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            UiCompanion.of(LocateInTreeAction.this.entity).displayInTree();
         }
      });
   }

   private IntegrationEntity getEntity(AbstractGraphNode var1) {
      if (var1 instanceof OperationWrapperNode) {
         return ((OperationWrapperNode)var1).getDataObject().getActivity();
      } else if (var1 instanceof EmailWrapperNode) {
         return ((EmailWrapperNode)var1).getDataObject().getActivity();
      } else if (var1 instanceof EntityActivityNode) {
         Object var2 = ((EntityActivityNode)var1).getDataObject().getContent();
         return (IntegrationEntity)var2;
      } else {
         return null;
      }
   }
}
