package org.etlforge.integration.client.mapping.webservice;

import java.util.Collections;
import java.util.List;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.SalesforceWizardRole;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.salesforce.SalesforceConnectionFactory;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.RelatedObject;
import org.etlforge.integration.salesforce.wsdl.RelatedObjectsFinder;
import org.etlforge.integration.salesforce.wsdl.RelatedObjectsOwner;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProviderFactory;
import org.etlforge.integration.salesforce.wsdl.partner.crom.AbstractCallStructureBuilder;
import org.etlforge.integration.salesforce.wsdl.partner.crom.UcuRequestCromBuilder;

final class UcuRequestRefresher extends PartnerWsdlStructureRefresher {
   public UcuRequestRefresher(SalesforceConnectionFactory var1, SObjectSchemaProviderFactory var2, Transformation var3, SfdcMethod var4) {
      super(var1, var2, var3, SalesforceWizardRole.request(var4));
   }

   @Override
   protected AbstractCallStructureBuilder createStructureBuilder() throws Exception {
      String var1 = this.lookupObjectName();
      SObjectSchema var2 = this.describeObject(var1);
      UcuRequestCromBuilder var3 = new UcuRequestCromBuilder(this.getMethod(), var2);
      List var4 = this.refreshRelatedObjectsInfo(var2);
      var3.setRelatedObjects(var4);
      return var3;
   }

   private List<RelatedObject> refreshRelatedObjectsInfo(SObjectSchema var1) {
      SalesforceWizardEntity var2 = this.findWizard().orNull();
      if (!(var2 instanceof RelatedObjectsOwner)) {
         return Collections.emptyList();
      }

      RelatedObjectsOwner var3 = (RelatedObjectsOwner)var2;
      PartnerWsdlStructureRefresher.ObjectLookupFunction var4 = new PartnerWsdlStructureRefresher.ObjectLookupFunction(var1);
      List var5 = RelatedObjectsFinder.find(var1, var4);
      if (var4.isSuccess()) {
         this.storeNewRelatedObjectInfo(var3, var5);
      } else {
         var5 = var3.getRelatedObjects();
      }

      return var5;
   }

   private void storeNewRelatedObjectInfo(RelatedObjectsOwner var1, List<RelatedObject> var2) {
      var1.setRelatedObjects(var2);
      if (var1 instanceof IntegrationEntity) {
         try {
            ((IntegrationEntity)var1).save();
         } catch (InterchangeSavingException var4) {
            ErrorLog.log(this.getClass(), null, var4);
         }
      }
   }
}
