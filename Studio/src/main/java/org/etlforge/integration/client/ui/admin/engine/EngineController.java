package org.etlforge.integration.client.ui.admin.engine;

import com.google.common.collect.Maps;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import org.etlforge.integration.client.admin.Admin;
import org.etlforge.integration.client.admin.ServerStatus;
import org.etlforge.integration.client.admin.engine.ServerEngine;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.action.ActionUtils;

public final class EngineController {
   private final ServerEngine engine;
   private final StartEngineAction startAction;
   private final StopEngineAction stopAction;
   private final EnumMap<Admin.StatusDetail, EngineControlAction> statusActions;
   private Admin.StatusDetail lastRequestedDetail;

   public EngineController(ServerEngine var1, Admin var2) {
      this.engine = var1;
      this.startAction = new StartEngineAction(var1, var2);
      this.stopAction = new StopEngineAction(var1, var2);
      this.statusActions = this.createStatusActions(var2);
      var1.addPropertyChangeListener(new EngineController.EngineListener());
      this.updateActions(var1.getStatus());
   }

   private EnumMap<Admin.StatusDetail, EngineControlAction> createStatusActions(Admin var1) {
      EnumMap var2 = Maps.newEnumMap(Admin.StatusDetail.class);
      Receiver var3 = new Receiver<Admin.StatusDetail>() {
         public void handle(Admin.StatusDetail var1) {
            EngineController.this.lastRequestedDetail = var1;
         }
      };

      for (Admin.StatusDetail var7 : Admin.StatusDetail.values()) {
         GetEngineStatusAction var8 = new GetEngineStatusAction(this.engine, var1, var7);
         var8.setCallback(var3);
         var2.put(var7, var8);
      }

      return var2;
   }

   private void updateActions(EnumSet<ServerStatus> var1) {
      this.startAction.updateAccordingToCurrentEngineStatus(var1);
      this.stopAction.updateAccordingToCurrentEngineStatus(var1);

      for (EngineControlAction var3 : this.statusActions.values()) {
         var3.updateAccordingToCurrentEngineStatus(var1);
      }
   }

   public void getEngineStatus() {
      Admin.StatusDetail var1 = this.lastRequestedDetail;
      if (var1 == null) {
         var1 = Admin.StatusDetail.NORMAL;
      }

      ActionUtils.run(this.getStatusAction(var1), this, "get-status");
   }

   public void setDisconnected() {
      this.startAction.setEnabled(false);
      this.stopAction.setEnabled(false);
   }

   public EngineControlAction getStartEngineAction() {
      return this.startAction;
   }

   public EngineControlAction getStopEngineAction() {
      return this.stopAction;
   }

   public EngineControlAction getStatusAction(Admin.StatusDetail var1) {
      return this.statusActions.get(var1);
   }

   public Collection<EngineControlAction> getAllStatusActions() {
      return this.statusActions.values();
   }

   private class EngineListener implements PropertyChangeListener {
      private EngineListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EngineController.this.updateActions(EngineController.this.engine.getStatus());
            }
         });
      }
   }
}
