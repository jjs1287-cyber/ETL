package com.etlforge.integration.client.project.share;

import com.google.common.base.Strings;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.data.entity.Transformation;

public final class TransformationResourcesImporter implements EntityResourcesImporter {
   private final DefaultEntityResourcesImporter defaultImporter;
   private final Transformation tx;
   private ManagedProject sourceProject;
   private ManagedProject targetProject;

   @ExtensionObjectConstructor
   public TransformationResourcesImporter(Transformation var1) {
      this.tx = var1;
      this.defaultImporter = new DefaultEntityResourcesImporter(var1);
   }

   @Override
   public void transferResources(ManagedProject var1, ManagedProject var2) {
      this.defaultImporter.transferResources(var1, var2);
      this.sourceProject = var1;
      this.targetProject = var2;
      this.exportStructureFiles();
      this.exportMappingStructures();
   }

   private void exportStructureFiles() {
      this.exportStructureFile("source_xml");
      this.exportStructureFile("target_xml");
   }

   private void exportStructureFile(String var1) {
      String var2 = this.tx.getProperty(var1);
      if (!Strings.isNullOrEmpty(var2)) {
         this.defaultImporter.transferFileStoreFile(var2);
      }
   }

   private void exportMappingStructures() {
      try {
         TransformationMappingStructureCacheExporter var1 = new TransformationMappingStructureCacheExporter(this.sourceProject, this.targetProject);
         var1.export(this.tx);
      } catch (Exception var2) {
         ErrorLog.log(
            this.getClass(),
            "Failed to copy the cached mapping structures for the Transformation \"" + this.tx.getName() + "\" from the source project to the target project.",
            var2
         );
      }
   }
}
