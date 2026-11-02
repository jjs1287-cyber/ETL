package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.data.entity.transformation.AutoMapper;

final class MappingTreeAutoMapper {
   private final TreeMapper treeMapper;
   private final Node sourceFolder;
   private final Node targetFolder;
   private final AutoMapper<Node, Node> worker;
   private boolean mappingAdded;

   public MappingTreeAutoMapper(TreeMapper var1, Node var2, Node var3) {
      this.treeMapper = var1;
      this.sourceFolder = var2;
      this.targetFolder = var3;
      MappingTreeAutoMapper.NameFunction var4 = new MappingTreeAutoMapper.NameFunction();
      this.worker = new AutoMapper<>(var4, var4);
   }

   public void run() {
      this.mappingAdded = false;
      if (this.sourceFolder.isFolder() && this.targetFolder.isFolder()) {
         this.expand();
         this.matchFields();
         this.matchFoldersAndRecurse();
      }
   }

   private void expand() {
      if (!this.sourceFolder.hasChildren() && !this.sourceFolder.isRecursive()) {
         this.sourceFolder.expandNode();
      }

      if (!this.targetFolder.hasChildren()) {
         this.targetFolder.expandNode();
      }
   }

   private void matchFields() {
      List var1 = this.getAvailableSourceFields();
      List var2 = this.getAvailableTargetFields();

      for (Object __etlItem48 : this.worker.findMatches(var1, var2)) {
         AutoMapper.Match var5 = (AutoMapper.Match) __etlItem48;
         this.map(var5);
      }
   }

   private List<Node> getAvailableSourceFields() {
      ArrayList var1 = Lists.newArrayList();

      for (Node var3 : this.sourceFolder.getChildren()) {
         if (!var3.isFolder() && !var3.isHidden()) {
            var1.add(var3);
         }
      }

      return var1;
   }

   private List<Node> getAvailableTargetFields() {
      ArrayList var1 = Lists.newArrayList();

      for (Node var3 : this.targetFolder.getChildren()) {
         if (!var3.isHidden()
            && !var3.isPrecondition()
            && MappableStatus.of(var3).isMappable()
            && !var3.isNonAutoMappableSfdcField()
            && !this.isAlreadyMapped(var3)) {
            var1.add(var3);
         }
      }

      return var1;
   }

   private boolean isAlreadyMapped(Node var1) {
      String var2 = var1.m_deName;
      return var2.isEmpty() ? true : this.treeMapper.isMapped(var2);
   }

   private void map(AutoMapper.Match<Node, Node> var1) {
      boolean var2 = this.treeMapper.addMapPair((Node)var1.source, (Node)var1.target);
      if (var2) {
         this.mappingAdded = true;
      }
   }

   private void matchFoldersAndRecurse() {
      List var1 = getFolders(this.sourceFolder);
      List var2 = getFolders(this.targetFolder);

      for (Object __etlItem97 : this.worker.findMatches(var1, var2)) {
         AutoMapper.Match var5 = (AutoMapper.Match) __etlItem97;
         this.recurse(var5);
      }
   }

   private static List<Node> getFolders(Node var0) {
      ArrayList var1 = Lists.newArrayList();

      for (Node var3 : var0.getChildren()) {
         if (var3.isFolder() && !var3.isHidden()) {
            var1.add(var3);
         }
      }

      return var1;
   }

   private void recurse(AutoMapper.Match<Node, Node> var1) {
      MappingTreeAutoMapper var2 = new MappingTreeAutoMapper(this.treeMapper, (Node)var1.source, (Node)var1.target);
      var2.run();
      if (var2.wasAtLeastOneMappingAdded()) {
         this.mappingAdded = true;
      }
   }

   public boolean wasAtLeastOneMappingAdded() {
      return this.mappingAdded;
   }

   private static class NameFunction implements Function<Node, String> {
      private NameFunction() {
      }

      public String apply(Node var1) {
         return var1.getMappingName();
      }
   }
}
