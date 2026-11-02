package org.etlforge.integration.client.ui.entity.selector;

import org.etlforge.integration.client.ui.entity.tree.ProjectPathFilterFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;

public final class ExistingEntityTreeSelectionUi<T extends IntegrationEntity> extends ExistingEntitySelectionUi<T> {
   public ExistingEntityTreeSelectionUi(IntegrationProject var1, Class<T> var2, String var3, String var4) {
      super(var1, var2, var3, var4);
   }

   public ExistingEntityTreeSelectionUi(IntegrationProject var1, Class<T> var2, EntityType var3) {
      super(var1, var2, var3);
   }

   @Override
   protected EntitySelector<T> createEntityDisplayer(IntegrationProject var1, Class<T> var2, String var3) {
      EntityTreeSelector var4 = new EntityTreeSelector(var1, var2);
      var4.setProjectVisible(false);
      var4.setIncludeToolbar(true);
      var4.setToolbarTitle(var3);
      var4.setTreeFilterFunction(new ProjectPathFilterFunction(var1));
      return var4;
   }
}
