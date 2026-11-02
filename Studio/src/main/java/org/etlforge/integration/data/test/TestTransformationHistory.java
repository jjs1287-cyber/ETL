package org.etlforge.integration.data.test;

import org.etlforge.integration.data.entity.Transformation;

public interface TestTransformationHistory extends TestResultHistory<TestTransformationResult> {
   void removeAll(Transformation var1);
}
