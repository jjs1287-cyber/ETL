package org.etlforge.integration.client.project;

import java.io.File;

public interface ProjectDirectory {
   String NAME = "projects";

   File getLocation();

   String getSuggestedNewProjectName();

   String getSuggestedNewProjectName(File var1);
}
