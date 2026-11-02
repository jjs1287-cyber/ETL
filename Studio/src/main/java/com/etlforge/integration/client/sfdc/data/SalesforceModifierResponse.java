package com.etlforge.integration.client.sfdc.data;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.salesforce.SfdcMethod;

public final class SalesforceModifierResponse {
   private final SalesforceModifierResponseMapper mapper;
   private final ImplementingObject<TextDocument> targetStructure;

   public SalesforceModifierResponse(IntegrationProject var1, SfdcMethod var2, WizardFolders var3, SalesforceModifierResponseMapper var4) {
      this.mapper = var4;
      this.targetStructure = this.getTargetStructure(var1, var2, var3);
   }

   private ImplementingObject<TextDocument> getTargetStructure(IntegrationProject var1, SfdcMethod var2, WizardFolders var3) {
      SingleFieldTextDocumentProvider var4 = new SingleFieldTextDocumentProvider(var1, "SFDC " + var2.getDisplayName() + " Response", "response");
      var4.setParent(var3.getWizardTypeRootFolder(EntityType.Document));
      return var4.getDocument();
   }

   public void applyTo(Transformation var1) {
      TextDocument var2 = this.targetStructure.getObject();
      var1.setOutputStructure(var2.getDataStructure());
      var1.setTargetTextDocumentId(var2.getID());
      this.mapper.insertResponseMappings(var1);
   }

   public ImplementingObject<TextDocument> getResponseDocument() {
      return this.targetStructure;
   }
}
