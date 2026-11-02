package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.integration.client.ui.interchange.entity.transformation.resources.TransformationResources;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.prefs.DrawAttributeMappingsPreference;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.prefs.DrawErrorMappingsPreference;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.prefs.DrawGeneratorMappingsPreference;

public enum MapAreaDisplayOption {
   ATTRIBUTES,
   GENERATORS,
   ERRORS;

   @Override
   public String toString() {
      return TransformationResources.getString("MapAreaDisplayOption." + this.name());
   }

   public BooleanPreference getPreference() {
      switch (this) {
         case ATTRIBUTES:
            return DrawAttributeMappingsPreference.INSTANCE;
         case GENERATORS:
            return DrawGeneratorMappingsPreference.INSTANCE;
         case ERRORS:
            return DrawErrorMappingsPreference.INSTANCE;
         default:
            throw new RuntimeException("Unexpected MapAreaDisplayOption: " + this.name());
      }
   }
}
