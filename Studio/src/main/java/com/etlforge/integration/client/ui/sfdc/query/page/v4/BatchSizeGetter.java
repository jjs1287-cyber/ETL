package com.etlforge.integration.client.ui.sfdc.query.page.v4;

import java.util.List;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.script.Transform;
import org.etlforge.integration.salesforce.config.SfdcMethodConfig;
import org.etlforge.integration.structure.mapping.MapStringFormat;
import org.etlforge.integration.structure.mapping.Mapping;

final class BatchSizeGetter {
   private final String batchSizePath;

   public BatchSizeGetter(SfdcMethodConfig var1) {
      this.batchSizePath = var1.getProperty("BatchSizePath");
   }

   public int getBatchSize(SalesforceQuery var1) {
      WizardItemBrowser var2 = new WizardItemBrowser(var1);
      Transformation var3 = var2.getRequestTransformation();
      return var3 != null ? this.getBatchSizeFromRequest(var3) : 2000;
   }

   private int getBatchSizeFromRequest(Transformation var1) {
      for (String var3 : var1.getMapList()) {
         Mapping var4 = MapStringFormat.parse(var3);
         String var5 = var4.getTargetPath().toString();
         if (var5.equals(this.batchSizePath)) {
            return this.parseOutBatchSizeFromMapping(var4.getExpression());
         }
      }

      return 2000;
   }

   private int parseOutBatchSizeFromMapping(String var1) {
      Transform var2 = new Transform();
      List var3 = var2.tokenize(var1);
      if (var3 != null) {
         for (Object __etlItem41 : var3) {
            Transform.Token var5 = (Transform.Token) __etlItem41;
            if (var5.m_id == 402) {
               return Integer.parseInt(var5.m_str);
            }
         }
      }

      return 2000;
   }
}
