package org.etlforge.integration.client.ui.interchange.entity.operation.page;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.etlforge.application.ui.action.ApplicationAction;

public final class OperationGraphActions {
   private final Map<String, ApplicationAction> actions = Maps.newHashMap();

   public OperationGraphActions(List<ApplicationAction> var1) {
      for (ApplicationAction var3 : var1) {
         this.actions.put(var3.getID(), var3);
      }
   }

   public ApplicationAction getAction(String var1) {
      return this.actions.get(var1);
   }
}
