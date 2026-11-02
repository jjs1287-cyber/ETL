package org.etlforge.integration.client.project.test;

import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.test.TestTransformationHistory;
import org.etlforge.integration.data.test.TestTransformationResult;
import org.etlforge.io.Folder;

final class TestTransformationResultXmlStore extends AbstractTestResultXmlStore<TestTransformationResult> implements TestTransformationHistory {
   TestTransformationResultXmlStore(Folder var1) {
      super(var1, "TestTransformation", new TestTransformationResult.Factory());
   }

   @Override
   public void removeAll(Transformation var1) {
      this.deleteAllResultsForSubject(var1.getID());
   }
}
