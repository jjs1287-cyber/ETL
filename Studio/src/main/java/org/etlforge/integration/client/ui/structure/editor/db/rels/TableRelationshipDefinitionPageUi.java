package org.etlforge.integration.client.ui.structure.editor.db.rels;

import com.google.common.base.Predicate;
import java.awt.EventQueue;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.tree.TreePath;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.database.ui.dnd.TransferableDatabaseObjects;
import org.etlforge.integration.client.ui.structure.editor.resources.StructureEditorResources;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.structure.database.TableRelationship;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.Enablabes;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class TableRelationshipDefinitionPageUi implements UiProvider {
   private final DatabaseObject[] tables;
   private final Relations relations;
   private final SourceTarget sourceTarget;
   private final ParentChildSelector parentChildSelector;
   private final RelationshipTree tree;
   private final TableRelationshipDefinitionPageUi.AddRelationAction addRelationAction;
   private final TableRelationshipDefinitionPageUi.EditJoinPropertiesAction editJoinPropertiesAction;
   private final TableRelationshipDefinitionPageUi.RemoveRelationAction removeRelationAction;

   public TableRelationshipDefinitionPageUi(DatabaseObject[] var1, Relations var2, SourceTarget var3) {
      this.relations = var2;
      this.tables = var1;
      this.sourceTarget = var3;
      this.addRelationAction = new TableRelationshipDefinitionPageUi.AddRelationAction();
      this.parentChildSelector = new ParentChildSelector(this.tables, new JoinChecker(var2));
      this.parentChildSelector.setEnabledStateTracker(Enablabes.fromAction(this.addRelationAction));
      this.tree = this.createTree();
      this.editJoinPropertiesAction = new TableRelationshipDefinitionPageUi.EditJoinPropertiesAction();
      this.removeRelationAction = new TableRelationshipDefinitionPageUi.RemoveRelationAction();
      this.removeRelationAction.setSelectionSource(this.tree);
      this.tree.setDefaultAction(this.editJoinPropertiesAction);
      this.tree.installKeybindings(this.editJoinPropertiesAction, this.removeRelationAction);
   }

   private RelationshipTree createTree() {
      RelationshipTree var1 = new RelationshipTree(this.tables, this.areMultipleRootsAllowed());
      var1.setTableNameDecorator(new TableNameJoinTypeDecorator(this.relations));
      var1.setTransferHandler(new TableRelationshipDefinitionPageUi.DragAndDropHandler());
      return var1;
   }

   private boolean areMultipleRootsAllowed() {
      return this.sourceTarget == SourceTarget.Target;
   }

   @Override
   public void decorate(UiCanvas var1) {
      new TableRelationshipDefinitionPageUi.Layout().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.parentChildSelector.requestFocus();
   }

   public void relationsChanged() {
      this.tree.update(this.relations.getAllRelationships());
      this.parentChildSelector.repaint();
   }

   private boolean defineProperties(TableRelationship var1) {
      final JoinPropertiesUi var2 = new JoinPropertiesUi(var1);
      WizardStyleDialog var3 = new WizardStyleDialog(UiUtils.getActiveWindow(), StructureEditorResources.getString("Db.Relations.JoinProperties"), var2) {
         @Override
         protected void onOk() {
            if (var2.checkInput()) {
               super.onOk();
            }
         }
      };
      var3.setFocusedComponent(var2);
      var3.manageLocation(JoinPropertiesUi.class);
      var3.setOkButtonAsDefault();
      var3.setVisible(true);
      if (var3.wasOkPressed()) {
         var2.apply();
         this.tree.refresh();
         return true;
      } else {
         return false;
      }
   }

   private class AddRelationAction extends AbstractAction {
      public AddRelationAction() {
         super(StructureEditorResources.getString("Db.Relations.Join"), new TableJoinIcon());
         this.putValue("ShortDescription", StructureEditorResources.getString("Db.Relations.Join.ToolTip"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         TableRelationship var2 = TableRelationshipDefinitionPageUi.this.parentChildSelector.getSelectedRelationship();
         if (var2 != null) {
            TableRelationship var3 = TableRelationshipDefinitionPageUi.this.relations.getRelation(var2.getParentObject(), var2.getChildObject());
            if (var3 != null) {
               var2 = var3;
            }

            this.defineJoinProperties(var2);
         }
      }

      private void defineJoinProperties(TableRelationship var1) {
         boolean var2 = TableRelationshipDefinitionPageUi.this.defineProperties(var1);
         if (var2) {
            TableRelationshipDefinitionPageUi.this.relations.add(var1);
            TableRelationshipDefinitionPageUi.this.relationsChanged();
         }
      }
   }

   private final class DragAndDropHandler extends TransferHandler {
      private DatabaseObject target;
      private DatabaseObject dragged;

      private DragAndDropHandler() {
      }

      @Override
      public boolean canImport(TransferSupport var1) {
         if (!var1.isDataFlavorSupported(TransferableDatabaseObjects.FLAVOR)) {
            return false;
         }

         try {
            this.target = this.getDropTarget(var1);
            return this.target == null || !TableRelationshipDefinitionPageUi.this.relations.isAncestor(this.dragged, this.target);
         } catch (Exception var3) {
            var3.printStackTrace();
            return false;
         }
      }

      private DatabaseObject getDropTarget(TransferSupport var1) {
         JTree.DropLocation var2 = (JTree.DropLocation)var1.getDropLocation();
         ((KongaTree)var1.getComponent()).autoscroll(var2.getDropPoint());
         TreePath var3 = var2.getPath();
         if (var3 == null) {
            return null;
         }

         KongaTreeNode var4 = (KongaTreeNode)var3.getLastPathComponent();
         return var4 != null ? (DatabaseObject)var4.getUserObject() : null;
      }

      @Override
      protected Transferable createTransferable(JComponent var1) {
         List var2 = ((KongaTree)var1).getSelectedNodes();
         if (var2.size() != 1) {
            return null;
         }

         this.dragged = (DatabaseObject)((KongaTreeNode)var2.get(0)).getUserObject();
         return new TransferableDatabaseObjects(Collections.singletonList(this.dragged));
      }

      @Override
      public int getSourceActions(JComponent var1) {
         return 1073741824;
      }

      @Override
      public boolean importData(TransferSupport var1) {
         if (this.target == null) {
            TableRelationshipDefinitionPageUi.this.relations.removeFromParent(this.dragged);
            TableRelationshipDefinitionPageUi.this.relationsChanged();
         } else {
            this.defineJoinPropeties();
         }

         return true;
      }

      private void defineJoinPropeties() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               TableRelationship var1 = new TableRelationship(DragAndDropHandler.this.target, DragAndDropHandler.this.dragged);
               if (TableRelationshipDefinitionPageUi.this.defineProperties(var1)) {
                  TableRelationshipDefinitionPageUi.this.relations.add(var1);
                  TableRelationshipDefinitionPageUi.this.relationsChanged();
               }
            }
         });
      }
   }

   private class EditJoinPropertiesAction extends AbstractSingleTypeSelectionAction<DatabaseObject> implements Predicate<List<DatabaseObject>> {
      public EditJoinPropertiesAction() {
         super(DatabaseObject.class, StructureEditorResources.getString("Db.Relations.Edit"));
         this.putValue("ShortDescription", StructureEditorResources.getString("Db.Relations.Edit.ToolTip"));
         this.putValue("AcceleratorKey", KeyUtils.getCtrlDown(69));
         this.addSelectionPredicate(this);
      }

      @Override
      protected void run(List<DatabaseObject> var1) {
         if (var1.size() == 2) {
            DatabaseObject var2 = (DatabaseObject)var1.get(0);
            DatabaseObject var3 = (DatabaseObject)var1.get(1);
            if (TableRelationshipDefinitionPageUi.this.relations.getParent(var2) == var3) {
               this.defineLinkKeys(var3, var2);
            } else if (TableRelationshipDefinitionPageUi.this.relations.getParent(var3) == var2) {
               this.defineLinkKeys(var2, var3);
            }
         } else if (var1.size() == 1) {
            DatabaseObject var4 = (DatabaseObject)var1.get(0);
            DatabaseObject var5 = TableRelationshipDefinitionPageUi.this.relations.getParent(var4);
            if (var5 != null) {
               this.defineLinkKeys(var5, var4);
            }
         }
      }

      public boolean apply(List<DatabaseObject> var1) {
         if (var1.size() != 2) {
            return var1.size() == 1 ? TableRelationshipDefinitionPageUi.this.relations.getParent((DatabaseObject)var1.get(0)) != null : false;
         }

         DatabaseObject var2 = (DatabaseObject)var1.get(0);
         DatabaseObject var3 = (DatabaseObject)var1.get(1);
         return TableRelationshipDefinitionPageUi.this.relations.getParent(var2) == var3
            || TableRelationshipDefinitionPageUi.this.relations.getParent(var3) == var2;
      }

      private void defineLinkKeys(DatabaseObject var1, DatabaseObject var2) {
         TableRelationship var3 = TableRelationshipDefinitionPageUi.this.relations.getRelation(var1, var2);
         TableRelationshipDefinitionPageUi.this.defineProperties(var3);
      }
   }

   private class Layout {
      private final BorderLayoutBuilder layout = new BorderLayoutBuilder(0, 10);

      public Layout() {
         this.layoutControls();
         this.layoutTree();
      }

      private void layoutControls() {
         BoxBuilder var1 = BoxBuilder.horizontal();
         KongaButton var2 = new KongaButton(TableRelationshipDefinitionPageUi.this.addRelationAction);
         var2.setIconTextGap(8);
         var1.addAllWithSpace(10, TableRelationshipDefinitionPageUi.this.parentChildSelector, var2);
         this.layout.north(var1);
      }

      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }

      private void layoutTree() {
         BoxBuilder var1 = BoxBuilder.horizontal()
            .addAllWithSpace(10, TableRelationshipDefinitionPageUi.this.editJoinPropertiesAction, TableRelationshipDefinitionPageUi.this.removeRelationAction);
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 5);
         var2.center(TableRelationshipDefinitionPageUi.this.tree).south(var1.invisible());
         this.layout.center(var2);
      }
   }

   private class RemoveRelationAction extends AbstractSingleObjectSelectionAction<DatabaseObject> {
      public RemoveRelationAction() {
         super(DatabaseObject.class, StructureEditorResources.getString("Db.Relations.RemoveJoin"));
         this.putValue("ShortDescription", StructureEditorResources.getString("Db.Relations.RemoveJoin.ToolTip"));
         this.putValue("AcceleratorKey", KeyUtils.DELETE);
      }

      protected boolean isEnabledFor(DatabaseObject var1) {
         return TableRelationshipDefinitionPageUi.this.relations.getParent(var1) != null;
      }

      protected void run(DatabaseObject var1, ActionEvent var2) {
         TableRelationshipDefinitionPageUi.this.relations.removeFromParent(var1);
         TableRelationshipDefinitionPageUi.this.relationsChanged();
      }
   }
}
