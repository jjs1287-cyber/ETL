package org.etlforge.integration.user;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.util.Property;
import org.etlforge.util.PropertyService;

public final class Group implements Role {
   public static final String NAME = "name";
   public static final String DESCRIPTION = "description";
   private static final GroupId ADMIN_GUID = new GroupId("14ec289d-1c2d-486e-98fb-16d5edbc5fbb");
   private final GroupId id;
   private final Property<String> name;
   private final Property<String> description;
   private final PropertyService propertyService;
   private final Set<User> members;
   private final List<GroupMemberListener> memberListeners;

   public Group(String var1) {
      this(new GroupId(), var1);
   }

   public Group(Group var1) {
      this(var1.id, var1.getName());
      this.setDescription(var1.getDescription());
   }

   public Group(GroupId var1, String var2) {
      Preconditions.checkNotNull(var2, "name");
      this.id = var1;
      this.propertyService = new PropertyService(this);
      this.name = this.propertyService.newProperty("name", var2);
      this.name.setAllowsNull(false);
      this.description = this.propertyService.newProperty("description");
      this.members = new LinkedHashSet<>();
      this.memberListeners = new CopyOnWriteArrayList<>();
   }

   public boolean isDeletable() {
      return !this.isAdmin();
   }

   public boolean isRenamable() {
      return !this.isAdmin();
   }

   public List<User> getMembers() {
      return Lists.newArrayList(this.members);
   }

   public boolean isMember(User var1) {
      return this.members.contains(var1);
   }

   public void addMember(User var1) {
      if (!this.isMember(var1)) {
         var1.addToGroup(this);
         this.members.add(var1);

         for (GroupMemberListener var3 : this.memberListeners) {
            var3.memberAdded(this, var1);
         }
      }
   }

   public void removeMember(User var1) throws ProtectedMemberException {
      if (this.isAdmin() && var1.isAdmin()) {
         throw new ProtectedMemberException(this, var1);
      }

      if (this.members.remove(var1)) {
         var1.removeFromGroup(this);

         for (GroupMemberListener var3 : this.memberListeners) {
            var3.memberRemoved(this, var1);
         }
      }
   }

   @Override
   public final String getName() {
      return this.name.get();
   }

   public final void setName(String var1) {
      if (this.isAdmin()) {
         throw new UnsupportedOperationException("The group " + this.getName() + " cannot be renamed");
      }

      this.name.set(var1);
   }

   public String getDescription() {
      return this.description.get();
   }

   public void setDescription(String var1) {
      this.description.set(var1);
   }

   public final GroupId getId() {
      return this.id;
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      this.propertyService.addPropertyChangeListener(var1);
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      this.propertyService.removePropertyChangeListener(var1);
   }

   public void addGroupMemberListener(GroupMemberListener var1) {
      this.memberListeners.add(var1);
   }

   public void removeGroupMemberListener(GroupMemberListener var1) {
      this.memberListeners.remove(var1);
   }

   public void restoreProperties(Group var1, Set<User> var2) {
      if (!this.isAdmin()) {
         this.setName(var1.getName());
      }

      this.setDescription(var1.getDescription());
      this.restoreMembers(var2);
   }

   private void restoreMembers(Set<User> var1) {
      HashSet var2 = Sets.newHashSet(this.getMembers());
      Iterator var3 = var2.iterator();

      while (var3.hasNext()) {
         User var4 = (User)var3.next();
         if (!var1.contains(var4)) {
            try {
               this.removeMember(var4);
            } catch (ProtectedMemberException var6) {
               var6.printStackTrace();
            }

            var3.remove();
         }
      }

      for (User var8 : var1) {
         if (!var2.contains(var8)) {
            this.addMember(var8);
         }
      }
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof Group && this.id.equals(((Group)var1).id);
   }

   @Override
   public int hashCode() {
      return this.id.hashCode();
   }

   @Override
   public String toString() {
      return this.getName();
   }

   private boolean isAdmin() {
      return this.id.equals(ADMIN_GUID);
   }
}
