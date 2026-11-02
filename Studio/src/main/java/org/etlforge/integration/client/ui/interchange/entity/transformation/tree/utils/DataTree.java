package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperSourceTree;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.reader.DocReader;
import org.etlforge.integration.structure.reader.JbNode;

public final class DataTree {
   private final TreeMapper treeMapper;
   private DocReader reader;
   private JbNode jbRoot;
   private final Set<TreeComponent> trees;
   private final List<DataTreeListener> listeners;

   public DataTree(TreeMapper var1) {
      this.treeMapper = var1;
      this.trees = Sets.newHashSet();
      this.listeners = new CopyOnWriteArrayList<>();
   }

   public void setDataRoot(TreeComponent var1, JbNode var2) {
      checkThread("create");
      verifyTree(var1);
      this.reader = var1.m_DocReader;
      this.jbRoot = var2;

      for (TreeComponent var4 : this.trees) {
         if (var4 != var1) {
            var4.load(var1.m_DocReader, var2);
         }
      }

      this.trees.add(var1);
      this.fireDataLoaded();
   }

   private void fireDataLoaded() {
      for (DataTreeListener var2 : this.listeners) {
         var2.newDataLoaded();
      }
   }

   private static void checkThread(String var0) {
      ApplicationUiUtils.logIfNotOnEDT(DataTree.class, var0);
   }

   public void removeData() {
      checkThread("removeData");

      for (TreeComponent var2 : this.trees) {
         var2.removeDataNodes();
      }

      this.jbRoot = null;
      this.fireDataRemoved();
   }

   private void fireDataRemoved() {
      for (DataTreeListener var2 : this.listeners) {
         var2.dataRemoved();
      }
   }

   public void addTreeComponent(TreeComponent var1) {
      checkThread("addTreeComponent");
      verifyTree(var1);
      if (this.trees.add(var1) && this.hasData()) {
         var1.load(this.reader, this.jbRoot);
         TreeMapperSourceTree var2 = this.treeMapper.m_sourceTreeComponent;
         this.copyJbNodeToNewTree(var2, var1, var1.m_root);

         try {
            var1.setDataValues_recursive(var1.m_root);
         } catch (IOException var4) {
            throw new RuntimeException(var4);
         }
      }
   }

   private void copyJbNodeToNewTree(TreeComponent var1, TreeComponent var2, Node var3) {
      Node var4 = var1.findNode(var3.m_deName);
      var3.m_jbNode = var4.m_jbNode;

      for (Node var6 : var3.getChildren()) {
         if (var6.isFolder()) {
            this.copyJbNodeToNewTree(var1, var2, var6);
         }
      }
   }

   public void removeTreeComponent(TreeComponent var1) {
      checkThread("removeTreeComponent");
      this.trees.remove(var1);
   }

   private static void verifyTree(TreeComponent var0) {
      Preconditions.checkArgument(var0.canHaveData(), "A " + var0.getClass().getName() + " cannot have source data.");
   }

   private boolean hasData() {
      return this.jbRoot != null;
   }

   public void displayInstance(TreeComponent var1, Node var2, JbNode var3) {
      checkThread("displayInstance");
      Preconditions.checkArgument(this.trees.contains(var1), "Unknown tree");
      this.displayInstanceInSingleTree(var1, var2, var3);
      this.displayInstanceInOtherTrees(var1, var2.m_deName, var3);
      if (this.treeMapper != null && this.treeMapper.m_targetTreeComponent == var1 && var2.m_jbNode.m_source != null) {
         TreeMapperSourceTree var4 = this.treeMapper.m_sourceTreeComponent;
         DataTree var5 = var4.getDataTree();
         JbNode var6 = var2.m_jbNode.m_source;
         Node var7 = var4.findNode(var6);
         var5.displayInstance(var4, var7, var6);
      }

      this.fireNewDataInstance(var2.m_deName);
   }

   private void init_child_instance_recursive(Node var1) {
      JbNode var2 = var1.m_jbNode;

      for (Node var4 : var1.getChildren()) {
         if (var4.isFolder()) {
            CROM var5 = var4.m_CROM;
            JbNode var6 = null;
            if (var2 != null) {
               var6 = var2.getChild(var5.getName());
            }

            if (var5.isLoopNode()) {
               if (var6 != null && var6.getChildren().size() > 0) {
                  var6 = var6.getChildren().get(0);
               } else {
                  var6 = null;
               }
            }

            var4.m_jbNode = var6;
            this.init_child_instance_recursive(var4);
         }
      }
   }

   private void displayInstanceInSingleTree(TreeComponent var1, Node var2, JbNode var3) {
      var2.resetDataNode();
      var2.m_jbNode = var3;
      this.init_child_instance_recursive(var2);
      var2 = var2.findCommonJbParent(var2.m_jbNode);

      try {
         var1.setDataValues_recursive(var2);
      } catch (IOException var5) {
         throw new RuntimeException("displayInstanceInSingleTree failed.\n" + var5);
      }

      var1.refresh();
      var1.onDataLoopNodeClicked();
   }

   private void displayInstanceInOtherTrees(TreeComponent var1, String var2, JbNode var3) {
      for (TreeComponent var5 : this.trees) {
         if (var5 != var1) {
            Node var6 = var5.findNode_expandTreeIfNotExist(var2);
            if (var6 != null) {
               this.displayInstanceInSingleTree(var5, var6, var3);

               try {
                  var5.setDataValues_recursive(var5.m_root);
               } catch (IOException var8) {
                  throw new RuntimeException("displayInstanceInOtherTrees failed.\n" + var8);
               }
            }
         }
      }
   }

   private void fireNewDataInstance(String var1) {
      for (DataTreeListener var3 : this.listeners) {
         var3.newDataInstanceSelected(var1);
      }
   }

   public void nextInstance(TreeComponent var1, Node var2) {
      checkThread("nextInstance");
      if (var2.isLoopNode()) {
         if (var2.m_jbNode != null) {
            JbNode var3 = var2.m_jbNode;
            if (var3.isInstance()) {
               int var4 = var3.getParent().getChildren().size();
               int var5 = (var3.getInstanceNumber() + 1) % var4;
               var3 = var3.getParent().getChildren().get(var5);
               this.displayInstance(var1, var2, var3);
            }
         }
      }
   }

   public void previousInstance(TreeComponent var1, Node var2) {
      checkThread("previousInstance");
      if (var2.isLoopNode()) {
         if (var2.m_jbNode != null) {
            JbNode var3 = var2.m_jbNode;
            if (var3.isInstance()) {
               int var4 = var3.getParent().getChildren().size();
               int var5 = (var3.getInstanceNumber() - 1 + var4) % var4;
               var3 = var3.getParent().getChildren().get(var5);
               this.displayInstance(var1, var2, var3);
            }
         }
      }
   }

   public void addListener(DataTreeListener var1) {
      this.listeners.add(var1);
   }

   public void removeListener(DataTreeListener var1) {
      this.listeners.remove(var1);
   }
}
