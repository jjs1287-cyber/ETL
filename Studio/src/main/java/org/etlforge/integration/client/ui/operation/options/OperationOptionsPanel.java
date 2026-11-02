package org.etlforge.integration.client.ui.operation.options;

import com.google.common.base.Optional;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.TextDocumentId;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.options.OperationOptions;
import org.etlforge.integration.data.entity.operation.options.SaveTargetRecordsOption;
import org.etlforge.integration.data.entity.operation.options.ValidateSourceFileAvailability;
import org.etlforge.integration.data.entity.operation.options.ValidateSourceFileOption;
import org.etlforge.system.Disposable;
import org.etlforge.system.Disposables;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.BoxBuilder;

public final class OperationOptionsPanel extends InputPanel implements Disposable {
   private final DebugModeOptionUi debugUi;
   private final SourceHistoryOptionUi historyUi;
   private final ChunkingOptionUi chunkingUi;
   private final Optional<SaveTargetRecordsOptionUi> saveTargetRecordsUi;
   private final Optional<ValidateSourceFileOptionUi> validateSourceFileUi;

   public OperationOptionsPanel(Operation var1, EntityExplorerSupport var2) {
      this.debugUi = new DebugModeOptionUi(var1);
      this.historyUi = new SourceHistoryOptionUi(var1);
      this.chunkingUi = new ChunkingOptionUi(var1);
      this.saveTargetRecordsUi = this.createSaveTargetRecordsUi(var1, var2);
      this.validateSourceFileUi = this.createValidateSourceFileUi(var1, var2);
      this.init();
   }

   private Optional<SaveTargetRecordsOptionUi> createSaveTargetRecordsUi(Operation var1, EntityExplorerSupport var2) {
      SaveTargetRecordsOption var3 = new SaveTargetRecordsOption(var1);
      return var3.isApplicableFor(var1.getPipeline()) ? Optional.of(new SaveTargetRecordsOptionUi(var3, var2)) : Optional.absent();
   }

   private Optional<ValidateSourceFileOptionUi> createValidateSourceFileUi(Operation var1, EntityExplorerSupport var2) {
      ValidateSourceFileAvailability var3 = new ValidateSourceFileAvailability();
      if (var3.isAvailable(var1)) {
         ValidateSourceFileOption var4 = new ValidateSourceFileOption(var1);
         TextDocumentId var5 = var3.getTextDocumentId();
         return Optional.of(new ValidateSourceFileOptionUi(var4, var5, var2));
      } else {
         return Optional.absent();
      }
   }

   public OperationOptionsPanel(OperationWrapper var1, EntityExplorerSupport var2) {
      this.debugUi = new DebugModeOptionUi(var1);
      this.historyUi = new SourceHistoryOptionUi(var1);
      this.chunkingUi = new ChunkingOptionUi(var1);
      this.saveTargetRecordsUi = this.createSaveTargetRecordsUi(var1, var2);
      this.validateSourceFileUi = this.createValidateSourceFileUi(var1, var2);
      this.init();
   }

   private Optional<SaveTargetRecordsOptionUi> createSaveTargetRecordsUi(OperationWrapper var1, EntityExplorerSupport var2) {
      SaveTargetRecordsOption var3 = var1.getOptions().getSaveTargetRecordsOption();
      return var3.isApplicableFor(var1.getPipeline()) ? Optional.of(new SaveTargetRecordsOptionUi(var3, var2)) : Optional.absent();
   }

   private Optional<ValidateSourceFileOptionUi> createValidateSourceFileUi(OperationWrapper var1, EntityExplorerSupport var2) {
      ValidateSourceFileAvailability var3 = new ValidateSourceFileAvailability();
      if (var3.isAvailable(var1.getPipeline())) {
         ValidateSourceFileOption var4 = var1.getOptions().getValidateSourceFileOption();
         TextDocumentId var5 = var3.getTextDocumentId();
         return Optional.of(new ValidateSourceFileOptionUi(var4, var5, var2));
      } else {
         return Optional.absent();
      }
   }

   private void init() {
      this.historyUi.registerAsChangeSource(this);
      this.debugUi.registerAsChangeSource(this);
      this.chunkingUi.registerAsChangeSource(this);
      if (this.saveTargetRecordsUi.isPresent()) {
         this.saveTargetRecordsUi.get().registerAsChangeSource(this);
      }

      if (this.validateSourceFileUi.isPresent()) {
         this.validateSourceFileUi.get().registerAsChangeSource(this);
      }

      this.doLayout();
      this.setSendInputChangeEvents(true);
      this.setSendUndoEvents(true);
   }

   private void doLayout() {
      BoxBuilder var1 = BoxBuilder.vertical().leftAligned();
      var1.addAllWithSpace(20, this.debugUi, this.historyUi, this.saveTargetRecordsUi.orNull(), this.validateSourceFileUi.orNull(), this.chunkingUi);
      this.wrap(var1);
   }

   @Override
   public void dispose() {
      Disposables.dispose(this.saveTargetRecordsUi);
      Disposables.dispose(this.validateSourceFileUi);
      super.dispose();
   }

   public OperationOptions getDefinedOptions() {
      return new OperationOptions(
         this.historyUi.getDefinedOption(),
         this.chunkingUi.getDefinedOption(),
         this.debugUi.getDefinedOption(),
         this.getSaveTargetRecordsOption(),
         this.getValidateSourceFileOption()
      );
   }

   private SaveTargetRecordsOption getSaveTargetRecordsOption() {
      return this.saveTargetRecordsUi.isPresent() ? this.saveTargetRecordsUi.get().getDefinedOption() : SaveTargetRecordsOption.no();
   }

   private ValidateSourceFileOption getValidateSourceFileOption() {
      return this.validateSourceFileUi.isPresent() ? this.validateSourceFileUi.get().getDefinedOption() : ValidateSourceFileOption.noValidation();
   }
}
