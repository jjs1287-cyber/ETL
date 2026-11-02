package org.etlforge.integration.client.mapping.db;

import com.google.common.collect.ImmutableList;
import javax.annotation.Nullable;
import org.etlforge.integration.client.mapping.AbstractMappingStructureFactory;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;

public final class DbMappingStructureFactory extends AbstractMappingStructureFactory {
   @Override
   public Iterable<? extends Class<? extends DataStructure>> getSupportedStructures() {
      return ImmutableList.of(DatabaseStructure.class);
   }

   @Nullable
   @Override
   public MappingTreeStructure getInputStructure(Transformation var1) throws MappingTreeStructureException {
      return this.getStructure((DatabaseStructure)var1.getInputStructure());
   }

   @Nullable
   @Override
   public MappingTreeStructure getOutputStructure(Transformation var1) throws MappingTreeStructureException {
      return this.getStructure((DatabaseStructure)var1.getOutputStructure());
   }

   @Nullable
   private MappingTreeStructure getStructure(DatabaseStructure var1) throws MappingTreeStructureException {
      DatabaseStructureMappingTreeConverter var2 = new DatabaseStructureMappingTreeConverter(this.getLogger());
      return var2.convert(var1);
   }
}
