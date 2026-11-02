package com.etlforge.integration.client.project.share;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;

public final class WebServiceCallResourcesImporter implements EntityResourcesImporter {
   private final WebServiceCall wsCall;
   private final DefaultEntityResourcesImporter defaultImporter;
   private ManagedProject sourceProject;
   private ManagedProject targetProject;

   @ExtensionObjectConstructor
   public WebServiceCallResourcesImporter(WebServiceCall var1) {
      this.wsCall = var1;
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
      WsdlFile var1 = this.wsCall.getWsdlFile();
      if (!var1.isRemote()) {
         this.defaultImporter.transferFileStoreFile(var1.getLocator());
      }
   }

   private void exportMappingStructures() {
      try {
         WebServiceMappingStructureCacheExporter var1 = new WebServiceMappingStructureCacheExporter(this.sourceProject, this.targetProject);
         var1.export(this.wsCall);
      } catch (Exception var2) {
         ErrorLog.log(
            this.getClass(),
            "Failed to copy the cached mapping structures for the Web Service Call \""
               + this.wsCall.getName()
               + "\" from the source project to the target project.",
            var2
         );
      }
   }
}
