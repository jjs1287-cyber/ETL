package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import com.google.common.base.Optional;
import java.awt.Point;
import java.io.IOException;
import java.io.Writer;
import java.util.StringTokenizer;
import javax.swing.Action;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.DataTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.LdapUtils;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.MenuBuilder;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.integration.structure.mapping.PathSeparator;

public final class TreeMapperTargetTree extends TreeComponent {
   public TreeMapperTargetTree(TreeMapper var1, boolean var2, boolean var3, boolean var4) {
      super(var1, 2, var2, var3, var4, var1.m_Transformation.getOutputMappingTreeFilter());
      this.setSearchTitle("Search Target Tree");
   }

   @Override
   public void refresh() {
      super.refresh();
      this.m_TreeMapper.m_mapArea.repaint();
   }

   @Override
   public void paintFocus() {
      this.m_TreeMapper.m_treePanel.repaint();
   }

   @Override
   protected DataStructure getRepresentedStructure() {
      return this.m_TreeMapper.getTransformation().getOutputStructure();
   }

   @Override
   public void init() {
      super.init();
      this.m_bIsLdapTarget = this.m_TreeMapper.m_Transformation.getOutputStructure().getStructureType() == DataStructureType.LDAP;
   }

   @Override
   public Node expandTreeAndLookForNode(String var1) throws TreeMapperException {
      if (var1 != null && var1.length() != 0) {
         boolean var2 = var1.charAt(0) != '[';
         int var3 = var2 ? 0 : 1;
         int var4 = var1.length();
         if (this.m_flat) {
            Node var14 = this.m_deHashtable.get(var1);
            if (var14 == null) {
               int var15 = var1.indexOf(35);
               int var20 = var2 ? var1.length() : var1.length() - 1;
               boolean var22 = var15 >= 0;
               boolean var24 = var1.endsWith("/PRESCRIPT/");
               boolean var25 = var1.endsWith("/PRECONDITION/") || var24;
               Node var26 = this.m_root;
               if (!var1.startsWith("//")) {
                  var26 = this.m_root.getFirstChildFolder();
               }

               if (var22) {
                  int var12 = Integer.parseInt(var1.substring(var15 + 1, var20));
                  Node var13 = var26;
                  var26 = var26.findMultipleMappingExist(var12);
                  if (var26 == null) {
                     var26 = var13.insertMultipleMapping(var12);
                  }
               }

               for (Node var28 : var26.getChildren()) {
                  if (var25) {
                     if (var28.isPrecondition()) {
                        return var28;
                     }
                  } else if (var1.equals(var28.getName())) {
                     return var28;
                  }
               }

               if (var25) {
                  return this.insertPreconditionNode(var26);
               } else {
                  throw new TreeMapperException("Failed to find the node:" + var1 + " for flat target structure.");
               }
            } else {
               return var14;
            }
         } else {
            while (var4 > var3 && !PathSeparator.isSeparator(var1.charAt(var4 - 1))) {
               var4--;
            }

            if (var4 < var3) {
               return null;
            }

            StringTokenizer var5 = PathSeparator.tokenize(var1.substring(var3, var4), false);
            Node var6 = this.m_root;
            Node var7 = null;
            if (var6.getName().length() > 0) {
               if (!var5.hasMoreTokens()) {
                  return null;
               }

               if (!var5.nextToken().equals(var6.getName())) {
                  throw new TreeMapperException(
                     "Find target data element failed, the root name is inconsistent with the path. root name=" + var6.getName() + ", path=" + var1
                  );
               }
            }

            while (var5.hasMoreTokens() && var6 != null) {
               String var8 = var5.nextToken();
               var6.expandNode();
               var7 = var6.findChildFolder(var8);
               if (var7 != null && var7.isValueLeaf()) {
                  if (var1.endsWith("/PRECONDITION/") || var1.endsWith("/PRESCRIPT/")) {
                     var7 = var7.turnOffShortDisplay();
                     var7 = this.insertPreconditionNode(var7);
                  }

                  return var7;
               }

               if (var7 == null && (var6.isDerivedTypeHolder() || var6.isSubstitutionGroupHeader())) {
                  String var9 = StringUtils.substringBefore(var8, "#");
                  var7 = var6.expandHoldersTypeNode(var9, var8);
               }

               if (var7 == null) {
                  int var23 = var8.indexOf(35);
                  if (var23 >= 0) {
                     if (var23 + 1 >= var8.length()) {
                        throw new TreeMapperException("Find target data element failed, path=" + var1);
                     }

                     int var10 = Integer.parseInt(var8.substring(var23 + 1));
                     String var11 = var8.substring(0, var23);
                     var6.expandNode();
                     var7 = var6.findChildFolder(var11);
                     if (var7 != null) {
                        var7.expandNode();
                        var7.insertMultipleMapping(var10);
                     }
                  } else {
                     var6.expandNode();
                  }

                  var7 = var6.findChildFolder(var8);
               }

               var6 = var7;
            }

            if (var6 != null) {
               var6.expandNode();
               if (var1.endsWith("/PRECONDITION/") || var1.endsWith("/PRESCRIPT/")) {
                  this.insertPreconditionNode(var6);
               }
            }

            var7 = this.findNode(var1);
            if (var7 == null && this.m_bIsLdapTarget && var6 != null) {
               String var21 = var1.substring(var4);
               if (var21.equals("Attribute?command")) {
                  var7 = LdapUtils.insertAttributeCommand(this, var6);
               } else if (var21.equals("Old?value")) {
                  var7 = LdapUtils.insertOldValueNode(this, var6);
               }
            }

            return var7;
         }
      } else {
         return null;
      }
   }

   @Override
   protected void onSearchResultHighlighted(Node var1) {
      if (var1.isTargetMapped()) {
         this.m_TreeMapper.getMappingManager().showMapping(var1.m_deName);
      } else {
         this.m_TreeMapper.getMappingManager().showMapping(null);
         this.m_TreeMapper.m_sourceTreeComponent.updateUnderlineNodes(null);
      }
   }

   @Override
   protected void handleDoubleClick(Node var1, int var2, int var3) {
      if (var1.m_parent == null) {
         this.m_TreeMapper.getOutputLink().follow();
      } else {
         if ((var2 == 3 || var2 == 2) && !var1.isFolder()) {
            this.m_TreeMapper.showFormulaBuilder(var1);
         }
      }
   }

   @Override
   protected boolean canHaveData() {
      return true;
   }

   @Override
   protected void onNodeSelected(Node var1, boolean var2) {
      this.m_TreeMapper.selectTargetNode(var1, var2);
   }

   @Override
   protected void onSelectionCleared() {
      this.m_TreeMapper.getMappingManager().showMapping(null);
      this.m_TreeMapper.m_sourceTreeComponent.updateUnderlineNodes(null);
   }

   @Override
   protected void onDataLoopNodeClicked() {
      this.m_TreeMapper.m_sourceTreeComponent.myPaint(null);
   }

   @Override
   protected boolean supportsToolTip() {
      return true;
   }

   @Override
   protected String getDomainString() {
      return this.m_getLocationNode.getDisplay()
         + "\n"
         + this.m_getLocationNode.m_deName
         + "\nLoopPath: "
         + this.getTargetLoopPath(this.m_getLocationNode.m_deName)
         + "\nGenerator: "
         + (this.m_getLocationNode.getSourcePath() == null ? "null" : this.m_getLocationNode.getSourcePath())
         + "\n";
   }

   private String getTargetLoopPath(String var1) {
      if (var1 != null && !this.m_flat) {
         NodePath var2 = new NodePath(var1);
         Optional var3 = var2.getPathToLastLoop();
         return var3.isPresent() ? (String)var3.get() : "";
      } else {
         return "";
      }
   }

   @Override
   protected boolean canHaveSourcePathTip() {
      return true;
   }

   @Override
   protected void onDragStopped(MenuBuilder var1, int var2, int var3) {
      Point var4 = this.m_TreeMapper.m_sourceTreeComponent.getLocationOnScreen();
      this.m_TreeMapper.m_sourceTreeComponent.getLocationStatus(var2 - var4.x, var3 - var4.y, false);
      Node var5 = this.m_TreeMapper.m_sourceTreeComponent.m_getLocationNode;
      Node var6 = this.getHighlightedNode();
      this.setDropNode(var5);
      if (var5 != null) {
         if (!var5.isFolder() && var6 != null && !var6.isFolder()) {
            this.m_TreeMapper.m_sourceTreeComponent.setHighlightedNode(var5);
            this.m_TreeMapper.doMap(var6, false);
         }

         if (var5.isSfdcNode()) {
            var5 = var5.m_parent;
         }

         if (var6 != null) {
            if (var6.isSfdcNode()) {
               var6 = var6.m_parent;
            }

            if (var5.isRootLoopNode() && var6.isRootLoopNode()) {
               TreeMapper var7 = this.getTreeMapper();
               if (!this.m_flat || var7 == null || !var7.m_sourceTreeComponent.m_flat) {
                  var1.addMenuItem("Define fixed source loop node");
               }
            }

            if (var5.isFolder() && var6.isFolder()) {
               if (!var5.hasChildren()) {
                  var5.expandNode();
               }

               if (var5.hasChildren()) {
                  var1.addMenuItem("Auto map");
               }
            }
         }

         if (this.m_popup.getComponentCount() == 1) {
            String var11 = this.m_popup.getComponent(0).getName();
            if ("Define fixed source loop node".equals(var11)) {
               try {
                  var5 = this.getHighlightedNode().isSfdcNode() ? this.getHighlightedNode().m_parent : this.getHighlightedNode();
                  var5.m_mappingKits.fixGeneratorPath(var5.m_deName);
               } catch (Exception var9) {
                  var9.printStackTrace();
               }
            } else {
               this.m_TreeMapper.doStraightMappings(var5, this.getHighlightedNode());
            }

            this.m_popup.removeAll();
         }
      }
   }

   @Override
   protected void populateFolderNodeContextMenu(Node var1, MenuBuilder var2) {
      Node var3 = var1.m_parent;
      if (var1.isTypeDefinition() && var3.isDerivedTypeHolder()
         || var1.isSubstitutionGroup()
         || var1.isOptional()
         || var1.isLoopNode()
         || var1.isChildOfOneOfNode()
         || var3 != null && var1.isFolder() && !this.m_TreeMapper.isTargetXml()) {
         if (this.m_rightClickNode.hasPrecondition()) {
            var2.addMenuItem("Remove condition");
         } else if (this.m_rightClickNode.canAddCondition() || this.m_rightClickNode == this.getSalesForceNode()) {
            var2.addMenuItem("Add condition");
         }

         if (var1.isMultipleMap()) {
            var2.addMenuItem("Rename");
            var2.addMenuItem("Remove current folder");
         }

         if (!this.getTreeMapper().getTransformation().isPartOfSalesforcePartnerWizard() || var1 != this.getSalesForceNode()) {
            var2.addMenuItem("Create extra folder for mappings");
         }
      } else if (var3 == null || var1.isFolder()) {
         if (this.m_rightClickNode.hasPrecondition()) {
            var2.addMenuItem("Remove script");
         } else {
            var2.addMenuItem("Add script");
         }
      }

      if (var1.isRootLoopNode() && var1.m_mappingKits.hasFixedGenerator()) {
         var2.addMenuItem("Remove fixed source loop node");
      }
   }

   @Override
   protected Writer getCacheWriter(InputAndOutputMappingTreeStructureCache var1) throws IOException {
      return var1.getOutputStructureWriter(this.getTreeMapper().getTransformation().getID());
   }

   @Override
   protected void handleDeleteRequest(Node var1) {
      if (this.m_gotFocus && var1 != null && this.m_TreeMapper.getMappingManager().hasMappings()) {
         this.m_TreeMapper.deleteMapping(new NodePath(var1.m_deName));
         if (var1.isPrecondition() && !var1.isTargetMapped()) {
            Answer var2 = Ask.yesOrNo(
               "The condition expression has been removed. Do you also want to delete the condition node itself?", "Remove Condition Node?"
            );
            if (var2.isYes()) {
               var1.m_parent.deletePreCondition();
               this.refresh();
            }
         }
      } else if (var1 != null && var1.isPrecondition()) {
         var1.m_parent.deletePreCondition();
         this.refresh();
      }
   }

   public Node getTlnDtdRoot(String var1) {
      Node var2 = this.m_deHashtable.get(var1);
      return var2 != null && !var2.isFolder() ? var2.getRootLoopNode() : null;
   }

   @Override
   public DataTree getDataTree() {
      return this.m_TreeMapper.getTargetDataTree();
   }

   @Override
   protected NodeVisibilityFilter createMappedNodesOnlyFilter() {
      return new MappedTargetNodeVisibilityFilter();
   }

   @Override
   protected Action getEditStructureAction() {
      return this.m_TreeMapper.getPageAction("action.ic.tx.EditTarget");
   }
}
