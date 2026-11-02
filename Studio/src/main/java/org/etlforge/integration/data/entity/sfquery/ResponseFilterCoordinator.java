package org.etlforge.integration.data.entity.sfquery;

import com.google.common.base.Optional;
import com.etlforge.integration.client.sfdc.data.SoqlObject;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.SystemTransformation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.entity.transformation.MappingTreeNodePathFilter;

public final class ResponseFilterCoordinator {
   private final Optional<IntegrationEntityLookup> lookup;

   public ResponseFilterCoordinator() {
      this.lookup = Optional.absent();
   }

   public ResponseFilterCoordinator(IntegrationEntityLookup var1) {
      this.lookup = Optional.fromNullable(var1);
   }

   public void updateResponseFilter(SalesforceQuery var1) {
      WizardItemBrowser var2 = this.createBrowser(var1);
      Transformation var3 = var2.getResponseTransformation();
      if (var3 != null) {
         MappingTreeNodePathFilter var4 = this.getResponseFilter(var1, var2);
         var3.setInputMappingTreeFilter(var4);
      }
   }

   private MappingTreeNodePathFilter getResponseFilter(SalesforceQuery var1, WizardItemBrowser var2) {
      Transformation var3 = var2.getRequestTransformation();
      if (var3 == SystemTransformation.SALESFORCE_QUERY_REQUEST) {
         SoqlObject var4 = var1.getQueryObject();
         return new QueryResponseMappingTreeFilter(var4);
      } else {
         return null;
      }
   }

   private WizardItemBrowser createBrowser(SalesforceQuery var1) {
      return this.lookup.isPresent() ? new WizardItemBrowser(var1, this.lookup.get()) : new WizardItemBrowser(var1);
   }
}
