package org.etlforge.integration.data.test;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.util.persist.Persistor;

public final class LoadSourceResult extends AbstractTestResult implements EntityTestResult {
   private static final String TRANS_ID = "TransId";
   private final TransformationId transId;

   private LoadSourceResult(Transformation var1, TestResult.Outcome var2, String var3) {
      this(var1.getID(), System.currentTimeMillis(), var2, var3);
   }

   private LoadSourceResult(TransformationId var1, long var2, TestResult.Outcome var4, String var5) {
      super(var2, var4, var5);
      this.transId = var1;
   }

   public static LoadSourceResult success(Transformation var0) {
      return new LoadSourceResult(var0, SUCCESS, "Success");
   }

   public static LoadSourceResult partial(Transformation var0, String var1) {
      Preconditions.checkNotNull(var1, "warnings");
      return new LoadSourceResult(var0, PARTIAL, var1);
   }

   public static LoadSourceResult error(Transformation var0, TestResultError var1) {
      return new LoadSourceResult(var0, ERROR, var1.toString());
   }

   public TransformationId getSubjectId() {
      return this.transId;
   }

   @Override
   public String getId() {
      return String.valueOf(this.getTimestamp());
   }

   @Override
   public void save(Persistor var1) {
      super.save(var1);
      var1.putString("TransId", this.transId.toString());
   }

   public static final class Factory extends AbstractTestResult.AbstractFactory<LoadSourceResult> {
      public LoadSourceResult restore(Persistor var1) {
         TransformationId var2 = (TransformationId)this.restoreEntityId(var1, "TransId", EntityType.Transformation);
         long var3 = this.restoreTimestamp(var1);
         TestResult.Outcome var5 = this.restoreOutcome(var1);
         String var6 = this.restoreDetails(var1);
         return new LoadSourceResult(var2, var3, var5, var6);
      }
   }
}
