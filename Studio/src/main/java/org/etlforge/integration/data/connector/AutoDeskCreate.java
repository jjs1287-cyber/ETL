package org.etlforge.integration.data.connector;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.AutoDeskCreateId;

public final class AutoDeskCreate extends AutoDeskFunction {
   @IdProperty
   public static final String AutoDesk_CREATE_ID = "autodeskcreate_id";
   @NameProperty
   public static final String AutoDesk_CREATE_NAME = "autodeskcreate_name";

   public AutoDeskCreate(String var1) {
      super(null, var1, EntityType.AutoDeskCreate);
   }

   public AutoDeskCreate(AutoDeskCreateId var1, String var2) {
      super(var1, var2, EntityType.AutoDeskCreate);
   }

   public AutoDeskCreateId getID() {
      return (AutoDeskCreateId)super.getID();
   }

   @Override
   public AutoDeskFunction.FunctionType getFunctionType() {
      return AutoDeskFunction.FunctionType.CREATE;
   }

   @Override
   public String[] getPropertyKeys() {
      return this.getPropertyKeys("autodeskcreate_id", "autodeskcreate_name");
   }
}
