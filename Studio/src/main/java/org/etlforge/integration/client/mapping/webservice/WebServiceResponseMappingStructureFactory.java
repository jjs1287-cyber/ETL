package org.etlforge.integration.client.mapping.webservice;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nullable;
import org.etlforge.integration.client.mapping.serverapi.transformation.TreeMapperInfoProvider;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.WebServiceResponseStructure;
import org.etlforge.integration.salesforce.SalesforceConnectionFactory;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProviderFactory;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;

public final class WebServiceResponseMappingStructureFactory extends WebServiceMappingStructureFactory {
   public WebServiceResponseMappingStructureFactory(TreeMapperInfoProvider var1, SalesforceConnectionFactory var2, SObjectSchemaProviderFactory var3) {
      super(var1, var2, var3);
   }

   @Override
   public Iterable<? extends Class<? extends DataStructure>> getSupportedStructures() {
      return ImmutableList.of(WebServiceResponseStructure.class);
   }

   @Nullable
   @Override
   public MappingTreeStructure getInputStructure(Transformation var1) throws MappingTreeStructureException {
      WebServiceResponseStructure var2 = (WebServiceResponseStructure)var1.getInputStructure();
      return var1.isPartOfSalesforcePartnerWizard() ? this.buildResponseForPartnerWsdlWizard(var1, var2) : this.buildTreeFromWsdl(var2, false);
   }

   private MappingTreeStructure buildResponseForPartnerWsdlWizard(Transformation var1, WebServiceResponseStructure var2) throws MappingTreeStructureException {
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
            return new QueryResponseRefresher(this.getSfdcConnectionFactory(), this.getSfdcSchemaProviderFactory(), var1);
         case UPSERT:
         case UPDATE:
         case CREATE:
            return new UcuResponseRefresher(this.getSfdcConnectionFactory(), this.getSfdcSchemaProviderFactory(), var1, var2);
         case DELETE:
            return new DeleteResponseRefresher(this.getSfdcConnectionFactory(), this.getSfdcSchemaProviderFactory(), var1);
         default:
            throw new RuntimeException("Unhandled SFDC method: " + var2);
      }
   }

   @Nullable
   @Override
   public MappingTreeStructure getOutputStructure(Transformation var1) throws MappingTreeStructureException {
      return this.buildTreeFromWsdl((WebServiceResponseStructure)var1.getOutputStructure(), true);
   }
}
