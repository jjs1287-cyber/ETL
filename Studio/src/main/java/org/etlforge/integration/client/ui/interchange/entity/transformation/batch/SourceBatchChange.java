package org.etlforge.integration.client.ui.interchange.entity.transformation.batch;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageSaveJob;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.SourceBatchParams;
import org.etlforge.system.callback.Receiver;

public final class SourceBatchChange implements EntityPageSaveJob {
   private final Transformation transformation;
   private final SourceBatchParams oldParams;
   private final SourceBatchParams newParams;
   private List<Receiver<SourceBatchParams>> undoRedoMonitors;

   public SourceBatchChange(Transformation var1, SourceBatchParams var2, SourceBatchParams var3) {
      this.transformation = var1;
      this.oldParams = var2;
      this.newParams = var3;
      this.undoRedoMonitors = Lists.newArrayList();
   }

   public SourceBatchParams getNewParams() {
      return this.newParams;
   }

   public void addUndoRedoMonitor(Receiver<SourceBatchParams> var1) {
      this.undoRedoMonitors.add(var1);
   }

   public void removeUndoRedoMonitor(Receiver<SourceBatchParams> var1) {
      this.undoRedoMonitors.remove(var1);
   }

   @Override
   public String getDescription() {
      return "Source Batch Change";
   }

   @Override
   public String getId() {
      return "source_batching";
   }

   @Override
   public void postSaveRevert() {
      this.transformation.setSourceBatchParams(this.oldParams);
   }

   @Override
   public void save() {
      this.transformation.setSourceBatchParams(this.newParams);
   }

   @Override
   public void redoWithoutSaving() {
      this.undoOrRedoTo(this.newParams);
   }

   @Override
   public void undoWithoutSaving() {
      this.undoOrRedoTo(this.oldParams);
   }

   private void undoOrRedoTo(SourceBatchParams var1) {
      for (Receiver var3 : this.undoRedoMonitors) {
         var3.handle(var1);
      }
   }
}
