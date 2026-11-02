package org.etlforge.integration.data.connector;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.AutoDeskDeleteId;

public final class AutoDeskDelete extends AutoDeskFunction {
   @IdProperty
   public static final String AutoDesk_DELETE_ID = "autodeskdelete_id";
   @NameProperty
   public static final String AutoDesk_DELETE_NAME = "autodeskdelete_name";

   public AutoDeskDelete(String var1) {
      super(null, var1, EntityType.AutoDeskDelete);
   }

   public AutoDeskDelete(AutoDeskDeleteId var1, String var2) {
      super(var1, var2, EntityType.AutoDeskDelete);
   }

   public AutoDeskDeleteId getID() {
      return (AutoDeskDeleteId)super.getID();
   }

   @Override
   public AutoDeskFunction.FunctionType getFunctionType() {
      return AutoDeskFunction.FunctionType.DELETE;
   }

   @Override
   public String[] getPropertyKeys() {
      return this.getPropertyKeys("autodeskdelete_id", "autodeskdelete_name");
   }
}
