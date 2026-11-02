package org.etlforge.integration.data.project;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableList.Builder;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.VersionFactory;
import org.etlforge.base.ListenerReport;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.IntegrationEntityMapLookup;
import org.etlforge.integration.data.entity.ReferenceReplacementService;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.data.entity.metadata.EntityTypeConfig;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.IntegrationProjectListeners;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.system.callback.Receiver;
import org.etlforge.util.version.Version;

public final class IntegrationProject extends IntegrationEntity {
   public static final String PROPERTY_INTERCHANGE_PROJECT_ID = "interchangeproject_id";
   public static final String PROPERTY_INTERCHANGE_PROJECT_NAME = "interchangeproject_name";
   public static final String PROPERTY_DESCRIPTION = "description";
   private static final String PROPERTY_CREATED_AT = "created_at";
   private static final String PROPERTY_MODIFIED_AT = "modified_at";
   private static final String DOWNLOADED_AT = "downloaded_at";
   private static final String DOWNLOADED_FROM = "downloaded_from";
   private static final String PROPERTY_LOCATION_ON_DISK = "location_on_disk";
   private static final String PROPERTY_DEPLOYABLE = "deployable";
   private static final String[] PROPERTIES = new String[]{"interchangeproject_id", "interchangeproject_name", "description"};
   private final ImmutableMap<EntityType, RootFolder> rootFolders;
   private final IntegrationProjectListeners projectListeners = new IntegrationProjectListeners();
   private final PropertyChangeListener ownerListener;
   private final DeleteManager deleteManager;
   private String jitterPackComment = "";
   private IntegrationEntityMapLookup entityLookup;
   private GlobalDataElements globalDEs;
   private List<BuiltInIntegrationEntity> systemObjects;
   private boolean tempProject;

   private IntegrationProject(String var1, IntegrationProjectId var2, Iterable<EntityType> var3) {
      super(EntityType.Project, var2, Preconditions.checkNotNull(var1, "name"));
      this.ownerListener = new IntegrationProject.OwnerListener();
      this.deleteManager = new DeleteManager(this);
      this.entityLookup = this.createEmptyLookup();
      this.systemObjects = Lists.newArrayList();
      this.globalDEs = GlobalDataElements.EMPTY;
      this.rootFolders = this.createRootFolders(var3);
      this.addProjectListener(new IntegrationProject.EntitiesDeletedHandler());
   }

   private IntegrationEntityMapLookup createEmptyLookup() {
      IntegrationEntityMapLookup var1 = new IntegrationEntityMapLookup();
      var1.addItem(this);
      return var1;
   }

   private ImmutableMap<EntityType, RootFolder> createRootFolders(Iterable<EntityType> var1) {
      LinkedHashMap var2 = Maps.newLinkedHashMap();

      for (EntityType var4 : var1) {
         if (var4.hasRootFolder()) {
            RootFolder var5 = this.createRootFolder(var4);
            var2.put(var4, var5);
         }
      }

      if (!var2.containsKey(EntityType.Folder)) {
         RootFolder var6 = this.createRootFolder(EntityType.Folder);
         var2.put(EntityType.Folder, var6);
      }

      return ImmutableMap.copyOf(var2);
   }

   private RootFolder createRootFolder(EntityType var1) {
      RootFolder var2 = new RootFolder(var1.getPlural(), var1);
      var2.setParent(this);
      this.entityLookup.addItem(var2);
      return var2;
   }

   @VisibleForTesting
   public static IntegrationProject createNewProject(String var0) {
      return createNewProject(var0, defaultEntityTypes());
   }

   private static Iterable<EntityType> defaultEntityTypes() {
      HashSet var0 = Sets.newHashSet(EntityType.values());
      var0.remove(EntityType.Project);
      var0.remove(EntityType.None);
      var0.remove(EntityType.RootFolder);
      var0.remove(EntityType.SystemSource);
      var0.remove(EntityType.SystemTarget);
      var0.remove(EntityType.JmsMessage);
      return var0;
   }

   public static IntegrationProject createNewProject(String var0, Iterable<EntityType> var1) {
      IntegrationProjectId var2 = new IntegrationProjectId();
      if (var0 == null) {
         var0 = var2.toString();
      }

      IntegrationProject var3 = new IntegrationProject(var0, var2, var1);
      Date var4 = new Date();
      var3.setCreatedAt(var4);
      var3.setModifiedAt(var4);
      return var3;
   }

   public static IntegrationProject createExistingProjectShell(String var0, IntegrationProjectId var1, Iterable<EntityType> var2) {
      return new IntegrationProject(Strings.nullToEmpty(var0), Preconditions.checkNotNull(var1, "id"), var2);
   }

   public static IntegrationProject createPartialProject(IntegrationProject var0, Collection<? extends IntegrationEntity> var1) {
      PartialProjectCreator var2 = new PartialProjectCreator(var0, var1);
      IntegrationProject var3 = new IntegrationProject(var0.getName(), var0.getID(), var0.getSupportedEntityTypes());
      var2.create(var3);
      var3.entityLookup = IntegrationEntityMapLookup.build(var0);

      for (BuiltInIntegrationEntity var5 : var0.systemObjects) {
         var3.systemObjects.add(var5);
         var3.entityLookup.addItem((IntegrationEntity)var5);
      }

      return var3;
   }

   public IntegrationProjectId getID() {
      return (IntegrationProjectId)super.getID();
   }

   @Override
   protected String getIdKey() {
      return "interchangeproject_id";
   }

   @Override
   protected String getNameKey() {
      return "interchangeproject_name";
   }

   @Override
   public Date getCreatedAt() {
      long var1 = this.getLongProperty("created_at", -1L);
      return var1 >= 0L ? new Date(var1) : null;
   }

   public void setJitterPackComment(String var1) {
      this.jitterPackComment = var1 == null ? "" : var1;
   }

   public String getJitterPackComment() {
      return this.jitterPackComment;
   }

   @Override
   public void setCreatedAt(Date var1) {
      this.setStringProperty("created_at", String.valueOf(var1.getTime()));
   }

   @Override
   public Date getModifiedAt() {
      long var1 = this.getLongProperty("modified_at", -1L);
      return var1 >= 0L ? new Date(var1) : null;
   }

   @Override
   public void setModifiedAt(Date var1) {
      this.setStringProperty("modified_at", String.valueOf(var1.getTime()));
   }

   public File getLocationOnDisk() {
      String var1 = this.getProperty("location_on_disk");
      return var1 == null ? null : new File(var1);
   }

   public void setLocationOnDisk(File var1) {
      this.setStringProperty("location_on_disk", var1.getAbsolutePath());
   }

   public void setSystemObjects(BuiltInIntegrationEntity... var1) {
      this.systemObjects = Arrays.asList(var1);

      for (BuiltInIntegrationEntity var5 : var1) {
         this.entityLookup.addItem((IntegrationEntity)var5);
      }
   }

   public GlobalDataElements getGlobalDataElements() {
      return this.globalDEs;
   }

   public void setGlobalDataElements(GlobalDataElements var1) {
      Preconditions.checkNotNull(var1, "des");
      this.globalDEs = var1;
      var1.scanProject(this);
   }

   public ImmutableList<EntityType> getSupportedEntityTypes() {
      LinkedHashSet var1 = Sets.newLinkedHashSet(this.rootFolders.keySet());
      var1.remove(EntityType.Folder);
      return ImmutableList.copyOf(var1);
   }

   public RootFolder getRootFolder(EntityType var1) {
      return this.rootFolders.get(var1);
   }

   public ImmutableList<RootFolder> getRootFoldersOfType(Class<?> var1) {
      Builder var2 = ImmutableList.builder();

      for (EntityType var4 : this.getSupportedEntityTypes()) {
         if (EntityTypeConfig.isInstanceOf(var4, var1)) {
            var2.add(this.rootFolders.get(var4));
         }
      }

      return var2.build();
   }

   @Override
   public Iterable<IntegrationEntity> getAllChildren() {
      synchronized (this.getDataLock()) {
         return Collections.unmodifiableCollection(this.children);
      }
   }

   public Collection<RootFolder> getAllRootFolders() {
      return Collections.unmodifiableCollection(this.rootFolders.values());
   }

   public void deleteItems(Collection<? extends IntegrationEntity> var1) {
      ArrayList var2 = Lists.newArrayList(var1);
      Collections.sort(var2, new ItemDeleteOrder());
      this.deleteManager.startCollecting();

      for (Object __etlItem268 : var2) {
         IntegrationEntity var4 = (IntegrationEntity) __etlItem268;
         var4.delete();
      }

      this.deleteManager.processDeletedItems(this.projectListeners);
   }

   public List<IntegrationEntity> getAllEntities() {
      return this.getAllEntitiesImpl();
   }

   public void visitAllEntities(Receiver<IntegrationEntity> var1) {
      for (IntegrationEntity var3 : this.getAllEntitiesImpl()) {
         var1.handle(var3);
      }
   }

   private List<IntegrationEntity> getAllEntitiesImpl() {
      ArrayList var1 = Lists.newArrayList();

      for (RootFolder var3 : this.rootFolders.values()) {
         this.getAllDescendants(var3, var1, true);
      }

      return var1;
   }

   public List<IntegrationEntity> getAllEntitiesOfType(EntityType var1) {
      ArrayList var2 = Lists.newArrayList();
      RootFolder var3 = this.rootFolders.get(var1);
      if (var3 != null) {
         this.getAllDescendants(var3, var2, false);
      }

      return var2;
   }

   public <T extends IntegrationEntity> FluentIterable<T> getAllEntitiesOfClass(Class<T> var1) {
      return FluentIterable.from(this.getAllEntitiesImpl()).filter(var1);
   }

   public <T extends IntegrationEntity> void visitAllEntitiesOfClass(Class<T> var1, Receiver<T> var2) {
      for (T var4 : this.getAllEntitiesOfClass(var1)) {
         var2.handle(var4);
      }
   }

   private void getAllDescendants(IntegrationEntity var1, List<IntegrationEntity> var2, boolean var3) {
      for (IntegrationEntity var5 : var1.getAllChildren()) {
         if (!(var5 instanceof BuiltInIntegrationEntity)) {
            if (var3 || !(var5 instanceof Folder)) {
               var2.add(var5);
            }

            this.getAllDescendants(var5, var2, var3);
         }
      }
   }

   public void insertEntityUnderRoot(IntegrationEntity var1) {
      this.insertEntity(var1, null);
   }

   public void insertEntity(IntegrationEntity var1, IntegrationEntity var2) {
      if (var2 == null) {
         EntityType var3 = EntityUtils.getItemType(var1);
         var2 = this.getRootFolder(var3);
      }

      if (ProjectUtils.getProject(var2) == this) {
         var1.setCreatedAt(new Date());
         var1.setParent(var2);
         this.deleteManager.listenTo(var1);
         this.entityLookup.addItem(var1);
         var1.addPropertyChangeListener("IntegrationEntity#Parent", this.ownerListener);
         this.fireEntityWasAdded(var1);
      }
   }

   public void reviveItems(List<? extends IntegrationEntity> var1, EntityRevivorCallback var2) {
      IntegrationProject.EntityRevivor var3 = new IntegrationProject.EntityRevivor();

      for (IntegrationEntity var5 : var1) {
         var3.revive(var5);
      }

      var3.finish(var2);
      this.fireEntitiesWereRevived(var1);
   }

   public void markAsDeployDirty() {
      for (RootFolder var2 : this.rootFolders.values()) {
         this.markAsDeployDirtyImpl(var2);
      }
   }

   private void markAsDeployDirtyImpl(IntegrationEntity var1) {
      var1.setDeployDirty(true);
      var1.setHasMoved();

      for (IntegrationEntity var3 : var1.getAllChildren()) {
         this.markAsDeployDirtyImpl(var3);
      }
   }

   public void addProjectListener(ProjectListener var1) {
      this.projectListeners.add(var1);
   }

   public void removeProjectListener(ProjectListener var1) {
      this.projectListeners.remove(var1);
   }

   private void fireEntityWasAdded(IntegrationEntity var1) {
      this.projectListeners.call().entitiesAdded(new IntegrationProjectEvent(this, var1));
   }

   private void fireEntitiesWereRevived(List<? extends IntegrationEntity> var1) {
      this.projectListeners.call().entitiesRevived(new IntegrationProjectEvent(this, var1));
   }

   private void fireEntityWasMoved(IntegrationEntity var1) {
      this.projectListeners.call().entitiesMoved(new IntegrationProjectEvent(this, var1));
   }

   public void projectWasLoaded() {
      this.entityLookup = this.createEmptyLookup();

      for (RootFolder var2 : this.rootFolders.values()) {
         this.registerWithProject(var2);
      }

      for (BuiltInIntegrationEntity var4 : this.systemObjects) {
         this.entityLookup.addItem((IntegrationEntity)var4);
      }

      this.globalDEs.scanProject(this);
   }

   private void registerWithProject(IntegrationEntity var1) {
      this.entityLookup.addItem(var1);
      if (!(var1 instanceof BuiltInIntegrationEntity)) {
         var1.addPropertyChangeListener("IntegrationEntity#Parent", this.ownerListener);
         var1.clearModificationFlag();
         this.deleteManager.listenTo(var1);

         for (IntegrationEntity var3 : var1.getAllChildren()) {
            this.registerWithProject(var3);
         }
      }
   }

   public void refreshItemLookup() {
      this.entityLookup = this.createEmptyLookup();
      this.visitAllEntities(new Receiver<IntegrationEntity>() {
         public void handle(IntegrationEntity var1) {
            IntegrationProject.this.entityLookup.addItem(var1);
         }
      });

      for (BuiltInIntegrationEntity var2 : this.systemObjects) {
         this.entityLookup.addItem((IntegrationEntity)var2);
      }
   }

   @Override
   public ValidationStatus getValidationStatus() {
      ValidationStatus var1 = super.getValidationStatus();
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

   public String getContentString() {
      return new IntegrationProject.ContentStringBuilder().build();
   }

   @Override
   public String[] getPropertyKeys() {
      return PROPERTIES;
   }

   public String[] getFolderItemEntry() {
      return new String[]{"0", Integer.toString(20), this.getID().toString()};
   }

   @Override
   public StringKeyValuePairs getCopyOfKeyValues() {
      StringKeyValuePairs var1 = super.getCopyOfKeyValues();
      var1.put("Name", this.getName());
      var1.put("description", this.getDescription());
      return var1;
   }

   @Override
   public boolean isRenameable() {
      return true;
   }

   @Override
   public boolean isMoveable() {
      return false;
   }

   @Override
   public ListenerReport reportListeners() {
      ListenerReport var1 = super.reportListeners();
      var1.addListener("PropertyChangeListener", this.ownerListener);

      for (ProjectListener var3 : this.projectListeners.getListeners()) {
         var1.addListener("IntegrationProjectListener", var3);
      }

      return var1;
   }

   public IntegrationEntityLookup getItemLookup() {
      return this.entityLookup;
   }

   public DependencyStore getCurrentDependencies() {
      return new DependencyStore(this.entityLookup);
   }

   @Override
   public Version getRequiredVersion() {
      Version var1 = VersionFactory.newVersion(0, 0, 0, 0);

      for (IntegrationEntity var3 : this.getAllEntities()) {
         Version var4 = var3.getRequiredVersion();
         if (var4.isLaterThan(var1)) {
            var1 = var4;
         }
      }

      return var1;
   }

   @Override
   protected boolean canHaveChildren() {
      return true;
   }

   public boolean isDeployable() {
      return this.getBooleanProperty("deployable", true);
   }

   public void setDeployable(boolean var1) {
      this.setBooleanProperty("deployable", var1);
   }

   @Override
   public boolean isCopyable() {
      return false;
   }

   @Override
   public boolean isDeletable() {
      return false;
   }

   public boolean doesAtLeastOneItemNeedDeploy() {
      for (RootFolder var2 : this.getAllRootFolders()) {
         if (var2.isDeployDirty()) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void replaceReferences(ReferenceReplacementService var1) {
      throw new UnsupportedOperationException("Projects cannot be deeply copied yet.");
   }

   public void generateNewId() {
      this.setID(new IntegrationProjectId());
   }

   public void flagAsTemporaryProject() {
      synchronized (this.getDataLock()) {
         this.tempProject = true;
      }
   }

   public boolean isTemporaryProject() {
      synchronized (this.getDataLock()) {
         return this.tempProject;
      }
   }

   public void setDownloadInfo(DownloadInfo var1) {
      synchronized (this.getDataLock()) {
         this.setProperty("downloaded_at", (long)var1.getWhen().getTime());
         this.setProperty("downloaded_from", var1.getServerName());
      }
   }

   public DownloadInfo getDownloadInfo() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("downloaded_from");
         if (var2 != null) {
            long var3 = this.getLongProperty("downloaded_at", -1L);
            if (var3 >= 0L) {
               return new DownloadInfo(var2, new Date(var3));
            }
         }

         return null;
      }
   }

   private class ContentStringBuilder {
      private StringBuilder sb;

      private ContentStringBuilder() {
      }

      public String build() {
         this.sb = new StringBuilder("Project: ").append(IntegrationProject.this.getName());
         this.sb.append(SystemUtils.LINE_SEPARATOR);

         for (RootFolder var2 : IntegrationProject.this.rootFolders.values()) {
            this.appendItemString(var2, 0);
         }

         return this.sb.toString();
      }

      private void appendItemString(IntegrationEntity var1, int var2) {
         for (int var3 = 0; var3 < var2; var3++) {
            this.sb.append(" ");
         }

         this.sb.append(var1.getName());
         this.sb.append("  ");
         this.sb.append(var1.getID());
         this.sb.append("  Deploy dirty: ");
         this.sb.append(var1.isDeployDirty());
         this.sb.append(SystemUtils.LINE_SEPARATOR);
         var2 += 4;

         for (IntegrationEntity var4 : var1.getAllChildren()) {
            this.appendItemString(var4, var2);
         }
      }
   }

   private class EntitiesDeletedHandler extends ProjectListener.Adapter {
      private EntitiesDeletedHandler() {
      }

      @Override
      public void entitiesDeleted(IntegrationProjectEvent var1) {
         for (IntegrationEntity var3 : var1.getEntities()) {
            IntegrationProject.this.entityLookup.removeItem(var3);
            var3.removePropertyChangeListener("IntegrationEntity#Parent", IntegrationProject.this.ownerListener);
         }
      }
   }

   private class EntityRevivor {
      private int numberOfRenamedEntities;

      private EntityRevivor() {
      }

      public void revive(IntegrationEntity var1) {
         this.clearFlags(var1);
         this.addToParent(var1);
         this.checkName(var1);
         this.setupListeners(var1);
      }

      private void clearFlags(IntegrationEntity var1) {
         var1.setDeleted(false);
         var1.setDeployDirty(true);
      }

      private void addToParent(IntegrationEntity var1) {
         var1.setParent(var1.getParent());
         IntegrationProject.this.entityLookup.addItem(var1);
      }

      private void checkName(IntegrationEntity var1) {
         String var2 = var1.getName();
         String var3 = EntityUtils.getAvailableName(var1, var2);
         if (!var3.equals(var2)) {
            var1.setName(var3);
            this.numberOfRenamedEntities++;
         }
      }

      private void setupListeners(IntegrationEntity var1) {
         var1.addPropertyChangeListener("IntegrationEntity#Parent", IntegrationProject.this.ownerListener);
         IntegrationProject.this.deleteManager.listenTo(var1);
      }

      public void finish(EntityRevivorCallback var1) {
         if (this.numberOfRenamedEntities > 0) {
            var1.warning("At least one of the revived items had to be renamed.");
         }
      }
   }

   private class OwnerListener implements PropertyChangeListener {
      private OwnerListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         Object var2 = var1.getSource();
         if (var2 instanceof IntegrationEntity && var1.getNewValue() != null) {
            IntegrationProject.this.fireEntityWasMoved((IntegrationEntity)var2);
         }
      }
   }
}
