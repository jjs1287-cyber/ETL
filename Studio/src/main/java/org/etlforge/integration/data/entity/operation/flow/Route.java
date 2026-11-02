package org.etlforge.integration.data.entity.operation.flow;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.script.ScriptedLinkSource;

public final class Route {
   private final OperationWrapper from;
   private final PipelineActivity sourceActivity;
   private final OperationActivityWrapper to;
   private final RouteType type;

   public Route(OperationWrapper var1, OperationActivityWrapper var2, RouteType var3) {
      this(var1, var2, null, var3);
   }

   public static Route fromScriptSource(PipelineActivity var0, OperationWrapper var1, OperationWrapper var2) {
      Preconditions.checkArgument(var0.getContent() instanceof ScriptedLinkSource, "The PipelineActivity does not represent a ScriptedLinkSource");
      return new Route(var1, var2, var0, RouteType.SCRIPTED);
   }

   private Route(OperationWrapper var1, OperationActivityWrapper var2, PipelineActivity var3, RouteType var4) {
      this.from = Preconditions.checkNotNull(var1);
      this.to = Preconditions.checkNotNull(var2);
      this.type = Preconditions.checkNotNull(var4);
      this.sourceActivity = var3;
   }

   public final OperationWrapper getFrom() {
      return this.from;
   }

   public final PipelineActivity getSourceActivity() {
      return this.sourceActivity;
   }

   public final OperationActivityWrapper getTo() {
      return this.to;
   }

   public final RouteType getType() {
      return this.type;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof Route ? this.equals((Route)var1) : false;
      }
   }

   private boolean equals(Route var1) {
      return this.to.equals(var1.to) && this.from.equals(var1.from) && this.sourceActivity == var1.sourceActivity && this.type == var1.type;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.from, this.to, this.sourceActivity, this.type);
   }

   @Override
   public String toString() {
      return "[" + this.type + "] " + this.from.getName() + " - " + this.to;
   }
}
