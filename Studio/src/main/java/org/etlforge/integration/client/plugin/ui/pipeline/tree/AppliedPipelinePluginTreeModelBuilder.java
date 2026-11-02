package org.etlforge.integration.client.plugin.ui.pipeline.tree;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePlugin;
import org.etlforge.integration.client.plugin.pipeline.MutableAppliedPipelinePluginStore;
import org.etlforge.integration.client.plugin.pipeline.ProjectPluginInfo;
import org.etlforge.integration.client.plugin.ui.resources.PluginUiResources;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeModel;

public final class AppliedPipelinePluginTreeModelBuilder {
   private ProjectPluginInfo projectPlugins;
   private String rootName;
   private Set<AppliedPipelinePlugin> plugins;

   public AppliedPipelinePluginTreeModelBuilder(ProjectPluginInfo var1) {
      this.projectPlugins = var1;
      this.plugins = Sets.newHashSet();
   }

   public void setProjectPlugins(ProjectPluginInfo var1) {
      this.projectPlugins = var1;
   }

   public void setRootName(String var1) {
      this.rootName = var1;
   }

   private String getRootName() {
      return this.rootName != null ? this.rootName : PluginUiResources.getString("AppliedPipelinePluginTree.Root");
   }

   public Set<AppliedPipelinePlugin> getPlugins() {
      return Sets.newHashSet(this.plugins);
   }

   public boolean isEmpty() {
      return this.plugins.isEmpty();
   }

   public KongaTreeModel buildNewModel() {
      if (this.projectPlugins != null) {
         DefaultKongaTreeNode var3 = new DefaultKongaTreeNode(null, this.getRootName(), true);
         Map var2 = this.buildDataMap();
         if (var2.isEmpty()) {
            new DefaultKongaTreeNode(var3, PluginUiResources.getString("AppliedPipelinePluginTree.Empty"), false);
         } else {
            this.populateModel(var3, var2);
         }

         return new DefaultKongaTreeModel(var3);
      } else {
         DefaultKongaTreeNode var1 = new DefaultKongaTreeNode(PluginUiResources.getString("AppliedPipelinePluginTree.NoProject"), false);
         return new DefaultKongaTreeModel(var1);
      }
   }

   private void populateModel(DefaultKongaTreeNode var1, Map<AppliedPipelinePluginTreeModelBuilder.PluginKey, SortedSet<IntegrationEntity>> var2) {
      for (Entry var4 : var2.entrySet()) {
         PipelinePluginTreeNode var5 = new PipelinePluginTreeNode(var1, ((AppliedPipelinePluginTreeModelBuilder.PluginKey)var4.getKey()).plugin);

         for (Object __etlItem74 : (SortedSet)var4.getValue()) {
            IntegrationEntity var7 = (IntegrationEntity) __etlItem74;
            var5.addChild(new IntegrationEntityTreeNode(var7));
         }
      }
   }

   private Map<AppliedPipelinePluginTreeModelBuilder.PluginKey, SortedSet<IntegrationEntity>> buildDataMap() {
      final TreeMap var1 = Maps.newTreeMap();
      final MutableAppliedPipelinePluginStore var2 = this.projectPlugins.getAppliedPlugins();
      this.plugins = Sets.newHashSet();
      this.projectPlugins.getProject().visitAllEntities(new Receiver<IntegrationEntity>() {
         public void handle(IntegrationEntity var1x) {
            for (AppliedPipelinePlugin var5 : var2.getAllAssociations(var1x)) {
               AppliedPipelinePluginTreeModelBuilder.PluginKey var6 = new AppliedPipelinePluginTreeModelBuilder.PluginKey(var5);
               AppliedPipelinePluginTreeModelBuilder.this.plugins.add(var5);
               if (var1.containsKey(var6)) {
                  ((SortedSet)var1.get(var6)).add(var1x);
               } else {
                  TreeSet var7 = Sets.newTreeSet(new EntityTypeAndNameSorter());
                  var7.add(var1x);
                  var1.put(var6, var7);
               }
            }
         }
      });
      return var1;
   }

   private static class PluginKey implements Comparable<AppliedPipelinePluginTreeModelBuilder.PluginKey> {
      public final AppliedPipelinePlugin plugin;

      public PluginKey(AppliedPipelinePlugin var1) {
         this.plugin = var1;
      }

      @Override
      public boolean equals(Object var1) {
         return var1 == this
            ? true
            : var1 instanceof AppliedPipelinePluginTreeModelBuilder.PluginKey
               && this.plugin.getPluginId().equals(((AppliedPipelinePluginTreeModelBuilder.PluginKey)var1).plugin.getPluginId());
      }

      @Override
      public int hashCode() {
         return this.plugin.getPluginId().hashCode();
      }

      public int compareTo(AppliedPipelinePluginTreeModelBuilder.PluginKey var1) {
         return this.plugin.getDisplayName().compareToIgnoreCase(var1.plugin.getDisplayName());
      }
   }
}
