package com.etlforge.integration.client.project.share;

import com.google.common.base.Strings;
import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.data.connector.ConnectorCall;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.structure.XsdBasedStructure;

public final class ConnectorCallResourcesImporter implements EntityResourcesImporter {
   private final ConnectorCall connectorCall;
   private final DefaultEntityResourcesImporter defaultImporter;

   @ExtensionObjectConstructor
   public ConnectorCallResourcesImporter(ConnectorCall var1) {
      this.connectorCall = var1;
      this.defaultImporter = new DefaultEntityResourcesImporter((IntegrationEntity)var1);
   }

   @Override
   public void transferResources(ManagedProject var1, ManagedProject var2) {
      this.defaultImporter.transferResources(var1, var2);
      this.transferXsdFile(this.connectorCall.getInputStructure());
      this.transferXsdFile(this.connectorCall.getOutputStructure());
   }

   private void transferXsdFile(XsdBasedStructure var1) {
      String var2 = var1.getXsdPath();
      if (!Strings.isNullOrEmpty(var2)) {
         this.defaultImporter.transferFileStoreFile(var2);
      }
   }
}
