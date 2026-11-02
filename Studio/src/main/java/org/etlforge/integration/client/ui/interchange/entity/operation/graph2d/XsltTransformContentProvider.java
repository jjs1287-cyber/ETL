package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.XsltTransform;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.callback.Receiver;

public final class XsltTransformContentProvider extends EntityContentProvider<XsltTransform> {
   public XsltTransformContentProvider(ContentProviderContext<XsltTransform> var1, IntegrationProject var2, Receiver<XsltTransform> var3) {
      super(var1, var2, new XsltTransformContentProvider.FilterProviderImpl(), EntityType.XsltTransform, XsltTransform.class, var3);
   }

   private static class FilterProviderImpl implements EntityFilterProvider<XsltTransform> {
      private FilterProviderImpl() {
      }

      @Override
      public EntityFilter getFilter(PipelineActivity<XsltTransform> var1) {
         return EntityFilter.ACCEPT_ALL;
      }
   }
}
