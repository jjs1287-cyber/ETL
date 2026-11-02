package org.etlforge.integration.salesforce.bulkload;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.etlforge.integration.salesforce.DateConversionParams;

public final class FileSplitterParams {
   public final HeaderMappings mappings;
   public final BatchFileParams batchFileParams;
   public final Optional<DateConversionParams> dateConversionParams;

   public static FileSplitterParams defaultParams(HeaderMappings var0) {
      return new FileSplitterParams(var0, BatchFileParams.DEFAULT, Optional.absent());
   }

   public FileSplitterParams(HeaderMappings var1, BatchFileParams var2, Optional<DateConversionParams> var3) {
      this.mappings = Preconditions.checkNotNull(var1, "mappings");
      this.batchFileParams = Preconditions.checkNotNull(var2, "batchFileParams");
      this.dateConversionParams = Preconditions.checkNotNull(var3, "dateConversionParams");
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.mappings, this.batchFileParams, this.dateConversionParams);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof FileSplitterParams)) {
         return false;
      }

      FileSplitterParams var2 = (FileSplitterParams)var1;
      return Objects.equal(this.mappings, var2.mappings)
         && this.batchFileParams.equals(var2.batchFileParams)
         && this.dateConversionParams.equals(var2.dateConversionParams);
   }
}
