package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import com.google.common.io.Files;
import java.io.File;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.ui.wait.WaitService;

final class ImportExportController {
   private final HeaderMappingsModel model;
   private final ImportExportFileProvider fileProvider = new ImportExportFileProvider();
   private WaitService waitService = WaitService.NONE;

   public ImportExportController(HeaderMappingsModel var1) {
      this.model = var1;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   public void importMappings() {
      File var1 = this.fileProvider.getImportFile();
      if (var1 != null) {
         this.readFile(var1);
      }
   }

   private void readFile(File var1) {
      MappingsImporter var2 = new MappingsImporter(Files.newInputStreamSupplier(var1), this.model);
      this.runComputation(var2.getComputation());
   }

   private void runComputation(Computation<?> var1) {
      var1.swingWorkerBuilder().withErrorLog(ErrorLevel.ATTENTION, this.getClass()).withWaitService(this.waitService).buildAndExecute();
   }

   public void exportMappings() {
      File var1 = this.fileProvider.getExportFile();
      if (var1 != null) {
         this.saveToFile(var1);
      }
   }

   private void saveToFile(File var1) {
      MappingsExporter var2 = new MappingsExporter(Files.newOutputStreamSupplier(var1), this.model.getMapList());
      this.runComputation(var2.getComputation());
   }
}
