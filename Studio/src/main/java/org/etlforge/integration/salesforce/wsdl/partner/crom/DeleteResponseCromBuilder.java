package org.etlforge.integration.salesforce.wsdl.partner.crom;

import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;

public final class DeleteResponseCromBuilder extends AbstractCallStructureBuilder {
   public DeleteResponseCromBuilder() {
      super(AbstractCallStructureBuilder.Mode.RESPONSE, new DebuggingInfoCromHeader(), new LimitInfoCromHeader());
   }

   @Override
   protected void populateBody(CROM var1) throws MappingTreeStructureException {
      this.buildResponseNode(var1);
      CROM var2 = this.getTransactionNode();
      if (var2 != null) {
         this.insertRequestStructure(var2);
      }
   }

   private void buildResponseNode(CROM var1) {
      CROM var2 = createElementInPartnerNamespace(var1, SfdcMethod.DELETE.getResponseElementName());
      buildResultStructure(var2, SfdcMethod.DELETE);
   }

   private void insertRequestStructure(CROM var1) throws MappingTreeStructureException {
      DeleteRequestCromBuilder var2 = new DeleteRequestCromBuilder();
      CROM var3 = createElementInETLForgeNamespace(var1, "request");
      var2.setParent(var3);
      var2.getCromTree();
   }
}
