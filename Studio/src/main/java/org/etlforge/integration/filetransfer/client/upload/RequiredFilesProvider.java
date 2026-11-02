package org.etlforge.integration.filetransfer.client.upload;

import java.io.File;
import java.util.List;
import javax.annotation.Nullable;

public interface RequiredFilesProvider {
   @Nullable
   List<RequiredFiles> locateRequiredFiles(List<RequiredFiles> var1, File var2);
}
