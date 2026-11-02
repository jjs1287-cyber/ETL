package org.etlforge.integration.client.ui.entity.search;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.entity.search.ReplaceableProperty;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;

final class ReplaceMemento {
   private final EntityPersistor saver;
   private final ReplaceableProperty property;
   private final Map<IntegrationEntity, Object> mementos;

   public ReplaceMemento(EntityPersistor var1, ReplaceableProperty var2, Collection<IntegrationEntity> var3) {
      this.saver = var1;
      this.property = var2;
      this.mementos = Maps.newHashMap();

      for (IntegrationEntity var5 : var3) {
         Object var6 = var2.getValue(var5);
         this.mementos.put(var5, var6);
      }
   }

   public void restore() {
      for (Entry var2 : this.mementos.entrySet()) {
         IntegrationEntity var3 = (IntegrationEntity)var2.getKey();
         Object var4 = var2.getValue();
         this.property.setValue(var3, var4);

         try {
            this.saver.save(var3);
         } catch (InterchangeSavingException var6) {
            ErrorLog.attention(this, null, var6);
         }
      }
   }
}
