package org.etlforge.integration.data.connector;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.MSCrmUpdateId;
import org.etlforge.integration.mscrm.FunctionType;

public final class MSCrmUpdate extends MSCrmFunction {
   @IdProperty
   public static final String MSCRM_UPDATE_ID = "mscrmupdate_id";
   @NameProperty
   public static final String MSCRM_UPDATE_NAME = "mscrmupdate_name";

   public MSCrmUpdate(String var1) {
      super(null, var1, EntityType.MSCrmUpdate);
   }

   public MSCrmUpdate(MSCrmUpdateId var1, String var2) {
      super(var1, var2, EntityType.MSCrmUpdate);
   }

   public MSCrmUpdateId getID() {
      return (MSCrmUpdateId)super.getID();
   }

   @Override
   public FunctionType getFunctionType() {
      return FunctionType.UPDATE;
   }

   @Override
   public String[] getPropertyKeys() {
      return this.getPropertyKeys("mscrmupdate_id", "mscrmupdate_name");
   }
}
