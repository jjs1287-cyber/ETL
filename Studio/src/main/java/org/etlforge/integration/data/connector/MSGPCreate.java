package org.etlforge.integration.data.connector;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.MSGPCreateId;

public final class MSGPCreate extends MSGPFunction {
   @IdProperty
   public static final String MSGP_CREATE_ID = "msgpcreate_id";
   @NameProperty
   public static final String MSGP_CREATE_NAME = "msgpcreate_name";

   public MSGPCreate(String var1) {
      super(null, var1, EntityType.MSGPCreate);
   }

   public MSGPCreate(MSGPCreateId var1, String var2) {
      super(var1, var2, EntityType.MSGPCreate);
   }

   public MSGPCreateId getID() {
      return (MSGPCreateId)super.getID();
   }

   @Override
   public MSGPFunction.FunctionType getFunctionType() {
      return MSGPFunction.FunctionType.CREATE;
   }

   @Override
   public String[] getPropertyKeys() {
      return this.getPropertyKeys("msgpcreate_id", "msgpcreate_name");
   }
}
