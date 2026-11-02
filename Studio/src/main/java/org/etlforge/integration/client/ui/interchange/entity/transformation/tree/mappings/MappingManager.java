package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.SourceTreeDataElementContainer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperException;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperSourceTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.grammar.CheckMapping;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.LdapUtils;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.data.entity.transformation.TransformationVariables;
import org.etlforge.integration.structure.mapping.InvalidMapping;
import org.etlforge.integration.structure.mapping.MapStringFormat;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.NodePath;

public final class MappingManager {
   private final TreeMapper treeMapper;
   private final TreeComponent targetTreeComponent;
   private boolean mapFileRead;
   private final MappingManager.MappingMap mappings;
   private final CheckMapping checkMapping;
   private final InvalidMappingsManager invalidMappingsMgr;
   private boolean bNoSourceDataUsedInMappings;
   private boolean bChecked;

   public MappingManager(TreeMapper var1) {
      this.treeMapper = var1;
      this.mappings = new MappingManager.MappingMap();
      this.checkMapping = new CheckMapping(var1);
      this.targetTreeComponent = var1.m_targetTreeComponent;
      this.invalidMappingsMgr = new InvalidMappingsManager(var1);
   }

   public void addMapping(NodeMapping var1) {
      this.mappings.add(var1);
      Node var2 = this.treeMapper.findTargetNode(var1.getTargetPath());
      if (var2 != null && !var2.isRootLoopNode()) {
         var2.updateMappingKits(var1.getExpression());
         this.treeMapper.m_sourceTreeComponent.updateUnderlineNodes(var2);
         var2.setFormulaFlag(this.treeMapper.checkIfFormula());

         try {
            this.treeMapper.checkDataTypeWarningForOneToOneMapping(var2);
         } catch (TreeMapperException var4) {
            var4.printStackTrace();
         }
      }
   }

   public void changeHighLighted(NodeMapping var1) {
      try {
         Node var2 = null;
         if (var1 != null) {
            var2 = this.treeMapper.m_targetTreeComponent.findNode(var1.getTargetPath());
         }

         this.treeMapper.m_sourceTreeComponent.updateUnderlineNodes(var2);
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   public NodeMapping findMapping(String var1) {
      NodePath var2 = new NodePath(var1);

      for (NodeMapping var4 : this.mappings) {
         if (var4.getTargetPath().equals(var2)) {
            return var4;
         }
      }

      return null;
   }

   public Iterable<NodeMapping> getMappings() {
      return this.mappings;
   }

   public boolean hasMappings() {
      return !this.mappings.isEmpty();
   }

   public void reOrder() {
      this.mappings.reOrder();
   }

   public boolean deleteMapping(NodePath var1) {
      return new MappingManager.MappingDeleter().deleteMapping(var1);
   }

   public boolean deleteMapping(NodeMapping var1) {
      return new MappingManager.MappingDeleter().deleteMapping(var1);
   }

   public boolean deleteMappings(Collection<NodePath> var1) {
      return new MappingManager.MappingDeleter().deleteMappings(var1);
   }

   public void replaceMapping(String var1, String var2) {
      this.replaceMappingImpl(var1, var2, false);
   }

   public void replaceMappingSilently(String var1, String var2) {
      this.replaceMappingImpl(var1, var2, true);
   }

   private void replaceMappingImpl(String var1, String var2, boolean var3) {
      NodePath var4 = new NodePath(var2);
      NodeMapping var5 = this.mappings.get(var4);
      if (var5 == null) {
         var5 = this.treeMapper.getNodeMappingFactory().newMapping(var4, var1);
         this.addMapping(var5);
      } else {
         String var6 = var5.getExpression();
         var5.setNewExpression(var1);
         if (!var3 && !var6.equals(var1)) {
            this.treeMapper.setDirty(true);
         }
      }

      TransformationVariables var11 = this.treeMapper.getDataElements();
      var11.scanMapping(this.treeMapper.getTransformation(), var5);
      Node var7 = this.targetTreeComponent.findNode_expandTreeIfNotExist(var2);
      var7.setFormulaFlag(this.treeMapper.checkIfFormula());

      try {
         this.treeMapper.checkDataTypeWarningForOneToOneMapping(var7);
         var7.updateMappingKits(var1);
         this.treeMapper.m_sourceTreeComponent.updateUnderlineNodes(var7);
         this.updateRequireMappingFlag();
         this.changeHighLighted(var5);
         this.validateMapping(var5, new FirstInstanceInserter());
         this.invalidMappingsMgr.deleteInvalidMapping(var5);
      } catch (TreeMapperException var10) {
         InvalidMapping var9 = new InvalidMapping(var5, var10.getMessage());
         this.invalidMappingsMgr.addInvalidMapping(var9);
      }
   }

   public void reset(List<String> var1) throws TreeMapperException {
      this.treeMapper.m_sourceTreeComponent.clearSourcePathList();
      this.treeMapper.m_targetTreeComponent.clearSourcePathList();
      this.treeMapper.m_targetTreeComponent.clearCheckFlags();
      this.treeMapper.m_sourceTreeComponent.clearUsedFlags();
      this.loadMapList(var1);
      this.mapFileRead = true;
      this.validateMappings();
   }

   public void restore() {
      this.targetTreeComponent.clearSourcePaths();
      TreeMapperSourceTree var1 = this.treeMapper.m_sourceTreeComponent;
      this.treeMapper.m_transform.setSourceDataElementContainer(new SourceTreeDataElementContainer(var1));

      for (NodeMapping var3 : this.mappings) {
         if (this.treeMapper.m_transform.getRpnTokenVector(var3.getExpression()) != null) {
            String var4 = var3.getExpression();
            String var5 = this.treeMapper.m_transform.rmInstanceTag(var4);
            if (!var5.equals(var3.getExpression())) {
               var3.setNewExpression(var5);
               this.treeMapper.setDirty(true);
            }
         }
      }
   }

   public NodeMapping showMapping(String var1) {
      NodeMapping var2 = null;
      if (var1 != null) {
         var2 = this.mappings.get(new NodePath(var1));
      }

      if (this.mapFileRead) {
         this.changeHighLighted(var2);
      }

      return var2;
   }

   public void updateRequireMappingFlag() {
      this.treeMapper.m_targetTreeComponent.m_root.clearRequireMappingFlag();

      for (NodeMapping var2 : this.mappings) {
         Node var3 = this.treeMapper.m_targetTreeComponent.findNode(var2.getTargetPath());
         if (var3 != null && !var3.isFolder()) {
            var3.setRequireMappingFolders();
            var3.setMapped();
         }
      }

      this.treeMapper.m_targetTreeComponent.setLinkedColumnFlag("MappingManager.updateRequireMappingFlag");
      this.treeMapper.m_targetTreeComponent.m_root.updateRequireMappingFlag(new HashSet<>());
      this.treeMapper.m_targetTreeComponent.m_root.clearMappedHiddenTypes();
      this.treeMapper.m_sourceTreeComponent.m_root.clearMappedHiddenTypes();
   }

   public void validateMappings() throws TreeMapperException {
      ArrayList var1 = null;
      this.bNoSourceDataUsedInMappings = true;
      FirstInstanceInserter var2 = new FirstInstanceInserter();

      for (NodeMapping var4 : this.mappings) {
         try {
            this.validateMapping(var4, var2);
            if (this.bChecked && this.bNoSourceDataUsedInMappings && !this.checkMapping.noSourceDataUsed()) {
               this.bNoSourceDataUsedInMappings = false;
            }
         } catch (TreeMapperException var9) {
            if (var1 == null) {
               var1 = Lists.newArrayList();
            }

            var1.add(new InvalidMapping(var4, var9.getMessage()));
            this.bNoSourceDataUsedInMappings = false;
         }
      }

      try {
         this.treeMapper.m_targetTreeComponent.m_root.m_mappingKits.updateGenerator();
      } catch (Exception var8) {
         ErrorLog.log(this.getClass(), "updateGenerator() throw unexpected exception when calling validatingMappings()", var8);
         var8.printStackTrace();
      }

      InvalidMapping[] var10 = this.getInvalidMappings();

      for (InvalidMapping var7 : var10) {
         if (var1 == null) {
            var1 = Lists.newArrayList();
         }

         var1.add(var7);
      }

      this.invalidMappingsMgr.setInvalidMappings(var1);
      if (var1 != null) {
         StringBuilder var12 = new StringBuilder();

         for (Object __etlItem256 : var1) {
            InvalidMapping var14 = (InvalidMapping) __etlItem256;
            var12.append(var14.getError() + " ");
         }

         throw new TreeMapperException(var12.toString());
      }
   }

   public InvalidMapping[] getInvalidMappings() {
      return this.invalidMappingsMgr.getInvalidMappings();
   }

   public void setConflictMappings(Set<InvalidMapping> var1) {
      this.invalidMappingsMgr.setConflictMappings(var1);
   }

   public void addInvalidMappingsListener(InvalidMappingsListener var1) {
      this.invalidMappingsMgr.addInvalidMappingsListener(var1);
   }

   public void removeInvalidMappingsListener(InvalidMappingsListener var1) {
      this.invalidMappingsMgr.removeInvalidMappingsListener(var1);
   }

   private void validateMapping(NodeMapping var1, FirstInstanceInserter var2) throws TreeMapperException {
      String var3 = var1.getExpression();
      NodePath var4 = var1.getTargetPath();
      String var5 = var4.toString();
      this.bChecked = false;
      if (var5.charAt(0) != '[') {
         Node var6 = this.targetTreeComponent.findNode(var5);
         if (var6 == null && !var5.endsWith(":nil")) {
            throw new TreeMapperException("Internal error, map list contains a target \"" + var5 + "\" which is not in the target tree.");
         }

         String var7 = this.checkMapping.checkSource(var3);
         var2.setSourceDeList(this.checkMapping.sourceDeList);
         this.bChecked = true;
         if (!this.targetTreeComponent.m_flat && var6 != null && !var4.containsLoop() && this.checkMapping.checkLoopSourceToSingleTarget(var3)) {
            String var8 = var2.processExpression(var3);
            if (var8 != null) {
               var3 = var8;
               var1.setNewExpression(var3);
            }
         }

         if (var6 != null) {
            var6.setTargetMappedFlag(true);
            if (var6.isLoopNode()) {
               var6.setSourcePath(var3);
            }
         }

         if (var7 != null && !var7.equals("")) {
            throw new TreeMapperException("Error:\n" + var7);
         }
      }
   }

   private void loadMapList(List<String> var1) throws TreeMapperException {
      this.mappings.clear();
      ArrayList var2 = null;
      this.invalidMappingsMgr.setInvalidMappings(var2);
      if (var1 != null) {
         for (String var4 : var1) {
            Mapping var5 = MapStringFormat.parse(var4);

            try {
               this.readInTargets(var5);
            } catch (TreeMapperException var9) {
               if (var2 == null) {
                  var2 = Lists.newArrayList();
               }

               var2.add(new InvalidMapping(var5, var9.getMessage()));
            }
         }
      } else {
         for (NodeMapping var15 : KongaListUtils.asList(this.mappings)) {
            try {
               this.readInTargets(var15);
            } catch (TreeMapperException var8) {
               if (var2 == null) {
                  var2 = Lists.newArrayList();
               }

               var2.add(new InvalidMapping(var15, var8.getMessage()));
            }
         }
      }

      InvalidMapping[] var11 = this.getInvalidMappings();

      for (InvalidMapping var7 : var11) {
         if (var2 == null) {
            var2 = Lists.newArrayList();
         }

         var2.add(var7);
      }

      this.updateRequireMappingFlag();
      this.mapFileRead = true;
      this.treeMapper.m_sourceTreeComponent.updateUnderlineNodes(null);
      this.treeMapper.m_targetTreeComponent.updateUnderlineNodes(null);
      this.invalidMappingsMgr.setInvalidMappings(var2);
      if (var2 != null) {
         StringBuilder var14 = new StringBuilder();

         for (Object __etlItem365 : var2) {
            InvalidMapping var18 = (InvalidMapping) __etlItem365;
            var14.append(var18.getError() + " ");
         }

         throw new TreeMapperException(var14.toString());
      }
   }

   private void readInTargets(Mapping var1) throws TreeMapperException {
      NodeMapping var2 = this.treeMapper.getNodeMappingFactory().newMapping(var1);
      String var3 = var1.getTargetPath().toString();
      Node var4 = this.targetTreeComponent.findNode(var3);
      if (var4 == null) {
         String var5 = var3;
         if (var5.endsWith("xsi:nil")) {
            var5 = "[" + var5.substring(0, var5.length() - 7) + "]";
            Node var6 = this.targetTreeComponent.expandTreeAndLookForNode(var5);
            var4 = var6.createNilNodeToMap();
         }
      }

      if (var4 == null) {
         var4 = this.targetTreeComponent.expandTreeAndLookForNode(var3);
         if (var4 != null && LdapUtils.isLdapAttributeCommand(this.targetTreeComponent, var4)) {
            LdapUtils.setLdapAttributeType(this.targetTreeComponent, var4, var1.getExpression());
         }

         this.targetTreeComponent.setDirty();
      }

      if (var4 == null) {
         String var14 = var3;
         if (var14.endsWith("xsi:nil")) {
            var14 = var14.substring(0, var14.length() - 7);
            Node var18 = this.targetTreeComponent.findNode(var14);
            if (var18 == null) {
               throw new TreeMapperException(
                  "Internal Error: Cannot find the target node " + var3 + " in the target tree.\n You may avoid the problem by removing the mapping."
               );
            }

            var4 = var18.createNilNodeToMap();
         }
      }

      if (var4 == null) {
         throw new TreeMapperException("Cannot find the target node " + var3 + " in the target tree.");
      }

      if (var3.charAt(0) == '[') {
         String var16 = var1.getExpression();
         String var19 = "Fixed ";
         if (var16 != null && var16.startsWith(var19)) {
            String var7 = StringUtils.removeStart(var16, var19);
            Node var8 = this.treeMapper.m_sourceTreeComponent.expandTreeAndLookForNode(var7);
            if (var8 != null && var8.m_parent == null && var16.startsWith(var19) && var8.getTreeComponent().m_flat) {
               var8 = var8.getChildAt(0);
            } else if (var8 != null) {
               this.addMapping(var2);
               int var9 = var19.length();
               int var10 = var16.length();

               try {
                  var4.m_mappingKits.fixGeneratorPath(var16.substring(var9 + 1, var10 - 1));
               } catch (Exception var12) {
                  throw new TreeMapperException("Cannot define the fixed multiplicity for the target node " + var3 + ".");
               }
            }
         } else {
            var4.m_mappingKits.resetGenerator();
         }
      } else {
         String var17 = this.checkMapping.checkSource(var1.getExpression());
         this.addMapping(var2);
         var4.setTargetMappedFlag(true);
         this.treeMapper.updateMapAreaTables(var1);
         if (var4.isLoopNode()) {
            var4.setSourcePath(var1.getExpression());
         }

         if (var17 != null && !var17.equals("")) {
            throw new TreeMapperException("target: " + var3 + "\nsource: " + var1.getExpression() + "\nsource expression contains errors\n" + var17);
         }

         if (LdapUtils.isLdapClassEntryCommand(var4.getTreeComponent(), var4)) {
            var4.setCmd(var1.getExpression());
         }
      }
   }

   public InvalidMapping getInvalidMapping(Node var1) {
      return this.invalidMappingsMgr.getInvalidMapping(var1);
   }

   public boolean isInvalidMappingChanged(List<InvalidMapping> var1) {
      Set var2 = this.invalidMappingsMgr.getInvalidMappingsAsSet();
      HashSet var3 = Sets.newHashSet(var1);
      boolean var4 = var2.equals(var3);
      return !var4;
   }

   public boolean noSourceDataUsed() {
      return this.bNoSourceDataUsedInMappings;
   }

   private final class MappingDeleter {
      private MappingDeleter() {
      }

      public boolean deleteMapping(NodePath var1) {
         NodeMapping var2 = MappingManager.this.mappings.get(var1);
         return this.deleteMapping(var2);
      }

      public boolean deleteMapping(NodeMapping var1) {
         if (var1 == null) {
            return false;
         }

         MappingManager.this.invalidMappingsMgr.deleteInvalidMapping(var1);
         boolean var2 = this.removeMappingImpl(var1);
         if (var2) {
            MappingManager.this.treeMapper.setDirty(true);
         }

         return var2;
      }

      private boolean removeMappingImpl(NodeMapping var1) {
         if (MappingManager.this.mappings.remove(var1)) {
            this.removeMappingFromTreeNode(var1);
            this.removeMappingFromProjectVariables(var1);
            this.repaintMappingTree();
            return true;
         } else {
            return false;
         }
      }

      private void removeMappingFromTreeNode(NodeMapping var1) {
         Node var2 = MappingManager.this.treeMapper.findTargetNode(var1.getTargetPath());
         if (var2 != null) {
            var2.setTargetMappedFlag(false);
            var2.removeMapping();
            MappingManager.this.treeMapper.updateMapAreaTables(var2.m_deName, null);

            try {
               var2.updateMappingKits("");
            } catch (Exception var4) {
               var4.printStackTrace();
            }
         }
      }

      private void removeMappingFromProjectVariables(NodeMapping var1) {
         TransformationVariables var2 = MappingManager.this.treeMapper.getDataElements();
         var2.removeMapping(MappingManager.this.treeMapper.getTransformation(), var1);
      }

      private void repaintMappingTree() {
         MappingManager.this.treeMapper.m_sourceTreeComponent.updateUnderlineNodes(null);
         MappingManager.this.treeMapper.m_targetTreeComponent.repaint();
      }

      public boolean deleteMappings(Collection<NodePath> var1) {
         int var2 = MappingManager.this.mappings.size();

         for (NodePath var4 : var1) {
            NodeMapping var5 = MappingManager.this.mappings.get(var4);
            if (var5 != null) {
               this.removeMappingImpl(var5);
            }
         }

         MappingManager.this.invalidMappingsMgr.deleteInvalidMappings(var1);
         if (MappingManager.this.mappings.size() < var2) {
            MappingManager.this.treeMapper.setDirty(true);
            return true;
         } else {
            return false;
         }
      }
   }

   private static class MappingMap implements Iterable<NodeMapping> {
      private final Map<NodePath, NodeMapping> map = Maps.newHashMap();
      private List<NodeMapping> sortedList;
      private static final Comparator<NodeMapping> MAPPING_ORDER = new Comparator<NodeMapping>() {
         public int compare(NodeMapping var1, NodeMapping var2) {
            int var3 = var1.getTargetOrder();
            int var4 = var2.getTargetOrder();
            return Ints.compare(var3, var4);
         }
      };

      public MappingMap() {
      }

      public void add(NodeMapping var1) {
         this.map.put(var1.getTargetPath(), var1);
         this.sortedList = null;
      }

      public NodeMapping get(NodePath var1) {
         return this.map.get(var1);
      }

      public boolean remove(NodeMapping var1) {
         if (this.map.remove(var1.getTargetPath()) != null) {
            this.sortedList = null;
            return true;
         } else {
            return false;
         }
      }

      public void reOrder() {
         for (NodeMapping var2 : this.map.values()) {
            var2.reOrder();
         }

         this.sortedList = null;
      }

      public void clear() {
         this.map.clear();
         this.sortedList = null;
      }

      @Override
      public Iterator<NodeMapping> iterator() {
         if (this.sortedList == null) {
            this.sortedList = Lists.newArrayList(this.map.values());
            Collections.sort(this.sortedList, MAPPING_ORDER);
         }

         return this.sortedList.iterator();
      }

      public boolean isEmpty() {
         return this.map.isEmpty();
      }

      public int size() {
         return this.map.size();
      }
   }
}
