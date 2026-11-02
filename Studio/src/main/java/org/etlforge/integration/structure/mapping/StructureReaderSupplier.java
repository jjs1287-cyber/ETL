package org.etlforge.integration.structure.mapping;

import com.google.common.base.Preconditions;
import com.google.common.io.InputSupplier;
import java.io.IOException;
import java.io.Reader;

public final class StructureReaderSupplier implements InputSupplier<Reader> {
   private final MappingTreeStructure jtr;

   public StructureReaderSupplier(MappingTreeStructure var1) {
      this.jtr = Preconditions.checkNotNull(var1);
   }

   public Reader getInput() throws IOException {
      return this.jtr.getReader();
   }
}
