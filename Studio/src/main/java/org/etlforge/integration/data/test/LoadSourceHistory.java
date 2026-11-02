package org.etlforge.integration.data.test;

import org.etlforge.integration.data.entity.Transformation;

public interface LoadSourceHistory extends TestResultHistory<LoadSourceResult> {
   void removeAll(Transformation var1);
}
