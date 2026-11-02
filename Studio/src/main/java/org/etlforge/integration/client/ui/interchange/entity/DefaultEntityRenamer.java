package org.etlforge.integration.client.ui.interchange.entity;

import org.etlforge.application.ui.Alert;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.util.name.IllegalNameException;

public final class DefaultEntityRenamer implements EntityRenamer {
   private final EntityPersistor persistor;

   public DefaultEntityRenamer(EntityPersistor var1) {
      this.persistor = var1;
   }

   @Override
   public boolean setNewName(IntegrationEntity var1, String var2) {
      String var3 = var1.getName();

      try {
         var1.setName(var2);
         this.persistor.save(var1);
         return true;
      } catch (IllegalNameException var5) {
         Alert.error("Invalid Name", var5.getMessage());
      } catch (InterchangeSavingException var6) {
         var1.setName(var3);
      }

      return false;
   }
}
