package org.etlforge.integration.client.project.persistor.v20;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.client.project.ProjectLoadingIssue;
import org.etlforge.integration.data.entity.EntityDescriptor;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.util.persist.Persistor;

final class ProjectStructurePersistor {
   private static final String ENTITY_TYPE = "EntityType";
   private static final String ENTITY = "Entity";
   private static final String NAME = "name";
   private static final String FOLDER = "Folder";
   private static final String ENTITY_ID = "entityId";

   public static void save(IntegrationProject var0, Persistor var1) {
      var1.putString("name", var0.getName());
      var1.putString("projectId", var0.getID().toString());

      for (RootFolder var3 : var0.getAllRootFolders()) {
         if (isSaveable(var3)) {
            save(var3, var1);
         }
      }
   }

   private static void save(RootFolder var0, Persistor var1) {
      Persistor var2 = var1.createChild("EntityType");
      var2.putString("name", var0.getItemType().name());

      for (IntegrationEntity var4 : var0.getAllChildren()) {
         save(var4, var2);
      }
   }

   private static void save(IntegrationEntity var0, Persistor var1) {
      if (var0 instanceof Folder) {
         Persistor var2 = var1.createChild("Folder");
         var2.putString("entityId", var0.getID().toString());
         var2.putString("name", var0.getName());

         for (IntegrationEntity var4 : var0.getAllChildren()) {
            save(var4, var2);
         }
      } else if (!var0.isTransient()) {
         Persistor var5 = var1.createChild("Entity");
         var5.putString("entityId", var0.getID().toString());
         var5.putString("name", var0.getName());
      }
   }

   private static boolean isSaveable(RootFolder var0) {
      return var0.getNumberOfChildren() > 0;
   }

   public static Map<EntityType, ProjectStructurePersistor.Node> load(Persistor var0, Set<ProjectLoadingIssue> var1) {
      HashMap var2 = Maps.newHashMap();

      for (Persistor var4 : var0.getChildren("EntityType")) {
         ProjectStructurePersistor.Node var5 = new ProjectStructurePersistor.Node(null, null, null);
         String var6 = var4.getString("name");
         EntityType var7 = null;

         try {
            var7 = EntityType.valueOf(var6);
         } catch (Exception var9) {
         }

         if (var7 != null) {
            buildTree(var7, var5, var4);
            var2.put(var7, var5);
         } else {
            collectEntitiesOfUnknownType(var6, var4, var1);
         }
      }

      return var2;
   }

   private static void buildTree(EntityType var0, ProjectStructurePersistor.Node var1, Persistor var2) {
      for (Persistor var4 : var2.getAllChildren()) {
         String var5 = var4.getString("entityId");
         String var6 = var4.getString("name");
         if ("Folder".equals(var4.getName())) {
            ProjectStructurePersistor.Node var7 = new ProjectStructurePersistor.Node(var1, new EntityDescriptor(EntityType.Folder, var5), var6);
            buildTree(var0, var7, var4);
         } else {
            new ProjectStructurePersistor.Node(var1, new EntityDescriptor(var0, var5), var6);
         }
      }
   }

   private static void collectEntitiesOfUnknownType(String var0, Persistor var1, Set<ProjectLoadingIssue> var2) {
      for (Persistor var4 : var1.getAllChildren()) {
         if ("Folder".equals(var4.getName())) {
            collectEntitiesOfUnknownType(var0, var4, var2);
         } else {
            ProjectLoadingIssue var5 = ProjectLoadingIssue.warning("Unknown item type: " + var0);
            var5.setEntityInfo(var4.getString("name"), null);
            var2.add(var5);
         }
      }
   }

   private ProjectStructurePersistor() {
   }

   static final class Node {
      private final EntityDescriptor entity;
      private final List<ProjectStructurePersistor.Node> children;
      private final String name;

      private Node(ProjectStructurePersistor.Node var1, EntityDescriptor var2, String var3) {
         this.entity = var2;
         this.name = var3;
         this.children = Lists.newArrayList();
         if (var1 != null) {
            var1.children.add(this);
         }
      }

      public EntityDescriptor getEntity() {
         return this.entity;
      }

      public Iterable<ProjectStructurePersistor.Node> children() {
         return this.children;
      }

      public String getName() {
         return this.name;
      }
   }
}
