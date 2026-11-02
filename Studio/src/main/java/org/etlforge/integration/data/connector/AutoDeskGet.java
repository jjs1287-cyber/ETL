package org.etlforge.integration.data.connector;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.AutoDeskGetId;

public final class AutoDeskGet extends AutoDeskFunction {
   @IdProperty
   public static final String AutoDesk_GET_ID = "autodeskget_id";
   @NameProperty
   public static final String AutoDesk_GET_NAME = "autodeskget_name";

   public AutoDeskGet(String var1) {
      super(null, var1, EntityType.AutoDeskGet);
   }

   public AutoDeskGet(AutoDeskGetId var1, String var2) {
      super(var1, var2, EntityType.AutoDeskGet);
   }

   public AutoDeskGetId getID() {
      return (AutoDeskGetId)super.getID();
   }

   @Override
   public AutoDeskFunction.FunctionType getFunctionType() {
      return AutoDeskFunction.FunctionType.GET;
   }

   @Override
   public String[] getPropertyKeys() {
      return this.getPropertyKeys("autodeskget_id", "autodeskget_name");
   }

   @Override
   public boolean hasRequestTransformation() {
      return true;
   }
}
