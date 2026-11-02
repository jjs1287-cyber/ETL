package org.etlforge.integration.debug.client.file;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.TransformationId;

public final class DataFileIdentifier {
   public final OperationId operationId;
   public final TransformationId transformationId;
   public final DataFileType type;
   public final int instanceNumber;
   public final int chunkNumber;

   public static DataFileIdentifier forLoadSource(TransformationId var0) {
      return new DataFileIdentifier(null, var0, DataFileType.SOURCE, 1, 1);
   }

   public DataFileIdentifier(OperationId var1, TransformationId var2, DataFileType var3, int var4, int var5) {
      Preconditions.checkNotNull(var2, "transformationId");
      Preconditions.checkNotNull(var3, "type");
      Preconditions.checkArgument(var4 >= 1, "instanceNumber must be >= 1");
      Preconditions.checkArgument(var5 >= 0, "chunkNumber must be >= 0");
      this.operationId = var1;
      this.transformationId = var2;
      this.type = var3;
      this.instanceNumber = var4;
      this.chunkNumber = var5;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.operationId, this.transformationId, this.type, this.instanceNumber, this.chunkNumber);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof DataFileIdentifier)) {
         return false;
      }

      DataFileIdentifier var2 = (DataFileIdentifier)var1;
      return Objects.equal(this.operationId, var2.operationId)
         && this.transformationId.equals(var2.transformationId)
         && this.type == var2.type
         && this.instanceNumber == var2.instanceNumber
         && this.chunkNumber == var2.chunkNumber;
   }
}
