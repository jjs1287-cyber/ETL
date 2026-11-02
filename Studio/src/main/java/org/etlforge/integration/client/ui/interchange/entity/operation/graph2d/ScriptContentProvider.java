package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.callback.Receiver;

public final class ScriptContentProvider extends EntityContentProvider<Script> {
   public ScriptContentProvider(ContentProviderContext<Script> var1, IntegrationProject var2, Receiver<Script> var3) {
      super(var1, var2, new ScriptContentProvider.FilterProviderImpl(), EntityType.Script, Script.class, var3);
   }

   private static class FilterProviderImpl implements EntityFilterProvider<Script> {
      private FilterProviderImpl() {
      }

      @Override
      public EntityFilter getFilter(PipelineActivity<Script> var1) {
         return EntityFilter.ACCEPT_ALL;
      }
   }
}
