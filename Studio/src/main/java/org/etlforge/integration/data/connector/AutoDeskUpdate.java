package org.etlforge.integration.data.connector;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.AutoDeskUpdateId;

public final class AutoDeskUpdate extends AutoDeskFunction {
   @IdProperty
   public static final String AutoDesk_UPDATE_ID = "autodeskupdate_id";
   @NameProperty
   public static final String AutoDesk_UPDATE_NAME = "autodeskupdate_name";

   public AutoDeskUpdate(String var1) {
      super(null, var1, EntityType.AutoDeskUpdate);
   }

   public AutoDeskUpdate(AutoDeskUpdateId var1, String var2) {
      super(var1, var2, EntityType.AutoDeskUpdate);
   }

   public AutoDeskUpdateId getID() {
      return (AutoDeskUpdateId)super.getID();
   }

   @Override
   public AutoDeskFunction.FunctionType getFunctionType() {
      return AutoDeskFunction.FunctionType.UPDATE;
   }

   @Override
   public String[] getPropertyKeys() {
      return this.getPropertyKeys("autodeskupdate_id", "autodeskupdate_name");
   }
}
