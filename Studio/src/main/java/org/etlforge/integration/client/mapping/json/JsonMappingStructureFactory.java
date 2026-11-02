package org.etlforge.integration.client.mapping.json;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nullable;
import org.etlforge.integration.client.mapping.AbstractMappingStructureFactory;
import org.etlforge.integration.client.mapping.DefaultMappingStructureCallback;
import org.etlforge.integration.client.mapping.serverapi.transformation.TreeMapperInfoProvider;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.JsonStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;

public final class JsonMappingStructureFactory extends AbstractMappingStructureFactory {
   private final TreeMapperInfoProvider infoProvider;

   public JsonMappingStructureFactory(TreeMapperInfoProvider var1) {
      this.infoProvider = Preconditions.checkNotNull(var1);
   }

   @Override
   public Iterable<? extends Class<? extends DataStructure>> getSupportedStructures() {
      return ImmutableList.of(JsonStructure.class);
   }

   @Nullable
   @Override
   public MappingTreeStructure getInputStructure(Transformation var1) throws MappingTreeStructureException {
      return this.createStructure((JsonStructure)var1.getInputStructure(), false);
   }

   @Nullable
   @Override
   public MappingTreeStructure getOutputStructure(Transformation var1) throws MappingTreeStructureException {
      return this.createStructure((JsonStructure)var1.getOutputStructure(), true);
   }

   @Nullable
   private MappingTreeStructure createStructure(JsonStructure var1, boolean var2) throws MappingTreeStructureException {
      DefaultMappingStructureCallback var3 = new DefaultMappingStructureCallback();
      this.infoProvider.getJtrForStructure(var1.getFileName(), var1.getRootName(), var2, var3);
      return var3.getMappingStructureOrThrow();
   }
}
