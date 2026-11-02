package org.etlforge.integration.client.ui.project.permissions;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.DoNotShowAgainQuestion;
import org.etlforge.integration.client.ui.entity.IntegrationEntityList;
import org.etlforge.integration.data.access.Permission;
import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.Role;
import org.etlforge.integration.user.User;
import org.etlforge.ui.layout.BorderLayoutBuilder;

final class PermissionsCache {
   private final Map<Role, Permissions> permissionCache = Maps.newHashMap();
   private final Set<Role> dirtyUsersAndGroups = new HashSet<>();
   private final PermissionsViewImpl view;
   private IntegrationEntity accessedEntity;
   private static final EnumSet<Permission> DEFAULT_PERMISSION = EnumSet.noneOf(Permission.class);
   private final Comparator<IntegrationEntity> SORTER = new EntityTypeAndNameSorter();

   public PermissionsCache(PermissionsViewImpl var1) {
      this.view = var1;
   }

   public void setAccessedEntity(IntegrationEntity var1) {
      if (var1 != this.accessedEntity) {
         this.accessedEntity = var1;
         this.clear();
      }
   }

   public boolean isDirty() {
      return !this.dirtyUsersAndGroups.isEmpty();
   }

   public Permissions getPermissions(Role var1) {
      Permissions var2 = this.permissionCache.get(var1);
      if (var2 == null) {
         if (var1 instanceof User) {
            var2 = this.view.getPermissions().getPermissions((User)var1, this.accessedEntity);
         } else {
            var2 = this.view.getPermissions().getPermissions((Group)var1, this.accessedEntity);
         }

         if (var2 == null) {
            var2 = new Permissions(DEFAULT_PERMISSION, null);
            this.permissionCache.put(var1, var2);
         }

         this.permissionCache.put(var1, var2);
      }

      return var2;
   }

   public void cachePermissions(Role var1, Permissions var2) {
      this.permissionCache.put(var1, var2);
      this.dirtyUsersAndGroups.add(var1);
   }

   public boolean applyAll(boolean var1) {
      boolean var2 = false;

      for (Role var4 : this.dirtyUsersAndGroups) {
         var2 = this.updatePermissionManager(var4, this.accessedEntity, this.permissionCache.get(var4), var1);
      }

      this.clear();
      return var2;
   }

   public void clear() {
      this.permissionCache.clear();
      this.dirtyUsersAndGroups.clear();
   }

   private boolean updatePermissionManager(Role var1, IntegrationEntity var2, Permissions var3, boolean var4) {
      return var1 instanceof User ? this.updateUserPermissions((User)var1, var2, var3, var4) : this.updateGroupPermissions((Group)var1, var2, var3, var4);
   }

   private boolean updateUserPermissions(User var1, IntegrationEntity var2, Permissions var3, boolean var4) {
      if (var3.isAllowed(Permission.READ)) {
         List var5 = this.view.getPermissions().getVetosReadPermission(var1, var2);
         Collections.sort(var5, this.SORTER);
         if (!var5.isEmpty() && !this.overrideReadVeto(var1, var5)) {
            return false;
         }
      }

      this.view.getPermissions().setPermissions(var1, var2, var3, var4);
      return true;
   }

   private boolean updateGroupPermissions(Group var1, IntegrationEntity var2, Permissions var3, boolean var4) {
      if (var3.isAllowed(Permission.READ)) {
         List var5 = this.view.getPermissions().getVetosReadPermission(var1, var2);
         Collections.sort(var5, this.SORTER);
         if (!var5.isEmpty() && !this.overrideReadVeto(var1, var5)) {
            return false;
         }
      }

      this.view.getPermissions().setPermissions(var1, var2, var3, var4);
      return true;
   }

   private boolean overrideReadVeto(Role var1, List<IntegrationEntity> var2) {
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 3);
      var3.northToSouth(getTopMessage(var1), new IntegrationEntityList(var2), getBottomMessage(var1));
      DoNotShowAgainQuestion var4 = new DoNotShowAgainQuestion(
         var3,
         Strings.get("Permissions.Cache.Override.Title"),
         AskBeforeOverridingReadVetoPreference.INSTANCE,
         Alert.Option.YES_NO,
         Alert.Option.YES,
         Alert.Type.WARNING
      );
      return var4.ask(this.view.getWindow().getFrame());
   }

   private static String getTopMessage(Role var0) {
      return Strings.format("Permissions.Cache.Override.Top", var0.getName());
   }

   private static String getBottomMessage(Role var0) {
      return Strings.format("Permissions.Cache.Override.Bottom", var0.getName());
   }
}
