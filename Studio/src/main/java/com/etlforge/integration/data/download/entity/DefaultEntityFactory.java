package com.etlforge.integration.data.download.entity;

import com.google.common.base.Throwables;
import com.etlforge.integration.data.download.InvalidDownloadedDataException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.metadata.EntityTypeConfig;

public final class DefaultEntityFactory implements EntityFactory {
   private final EntityType type;
   private final Class<? extends IntegrationEntity> entityClass;
   private final String idKey;
   private final String nameKey;

   public DefaultEntityFactory(EntityType var1) {
      this.type = var1;

      try {
         this.entityClass = EntityTypeConfig.getEntityClass(var1);
      } catch (ClassNotFoundException var3) {
         throw new RuntimeException(var3);
      }

      this.idKey = this.getPropertyKey(IdProperty.class);
      this.nameKey = this.getPropertyKey(NameProperty.class);
   }

   private final String getPropertyKey(Class<? extends Annotation> var1) {
      try {
         for (Field var5 : this.entityClass.getFields()) {
            if (var5.isAnnotationPresent(var1)) {
               return (String)var5.get(null);
            }
         }
      } catch (Exception var6) {
         Throwables.propagate(var6);
      }

      throw new RuntimeException("The class " + var1.getName() + " does not contain a field annotated by " + var1.getName() + ".");
   }

   @Override
   public IntegrationEntity createItem(StringKeyValuePairs var1) throws InvalidDownloadedDataException {
      IntegrationEntityId var2 = DownloadedDataUtils.getEntityId(var1, this.idKey, this.type);
      String var3 = DownloadedDataUtils.getRequiredValue(var1, this.nameKey);

      try {
         Constructor var4 = this.entityClass.getConstructor(var2.getClass(), String.class);
         IntegrationEntity var5 = (IntegrationEntity)var4.newInstance(var2, var3);
         var5.setPropertyValues(var1);
         return var5;
      } catch (Exception var6) {
         throw Throwables.propagate(var6);
      }
   }
}
