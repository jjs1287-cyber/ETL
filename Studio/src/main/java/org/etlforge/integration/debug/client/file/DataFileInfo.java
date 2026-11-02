package org.etlforge.integration.debug.client.file;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;

public final class DataFileInfo implements Serializable {
   private static final long serialVersionUID = -5324997600091814744L;
   private final DataFileType type;
   private final int numberOfChunks;

   public DataFileInfo(DataFileType var1, int var2) {
      Preconditions.checkNotNull(var1, "type");
      Preconditions.checkArgument(var2 >= 0, "numberOfChunks must be >= 0");
      this.type = var1;
      this.numberOfChunks = var2;
   }

   public DataFileType getType() {
      return this.type;
   }

   public int getNumberOfChunks() {
      return this.numberOfChunks;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.type, this.numberOfChunks);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof DataFileInfo)) {
         return false;
      }

      DataFileInfo var2 = (DataFileInfo)var1;
      return this.type == var2.type && this.numberOfChunks == var2.numberOfChunks;
   }
}
