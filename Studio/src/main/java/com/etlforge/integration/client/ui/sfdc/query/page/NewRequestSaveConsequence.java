package com.etlforge.integration.client.ui.sfdc.query.page;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.etlforge.integration.client.sfdc.data.SoapHeaderMappingsReplacer;
import com.etlforge.integration.client.sfdc.data.SoapHeaderTranslator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.client.ui.interchange.entity.operation.page.OperationPage;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.SystemTransformation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.ws.WebServicePipelineBrowser;
import org.etlforge.integration.data.entity.sfdc.WizardLookup;
import org.etlforge.integration.data.entity.sfquery.ResponseFilterCoordinator;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.salesforce.soap.header.SessionHeader;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.lang.Objects;

final class NewRequestSaveConsequence implements SaveConsequence {
   private final OperationPage page;
   private final SalesforceQuery query;
   private final Operation operation;
   private Transformation originalRequest;
   private Transformation newRequest;

   public NewRequestSaveConsequence(OperationPage var1, SalesforceQuery var2) {
      this.page = var1;
      this.query = var2;
      this.operation = var1.getObject();
      this.originalRequest = this.getRequestTransformation();
   }

   private Transformation getRequestTransformation() {
      WebServicePipelineBrowser var1 = new WebServicePipelineBrowser(this.operation.getPipeline());
      return var1.getRequest();
   }

   @Override
   public boolean isAllowed() {
      OperationPipeline var1 = this.page.getPipeline();
      WebServicePipelineBrowser var2 = new WebServicePipelineBrowser(var1);
      this.newRequest = var2.getRequest();
      if (this.newRequest == null) {
         NonBlockingAlert.error("This operation implements a Salesforce wizard. It must have a request Transformation.", "Not Allowed");
         return false;
      }

      if (this.newRequest != this.originalRequest) {
         SalesforceConnectorId var3 = this.query.getConnectorId();
         WizardLookup var4 = WizardLookup.fromRequest(this.newRequest);

         for (SalesforceWizardEntity var6 : var4.findAllWizards()) {
            if (!Objects.equal(var3, var6.getConnectorId())) {
               NonBlockingAlert.error(
                  "The selected request Transformation is already used by another Salesforce wizard, that connects to a different Salesforce Org than this one. This is not allowed.",
                  "Not Allowed"
               );
               return false;
            }
         }
      }

      return true;
   }

   @Override
   public void apply() {
      if (this.newRequest != this.originalRequest) {
         this.mapSoapHeaders();
         this.updateResponseMappingTreeFilter();
         this.originalRequest = this.newRequest;
      }
   }

   private void mapSoapHeaders() {
      if (this.newRequest != SystemTransformation.SALESFORCE_QUERY_REQUEST) {
         Set var1 = this.getSoapHeaders();
         List var2 = this.newRequest.getMapList();
         SoapHeaderMappingsReplacer var3 = new SoapHeaderMappingsReplacer(var1);
         var3.replaceSoapHeaders(var2);
         this.newRequest.setMapList(var2);

         try {
            this.newRequest.save();
         } catch (InterchangeSavingException var5) {
            ErrorLog.attention(NewRequestSaveConsequence.class, null, var5);
         }
      }
   }

   private Set<SoapHeader> getSoapHeaders() {
      Function var1 = new Function<SalesforceWizardEntity.Header, SoapHeader>() {
         private final SoapHeaderTranslator t = new SoapHeaderTranslator();

         public SoapHeader apply(SalesforceWizardEntity.Header var1) {
            return this.t.toSoapHeader(var1);
         }
      };
      HashSet var2 = Sets.newHashSet(
         FluentIterable.from(this.query.getSoapHeaders()).transform(var1).filter(Predicates.not(Predicates.instanceOf(SessionHeader.class)))
      );
      SalesforceConnectorId var3 = this.query.getConnectorId();
      if (var3 != null) {
         var2.add(new SessionHeader(var3));
      }

      return var2;
   }

   private void updateResponseMappingTreeFilter() {
      ResponseFilterCoordinator var1 = new ResponseFilterCoordinator();
      var1.updateResponseFilter(this.query);
   }
}
