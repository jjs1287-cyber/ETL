package org.etlforge.integration.data.connector;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.AutoDeskUpsertId;

public final class AutoDeskUpsert extends AutoDeskFunction {
   @IdProperty
   public static final String AutoDesk_UPSERT_ID = "autodeskupsert_id";
   @NameProperty
   public static final String AutoDesk_UPSERT_NAME = "autodeskupsert_name";

   public AutoDeskUpsert(String var1) {
      super(null, var1, EntityType.AutoDeskUpsert);
   }

   public AutoDeskUpsert(AutoDeskUpsertId var1, String var2) {
      super(var1, var2, EntityType.AutoDeskUpsert);
   }

   public AutoDeskUpsertId getID() {
      return (AutoDeskUpsertId)super.getID();
   }

   @Override
   public AutoDeskFunction.FunctionType getFunctionType() {
      return AutoDeskFunction.FunctionType.UPSERT;
   }

   @Override
   public String[] getPropertyKeys() {
      return this.getPropertyKeys("autodeskupsert_id", "autodeskupsert_name");
   }
}
