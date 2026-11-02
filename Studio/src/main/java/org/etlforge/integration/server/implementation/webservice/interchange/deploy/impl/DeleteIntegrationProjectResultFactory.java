package org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl;

import com.etlforge.integration.client.admin.project.DeleteIntegrationProjectResult;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CDeployWarning;

final class DeleteIntegrationProjectResultFactory {
   private static String ERROR_TAG = "ERROR:";

   public DeleteIntegrationProjectResult create(IntegrationProjectId var1, CDeployWarning[] var2) {
      ArrayList var3 = new ArrayList();
      StringBuilder var4 = new StringBuilder();

      for (CDeployWarning var8 : var2) {
         if (var8 != null && var8.getEntity().getId().equalsIgnoreCase(var1.toString())) {
            String var9 = var8.getWarning();
            if (StringUtils.startsWithIgnoreCase(var9, ERROR_TAG)) {
               if (var4.length() > 0) {
                  var4.append("\n");
               }

               var4.append(StringUtils.substringAfter(var9, ERROR_TAG));
            } else {
               var3.add(var9);
            }
         }
      }

      return new DeleteIntegrationProjectResultFactory.DeleteIntegrationProjectResultImpl(var1, var4.toString(), var3);
   }

   private static class DeleteIntegrationProjectResultImpl implements DeleteIntegrationProjectResult {
      private final String error;
      private final List<String> warnings;
      private final IntegrationProjectId projectId;

      public DeleteIntegrationProjectResultImpl(IntegrationProjectId var1, String var2, List<String> var3) {
         this.error = var2;
         this.warnings = var3;
         this.projectId = var1;
      }

      @Override
      public String getError() {
         return this.error;
      }

      @Override
      public IntegrationProjectId getProjectId() {
         return this.projectId;
      }

      @Override
      public List<String> getWarnings() {
         return this.warnings;
      }

      @Override
      public boolean success() {
         return this.error.isEmpty();
      }
   }
}
