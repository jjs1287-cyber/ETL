package org.etlforge.integration.data.entity;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.etlforge.lang.KongaClassUtils;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.name.NameValidator;
import org.etlforge.util.name.NameValidatorStore;

public final class EntityUtils {
   public static boolean isNameUnique(IntegrationEntity var0, String var1) {
      synchronized (var0.getDataLock()) {
         IntegrationEntity var3 = var0.getParent();
         return var3 == null ? true : isChildNameUnique(var3, var1, var0);
      }
   }

   public static boolean isChildNameUnique(IntegrationEntity var0, String var1) {
      return isChildNameUnique(var0, var1, null);
   }

   private static boolean isChildNameUnique(IntegrationEntity var0, String var1, IntegrationEntity var2) {
      synchronized (var0.getDataLock()) {
         for (IntegrationEntity var5 : var0.getAllChildren()) {
            if (var5 != var2 && var5.getName().equalsIgnoreCase(var1)) {
               return false;
            }
         }

         return true;
      }
   }

   public static String getAvailableName(IntegrationEntity var0, String var1) throws IllegalNameException {
      NameValidator var2 = NameValidatorStore.getInstance().get(var0);
      if (var2 != null) {
         var2.validateName(var1);
      }

      String var3 = var1;

      for (int var4 = 2; !isNameUnique(var0, var3); var4++) {
         var3 = var1 + " (" + var4 + ")";
      }

      return var3;
   }

   public static String getAvailableName(IntegrationEntity var0, IntegrationEntity var1, String var2) throws IllegalNameException {
      NameValidator var3 = NameValidatorStore.getInstance().get(var0);
      if (var3 != null) {
         var3.validateName(var2);
      }

      String var4 = var2;

      for (int var5 = 2; isNameTaken(var1, var4); var5++) {
         var4 = var2 + " (" + var5 + ")";
      }

      return var4;
   }

   private static boolean isNameTaken(IntegrationEntity var0, String var1) {
      for (IntegrationEntity var3 : var0.getAllChildren()) {
         if (var1.equalsIgnoreCase(var3.getName())) {
            return true;
         }
      }

      return false;
   }

   public static String getAvailableChildName(Folder var0, String var1) {
      String var2 = var1;

      for (int var3 = 2; isNameTaken(var0, var2); var3++) {
         var2 = var1 + " (" + var3 + ")";
      }

      return var2;
   }

   public static EntityType getItemType(IntegrationEntity var0) {
      return var0 instanceof Folder ? ((Folder)var0).getItemType() : var0.getEntityType();
   }

   public static <T extends IntegrationEntityId> Optional<T> getId(IntegrationEntity var0, Class<T> var1) {
      return var0 == null ? Optional.absent() : Optional.of((T)var1.cast(var0.getID()));
   }

   public static <T extends IntegrationEntityId> void storeId(T var0, Class<T> var1, IntegrationEntity var2, String var3) {
      String var4 = var0 == null ? null : var0.toString();
      String var5;
      synchronized (var2.getDataLock()) {
         var5 = var2.getProperty(var3);
         var2.setProperty(var3, var4);
      }

      if (!Objects.equal(var4, var5)) {
         IntegrationEntityId var6 = getOldId(var1, var5);
         var2.firePropertyChange(var3, var6, var0);
      }
   }

   private static <T extends IntegrationEntityId> T getOldId(Class<T> var0, String var1) {
      if (var1 == null) {
         return null;
      }

      try {
         Constructor var2 = var0.getConstructor(String.class);
         return (T)var2.newInstance(var1);
      } catch (InvocationTargetException var3) {
         var3.getCause().printStackTrace();
         return null;
      } catch (Exception var4) {
         throw new RuntimeException(var4);
      }
   }

   public static <T extends IntegrationEntityId> void storeId(Optional<T> var0, Class<T> var1, IntegrationEntity var2, String var3) {
      storeId((T)var0.orNull(), var1, var2, var3);
   }

   public static <T extends IntegrationEntityId> Optional<T> readId(Class<T> var0, IntegrationEntity var1, String var2) {
      synchronized (var1.getDataLock()) {
         String var4 = var1.getProperty(var2);
         if (!Strings.isNullOrEmpty(var4)) {
            Optional var10000;
            try {
               Constructor var5 = KongaClassUtils.getSingleArgumentConstructor(var0, String.class);
               var10000 = Optional.of(var5.newInstance(var4));
            } catch (Exception var7) {
               throw Throwables.propagate(var7);
            }

            return var10000;
         }
      }

      return Optional.absent();
   }

   public static <T extends IntegrationEntity> Optional<T> lookupInstance(
      IntegrationEntityLookup var0, Optional<? extends IntegrationEntityId> var1, Class<T> var2
   ) {
      return var1.isPresent() ? Optional.fromNullable(var0.getEntity((IntegrationEntityId)var1.get(), var2)) : Optional.absent();
   }

   private EntityUtils() {
   }
}
