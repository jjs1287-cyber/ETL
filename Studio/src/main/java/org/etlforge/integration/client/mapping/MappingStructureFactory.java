package org.etlforge.integration.client.mapping;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;

@ParametersAreNonnullByDefault
public interface MappingStructureFactory {
   Iterable<? extends Class<? extends DataStructure>> getSupportedStructures();

   @Nullable
   MappingTreeStructure getInputStructure(Transformation var1) throws MappingTreeStructureException;

   @Nullable
   MappingTreeStructure getOutputStructure(Transformation var1) throws MappingTreeStructureException;
}
