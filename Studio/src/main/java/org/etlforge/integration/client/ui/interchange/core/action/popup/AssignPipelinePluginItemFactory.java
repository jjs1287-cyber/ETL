package org.etlforge.integration.client.ui.interchange.core.action.popup;

import com.google.common.base.Predicate;
import javax.swing.Action;
import org.etlforge.application.ui.action.popup.SingleItemPopupMenuFactory;
import org.etlforge.integration.client.plugin.pipeline.PipelinePluginSpecification;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class AssignPipelinePluginItemFactory extends SingleItemPopupMenuFactory {
   public AssignPipelinePluginItemFactory(Action var1) {
      super(var1, new AssignPipelinePluginItemFactory.Filter());
   }

   private static class Filter implements Predicate<Object> {
      private Filter() {
      }

      @Override
      public boolean apply(Object var1) {
         return var1 instanceof IntegrationEntity ? this.isEntitySupported((IntegrationEntity)var1) : false;
      }

      private boolean isEntitySupported(IntegrationEntity var1) {
         PipelinePluginSpecification var2 = PipelinePluginSpecification.getLatest();
         return var2.isEntitySupported(var1);
      }
   }
}
