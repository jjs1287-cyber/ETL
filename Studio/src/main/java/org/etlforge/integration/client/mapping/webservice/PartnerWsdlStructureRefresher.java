package org.etlforge.integration.client.mapping.webservice;

import com.google.common.base.Optional;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.SfdcMethodEntityFilter;
import org.etlforge.integration.data.entity.sfdc.WizardLookup;
import org.etlforge.integration.data.entity.transformation.SalesforceWizardRole;
import org.etlforge.integration.salesforce.OrgIdentifier;
import org.etlforge.integration.salesforce.SalesforceConnection;
import org.etlforge.integration.salesforce.SalesforceConnectionFactory;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.CachingSObjectSchemaProvider;
import org.etlforge.integration.salesforce.wsdl.ObjectSchemaLookup;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProvider;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProviderFactory;
import org.etlforge.integration.salesforce.wsdl.partner.crom.AbstractCallStructureBuilder;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.xml.ExpandedName;

abstract class PartnerWsdlStructureRefresher {
   private final SalesforceConnectionFactory connectionFactory;
   private final SObjectSchemaProviderFactory schemaProviderFactory;
   private final Transformation tx;
   private final SalesforceWizardRole role;
   private SObjectSchemaProvider schemaProvider;

   public PartnerWsdlStructureRefresher(SalesforceConnectionFactory var1, SObjectSchemaProviderFactory var2, Transformation var3, SalesforceWizardRole var4) {
      this.connectionFactory = var1;
      this.schemaProviderFactory = var2;
      this.tx = var3;
      this.role = var4;
   }

   protected final SfdcMethod getMethod() {
      return this.role.method;
   }

   public final MappingTreeStructure buildMappingTree() throws Exception {
      try {
         AbstractCallStructureBuilder var1 = this.createStructureBuilder();
         return var1.getMappingTreeStructure();
      } finally {
         this.closeSchemaProvider();
      }
   }

   private void closeSchemaProvider() {
      if (this.schemaProvider != null) {
         this.schemaProvider.close();
      }
   }

   protected abstract AbstractCallStructureBuilder createStructureBuilder() throws Exception;

   protected final String lookupObjectName() throws Exception {
      ExpandedName var1 = this.tx.getSalesforceObject();
      if (var1 == null) {
         throw new Exception(
            "Could not find the name of the Salesforce object used by this call to Salesforce. Please verify that your "
               + this.role.method.getMethodName()
               + " configuration is complete."
         );
      } else {
         return var1.getLocalName();
      }
   }

   protected final SObjectSchema describeObject(String var1) throws Exception {
      SObjectSchemaProvider var2 = this.getSchemaProvider();
      return var2 instanceof CachingSObjectSchemaProvider ? ((CachingSObjectSchemaProvider)var2).refreshObjectDefinition(var1) : var2.describeObject(var1);
   }

   protected final SObjectSchemaProvider getSchemaProvider() throws Exception {
      if (this.schemaProvider == null) {
         SalesforceConnector var1 = this.findConnector();
         this.schemaProvider = this.createSchemaProvider(var1);
      }

      return this.schemaProvider;
   }

   private SalesforceConnector findConnector() throws Exception {
      WizardLookup var1 = this.createWizardLookup();
      Optional var2 = var1.findConnector();
      if (var2.isPresent()) {
         return (SalesforceConnector)var2.get();
      } else {
         throw new Exception(
            "Could not find the Salesforce Connector used by this call to Salesforce. Please verify that your "
               + this.role.method.getMethodName()
               + " configuration is complete."
         );
      }
   }

   private WizardLookup createWizardLookup() {
      WizardLookup var1 = this.role.position == SalesforceWizardRole.Position.REQUEST ? WizardLookup.fromRequest(this.tx) : WizardLookup.fromResponse(this.tx);
      var1.setWizardFilter(new SfdcMethodEntityFilter(this.role.method));
      return var1;
   }

   private SObjectSchemaProvider createSchemaProvider(SalesforceConnector var1) {
      OrgIdentifier var2 = var1.getOrgIdentifier();
      SalesforceConnection var3 = this.connectionFactory.newConnection(var2);
      return this.schemaProviderFactory.newProvider(var3);
   }

   protected final Optional<SalesforceWizardEntity> findWizard() {
      WizardLookup var1 = this.createWizardLookup();
      return var1.findWizard();
   }

   protected class ObjectLookupFunction extends ObjectSchemaLookup {
      public ObjectLookupFunction(SObjectSchema var2) {
         super(var2);
         this.setErrorReporter(new LoggingExceptionHandler(PartnerWsdlStructureRefresher.class, ErrorLevel.LOG));
      }

      @Override
      protected SObjectSchema lookup(String var1) throws Exception {
         return PartnerWsdlStructureRefresher.this.describeObject(var1);
      }
   }
}
