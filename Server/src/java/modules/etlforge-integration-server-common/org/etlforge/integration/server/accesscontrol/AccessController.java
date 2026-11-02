package org.etlforge.integration.server.accesscontrol;

import com.google.common.collect.Sets;
import java.util.HashSet;
import org.etlforge.integration.data.access.Permission;
import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.server.data.EntityDescriptor;
import org.etlforge.integration.server.idmappings.GuidToIdDbMapper;
import org.etlforge.integration.server.idmappings.GuidToIdMappingException;

public class AccessController {
   private CachedUserPermissionsLookup userPerms;
   private CachedGroupPermissionsLookup groupPerms;
   private CachedGroupLookup groupLookup;
   private GuidToIdDbMapper guidToIdMapper;

   public void setUserPermissionsLookup(UserPermissionsLookup var1) {
      this.userPerms = new CachedUserPermissionsLookup(var1);
   }

   public void setGroupPermissionsLookup(GroupPermissionsLookup var1) {
      this.groupPerms = new CachedGroupPermissionsLookup(var1);
   }

   public void setGroupLookup(GroupLookup var1) {
      this.groupLookup = new CachedGroupLookup(var1);
   }

   public void setGuidToIdMapper(GuidToIdDbMapper var1) {
      this.guidToIdMapper = var1;
   }

   public void clearCaches() {
      this.userPerms.reset();
      this.groupPerms.reset();
      this.groupLookup.reset();
   }

   public boolean hasPermission(User var1, IntegrationEntityId var2, Permission var3) throws AccessLookupException {
      try {
         if (this.isAdmin(var1)) {
            return true;
         }

         Long var4 = this.guidToIdMapper.getId(var2);
         if (var4 == null) {
            return false;
         }

         EntityDescriptor var5 = new EntityDescriptor(var2.getEntityType(), var4);
         return this.hasPermissionImpl(var1, var5, var3, false);
      } catch (GuidToIdMappingException var6) {
         throw new AccessLookupException(var6);
      }
   }

   public boolean hasPermission(User var1, EntityDescriptor var2, Permission var3) {
      return this.hasPermissionImpl(var1, var2, var3, true);
   }

   private boolean hasPermissionImpl(User var1, EntityDescriptor var2, Permission var3, boolean var4) {
      if (var4 && this.isAdmin(var1)) {
         return true;
      }

      Permissions var5 = this.userPerms.getPermissions(var1, var2);
      if (this.checkOwner(var5, var3)) {
         return true;
      }

      if (var5.isDenied(var3)) {
         return false;
      }

      HashSet var6 = Sets.newHashSet();

      for (Group var8 : this.groupLookup.getGroups(var1)) {
         Permissions var9 = this.groupPerms.getPermissions(var8, var2);
         if (var9.isDenied(var3)) {
            return false;
         }

         var6.add(var9);
      }

      if (var5.isAllowed(var3)) {
         return true;
      }

      for (Object __etlItem91 : var6) {
         Permissions var11 = (Permissions) __etlItem91;
         if (var11.isAllowed(var3)) {
            return true;
         }
      }

      return false;
   }

   private boolean checkOwner(Permissions var1, Permission var2) {
      if (var1.isOwner()) {
         return var2 != Permission.READ && var2 != Permission.CHANGE_PERMISSIONS ? !var1.isDenied(var2) : true;
      } else {
         return false;
      }
   }

   private boolean isAdmin(User var1) {
      if (var1.isAdmin()) {
         return true;
      }

      for (Group var3 : this.groupLookup.getGroups(var1)) {
         if (var3.isAdmin()) {
            return true;
         }
      }

      return false;
   }
}
