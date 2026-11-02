package org.etlforge.integration.client.project.permission;

import java.util.List;

public interface PermissionsStorage {
   void save(List<UserPermission> var1, List<GroupPermission> var2) throws PermissionsStorageException;

   List<UserPermission> readUserPermissions() throws PermissionsStorageException;

   List<GroupPermission> readGroupPermissions() throws PermissionsStorageException;
}
