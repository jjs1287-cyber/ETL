package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.structure.mapping.InvalidMapping;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.NodePath;

final class InvalidMappingsManager {
   private final TreeMapper treeMapper;
   private Map<NodePath, InvalidMapping> invalidMappings;
   private final List<InvalidMappingsListener> invalidMappingsListener;

   public InvalidMappingsManager(TreeMapper var1) {
      this.treeMapper = var1;
      this.invalidMappings = this.createInvalidMappingHolder();
      this.invalidMappingsListener = new CopyOnWriteArrayList<>();
   }

   public InvalidMapping[] getInvalidMappings() {
      return this.invalidMappings.values().toArray(new InvalidMapping[this.invalidMappings.size()]);
   }

   public void addInvalidMappingsListener(InvalidMappingsListener var1) {
      this.invalidMappingsListener.add(var1);
   }

   public void removeInvalidMappingsListener(InvalidMappingsListener var1) {
      this.invalidMappingsListener.remove(var1);
   }

   public void addInvalidMapping(InvalidMapping var1) {
      NodePath var2 = var1.getTargetPath();
      InvalidMapping var3 = this.invalidMappings.get(var2);
      boolean var4 = var3 == null || !var3.equals(var1);
      this.invalidMappings.put(var2, var1);
      if (var4) {
         this.fireInvalidMappingsChanged();
      }
   }

   public void setInvalidMappings(List<InvalidMapping> var1) {
      HashSet var2 = Sets.newHashSet(this.invalidMappings.values());
      this.invalidMappings = this.createInvalidMappingHolder();
      if (var1 != null) {
         for (InvalidMapping var4 : var1) {
            this.invalidMappings.put(var4.getTargetPath(), var4);
         }
      }

      HashSet var5 = Sets.newHashSet(this.invalidMappings.values());
      if (!var2.equals(var5)) {
         this.fireInvalidMappingsChanged();
      }
   }

   public void deleteInvalidMapping(Mapping var1) {
      if (this.invalidMappings.remove(var1.getTargetPath()) != null) {
         this.fireInvalidMappingsChanged();
      }
   }

   public void deleteInvalidMappings(Collection<NodePath> var1) {
      if (!var1.isEmpty()) {
         boolean var2 = false;

         for (NodePath var4 : var1) {
            if (this.invalidMappings.remove(var4) != null) {
               var2 = true;
            }
         }

         if (var2) {
            this.fireInvalidMappingsChanged();
         }
      }
   }

   public void setConflictMappings(Set<InvalidMapping> var1) {
      HashMap var2 = new HashMap();

      for (InvalidMapping var4 : var1) {
         var2.put(var4.getTargetPath(), var4);
      }

      for (NodePath var5 : Sets.newHashSet(this.invalidMappings.keySet())) {
         if (var5.path.startsWith("[") && var2.get(var5) == null) {
            this.invalidMappings.remove(var5);
         }
      }

      for (InvalidMapping var9 : var1) {
         this.invalidMappings.put(var9.getTargetPath(), var9);
      }

      this.fireInvalidMappingsChanged();
   }

   private void fireInvalidMappingsChanged() {
      InvalidMapping[] var1 = this.getInvalidMappings();

      for (InvalidMappingsListener var3 : this.invalidMappingsListener) {
         var3.invalidMappingsChanged(this.treeMapper, var1);
      }
   }

   private Map<NodePath, InvalidMapping> createInvalidMappingHolder() {
      return Maps.newLinkedHashMap();
   }

   public InvalidMapping getInvalidMapping(Node var1) {
      return this.invalidMappings.get(new NodePath(var1.m_deName));
   }

   public Set<InvalidMapping> getInvalidMappingsAsSet() {
      return Sets.newHashSet(this.invalidMappings.values());
   }
}
