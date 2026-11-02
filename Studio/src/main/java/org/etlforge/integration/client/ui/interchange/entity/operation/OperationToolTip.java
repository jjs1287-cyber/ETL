package org.etlforge.integration.client.ui.interchange.entity.operation;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.id.ScheduleId;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class OperationToolTip extends IntegrationEntityToolTip {
   public OperationToolTip(Operation var1) {
      super(var1);
      this.addType(var1);
      this.addScheduleInfo(var1);
   }

   private void addType(Operation var1) {
      this.addEntry("Activity:", var1.getOperationType());
   }

   private void addScheduleInfo(Operation var1) {
      String var2 = this.getScheduleString(var1);
      this.addEntry("Runs on schedule:", var2);
   }

   private String getScheduleString(Operation var1) {
      ScheduleId var2 = var1.getScheduleId();
      if (var2 != null) {
         IntegrationProject var3 = ProjectUtils.getProject(var1);
         if (var3 != null) {
            Schedule var4 = var3.getItemLookup().getEntity(var2, Schedule.class);
            if (var4 != null) {
               return var4.getName();
            }
         }

         return "Yes";
      } else {
         return "No";
      }
   }
}
