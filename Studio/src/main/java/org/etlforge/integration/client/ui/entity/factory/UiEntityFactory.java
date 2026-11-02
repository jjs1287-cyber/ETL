package org.etlforge.integration.client.ui.entity.factory;

import org.etlforge.integration.data.entity.EntityType;

public interface UiEntityFactory {
   void createEntity(EntityType var1, UiEntityFactoryCallback var2);

   void createEntity(Request var1, UiEntityFactoryCallback var2);
}
