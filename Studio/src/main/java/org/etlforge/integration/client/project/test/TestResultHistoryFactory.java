package org.etlforge.integration.client.project.test;

import org.etlforge.integration.data.test.LoadSourceHistory;
import org.etlforge.integration.data.test.TestConnectionHistory;
import org.etlforge.integration.data.test.TestOperationHistory;
import org.etlforge.integration.data.test.TestTransformationHistory;
import org.etlforge.io.Folder;

public abstract class TestResultHistoryFactory {
   public abstract TestTransformationHistory newTestTransformationHistory();

   public abstract TestOperationHistory newTestOperationHistory();

   public abstract LoadSourceHistory newLoadSourceHistory();

   public abstract TestConnectionHistory newTestConnectionHistory();

   public static TestResultHistoryFactory newFileBasedHistoryFactory(final Folder var0) {
      return new TestResultHistoryFactory() {
         @Override
         public LoadSourceHistory newLoadSourceHistory() {
            return new LoadSourceResultXmlStore(var0);
         }

         @Override
         public TestOperationHistory newTestOperationHistory() {
            return new TestOperationResultXmlStore(var0);
         }

         @Override
         public TestTransformationHistory newTestTransformationHistory() {
            return new TestTransformationResultXmlStore(var0);
         }

         @Override
         public TestConnectionHistory newTestConnectionHistory() {
            return new TestConnectionResultXmlStore(var0);
         }
      };
   }
}
