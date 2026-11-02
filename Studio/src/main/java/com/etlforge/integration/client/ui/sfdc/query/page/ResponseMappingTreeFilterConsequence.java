package com.etlforge.integration.client.ui.sfdc.query.page;

import com.google.common.base.Objects;
import com.etlforge.integration.client.ui.sfdc.page.soql.SoqlModel;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.entity.sfquery.QueryResponseMappingTreeFilter;
import org.etlforge.integration.data.entity.transformation.MappingTreeNodePathFilter;
import org.etlforge.integration.salesforce.parser.ParsedQuery;

final class ResponseMappingTreeFilterConsequence implements SaveConsequence {
   private final SalesforceQuery query;
   private final SoqlModel soqlModel;
   private final Transformation response;
   private MappingTreeNodePathFilter currentFilter;

   public ResponseMappingTreeFilterConsequence(SalesforceQuery var1, SoqlModel var2) {
      this.query = var1;
      this.soqlModel = var2;
      this.response = this.getResponse();
      this.currentFilter = this.response == null ? null : this.response.getInputMappingTreeFilter();
   }

   private Transformation getResponse() {
      WizardItemBrowser var1 = new WizardItemBrowser(this.query);
      return var1.getResponseTransformation();
   }

   @Override
   public boolean isAllowed() {
      return true;
   }

   @Override
   public void apply() {
      if (this.response != null) {
         MappingTreeNodePathFilter var1 = this.getMappingTreeFilter();
         boolean var2 = !Objects.equal(this.currentFilter, var1);
         if (var2) {
            this.response.setInputMappingTreeFilter(var1);
         }

         this.currentFilter = var1;
      }
   }

   private MappingTreeNodePathFilter getMappingTreeFilter() {
      ParsedQuery var1 = this.soqlModel.getParsedQuery();
      return new QueryResponseMappingTreeFilter(var1);
   }
}
