package org.etlforge.integration.data.entity;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import java.util.Arrays;
import java.util.EnumSet;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.LocatableFilter;

public class LocatableFolders {
   private final RootFolder parent;
   private final LocatableFolders.FolderDescriptor files;
   private final LocatableFolders.FolderDescriptor databases;
   private final LocatableFolders.FolderDescriptor ldap;

   public LocatableFolders(RootFolder var1) {
      this.parent = var1;
      this.files = new LocatableFolders.FolderDescriptor(var1, "Files", DataLocationType.getFileBasedTypes());
      this.databases = new LocatableFolders.FolderDescriptor(var1, "Databases", DataLocationType.Database);
      this.ldap = new LocatableFolders.FolderDescriptor(var1, "LDAP", DataLocationType.LDAP);
   }

   protected boolean isSupported(DataLocationType var1) {
      return true;
   }

   public final void prepare(boolean var1) {
      this.files.prepare(var1);
      this.databases.prepare(var1 && this.isSupported(DataLocationType.Database));
      this.ldap.prepare(var1 && this.isSupported(DataLocationType.LDAP));
   }

   public final void restrictTypes(Locatable var1, Folder var2) {
      if (var2 != this.parent) {
         for (LocatableFolders.FolderDescriptor var4 : Arrays.asList(this.files, this.databases, this.ldap)) {
            if (var4.isAncestorOf(var2)) {
               var4.restrictTypes(var1);
            }
         }
      }
   }

   public final boolean isTypeAllowed(DataLocationType var1, Folder var2) {
      LocatableFolders.FolderDescriptor var3 = this.getDescriptorForType(var1);
      return var3 == null || var3.isTypeAllowed(var2);
   }

   private LocatableFolders.FolderDescriptor getDescriptorForType(DataLocationType var1) {
      if (var1.isFileBased()) {
         return this.files;
      } else if (var1 == DataLocationType.Database) {
         return this.databases;
      } else {
         return var1 == DataLocationType.LDAP ? this.ldap : null;
      }
   }

   public final Folder findFolderForNewItem(Locatable var1, Folder var2) {
      DataLocationType var3 = var1.getLocation().getDataLocationType();
      return this.findFolderForType(var3, var2);
   }

   public final Folder findFolderForType(DataLocationType var1, Folder var2) {
      if (this.isTypeAllowed(var1, var2)) {
         return var2;
      }

      LocatableFolders.FolderDescriptor var3 = this.getDescriptorForType(var1);
      if (var3 != null) {
         Folder var4 = var3.findExistingFolder();
         if (var4 != null) {
            return var4;
         }
      }

      return this.parent;
   }

   public final Folder findFolderForFileBasedLocation(Folder var1) {
      return this.findFolderForType(DataLocationType.FileShare, var1);
   }

   public final EnumSet<DataLocationType> getAllowedTypesFor(Folder var1) {
      EnumSet var2 = null;

      for (LocatableFolders.FolderDescriptor var4 : Arrays.asList(this.files, this.databases, this.ldap)) {
         if (var4.isAncestorOf(var1)) {
            var2 = var4.getTypes();
            break;
         }
      }

      if (var2 == null) {
         var2 = EnumSet.allOf(DataLocationType.class);
      }

      Iterables.removeIf(var2, new Predicate<DataLocationType>() {
         public boolean apply(DataLocationType var1) {
            return !LocatableFolders.this.isSupported(var1);
         }
      });
      return var2;
   }

   private static class FolderDescriptor {
      private final Folder root;
      private final String name;
      private final EnumSet<DataLocationType> types;
      private final EntityFilter itemFilter;

      public FolderDescriptor(Folder var1, String var2, DataLocationType var3) {
         this(var1, var2, EnumSet.of(var3));
      }

      public FolderDescriptor(Folder var1, String var2, EnumSet<DataLocationType> var3) {
         this.root = var1;
         this.name = var2;
         this.types = EnumSet.copyOf(var3);
         this.itemFilter = new LocatableFilter(var3);
      }

      public void prepare(boolean var1) {
         Folder var2 = this.findExistingFolder();
         if (var2 == null && var1) {
            var2 = this.insertIntoRoot();
         }

         if (var2 != null) {
            var2.setItemFilter(this.itemFilter);
         }
      }

      public EnumSet<DataLocationType> getTypes() {
         return EnumSet.copyOf(this.types);
      }

      public Folder findExistingFolder() {
         Folder var1 = this.root.findSubFolder(this.name);
         return var1 != null && var1.isProtected() ? var1 : null;
      }

      private Folder insertIntoRoot() {
         Folder var1 = new Folder(this.name, this.root.getItemType());
         var1.protect();
         this.root.addSubFolder(var1);
         return var1;
      }

      public boolean isAncestorOf(Folder var1) {
         Folder var2 = this.findExistingFolder();
         return var1 == var2 || var2 != null && var2.isAncestorOf(var1);
      }

      public void restrictTypes(Locatable var1) {
         var1.setAllowedLocationTypes(this.types);
      }

      public boolean isTypeAllowed(Folder var1) {
         Folder var2 = this.findExistingFolder();
         return var2 == null || var1 == var2 || var2.isAncestorOf(var1);
      }
   }
}
