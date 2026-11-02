package com.etlforge.integration.client.ui.sfdc.query.page;

import com.etlforge.integration.client.ui.sfdc.page.soql.SoqlModel;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.ui.wait.WaitService;

final class SoqlChangeConsequence implements SaveConsequence {
   private final ResponseMappingTreeFilterConsequence mappingTreeFilterConsequence;
   private final FieldListChangeConsequence fieldListConsequence;

   public SoqlChangeConsequence(SalesforceQuery var1, SoqlModel var2) {
      this.mappingTreeFilterConsequence = new ResponseMappingTreeFilterConsequence(var1, var2);
      this.fieldListConsequence = new FieldListChangeConsequence(var1, var2);
   }

   public void setWaitService(WaitService var1) {
      this.fieldListConsequence.setWaitService(var1);
   }

   @Override
   public boolean isAllowed() {
      return this.fieldListConsequence.isAllowed();
   }

   @Override
   public void apply() {
      this.mappingTreeFilterConsequence.apply();
      this.fieldListConsequence.apply();
   }
}
