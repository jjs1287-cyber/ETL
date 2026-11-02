package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import com.google.common.base.Objects;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.IOException;
import java.io.Writer;
import java.util.StringTokenizer;
import javax.swing.Action;
import javax.swing.JViewport;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.dnd.DropStrategy;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.dnd.DropZone;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaBuilderSourceTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaDisplayer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.DataTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.MenuBuilder;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Utils;
import org.etlforge.integration.data.entity.transformation.SourceBatchParams;
import org.etlforge.integration.data.script.ScriptUtils;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.integration.structure.mapping.PathSeparator;
import org.etlforge.ui.util.UiUtils;

public final class TreeMapperSourceTree extends TreeComponent {
   private static final DropZone[] NO_DROP_ZONES = new DropZone[0];
   private Node batchNode;
   private int batchSize;
   private DropZone[] dropZones;
   private TreeMapperSourceTree.DropZonePainter dropZonePainter;
   private Point dropLocation;
   private DropStrategy dropStrategy = DropStrategy.NONE;

   public TreeMapperSourceTree(TreeMapper var1, boolean var2, boolean var3, boolean var4) {
      super(var1, 77, var2, var3, var4, var1.m_Transformation.getInputMappingTreeFilter());
      this.dropZones = NO_DROP_ZONES;
      this.setSearchTitle("Search Source Tree");
      this.setDropTarget(new DropTarget(this, new TreeMapperSourceTree.DropHandler()));
   }

   @Override
   public void readMappingStructure(MappingTreeStructure var1, String var2) {
      super.readMappingStructure(var1, var2);
   }

   public void setDropStrategy(DropStrategy var1) {
      this.dropStrategy = var1;
   }

   @Override
   public void refresh() {
      super.refresh();
      this.m_TreeMapper.m_mapArea.repaint();
   }

   @Override
   public void init() {
      this.refreshBatchNode();
      super.init();
   }

   public void refreshBatchNode() {
      SourceBatchParams var1 = this.getTreeMapper().getTransformation().getSourceBatchParams();
      this.refreshBatchNode(var1);
   }

   public void refreshBatchNode(SourceBatchParams var1) {
      if (this.batchNode != null) {
         this.batchNode.setBatchNodeFlag(false);
      }

      String var2 = var1.getNodePath();
      this.batchNode = this.findNode(var2);
      this.batchSize = var1.getBatchSize();
      if (this.batchNode != null && this.batchSize > 0) {
         this.batchNode.setBatchNodeFlag(true);
      }

      this.getTreeMapper().repaint();
   }

   @Override
   public void paintFocus() {
      this.m_TreeMapper.m_treePanel.repaint();
   }

   @Override
   protected DataStructure getRepresentedStructure() {
      return this.m_TreeMapper.getTransformation().getInputStructure();
   }

   @Override
   public Node expandTreeAndLookForNode(String var1) throws TreeMapperException {
      if (var1 != null && var1.length() != 0) {
         Node var2 = this.m_deHashtable.get(var1);
         if (var2 != null) {
            return var2;
         }

         boolean var3 = var1.charAt(0) != '[';
         int var4 = var3 ? 0 : 1;
         int var5 = var1.length();
         if (this.m_flat) {
            var2 = this.m_deHashtable.get(var1);
            if (var2 == null) {
               throw new TreeMapperException("Find source data element failed, path=" + var1);
            } else {
               return var2;
            }
         } else {
            while (var5 > var4 && !PathSeparator.isSeparator(var1.charAt(var5 - 1))) {
               var5--;
            }

            if (var5 < var4) {
               return null;
            }

            StringTokenizer var6 = PathSeparator.tokenize(var1.substring(var4, var5), false);
            Node var7 = this.m_root;
            if (var7.getName().length() > 0) {
               if (!var6.hasMoreTokens()) {
                  return null;
               }

               if (!var6.nextToken().equals(var7.getName())) {
                  throw new TreeMapperException(
                     "Find source data element failed, the root name is inconsistent with the path. root name=" + var7.getName() + ", path=" + var1
                  );
               }
            }

            while (var6.hasMoreTokens()) {
               String var8 = var6.nextToken();
               var7.expandNode();
               int var9 = var8.indexOf(35);
               if (var9 > 0) {
                  var8 = var8.substring(0, var9);
               }

               var2 = var7.findChildFolder(var8);
               if (var2 != null && var2.isValueLeaf()) {
                  return var2;
               }

               if (var2 == null) {
                  if (!var7.isDerivedTypeHolder() && !var7.isSubstitutionGroupHeader()) {
                     var7.expandNode();
                  } else {
                     var7.expandHoldersTypeNode(var8, var8);
                  }

                  var2 = var7.findChildFolder(var8);
               }

               if (var2 == null) {
                  throw new TreeMapperException("The source data element path=" + var1 + " is invalid.");
               }

               var7 = var2;
            }

            var7.expandNode();
            String var12 = ScriptUtils.stripInstanceTag(var1);
            if (!var3 && var12.charAt(0) != '[') {
               var12 = "[" + var12 + "]";
            }

            return this.findNode(var12);
         }
      } else {
         return null;
      }
   }

   @Override
   protected void handleDoubleClick(Node var1, int var2, int var3) {
      if (var1.m_parent == null) {
         this.m_TreeMapper.getInputLink().follow();
      } else {
         if (var2 == 3 || var2 == 2) {
            Node var4 = this.getTreeMapper().m_targetTreeComponent.getHighlightedNode();
            if (!var1.isFolder() && var4 != null && !this.getTreeMapper().m_targetTreeComponent.getHighlightedNode().isFolder()) {
               this.getTreeMapper().doMap(this.getTreeMapper().m_targetTreeComponent.getHighlightedNode(), false);
            }
         }
      }
   }

   @Override
   protected void onSearchResultHighlighted(Node var1) {
      this.m_TreeMapper.getMappingManager().showMapping(null);
      this.m_TreeMapper.m_targetTreeComponent.updateUnderlineNodes(var1.isSourceUsed() ? var1 : null);
      if (this.m_TreeMapper.m_targetTreeComponent.m_selectedNodes.size() > 0) {
         Node var2 = this.m_TreeMapper.m_targetTreeComponent.m_selectedNodes.get(0);
         this.m_TreeMapper.m_targetTreeComponent.showNode(var2, false);
      } else {
         this.m_TreeMapper
            .m_targetTreeComponent
            .repaint(
               0,
               this.m_TreeMapper.m_targetTreeComponent.m_ymin,
               this.m_TreeMapper.m_targetTreeComponent.m_xmax,
               this.m_TreeMapper.m_targetTreeComponent.m_ymax
            );
      }
   }

   @Override
   protected String getToolTipForLocation(boolean var1, int var2) {
      String var3 = super.getToolTipForLocation(var1, var2);
      if (var2 == 3 && this.m_getLocationNode != null && this.m_getLocationNode == this.batchNode && this.batchSize > 0) {
         if (StringUtils.isNotEmpty(var3)) {
            var3 = var3 + "\n";
         }

         var3 = var3 + "This is the transformation's batch node.\nBatch size: " + this.batchSize;
      }

      return var3;
   }

   @Override
   protected boolean canHaveData() {
      return true;
   }

   @Override
   protected void onNodeSelected(Node var1, boolean var2) {
      this.m_TreeMapper.selectSourceNode(var1, var2);
   }

   @Override
   protected void onSelectionCleared() {
      this.m_TreeMapper.m_targetTreeComponent.updateUnderlineNodes(null);
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
         + this.getSourceLoopPath(this.m_getLocationNode.m_deName);
   }

   private String getSourceLoopPath(String var1) {
      if (var1 != null && !this.m_flat) {
         var1 = Utils.removeBrackets(var1);
         int var2 = var1.lastIndexOf(".");

         while (var2 > 0) {
            char var7 = 36;
            int var3 = var2 - 1;

            while (var3 >= 0 && (var7 = var1.charAt(var3)) != '.' && var7 != '$') {
               var3--;
            }

            String var5 = var1.substring(var3 + 1, var2);
            if (var3 == 0 || var5.indexOf(35) < 0) {
               break;
            }

            if (var7 == '.') {
               var2 = var3;
            } else {
               var2 = var1.lastIndexOf(46, var3);
            }
         }

         return var2 < 0 ? "" : var1.substring(0, var2 + 1);
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
      Point var4 = this.m_TreeMapper.m_targetTreeComponent.getLocationOnScreen();
      this.m_TreeMapper.m_targetTreeComponent.getLocationStatus(var2 - var4.x, var3 - var4.y, false);
      Node var5 = this.m_TreeMapper.m_targetTreeComponent.m_getLocationNode;
      this.setDropNode(var5);
      if (var5 != null) {
         if (var5.isSfdcNode()) {
            var5 = var5.m_parent;
         }

         if (!var5.isFolder() && !this.getHighlightedNode().isFolder()) {
            this.m_TreeMapper.doMap(var5, false);
         }

         if (var5.isRootLoopNode() && this.getHighlightedNode().isRootLoopNode()) {
            TreeMapper var6 = this.getTreeMapper();
            if (!this.m_flat || var6 == null || !var6.m_targetTreeComponent.m_flat) {
               var1.addMenuItem("Define fixed source loop node");
            }
         }

         if (var5.isFolder() && this.getHighlightedNode().isFolder()) {
            if (!var5.hasChildren()) {
               var5.expandNode();
            }

            if (var5.hasChildren()) {
               var1.addMenuItem("Auto map");
            }
         }

         if (this.m_popup.getComponentCount() == 1) {
            String var9 = this.m_popup.getComponent(0).getName();
            if ("Define fixed source loop node".equals(var9)) {
               try {
                  var5.m_mappingKits.fixGeneratorPath(this.getHighlightedNode().m_deName);
               } catch (Exception var8) {
                  var8.printStackTrace();
               }
            } else {
               this.m_TreeMapper.doStraightMappings(this.getHighlightedNode(), var5);
            }

            this.m_popup.removeAll();
         }
      }
   }

   @Override
   protected Writer getCacheWriter(InputAndOutputMappingTreeStructureCache var1) throws IOException {
      return var1.getInputStructureWriter(this.getTreeMapper().getTransformation().getID());
   }

   @Override
   public void setSimpleXmlElementDisplay(boolean var1) {
      super.setSimpleXmlElementDisplay(var1);
      FormulaDisplayer var2 = this.getTreeMapper().getTreeMapperContainer().getFormulaDisplayer();
      var2.setShortValueDisplay(var1);
   }

   @Override
   public DataTree getDataTree() {
      return this.m_TreeMapper.getSourceDataTree();
   }

   public Node getGenerator(NodePath var1) {
      TreeMapperTargetTree var2 = this.getTreeMapper().m_targetTreeComponent;
      Node var3 = var2.findNode(var1);
      if (var3 != null) {
         String var4 = var3.getRootLoopGenerator();
         if (var4 != null) {
            return this.findNode("[" + var4 + "]");
         }
      }

      return null;
   }

   public NodeVisibilityFilter getFilterForFormulaBuilder(FormulaBuilderSourceTree var1) {
      CompoundNodeVisibilityFilter var2 = new CompoundNodeVisibilityFilter();
      if (this.hasEffectivePathFilter()) {
         var2.addFilter(new PathBasedNodeVisibilityFilter(this.m_TreeMapper, this.getNodePathFilter()));
      }

      if (this.isShowDataNodesOnly()) {
         var2.addFilter(new NodesWithDataVisibiltyFilter(var1));
      }

      return var2.isEmpty() ? NodeVisibilityFilter.NOTHING_HIDDEN : var2;
   }

   @Override
   protected NodeVisibilityFilter createMappedNodesOnlyFilter() {
      return new MappedSourceNodeVisibilityFilter();
   }

   @Override
   protected Action getEditStructureAction() {
      return this.m_TreeMapper.getPageAction("action.ic.tx.EditSource");
   }

   @Override
   public void paint(Graphics var1) {
      if (this.dropZones.length > 0) {
         this.getDropZonePainter().paint(this.dropZones, var1);
      } else {
         super.paint(var1);
      }
   }

   private TreeMapperSourceTree.DropZonePainter getDropZonePainter() {
      if (this.dropZonePainter == null) {
         this.dropZonePainter = new TreeMapperSourceTree.DropZonePainter();
      }

      return this.dropZonePainter;
   }

   private final class DropHandler extends DropTargetAdapter {
      private DropHandler() {
      }

      @Override
      public void dragEnter(DropTargetDragEvent var1) {
         TreeMapperSourceTree.this.dropLocation = var1.getLocation();
         this.setDropZones(TreeMapperSourceTree.this.dropStrategy.getDropZones(var1));
      }

      private void setDropZones(DropZone[] var1) {
         TreeMapperSourceTree.this.dropZones = var1;
         TreeMapperSourceTree.this.repaint();
      }

      @Override
      public void dragOver(DropTargetDragEvent var1) {
         Point var2 = TreeMapperSourceTree.this.dropLocation;
         TreeMapperSourceTree.this.dropLocation = var1.getLocation();
         boolean var3 = false;

         for (DropZone var7 : TreeMapperSourceTree.this.dropZones) {
            if (var7.isDropSupported()) {
               var3 = true;
               break;
            }
         }

         if (var3) {
            var1.acceptDrag(2);
         } else {
            var1.rejectDrag();
         }

         if (!Objects.equal(var2, TreeMapperSourceTree.this.dropLocation)) {
            TreeMapperSourceTree.this.repaint();
         }
      }

      @Override
      public void dragExit(DropTargetEvent var1) {
         this.cleanup();
      }

      private void cleanup() {
         TreeMapperSourceTree.this.dropLocation = null;
         this.setDropZones(TreeMapperSourceTree.NO_DROP_ZONES);
      }

      @Override
      public void drop(DropTargetDropEvent var1) {
         boolean var2 = false;

         try {
            var2 = TreeMapperSourceTree.this.dropStrategy.handleDrop(var1);
         } finally {
            this.cleanup();
            var1.dropComplete(var2);
         }
      }
   }

   private class DropZonePainter {
      private DropZonePainter() {
      }

      public void paint(DropZone[] var1, Graphics var2) {
         Graphics2D var3 = (Graphics2D)var2.create();

         try {
            this.fillBackground(var3);
            this.drawTranslucency(var3);
            this.drawDropZones(var1, var3);
         } finally {
            var3.dispose();
         }
      }

      private void fillBackground(Graphics2D var1) {
         Color var2 = var1.getColor();
         var1.setColor(TreeMapperSourceTree.this.getBackground());
         var1.fillRect(0, 0, TreeMapperSourceTree.this.getWidth(), TreeMapperSourceTree.this.getHeight());
         var1.setColor(var2);
      }

      private void drawTranslucency(Graphics2D var1) {
         Composite var2 = var1.getComposite();
         var1.setComposite(AlphaComposite.getInstance(3, 0.075F));
         TreeMapperSourceTree.super.paint(var1);
         var1.setComposite(var2);
      }

      private void drawDropZones(DropZone[] var1, Graphics2D var2) {
         UiUtils.makeAntiAliased(var2);
         JViewport var3 = (JViewport)TreeMapperSourceTree.this.getParent();
         Rectangle var4 = var3.getViewRect();
         int var5 = var4.height / var1.length;
         int var6 = var4.y;
         int var7 = var4.width;

         for (DropZone var11 : var1) {
            int var12 = var6 + var5;
            var11.setActive(TreeMapperSourceTree.this.dropLocation.y >= var6 && TreeMapperSourceTree.this.dropLocation.y < var12);
            var11.render(var2, var6, var7, var5);
            var6 = var12;
         }
      }
   }
}
