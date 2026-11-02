package org.etlforge.integration.activity.client.files;

import java.io.File;
import org.etlforge.integration.common.server.file.OperationFile;

public interface DownloadDestinationProvider {
   File getDestination(OperationFile var1);
}
