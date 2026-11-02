package org.etlforge.integration.client.project.rename;

import org.etlforge.integration.client.project.ProjectLocation;

public interface ProjectNameConflictResolver {
   ProjectNameConflictResolver.Resolution resolve(ProjectLocation var1);

   enum Resolution {
      OVERWRITE,
      CANCEL;
   }
}
