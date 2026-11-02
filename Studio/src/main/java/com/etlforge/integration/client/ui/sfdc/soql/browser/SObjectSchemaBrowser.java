package com.etlforge.integration.client.ui.sfdc.soql.browser;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.etlforge.integration.salesforce.ui.NameDisplayModes;
import com.etlforge.integration.salesforce.ui.renderer.Renderers;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.integration.salesforce.wsdl.ObjectSchemaLookup;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.event.DoubleClick;
import org.etlforge.ui.event.Mouse;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.PopupMenuRequestHandlers;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.TreeSelectionSource;
import org.etlforge.util.ObservableValue;

public final class SObjectSchemaBrowser implements Disposable, UiProvider {
   private final ObjectSchemaLookup schemaLookup;
   private final ObservableValue<SObjectSchema> object;
   private final NameDisplayModes nameDisplayModes;
   private final DefaultKongaTreeModel treeModel = new DefaultKongaTreeModel(new DefaultKongaTreeNode("Root", true));
   private final KongaTree tree;
   private final SchemaBrowserFilterUi filterUi;
   private final SObjectSchemaBrowser.ObjectAction objectAction = new SObjectSchemaBrowser.ObjectAction();
   private final SObjectSchemaBrowser.FieldAction fieldAction = new SObjectSchemaBrowser.FieldAction();
   private final SObjectSchemaBrowser.EnterAction enterAction = new SObjectSchemaBrowser.EnterAction();
   private final List<SchemaBrowserActionListener> actionListeners = Lists.newCopyOnWriteArrayList();
   private final ObservableValue.Listener<SObjectSchema> objectListener = new ObservableValue.Listener<SObjectSchema>() {
      public void valueChanged(ObservableValue<? extends SObjectSchema> var1, SObjectSchema var2, SObjectSchema var3) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               SObjectSchemaBrowser.this.populateModel();
            }
         });
      }
   };

   public SObjectSchemaBrowser(ObjectSchemaLookup var1, ObservableValue<SObjectSchema> var2, NameDisplayModes var3) {
      this.schemaLookup = var1;
      this.object = var2;
      this.nameDisplayModes = var3;
      this.tree = this.createTree();
      this.filterUi = new SchemaBrowserFilterUi(this.tree, var2);
      this.object.addListener(this.objectListener);
      this.installActions();
      this.populateModel();
   }

   private KongaTree createTree() {
      KongaTree var1 = new KongaTree(this.treeModel);
      var1.setRootVisible(false);
      var1.setShowsRootHandles(true);
      var1.addTreeWillExpandListener(new SObjectSchemaBrowser.ExpansionListener());
      var1.setTransferHandler(new SchemaBrowserTransferHandler());
      var1.setDragEnabled(true);
      var1.setCellRenderer(Renderers.forTree(this.nameDisplayModes));
      return var1;
   }

   private void installActions() {
      TreeSelectionSource var1 = new TreeSelectionSource(this.tree);
      this.fieldAction.setSelectionSource(var1);
      this.objectAction.setSelectionSource(var1);
      this.enterAction.setSelectionSource(var1);
      ActionKeyBinding.install(this.enterAction, this.tree);
      PopupMenuRequestHandlers.install(this.tree, this.objectAction, this.fieldAction);
      DoubleClick.on(this.tree).when(Mouse.onTreeRow(this.tree)).toInvoke(this.fieldAction);
   }

   private void populateModel() {
      KongaTreeNode var1 = this.treeModel.getRoot();
      ((DefaultKongaTreeNode)var1).removeAllChildren();
      boolean var2 = false;
      if (this.object.get() != null) {
         RootObjectNode var3 = new RootObjectNode(var1, this.object.get());

         for (KongaTreeNode var6 : this.buildChildNodes(this.object.get(), true)) {
            var3.addChild(var6);
         }

         var2 = true;
      }

      this.treeModel.fireTreeStructureChanged(this.treeModel, this.treeModel.getPath(var1));
      if (var2) {
         this.tree.expandRow(0);
      }
   }

   private Iterable<KongaTreeNode> buildChildNodes(SObjectSchema var1, boolean var2) {
      TreeSet var3 = Sets.newTreeSet(Ordering.<String>natural().onResultOf(this.nodeToName()));

      for (SObjectSchema.Field var5 : var1.getFields()) {
         var3.add(new FieldNode(var5));
      }

      for (SObjectSchema.Parent var8 : var1.getParents()) {
         var3.add(new ParentRelationshipNode(var8.getName(), var8.getObjectTypeInQuery()));
      }

      if (var2) {
         for (SObjectSchema.Child var9 : var1.getChildren()) {
            var3.add(new ChildRelationshipNode(var9.getName(), var9.getObjectType()));
         }
      }

      return var3;
   }

   @Override
   public void dispose() {
      this.object.removeListener(this.objectListener);
   }

   @Override
   public void decorate(UiCanvas var1) {
      new BorderLayoutBuilder(0, 5).center(this.tree).south(this.filterUi).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   public void addActionListener(SchemaBrowserActionListener var1) {
      this.actionListeners.add(var1);
   }

   public void removeActionListener(SchemaBrowserActionListener var1) {
      this.actionListeners.remove(var1);
   }

   public SObjectSchemaBrowser withVisibleRowCount(int var1) {
      this.tree.setVisibleRowCount(var1);
      return this;
   }

   private Function<KongaTreeNode, String> nodeToName() {
      return new Function<KongaTreeNode, String>() {
         public String apply(KongaTreeNode var1) {
            return var1 instanceof FieldNode
               ? SObjectSchemaBrowser.this.nameDisplayModes.field.getDisplayName(((FieldNode)var1).getUserObject())
               : var1.getDisplayName();
         }
      };
   }

   private class EnterAction extends AbstractSingleTypeSelectionAction<KongaTreeNode> {
      public EnterAction() {
         super(KongaTreeNode.class, "&Insert Selected Object");
         this.putValue("SmallIcon", ApplicationResources.ICONS.getIcon("ArrowLeft.16"));
         this.putValue("AcceleratorKey", KeyUtils.ENTER);
      }

      @Override
      protected boolean isEnabledFor(List<KongaTreeNode> var1) {
         if (var1.isEmpty()) {
            return false;
         }

         KongaTreeNode var2 = null;
         boolean var3 = false;

         for (KongaTreeNode var5 : var1) {
            Object var6 = var5.getUserObject();
            if (var6 instanceof SObjectSchema) {
               var3 = true;
               if (var2 != null) {
                  return false;
               }

               var2 = var5;
            } else if (var6 instanceof SObjectSchema.Field) {
               if (var3) {
                  return false;
               }

               KongaTreeNode var7 = var5.getParent();
               if (var2 == null) {
                  var2 = var7;
               } else if (var2 != var7) {
                  return false;
               }
            }
         }

         return true;
      }

      @Override
      protected void run(List<KongaTreeNode> var1) {
         if (SObjectSchemaBrowser.this.objectAction.isEnabled()) {
            ActionUtils.run(SObjectSchemaBrowser.this.objectAction, SObjectSchemaBrowser.this.tree, "ENTER");
         } else if (SObjectSchemaBrowser.this.fieldAction.isEnabled()) {
            ActionUtils.run(SObjectSchemaBrowser.this.fieldAction, SObjectSchemaBrowser.this.tree, "ENTER");
            SObjectSchemaBrowser.this.tree.requestFocusInWindow();
         }
      }
   }

   private class ExpansionListener implements TreeWillExpandListener {
      private ExpansionListener() {
      }

      @Override
      public void treeWillExpand(TreeExpansionEvent var1) throws ExpandVetoException {
         RelationshipNode var2 = this.getNodeToExpand(var1);
         if (var2 != null && var2.getChildCount() == 0) {
            this.expandNode(var2);
         }
      }

      private RelationshipNode getNodeToExpand(TreeExpansionEvent var1) {
         TreePath var2 = var1.getPath();
         Object var3 = var2.getLastPathComponent();
         return var3 instanceof RelationshipNode ? (RelationshipNode)var3 : null;
      }

      private void expandNode(RelationshipNode var1) {
         SObjectSchemaBrowser.this.treeModel.insertNode(new SObjectSchemaBrowser.WaitNode(), var1);
         SObjectSchemaBrowser.NodeBuilder var2 = SObjectSchemaBrowser.this.new NodeBuilder(var1.getObjectType());
         SObjectSchemaBrowser.NodeInserter var3 = SObjectSchemaBrowser.this.new NodeInserter(var1);
         Computation.create(var2, var3).swingWorkerBuilder().buildAndExecute();
      }

      @Override
      public void treeWillCollapse(TreeExpansionEvent var1) throws ExpandVetoException {
      }
   }

   private class FieldAction extends AbstractSingleTypeSelectionAction<FieldNode> {
      public FieldAction() {
         super(FieldNode.class, "Insert Field");
      }

      @Override
      protected void run(List<FieldNode> var1) {
         if (!SObjectSchemaBrowser.this.actionListeners.isEmpty()) {
            ImmutableList var2 = FluentIterable.from(var1).transform(new Function<FieldNode, String>() {
               public String apply(FieldNode var1) {
                  return var1.getSoqlRepresentation();
               }
            }).toImmutableList();
            Optional var3 = this.getObjectName(var1);

            for (SchemaBrowserActionListener var5 : SObjectSchemaBrowser.this.actionListeners) {
               var5.fieldsSelected(var3, var2);
            }
         }
      }

      private Optional<String> getObjectName(List<FieldNode> var1) {
         String var2 = null;

         for (FieldNode var4 : var1) {
            KongaTreeNode var5 = var4.getParent();
            String var6 = null;
            if (var5 instanceof RootObjectNode) {
               var6 = ((SObjectSchema)var5.getUserObject()).getName();
            } else if (var5 instanceof ChildRelationshipNode) {
               var6 = ((ChildRelationshipNode)var5).getObjectType();
            }

            if (var6 != null) {
               if (var2 != null) {
                  return Optional.absent();
               }

               var2 = var6;
            }
         }

         return Optional.fromNullable(var2);
      }
   }

   private class NodeBuilder implements Callable<Iterable<KongaTreeNode>> {
      private final String objectType;

      public NodeBuilder(String var2) {
         this.objectType = var2;
      }

      public Iterable<KongaTreeNode> call() throws Exception {
         SObjectSchema var1 = this.lookupSchema();
         boolean var2 = false;
         return SObjectSchemaBrowser.this.buildChildNodes(var1, var2);
      }

      private SObjectSchema lookupSchema() throws SalesforceException {
         return SObjectSchemaBrowser.this.schemaLookup.apply(this.objectType);
      }
   }

   private class NodeInserter implements Receiver<Iterable<KongaTreeNode>> {
      private final KongaTreeNode parentNode;

      public NodeInserter(KongaTreeNode var2) {
         this.parentNode = var2;
      }

      public void handle(Iterable<KongaTreeNode> var1) {
         this.parentNode.remove(0);

         for (KongaTreeNode var3 : var1) {
            this.parentNode.addChild(var3);
         }

         SObjectSchemaBrowser.this.treeModel
            .fireTreeStructureChanged(SObjectSchemaBrowser.this.treeModel, SObjectSchemaBrowser.this.treeModel.getPath(this.parentNode));
      }
   }

   private class ObjectAction extends AbstractSingleObjectSelectionAction<KongaTreeNode> {
      public ObjectAction() {
         super(KongaTreeNode.class, "Insert Object");
      }

      protected boolean isEnabledFor(KongaTreeNode var1) {
         return var1 instanceof RootObjectNode || var1 instanceof ChildRelationshipNode;
      }

      protected void run(KongaTreeNode var1, ActionEvent var2) {
         String var3 = this.getNameFromNode(var1);

         for (SchemaBrowserActionListener var5 : SObjectSchemaBrowser.this.actionListeners) {
            var5.objectSelected(var3);
         }
      }

      private String getNameFromNode(KongaTreeNode var1) {
         return var1 instanceof RootObjectNode ? ((RootObjectNode)var1).getUserObject().getName() : var1.getDisplayName();
      }
   }

   private static class WaitNode extends DefaultKongaTreeNode {
      public WaitNode() {
         super("Retrieving definitions...", false);
      }
   }
}
