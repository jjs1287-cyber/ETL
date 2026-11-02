package org.etlforge.integration.client.mapping.text;

import com.google.common.collect.ImmutableList;
import javax.annotation.Nullable;
import org.etlforge.integration.client.mapping.AbstractMappingStructureFactory;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.system.callback.CallbackResultAdapter;

public final class SimpleTextMappingStructureFactory extends AbstractMappingStructureFactory {
   @Override
   public Iterable<? extends Class<? extends DataStructure>> getSupportedStructures() {
      return ImmutableList.of(SimpleTextStructure.class);
   }

   @Nullable
   @Override
   public MappingTreeStructure getInputStructure(Transformation var1) {
      return this.createStructure((SimpleTextStructure)var1.getInputStructure());
   }

   @Nullable
   @Override
   public MappingTreeStructure getOutputStructure(Transformation var1) {
      return this.createStructure((SimpleTextStructure)var1.getOutputStructure());
   }

   private MappingTreeStructure createStructure(SimpleTextStructure var1) {
      return SimpleTextStructureMappingTreeConverter.convert(var1, new CallbackResultAdapter() {});
   }
}
