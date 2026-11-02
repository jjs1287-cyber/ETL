package org.etlforge.integration.user;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.etlforge.util.Property;
import org.etlforge.util.PropertyService;
import org.etlforge.util.crypto.DigestUtils;

public class User implements Role {
   public static final String USER_NAME = "userName";
   public static final String DESCRIPTION = "description";
   public static final String PASSWORD = "password";
   public static final String PASSWORD_HASH = "passwordHash";
   public static final String FIRST_NAME = "firstName";
   public static final String LAST_NAME = "lastName";
   public static final String EMAIL = "email";
   private static final UserId ADMIN_GUID = new UserId("13ec289d-1c2d-486e-98fb-16d5edbc5fbb");
   private static final UserId ANONYMOUS_GUID = new UserId("76d1dc9a-a7e7-4576-954d-77151f699f86");
   private final UserId id;
   private final PropertyService propertyService;
   private final Property<String> userName;
   private final Property<String> firstName;
   private final Property<String> lastName;
   private final Property<String> password;
   private final Property<String> passwordHash;
   private final Property<String> email;
   private final Property<String> description;
   private final Set<Group> groups;

   public User(String var1) {
      this(new UserId(), var1);
   }

   public User(User var1) {
      this(var1.id, var1.getName());
      this.setFirstName(var1.getFirstName());
      this.setLastName(var1.getLastName());
      this.setPasswordHash(var1.getPasswordHash());
      this.setEmail(var1.getEmail());
      this.setDescription(var1.getDescription());
   }

   public User(UserId var1, String var2) {
      this.id = Preconditions.checkNotNull(var1, "id");
      this.propertyService = new PropertyService(this);
      this.userName = this.propertyService.newProperty("userName", Preconditions.checkNotNull(var2, "name"));
      this.userName.setAllowsNull(false);
      this.firstName = this.propertyService.newProperty("firstName");
      this.lastName = this.propertyService.newProperty("lastName");
      this.password = this.propertyService.newProperty("password");
      this.passwordHash = this.propertyService.newProperty("passwordHash");
      this.email = this.propertyService.newProperty("email");
      this.description = this.propertyService.newProperty("description");
      this.groups = new LinkedHashSet<>();
   }

   public UserId getId() {
      return this.id;
   }

   public boolean isDeletable() {
      return !this.isBuiltInUser();
   }

   public boolean isRenamable() {
      return !this.isBuiltInUser();
   }

   public String getUserName() {
      return this.userName.get();
   }

   @Override
   public String getName() {
      return this.userName.get();
   }

   public void setUserName(String var1) {
      if (this.isBuiltInUser()) {
         throw new UnsupportedOperationException("The user " + this.getName() + " cannot be renamed");
      }

      this.userName.set(var1);
   }

   public String getFirstName() {
      return this.firstName.get();
   }

   public String getLastName() {
      return this.lastName.get();
   }

   public String getEmail() {
      return this.email.get();
   }

   public String getPassword() {
      return this.password.get();
   }

   public void setPassword(String var1) {
      this.password.set(var1);
      String var2 = var1 != null ? DigestUtils.getMd5DigestAsHexString(var1) : null;
      this.passwordHash.set(var2);
   }

   public String getPasswordHash() {
      return this.passwordHash.get();
   }

   public void setPasswordHash(String var1) {
      if (!Objects.equal(var1, this.getPasswordHash())) {
         this.passwordHash.set(var1);
         this.password.set(null);
      }
   }

   public void setFirstName(String var1) {
      this.firstName.set(var1);
   }

   public void setLastName(String var1) {
      this.lastName.set(var1);
   }

   public void setEmail(String var1) {
      this.email.set(var1);
   }

   public String getDescription() {
      return this.description.get();
   }

   public void setDescription(String var1) {
      this.description.set(var1);
   }

   public List<Group> getGroups() {
      return Lists.newArrayList(this.groups);
   }

   public boolean isAdmin() {
      return this.id.equals(ADMIN_GUID);
   }

   public void restoreProperties(User var1, Set<Group> var2) {
      if (!this.isBuiltInUser()) {
         this.setUserName(var1.getName());
      }

      this.setFirstName(var1.getFirstName());
      this.setLastName(var1.getLastName());
      this.setEmail(var1.getEmail());
      this.setDescription(var1.getDescription());
      this.setPasswordHash(var1.getPasswordHash());
      this.restoreGroupMembership(var2);
   }

   private void restoreGroupMembership(Set<Group> var1) {
      HashSet var2 = Sets.newHashSet(this.getGroups());
      Iterator var3 = var2.iterator();

      while (var3.hasNext()) {
         Group var4 = (Group)var3.next();
         if (!var1.contains(var4)) {
            try {
               var4.removeMember(this);
            } catch (ProtectedMemberException var6) {
               var6.printStackTrace();
            }

            var3.remove();
         }
      }

      for (Group var8 : var1) {
         if (!var2.contains(var8)) {
            var8.addMember(this);
         }
      }
   }

   void addToGroup(Group var1) {
      Preconditions.checkNotNull(var1, "group");
      this.groups.add(var1);
   }

   void removeFromGroup(Group var1) {
      this.groups.remove(var1);
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      this.propertyService.addPropertyChangeListener(var1);
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      this.propertyService.removePropertyChangeListener(var1);
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof User && this.id.equals(((User)var1).id);
   }

   @Override
   public int hashCode() {
      return this.id.hashCode();
   }

   @Override
   public String toString() {
      return this.getName();
   }

   private boolean isBuiltInUser() {
      return this.isAdmin() || this.id.equals(ANONYMOUS_GUID);
   }
}
