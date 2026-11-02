package org.etlforge.integration.activity.serverapi.history;

final class MockOperationHistoryProvider implements OperationHistoryProvider {
   @Override
   public void getOperationHistory(HistoryRequest var1, GetOperationHistoryCallback var2) {
      var2.caught(new UnsupportedOperationException());
   }
}
