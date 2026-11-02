package org.etlforge.integration.data.connector;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.MSGPUpdateId;

public final class MSGPUpdate extends MSGPFunction {
   @IdProperty
   public static final String MSGP_UPDATE_ID = "msgpupdate_id";
   @NameProperty
   public static final String MSGP_UPDATE_NAME = "msgpupdate_name";

   public MSGPUpdate(String var1) {
      super(null, var1, EntityType.MSGPUpdate);
   }

   public MSGPUpdate(MSGPUpdateId var1, String var2) {
      super(var1, var2, EntityType.MSGPUpdate);
   }

   public MSGPUpdateId getID() {
      return (MSGPUpdateId)super.getID();
   }

   @Override
   public MSGPFunction.FunctionType getFunctionType() {
      return MSGPFunction.FunctionType.UPDATE;
   }

   @Override
   public String[] getPropertyKeys() {
      return this.getPropertyKeys("msgpupdate_id", "msgpupdate_name");
   }
}
