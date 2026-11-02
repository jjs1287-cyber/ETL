package org.etlforge.integration.data.entity.operation.options;

import com.google.common.base.Objects;
import java.util.List;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.structure.DataStructure;

public final class ChunkingOption implements OperationOption {
   private final int chunkSize;
   private final int targetChunkSize;
   private final int maxNumberOfThreads;
   private final String sourceNode;
   private final String targetNode;

   public ChunkingOption(Operation var1) {
      this(
         var1.getChunkSize(),
         var1.getTargetChunkSize(),
         var1.getIntProperty("max_number_of_threads", 1),
         var1.getProperty("source_chunk_node"),
         var1.getProperty("target_chunk_node")
      );
   }

   public ChunkingOption(ChunkingOption var1) {
      this(var1.getChunkSize(), var1.getTargetChunkSize(), var1.getMaxNumberOfThreads(), var1.getSourceChunkNode(), var1.getTargetChunkNode());
   }

   public ChunkingOption(int var1, int var2, int var3, String var4, String var5) {
      this.chunkSize = var1;
      this.targetChunkSize = var2;
      this.maxNumberOfThreads = var3;
      this.sourceNode = var4 != null ? var4 : "";
      this.targetNode = var5 != null ? var5 : "";
   }

   @Override
   public void applyTo(Operation var1) {
      var1.setProperty("max_number_of_threads", this.maxNumberOfThreads > 0 ? this.maxNumberOfThreads : 1);
      var1.setProperty("chunk_size", this.chunkSize);
      var1.setProperty("target_chunk_size", this.targetChunkSize);
      var1.setProperty("source_chunk_node", this.sourceNode);
      var1.setProperty("target_chunk_node", this.targetNode);
   }

   public boolean isEnabled() {
      return this.chunkSize > 0;
   }

   public final int getChunkSize() {
      return this.chunkSize;
   }

   public final int getTargetChunkSize() {
      return this.targetChunkSize;
   }

   public int getMaxNumberOfThreads() {
      return this.maxNumberOfThreads;
   }

   public String getSourceChunkNode() {
      return this.sourceNode;
   }

   public String getTargetChunkNode() {
      return this.targetNode;
   }

   @Override
   public boolean isApplicableFor(OperationPipeline var1) {
      if (!var1.getType().isChunkable()) {
         return false;
      }

      DataStructure var2 = getInputStructure(var1);
      return var2 != null && var2.isChunkable();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof ChunkingOption)) {
         return false;
      }

      ChunkingOption var2 = (ChunkingOption)var1;
      return this.chunkSize == var2.chunkSize
         && this.targetChunkSize == var2.targetChunkSize
         && this.maxNumberOfThreads == var2.maxNumberOfThreads
         && this.sourceNode.equals(var2.sourceNode)
         && this.targetNode.equals(var2.targetNode);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.chunkSize, this.maxNumberOfThreads, this.sourceNode, this.targetNode);
   }

   private static DataStructure getInputStructure(OperationPipeline var0) {
      Transformation var1 = getFirstTransformation(var0);
      return var1 != null ? var1.getInputStructure() : null;
   }

   private static Transformation getFirstTransformation(OperationPipeline var0) {
      return findFirstTransformation(var0.getActivities());
   }

   private static Transformation findFirstTransformation(List<PipelineActivity> var0) {
      for (PipelineActivity var2 : var0) {
         if (var2 instanceof TransformationActivity) {
            return ((TransformationActivity)var2).getContent();
         }
      }

      return null;
   }
}
