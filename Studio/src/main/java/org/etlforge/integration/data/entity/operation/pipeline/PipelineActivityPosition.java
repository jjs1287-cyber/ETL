package org.etlforge.integration.data.entity.operation.pipeline;

import com.google.common.base.Objects;

public final class PipelineActivityPosition {
   private static final String DELIMITER = "|";
   private static final String DELIMITER_REGEX = "\\|";
   private final String type;
   private final int count;

   PipelineActivityPosition(String var1, int var2) {
      this.type = var1;
      this.count = var2;
   }

   public static final PipelineActivityPosition fromString(String var0) {
      String[] var1 = var0.split("\\|");
      return new PipelineActivityPosition(var1[0], Integer.parseInt(var1[1]));
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof PipelineActivityPosition)) {
         return false;
      }

      PipelineActivityPosition var2 = (PipelineActivityPosition)var1;
      return this.type.equals(var2.type) && this.count == var2.count;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.type, this.count);
   }

   @Override
   public String toString() {
      return this.type + "|" + this.count;
   }
}
