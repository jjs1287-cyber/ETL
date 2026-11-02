package com.etlforge.integration.client.ui.sfdc.page;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageSaveJob;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.options.ChunkingOption;
import org.etlforge.integration.data.entity.operation.options.OperationOptions;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.system.callback.Receiver;

public final class ChunkingSettingsSaveJob implements EntityPageSaveJob {
   private final Operation op;
   private final EntityPersistor persistor;
   private final ChunkingOption oldSettings;
   private final ChunkingOption newSettings;
   private final Receiver<ChunkingOption> undoRedoStateReceiver;

   public ChunkingSettingsSaveJob(Operation var1, ChunkingOption var2, ChunkingOption var3, EntityPersistor var4, Receiver<ChunkingOption> var5) {
      this.op = var1;
      this.oldSettings = var2;
      this.newSettings = var3;
      this.persistor = var4;
      this.undoRedoStateReceiver = var5;
   }

   @Override
   public String getDescription() {
      return "Chunking Settings Change";
   }

   @Override
   public String getId() {
      return "chunking_settings";
   }

   @Override
   public void save() {
      this.apply(this.newSettings);
   }

   private void apply(ChunkingOption var1) {
      OperationOptions var2 = this.op.getOperationOptions();
      OperationOptions var3 = var2.withChunkingOption(var1);
      this.op.setOperationOptions(var3);

      try {
         this.persistor.save(this.op);
      } catch (InterchangeSavingException var5) {
         this.op.setOperationOptions(var2);
         ErrorLog.attention(this.getClass(), "The new chunking settings could not be saved.", var5);
      }
   }

   @Override
   public void postSaveRevert() {
      this.apply(this.oldSettings);
   }

   @Override
   public void undoWithoutSaving() {
      this.undoRedoStateReceiver.handle(this.oldSettings);
   }

   @Override
   public void redoWithoutSaving() {
      this.undoRedoStateReceiver.handle(this.newSettings);
   }
}
