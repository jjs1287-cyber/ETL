package org.etlforge.integration.debug.client;

import org.etlforge.integration.data.entity.IntegrationEntityId;

public abstract class BreakLocationDecorator implements BreakLocation {
   private final BreakLocation decoratee;

   protected BreakLocationDecorator(BreakLocation var1) {
      this.decoratee = var1;
   }

   @Override
   public IntegrationEntityId getIntegrationEntityId() {
      return this.decoratee == null ? null : this.decoratee.getIntegrationEntityId();
   }

   @Override
   public int getLineNumber() {
      return this.decoratee == null ? -1 : this.decoratee.getLineNumber();
   }

   @Override
   public String getTargetNodePath() {
      return this.decoratee == null ? null : this.decoratee.getTargetNodePath();
   }

   @Override
   public GeneratorInstance getGeneratorInstance() {
      return this.decoratee == null ? null : this.decoratee.getGeneratorInstance();
   }
}
