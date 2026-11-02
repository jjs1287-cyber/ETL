package org.etlforge.integration.client.mapping.webservice;

import com.google.common.base.Optional;
import java.util.List;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.SalesforceWizardRole;
import org.etlforge.integration.salesforce.SalesforceConnectionFactory;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.RelatedObjectsOwner;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProviderFactory;
import org.etlforge.integration.salesforce.wsdl.partner.crom.AbstractCallStructureBuilder;
import org.etlforge.integration.salesforce.wsdl.partner.crom.UcuRequestCromBuilder;
import org.etlforge.integration.salesforce.wsdl.partner.crom.UcuResponseCromBuilder;

final class UcuResponseRefresher extends PartnerWsdlStructureRefresher {
   public UcuResponseRefresher(SalesforceConnectionFactory var1, SObjectSchemaProviderFactory var2, Transformation var3, SfdcMethod var4) {
      super(var1, var2, var3, SalesforceWizardRole.response(var4));
   }

   @Override
   protected AbstractCallStructureBuilder createStructureBuilder() throws Exception {
      UcuRequestCromBuilder var1 = this.createRequestBuilder();
      return new UcuResponseCromBuilder(this.getMethod(), var1);
   }

   private UcuRequestCromBuilder createRequestBuilder() throws Exception {
      String var1 = this.lookupObjectName();
      SObjectSchema var2 = this.describeObject(var1);
      UcuRequestCromBuilder var3 = new UcuRequestCromBuilder(this.getMethod(), var2);
      if (this.getMethod().supportsForeignKeys()) {
         Optional var4 = this.findWizard();
         if (var4.isPresent()) {
            List var5 = ((RelatedObjectsOwner)var4.get()).getRelatedObjects();
            var3.setRelatedObjects(var5);
         }
      }

      return var3;
   }
}
