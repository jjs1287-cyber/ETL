package org.etlforge.integration.data.connector;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.MSGPDeleteId;

public final class MSGPDelete extends MSGPFunction {
   @IdProperty
   public static final String MSGP_DELETE_ID = "msgpdelete_id";
   @NameProperty
   public static final String MSGP_DELETE_NAME = "msgpdelete_name";

   public MSGPDelete(String var1) {
      super(null, var1, EntityType.MSGPDelete);
   }

   public MSGPDelete(MSGPDeleteId var1, String var2) {
      super(var1, var2, EntityType.MSGPDelete);
   }

   public MSGPDeleteId getID() {
      return (MSGPDeleteId)super.getID();
   }

   @Override
   public MSGPFunction.FunctionType getFunctionType() {
      return MSGPFunction.FunctionType.DELETE;
   }

   @Override
   public String[] getPropertyKeys() {
      return this.getPropertyKeys("msgpdelete_id", "msgpdelete_name");
   }
}
