package org.etlforge.integration.data.entity;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Constraint;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.id.FolderId;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.system.annotations.InternalConstructor;

public class Folder extends IntegrationEntity {
   public static final String FOLDER_ID = "folder_id";
   public static final String FOLDER_NAME = "folder_name";
   public static final String CONTENTS_ENTITY_TYPE = "contentsentitytype_id";
   public static final String PROTECTED = "protected";
   private static final String[] PROPERTIES = new String[]{"folder_id", "folder_name", "contentsentitytype_id", "protected"};
   private final EntityType itemType;
   private Optional<EntityFilter> itemFilter = Optional.absent();

   public static final Folder createFolder(StringKeyValuePairs var0) {
      int var1 = var0.getInt("contentsentitytype_id", -1);
      EntityType var2 = null;
      if (var1 != -1) {
         var2 = EntityType.fromId(var1);
      }

      String var3 = var0.getString("folder_id");
      String var4 = var0.getString("folder_name");
      boolean var5 = var0.getBoolean("protected");
      Folder var6 = new Folder(new FolderId(var3), var4, var2);
      var6.setProperty("protected", var5, false);
      return var6;
   }

   public Folder(String var1, EntityType var2) {
      this(null, var1, var2);
   }

   public Folder(FolderId var1, String var2, EntityType var3) {
      super(EntityType.Folder, var1, var2);
      this.itemType = var3;
   }

   @InternalConstructor
   public Folder(FolderId var1, String var2, String var3) {
      this(var1, var2, EntityType.valueOf(var3));
   }

   @Override
   public String[] getPropertyKeys() {
      return (String[])PROPERTIES.clone();
   }

   public final EntityType getItemType() {
      return this.itemType;
   }

   public FolderId getID() {
      return (FolderId)super.getID();
   }

   public final boolean isAllowedChild(IntegrationEntity var1) {
      if (var1.getParent() == this) {
         return false;
      }

      if (var1 instanceof Folder && ((Folder)var1).isAncestorOf(this)) {
         return false;
      }

      FolderChildFilter var2 = new FolderChildFilter(this);
      return var2.apply(var1);
   }

   public boolean addSubFolder(Folder var1) {
      Folder var2 = (Folder)this.checkChildConstraint(var1);
      if (var2.getParent() == this) {
         return false;
      }

      Preconditions.checkState(
         var2.getParent() == null, "subFolder already belongs to a folder. It must be removed from that folder before it can be added to this folder."
      );
      if (var1.isAncestorOf(this)) {
         throw new IllegalArgumentException("Recursive folder structures are not allowed.");
      }

      var2.setParent(this);
      return true;
   }

   private IntegrationEntity checkChildConstraint(IntegrationEntity var1) {
      Constraint var2 = this.getChildConstraint();
      return (IntegrationEntity) (var2.checkElement(var1));
   }

   private Constraint<IntegrationEntity> getChildConstraint() {
      FolderChildFilter var1 = new FolderChildFilter(this);
      return EntityConstraints.of(var1, "Not allowed under this folder: ");
   }

   public final List<Folder> getAllSubFolders() {
      ArrayList var1 = Lists.newArrayList();

      for (IntegrationEntity var3 : this.getAllChildren()) {
         if (var3 instanceof Folder) {
            var1.add((Folder)var3);
         }
      }

      return var1;
   }

   public boolean addItem(IntegrationEntity var1) {
      IntegrationEntity var2 = this.checkChildConstraint(var1);
      if (var2.getParent() == this) {
         return false;
      }

      Preconditions.checkState(
         var2.getParent() == null, "item already belongs to another folder. It must be removed from that folder before it can be added to this folder."
      );
      var2.setParent(this);
      return true;
   }

   @Override
   protected void addChild(IntegrationEntity var1) {
      this.checkChildType(var1);
      super.addChild(var1);
   }

   private void checkChildType(IntegrationEntity var1) {
      EntityType var2 = EntityUtils.getItemType(var1);
      Preconditions.checkArgument(
         this.itemType == null || var2 == this.itemType, "This folder may only contain items of type " + this.itemType + ". Got: " + var2
      );
   }

   @Override
   public final Iterable<IntegrationEntity> getAllChildren() {
      synchronized (this.getDataLock()) {
         return Lists.newArrayList(this.children);
      }
   }

   public final List<IntegrationEntity> getAllItems() {
      ArrayList var1 = Lists.newArrayList();

      for (IntegrationEntity var3 : this.getAllChildren()) {
         if (!(var3 instanceof Folder)) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public final <T extends IntegrationEntity> List<T> getAllItemsRecursively(Class<T> var1) {
      ArrayList var2 = Lists.newArrayList();
      collectAllItemsRecursively(this, var1, var2);
      return var2;
   }

   private static <E extends IntegrationEntity> void collectAllItemsRecursively(IntegrationEntity var0, Class<E> var1, List<E> var2) {
      if (var1.isInstance(var0)) {
         var2.add(var1.cast(var0));
      }

      if (var0 instanceof Folder) {
         for (IntegrationEntity var4 : var0.getAllChildren()) {
            collectAllItemsRecursively(var4, var1, var2);
         }
      }
   }

   public List<IntegrationEntity> getAllItemsRecursively() {
      ArrayList var1 = Lists.newArrayList();
      collectAllItemsRecursively(this, var1);
      return var1;
   }

   private static void collectAllItemsRecursively(IntegrationEntity var0, List<IntegrationEntity> var1) {
      if (var0 instanceof Folder) {
         for (IntegrationEntity var3 : var0.getAllChildren()) {
            collectAllItemsRecursively(var3, var1);
         }
      } else {
         var1.add(var0);
      }
   }

   @Override
   public final boolean isDeployDirty() {
      return super.isDeployDirty() || this.hasDeployDirtyDescendants();
   }

   private boolean hasDeployDirtyDescendants() {
      return Iterables.any(this.getAllItems(), EntityFilters.DEPLOY_DIRTY) || Iterables.any(this.getAllSubFolders(), EntityFilters.DEPLOY_DIRTY);
   }

   public final int getNumberOfSubFoldersRecursively() {
      int var1 = 0;

      for (IntegrationEntity var3 : this.getAllChildren()) {
         if (var3 instanceof Folder) {
            var1 += 1 + ((Folder)var3).getNumberOfSubFoldersRecursively();
         }
      }

      return var1;
   }

   public final int getNumberOfItemsRecursively() {
      int var1 = 0;

      for (IntegrationEntity var3 : this.getAllChildren()) {
         if (var3 instanceof Folder) {
            var1 += ((Folder)var3).getNumberOfItemsRecursively();
         } else {
            var1++;
         }
      }

      return var1;
   }

   public final IntegrationEntity getDescendant(IntegrationEntityId var1) {
      return this.getDescendantImpl(this, var1);
   }

   private IntegrationEntity getDescendantImpl(IntegrationEntity var1, IntegrationEntityId var2) {
      if (var1.getID().equals(var2)) {
         return var1;
      }

      for (IntegrationEntity var4 : var1.getAllChildren()) {
         IntegrationEntity var5 = this.getDescendantImpl(var4, var2);
         if (var5 != null) {
            return var5;
         }
      }

      return null;
   }

   @Override
   public final StringKeyValuePairs getCopyOfKeyValues() {
      StringKeyValuePairs var1 = new StringKeyValuePairs();
      var1.put("folder_id", this.getID().toString());
      String var2 = this.getName();
      if (var2.equals("ETLForge Connect\u2122")) {
         var2 = "ETLForge Connect";
      }

      var1.put("folder_name", var2);
      var1.put(this.getNameKey(), var2);
      var1.put("contentsentitytype_id", Integer.toString(this.getItemType().getId()));
      var1.putBoolean("protected", this.isProtected());
      return var1;
   }

   @Override
   protected final boolean canHaveChildren() {
      return true;
   }

   @Override
   public final boolean isCopyable() {
      return false;
   }

   public final void traverseItemsDepthFirst(Predicate<IntegrationEntity> var1) {
      this.traverseItemsDepthFirstImpl(var1);
   }

   private boolean traverseItemsDepthFirstImpl(Predicate<IntegrationEntity> var1) {
      for (Folder var3 : this.getAllSubFolders()) {
         if (!var3.traverseItemsDepthFirstImpl(var1)) {
            return false;
         }
      }

      for (IntegrationEntity var5 : this.getAllItems()) {
         if (!var1.apply(var5)) {
            return false;
         }
      }

      return true;
   }

   @Override
   public final void replaceReferences(ReferenceReplacementService var1) {
      throw new UnsupportedOperationException("Deep cloning of folders are not supported yet.");
   }

   public void generateNewId() {
      this.setID(new FolderId());
   }

   @Override
   public final ValidationStatus getValidationStatus() {
      return this.getCompoundValidationStatus();
   }

   public final ValidationStatus getCompoundValidationStatus() {
      ValidationStatus var1 = this.getCurrentValidationResult().status();
      if (var1 == ValidationStatus.INVALID) {
         return var1;
      }

      for (IntegrationEntity var3 : this.getAllChildren()) {
         ValidationStatus var4 = var3.getValidationStatus();
         if (var4.isMoreSevere(var1)) {
            var1 = var4;
            if (var1 == ValidationStatus.INVALID) {
               break;
            }
         }
      }

      return var1;
   }

   public final Folder findSubFolder(String var1) {
      for (Folder var3 : this.getAllSubFolders()) {
         if (var3.getName().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   @Override
   public final boolean isDeletable() {
      return !this.isProtected();
   }

   @Override
   public final boolean isMoveable() {
      return !this.isProtected();
   }

   @Override
   public final boolean isRenameable() {
      return !this.isProtected() && !this.isEtlforgeConnectFolder() && !ETLForgeConnectNames.isConnectWizardFolder(this);
   }

   public final boolean isEtlforgeConnectFolder() {
      return ETLForgeConnectNames.isEtlforgeConnectFolder(this);
   }

   public final boolean isSalesforceFolder() {
      return ETLForgeConnectNames.isSalesforceWizardsFolder(this);
   }

   public final boolean isSharedSalesforceItemsFolder() {
      return ETLForgeConnectNames.isSharedSalesforceItemsFolder(this);
   }

   public final Optional<EntityFilter> getItemFilter() {
      synchronized (this.getDataLock()) {
         for (IntegrationEntity var2 = this; var2 instanceof Folder; var2 = var2.getParent()) {
            Folder var3 = (Folder)var2;
            if (var3.itemFilter.isPresent()) {
               return var3.itemFilter;
            }
         }
      }

      return Optional.absent();
   }

   public final void setItemFilter(EntityFilter var1) {
      synchronized (this.getDataLock()) {
         this.itemFilter = var1 == null ? Optional.absent() : Optional.of(var1);
      }
   }

   public final boolean isProtected() {
      return this.getBooleanProperty("protected", false);
   }

   public final void protect() {
      this.setBooleanProperty("protected", true);
   }

   public final boolean isEmpty() {
      return this.getNumberOfChildren() == 0;
   }
}
