package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import org.etlforge.integration.debug.client.TestResult;

public interface TestProcessCallback {
   void beforeTestStarts();

   void testCompletedSuccessfully(TestResult var1);
}
