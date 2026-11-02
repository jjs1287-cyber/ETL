package org.etlforge.integration.data.project.path;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;

final class ProjectPath {
   private final ImmutableList<PathParticle> particles;

   public ProjectPath(String var1) {
      this.particles = new ProjectPath.Builder(var1).get();
   }

   public boolean isPathToFolder() {
      return !this.particles.isEmpty() && this.lastParticle().isFolder();
   }

   private PathParticle lastParticle() {
      return this.particles.get(this.particles.size() - 1);
   }

   public boolean hasWildcards() {
      for (PathParticle var2 : this.particles) {
         if (var2.hasWildcard()) {
            return true;
         }
      }

      return false;
   }

   public Optional<IntegrationEntity> search(IntegrationProject var1) {
      return this.particles.isEmpty() ? Optional.absent() : this.searchDown(var1, 0);
   }

   private Optional<IntegrationEntity> searchDown(IntegrationEntity var1, int var2) {
      PathParticle var3 = this.particles.get(var2);
      IntegrationEntity var4 = var3.findMatchingChild(var1);
      if (var4 == null) {
         return Optional.absent();
      } else {
         return var2 == this.particles.size() - 1 ? Optional.of(var4) : this.searchDown(var4, var2 + 1);
      }
   }

   public Set<IntegrationEntity> searchForItems(IntegrationProject var1) {
      return this.particles.isEmpty() ? new HashSet<>() : this.searchDownForItems(var1, 0);
   }

   private Set<IntegrationEntity> searchDownForItems(IntegrationEntity var1, int var2) {
      PathParticle var3 = this.particles.get(var2);
      Set var4 = var3.findMatchingChildren(var1);
      if (var2 == this.particles.size() - 1) {
         return var4;
      }

      HashSet var5 = Sets.newHashSet();

      for (Object __etlItem68 : var4) {
         IntegrationEntity var7 = (IntegrationEntity) __etlItem68;
         var5.addAll(this.searchDownForItems(var7, var2 + 1));
      }

      return var5;
   }

   public static String toPathString(IntegrationEntity var0) {
      boolean var1 = var0 instanceof Folder;
      LinkedList var2 = Lists.newLinkedList();

      while (var0 != null && !(var0 instanceof IntegrationProject)) {
         String var3 = var0.getName();
         var3 = var3.replaceAll("\\\\", "\\\\\\\\").replaceAll("/", "\\\\/").replaceAll("\\*", "\\\\*").replaceAll("\\+", "\\\\+");
         var2.addFirst(var3);
         var0 = var0.getParent();
      }

      String var5 = StringUtils.join(var2, "/");
      if (var1) {
         var5 = var5 + "/";
      }

      return var5;
   }

   private static class Builder {
      private final List<PathParticle> particles = Lists.newArrayList();
      private boolean lastParticleIsFolder;

      public Builder(String var1) {
         String[] var2 = tokenizePath(var1);
         if (var2.length != 0) {
            int var3 = var2.length - 1;
            this.lastParticleIsFolder = var2[var3].isEmpty();

            for (int var4 = 0; var4 < var2.length; var4++) {
               boolean var5 = var4 == 0;
               boolean var6 = var4 == var3;
               this.processPart(var2[var4], var5, var6);
            }
         }
      }

      private static String[] tokenizePath(String var0) {
         List var1 = new ProjectPathTokenizer().tokenize(var0);
         return (String[]) ignoreLeadingEmptyToken(var1).toArray(new String[var1.size()]);
      }

      private static List<String> ignoreLeadingEmptyToken(List<String> var0) {
         if (!var0.isEmpty() && ((String)var0.get(0)).isEmpty()) {
            var0.remove(0);
         }

         return var0;
      }

      private void processPart(String var1, boolean var2, boolean var3) {
         if (var1.isEmpty()) {
            assert var3;
         } else {
            PathParticle.Type var4 = this.getType(var2, var3);
            this.particles.add(new PathParticle(var1, var4));
         }
      }

      private PathParticle.Type getType(boolean var1, boolean var2) {
         if (var1) {
            return PathParticle.Type.ROOT;
         }

         boolean var3 = var2 && !this.lastParticleIsFolder;
         return var3 ? PathParticle.Type.FOLDER_OR_LEAF : PathParticle.Type.FOLDER;
      }

      public ImmutableList<PathParticle> get() {
         return ImmutableList.copyOf(this.particles);
      }
   }
}
