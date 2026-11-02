package org.etlforge.integration.data.test;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.util.persist.Persistor;

public final class TestOperationResult extends AbstractTestResult implements EntityTestResult {
   private static final String OP_ID = "OpId";
   private static final String TRANS_ID = "TransId";
   private final OperationId opId;
   private final TransformationId transId;

   private TestOperationResult(Operation var1, Transformation var2, TestResult.Outcome var3, String var4) {
      this(var1.getID(), var2.getID(), System.currentTimeMillis(), var3, var4);
   }

   private TestOperationResult(OperationId var1, TransformationId var2, long var3, TestResult.Outcome var5, String var6) {
      super(var3, var5, var6);
      this.opId = var1;
      this.transId = var2;
   }

   public static TestOperationResult success(Operation var0, Transformation var1) {
      return new TestOperationResult(var0, var1, SUCCESS, "Success");
   }

   public static TestOperationResult success(Operation var0, Transformation var1, String var2) {
      return var2 != null && var2.length() > 0 ? partial(var0, var1, var2) : success(var0, var1);
   }

   public static TestOperationResult partial(Operation var0, Transformation var1, String var2) {
      Preconditions.checkNotNull(var2, "warnings");
      return new TestOperationResult(var0, var1, PARTIAL, var2);
   }

   public static TestOperationResult error(Operation var0, Transformation var1, TestResultError var2) {
      return new TestOperationResult(var0, var1, ERROR, var2.toString());
   }

   public TransformationId getTransformationId() {
      return this.transId;
   }

   public OperationId getSubjectId() {
      return this.opId;
   }

   @Override
   public String getId() {
      return String.valueOf(this.getTimestamp());
   }

   @Override
   public void save(Persistor var1) {
      super.save(var1);
      var1.putString("OpId", this.opId.toString());
      var1.putString("TransId", this.transId.toString());
   }

   public static final class Factory extends AbstractTestResult.AbstractFactory<TestOperationResult> {
      public TestOperationResult restore(Persistor var1) {
         OperationId var2 = (OperationId)this.restoreEntityId(var1, "OpId", EntityType.Operation);
         TransformationId var3 = (TransformationId)this.restoreEntityId(var1, "TransId", EntityType.Transformation);
         long var4 = this.restoreTimestamp(var1);
         TestResult.Outcome var6 = this.restoreOutcome(var1);
         String var7 = this.restoreDetails(var1);
         return new TestOperationResult(var2, var3, var4, var6, var7);
      }
   }
}
