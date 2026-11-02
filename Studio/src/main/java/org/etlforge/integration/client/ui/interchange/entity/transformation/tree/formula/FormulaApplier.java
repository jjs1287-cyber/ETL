package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import javax.swing.SwingUtilities;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.NodeMapping;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.data.script.ScriptConstants;

final class FormulaApplier {
   private final FormulaBuilderUi ui;
   private final TreeMapper treeMapper;
   private String targetDE;
   private NodeMapping mapping;
   private boolean ok;
   private boolean applySilently;

   public FormulaApplier(TreeMapper var1, FormulaBuilderUi var2) {
      this.ui = var2;
      this.treeMapper = var1;
      this.ok = true;
   }

   public final void setApplySilently(boolean var1) {
      this.applySilently = var1;
   }

   public NodeMapping getMapping() {
      return this.mapping;
   }

   public boolean apply(NodeMapping var1, String var2) {
      this.mapping = var1;
      this.targetDE = var2;
      String var3 = this.ui.getTranslatedExpression();
      Node var4 = this.treeMapper.findTargetNode(var2);
      if (this.isEmptyMapping(var3)) {
         this.saveEmptyFormula(var3, var4);
      } else {
         this.saveNonEmptyFormula(var3, var4);
      }

      return this.ok;
   }

   private boolean isEmptyMapping(String var1) {
      return ScriptConstants.isEmptyExpression(var1);
   }

   private void saveEmptyFormula(String var1, Node var2) {
      this.treeMapper.getMappingManager().deleteMapping(this.mapping);
      this.treeMapper.updateMappingFlag();
      this.treeMapper.updateMapAreaTables(this.targetDE, null);

      try {
         var2.updateMappingKits(var1);
      } catch (Exception var4) {
         var4.printStackTrace();
      }
   }

   private void saveNonEmptyFormula(String var1, Node var2) {
      String var3 = this.treeMapper.verifyExpression(var1);
      Node var4 = var2.getRootLoopNode();
      boolean var5 = var4.m_mappingKits.hasFixedGenerator();
      String var6 = var5 ? var4.m_mappingKits.getGenerator() : null;
      if (var3.isEmpty()) {
         this.saveFormulaThatAppearsToBeValid(var1, var6);
      } else {
         this.handleInvalidFormula(var3, var6);
      }
   }

   private void saveFormulaThatAppearsToBeValid(String var1, String var2) {
      if (this.treeMapper.m_transform.hasCResolve()) {
         try {
            var1 = this.treeMapper.m_transform.addInstanceTag(var1, var2);
         } catch (Exception var4) {
            this.saveInvalidFormula(var2, false);
            return;
         }
      }

      this.save(var1);
   }

   private void saveInvalidFormula(String var1, boolean var2) {
      this.treeMapper.setDirty(true);
      String var3 = this.ui.getExpression();
      if (var2 && this.treeMapper.m_transform.hasCResolve()) {
         try {
            var3 = this.treeMapper.m_transform.addInstanceTag(var3, var1);
         } catch (Exception var5) {
         }
      }

      this.treeMapper.getMappingManager().replaceMapping(var3, this.targetDE);
      this.treeMapper.updateMapAreaTables(this.targetDE, var3);
      this.treeMapper.setDirty(true);
   }

   private void save(String var1) {
      if (this.mapping != null) {
         this.treeMapper.getMappingManager().replaceMapping(var1, this.targetDE);
      } else {
         this.mapping = this.treeMapper.getNodeMappingFactory().newMapping(this.targetDE, var1);
         this.treeMapper.getMappingManager().addMapping(this.mapping);
         this.treeMapper.updateMappingFlag();
         this.treeMapper.getMappingManager().changeHighLighted(this.mapping);
         this.treeMapper.setDirty(true);
      }

      this.treeMapper.updateMapAreaTables(this.targetDE, var1);
   }

   private void handleInvalidFormula(String var1, String var2) {
      this.ok = this.isItOkToSaveInvalidFormula(var1);
      if (this.ok) {
         this.saveInvalidFormula(var2, true);
      }
   }

   private boolean isItOkToSaveInvalidFormula(String var1) {
      if (this.applySilently) {
         return true;
      }

      Answer var2 = Ask.yesOrNo(
         SwingUtilities.getWindowAncestor(this.treeMapper),
         var1 + "\n\nAre you sure you want to save your changes?",
         "Invalid formula",
         Alert.Option.NO,
         Alert.Type.ERROR
      );
      return var2.isYes();
   }
}
