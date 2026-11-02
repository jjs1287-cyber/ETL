package org.etlforge.integration.data.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.VersionFactory;
import org.etlforge.application.extension.ExtensionObjectService;
import org.etlforge.base.ListenerReport;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.ValidationListener;
import org.etlforge.integration.data.ValidationMessageCollectorImpl;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.validation.ValidationContext;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationResult;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.system.annotations.InternalMethod;
import org.etlforge.util.id.KongaID;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.name.NameValidator;
import org.etlforge.util.name.NameValidatorStore;
import org.etlforge.util.persist.BinaryPersistor;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.version.Version;

public abstract class IntegrationEntity extends IntegrationData {
   public static final String DEPLOY_DIRTY_PROPERTY = "IntegrationEntity#DeployDirty";
   public static final String PARENT_PROPERTY = "IntegrationEntity#Parent";
   public static final String CHILD_ADDED_PROPERTY = "IntegrationEntity#ChildAdded";
   public static final String CHILD_REMOVED_PROPERTY = "IntegrationEntity#ChildRemoved";
   public static final String CREATED_AT_PROPERTY = "IntegrationEntity#CreatedAt";
   public static final String MODIFIED_AT_PROPERTY = "IntegrationEntity#MofidiedAt";
   public static final String TRANSIENT_PROPERTY = "IntegrationEntity#Transient";
   private static final Version MINIMUM_REQUIRED_VERSION = VersionFactory.newVersion(3, 0, 0, 20);
   private transient IntegrationEntity parent;
   protected List<IntegrationEntity> children;
   private EntityType entityType;
   private boolean hasBeenDeployed;
   private boolean deployDirty = true;
   private boolean hasMoved;
   private transient boolean isTransient;
   private transient Date deletedAt;
   private transient List<ValidationListener> validationListeners = new CopyOnWriteArrayList<>();
   private transient ValidationResult currentValidationResult = ValidationResult.VALID;
   private boolean modified;

   protected IntegrationEntity(EntityType var1, IntegrationEntityId var2, String var3) {
      super(var2 == null ? var1.createIntegrationEntityId() : var2, var3);
      this.entityType = var1;
      this.children = Lists.newArrayList();
      this.setProperty(this.getIdKey(), var2);
      this.addPropertyChangeListener(new IntegrationEntity.ModificationListener());
   }

   public IntegrationEntityId getID() {
      KongaID var1 = super.getID();
      if (var1 instanceof IntegrationEntityId) {
         return (IntegrationEntityId)var1;
      }

      IntegrationEntityId var2 = this.createNewID();
      this.setID(var2);
      return var2;
   }

   private IntegrationEntityId createNewID() {
      return this.entityType.createIntegrationEntityId();
   }

   public boolean isDeployDirty() {
      return this.deployDirty || !this.hasBeenDeployed;
   }

   public final void setDeployDirty(boolean var1) {
      boolean var2;
      synchronized (this.getDataLock()) {
         var2 = this.deployDirty;
         this.deployDirty = var1;
         if (!var1) {
            this.hasMoved = false;
         }
      }

      if (var2 != var1) {
         this.firePropertyChange("IntegrationEntity#DeployDirty", var2, var1);
      }
   }

   public final boolean hasMoved() {
      if (this instanceof BuiltInIntegrationEntity) {
         return false;
      }

      synchronized (this.getDataLock()) {
         return this.hasMoved;
      }
   }

   @InternalMethod
   public final void setHasMoved() {
      synchronized (this.getDataLock()) {
         this.hasMoved = true;
         this.deployDirty = true;
      }
   }

   protected String getNameKey() {
      String var1 = this.getEntityType().name() + "_name";
      return var1.toLowerCase();
   }

   protected String getIdKey() {
      String var1 = this.getEntityType().name() + "_id";
      return var1.toLowerCase();
   }

   public final EntityType getEntityType() {
      return this.entityType;
   }

   protected final void setEntityType(EntityType var1) {
      this.entityType = var1;
   }

   public final IntegrationEntity getParent() {
      synchronized (this.getDataLock()) {
         return this.parent;
      }
   }

   public RootFolder getRootFolder() {
      for (IntegrationEntity var1 = this.getParent(); var1 != null; var1 = var1.getParent()) {
         if (var1 instanceof RootFolder) {
            return (RootFolder)var1;
         }
      }

      return null;
   }

   @Override
   public final String getName() {
      String var1 = super.getName();
      if (StringUtils.isNotEmpty(var1)) {
         return var1;
      }

      var1 = this.getProperty(this.getNameKey());
      if (StringUtils.isNotEmpty(var1)) {
         try {
            this.setName(var1);
         } catch (IllegalNameException var3) {
            var3.printStackTrace();
         }
      }

      return var1;
   }

   @Override
   public void setName(String var1) throws IllegalNameException {
      if (var1 == null) {
         var1 = "";
      }

      var1 = var1.trim();
      if (!EntityUtils.isNameUnique(this, var1)) {
         throw new IllegalNameException("The name \"" + var1 + "\" is already in use.");
      }

      super.setName(var1);
   }

   public final boolean hasBeenDeployed() {
      if (this instanceof BuiltInIntegrationEntity) {
         return true;
      }

      synchronized (this.getDataLock()) {
         return this.hasBeenDeployed;
      }
   }

   @InternalMethod
   public final void setDeployed(boolean var1) {
      synchronized (this.getDataLock()) {
         this.hasBeenDeployed = var1;
      }
   }

   @Override
   protected void setID(KongaID var1) {
      synchronized (this.getDataLock()) {
         super.setID(var1);
         this.setProperty(this.getIdKey(), var1);
      }
   }

   public final void setPropertyValues(StringKeyValuePairs var1) {
      String var2 = this.getNameKey();
      String var3 = this.getIdKey();

      for (String var5 : var1.getKeys()) {
         if (!var5.equals(var2) && !var5.equals(var3)) {
            this.setProperty(var5, var1.get(var5));
         }
      }
   }

   public abstract String[] getPropertyKeys();

   public final String[] getPropertyValuesForDeploy() {
      String[] var1 = this.getPropertyKeys();
      return this.getPropertyValuesForDeploy(var1);
   }

   public final String[] getPropertyValuesForDeploy(String[] var1) {
      int var2 = var1.length;
      String[] var3 = new String[var2];
      StringKeyValuePairs var4 = this.getCopyOfKeyValues();
      var4.put(this.getIdKey(), this.getID().toString());
      String var5 = this.getNameKey();
      if (!var4.containsKey(var5)) {
         var4.put(var5, this.getName());
      }

      for (int var6 = 0; var6 < var2; var6++) {
         String var7 = var4.get(var1[var6]);
         var3[var6] = var7;
      }

      return var3;
   }

   public IntegrationEntity clone() {
      synchronized (this.getDataLock()) {
         IntegrationEntity var2 = (IntegrationEntity)super.clone();
         var2.parent = null;
         var2.children = Lists.newArrayList();
         var2.validationListeners = new CopyOnWriteArrayList<>();
         var2.hasBeenDeployed = false;
         var2.deployDirty = true;
         var2.hasMoved = false;
         var2.addPropertyChangeListener(var2.new ModificationListener());
         return var2;
      }
   }

   public final IntegrationEntity cloneWithNewID() {
      synchronized (this.getDataLock()) {
         IntegrationEntity var2 = this.clone();
         IntegrationEntityId var3 = var2.createNewID();
         var2.setID(var3);
         var2.isTransient = true;
         return var2;
      }
   }

   public final IntegrationEntity copyWithIdentity(IntegrationEntityId identity) {
      Preconditions.checkNotNull(identity, "identity");
      IntegrationEntity copy = this.clone();
      copy.setID(identity);
      return copy;
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder(this.getName());
      var1.append(" [").append(this.entityType.getDisplayName()).append("]");
      var1.append(" [").append(this.getID()).append("]");
      return var1.toString();
   }

   @Override
   public final boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof IntegrationEntity && this.getID().equals(((IntegrationEntity)var1).getID());
   }

   @Override
   public final int hashCode() {
      return this.getID().hashCode();
   }

   public ValidationStatus getValidationStatus() {
      synchronized (this.getDataLock()) {
         return this.getCurrentValidationResult().status();
      }
   }

   @Override
   protected final void collectValidationMessages(ValidationMessageCollector var1) {
      try {
         this.checkName();
      } catch (IllegalNameException var3) {
         var1.addMessage(var3.getMessage(), ValidationStatus.INVALID);
      }

      this.validateOtherPropertiesThanTheName(var1);
   }

   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
   }

   private void checkName() throws IllegalNameException {
      String var1 = this.getName();
      if (!EntityUtils.isNameUnique(this, var1)) {
         throw new IllegalNameException("The name \"" + var1 + "\" is already in use.");
      }

      NameValidator var2 = NameValidatorStore.getInstance().get(this);
      if (var2 != null) {
         var2.validateName(var1);
      }
   }

   public List<? extends EntityLink> getLinks(IntegrationEntityLookup var1) {
      return Lists.newArrayList();
   }

   public void setParent(IntegrationEntity var1) {
      IntegrationEntity var2;
      boolean var3;
      synchronized (this.getDataLock()) {
         var2 = this.getParent();
         var3 = var1 != var2;
         if (var3 && var2 != null) {
            var2.removeChild(this);
            this.setHasMoved();
         }

         this.parent = var1;
         if (var1 != null) {
            var1.addChild(this);
         }
      }

      if (var3) {
         this.firePropertyChange("IntegrationEntity#Parent", var2, var1);
      }
   }

   public final int getNumberOfChildren() {
      synchronized (this.getDataLock()) {
         return this.children.size();
      }
   }

   @InternalMethod
   protected void addChild(IntegrationEntity var1) {
      Preconditions.checkState(this.canHaveChildren(), "This entity may not have children");
      Preconditions.checkNotNull(var1, "child");
      synchronized (this.getDataLock()) {
         if (this.children.contains(var1)) {
            return;
         }

         this.children.add(var1);
      }

      this.firePropertyChange("IntegrationEntity#ChildAdded", null, var1);
   }

   protected boolean canHaveChildren() {
      return false;
   }

   public Iterable<IntegrationEntity> getAllChildren() {
      return Collections.emptyList();
   }

   @InternalMethod
   boolean removeChild(IntegrationEntity var1) {
      boolean var2;
      synchronized (this.getDataLock()) {
         var2 = this.children.remove(var1);
      }

      if (var2) {
         this.firePropertyChange("IntegrationEntity#ChildRemoved", var1, null);
      }

      return var2;
   }

   @Override
   public final void delete() {
      if (!this.isDeletable()) {
         throw new UnsupportedOperationException("This item cannot be deleted");
      }

      synchronized (this.getDataLock()) {
         if (!this.isDeleted()) {
            if (this.getParent() != null) {
               this.getParent().removeChild(this);
            }

            for (IntegrationEntity var4 : Lists.newArrayList(this.getAllChildren())) {
               var4.delete();
            }

            super.delete();
            this.doAdditionalCleanupOnDelete();
            this.deletedAt = new Date();
         }
      }
   }

   protected void doAdditionalCleanupOnDelete() {
   }

   public final boolean isTransient() {
      synchronized (this.getDataLock()) {
         return this.isTransient;
      }
   }

   @InternalMethod
   public final void setTransient(boolean var1) {
      boolean var2;
      synchronized (this.getDataLock()) {
         var2 = this.isTransient;
         this.isTransient = var1;
      }

      if (var2 != var1) {
         this.firePropertyChange("IntegrationEntity#Transient", var2, var1);
      }
   }

   public boolean isRenameable() {
      return true;
   }

   public final Date getDeletedAt() {
      synchronized (this.getDataLock()) {
         return this.isDeleted() ? this.deletedAt : null;
      }
   }

   public void setCreatedAt(Date var1) {
      this.setLongProperty("IntegrationEntity#CreatedAt", var1.getTime());
   }

   public Date getCreatedAt() {
      long var1 = this.getLongProperty("IntegrationEntity#CreatedAt", -1L);
      return var1 >= 0L ? new Date(var1) : null;
   }

   public final void willBeSaved() {
      synchronized (this.getDataLock()) {
         if (this.modified) {
            this.setModifiedAt(new Date());
            this.modified = false;
         }
      }
   }

   public final void clearModificationFlag() {
      synchronized (this.getDataLock()) {
         this.modified = false;
      }
   }

   public void setModifiedAt(Date var1) {
      this.setLongProperty("IntegrationEntity#MofidiedAt", var1.getTime());
   }

   public Date getModifiedAt() {
      long var1 = this.getLongProperty("IntegrationEntity#MofidiedAt", -1L);
      return var1 >= 0L ? new Date(var1) : null;
   }

   public boolean isEditable() {
      return true;
   }

   public boolean isMoveable() {
      return true;
   }

   public boolean isCopyable() {
      return true;
   }

   public boolean isDeepCopyable() {
      return false;
   }

   public boolean isDeletable() {
      return true;
   }

   public final boolean isAncestorOf(IntegrationEntity var1) {
      for (IntegrationEntity var2 = var1; var2 != null; var2 = var2.getParent()) {
         if (var2 == this) {
            return true;
         }
      }

      return false;
   }

   @Override
   public final void writeTo(Persistor var1) {
      synchronized (this.getDataLock()) {
         this.writeHeader(var1);
         super.writeTo(var1);
         this.writeAdditionalData(var1);
      }
   }

   protected void writeAdditionalData(Persistor var1) {
   }

   private void writeHeader(Persistor var1) {
      Persistor var2 = var1.createChild("Header");
      var2.putString("Name", this.getName());
      var2.putText("Description", this.getDescription());
      var2.putString("ID", this.getID().toString());
      var2.putBoolean("Deployed", this.hasBeenDeployed);
      var2.putBoolean("DeployDirty", this.deployDirty);
      var2.putBoolean("HasMoved", this.hasMoved);
      var2.putBoolean("Deleted", this.isDeleted());
   }

   @Override
   public final void restoreFrom(Persistor var1) {
      if (this instanceof BuiltInIntegrationEntity) {
         throw new UnsupportedOperationException("Built-in project items should not be restored. This is a bug.");
      }

      synchronized (this.getDataLock()) {
         this.restoreHeader(var1);
         super.restoreFrom(var1);
         this.restoreAdditionalData(var1);
      }
   }

   private void restoreHeader(Persistor var1) {
      Persistor var2 = var1.getFirstChild("Header");
      if (var2 == null) {
         throw new IllegalArgumentException("Invalid persistor: the Header element is missing");
      }

      String var3 = var2.getString("Name");
      if (var3 == null) {
         throw new IllegalArgumentException("Invalid persistor: the Name field is missing");
      }

      try {
         this.setName(var3);
      } catch (IllegalNameException var5) {
         var5.printStackTrace();
      }

      this.setDescription(var2.getText("Description"));
      this.hasBeenDeployed = var2.getBoolean("Deployed");
      this.deployDirty = var2.getBoolean("DeployDirty");
      this.hasMoved = var2.getBoolean("HasMoved");
      this.setDeleted(var2.getBoolean("Deleted"));
   }

   protected void restoreAdditionalData(Persistor var1) {
   }

   public void restoreDependentData(Persistor var1, IntegrationEntityLookup var2) {
   }

   public void doPostImportProcessing(IntegrationEntityLookup var1) {
   }

   public final void importFrom(IntegrationEntity var1) {
      Preconditions.checkArgument(this.entityType == var1.entityType, "other must be of the same type as this entity");
      Preconditions.checkArgument(this.getID().equals(var1.getID()), "other must have the same ID as this entity");
      synchronized (this.getDataLock()) {
         synchronized (var1.getDataLock()) {
            this.setID(var1.getID());
            if (EntityUtils.isNameUnique(this, var1.getName())) {
               this.setName(var1.getName());
            }

            this.setDescription(var1.getDescription());
            BinaryPersistor var4 = BinaryPersistor.newRoot("Properties");
            var1.writeTo(var4);
            var4.getFirstChild("Header").putString("Name", this.getName());
            this.restoreFrom(var4);
            this.importAdditionalDataFrom(var1);
         }
      }

      this.firePropertyChange(null, null, null);
   }

   protected void importAdditionalDataFrom(IntegrationEntity var1) {
   }

   public final <T> T getExtensionObject(Class<T> var1) {
      return ExtensionObjectService.getService().getExtensionObject(this, var1);
   }

   public Version getRequiredVersion() {
      return MINIMUM_REQUIRED_VERSION;
   }

   public void replaceReferences(ReferenceReplacementService var1) {
   }

   public final ValidationResult validate(ValidationContext var1) {
      if (this.isDeleted()) {
         return ValidationResult.VALID;
      }

      ValidationMessageCollectorImpl var2 = new ValidationMessageCollectorImpl(var1);
      ValidationResult var3;
      synchronized (this.getDataLock()) {
         var2.collect(this);
         var3 = var2.result();
         this.currentValidationResult = var3;
      }

      this.notifyValidationListeners(var3);
      return var3;
   }

   private void notifyValidationListeners(ValidationResult var1) {
      for (ValidationListener var3 : this.validationListeners) {
         var3.validationComplete(this, var1);
      }
   }

   public final void addValidationListener(ValidationListener var1) {
      this.validationListeners.add(var1);
   }

   public final void removeValidationListener(ValidationListener var1) {
      this.validationListeners.remove(var1);
   }

   public final ValidationResult getCurrentValidationResult() {
      synchronized (this.getDataLock()) {
         return this.currentValidationResult;
      }
   }

   @Override
   public ListenerReport reportListeners() {
      ListenerReport var1 = super.reportListeners();
      var1.addListeners("Validation", this.validationListeners);
      return var1;
   }

   @Override
   public final void addPropertyChangeListener(PropertyChangeListener var1) {
      if (!(this instanceof BuiltInIntegrationEntity)) {
         super.addPropertyChangeListener(var1);
      }
   }

   @Override
   public final void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      if (!(this instanceof BuiltInIntegrationEntity)) {
         super.addPropertyChangeListener(var1, var2);
      }
   }

   public final boolean save() throws InterchangeSavingException {
      EntityPersistor var1 = this.getExtensionObject(EntityPersistor.class);
      if (var1 == null) {
         return false;
      }

      var1.save(this);
      return true;
   }

   private class ModificationListener implements PropertyChangeListener {
      private final Set<String> propertiesToIgnore = Sets.newHashSet(
         "IntegrationEntity#MofidiedAt", "IntegrationEntity#DeployDirty", "IntegrationEntity#CreatedAt", "IntegrationEntity#Transient", "Deleted"
      );

      private ModificationListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (this.isRelevant(var1)) {
            IntegrationEntity.this.modified = true;
         }
      }

      private boolean isRelevant(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         return !this.propertiesToIgnore.contains(var2);
      }
   }
}
