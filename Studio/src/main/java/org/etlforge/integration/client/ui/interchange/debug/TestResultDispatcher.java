package org.etlforge.integration.client.ui.interchange.debug;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Map;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.debug.client.TestResult;

final class TestResultDispatcher {
   private final Multimap<IntegrationEntityId, TestResultDisplayer> testResultDisplayers = HashMultimap.create();
   private final Map<EntityType, TestResultDisplayer> typeDisplayers;

   public TestResultDispatcher(Map<EntityType, TestResultDisplayer> var1) {
      this.typeDisplayers = Maps.newHashMap(var1);
   }

   public void addTestResultDisplayer(IntegrationEntityId var1, TestResultDisplayer var2) {
      this.testResultDisplayers.put(var1, var2);
   }

   public void removeTestResultDisplayer(IntegrationEntityId var1, TestResultDisplayer var2) {
      this.testResultDisplayers.remove(var1, var2);
   }

   public void dispatch(TestResult var1) {
      IntegrationEntityId var2 = var1.getIdOfEntityToDisplay();
      if (this.testResultDisplayers.containsKey(var2)) {
         this.dispatchToMappedDisplayers(var1, var2);
      } else {
         this.dispatchToTypeDisplayer(var1, var2);
      }
   }

   private void dispatchToMappedDisplayers(TestResult var1, IntegrationEntityId var2) {
      for (TestResultDisplayer var5 : this.testResultDisplayers.get(var2)) {
         var5.showTestResult(var1);
      }
   }

   private void dispatchToTypeDisplayer(TestResult var1, IntegrationEntityId var2) {
      TestResultDisplayer var3 = this.typeDisplayers.get(var2.getEntityType());
      if (var3 != null) {
         var3.showTestResult(var1);
      } else {
         ErrorLog.attention(
            this.getClass(),
            null,
            new Exception("Configuration error: Cannot display the TestResult for " + var2.getEntityType().getPlural() + ". This is a bug.")
         );
      }
   }
}
