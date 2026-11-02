package org.etlforge.integration.data.test;

import org.etlforge.integration.data.entity.Operation;

public interface TestOperationHistory extends TestResultHistory<TestOperationResult> {
   void removeAll(Operation var1);
}
