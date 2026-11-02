package org.etlforge.integration.client.mapping;

import com.google.common.collect.ImmutableList;
import javax.annotation.Nullable;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.NoStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureFactory;

final class NoMappingStructureFactory implements MappingStructureFactory {
   @Override
   public Iterable<? extends Class<? extends DataStructure>> getSupportedStructures() {
      return ImmutableList.of(NoStructure.class);
   }

   @Nullable
   @Override
   public MappingTreeStructure getInputStructure(Transformation var1) {
      return this.createStructure(false);
   }

   @Nullable
   @Override
   public MappingTreeStructure getOutputStructure(Transformation var1) {
      return this.createStructure(true);
   }

   private MappingTreeStructure createStructure(boolean var1) {
      String var2 = NoStructure.getJtr(var1);
      return MappingTreeStructureFactory.fromString(var2);
   }
}
