package org.etlforge.integration.client.ui.interchange;

import org.etlforge.application.extension.ExtensionObjectFactory;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;

public final class EntityPersistorExtensionFactory implements ExtensionObjectFactory {
   private final IntegrationProject project;
   private final EntityPersistor persistor;

   public EntityPersistorExtensionFactory(IntegrationProject var1, EntityPersistor var2) {
      this.project = var1;
      this.persistor = var2;
   }

   @Override
   public Class[] getExtensionTypes() {
      return new Class[]{EntityPersistor.class};
   }

   @Override
   public Object getExtensionObject(Object var1, Class var2) {
      if (EntityPersistor.class.equals(var2)) {
         IntegrationEntity var3 = (IntegrationEntity)var1;
         if (ProjectUtils.getProject(var3) == this.project) {
            return this.persistor;
         }
      }

      return null;
   }
}
