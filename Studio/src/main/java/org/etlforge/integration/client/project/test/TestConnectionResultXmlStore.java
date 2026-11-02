package org.etlforge.integration.client.project.test;

import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.test.TestConnectionHistory;
import org.etlforge.integration.data.test.TestConnectionResult;
import org.etlforge.io.Folder;

final class TestConnectionResultXmlStore extends AbstractTestResultXmlStore<TestConnectionResult> implements TestConnectionHistory {
   TestConnectionResultXmlStore(Folder var1) {
      super(var1, "TestConnection", new TestConnectionResult.Factory());
   }

   @Override
   public void removeAll(Locatable var1) {
      this.deleteAllResultsForSubject(var1.getID());
   }
}
