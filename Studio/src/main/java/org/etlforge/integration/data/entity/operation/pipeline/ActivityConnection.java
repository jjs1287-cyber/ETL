package org.etlforge.integration.data.entity.operation.pipeline;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public final class ActivityConnection {
   private final PipelineActivity from;
   private final PipelineActivity to;

   public ActivityConnection(PipelineActivity var1, PipelineActivity var2) {
      Preconditions.checkArgument(var1 != null || var2 != null, "from and to cannot both be null");
      this.from = var1;
      this.to = var2;
   }

   public PipelineActivity getFrom() {
      return this.from;
   }

   public PipelineActivity getTo() {
      return this.to;
   }

   public boolean isIsolated() {
      return this.from == null && this.to == null;
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("Connection");
      if (this.from != null || this.to != null) {
         var1.append(" [");
         var1.append(toString(this.from)).append(" - ").append(toString(this.to));
         var1.append("]");
      }

      return var1.toString();
   }

   private static String toString(PipelineActivity var0) {
      return var0 == null ? " " : var0.getType().toString();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof ActivityConnection ? this.equals((ActivityConnection)var1) : false;
      }
   }

   private boolean equals(ActivityConnection var1) {
      return Objects.equal(this.from, var1.from) && Objects.equal(this.to, var1.to);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.from, this.to);
   }
}
