package org.etlforge.integration.client.project;

import java.io.File;
import java.util.List;
import org.etlforge.integration.client.project.event.RecentProjectsListener;

public interface RecentProjects {
   void addProjectLocation(ProjectLocation var1);

   void removeProjectLocation(ProjectLocation var1);

   void projectWasRenamed(String var1, File var2, String var3);

   List<ProjectLocation> getExistingLocations();

   int getNumberOfRecentProjects();

   void addRecentProjectsListener(RecentProjectsListener var1);

   void removeRecentProjectsListener(RecentProjectsListener var1);
}
