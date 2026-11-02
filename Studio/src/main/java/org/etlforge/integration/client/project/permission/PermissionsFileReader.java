package org.etlforge.integration.client.project.permission;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.user.GroupId;
import org.etlforge.integration.user.RoleId;
import org.etlforge.integration.user.UserId;
import org.etlforge.io.Folder;
import org.etlforge.io.KongaIoUtils;

final class PermissionsFileReader {
   private final Folder root;
   private final ImmutableSet<EntityType> entityTypes;

   public PermissionsFileReader(Folder var1, Iterable<EntityType> var2) {
      this.root = var1;
      this.entityTypes = ImmutableSet.copyOf(var2);
   }

   public List<UserPermission> readUserPermissions() throws PermissionsStorageException {
      ArrayList var1 = Lists.newArrayList();

      for (EntityType var3 : this.entityTypes) {
         File var4 = this.root.getFile(var3.toString() + "_User.dat");
         if (var4.exists() && var4.canRead()) {
            List var5 = this.readImpl(var4, new PermissionsFileReader.UserFactory(var3));
            var1.addAll(var5);
         }
      }

      return var1;
   }

   private <T extends RoleId, V extends RolePermission<T>> List<V> readImpl(File var1, PermissionsFileReader.EntryFactory<T, V> var2) throws PermissionsStorageException {
      BufferedReader var3 = null;

      try {
         ArrayList var4 = Lists.newArrayList();
         var3 = new BufferedReader(new InputStreamReader(new FileInputStream(var1), Charsets.UTF_8));
         String var5 = null;

         while ((var5 = var3.readLine()) != null) {
            if (var5.length() > 0) {
               RolePermission var6 = var2.parseLine(var5);
               var4.add(var6);
            }
         }

         return var4;
      } catch (IOException var10) {
         throw new PermissionsStorageException("An error occurred when reading the project's permission information.", var10);
      } finally {
         KongaIoUtils.close(var3);
      }
   }

   public List<GroupPermission> readGroupPermissions() throws PermissionsStorageException {
      ArrayList var1 = Lists.newArrayList();

      for (EntityType var3 : this.entityTypes) {
         File var4 = this.root.getFile(var3.toString() + "_Group.dat");
         if (var4.exists() && var4.canRead()) {
            List var5 = this.readImpl(var4, new PermissionsFileReader.GroupFactory(var3));
            var1.addAll(var5);
         }
      }

      return var1;
   }

   private abstract static class EntryFactory<T extends RoleId, V extends RolePermission<T>> {
      private final EntityType type;

      public EntryFactory(EntityType var1) {
         this.type = var1;
      }

      public final V parseLine(String var1) throws PermissionsStorageException {
         String[] var2 = var1.split(",");

         try {
            IntegrationEntityId var3 = this.type.getIntegrationId(var2[0]);
            RoleId var4 = this.parseRoleId(var2[1]);
            int var5 = Integer.parseInt(var2[2]);
            int var6 = Integer.parseInt(var2[3]);
            return this.createRolePermission(var3, (T)var4, var5, var6);
         } catch (NumberFormatException var7) {
            throw new PermissionsStorageException("Invalid format.", var7);
         } catch (IllegalArgumentException var8) {
            throw new PermissionsStorageException("Invalid format.", var8);
         } catch (ArrayIndexOutOfBoundsException var9) {
            throw new PermissionsStorageException("Invalid format.", var9);
         }
      }

      protected abstract T parseRoleId(String var1);

      protected abstract V createRolePermission(IntegrationEntityId var1, T var2, int var3, int var4);
   }

   private static class GroupFactory extends PermissionsFileReader.EntryFactory<GroupId, GroupPermission> {
      public GroupFactory(EntityType var1) {
         super(var1);
      }

      protected GroupId parseRoleId(String var1) {
         return new GroupId(var1);
      }

      protected GroupPermission createRolePermission(IntegrationEntityId var1, GroupId var2, int var3, int var4) {
         return new GroupPermission(var2, var1, var3, var4);
      }
   }

   private static class UserFactory extends PermissionsFileReader.EntryFactory<UserId, UserPermission> {
      public UserFactory(EntityType var1) {
         super(var1);
      }

      protected UserId parseRoleId(String var1) {
         return new UserId(var1);
      }

      protected UserPermission createRolePermission(IntegrationEntityId var1, UserId var2, int var3, int var4) {
         return new UserPermission(var2, var1, var3, var4);
      }
   }
}
