package org.etlforge.integration.client.ui.admin.component.user2;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.user.UserManager;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.GroupMemberListener;
import org.etlforge.integration.user.ProtectedMemberException;
import org.etlforge.integration.user.Role;
import org.etlforge.integration.user.User;
import org.etlforge.system.Disposable;
import org.etlforge.util.name.NameSorter;

public final class UserConsoleModel implements Disposable {
   private final UserManager userManager;
   private ServerInfo currentServer;
   private boolean downloading;
   private boolean uploading;
   private Set<User> users;
   private Set<Group> groups;
   private boolean active;
   private boolean readOnly;
   private boolean dirty;
   private final List<UserConsoleModelListener> listeners;
   private final PropertyChangeListener userChangeDispatcher;
   private final PropertyChangeListener groupChangeDispatcher;
   private final GroupMemberListener groupMemberListenerDispatcher;
   private volatile ServerGuid idOfServerOfOrigin;

   public UserConsoleModel(UserManager var1) {
      this.userManager = var1;
      this.users = Sets.newHashSet();
      this.groups = Sets.newHashSet();
      this.listeners = new CopyOnWriteArrayList<>();
      this.userChangeDispatcher = new UserConsoleModel.UserPropertyChangeDispatcher();
      this.groupChangeDispatcher = new UserConsoleModel.GroupPropertyChangeDispatcher();
      this.groupMemberListenerDispatcher = new UserConsoleModel.GroupMemberListenerDispatcher();
   }

   @Override
   public void dispose() {
      this.stopListeningToUsers();
      this.stopListeningToGroups();
   }

   public UserManager getUserManager() {
      return this.userManager;
   }

   public ServerInfo getCurrentServer() {
      return this.currentServer;
   }

   public void setCurrentServer(ServerInfo var1) {
      if (!Objects.equal(getServerId(var1), getServerId(this.currentServer))) {
         this.currentServer = var1;

         for (UserConsoleModelListener var3 : this.listeners) {
            var3.newServerConnectedTo(this);
         }

         this.clearData();
      }
   }

   public void setDownloadingInfo(boolean var1) {
      if (var1 != this.downloading) {
         if (var1) {
            this.clearData();
            this.setActive(false);
         }

         this.downloading = var1;

         for (UserConsoleModelListener var3 : this.listeners) {
            var3.downloadStatusChanged(this, var1);
         }
      }
   }

   public void setUploadingInfo(boolean var1) {
      if (var1 != this.uploading) {
         this.uploading = var1;

         for (UserConsoleModelListener var3 : this.listeners) {
            var3.uploadStatusChanged(this, var1);
         }
      }
   }

   public void setReadOnly(boolean var1) {
      if (var1 != this.readOnly) {
         this.readOnly = var1;

         for (UserConsoleModelListener var3 : this.listeners) {
            var3.readOnlyStateChanged(this, var1);
         }
      }
   }

   public boolean isReadOnly() {
      return this.readOnly;
   }

   public boolean isEmpty() {
      return this.users.isEmpty() && this.groups.isEmpty();
   }

   public boolean isActive() {
      return this.active;
   }

   private void setActive(boolean var1) {
      if (this.active != var1) {
         this.active = var1;

         for (UserConsoleModelListener var3 : this.listeners) {
            var3.activeStatusChanged(this, var1);
         }
      }
   }

   public void setNewUserData(List<User> var1, List<Group> var2, ServerGuid var3) {
      this.stopListeningToGroups();
      this.users = Sets.newHashSet(var1);
      this.groups = Sets.newHashSet(var2);
      this.startListeningToUsers();
      this.startListeningToGroups();

      for (UserConsoleModelListener var5 : this.listeners) {
         var5.newUserDataDownloaded(this);
      }

      this.idOfServerOfOrigin = var3;
      this.setDirty(false);
      this.setActive(true);
   }

   public ServerGuid getIdOfServerOfOrigin() {
      return this.idOfServerOfOrigin;
   }

   public List<User> getUsers() {
      ArrayList var1 = Lists.newArrayList(this.users);
      Collections.sort(var1, NameSorter.ignoreCase());
      return var1;
   }

   public void addUser(User var1) {
      if (this.users.add(var1)) {
         for (UserConsoleModelListener var3 : this.listeners) {
            var3.userAdded(this, var1);
         }

         this.listenToUser(var1);
         this.setDirty(true);
      }
   }

   public void removeUser(User var1) throws VetoedModelChangeException {
      if (!var1.isDeletable()) {
         throw new VetoedModelChangeException("The user " + var1.getName() + " cannot be deleted.");
      }

      if (this.users.remove(var1)) {
         this.setDirty(true);
         this.stopListeningToUser(var1);

         for (UserConsoleModelListener var3 : this.listeners) {
            var3.userRemoved(this, var1);
         }

         try {
            for (Group var6 : this.groups) {
               var6.removeMember(var1);
            }
         } catch (ProtectedMemberException var4) {
            throw new VetoedModelChangeException(var4.getMessage());
         }
      }
   }

   public List<Group> getGroups() {
      ArrayList var1 = Lists.newArrayList(this.groups);
      Collections.sort(var1, NameSorter.ignoreCase());
      return var1;
   }

   public void addGroup(Group var1) {
      if (this.groups.add(var1)) {
         for (UserConsoleModelListener var3 : this.listeners) {
            var3.groupAdded(this, var1);
         }

         this.listenToGroup(var1);
         this.setDirty(true);
      }
   }

   public void removeGroup(Group var1) throws VetoedModelChangeException {
      if (!var1.isDeletable()) {
         throw new VetoedModelChangeException("The group " + var1.getName() + " cannot be deleted.");
      }

      if (this.groups.remove(var1)) {
         this.stopListeningTo(var1);

         for (UserConsoleModelListener var3 : this.listeners) {
            var3.groupRemoved(this, var1);
         }

         this.setDirty(true);
      }
   }

   public void clearData() {
      this.stopListeningToUsers();
      this.stopListeningToGroups();
      this.users = Sets.newHashSet();
      this.groups = Sets.newHashSet();

      for (UserConsoleModelListener var2 : this.listeners) {
         var2.allDataRemoved(this);
      }

      this.setDirty(false);
      this.setActive(false);
   }

   public boolean isDirty() {
      return this.dirty;
   }

   public void setDirty(boolean var1) {
      if (this.dirty != var1) {
         this.dirty = var1;

         for (UserConsoleModelListener var3 : this.listeners) {
            var3.dirtyStatusChanged(this, var1);
         }
      }
   }

   public void addModelListener(UserConsoleModelListener var1) {
      this.listeners.add(var1);
   }

   public void removeModelListener(UserConsoleModelListener var1) {
      this.listeners.remove(var1);
   }

   public boolean isUserNameAvailable(User var1, String var2) {
      return isNameAvailable(this.users, var1, var2);
   }

   public boolean isGroupNameAvailable(Group var1, String var2) {
      return isNameAvailable(this.groups, var1, var2);
   }

   public void validate() throws InvalidUserDataException {
      validateNames(this.users, "user");
      validateNames(this.groups, "group");
   }

   private static boolean isNameAvailable(Iterable<? extends Role> var0, Role var1, String var2) {
      for (Role var4 : var0) {
         if (var4 != var1 && var4.getName().equalsIgnoreCase(var2)) {
            return false;
         }
      }

      return true;
   }

   private static void validateNames(Iterable<? extends Role> var0, String var1) throws InvalidUserDataException {
      HashSet var2 = Sets.newHashSet();

      for (Role var4 : var0) {
         String var5 = var4.getName();
         if (StringUtils.isBlank(var5)) {
            throw new InvalidUserDataException("Empty " + var1 + " names are not allowed");
         }

         if (var2.contains(var5)) {
            throw new InvalidUserDataException("Duplicated " + var1 + " name: " + var5);
         }

         var2.add(var5);
      }
   }

   private void startListeningToUsers() {
      for (User var2 : this.users) {
         this.listenToUser(var2);
      }
   }

   private void listenToUser(User var1) {
      var1.addPropertyChangeListener(this.userChangeDispatcher);
   }

   private void stopListeningToUsers() {
      for (User var2 : this.users) {
         this.stopListeningToUser(var2);
      }
   }

   private void stopListeningToUser(User var1) {
      var1.removePropertyChangeListener(this.userChangeDispatcher);
   }

   private void startListeningToGroups() {
      for (Group var2 : this.groups) {
         this.listenToGroup(var2);
      }
   }

   private void listenToGroup(Group var1) {
      var1.addPropertyChangeListener(this.groupChangeDispatcher);
      var1.addGroupMemberListener(this.groupMemberListenerDispatcher);
   }

   private void stopListeningToGroups() {
      for (Group var2 : this.groups) {
         this.stopListeningTo(var2);
      }
   }

   private void stopListeningTo(Group var1) {
      var1.removePropertyChangeListener(this.groupChangeDispatcher);
      var1.removeGroupMemberListener(this.groupMemberListenerDispatcher);
   }

   private static ServerGuid getServerId(ServerInfo var0) {
      return var0 != null ? var0.getGuid() : null;
   }

   private class GroupMemberListenerDispatcher implements GroupMemberListener {
      private GroupMemberListenerDispatcher() {
      }

      @Override
      public void memberAdded(Group var1, User var2) {
         for (GroupMemberListener var4 : UserConsoleModel.this.listeners) {
            var4.memberAdded(var1, var2);
         }

         UserConsoleModel.this.setDirty(true);
      }

      @Override
      public void memberRemoved(Group var1, User var2) {
         for (GroupMemberListener var4 : UserConsoleModel.this.listeners) {
            var4.memberRemoved(var1, var2);
         }

         UserConsoleModel.this.setDirty(true);
      }
   }

   private class GroupPropertyChangeDispatcher implements PropertyChangeListener {
      private GroupPropertyChangeDispatcher() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         for (UserConsoleModelListener var3 : UserConsoleModel.this.listeners) {
            var3.groupPropertyChanged(UserConsoleModel.this, var1);
         }

         UserConsoleModel.this.setDirty(true);
      }
   }

   private class UserPropertyChangeDispatcher implements PropertyChangeListener {
      private UserPropertyChangeDispatcher() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         for (UserConsoleModelListener var3 : UserConsoleModel.this.listeners) {
            var3.userPropertyChanged(UserConsoleModel.this, var1);
         }

         UserConsoleModel.this.setDirty(true);
      }
   }
}
