package org.etlforge.integration.client.ui.structure.editor.db.rels;

import com.google.common.collect.Sets;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.database.ui.ColumnDisplayOrder;
import org.etlforge.integration.client.database.ui.ColumnDisplayOrderSelector;
import org.etlforge.integration.client.database.ui.TableIcon;
import org.etlforge.integration.data.structure.database.FieldRelationship;
import org.etlforge.integration.data.structure.database.TableRelationship;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListUtils;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class DropdownLinkKeysDefiner implements LinkKeysDefiner {
   private final TableRelationship relation;
   private final DropdownLinkKeysDefiner.LinkDefiner linkDefiner;
   private final ColumnDisplayOrderSelector displayOrderSelector;
   private final Set<FieldRelationship> links;
   private final DropdownLinkKeysDefiner.ExistingLinksDisplayer existingLinkDisplayer;

   public DropdownLinkKeysDefiner(TableRelationship var1) {
      this.relation = var1;
      this.displayOrderSelector = new ColumnDisplayOrderSelector();
      this.linkDefiner = new DropdownLinkKeysDefiner.LinkDefiner(var1.getParentObject(), var1.getChildObject(), this.displayOrderSelector.getSelectedOrder());
      this.links = Sets.newLinkedHashSet(var1.getFieldRelationships());
      this.existingLinkDisplayer = new DropdownLinkKeysDefiner.ExistingLinksDisplayer();
      this.existingLinkDisplayer.update();
      this.displayOrderSelector.setCallback(new Receiver<ColumnDisplayOrder>() {
         public void handle(ColumnDisplayOrder var1) {
            DropdownLinkKeysDefiner.this.linkDefiner.setDisplayOrder(var1);
         }
      });
   }

   @Override
   public boolean checkInput() {
      return true;
   }

   @Override
   public void apply() {
      this.relation.setFieldRelationships(this.links);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 10);
      var3.north(this.linkDefiner).east(this.displayOrderSelector);
      var2.north(var3).center(this.existingLinkDisplayer);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.linkDefiner.requestFocus();
   }

   private class ExistingLinksDisplayer implements UiProvider {
      private final KongaList list;
      private final DropdownLinkKeysDefiner.ExistingLinksDisplayer.DeleteLinkAction deleteAction = new DropdownLinkKeysDefiner.ExistingLinksDisplayer.DeleteLinkAction(
         
      );

      public ExistingLinksDisplayer() {
         KongaListFactory var2 = ComponentFactories.newKongaListFactory();
         var2.setVisibleRowCount(4);
         var2.addFocusKeyBinding(this.deleteAction, "delete");
         this.list = var2.newList();
         this.deleteAction.setSelectionSource(this.list);
      }

      @Override
      public void decorate(UiCanvas var1) {
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 5);
         var2.northToSouth("Existing links:", this.list, InvisiblePanel.of(this.deleteAction));
         var2.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.list.requestFocus();
         this.list.selectFirst();
      }

      public void update() {
         DefaultListModel var1 = ListUtils.createModel(DropdownLinkKeysDefiner.this.links);
         this.list.setModel(var1);
      }

      public void select(FieldRelationship var1) {
         this.list.setSelection(SelectionFactory.newOrderedSelection(var1));
      }

      private class DeleteLinkAction extends AbstractSingleTypeSelectionAction<FieldRelationship> {
         public DeleteLinkAction() {
            super(FieldRelationship.class, "Delete Selected Links");
            this.putValue("AcceleratorKey", KeyUtils.DELETE);
         }

         @Override
         protected void run(List<FieldRelationship> var1) {
            DropdownLinkKeysDefiner.this.links.removeAll(var1);
            ExistingLinksDisplayer.this.update();
         }
      }
   }

   private class LinkDefiner implements UiProvider, ActionListener {
      private final DatabaseObject parent;
      private final JComboBox parentSelector;
      private final DatabaseObject child;
      private final JComboBox childSelector;

      public LinkDefiner(DatabaseObject var2, DatabaseObject var3, ColumnDisplayOrder var4) {
         this.parent = var2;
         this.parentSelector = new KongaComboBox(var4.getColumns(var2));
         this.parentSelector.setRenderer(new DropdownLinkKeysDefiner.LinkDefiner.Renderer("Select Parent Key"));
         this.parentSelector.addActionListener(this);
         this.child = var3;
         this.childSelector = new KongaComboBox(var4.getColumns(var3));
         this.childSelector.setRenderer(new DropdownLinkKeysDefiner.LinkDefiner.Renderer("Select Child Key"));
         this.childSelector.addActionListener(this);
         this.clearSelection();
      }

      private void clearSelection() {
         this.parentSelector.setSelectedIndex(-1);
         this.childSelector.setSelectedIndex(-1);
      }

      public void setDisplayOrder(ColumnDisplayOrder var1) {
         this.setNewDisplayOrder(var1, this.parent, this.parentSelector);
         this.setNewDisplayOrder(var1, this.child, this.childSelector);
      }

      private void setNewDisplayOrder(ColumnDisplayOrder var1, DatabaseObject var2, JComboBox var3) {
         Object var4 = var3.getSelectedItem();
         DatabaseColumn[] var5 = var1.getColumns(var2);
         var3.setModel(new DefaultComboBoxModel<>(var5));
         var3.setSelectedItem(var4);
      }

      public FieldRelationship getLink() {
         DatabaseColumn var1 = (DatabaseColumn)this.parentSelector.getSelectedItem();
         DatabaseColumn var2 = (DatabaseColumn)this.childSelector.getSelectedItem();
         return var1 != null && var2 != null ? new FieldRelationship(var1, var2) : null;
      }

      @Override
      public void decorate(UiCanvas var1) {
         Grid var2 = Grid.oneRow(20);
         var2.addAll(
            this.layoutSelector(DropdownLinkKeysDefiner.this.relation.getParentObject(), this.parentSelector),
            this.layoutSelector(DropdownLinkKeysDefiner.this.relation.getChildObject(), this.childSelector)
         );
         var2.decorate(var1);
      }

      private Object layoutSelector(DatabaseObject var1, JComboBox var2) {
         BoxBuilder var3 = BoxBuilder.vertical().leftAligned();
         var3.addAllWithSpace(3, this.getTableLabel(var1), var2);
         return var3;
      }

      private JLabel getTableLabel(DatabaseObject var1) {
         return new JLabel(var1.getName(), new TableIcon(), 10);
      }

      @Override
      public void requestFocus() {
         this.parentSelector.requestFocusInWindow();
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         FieldRelationship var2 = this.getLink();
         if (var2 != null) {
            DropdownLinkKeysDefiner.this.links.add(var2);
            DropdownLinkKeysDefiner.this.existingLinkDisplayer.update();
            DropdownLinkKeysDefiner.this.existingLinkDisplayer.select(var2);
            this.clearSelection();
         }
      }

      private class Renderer extends DefaultListCellRenderer {
         private final String noSelectionText;

         public Renderer(String var2) {
            this.noSelectionText = var2;
         }

         @Override
         public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
            super.getListCellRendererComponent(var1, var2, var3, var4, var5);
            if (var2 == null) {
               this.setText(this.noSelectionText);
            } else {
               this.setText(((DatabaseColumn)var2).getName());
            }

            return this;
         }
      }
   }
}
