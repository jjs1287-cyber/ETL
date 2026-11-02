package org.etlforge.integration.client.mapping.text;

import com.google.common.collect.ImmutableList;
import javax.annotation.Nullable;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.ComplexTextStructure;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;

public final class TextMappingStructureFactory implements MappingStructureFactory {
   @Override
   public Iterable<? extends Class<? extends DataStructure>> getSupportedStructures() {
      return ImmutableList.of(SimpleTextStructure.class, ComplexTextStructure.class);
   }

   @Nullable
   @Override
   public MappingTreeStructure getInputStructure(Transformation var1) throws MappingTreeStructureException {
      return this.create(var1.getInputStructure());
   }

   @Nullable
   @Override
   public MappingTreeStructure getOutputStructure(Transformation var1) throws MappingTreeStructureException {
      return this.create(var1.getOutputStructure());
   }

   private MappingTreeStructure create(DataStructure var1) {
      if (var1 instanceof SimpleTextStructure) {
         return SimpleTextStructureMappingTreeConverter.convert((SimpleTextStructure)var1, null);
      } else if (var1 instanceof ComplexTextStructure) {
         return ComplexTextStructureMappingTreeConverter.convert((ComplexTextStructure)var1, null);
      } else {
         throw new IllegalArgumentException("Unsupported DataStructure: " + var1);
      }
   }
}
