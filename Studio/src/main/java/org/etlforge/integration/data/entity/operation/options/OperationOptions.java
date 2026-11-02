package org.etlforge.integration.data.entity.operation.options;

import com.google.common.base.Objects;
import org.etlforge.integration.data.entity.Operation;

public final class OperationOptions {
   private final SourceHistoryOption historyOption;
   private final ChunkingOption chunkingOption;
   private final RunInDebugModeOption debugOption;
   private final SaveTargetRecordsOption saveTargetRecordsOption;
   private final ValidateSourceFileOption validateSourceFileOption;

   public OperationOptions(Operation var1) {
      this.historyOption = new SourceHistoryOption(var1);
      this.chunkingOption = new ChunkingOption(var1);
      this.debugOption = new RunInDebugModeOption(var1);
      this.saveTargetRecordsOption = new SaveTargetRecordsOption(var1);
      this.validateSourceFileOption = new ValidateSourceFileOption(var1);
   }

   public OperationOptions(
      SourceHistoryOption var1, ChunkingOption var2, RunInDebugModeOption var3, SaveTargetRecordsOption var4, ValidateSourceFileOption var5
   ) {
      this.historyOption = var1;
      this.chunkingOption = var2;
      this.debugOption = var3;
      this.saveTargetRecordsOption = var4;
      this.validateSourceFileOption = var5;
   }

   public SourceHistoryOption getSourceHistoryOption() {
      return this.historyOption;
   }

   public OperationOptions withSourceHistoryOption(boolean var1) {
      return new OperationOptions(
         new SourceHistoryOption(var1), this.chunkingOption, this.debugOption, this.saveTargetRecordsOption, this.validateSourceFileOption
      );
   }

   public void applyTo(Operation var1) {
      this.historyOption.applyTo(var1);
      this.chunkingOption.applyTo(var1);
      this.debugOption.applyTo(var1);
      this.saveTargetRecordsOption.applyTo(var1);
      this.validateSourceFileOption.applyTo(var1);
   }

   public ChunkingOption getChunkingOption() {
      return this.chunkingOption;
   }

   public OperationOptions withChunkingOption(ChunkingOption var1) {
      return new OperationOptions(this.historyOption, var1, this.debugOption, this.saveTargetRecordsOption, this.validateSourceFileOption);
   }

   public RunInDebugModeOption getDebugOption() {
      return this.debugOption;
   }

   public OperationOptions withDebugOption(boolean var1) {
      return new OperationOptions(
         this.historyOption, this.chunkingOption, new RunInDebugModeOption(var1), this.saveTargetRecordsOption, this.validateSourceFileOption
      );
   }

   public SaveTargetRecordsOption getSaveTargetRecordsOption() {
      return this.saveTargetRecordsOption;
   }

   public ValidateSourceFileOption getValidateSourceFileOption() {
      return this.validateSourceFileOption;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof OperationOptions)) {
         return false;
      }

      OperationOptions var2 = (OperationOptions)var1;
      return this.historyOption.equals(var2.historyOption)
         && this.chunkingOption.equals(var2.chunkingOption)
         && this.debugOption.equals(var2.debugOption)
         && this.saveTargetRecordsOption.equals(var2.saveTargetRecordsOption)
         && this.validateSourceFileOption.equals(var2.validateSourceFileOption);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.historyOption, this.chunkingOption, this.debugOption, this.saveTargetRecordsOption, this.validateSourceFileOption);
   }
}
