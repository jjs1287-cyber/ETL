package com.etlforge.integration.client.ui.sfdc.query.page.v4;

import com.etlforge.integration.client.sfdc.data.SoqlObject;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.entity.sfquery.QueryResponseMappingTreeFilter;
import org.etlforge.integration.data.entity.transformation.MappingTreeNodePathFilter;
import org.etlforge.xml.ExpandedName;

final class ResponseNodeFilterConsequenceV4 implements SaveConsequence {
   private final SalesforceQueryPageV4 page;

   public ResponseNodeFilterConsequenceV4(SalesforceQueryPageV4 var1) {
      this.page = var1;
   }

   @Override
   public boolean isAllowed() {
      return true;
   }

   @Override
   public void apply() {
      Transformation var1 = this.getResponseTransformation();
      if (var1 != null) {
         this.applyFilter(var1);
      }
   }

   private void applyFilter(Transformation var1) {
      MappingTreeNodePathFilter var2 = var1.getInputMappingTreeFilter();
      SoqlObject var3 = this.getSoql();
      QueryResponseMappingTreeFilter var4 = new QueryResponseMappingTreeFilter(var3);
      if (!var4.equals(var2)) {
         var1.setInputMappingTreeFilter(var4);
      }
   }

   private Transformation getResponseTransformation() {
      SalesforceQuery var1 = this.page.getObject();
      WizardItemBrowser var2 = new WizardItemBrowser(var1);
      return var2.getResponseTransformation();
   }

   private SoqlObject getSoql() {
      String var1 = this.page.getQueryString();
      ExpandedName var2 = this.page.getObject().getObject();
      return new SoqlObject(var1, var2, false);
   }
}
