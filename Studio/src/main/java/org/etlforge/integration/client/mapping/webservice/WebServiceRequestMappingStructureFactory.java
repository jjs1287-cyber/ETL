package org.etlforge.integration.client.mapping.webservice;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nullable;
import org.etlforge.integration.client.mapping.serverapi.transformation.TreeMapperInfoProvider;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.WebServiceRequestStructure;
import org.etlforge.integration.salesforce.SalesforceConnectionFactory;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProviderFactory;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;

public final class WebServiceRequestMappingStructureFactory extends WebServiceMappingStructureFactory {
   public WebServiceRequestMappingStructureFactory(TreeMapperInfoProvider var1, SalesforceConnectionFactory var2, SObjectSchemaProviderFactory var3) {
      super(var1, var2, var3);
   }

   @Override
   public Iterable<? extends Class<? extends DataStructure>> getSupportedStructures() {
      return ImmutableList.of(WebServiceRequestStructure.class);
   }

   @Nullable
   @Override
   public MappingTreeStructure getInputStructure(Transformation var1) throws MappingTreeStructureException {
      return this.buildTreeFromWsdl((WebServiceRequestStructure)var1.getInputStructure(), false);
   }

   @Nullable
   @Override
   public MappingTreeStructure getOutputStructure(Transformation var1) throws MappingTreeStructureException {
      WebServiceRequestStructure var2 = (WebServiceRequestStructure)var1.getOutputStructure();
      return var1.isPartOfSalesforcePartnerWizard() ? this.buildRequestForPartnerWsdlWizard(var1, var2) : this.buildTreeFromWsdl(var2, true);
   }

   @Nullable
   private MappingTreeStructure buildRequestForPartnerWsdlWizard(Transformation var1, WebServiceRequestStructure var2) throws MappingTreeStructureException {
      String var3 = var2.getOperationName();
      SfdcMethod var4 = SfdcMethod.fromMethodName(var3);
      PartnerWsdlStructureRefresher var5 = this.getPartnerWsdlRefresher(var1, var4);

      try {
         return var5.buildMappingTree();
      } catch (Exception var7) {
         Throwables.propagateIfInstanceOf(var7, MappingTreeStructureException.class);
         throw new MappingTreeStructureException(var7);
      }
   }

   private PartnerWsdlStructureRefresher getPartnerWsdlRefresher(Transformation var1, SfdcMethod var2) {
      switch (var2) {
         case QUERY:
            return new QueryRequestRefresher(this.getSfdcConnectionFactory(), this.getSfdcSchemaProviderFactory(), var1);
         case UPSERT:
         case UPDATE:
         case CREATE:
            return new UcuRequestRefresher(this.getSfdcConnectionFactory(), this.getSfdcSchemaProviderFactory(), var1, var2);
         case DELETE:
            return new DeleteRequestRefresher(this.getSfdcConnectionFactory(), this.getSfdcSchemaProviderFactory(), var1);
         default:
            throw new RuntimeException("Unhandled SFDC method: " + var2);
      }
   }
}
