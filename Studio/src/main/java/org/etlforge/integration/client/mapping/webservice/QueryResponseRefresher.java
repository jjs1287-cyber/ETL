package org.etlforge.integration.client.mapping.webservice;

import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.SalesforceWizardRole;
import org.etlforge.integration.salesforce.SalesforceConnectionFactory;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProviderFactory;
import org.etlforge.integration.salesforce.wsdl.partner.crom.AbstractCallStructureBuilder;
import org.etlforge.integration.salesforce.wsdl.partner.crom.QueryResponseCromBuilder;

final class QueryResponseRefresher extends PartnerWsdlStructureRefresher {
   public QueryResponseRefresher(SalesforceConnectionFactory var1, SObjectSchemaProviderFactory var2, Transformation var3) {
      super(var1, var2, var3, SalesforceWizardRole.response(SfdcMethod.QUERY));
   }

   @Override
   protected AbstractCallStructureBuilder createStructureBuilder() throws Exception {
      String var1 = this.lookupObjectName();
      SObjectSchema var2 = this.describeObject(var1);
      QueryResponseCromBuilder var3 = new QueryResponseCromBuilder(var2);
      var3.includeRelationships(this.getSchemaProvider());
      return var3;
   }
}
