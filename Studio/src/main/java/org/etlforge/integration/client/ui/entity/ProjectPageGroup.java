package org.etlforge.integration.client.ui.entity;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;

public final class ProjectPageGroup {
   private static final String PREAMBLE = ProjectPageGroup.class.getName() + ":";

   public static String get(IntegrationProject var0) {
      return var0 != null ? PREAMBLE + var0.getID().toString() : null;
   }

   public static String get(IntegrationEntity var0) {
      IntegrationProject var1 = ProjectUtils.getProject(var0);
      return get(var1);
   }

   private ProjectPageGroup() {
   }
}
