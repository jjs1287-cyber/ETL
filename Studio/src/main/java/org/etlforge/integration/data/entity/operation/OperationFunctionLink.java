package org.etlforge.integration.data.entity.operation;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.link.AbstractEntityLink;
import org.etlforge.integration.data.function.Function;

public final class OperationFunctionLink extends AbstractEntityLink {
   public OperationFunctionLink(IntegrationEntity var1, Operation var2) {
      super(var1, var2);
      Preconditions.checkArgument(var1 instanceof Function, "func must implement the interface " + Function.class.getName() + ".");
   }

   @Override
   public boolean isStrong() {
      return false;
   }

   public Operation getDependantObject() {
      return (Operation)super.getDependantObject();
   }

   public Function getFunction() {
      return (Function)this.getMainObject();
   }

   @Override
   protected void mainObjectDeleted() {
      this.getDependantObject().functionWasDeleted((Function)this.getMainObject());
      super.mainObjectDeleted();
   }
}
