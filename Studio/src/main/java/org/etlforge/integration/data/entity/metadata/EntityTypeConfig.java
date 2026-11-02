package org.etlforge.integration.data.entity.metadata;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class EntityTypeConfig {
   public static IntegrationEntity createNewEntity(EntityType var0, String var1) throws ClassNotFoundException {
      try {
         Class var2 = getEntityClass(var0);
         Constructor var3 = var2.getConstructor(String.class);
         return instantiate(var3, var1);
      } catch (SecurityException var4) {
         throw new RuntimeException(var4);
      } catch (NoSuchMethodException var5) {
         throw new RuntimeException(var5);
      }
   }

   public static Class<? extends IntegrationEntity> getEntityClass(EntityType var0) throws ClassNotFoundException {
      Preconditions.checkNotNull(var0, "type");
      Preconditions.checkArgument(var0.canBeInstantiated(), var0 + " cannot be instantiated");
      Class var1 = Class.forName(var0.getImplementingClassName().get());
      assert IntegrationEntity.class.isAssignableFrom(var1);
      return var1.asSubclass(IntegrationEntity.class);
   }

   private static IntegrationEntity instantiate(Constructor<? extends IntegrationEntity> var0, Object... var1) {
      try {
         return (IntegrationEntity)var0.newInstance(var1);
      } catch (SecurityException var3) {
         throw new RuntimeException(var3);
      } catch (IllegalArgumentException var4) {
         throw new RuntimeException(var4);
      } catch (InstantiationException var5) {
         throw new RuntimeException(var5);
      } catch (IllegalAccessException var6) {
         throw new RuntimeException(var6);
      } catch (InvocationTargetException var7) {
         throw new RuntimeException(var7);
      }
   }

   public static IntegrationEntity recreateEntity(EntityType var0, IntegrationEntityId var1, String var2) throws ClassNotFoundException {
      try {
         Class var3 = getEntityClass(var0);
         Constructor var4 = var3.getConstructor(var1.getClass(), String.class);
         return instantiate(var4, var1, var2);
      } catch (SecurityException var5) {
         throw new RuntimeException(var5);
      } catch (NoSuchMethodException var6) {
         throw new RuntimeException(var6);
      }
   }

   public static boolean isInstanceOf(EntityType var0, Class<?> var1) {
      Optional var2 = var0.getImplementingClassName();
      if (!var2.isPresent()) {
         return false;
      }

      try {
         Class var3 = getEntityClass(var0);
         return var1.isAssignableFrom(var3);
      } catch (ClassNotFoundException var4) {
         return false;
      }
   }
}
