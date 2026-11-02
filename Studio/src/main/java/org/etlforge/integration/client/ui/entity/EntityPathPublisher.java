package org.etlforge.integration.client.ui.entity;

import org.etlforge.integration.data.entity.EntityPath;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.event.ApplicationMessageEvent;

public class EntityPathPublisher {
   private final Object sender;
   private long lifetime;
   private boolean includeProject;

   public EntityPathPublisher(Object var1) {
      this.sender = var1;
      this.lifetime = 2000L;
   }

   public void setLifeTimeInMillis(long var1) {
      this.lifetime = var1;
   }

   public void setIncludeProject(boolean var1) {
      this.includeProject = var1;
   }

   public void publish(IntegrationEntity var1) {
      if (var1 == null) {
         ApplicationMessageEvent.postInformationMessage(this.sender, " ");
      } else {
         EntityPath var2 = new EntityPath(var1, this.includeProject);
         ApplicationMessageEvent.postInformationMessage(this.sender, var2.toString(), new ApplicationMessageEvent.LifeTime(this.lifetime));
      }
   }
}
