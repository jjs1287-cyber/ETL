package org.etlforge.integration.data.entity.operation;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.etlforge.integration.data.IntegrationId;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.data.project.ProjectUtils;

public final class OperationFunctionInfo {
   private final Operation operation;
   private final IntegrationEntityLookup dataLookup;

   public OperationFunctionInfo(Operation var1) {
      this(var1, ProjectUtils.getProject(var1).getItemLookup());
   }

   public OperationFunctionInfo(Operation var1, IntegrationEntityLookup var2) {
      this.operation = Preconditions.checkNotNull(var1);
      this.dataLookup = Preconditions.checkNotNull(var2);
   }

   public Function getFirstFunction() {
      List var1 = this.operation.getFunctionIDs();
      return var1.isEmpty() ? null : this.getFunction((IntegrationId)var1.get(0));
   }

   public Function getLastFunction() {
      List var1 = this.operation.getFunctionIDs();
      return var1.isEmpty() ? null : this.getFunction((IntegrationId)var1.get(var1.size() - 1));
   }

   public Function getLastNonPassThroughFunction() {
      List var1 = this.operation.getFunctionIDs();
      Collections.reverse(var1);

      for (Object __etlItem44 : var1) {
         IntegrationId var3 = (IntegrationId) __etlItem44;
         if (!TransformationId.isPassThrough(var3)) {
            return this.getFunction(var3);
         }
      }

      return null;
   }

   private Function getFunction(IntegrationId var1) {
      if (var1 instanceof IntegrationEntityId) {
         IntegrationEntity var2 = this.dataLookup.getEntity((IntegrationEntityId)var1);
         if (var2 instanceof Function) {
            return (Function)var2;
         }
      }

      return null;
   }

   public List<Function> getFunctions() {
      ArrayList var1 = Lists.newArrayList();

      for (IntegrationId var3 : this.operation.getFunctionIDs()) {
         if (var3 instanceof IntegrationEntityId) {
            IntegrationEntity var4 = this.dataLookup.getEntity((IntegrationEntityId)var3);
            if (var4 instanceof Function) {
               var1.add((Function)var4);
            }
         }
      }

      return var1;
   }
}
