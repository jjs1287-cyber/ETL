package org.etlforge.integration.client.project.jitterpack;

import java.io.File;
import org.etlforge.integration.client.project.ProjectLoadingCancelledException;
import org.etlforge.integration.client.project.ProjectLoadingIssuesHandler;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.util.version.UnsupportedVersionException;

public interface Importer {
   IntegrationProject unpack(JitterPack var1, JitterPackDescriptor var2, File var3, ImportParameters var4, ProjectLoadingIssuesHandler var5) throws JitterPackException, UnsupportedVersionException, ProjectLoadingCancelledException;

   JitterPackDescriptor extractDescriptor(File var1, ImportParameters var2) throws JitterPackException, UnsupportedVersionException;
}
