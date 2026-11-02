package org.etlforge.integration.client.ui.operation.runner;

import java.io.Reader;
import java.util.List;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;

public interface OperationRunner {
   Receiver<Long> NO_CALLBACK = NoReceiver.create();

   void run(Operation var1, int var2, Receiver<Long> var3);

   void runAll(List<Operation> var1);

   void runWithSourceData(Operation var1, Reader var2, Receiver<Long> var3);
}
