package org.etlforge.integration.data.test;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.util.persist.PersistableFactory;
import org.etlforge.util.persist.Persistor;

abstract class AbstractTestResult implements TestResult {
   private static final String OUTCOME = "Outcome";
   private static final String TIME = "Time";
   private static final String DETAILS = "Details";
   private final long timestamp;
   private final TestResult.Outcome outcome;
   private final String details;

   protected AbstractTestResult(long var1, TestResult.Outcome var3, String var4) {
      this.timestamp = var1;
      this.outcome = var3;
      this.details = var4;
   }

   public final boolean isSuccess() {
      return this.outcome != TestResult.Outcome.ERROR;
   }

   @Override
   public final String getDetails() {
      return this.details;
   }

   @Override
   public final TestResult.Outcome getOutcome() {
      return this.outcome;
   }

   @Override
   public final long getTimestamp() {
      return this.timestamp;
   }

   @Override
   public void save(Persistor var1) {
      var1.putString("Outcome", this.outcome.name());
      var1.putLong("Time", this.timestamp);
      var1.putString("Details", this.details);
   }

   protected abstract static class AbstractFactory<T extends AbstractTestResult> implements PersistableFactory<T> {
      protected TestResult.Outcome restoreOutcome(Persistor var1) {
         String var2 = var1.getString("Outcome");
         checkNull("Outcome", var2);
         return TestResult.Outcome.valueOf(var2);
      }

      protected String restoreDetails(Persistor var1) {
         String var2 = var1.getString("Details");
         checkNull("Details", var2);
         return var2;
      }

      protected long restoreTimestamp(Persistor var1) {
         return var1.getLong("Time");
      }

      protected IntegrationEntityId restoreEntityId(Persistor var1, String var2, EntityType var3) {
         String var4 = var1.getString(var2);
         checkNull(var2, var4);
         return var3.getIntegrationId(var4);
      }

      protected static void checkNull(String var0, String var1) throws IllegalArgumentException {
         if (var1 == null) {
            throw new IllegalArgumentException("The persistor does not contain an entry for " + var0);
         }
      }
   }
}
