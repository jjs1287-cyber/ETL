package org.etlforge.integration.data.entity.httpendpoint;

import java.beans.PropertyChangeEvent;
import org.etlforge.integration.data.entity.HttpEndpoint;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.link.AbstractEntityLink;

public class HttpEndpointOperationLink extends AbstractEntityLink {
   public HttpEndpointOperationLink(Operation var1, HttpEndpoint var2) {
      super(var1, var2);
   }

   public HttpEndpoint getDependantObject() {
      return (HttpEndpoint)super.getDependantObject();
   }

   public Operation getMainObject() {
      return (Operation)super.getMainObject();
   }

   @Override
   public boolean isStrong() {
      return false;
   }

   @Override
   protected void mainObjectChanged(PropertyChangeEvent var1) {
      boolean var2 = TriggerableOperationFilter.FILTER.apply(this.getMainObject());
      if (!var2) {
         this.removeOperation();
      }
   }

   @Override
   protected void mainObjectDeleted() {
      super.mainObjectDeleted();
      this.removeOperation();
   }

   private void removeOperation() {
      OperationId var1 = this.getMainObject().getID();
      TriggeredOperations var2 = this.getDependantObject().getTriggeredOperations();
      if (var2.removeOperation(var1)) {
         this.getDependantObject().setTriggeredOperations(var2);
      }
   }
}
