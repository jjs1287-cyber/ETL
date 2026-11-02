package org.etlforge.integration.client.ui.interchange.entity.document.page;

import java.util.concurrent.Callable;
import org.etlforge.integration.client.mapping.text.ComplexTextStructureMappingTreeConverter;
import org.etlforge.integration.client.mapping.text.SimpleTextStructureMappingTreeConverter;
import org.etlforge.integration.data.structure.ComplexTextStructure;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.system.callback.CallbackResultAdapter;

final class MappingTreeStructureBuilder implements Callable<MappingTreeStructure> {
   private final TextStructure structure;

   public MappingTreeStructureBuilder(TextStructure var1) {
      this.structure = var1;
   }

   public MappingTreeStructure call() {
      CallbackResultAdapter var1 = new CallbackResultAdapter() {};
      return this.structure instanceof SimpleTextStructure
         ? SimpleTextStructureMappingTreeConverter.convert((SimpleTextStructure)this.structure, var1)
         : ComplexTextStructureMappingTreeConverter.convert((ComplexTextStructure)this.structure, var1);
   }
}
