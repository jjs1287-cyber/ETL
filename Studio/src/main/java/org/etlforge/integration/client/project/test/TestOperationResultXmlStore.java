package org.etlforge.integration.client.project.test;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.test.TestOperationHistory;
import org.etlforge.integration.data.test.TestOperationResult;
import org.etlforge.io.Folder;

final class TestOperationResultXmlStore extends AbstractTestResultXmlStore<TestOperationResult> implements TestOperationHistory {
   public TestOperationResultXmlStore(Folder var1) {
      super(var1, "TestOperation", new TestOperationResult.Factory());
   }

   @Override
   public void removeAll(Operation var1) {
      this.deleteAllResultsForSubject(var1.getID());
   }
}
