package org.etlforge.integration.data.test;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.util.persist.Persistor;

public final class TestConnectionResult extends AbstractTestResult implements EntityTestResult {
   private static final String ENTITY_ID = "EntityId";
   private static final String ENTITY_TYPE = "EntityType";
   private static final String LOCATION_DETAILS = "LocationDetails";
   private final IntegrationEntityId entityId;
   private final EntityType entityType;
   private final LocationSpecificConnectionDetails details;

   private TestConnectionResult(IntegrationEntity var1, TestResult.Outcome var2, String var3, LocationSpecificConnectionDetails var4) {
      this(var1.getID(), System.currentTimeMillis(), var2, var3, var4);
   }

   private TestConnectionResult(IntegrationEntityId var1, long var2, TestResult.Outcome var4, String var5, LocationSpecificConnectionDetails var6) {
      super(var2, var4, var5);
      this.entityId = var1;
      this.entityType = var1.getEntityType();
      this.details = var6;
   }

   public static TestConnectionResult success(IntegrationEntity var0) {
      return new TestConnectionResult(var0, SUCCESS, "Connection was successful", null);
   }

   public static TestConnectionResult success(IntegrationEntity var0, LocationSpecificConnectionDetails var1) {
      return new TestConnectionResult(var0, SUCCESS, "Connection was successful", Preconditions.checkNotNull(var1, "details"));
   }

   public static TestConnectionResult error(IntegrationEntity var0, TestResultError var1) {
      return new TestConnectionResult(var0, ERROR, var1.toString(), null);
   }

   @Override
   public IntegrationEntityId getSubjectId() {
      return this.entityId;
   }

   public EntityType getEntityType() {
      return this.entityType;
   }

   @Override
   public void save(Persistor var1) {
      super.save(var1);
      var1.putString("EntityId", this.entityId.toString());
      var1.putString("EntityType", this.entityType.name());
      if (this.details != null) {
         this.details.save(var1.createChild("LocationDetails"));
      }
   }

   @Override
   public String getId() {
      return String.valueOf(this.getTimestamp());
   }

   public LocationSpecificConnectionDetails getLocationSpecificDetails() {
      return this.details;
   }

   public static final class Factory extends AbstractTestResult.AbstractFactory<TestConnectionResult> {
      public TestConnectionResult restore(Persistor var1) {
         EntityType var2 = this.restoreEntityType(var1);
         IntegrationEntityId var3 = this.restoreEntityId(var1, "EntityId", var2);
         long var4 = this.restoreTimestamp(var1);
         TestResult.Outcome var6 = this.restoreOutcome(var1);
         String var7 = this.restoreDetails(var1);
         Object var8 = null;
         return new TestConnectionResult(var3, var4, var6, var7, (LocationSpecificConnectionDetails)var8);
      }

      private EntityType restoreEntityType(Persistor var1) {
         String var2 = var1.getString("EntityType");
         checkNull("EntityType", var2);
         return EntityType.valueOf(var2);
      }
   }
}
