package org.etlforge.integration.client.ui.entity.tree;

import org.etlforge.integration.client.ui.entity.EntityPathPublisher;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.event.KongaTreeAdapter;
import org.etlforge.ui.widget.tree.event.KongaTreeEvent;

public final class EntityTreePathPublisher extends KongaTreeAdapter {
   private final EntityPathPublisher impl;

   public EntityTreePathPublisher(IntegrationEntityTree var1) {
      this.impl = new EntityPathPublisher(var1);
      this.impl.setIncludeProject(true);
   }

   @Override
   public void selectionChanged(KongaTreeEvent var1) {
      if (var1.getNumberOfSourceNodes() == 1) {
         this.handleSelection(var1.getSourceNode());
      } else {
         this.handleSelection(null);
      }
   }

   private void handleSelection(KongaTreeNode var1) {
      IntegrationEntity var2 = null;
      if (var1 instanceof IntegrationEntityTreeNode) {
         var2 = ((IntegrationEntityTreeNode)var1).getUserObject();
      }

      this.impl.publish(var2);
   }
}
