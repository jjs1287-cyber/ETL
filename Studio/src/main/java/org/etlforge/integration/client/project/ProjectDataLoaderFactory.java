package org.etlforge.integration.client.project;

import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.util.version.UnsupportedVersionException;

public interface ProjectDataLoaderFactory {
   ProjectDataLoader newLoader(IntegrationProject var1, ProjectFile var2) throws UnsupportedVersionException;
}
