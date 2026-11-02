package org.etlforge.integration.activity.ui.files;

import org.etlforge.integration.activity.client.files.ResultFileSummary;
import org.etlforge.integration.common.server.file.OperationFile;
import org.etlforge.system.callback.Receiver;

public interface OperationFileController {
   void openFile(OperationFile var1, Receiver<ResultFileSummary> var2);
}
