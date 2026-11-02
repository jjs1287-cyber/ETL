package org.etlforge.integration.data.function;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.structure.DataStructure;

public final class UndefinedFunction implements Function {
   public static final String NAME = "(none)";
   private final IntegrationEntityId id;

   public UndefinedFunction(IntegrationEntityId var1) {
      this.id = Preconditions.checkNotNull(var1);
   }

   @Override
   public IntegrationEntityId getFunctionID() {
      return this.id;
   }

   @Override
   public String getName() {
      return "(none)";
   }

   @Override
   public EntityType getEntityType() {
      return null;
   }

   @Override
   public DataStructure getInputStructure() {
      return null;
   }

   @Override
   public DataStructure getOutputStructure() {
      return null;
   }

   @Override
   public boolean acceptsOutputFrom(Function var1) {
      return false;
   }

   @Override
   public boolean hasRequestTransformation() {
      return false;
   }
}
