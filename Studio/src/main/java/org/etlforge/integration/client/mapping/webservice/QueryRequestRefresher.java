package org.etlforge.integration.client.mapping.webservice;

import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.SalesforceWizardRole;
import org.etlforge.integration.salesforce.SalesforceConnectionFactory;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProviderFactory;
import org.etlforge.integration.salesforce.wsdl.partner.crom.AbstractCallStructureBuilder;
import org.etlforge.integration.salesforce.wsdl.partner.crom.QueryRequestCromBuilder;

final class QueryRequestRefresher extends PartnerWsdlStructureRefresher {
   public QueryRequestRefresher(SalesforceConnectionFactory var1, SObjectSchemaProviderFactory var2, Transformation var3) {
      super(var1, var2, var3, SalesforceWizardRole.request(SfdcMethod.QUERY));
   }

   @Override
   protected AbstractCallStructureBuilder createStructureBuilder() {
      return new QueryRequestCromBuilder();
   }
}
