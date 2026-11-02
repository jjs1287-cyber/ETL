package org.etlforge.integration.client.database.ui.sqlbuilder.tree;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.JXTreeTable.TreeTableHacker;
import org.jdesktop.swingx.JXTreeTable.TreeTableHackerExt;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.integration.client.database.ui.TableIcon;
import org.etlforge.integration.client.database.ui.resources.DatabaseUiResources;
import org.etlforge.integration.client.database.ui.sqlbuilder.DbObjectsController;
import org.etlforge.integration.client.database.ui.sqlbuilder.DbObjectsModel;
import org.etlforge.integration.client.database.ui.sqlbuilder.DbObjectsModelListener;
import org.etlforge.integration.client.database.ui.sqlbuilder.DbObjectsView;
import org.etlforge.integration.client.database.ui.sqlbuilder.DbObjectsViewSelection;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.event.DoubleClick;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.selection.ListSelectionDispatcher;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.util.KongaIconValues;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.progress.ProgressMessage;
import org.etlforge.ui.widget.DefaultComponentAction;
import org.etlforge.ui.widget.KongaTreeTable;

public class DbObjectsTree implements DbObjectsView {
   private final DbObjectsModel model;
   private final JXTreeTable treeTable;
   private final FilterControl filterControl;
   private DbObjectsController controller;
   private final DbObjectsTree.DownloadAction downloadAction;
   private final DbObjectsTree.CancelDownloadAction cancelDownloadAction;
   private final DbObjectsTree.TreeExpansionListenerImpl expansionListener;
   private DownloadingIndicator downloadingIndicator;
   private final DbObjectsTree.SelectionDispatcher selectionDispatcher = new DbObjectsTree.SelectionDispatcher();

   public DbObjectsTree(DbObjectsModel var1) {
      this.model = var1;
      var1.addModelListener(new DbObjectsTree.ModelListener());
      this.expansionListener = new DbObjectsTree.TreeExpansionListenerImpl();
      this.treeTable = this.createTreeTable();
      this.filterControl = new FilterControl(this.treeTable);
      this.downloadAction = new DbObjectsTree.DownloadAction(this.filterControl);
      this.filterControl.installDefaultAction(this.downloadAction);
      this.cancelDownloadAction = new DbObjectsTree.CancelDownloadAction();
   }

   private JXTreeTable createTreeTable() {
      RootNode var1 = new RootNode(this.model.getProjectItem());
      DbObjectsTreeModel var2 = new DbObjectsTreeModel(var1);
      DbObjectsTree.TreeTableImpl var3 = new DbObjectsTree.TreeTableImpl(var2);
      var3.setRootVisible(true);
      var3.addTreeExpansionListener(this.expansionListener);
      var3.getSelectionModel().addListSelectionListener(new DbObjectsTree.SelectionDispatcher());
      return var3;
   }

   public void setController(DbObjectsController var1) {
      this.controller = var1;
      this.downloadAction.setController(var1);
      this.cancelDownloadAction.setController(var1);
      var1.setView(this);
   }

   public void setDefaultAction(Action var1) {
      DefaultComponentAction.bind(this.treeTable, var1);
      this.installDoubleClick(var1);
      if (var1 instanceof SelectionAction) {
         ((SelectionAction)var1).setSelectionSource(this);
      }
   }

   private void installDoubleClick(Action var1) {
      Predicate var2 = new Predicate<MouseEvent>() {
         public boolean apply(MouseEvent var1) {
            TreePath var2x = DbObjectsTree.this.treeTable.getPathForLocation(var1.getX(), var1.getY());
            return var2x.getLastPathComponent() instanceof ColumnNode;
         }
      };
      DoubleClick.on(this.treeTable).when(var2).toInvoke(var1);
   }

   public void setVisibleRowCount(int var1) {
      this.treeTable.setVisibleRowCount(var1);
   }

   @Override
   public DbObjectsModel getModel() {
      return this.model;
   }

   @Override
   public DbObjectsViewSelection getSelection() {
      List var1 = this.getSelectedItemsOfType(DatabaseObject.class);
      List var2 = this.getSelectedItemsOfType(DatabaseColumn.class);
      return new DbObjectsViewSelection(var1, var2);
   }

   private <T> List<T> getSelectedItemsOfType(Class<T> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (int var6 : this.treeTable.getSelectedRows()) {
         Object var7 = this.treeTable.getValueAt(var6, 0);
         if (var1.isInstance(var7)) {
            var2.add(var1.cast(var7));
         }
      }

      return var2;
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.selectionDispatcher.addListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.selectionDispatcher.removeListener(var1);
   }

   @Override
   public void setDownloadingTables(final boolean var1) {
      UiUtils.runOnEventThread(
         new Runnable() {
            @Override
            public void run() {
               DbObjectsTree.this.downloadAction.setAvailable(!var1);
               DbObjectsTree.this.cancelDownloadAction.setAvailable(var1);
               if (var1) {
                  DbObjectsTree.this.downloadingIndicator = new DownloadingIndicator(
                     DbObjectsTree.this.treeTable, (RootNode)DbObjectsTree.this.treeTable.getTreeTableModel().getRoot()
                  );
                  DbObjectsTree.this.downloadingIndicator.start();
               } else if (DbObjectsTree.this.downloadingIndicator != null) {
                  DbObjectsTree.this.downloadingIndicator.stop();
                  DbObjectsTree.this.downloadingIndicator = null;
               }
            }
         }
      );
   }

   @Override
   public void setDownloadingColumns(final DatabaseObject var1, final boolean var2) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DbObjectsTree.this.downloadAction.setAvailable(!var2);
            DbObjectsTree.this.cancelDownloadAction.setAvailable(var2);
            if (var2) {
               RootNode var1x = (RootNode)DbObjectsTree.this.treeTable.getTreeTableModel().getRoot();

               for (int var2x = 0; var2x < var1x.getChildCount(); var2x++) {
                  AbstractMutableTreeTableNode var3 = (AbstractMutableTreeTableNode)var1x.getChildAt(var2x);
                  if (var3.getUserObject() == var1) {
                     DbObjectsTree.this.downloadingIndicator = new DownloadingIndicator(DbObjectsTree.this.treeTable, (DatabaseTableNode)var3);
                     DbObjectsTree.this.downloadingIndicator.start();
                  }
               }
            } else if (DbObjectsTree.this.downloadingIndicator != null) {
               DbObjectsTree.this.downloadingIndicator.stop();
               DbObjectsTree.this.downloadingIndicator = null;
            }
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      BoxBuilder var3 = BoxBuilder.horizontal();
      var3.addAllWithSpace(10, this.downloadAction, this.cancelDownloadAction, this.filterControl).glue();
      var2.center(this.treeTable).south(var3);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.treeTable.requestFocusInWindow();
   }

   private static class CancelDownloadAction extends AbstractAction {
      private DbObjectsController controller;

      public CancelDownloadAction() {
         super("&Cancel Download");
         this.setEnabled(false);
      }

      public void setController(DbObjectsController var1) {
         this.controller = var1;
      }

      public void setAvailable(boolean var1) {
         this.setEnabled(var1 && this.controller != null);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.controller.cancelDownload();
      }
   }

   private static class DownloadAction extends AbstractAction {
      private DbObjectsController controller;
      private final FilterControl filterControl;

      public DownloadAction(FilterControl var1) {
         super("&Download");
         this.filterControl = var1;
         this.setEnabled(false);
      }

      public void setController(DbObjectsController var1) {
         this.controller = var1;
         this.setEnabled(var1 != null);
      }

      public void setAvailable(boolean var1) {
         this.setEnabled(var1 && this.controller != null);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         TableSearchFilter var2 = this.filterControl.getInfoProviderFilter();
         this.controller.downloadTables(var2);
      }
   }

   private static class DownloadIndicatorHighlighter extends AbstractHighlighter {
      public DownloadIndicatorHighlighter() {
         super(new HighlightPredicate() {
            @Override
            public boolean isHighlighted(Component var1, ComponentAdapter var2) {
               return var2.getValue() instanceof ProgressMessage;
            }
         });
      }

      @Override
      protected Component doHighlight(Component var1, ComponentAdapter var2) {
         Fonts.convertToItalic(var1);
         return var1;
      }
   }

   private class ModelListener extends DbObjectsModelListener.Adapter {
      private ModelListener() {
      }

      @Override
      public void modelChanged(DbObjectsModel var1) {
         DbObjectsTree.TreeModelBuilder var2 = DbObjectsTree.this.new TreeModelBuilder(null);
         var2.execute();
      }

      @Override
      public void tableChanged(DbObjectsModel var1, DatabaseObject var2) {
         DbObjectsTree.TreeModelBuilder var3 = DbObjectsTree.this.new TreeModelBuilder(var2);
         var3.execute();
      }
   }

   private class SelectionDispatcher extends ListSelectionDispatcher {
      private SelectionDispatcher() {
      }

      @Override
      protected Selection getSelection(ListSelectionEvent var1) {
         return DbObjectsTree.this.getSelection();
      }
   }

   private class TreeExpansionListenerImpl implements TreeExpansionListener {
      public boolean blocked;

      private TreeExpansionListenerImpl() {
      }

      @Override
      public void treeExpanded(TreeExpansionEvent var1) {
         if (!this.blocked) {
            Object var2 = this.getNode(var1);
            if (var2 instanceof TreeTableNode) {
               TreeTableNode var3 = (TreeTableNode)var2;
               if (var3.getChildCount() > 0) {
                  return;
               }

               Object var4 = var3.getUserObject();
               if (var4 instanceof DatabaseObject) {
                  DatabaseObject var5 = (DatabaseObject)var4;
                  DbObjectsTree.this.controller.downloadColumns(var5);
               }
            }
         }
      }

      private Object getNode(TreeExpansionEvent var1) {
         TreePath var2 = var1.getPath();
         return var2.getLastPathComponent();
      }

      @Override
      public void treeCollapsed(TreeExpansionEvent var1) {
      }
   }

   private class TreeModelBuilder extends KongaSwingWorker<DbObjectsTreeModel, Void> {
      private final DatabaseObject tableToExpand;

      public TreeModelBuilder(DatabaseObject var2) {
         this.tableToExpand = var2;
      }

      protected DbObjectsTreeModel doInBackground() throws Exception {
         RootNode var1 = new RootNode(DbObjectsTree.this.model.getProjectItem());

         for (DatabaseObject var4 : this.getTablesInOrder()) {
            this.addTableToTree(var1, var4);
         }

         return new DbObjectsTreeModel(var1);
      }

      private List<DatabaseObject> getTablesInOrder() {
         ArrayList var1 = Lists.newArrayList(DbObjectsTree.this.model.getTables());
         Collections.sort(var1, new Comparator<DatabaseObject>() {
            public int compare(DatabaseObject var1, DatabaseObject var2) {
               int var3 = var1.getType().compareTo(var2.getType());
               if (var3 == 0) {
                  var3 = var1.getName().compareToIgnoreCase(var2.getName());
               }

               return var3;
            }
         });
         return var1;
      }

      private void addTableToTree(RootNode var1, DatabaseObject var2) {
         DatabaseTableNode var3 = new DatabaseTableNode(var2);
         var1.add(var3);

         for (DatabaseColumn var6 : DbObjectsTree.this.model.getColumns(var2)) {
            var3.add(new ColumnNode(var6));
         }
      }

      protected void onSuccess(DbObjectsTreeModel var1) {
         DbObjectsTree.this.expansionListener.blocked = true;
         DbObjectsTree.this.treeTable.setTreeTableModel(var1);
         if (this.tableToExpand != null) {
            int var2 = var1.getChildCount(var1.getRoot());

            for (int var3 = 0; var3 < var2; var3++) {
               DatabaseTableNode var4 = (DatabaseTableNode)var1.getChild(var1.getRoot(), var3);
               if (var4.getUserObject() == this.tableToExpand) {
                  DbObjectsTree.this.treeTable.expandRow(1 + var3);
                  break;
               }
            }
         }

         DbObjectsTree.this.expansionListener.blocked = false;
      }
   }

   private static class TreeTableImpl extends KongaTreeTable {
      private final TableCellRenderer rootRenderer = this.createRootRenderer();
      private final TableCellRenderer pkRenderer = this.createPrimaryKeyRenderer();
      private final TableCellRenderer indicatorRenderer = this.createIndicatorRenderer();

      public TreeTableImpl(TreeTableModel var1) {
         super(var1);
         this.setOpenIcon(new TableIcon());
         this.setClosedIcon(new TableIcon());
         this.setLeafIcon(null);
         this.addHighlighter(new DbObjectsTree.DownloadIndicatorHighlighter());
         this.setDragEnabled(true);
         this.setTransferHandler(new TreeTransferHandler());
         this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent var1) {
               if (TreeTableImpl.this.getSelectedRowCount() > 0) {
                  TreeTableImpl.this.getTransferHandler().exportAsDrag(TreeTableImpl.this, var1, 1);
               }
            }
         });
         this.setStyle(TableStyles.normal());
      }

      private TableCellRenderer createRootRenderer() {
         StringValue var1 = new StringValue() {
            @Override
            public String getString(Object var1) {
               return ((IntegrationEntity)var1).getName();
            }
         };
         IconValue var2 = new IconValue() {
            @Override
            public Icon getIcon(Object var1) {
               EntityType var2x = ((IntegrationEntity)var1).getEntityType();
               return EntityIcons.forType(var2x).getDefaultIcon(EntityIcons.Size.SMALL);
            }
         };
         return new DefaultTableRenderer(var1, var2);
      }

      private TableCellRenderer createPrimaryKeyRenderer() {
         StringValue var1 = StringValues.EMPTY;
         IconValue var2 = new IconValue() {
            @Override
            public Icon getIcon(Object var1) {
               return Boolean.TRUE.equals(var1) ? DatabaseUiResources.PRIMARY_KEY_ICON : null;
            }
         };
         return new DefaultTableRenderer(var1, var2);
      }

      private TableCellRenderer createIndicatorRenderer() {
         StringValue var1 = StringValues.TO_STRING;
         IconValue var2 = KongaIconValues.empty(16, 16);
         return new DefaultTableRenderer(var1, var2);
      }

      @Override
      public TableCellRenderer getCellRenderer(int var1, int var2) {
         if (var1 == 0 && var2 == 0) {
            return this.rootRenderer;
         }

         if (var2 == 2 && var1 > 1) {
            return this.pkRenderer;
         }

         if (var2 == 0) {
            TreePath var3 = this.getPathForRow(var1);
            if (var3.getLastPathComponent() instanceof DownloadingIndicatorNode) {
               return this.indicatorRenderer;
            }
         }

         return super.getCellRenderer(var1, var2);
      }

      @Override
      protected TreeTableHacker createTreeTableHacker() {
         return new TreeTableHackerExt() {
            @Override
            protected boolean isHitDetectionFromProcessMouse() {
               return false;
            }
         };
      }
   }
}
