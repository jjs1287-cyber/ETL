package org.etlforge.integration.client.ui.interchange.cli;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.interchange.core.action.DeleteConfirmer;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.DeleteRequestHandler;

public final class DeletePathExecutor extends AbstractProjectPathCommandExecutor {
   private final DeleteRequestHandler handler;

   public DeletePathExecutor(CurrentProjectService var1, DeleteRequestHandler var2) {
      super(var1, EntityFilters.DELETABLE);
      this.handler = var2;
   }

   @Override
   protected void noMatchingItems(String var1) {
      Alert.error("No deletable items matching the path \"" + var1 + "\"", "Nothing to Delete");
   }

   @Override
   protected void processMatchingItems(Set<IntegrationEntity> var1) {
      this.deleteItems(Lists.newArrayList(var1));
   }

   private void deleteItems(final List<IntegrationEntity> var1) {
      if (DeleteConfirmer.confirm(var1)) {
         (new KongaSwingWorker<Void, Void>() {
            protected Void doInBackground() throws Exception {
               DeletePathExecutor.this.handler.deleteRequested(var1);
               return null;
            }
         }).execute();
      }
   }
}
