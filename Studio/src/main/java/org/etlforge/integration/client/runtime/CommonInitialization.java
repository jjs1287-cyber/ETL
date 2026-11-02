package org.etlforge.integration.client.runtime;

import org.etlforge.application.extension.ExtensionObjectService;
import org.etlforge.integration.client.info.BuildProperties;
import org.etlforge.integration.data.DefaultIntegrationNameValidator;
import org.etlforge.integration.data.ProjectNameValidator;
import org.etlforge.integration.data.entity.EntityExtensionConfig;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.util.name.NameValidatorStore;

public final class CommonInitialization {
   private final InitializationErrorHandler errorHandler;

   public CommonInitialization(InitializationErrorHandler var1) {
      this.errorHandler = var1;
   }

   public void initializeObjectStructure() {
      this.installNameValidators();
      this.loadOperationTypes();
   }

   private void installNameValidators() {
      NameValidatorStore var1 = NameValidatorStore.getInstance();
      var1.put(IntegrationEntity.class, new DefaultIntegrationNameValidator());
      var1.put(IntegrationProject.class, new ProjectNameValidator());
   }

   private void loadOperationTypes() {
      OperationType.load();
   }

   public void loadMetaData() {
      try {
         EntityExtensionConfig var1 = new EntityExtensionConfig(ExtensionObjectService.getService());
         var1.loadDefaultConfig();
      } catch (Exception var2) {
         this.handleMetaDataLoadingError(var2);
      }
   }

   public void loadBuildProperties() {
      BuildProperties.init();
   }

   private void handleMetaDataLoadingError(Exception var1) {
      this.fatalExceptionOccurred("loadMetaData", 4, var1);
   }

   public void setKeepStandardConsole(boolean var1) {
      System.setProperty("SystemOutToConsole", Boolean.toString(var1));
      System.setProperty("SystemErrToConsole", Boolean.toString(var1));
   }

   private void fatalExceptionOccurred(String var1, int var2, Throwable var3) {
      this.errorHandler.handle(var1, var2, var3);
   }
}
