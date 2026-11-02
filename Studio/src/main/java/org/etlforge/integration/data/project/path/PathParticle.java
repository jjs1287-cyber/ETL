package org.etlforge.integration.data.project.path;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.string.StringMatcherPredicate;
import org.etlforge.string.StringMatchers;

final class PathParticle {
   private final Predicate<String> matcher;
   private final PathParticle.Type type;
   private final boolean hasWildcard;

   public PathParticle(String var1, PathParticle.Type var2) {
      this.type = var2;
      this.hasWildcard = Wildcards.hasWildcard(var1);
      if (this.hasWildcard) {
         this.matcher = Wildcards.isAnyLevel(var1) ? null : Wildcards.parse(var1);
      } else {
         this.matcher = new StringMatcherPredicate(StringMatchers.EQUALS_IGNORE_CASE, var1);
      }
   }

   public boolean isFolder() {
      return this.type == PathParticle.Type.ROOT || this.type == PathParticle.Type.FOLDER;
   }

   public boolean hasWildcard() {
      return this.hasWildcard;
   }

   public boolean matches(IntegrationEntity var1) {
      return this.type.validate(var1) && this.matches(var1.getName());
   }

   public boolean matches(String var1) {
      return this.matcher == null || this.matcher.apply(var1);
   }

   public IntegrationEntity findMatchingChild(IntegrationEntity var1) {
      Set var2 = this.findMatchingChildren(var1);
      return var2.isEmpty() ? null : (IntegrationEntity)var2.iterator().next();
   }

   public Set<IntegrationEntity> findMatchingChildren(IntegrationEntity var1) {
      HashSet var2 = Sets.newHashSet();
      if (this.matcher == null) {
         this.handleAnyLevelWildcard(var1, var2);
      } else {
         for (IntegrationEntity var4 : var1.getAllChildren()) {
            if (this.matches(var4)) {
               var2.add(var4);
            }
         }
      }

      return var2;
   }

   private void handleAnyLevelWildcard(IntegrationEntity var1, Set<IntegrationEntity> var2) {
      if (var1 instanceof IntegrationProject) {
         for (RootFolder var4 : ((IntegrationProject)var1).getAllRootFolders()) {
            var2.add(var4);
            var2.addAll(var4.getAllItemsRecursively(Folder.class));
         }
      } else if (var1 instanceof Folder) {
         var2.addAll(((Folder)var1).getAllItemsRecursively(Folder.class));
      }
   }

   public enum Type {
      ROOT,
      FOLDER,
      FOLDER_OR_LEAF;

      public boolean validate(IntegrationEntity var1) {
         EntityType var2 = var1.getEntityType();
         switch (this) {
            case ROOT:
               return var2 == EntityType.RootFolder;
            case FOLDER:
               return var2 == EntityType.Folder;
            case FOLDER_OR_LEAF:
               return var2 != EntityType.Project && var2 != EntityType.RootFolder;
            default:
               throw new AssertionError("Unexpected Type: " + this);
         }
      }
   }
}
