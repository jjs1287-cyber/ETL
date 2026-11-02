package org.etlforge.integration.client.ui.operation.options;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import javax.swing.JLabel;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.client.mapping.CromTreeFactory;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.client.ui.crom.tree.CromTreeWidget;
import org.etlforge.integration.client.ui.crom.tree.SelectDerivedTypeAction;
import org.etlforge.integration.client.ui.crom.tree.SelectSubstitutionGroupAction;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_Tree;
import org.etlforge.integration.structure.crom.CromPredicates;
import org.etlforge.integration.structure.crom.CromTreeException;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionListeners;
import org.etlforge.ui.widget.tree.KongaTree;

public final class ChunkNodeSelector implements UiProvider {
   private final CromTreeWidget tree;
   private final String initialSelection;
   private final ChunkNodeSelector.SelectionFilter selectionFilter = new ChunkNodeSelector.SelectionFilter();
   private final JLabel invalidSelectionLabel = new JLabel(" ");

   public ChunkNodeSelector(String var1) {
      this.initialSelection = var1;
      this.tree = this.createEmptyTree();
      this.tree.addSelectionListener(this.selectionFilter);
   }

   private CromTreeWidget createEmptyTree() {
      CromTreeWidget var1 = new CromTreeWidget();
      var1.setSelectionMode(KongaTree.SelectionMode.SINGLE_TREE_SELECTION);
      var1.setIncludeLeafNodes(false);
      var1.addContextMenuAction(new SelectDerivedTypeAction(var1));
      var1.addContextMenuAction(new SelectSubstitutionGroupAction(var1));
      return var1;
   }

   public void createStructures(Transformation var1, SourceTarget var2) {
      this.tree.displayWaitNode();
      ChunkNodeSelector.Worker var3 = new ChunkNodeSelector.Worker(var1, var2);
      var3.execute();
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 3);
      var2.northToSouth("Select the chunking node:", this.tree, this.invalidSelectionLabel);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocus();
   }

   public void addSelectionListener(SelectionListener var1) {
      this.selectionFilter.addListener(var1);
   }

   public void setDefaultAction(SelectionAction var1) {
      this.tree.setDefaultAction(var1);
   }

   public CROM getSelectedNode() {
      return this.selectionFilter.selectedNode;
   }

   private class SelectionFilter implements SelectionListener {
      private final SelectionListeners listeners = new SelectionListeners();
      private final Predicate<CROM> filter = CromPredicates.ofType(524360L);
      public CROM selectedNode;

      private SelectionFilter() {
      }

      public void addListener(SelectionListener var1) {
         this.listeners.add(var1);
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         SelectionChangedEvent var2 = new SelectionChangedEvent(var1.getSource(), this.filterSelection(var1));
         this.listeners.call().selectionChanged(var2);
      }

      private OrderedSelection filterSelection(SelectionChangedEvent var1) {
         ChunkNodeSelector.this.invalidSelectionLabel.setText(" ");
         ChunkNodeSelector.this.invalidSelectionLabel.setIcon(null);
         OrderedSelection var2 = (OrderedSelection)var1.getSelection();
         if (var2.size() == 1) {
            CROM var3 = (CROM)var2.head();
            if (this.filter.apply(var3)) {
               this.selectedNode = var3;
               return var2;
            }

            ChunkNodeSelector.this.invalidSelectionLabel.setIcon(CommonIcons.INFO_16);
            ChunkNodeSelector.this.invalidSelectionLabel.setText("Only loop nodes can be selected as chunk node");
         }

         this.selectedNode = null;
         return OrderedSelection.EMPTY;
      }
   }

   private final class Worker extends KongaSwingWorker<CROM_Tree, Void> {
      private final Transformation tx;
      private final SourceTarget st;

      public Worker(Transformation var2, SourceTarget var3) {
         this.tx = var2;
         this.st = var3;
      }

      protected CROM_Tree doInBackground() throws Exception {
         CROM_Tree var1 = this.findCachedTree();
         if (var1 == null) {
            var1 = this.createNewTree();
         }

         return var1;
      }

      private CROM_Tree findCachedTree() {
         try {
            Optional var1 = this.lookupJtrFromCache();
            if (var1.isPresent()) {
               return CROM_Tree.fromMappingStructure((MappingTreeStructure)var1.get());
            }
         } catch (CromTreeException var2) {
            ErrorLog.log(this.getClass(), "Error converting a mapping structure to a CROM tree.", var2);
         } catch (DataCacheException var3) {
            ErrorLog.log(this.getClass(), "Error when accessing the project's mapping structure cache.", var3);
         }

         return null;
      }

      private Optional<MappingTreeStructure> lookupJtrFromCache() throws DataCacheException {
         ManagedProject var1 = ProjectUtils.getManagedProject(this.tx);
         InputAndOutputMappingTreeStructureCache var2 = var1.getTransformationMappingStructureCache();
         return this.st == SourceTarget.Source ? var2.getInputStructure(this.tx.getID()) : var2.getOutputStructure(this.tx.getID());
      }

      private CROM_Tree createNewTree() throws MappingTreeStructureException {
         CromTreeFactory var1 = new CromTreeFactory(this.tx, this.st);
         return var1.createTree();
      }

      protected void onSuccess(CROM_Tree var1) {
         if (var1 != null) {
            ChunkNodeSelector.this.tree.setCromTree(var1);
            if (!StringUtils.isNotEmpty(ChunkNodeSelector.this.initialSelection)) {
               ChunkNodeSelector.this.tree.expandRoot();
            }
         } else {
            ChunkNodeSelector.this.tree.removeWaitNode();
         }
      }
   }
}
