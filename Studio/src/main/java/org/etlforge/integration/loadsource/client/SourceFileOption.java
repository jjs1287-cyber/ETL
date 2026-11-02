package org.etlforge.integration.loadsource.client;

import java.io.File;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.util.option.InputOption;

public interface SourceFileOption extends InputOption<File> {
   void setFinalSelectionMonitor(Receiver<Void> var1);

   boolean includeSelectionInProject();
}
