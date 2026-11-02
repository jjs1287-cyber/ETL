package org.etlforge.integration.project.upgrade.v_5_0_0_60;

import org.etlforge.integration.data.CC;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;

final class GeneralUpgrade<T extends IntegrationEntity> {
   private final Class<T> entityClass;
   private final String propertyName;

   public static <E extends IntegrationEntity> GeneralUpgrade<E> create(Class<E> var0, String var1) {
      return new GeneralUpgrade<>(var0, var1);
   }

   private GeneralUpgrade(Class<T> var1, String var2) {
      this.entityClass = var1;
      this.propertyName = var2;
   }

   public void run(IntegrationProject var1) {
      for (IntegrationEntity var4 : var1.getAllEntitiesOfClass(this.entityClass)) {
         this.upgrade((T)var4);
      }
   }

   private void upgrade(T var1) {
      synchronized (var1.getDataLock()) {
         String var3 = var1.getProperty(this.propertyName);
         if (var3 != null) {
            String var4 = CC.hide(var3);
            var1.setProperty(this.propertyName, var4);
            var1.setDeployDirty(true);
         }
      }
   }
}
