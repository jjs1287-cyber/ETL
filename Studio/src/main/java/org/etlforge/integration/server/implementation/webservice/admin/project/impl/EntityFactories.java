package org.etlforge.integration.server.implementation.webservice.admin.project.impl;

import com.google.common.base.Function;
import com.etlforge.integration.data.download.ItemDownloadConfig;
import com.etlforge.integration.data.download.entity.DefaultEntityFactory;
import com.etlforge.integration.data.download.entity.EntityFactory;
import com.etlforge.integration.data.download.entity.ScriptFactory;
import com.etlforge.integration.data.download.entity.SourceFactory;
import com.etlforge.integration.data.download.entity.TargetFactory;
import org.etlforge.application.extension.ExtensionObjectService;
import org.etlforge.collections.LookupMap;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.lang.exception.NotImplementedYetException;

final class EntityFactories {
   private final LookupMap<EntityType, EntityFactory> factories = LookupMap.withSupplier(new Function<EntityType, EntityFactory>() {
      public EntityFactory apply(EntityType var1) {
         switch (var1) {
            case Source:
               return new SourceFactory();
            case Target:
               return new TargetFactory();
            case Script:
               return new ScriptFactory();
            default:
               ItemDownloadConfig var2 = ExtensionObjectService.getService().getExtensionObject(var1, ItemDownloadConfig.class);
               if (var2 != null) {
                  return var2.getFactory();
               } else {
                  try {
                     return new DefaultEntityFactory(var1);
                  } catch (Exception var4) {
                     throw new NotImplementedYetException("I do not know how to handle an entity of type " + var1 + ": " + var4.getMessage());
                  }
               }
         }
      }
   });

   public EntityFactory forType(EntityType var1) {
      return this.factories.getValue(var1);
   }
}
