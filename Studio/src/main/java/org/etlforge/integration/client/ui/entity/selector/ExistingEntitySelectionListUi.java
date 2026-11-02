package org.etlforge.integration.client.ui.entity.selector;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;

public class ExistingEntitySelectionListUi<T extends IntegrationEntity> extends ExistingEntitySelectionUi<T> {
   public ExistingEntitySelectionListUi(IntegrationProject var1, Class<T> var2, String var3, String var4) {
      super(var1, var2, var3, var4);
   }

   public ExistingEntitySelectionListUi(IntegrationProject var1, Class<T> var2, EntityType var3) {
      super(var1, var2, var3);
   }

   @Override
   protected EntitySelector<T> createEntityDisplayer(IntegrationProject var1, Class<T> var2, String var3) {
      return new EntityListSelector<>(var1, var2);
   }
}
