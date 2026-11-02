package org.etlforge.integration.client.ui.structure.editor;

import java.util.List;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.structure.mapping.InvalidMapping;

public class EditorMemento {
   private final Transformation transformation;
   private final SourceTarget st;
   private final DataStructure structure;
   private final List<String> mappings;
   private final List<InvalidMapping> invalidMappings;

   public EditorMemento(Transformation var1, SourceTarget var2) {
      this.transformation = var1;
      this.st = var2;
      if (var2 == SourceTarget.Source) {
         this.structure = var1.getInputStructure();
      } else {
         this.structure = var1.getOutputStructure();
      }

      this.mappings = var1.getMapList();
      this.invalidMappings = var1.getInvalidMappings();
   }

   public void restore() {
      if (this.st == SourceTarget.Source) {
         this.transformation.setInputStructure(this.structure);
      } else {
         this.transformation.setOutputStructure(this.structure);
      }

      this.transformation.setMapList(this.mappings);
      this.transformation.setInvalidMappings(this.invalidMappings);
   }

   protected final Transformation getTransformation() {
      return this.transformation;
   }

   protected final SourceTarget getUse() {
      return this.st;
   }
}
