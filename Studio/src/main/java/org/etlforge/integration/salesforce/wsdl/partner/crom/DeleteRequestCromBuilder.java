package org.etlforge.integration.salesforce.wsdl.partner.crom;

import org.etlforge.integration.structure.crom.CROM;

public final class DeleteRequestCromBuilder extends AbstractCallStructureBuilder {
   public DeleteRequestCromBuilder() {
      super(
         AbstractCallStructureBuilder.Mode.REQUEST,
         new SessionCromHeader(),
         new CallOptionsCromHeader(),
         new PackageVersionCromHeader(),
         new UserTerritoryDeleteCromHeader(),
         new EmailCromHeader(),
         new AllowFieldTruncationCromHeader(),
         new DisableFeedTrackingCromHeader(),
         new StreamingEnabledCromHeader(),
         new AllOrNoneCromHeader(),
         new DebuggingCromHeader()
      );
   }

   @Override
   protected void populateBody(CROM var1) {
      CROM var2 = createElementInPartnerNamespace(var1, "delete");
      CROM var3 = createElementInPartnerNamespace(var2, "ids");
      CromUtils.makeOptionalLoop(var3);
      createStringValueNode(var3);
   }
}
