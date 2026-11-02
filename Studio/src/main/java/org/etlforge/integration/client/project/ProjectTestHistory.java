package org.etlforge.integration.client.project;

import org.etlforge.integration.data.test.LoadSourceHistory;
import org.etlforge.integration.data.test.TestConnectionHistory;
import org.etlforge.integration.data.test.TestOperationHistory;
import org.etlforge.integration.data.test.TestTransformationHistory;

public interface ProjectTestHistory {
   TestTransformationHistory getTestTransformationHistory();

   TestOperationHistory getTestOperationHistory();

   LoadSourceHistory getLoadSourceHistory();

   TestConnectionHistory getTestConnectionHistory();
}
