package org.etlforge.integration.data.test;

import org.etlforge.integration.data.Locatable;

public interface TestConnectionHistory extends TestResultHistory<TestConnectionResult> {
   void removeAll(Locatable var1);
}
