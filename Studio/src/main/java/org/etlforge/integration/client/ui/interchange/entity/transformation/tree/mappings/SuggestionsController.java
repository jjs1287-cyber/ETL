package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import java.util.List;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.BreadthFirstTraverser;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;

final class SuggestionsController {
   private final SuggestionsModel model;
   private SuggestionsController.NodeFinder worker;

   public SuggestionsController(SuggestionsModel var1) {
      this.model = var1;
   }

   public void startSearch() {
      this.stopSearch();
      this.model.clear();
      this.model.setWorking(true);
      this.worker = new SuggestionsController.NodeFinder();
      this.worker.execute();
   }

   public void stopSearch() {
      if (this.worker != null) {
         this.worker.cancel(true);
         this.worker = null;
      }
   }

   private class NodeFinder extends KongaSwingWorker<Void, Node> {
      private NodeFinder() {
      }

      protected Void doInBackground() throws Exception {
         BreadthFirstTraverser var1 = new BreadthFirstTraverser(SuggestionsController.this.model.getOpposingRoot());
         NodeSuggestionStrategy var2 = SuggestionsController.this.model.getStrategy();

         for (Node var4 : var1) {
            if (var2.isCandidate(var4)) {
               this.publish(var4);
            }

            if (this.isCancelled()) {
               break;
            }
         }

         return null;
      }

      @Override
      protected void process(List<Node> var1) {
         SuggestionsController.this.model.addTargetNodes(var1);
      }

      @Override
      protected void finished() {
         SuggestionsController.this.model.setWorking(false);
         SuggestionsController.this.worker = null;
      }
   }
}
