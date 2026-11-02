package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperException;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.TransformationVariables;
import org.etlforge.integration.structure.mapping.MapStringFormat;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.MappingFactory;
import org.etlforge.integration.structure.mapping.NodePath;

final class TreeMapperEditFactory {
   private final TransformationPanel tfPanel;
   private TreeMapper treeMapper;
   private List<String> currentMappings;
   private Map<String, String> currentRenamedNodes;

   public TreeMapperEditFactory(TransformationPanel var1) {
      this.tfPanel = var1;
   }

   public void setTreeMapper(TreeMapper var1) {
      this.treeMapper = var1;
      this.currentMappings = var1.getCurrentMappings();
      this.currentRenamedNodes = var1.getRenamedTargetNodes();
   }

   public UndoableEdit getEdit() {
      if (this.treeMapper == null) {
         return null;
      }

      List var1 = this.treeMapper.getCurrentMappings();
      Map var2 = this.treeMapper.getRenamedTargetNodes();
      TreeMapperEditFactory.MappingUndoableEdit var3 = null;
      if (this.isChange(var1) || this.isChange(var2)) {
         var3 = new TreeMapperEditFactory.MappingUndoableEdit(this.currentMappings, var1, this.currentRenamedNodes, var2);
         this.currentMappings = var1;
      }

      return var3;
   }

   private boolean isChange(List<String> var1) {
      return !Sets.newHashSet(this.currentMappings).equals(Sets.newHashSet(var1));
   }

   private boolean isChange(Map<String, String> var1) {
      return !var1.equals(this.currentRenamedNodes);
   }

   private static final class MappingDiff {
      private final Map<NodePath, Mapping> addedTargetNodes;
      private final Map<NodePath, Mapping> removedTargetNodes;
      private final Map<NodePath, TreeMapperEditFactory.MappingDiff.ExpressionChange> changedMappings;

      public MappingDiff(List<String> var1, List<String> var2) {
         List var3 = parseMapStrings(var1);
         List var4 = parseMapStrings(var2);
         this.addedTargetNodes = this.createTargetNodeDiff(var4, var3);
         this.removedTargetNodes = this.createTargetNodeDiff(var3, var4);
         this.changedMappings = this.createExpressionDiff(var3, var4);
      }

      private static List<Mapping> parseMapStrings(List<String> var0) {
         return KongaListUtils.transform(var0, MapStringFormat.STRING_TO_MAPPING);
      }

      private Map<NodePath, Mapping> createTargetNodeDiff(List<Mapping> var1, List<Mapping> var2) {
         Map var3 = mapify(var1);

         for (Mapping var5 : var2) {
            var3.remove(var5.getTargetPath());
         }

         return var3;
      }

      private static Map<NodePath, Mapping> mapify(List<Mapping> var0) {
         HashMap var1 = Maps.newHashMap();

         for (Mapping var3 : var0) {
            var1.put(var3.getTargetPath(), var3);
         }

         return var1;
      }

      private Map<NodePath, TreeMapperEditFactory.MappingDiff.ExpressionChange> createExpressionDiff(List<Mapping> var1, List<Mapping> var2) {
         HashMap var3 = Maps.newHashMap();
         Map var4 = mapify(var1);

         for (Mapping var6 : var2) {
            Mapping var7 = (Mapping)var4.get(var6.getTargetPath());
            if (var7 != null && !var7.getExpression().equals(var6.getExpression())) {
               var3.put(var6.getTargetPath(), new TreeMapperEditFactory.MappingDiff.ExpressionChange(var7.getExpression(), var6.getExpression()));
            }
         }

         return var3;
      }

      public void undo(Transformation var1, TransformationVariables var2) {
         for (Mapping var4 : this.addedTargetNodes.values()) {
            var2.removeMapping(var1, var4);
         }

         for (Mapping var8 : this.removedTargetNodes.values()) {
            var2.scanMapping(var1, var8);
         }

         for (Entry var9 : this.changedMappings.entrySet()) {
            Mapping var5 = MappingFactory.newMapping(
               (NodePath)var9.getKey(), ((TreeMapperEditFactory.MappingDiff.ExpressionChange)var9.getValue()).oldExpression
            );
            var2.scanMapping(var1, var5);
         }
      }

      public void redo(Transformation var1, TransformationVariables var2) {
         for (Mapping var4 : this.removedTargetNodes.values()) {
            var2.removeMapping(var1, var4);
         }

         for (Mapping var8 : this.addedTargetNodes.values()) {
            var2.scanMapping(var1, var8);
         }

         for (Entry var9 : this.changedMappings.entrySet()) {
            Mapping var5 = MappingFactory.newMapping(
               (NodePath)var9.getKey(), ((TreeMapperEditFactory.MappingDiff.ExpressionChange)var9.getValue()).newExpression
            );
            var2.scanMapping(var1, var5);
         }
      }

      private static final class ExpressionChange {
         public final String oldExpression;
         public final String newExpression;

         private ExpressionChange(String var1, String var2) {
            this.oldExpression = var1;
            this.newExpression = var2;
         }
      }
   }

   private class MappingUndoableEdit extends AbstractUndoableEdit {
      private final List<String> previousMappings;
      private final List<String> newMappings;
      private final Map<String, String> previousRenamedNodes;
      private final Map<String, String> newRenamedNodes;
      private final TreeMapperEditFactory.MappingDiff mappingDiff;

      public MappingUndoableEdit(List<String> var2, List<String> var3, Map<String, String> var4, Map<String, String> var5) {
         this.previousMappings = var2;
         this.newMappings = var3;
         this.previousRenamedNodes = var4;
         this.newRenamedNodes = var5;
         this.mappingDiff = new TreeMapperEditFactory.MappingDiff(var2, var3);
      }

      @Override
      public void redo() throws CannotRedoException {
         super.redo();
         this.updateTreeMapper(this.newMappings, this.newRenamedNodes);
         Transformation var1 = TreeMapperEditFactory.this.treeMapper.getTransformation();
         TransformationVariables var2 = TreeMapperEditFactory.this.treeMapper.getDataElements();
         this.mappingDiff.redo(var1, var2);
      }

      @Override
      public void undo() throws CannotUndoException {
         super.undo();
         this.updateTreeMapper(this.previousMappings, this.previousRenamedNodes);
         Transformation var1 = TreeMapperEditFactory.this.treeMapper.getTransformation();
         TransformationVariables var2 = TreeMapperEditFactory.this.treeMapper.getDataElements();
         this.mappingDiff.undo(var1, var2);
      }

      @Override
      public String getPresentationName() {
         return Strings.get("TreeMapper.Mapping.Edit");
      }

      private void updateTreeMapper(List<String> var1, Map<String, String> var2) {
         TreeMapperEditFactory.this.tfPanel.setSendUndoEvents(false);
         TreeMapperEditFactory.this.tfPanel.setSendInputChangeEvents(false);

         try {
            this.applyMappings(var1);
            this.setRenamedTargetNodes(var2);
         } finally {
            TreeMapperEditFactory.this.tfPanel.setSendUndoEvents(true);
            TreeMapperEditFactory.this.tfPanel.setSendInputChangeEvents(true);
            TreeMapperEditFactory.this.currentMappings = var1;
            TreeMapperEditFactory.this.currentRenamedNodes = var2;
         }
      }

      private void applyMappings(List<String> var1) {
         try {
            TreeMapperEditFactory.this.treeMapper.m_mapAreaTable.clear();
            TreeMapperEditFactory.this.treeMapper.getMappingManager().reset(var1);
            TreeMapperEditFactory.this.treeMapper.updateMappingFlag();
            TreeMapperEditFactory.this.treeMapper.m_sourceTreeComponent.refresh();
            TreeMapperEditFactory.this.treeMapper.m_targetTreeComponent.refresh();
         } catch (TreeMapperException var3) {
         }
      }

      private void setRenamedTargetNodes(Map<String, String> var1) {
         TreeMapperEditFactory.this.tfPanel.getTreeMapper().setRenamedTargetNodes(var1);
      }
   }
}
