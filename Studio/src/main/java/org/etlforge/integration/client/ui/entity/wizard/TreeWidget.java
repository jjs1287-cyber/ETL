package org.etlforge.integration.client.ui.entity.wizard;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashSet;
import javax.accessibility.Accessible;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.tree.TreeModel;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.factory.Request;
import org.etlforge.integration.client.ui.entity.factory.Result;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.client.ui.entity.tree.DynamicTreeModelUpdater;
import org.etlforge.integration.client.ui.entity.tree.EntityTreeFilter;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModel;
import org.etlforge.integration.client.ui.entity.tree.ProjectTreeFragmentCreator;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.ProjectPropertyChangeSupport;
import org.etlforge.integration.uidelegate.entity.renderer.IntegrationEntityListDecorRenderer;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.lang.Intervals;
import org.etlforge.system.Disposable;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionDispatcher;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.combobox.AbstractTreeComboBoxModel;
import org.etlforge.ui.widget.combobox.DefaultTreeComboBoxRenderer;
import org.etlforge.ui.widget.combobox.TreeComboBox;
import org.etlforge.ui.widget.combobox.TreeComboBoxSelectionControl;
import org.etlforge.ui.widget.label.LabelUtils;
import org.etlforge.ui.widget.list.ActionListRenderer;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class TreeWidget<T extends IntegrationEntity> implements SelectionWidget<T> {
   private final IntegrationEntityTreeModel treeModel;
   private final EntityExplorerSupport explorer;
   private final Class<T> entityClass;
   private final SelectionAction editAction;
   private final Action createNewAction;
   private final JButton editButton;
   private EntityFilter filter = EntityFilter.ACCEPT_ALL;
   private final Disposable uninstaller;
   private Optional<UiEntityFactoryCallback> newItemHandler = Optional.absent();
   private TreeWidget<T>.ComboModelImpl comboModel;
   private final TreeComboBox comboBox;
   private final DefaultTreeComboBoxRenderer renderer;
   private final JLabel comboLabel;
   private final ProjectPropertyChangeSupport modificationListener;
   private final LinkedHashSet<IntegrationEntity> nonProjectItems;
   private final TreeWidget<T>.ComboRebuilder rebuilder;
   private final TreeWidget<T>.NonProjectItemDeleteListener nonProjectItemDeleteListener;
   private boolean editAutomaticallyOnSelection;
   private TreeWidget<T>.AutoEditorListener autoEditorListener;
   private boolean includeNoneItem;
   private final SelectionDispatcher selectionDispatcher = new SelectionDispatcher();
   private static final String PROMPT = "Select or Create New...";
   private static final String NONE = "None";

   public TreeWidget(EntityExplorerSupport var1, EntityType var2, Class<T> var3, EntityFilter var4) {
      this(var1, var2, var3, var4, Request.Source.OTHER);
   }

   public TreeWidget(EntityExplorerSupport var1, EntityType var2, Class<T> var3, EntityFilter var4, Request.Source var5) {
      this.explorer = var1;
      this.entityClass = var3;
      this.filter = var4 != null ? var4 : EntityFilter.ACCEPT_ALL;
      this.nonProjectItems = Sets.newLinkedHashSet();
      this.nonProjectItemDeleteListener = new TreeWidget.NonProjectItemDeleteListener();
      this.treeModel = ProjectTreeFragmentCreator.createTreeModel(var1.getProject(), var2);
      this.treeModel.setFilter(createTreeFilter(var4));
      this.uninstaller = new DynamicTreeModelUpdater(var1.getProject(), var2, this.treeModel);
      this.editAction = var1.getEditSelectedEntityAction(var2);
      this.createNewAction = var1.getCreateNewEntityAction(var2, var5, new TreeWidget.NewItemCallback());
      this.comboModel = new TreeWidget.ComboModelImpl(this.treeModel, false);
      this.comboBox = new TreeComboBox(this.comboModel);
      this.comboBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXxXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
      this.comboBox.setMaximumRowCount(Intervals.valueWithin(this.comboModel.getSize(), 8, 20));
      TreeComboBoxSelectionControl.install(this.comboBox);
      this.renderer = new DefaultTreeComboBoxRenderer(this.comboModel, new TreeWidget.RendererDelegate());
      this.comboBox.setRenderer(this.renderer);
      this.rebuilder = new TreeWidget.ComboRebuilder();
      this.modificationListener = new ProjectPropertyChangeSupport(var1.getProject(), var2, false);
      this.modificationListener.addPropertyChangeListener("IntegrationEntity#MofidiedAt", this.rebuilder);
      this.treeModel.addTreeModelListener(this.rebuilder);
      this.editAction.setSelectionSource(this.comboBox);
      this.comboLabel = new JLabel();
      this.comboLabel.setLabelFor(this.comboBox);
      this.editButton = new KongaButton(this.editAction);
      TreeWidget.CreateNewActionTrigger.install(this.comboBox, this.createNewAction);
      this.comboBox.addSelectionListener(this.selectionDispatcher);
      this.selectionDispatcher.addListener(new TreeWidget.ToolTipInstaller());
   }

   private static KongaTreeFilter createTreeFilter(EntityFilter var0) {
      return var0 != null ? new EntityTreeFilter(var0) : null;
   }

   @Override
   public void dispose() {
      this.modificationListener.dispose();
      this.uninstaller.dispose();

      for (IntegrationEntity var2 : this.nonProjectItems) {
         this.stopListeningToNonProjectItem(var2);
      }
   }

   private void stopListeningToNonProjectItem(IntegrationEntity var1) {
      var1.removePropertyChangeListener("IntegrationEntity#MofidiedAt", this.rebuilder);
      var1.removePropertyChangeListener("Deleted", this.nonProjectItemDeleteListener);
   }

   @Override
   public void decorate(UiCanvas var1) {
      new BorderLayoutBuilder(10, 1).north(this.comboLabel).center(this.comboBox).east(this.getEditButtonRepresentation()).decorate(var1);
   }

   private JComponent getEditButtonRepresentation() {
      return this.editAutomaticallyOnSelection ? new JLabel(" ") : this.editButton;
   }

   public JComponent[] getLayoutComponents(boolean var1) {
      JComponent var2 = this.getEditButtonRepresentation();
      return var1 ? new JComponent[]{this.comboLabel, this.comboBox, var2} : new JComponent[]{this.comboBox, var2};
   }

   public void layoutWithLabelInTwoColumns(TwoColumnLayout var1) {
      JComponent[] var2 = this.getLayoutComponents(true);
      JComponent var3 = var2[0];
      JPanel var4 = Horizontal.box().addAllWithSpace(5, var2[1], var2[2]).container();
      var1.addRow(var3, var4);
   }

   public JLabel getLabel() {
      return this.comboLabel;
   }

   @Override
   public void requestFocus() {
      this.comboBox.requestFocusInWindow();
   }

   public void setEnabled(boolean var1) {
      this.comboLabel.setEnabled(var1);
      this.comboBox.setEnabled(var1);
      this.editButton.setEnabled(var1 && this.editAction.isEnabled());
   }

   public void setEditAutomaticallyOnSelection(boolean var1) {
      if (var1 != this.editAutomaticallyOnSelection) {
         this.editAutomaticallyOnSelection = var1;
         if (var1) {
            this.autoEditorListener = new TreeWidget.AutoEditorListener();
            this.comboBox.addActionListener(this.autoEditorListener);
            if (this.getSelectedEntity() != null) {
               this.autoEditorListener.scheduleEdit();
            }
         } else {
            this.comboBox.removeActionListener(this.autoEditorListener);
            this.autoEditorListener = null;
         }
      }
   }

   public void setIncludeNoneItem(boolean var1) {
      if (this.includeNoneItem != var1) {
         this.includeNoneItem = var1;
         this.rebuild();
      }
   }

   @Override
   public void setFilter(EntityFilter var1) {
      this.filter = var1 == null ? EntityFilters.ACCEPTS_ALL : var1;
      this.treeModel.setFilter(createTreeFilter(var1));
      this.rebuild();
   }

   private void rebuild() {
      this.selectionDispatcher.setBlocked(true);
      Object var1 = this.comboModel.getSelectedItem();
      this.comboModel = new TreeWidget.ComboModelImpl(this.treeModel, this.includeNoneItem);
      this.renderer.setModel(this.comboModel);
      this.comboBox.setModel(this.comboModel);
      Object var2 = this.getItemToSelectAfterRebuild(var1);
      this.comboModel.setSelectedItem(var2);
      this.selectionDispatcher.setBlocked(false);
      if (var2 != var1) {
         SelectionChangedEvent var3 = new SelectionChangedEvent(this.comboBox, this.comboBox.getSelection());
         this.selectionDispatcher.selectionChanged(var3);
      }
   }

   private Object getItemToSelectAfterRebuild(Object var1) {
      for (int var2 = 0; var2 < this.comboModel.getSize(); var2++) {
         if (this.comboModel.getElementAt(var2) == var1) {
            return var1;
         }
      }

      return this.getNullSelection();
   }

   private Object getNullSelection() {
      return this.includeNoneItem ? "None" : "Select or Create New...";
   }

   @Override
   public boolean accepts(IntegrationEntity var1) {
      return this.entityClass.isInstance(var1) && this.filter.apply(var1);
   }

   @Override
   public void addSelectionListener(org.etlforge.ui.selection.SelectionListener var1) {
      this.selectionDispatcher.addListener(var1);
   }

   @Override
   public void removeSelectionListener(org.etlforge.ui.selection.SelectionListener var1) {
      this.selectionDispatcher.removeListener(var1);
   }

   @Override
   public T getSelectedEntity() {
      Object var1 = this.comboBox.getSelectedItem();
      return this.entityClass.isInstance(var1) && this.filter.apply((IntegrationEntity)var1) ? this.entityClass.cast(var1) : null;
   }

   @Override
   public void installUndoRedo(UndoRedoInstaller var1) {
      var1.install(this.comboBox);
   }

   @Override
   public void setNewEntityCallback(UiEntityFactoryCallback var1) {
      this.newItemHandler = Optional.of(var1);
   }

   public void clearSelection() {
      Object var1 = this.getNullSelection();
      this.comboBox.getModel().setSelectedItem(var1);
   }

   @Override
   public void setSelectedEntity(IntegrationEntityId var1) {
      Object var2 = this.explorer.getProject().getItemLookup().getEntity(var1);
      if (var2 == null) {
         var2 = this.getNullSelection();
      }

      this.comboBox.setSelectedItem(var2);
   }

   public void setSelectedEntity(IntegrationEntity var1) {
      this.comboBox.setSelectedItem(var1);
   }

   @Override
   public void setSelectorLabel(String var1) {
      MnemonicString var2 = ApplicationResources.MNEMONIC_FACTORY.fromString(var1);
      LabelUtils.applyMnemonics(this.comboLabel, var2);
   }

   public void addNonProjectItem(IntegrationEntity var1) {
      boolean var2 = this.nonProjectItems.add(var1);
      if (var2) {
         var1.addPropertyChangeListener("IntegrationEntity#MofidiedAt", this.rebuilder);
         var1.addPropertyChangeListener("Deleted", this.nonProjectItemDeleteListener);
         this.rebuild();
      }
   }

   public void selectOnlyAvailableItem() {
      if (this.comboModel.isOnlyOneItemAvailable()) {
         this.comboModel.selectFirstAvailableItem();
      }
   }

   public InputField asInputField(String var1) {
      return new ComboBoxInputField(var1, this.comboBox);
   }

   private final class AutoEditorListener implements ActionListener {
      private DelayedJob job;
      private IntegrationEntity displayedEntity;

      private AutoEditorListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.job != null) {
            this.job.cancel();
         }

         IntegrationEntity var2 = TreeWidget.this.getSelectedEntity();
         if (var2 != null) {
            if (var2 == this.displayedEntity) {
               ActionUtils.run(TreeWidget.this.editAction, this, "edit");
               this.displayedEntity = null;
            }

            this.displayedEntity = var2;
            if (!var2.isTransient()) {
               this.scheduleEdit();
            }
         }
      }

      public void scheduleEdit() {
         this.job = DelayedJob.createAndSchedule(250, new Runnable() {
            @Override
            public void run() {
               ActionUtils.run(TreeWidget.this.editAction, this, "edit");
               AutoEditorListener.this.displayedEntity = null;
            }
         });
      }
   }

   private final class ComboModelImpl extends AbstractTreeComboBoxModel {
      public ComboModelImpl(TreeModel var2, boolean var3) {
         super(var2, false);
         this.insertNonProjectItems();
         this.insertCreateNewAction();
         this.includeNoneItem(var3);
         this.setSelectedItem("Select or Create New...");
      }

      private void insertNonProjectItems() {
         int var1 = 0;

         for (IntegrationEntity var3 : TreeWidget.this.nonProjectItems) {
            this.insertItemAt(var3, var1);
            var1++;
         }
      }

      private void insertCreateNewAction() {
         boolean var1 = this.getSize() > 0;
         this.insertItemAt(TreeWidget.this.createNewAction, 0);
         if (var1) {
            this.insertItemAt(new JSeparator(), 1);
         }
      }

      private void includeNoneItem(boolean var1) {
         if (var1) {
            this.insertItemAt("None", 0);
         }
      }

      @Override
      protected Object getModelObject(Object var1) {
         if (var1 instanceof KongaTreeNode) {
            Object var2 = ((KongaTreeNode)var1).getUserObject();
            if (var2 instanceof IntegrationEntity) {
               return var2;
            }
         }

         return null;
      }

      @Override
      public boolean isSelectable(Object var1) {
         return var1 == "Select or Create New..."
            || var1 == "None"
            || this.isAcceptedProjectItem(var1)
            || TreeWidget.this.comboBox.isPopupVisible() && var1 instanceof Action;
      }

      private boolean isAcceptedProjectItem(Object var1) {
         return TreeWidget.this.entityClass.isInstance(var1) && TreeWidget.this.filter.apply((IntegrationEntity)var1);
      }

      public boolean isOnlyOneItemAvailable() {
         boolean var1 = false;

         for (int var2 = 0; var2 < this.getSize(); var2++) {
            if (this.isAcceptedProjectItem(this.getElementAt(var2))) {
               if (var1) {
                  return false;
               }

               var1 = true;
            }
         }

         return var1;
      }
   }

   private final class ComboRebuilder implements TreeModelListener, PropertyChangeListener {
      private ComboRebuilder() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         assert "IntegrationEntity#MofidiedAt".equals(var1.getPropertyName());
         if (TreeWidget.this.entityClass.isInstance(var1.getSource())) {
            final IntegrationEntity var2 = (IntegrationEntity)var1.getSource();
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  if (this.needsRebuild()) {
                     TreeWidget.this.rebuild();
                  }
               }

               private boolean needsRebuild() {
                  return var2 != TreeWidget.this.getSelectedEntity() || !TreeWidget.this.filter.apply(var2);
               }
            });
         }
      }

      @Override
      public void treeNodesChanged(TreeModelEvent var1) {
         TreeWidget.this.comboBox.repaint();
      }

      @Override
      public void treeNodesInserted(TreeModelEvent var1) {
         TreeWidget.this.rebuild();
      }

      @Override
      public void treeNodesRemoved(TreeModelEvent var1) {
         TreeWidget.this.rebuild();
      }

      @Override
      public void treeStructureChanged(TreeModelEvent var1) {
         TreeWidget.this.rebuild();
      }
   }

   private static class CreateNewActionTrigger {
      public static void install(TreeComboBox var0, Action var1) {
         var0.addActionListener(new TreeWidget.CreateNewActionTrigger.SelectionListener(var1));
         ActionKeyBinding.install(new TreeWidget.CreateNewActionTrigger.EnterAction(var1), var0);
      }

      private static class EnterAction extends AbstractAction {
         private final Action createNew;

         public EnterAction(Action var1) {
            this.createNew = var1;
            this.putValue("AcceleratorKey", KeyUtils.ENTER);
         }

         @Override
         public void actionPerformed(ActionEvent var1) {
            TreeComboBox var2 = (TreeComboBox)var1.getSource();
            if (var2.getSelectedItem() == this.createNew) {
               this.createNew.actionPerformed(var1);
               var2.setPopupVisible(false);
            } else {
               this.processSelection(var1, var2);
            }
         }

         private void processSelection(ActionEvent var1, TreeComboBox var2) {
            if (var2.isPopupVisible()) {
               this.applyListSelection(var2);
               var2.setPopupVisible(false);
            } else {
               this.triggerRootPaneActionIfPresent(var1, var2);
            }
         }

         private void applyListSelection(TreeComboBox var1) {
            ComboBoxUI var2 = var1.getUI();
            if (var2 instanceof BasicComboBoxUI) {
               Accessible var3 = ((BasicComboBoxUI)var2).getAccessibleChild(var1, 0);
               if (var3 instanceof ComboPopup) {
                  JList var4 = ((ComboPopup)var3).getList();
                  Object var5 = var4.getSelectedValue();
                  if (var5 != null && var1.isSelectable(var5)) {
                     var1.setSelectedItem(var5);
                  }
               }
            }
         }

         private void triggerRootPaneActionIfPresent(ActionEvent var1, TreeComboBox var2) {
            JRootPane var3 = SwingUtilities.getRootPane(var2);
            if (var3 != null) {
               InputMap var4 = var3.getInputMap(2);
               ActionMap var5 = var3.getActionMap();
               if (var4 != null && var5 != null) {
                  Object var6 = var4.get(KeyStroke.getKeyStroke(10, 0));
                  if (var6 != null) {
                     Action var7 = var5.get(var6);
                     if (var7 != null) {
                        var7.actionPerformed(new ActionEvent(var3, var1.getID(), var1.getActionCommand(), var1.getWhen(), var1.getModifiers()));
                     }
                  }
               }
            }
         }
      }

      private static class SelectionListener implements ActionListener {
         private final Action createNew;

         public SelectionListener(Action var1) {
            this.createNew = var1;
         }

         @Override
         public void actionPerformed(final ActionEvent var1) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  JComboBox var1x = (JComboBox)var1.getSource();
                  if (!var1x.isPopupVisible()) {
                     Object var2 = var1x.getSelectedItem();
                     if (var2 == SelectionListener.this.createNew) {
                        SelectionListener.this.createNew.actionPerformed(var1);
                     }
                  }
               }
            });
         }
      }
   }

   private final class NewItemCallback implements UiEntityFactoryCallback, PropertyChangeListener {
      private IntegrationEntity createdItem;

      private NewItemCallback() {
      }

      @Override
      public Result preProjectInsertion(IntegrationEntity var1, Folder var2) {
         return TreeWidget.this.newItemHandler.isPresent()
            ? TreeWidget.this.newItemHandler.get().preProjectInsertion(var1, var2)
            : Result.continueProcess(var2);
      }

      @Override
      public void postProjectInsertion(IntegrationEntity var1) {
         if (TreeWidget.this.newItemHandler.isPresent()) {
            TreeWidget.this.newItemHandler.get().postProjectInsertion(var1);
         }

         this.updateUi(var1);
      }

      private void updateUi(final IntegrationEntity var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               if (NewItemCallback.this.createdItem != null) {
                  NewItemCallback.this.createdItem.removePropertyChangeListener(NewItemCallback.this);
               }

               NewItemCallback.this.createdItem = var1;
               NewItemCallback.this.createdItem.addPropertyChangeListener(NewItemCallback.this);
               this.selectNewItemAndForceChangeEvent(var1);
               TreeWidget.this.explorer.openPageFor(var1);
            }

            private void selectNewItemAndForceChangeEvent(IntegrationEntity var1x) {
               assert !TreeWidget.this.selectionDispatcher.isBlocked() : "The SelectionDispatcher should not be blocked at this point.";
               TreeWidget.this.comboBox.setSelectedItem(var1x);
               OrderedSelection var2 = SelectionFactory.newOrderedSelection(var1x);
               SelectionChangedEvent var3 = new SelectionChangedEvent(TreeWidget.this.comboBox, var2);
               TreeWidget.this.selectionDispatcher.selectionChanged(var3);
            }
         });
      }

      @Override
      public void propertyChange(final PropertyChangeEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               String var1x = var1.getPropertyName();
               if ("IntegrationEntity#MofidiedAt".equals(var1x)) {
                  NewItemCallback.this.createdItem.removePropertyChangeListener(NewItemCallback.this);
                  EventQueue.invokeLater(new Runnable() {
                     @Override
                     public void run() {
                        TreeWidget.this.comboBox.setSelectedItem(NewItemCallback.this.createdItem);
                     }
                  });
               } else if ("Deleted".equals(var1x)) {
                  NewItemCallback.this.createdItem.removePropertyChangeListener(NewItemCallback.this);
               }
            }
         });
      }
   }

   private final class NonProjectItemDeleteListener implements PropertyChangeListener {
      private NonProjectItemDeleteListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         assert "Deleted".equals(var1.getPropertyName());
         final IntegrationEntity var2 = (IntegrationEntity)var1.getSource();
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               NonProjectItemDeleteListener.this.removeNonProjectItem(var2);
            }
         });
      }

      private void removeNonProjectItem(IntegrationEntity var1) {
         boolean var2 = TreeWidget.this.nonProjectItems.remove(var1);
         if (var2) {
            TreeWidget.this.stopListeningToNonProjectItem(var1);
            TreeWidget.this.rebuild();
         }
      }
   }

   private static class RendererDelegate implements ListCellRenderer {
      private final IntegrationEntityListDecorRenderer itemRenderer = new IntegrationEntityListDecorRenderer();
      private final ActionListRenderer actionRenderer = ActionListRenderer.withItalicAndBoldFont();
      private final Component separatorRenderer = new JSeparator();

      private RendererDelegate() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         if (var2 instanceof Action) {
            return this.actionRenderer.getListCellRendererComponent(var1, var2, var3, var4, var5);
         } else {
            return var2 instanceof JSeparator ? this.separatorRenderer : this.itemRenderer.getListCellRendererComponent(var1, var2, var3, var4, var5);
         }
      }
   }

   private class ToolTipInstaller implements org.etlforge.ui.selection.SelectionListener {
      private ToolTipInstaller() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         IntegrationEntity var2 = TreeWidget.this.getSelectedEntity();
         String var3 = var2 == null ? null : ToolTipStringFactory.getToolTip(var2);
         TreeWidget.this.comboBox.setToolTipText(var3);
      }
   }
}
