package org.etlforge.integration.data.entity.transformation;

import com.google.common.base.Objects;
import org.apache.commons.lang.StringUtils;

public final class SourceBatchParams implements Cloneable {
   public static final SourceBatchParams NONE = new SourceBatchParams(null, 0);
   private final String nodePath;
   private final int size;

   public SourceBatchParams(String var1, int var2) {
      this.validateArguments(var1, var2);
      this.nodePath = var1 != null ? var1.trim() : "";
      this.size = Math.max(0, var2);
   }

   private void validateArguments(String var1, int var2) {
      if (StringUtils.isBlank(var1)) {
         if (var2 > 0) {
            throw new IllegalArgumentException("Conflicting arguments: nodePath is null or empty, but size is > 0");
         }
      } else if (var2 <= 0) {
         throw new IllegalArgumentException("Conflicting arguments: nodePath has a non-blank value, but size is <= 0");
      }
   }

   public String getNodePath() {
      return this.nodePath;
   }

   public int getBatchSize() {
      return this.size;
   }

   public SourceBatchParams clone() {
      try {
         return (SourceBatchParams)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof SourceBatchParams)) {
         return false;
      }

      SourceBatchParams var2 = (SourceBatchParams)var1;
      return this.nodePath.equals(var2.nodePath) && this.size == var2.size;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.nodePath, this.size);
   }

   @Override
   public String toString() {
      return this.nodePath.isEmpty() ? "[None]" : "Node path: " + this.nodePath + ", batch size: " + this.size;
   }
}
