package org.etlforge.integration.client.ui.interchange.component.find;

import java.util.Collection;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;

public final class MappingSearchType extends AbstractScriptBasedSearchType {
   public MappingSearchType() {
      super("Transformation Mappings", EntityType.Transformation);
   }

   @Override
   protected Collection<String> getRawStrings(IntegrationEntity var1) {
      return ((Transformation)var1).getMapList();
   }
}
