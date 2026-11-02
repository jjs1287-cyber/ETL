package org.etlforge.integration.server.implementation.webservice.admin.project.impl;

import java.util.Date;
import org.etlforge.integration.client.scheduler.ScheduledRun;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Schedule;

class ScheduledRunImpl implements ScheduledRun {
   private final Date timeOfRun;
   private final Operation operation;
   private final Schedule schedule;

   public ScheduledRunImpl(Date var1, Operation var2, Schedule var3) {
      this.timeOfRun = new Date(var1.getTime());
      this.operation = var2;
      this.schedule = var3;
   }

   @Override
   public Operation getOperation() {
      return this.operation;
   }

   @Override
   public Schedule getSchedule() {
      return this.schedule;
   }

   @Override
   public Date getTimeOfRun() {
      return new Date(this.timeOfRun.getTime());
   }
}
