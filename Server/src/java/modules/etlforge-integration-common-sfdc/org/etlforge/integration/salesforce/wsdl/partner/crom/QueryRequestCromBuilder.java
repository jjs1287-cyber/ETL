package org.etlforge.integration.salesforce.wsdl.partner.crom;

import org.etlforge.integration.structure.crom.CROM;

public final class QueryRequestCromBuilder extends AbstractCallStructureBuilder {
   public QueryRequestCromBuilder() {
      super(
         AbstractCallStructureBuilder.Mode.REQUEST,
         new SessionCromHeader(),
         new CallOptionsCromHeader(),
         new QueryOptionCromHeader(),
         new MruCromHeader(),
         new PackageVersionCromHeader()
      );
   }

   @Override
   protected void populateBody(CROM var1) {
      CROM var2 = createElementInPartnerNamespace(var1, "query");
      CROM var3 = createElementInPartnerNamespace(var2, "queryString");
      createStringValueNode(var3);
   }
}
