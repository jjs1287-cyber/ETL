package org.etlforge.integration.data.test;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.util.persist.Persistor;

public final class TestTransformationResult extends AbstractTestResult implements EntityTestResult {
   private static final String TRANS_ID = "TransId";
   private final TransformationId transId;

   private TestTransformationResult(Transformation var1, TestResult.Outcome var2, String var3) {
      this(var1.getID(), System.currentTimeMillis(), var2, var3);
   }

   private TestTransformationResult(TransformationId var1, long var2, TestResult.Outcome var4, String var5) {
      super(var2, var4, var5);
      this.transId = var1;
   }

   public static TestTransformationResult success(Transformation var0) {
      return new TestTransformationResult(var0, SUCCESS, "Success");
   }

   public static TestTransformationResult success(Transformation var0, String var1) {
      return var1 != null && var1.length() > 0 ? partial(var0, var1) : success(var0);
   }

   public static TestTransformationResult partial(Transformation var0, String var1) {
      Preconditions.checkNotNull(var1, "warnings");
      return new TestTransformationResult(var0, PARTIAL, var1);
   }

   public static TestTransformationResult error(Transformation var0, TestResultError var1) {
      return new TestTransformationResult(var0, ERROR, var1.toString());
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

   public static final class Factory extends AbstractTestResult.AbstractFactory<TestTransformationResult> {
      public TestTransformationResult restore(Persistor var1) {
         TransformationId var2 = (TransformationId)this.restoreEntityId(var1, "TransId", EntityType.Transformation);
         long var3 = this.restoreTimestamp(var1);
         TestResult.Outcome var5 = this.restoreOutcome(var1);
         String var6 = this.restoreDetails(var1);
         return new TestTransformationResult(var2, var3, var5, var6);
      }
   }
}
