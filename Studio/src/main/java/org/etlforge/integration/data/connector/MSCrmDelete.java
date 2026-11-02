package org.etlforge.integration.data.connector;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.MSCrmDeleteId;
import org.etlforge.integration.mscrm.FunctionType;

public final class MSCrmDelete extends MSCrmFunction {
   @IdProperty
   public static final String MSCRM_DELETE_ID = "mscrmdelete_id";
   @NameProperty
   public static final String MSCRM_DELETE_NAME = "mscrmdelete_name";

   public MSCrmDelete(String var1) {
      super(null, var1, EntityType.MSCrmDelete);
   }

   public MSCrmDelete(MSCrmDeleteId var1, String var2) {
      super(var1, var2, EntityType.MSCrmDelete);
   }

   public MSCrmDeleteId getID() {
      return (MSCrmDeleteId)super.getID();
   }

   @Override
   public FunctionType getFunctionType() {
      return FunctionType.DELETE;
   }

   @Override
   public String[] getPropertyKeys() {
      return this.getPropertyKeys("mscrmdelete_id", "mscrmdelete_name");
   }
}
