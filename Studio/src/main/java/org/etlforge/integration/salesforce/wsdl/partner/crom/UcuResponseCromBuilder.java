package org.etlforge.integration.salesforce.wsdl.partner.crom;

import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;

public final class UcuResponseCromBuilder extends AbstractCallStructureBuilder {
   private final SfdcMethod method;
   private final UcuRequestCromBuilder requestBuilder;

   public UcuResponseCromBuilder(SfdcMethod var1, UcuRequestCromBuilder var2) {
      super(AbstractCallStructureBuilder.Mode.RESPONSE, new DebuggingInfoCromHeader(), new LimitInfoCromHeader());
      this.method = var1;
      this.requestBuilder = var2;
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
      CROM var2 = createElementInPartnerNamespace(var1, this.method.getResponseElementName());
      buildResultStructure(var2, this.method);
   }

   private void insertRequestStructure(CROM var1) throws MappingTreeStructureException {
      CROM var2 = createElementInETLForgeNamespace(var1, "request");
      this.requestBuilder.setParent(var2);
      this.requestBuilder.getCromTree();
      this.setTypeMap(this.requestBuilder.getTypeMap());
   }
}
