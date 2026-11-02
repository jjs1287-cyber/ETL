package org.etlforge.integration.data.connector;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.MSCrmCreateId;
import org.etlforge.integration.mscrm.FunctionType;

public final class MSCrmCreate extends MSCrmFunction {
   @IdProperty
   public static final String MSCRM_CREATE_ID = "mscrmcreate_id";
   @NameProperty
   public static final String MSCRM_CREATE_NAME = "mscrmcreate_name";

   public MSCrmCreate(String var1) {
      super(null, var1, EntityType.MSCrmCreate);
   }

   public MSCrmCreate(MSCrmCreateId var1, String var2) {
      super(var1, var2, EntityType.MSCrmCreate);
   }

   public MSCrmCreateId getID() {
      return (MSCrmCreateId)super.getID();
   }

   @Override
   public FunctionType getFunctionType() {
      return FunctionType.CREATE;
   }

   @Override
   public String[] getPropertyKeys() {
      return this.getPropertyKeys("mscrmcreate_id", "mscrmcreate_name");
   }
}
