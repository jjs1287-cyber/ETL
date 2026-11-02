package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicBoolean;
import org.etlforge.integration.data.entity.transformation.MappingTreeNodePathFilter;

final class MappingNodeFilterChangeReaction implements PropertyChangeListener {
   private final TransformationPage page;
   private final AtomicBoolean armed;

   public MappingNodeFilterChangeReaction(TransformationPage var1) {
      this.page = var1;
      this.armed = new AtomicBoolean(false);
   }

   public void activate() {
      this.armed.set(true);
   }

   @Override
   public void propertyChange(PropertyChangeEvent var1) {
      String var2 = var1.getPropertyName();
      MappingTreeNodePathFilter var3 = (MappingTreeNodePathFilter)var1.getNewValue();
      if ("InputNodeFilter".equals(var2)) {
         if (this.armed.get()) {
            this.page.getTreeMapper().m_sourceTreeComponent.setNodePathFilter(var3);
         }
      } else if ("OutputNodeFilter".equals(var2)) {
         if (this.armed.get()) {
            this.page.getTreeMapper().m_targetTreeComponent.setNodePathFilter(var3);
         }
      } else {
         assert false : "I should be listening to the property " + var2;
      }
   }
}
