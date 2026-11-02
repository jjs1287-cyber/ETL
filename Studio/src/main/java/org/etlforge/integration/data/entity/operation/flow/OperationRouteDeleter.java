package org.etlforge.integration.data.entity.operation.flow;

class OperationRouteDeleter {
   private final OperationWrapper from;

   public OperationRouteDeleter(OperationWrapper var1) {
      this.from = var1;
   }

   public boolean deleteRoute(Route var1) {
      if (this.from.removeOutgoingRoute(var1)) {
         switch (var1.getType()) {
            case SUCCESS:
               this.updateSuccessProperty(var1);
               break;
            case FAILURE:
               this.updateFailureProperty(var1);
               break;
            case SIBLING:
               this.from.removeSiblingOperation(((OperationWrapper)var1.getTo()).getActivity());
               break;
            case SCRIPTED:
               return true;
         }
      }

      return false;
   }

   private void updateSuccessProperty(Route var1) {
      boolean var2 = var1.getTo() instanceof OperationWrapper;
      if (var2) {
         this.from.setSuccessOperationId(null);
      } else {
         this.from.setSuccessEmailId(null);
      }
   }

   private void updateFailureProperty(Route var1) {
      boolean var2 = var1.getTo() instanceof OperationWrapper;
      if (var2) {
         this.from.setFailureOperationId(null);
      } else {
         this.from.setFailureEmailId(null);
      }
   }
}
