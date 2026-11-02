package org.etlforge.integration.client.project.migration;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import org.etlforge.application.filestore.*;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.integration.client.project.*;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.project.*;
import org.etlforge.util.persist.BinaryPersistor;

public final class ProjectMigration {
   public static final class Entry {
      public final IntegrationEntity source, destination;
      public final String action;
      Entry(IntegrationEntity source, IntegrationEntity destination, String action) {
         this.source=source; this.destination=destination; this.action=action;
      }
   }
   public static final class Plan {
      public final List<Entry> entries=new ArrayList<>();
      public int created, updated, kept;
      public String summary() { return created+" created, "+updated+" replaced, "+kept+" preserved"; }
   }
   public static final class Result {
      public final Plan plan;
      public final Path backup;
      Result(Plan plan, Path backup) { this.plan=plan; this.backup=backup; }
   }
   public static boolean preserve(IntegrationEntity entity) {
      EntityType type=entity.getEntityType();
      return type==EntityType.ProjectVariable || type==EntityType.Source || type==EntityType.Target;
   }
   private static String path(IntegrationEntity entity) {
      LinkedList<String> parts=new LinkedList<>();
      for(IntegrationEntity item=entity; item!=null && !(item instanceof IntegrationProject); item=item.getParent())
         parts.addFirst(item instanceof RootFolder?((RootFolder)item).getItemType().name():item.getName());
      return String.join("/",parts);
   }
   public static Plan plan(IntegrationProject source, IntegrationProject target) {
      if(source==target)throw new IllegalArgumentException("Select a different destination project.");
      Plan plan=new Plan();Set<IntegrationEntityId> claimed=new HashSet<>();
      List<IntegrationEntity> existing=target.getAllEntities();
      for(IntegrationEntity item:source.getAllEntities()) {
         if(item instanceof Folder || item instanceof BuiltInIntegrationEntity)continue;
         IntegrationEntity match=target.getItemLookup().getEntity(item.getID());
         if(match!=null && match.getEntityType()!=item.getEntityType())throw new IllegalArgumentException("Conflicting object ID: "+item.getName());
         if(match!=null && item.getEntityType()==EntityType.ProjectVariable && !match.getName().equals(item.getName()))match=null;
         if(match==null) {
            List<IntegrationEntity> names=new ArrayList<>();
            for(IntegrationEntity candidate:existing)if(candidate.getEntityType()==item.getEntityType() && candidate.getName().equals(item.getName())) {
               if(path(candidate).equals(path(item))) { match=candidate; break; }
               names.add(candidate);
            }
            if(match==null && names.size()==1)match=names.get(0);
            else if(match==null && names.size()>1)throw new IllegalArgumentException("Ambiguous destination object: "+path(item));
         }
         if(match!=null && !claimed.add(match.getID()))throw new IllegalArgumentException("Two source objects match "+path(match));
         String action=match==null?"Create":preserve(item)?"Preserve":"Replace";
         plan.entries.add(new Entry(item,match,action));
         if(match==null)plan.created++;else if(preserve(item))plan.kept++;else plan.updated++;
      }
      return plan;
   }
   public static Plan merge(ManagedProject source, ManagedProject destination) throws Exception {
      IntegrationProject target=destination.getProject();
      Plan plan=plan(source.getProject(),target);
      final Map<IntegrationEntityId,IntegrationEntity> replacements=new HashMap<>();
      final Map<IntegrationEntityId,IntegrationEntity> parents=new HashMap<>();
      for(RootFolder root:source.getProject().getAllRootFolders())parents.put(root.getID(),target.getRootFolder(root.getItemType()));
      for(IntegrationEntity item:source.getProject().getAllEntities())if(item instanceof Folder)parent(item,target,parents);
      for(Entry entry:plan.entries) {
         IntegrationEntity result=entry.destination;
         if(result==null) {
            result=target.getItemLookup().getEntity(entry.source.getID())==null?entry.source.clone():entry.source.cloneWithNewID();result.setTransient(false);result.setDeployed(false);result.setDeployDirty(true);
            target.insertEntity(result,parent(entry.source.getParent(),target,parents));
         }
         replacements.put(entry.source.getID(),result);
      }
      ReferenceReplacementService service=new ReferenceReplacementService() {
         public IntegrationEntity getReplacement(IntegrationEntityId id) {
            IntegrationEntity replacement=replacements.get(id);
            if(replacement!=null)return replacement;
            IntegrationEntity original=source.getProject().getItemLookup().getEntity(id);
            if(original==null && id!=null)throw new IllegalArgumentException("Source contains an unresolved object reference: "+id);
            return original;
         }
         public <T extends IntegrationEntity>T getReplacement(IntegrationEntityId id,Class<T> type) { return type.cast(getReplacement(id)); }
         public IntegrationEntityLookup getItemLookup() { return new IntegrationEntityLookup() {
            public IntegrationEntity getEntity(IntegrationEntityId id){IntegrationEntity item=source.getProject().getItemLookup().getEntity(id);return item!=null?item:target.getItemLookup().getEntity(id);}
            public <T extends IntegrationEntity>T getEntity(IntegrationEntityId id,Class<T> type){return type.cast(getEntity(id));}
         }; }
      };
      copyFiles(source.getFileStore(),destination.getFileStore(),source.getFileStore().getRoot());
      Map<IntegrationEntityId,IntegrationEntity> copies=new HashMap<>();
      for(Entry entry:plan.entries) {
         if(entry.action.equals("Preserve"))continue;
         IntegrationEntity result=replacements.get(entry.source.getID());
         IntegrationEntity copy=entry.source.copyWithIdentity(result.getID());
         if(copy instanceof Operation)((Operation)copy).setPipeline(new org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline(((Operation)entry.source).getPipeline()));
         copy.setTransient(false);copy.setDeployed(false);copy.setDeployDirty(true);
         if(entry.destination!=null && !EntityUtils.isNameUnique(result,copy.getName()))throw new IllegalArgumentException("Cannot replace object because its new name conflicts: "+copy.getName());
         result.importFrom(copy);result.setDeployDirty(true);
         copies.put(entry.source.getID(),copy);
      }
      for(Entry entry:plan.entries) {
         if(entry.action.equals("Preserve"))continue;
         IntegrationEntity result=replacements.get(entry.source.getID());
         IntegrationEntity copy=copies.get(entry.source.getID());
         copy.replaceReferences(service);result.importFrom(copy);result.setDeployDirty(true);
         source.getEntityResources().exportAs(entry.source,result,destination.getEntityResources());
         org.etlforge.integration.client.entity.settings.FileResource layout=destination.getEntityResources().getFileResource(result,"GraphLayout.xml");
         if(result instanceof Operation && layout.exists()) {
            String text=layout.read();
            for(Map.Entry<IntegrationEntityId,IntegrationEntity> mapping:replacements.entrySet())text=text.replace(mapping.getKey().toString(),mapping.getValue().getID().toString());
            layout.write(text);
         }
         destination.getPipelinePlugins().applyNewPluginSettings(result,Arrays.asList(source.getPipelinePlugins().getAppliedPlugins().getAllAssociations(entry.source)),plugin->source.getPipelinePlugins().getManifestCache());
      }
      target.refreshItemLookup();
      for(Entry entry:plan.entries)if(!entry.action.equals("Preserve"))replacements.get(entry.source.getID()).doPostImportProcessing(target.getItemLookup());
      destination.rebuildDependencies();destination.saveToDisk();
      return plan;
   }
   private static IntegrationEntity parent(IntegrationEntity source,IntegrationProject target,Map<IntegrationEntityId,IntegrationEntity> parents) {
      IntegrationEntity found=parents.get(source.getID());if(found!=null)return found;
      if(!(source instanceof Folder))throw new IllegalArgumentException("Unsupported parent "+source.getName());
      Folder enclosing=(Folder)parent(source.getParent(),target,parents);
      IntegrationEntity identified=target.getItemLookup().getEntity(source.getID());
      Folder folder=identified instanceof Folder?(Folder)identified:enclosing.findSubFolder(source.getName());
      if(identified!=null && !(identified instanceof Folder))throw new IllegalArgumentException("Folder ID conflicts with an object: "+source.getName());
      if(folder==null){folder=(Folder)source.clone();target.insertEntity(folder,enclosing);}
      parents.put(source.getID(),folder);return folder;
   }
   private static void copyFiles(FileStore source,FileStore target,FileStoreFile file)throws Exception {
      if(file.getPhysicalRepresentation().isFile()) {
         target.addFile(file.getPhysicalRepresentation(),file.getPath(),FileCollisionResolver.REPLACE_EXISTING);
         for(FileStoreFile dependency:source.getRequiredFiles(file.getPath()))target.addDependency(file.getPath(),dependency.getPath());
      }
      for(Iterator<FileStoreFile> children=file.getChildren();children.hasNext();)copyFiles(source,target,children.next());
   }
   public static Result migrate(ProjectManager manager,ManagedProject source,File projectFile)throws Exception {
      Path target=projectFile.toPath().toRealPath().getParent();
      Path original=source.getLocation().toPath().toRealPath();
      if(target.equals(original) || target.startsWith(original) || original.startsWith(target))throw new IllegalArgumentException("Source and destination project folders must be separate.");
      if(!projectFile.getName().endsWith(".jip"))throw new IllegalArgumentException("Select a Studio project (.jip) file.");
      String name=projectFile.getName().substring(0,projectFile.getName().length()-4);
      ProjectFileLock lock=new ProjectFileLock();File lockedFolder=target.toFile();
      if(!lock.acquire(lockedFolder))throw new IllegalStateException("The destination project is open. Close it before migrating.");
      Path work=null;
      Path backup=target.resolveSibling(target.getFileName()+"-before-migration-"+System.currentTimeMillis());
      ManagedProject staged=null;boolean committed=false;
      IntegrationProjectLock.acquire();
      try {
         work=Files.createTempDirectory(target.getParent(),".etlforge-migrate-");
         copyTree(target,backup);copyTree(target,work);
         IntegrationProject data=manager.loadProject(name,work.toFile(),ProjectLoadingIssuesHandler.IGNORE);
         staged=manager.getProject(data);Plan plan=merge(source,staged);manager.closeProject(staged);staged=null;
         final Path prepared=work;
         commit(backup,target,()->{copyTree(prepared,target);return null;});committed=true;
         return new Result(plan,backup);
      } finally {
         try {if(staged!=null)manager.closeProject(staged);}finally {IntegrationProjectLock.release();lock.release(lockedFolder);if(committed && work!=null)try{removeTree(work);}catch(IOException ignored){}}
      }
   }
   private static void commit(Path backup,Path target,java.util.concurrent.Callable<Void> action)throws Exception {
      try {action.call();}
      catch(Exception failure) {try{restoreTree(backup,target);}catch(Exception rollback){failure.addSuppressed(rollback);}throw failure;}
   }
   private static void copyTree(Path from,Path to)throws IOException {
      Files.walkFileTree(from,new SimpleFileVisitor<Path>() {
         public FileVisitResult preVisitDirectory(Path path,BasicFileAttributes attributes)throws IOException {
            if(attributes.isSymbolicLink() || Files.isSymbolicLink(path) || Files.readAttributes(path,BasicFileAttributes.class,LinkOption.NOFOLLOW_LINKS).isOther())throw new IOException("Project folders must not contain links: "+path);
            Files.createDirectories(to.resolve(from.relativize(path)));return FileVisitResult.CONTINUE;
         }
         public FileVisitResult visitFile(Path path,BasicFileAttributes attributes)throws IOException {
            if(path.getFileName().toString().equals(".project.lock"))return FileVisitResult.CONTINUE;
            if(!attributes.isRegularFile() || attributes.isSymbolicLink())throw new IOException("Project contains a link or unsupported file: "+path);
            Path destination=to.resolve(from.relativize(path));
            if(Files.isSymbolicLink(destination))throw new IOException("Destination file is a link: "+destination);
            Files.copy(path,destination,StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.COPY_ATTRIBUTES);return FileVisitResult.CONTINUE;
         }
      });
   }
   private static void restoreTree(Path backup,Path target)throws IOException {
      try(java.util.stream.Stream<Path> paths=Files.walk(target)) {
         for(Path path:(Iterable<Path>)paths.sorted(Comparator.reverseOrder())::iterator) {
            if(path.equals(target) || path.getFileName().toString().equals(".project.lock"))continue;
            if(!Files.exists(backup.resolve(target.relativize(path)),LinkOption.NOFOLLOW_LINKS))Files.delete(path);
         }
      }
      copyTree(backup,target);
   }
   private static void removeTree(Path root)throws IOException {
      Path resolved=root.toAbsolutePath().normalize();
      if(!resolved.getFileName().toString().startsWith(".etlforge-migrate-"))throw new IOException("Unexpected migration staging path");
      try(java.util.stream.Stream<Path> paths=Files.walk(resolved)) {for(Path path:(Iterable<Path>)paths.sorted(Comparator.reverseOrder())::iterator)Files.delete(path);}
   }
}
