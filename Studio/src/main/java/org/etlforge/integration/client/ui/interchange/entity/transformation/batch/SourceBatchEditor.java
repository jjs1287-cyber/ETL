package org.etlforge.integration.client.ui.interchange.entity.transformation.batch;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.SourceBatchParams;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;

public class SourceBatchEditor {
   private final Transformation transformation;
   private final BatchSizeInput sizeInput;
   private Receiver<SourceBatchParams> undoRedoMonitor;
   private SourceBatchParams currentParams;

   public SourceBatchEditor(Transformation var1, BatchSizeInput var2) {
      this.transformation = var1;
      this.sizeInput = var2;
      this.currentParams = var1.getSourceBatchParams();
      this.undoRedoMonitor = NoReceiver.create();
   }

   public void setUndoRedoMonitor(Receiver<SourceBatchParams> var1) {
      this.undoRedoMonitor = var1;
   }

   public SourceBatchChange edit(String var1) {
      Preconditions.checkArgument(StringUtils.isNotBlank(var1), "path may not be null or empty");
      Integer var2 = this.askForBatchSize(var1);
      if (var2 != null) {
         SourceBatchParams var3 = this.createNewParams(var1, var2);
         if (!var3.equals(this.currentParams)) {
            SourceBatchChange var4 = this.createChangeObject(this.currentParams, var3);
            this.currentParams = var3;
            return var4;
         }
      }

      return null;
   }

   private Integer askForBatchSize(String var1) {
      return this.sizeInput.getBatchSize(var1, this.currentParams.getBatchSize());
   }

   private SourceBatchParams createNewParams(String var1, int var2) {
      return var2 > 0 ? new SourceBatchParams(var1, var2) : SourceBatchParams.NONE;
   }

   private SourceBatchChange createChangeObject(SourceBatchParams var1, SourceBatchParams var2) {
      SourceBatchChange var3 = new SourceBatchChange(this.transformation, var1, var2);
      var3.addUndoRedoMonitor(new Receiver<SourceBatchParams>() {
         public void handle(SourceBatchParams var1) {
            SourceBatchEditor.this.currentParams = var1;
            SourceBatchEditor.this.undoRedoMonitor.handle(var1);
         }
      });
      return var3;
   }
}
