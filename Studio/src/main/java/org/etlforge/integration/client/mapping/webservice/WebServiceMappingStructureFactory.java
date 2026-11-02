package org.etlforge.integration.client.mapping.webservice;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.mapping.AbstractMappingStructureFactory;
import org.etlforge.integration.client.mapping.DefaultMappingStructureCallback;
import org.etlforge.integration.client.mapping.serverapi.transformation.TreeMapperInfoProvider;
import org.etlforge.integration.data.entity.webservicecall.WebServiceOperationIdentifier;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;
import org.etlforge.integration.data.structure.WebServiceRequestStructure;
import org.etlforge.integration.data.structure.WebServiceStructure;
import org.etlforge.integration.salesforce.SalesforceConnectionFactory;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProviderFactory;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;

abstract class WebServiceMappingStructureFactory extends AbstractMappingStructureFactory {
   private final TreeMapperInfoProvider infoProvider;
   private final SalesforceConnectionFactory sfdcConnectionFactory;
   private final SObjectSchemaProviderFactory sfdcSchemaProviderFactory;

   protected WebServiceMappingStructureFactory(TreeMapperInfoProvider var1, SalesforceConnectionFactory var2, SObjectSchemaProviderFactory var3) {
      this.infoProvider = Preconditions.checkNotNull(var1, "infoProvider");
      this.sfdcConnectionFactory = Preconditions.checkNotNull(var2, "sfdcConnectionFactory");
      this.sfdcSchemaProviderFactory = Preconditions.checkNotNull(var3, "sfdcSchemaProviderFactory");
   }

   protected final SalesforceConnectionFactory getSfdcConnectionFactory() {
      return this.sfdcConnectionFactory;
   }

   protected final SObjectSchemaProviderFactory getSfdcSchemaProviderFactory() {
      return this.sfdcSchemaProviderFactory;
   }

   protected final MappingTreeStructure buildTreeFromWsdl(WebServiceStructure var1, boolean var2) throws MappingTreeStructureException {
      DefaultMappingStructureCallback var3 = new DefaultMappingStructureCallback();
      WebServiceOperationInfo var4 = var1.getOpInfo();
      WebServiceOperationIdentifier var5 = new WebServiceOperationIdentifier(
         var4.getProperty("Operation"), var4.getProperty("InputName"), var4.getProperty("OutputName")
      );
      this.infoProvider
         .getJtrForWS(
            var1.getFunctionCallId(),
            var4.getProperty("Identifier"),
            var4.getProperty("Binding"),
            var5,
            var4.getProperty("SoapStyle"),
            var4.getProperty("SoapBodyUse"),
            var2,
            var1 instanceof WebServiceRequestStructure,
            var3
         );
      return var3.getMappingStructureOrThrow();
   }
}
