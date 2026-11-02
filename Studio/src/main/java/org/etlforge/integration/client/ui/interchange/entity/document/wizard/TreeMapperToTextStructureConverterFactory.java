package org.etlforge.integration.client.ui.interchange.entity.document.wizard;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperNode;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.util.exception.NotSupportedException;

public class TreeMapperToTextStructureConverterFactory {
   public TreeMapperToTextStructureConverter newSourceStructureConverter(Transformation var1, TreeMapperNode var2) throws NotSupportedException {
      DataStructure var3 = var1.getInputStructure();
      return this.newConverterImpl(var2, var3);
   }

   public TreeMapperToTextStructureConverter newTargetStructureConverter(Transformation var1, TreeMapperNode var2) throws NotSupportedException {
      DataStructure var3 = var1.getOutputStructure();
      return this.newConverterImpl(var2, var3);
   }

   private TreeMapperToTextStructureConverter newConverterImpl(TreeMapperNode var1, DataStructure var2) throws NotSupportedException {
      DataStructureType var3 = var2.getStructureType();
      switch (var3) {
         case Database:
            return new DatabaseToTextStructureConverter(var1);
         case None:
            throw new NotSupportedException("No structure to convert.");
         default:
            throw new NotSupportedException("Unsupported conversion: " + var3 + " to Text");
      }
   }
}
