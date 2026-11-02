package org.etlforge.integration.client.ui.entity.search;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.etlforge.system.Disposable;

public final class EntitySearchService implements Disposable {
   private final EntitySearchModel model;
   private final ExecutorService searchThread;
   private final PropertyChangeListener modelListener;
   private SearchJob currentSearch;

   public EntitySearchService(EntitySearchModel var1) {
      this.model = var1;
      this.searchThread = Executors.newFixedThreadPool(1);
      this.modelListener = new EntitySearchService.ModelListener();
      var1.addPropertyChangeListener(this.modelListener);
   }

   @Override
   public void dispose() {
      this.model.removePropertyChangeListener(this.modelListener);
   }

   private class ModelListener implements PropertyChangeListener {
      private ModelListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (!"matches".equals(var1.getPropertyName())) {
            this.stopCurrentSearcher();
            this.startNewSearch();
         }
      }

      private void stopCurrentSearcher() {
         if (EntitySearchService.this.currentSearch != null) {
            EntitySearchService.this.currentSearch.stop();
         }
      }

      private void startNewSearch() {
         EntitySearchService.this.currentSearch = new SearchJob(EntitySearchService.this.model);
         EntitySearchService.this.searchThread.submit(EntitySearchService.this.currentSearch);
      }
   }
}
