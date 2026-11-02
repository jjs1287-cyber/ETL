package org.etlforge.integration.data.test;

import org.etlforge.integration.data.Locatable;

public final class EmptyTestConnectionHistory extends EmptyTestResultHistory<TestConnectionResult> implements TestConnectionHistory {
   @Override
   public void removeAll(Locatable var1) {
   }
}
