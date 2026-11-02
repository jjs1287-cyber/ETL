package com.etlforge.integration.client.ui.sfdc.wizard.soql.relational;

import java.awt.event.ActionEvent;
import javax.swing.DefaultListModel;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.list.ItalicTextListDecorator;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListSelectionMode;
import org.etlforge.ui.widget.list.ListUtils;

final class ChildRelationshipList implements UiProvider {
   private final Controller controller;
   private final KongaList list;
   private final ChildRelationshipList.SelectChildAction selectChildAction = new ChildRelationshipList.SelectChildAction();

   public ChildRelationshipList(RelationalFieldSelectorModel var1, Controller var2) {
      this.controller = var2;
      this.list = this.createEmptyList();
      this.list.setDefaultAction(this.selectChildAction);
      ChildRelationshipList.SelectedObjectBinding var3 = new ChildRelationshipList.SelectedObjectBinding(var1);
      var3.syncUi();
   }

   private KongaList createEmptyList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setVisibleRowCount(4);
      var1.setLabel(KongaLabel.withMnemonic("&Child relationships - double click to view fields"));
      var1.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
      return var1.newList();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.list.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
   }

   private class SelectChildAction extends AbstractSingleObjectSelectionAction<Relationship> {
      private RootObject parent;

      public SelectChildAction() {
         super(Relationship.class);
      }

      protected void run(Relationship var1, ActionEvent var2) {
         ChildRelationshipList.this.controller.selectChild(this.parent, var1);
      }

      protected boolean isEnabledFor(Relationship var1) {
         return this.parent != null;
      }

      public void setParent(RootObject var1) {
         this.parent = var1;
         this.setEnabled(var1 != null && this.getSelectedObject() != null);
      }
   }

   private class SelectedObjectBinding extends AbstractBinding<FieldOwner> {
      public SelectedObjectBinding(RelationalFieldSelectorModel var2) {
         super(var2, "selectedObject");
      }

      protected void updateUiWithValue(FieldOwner var1) {
         this.updateList(var1);
         ChildRelationshipList.this.selectChildAction.setParent(var1 instanceof RootObject ? (RootObject)var1 : null);
      }

      private void updateList(FieldOwner var1) {
         if (var1 instanceof RootObject) {
            RootObject var2 = (RootObject)var1;
            DefaultListModel var3 = ListUtils.createModel(var2.getChildRelationships());
            ChildRelationshipList.this.list.setModel(var3);
         } else {
            ChildRelationshipList.this.list.setModel(ListUtils.createModel(new ItalicTextListDecorator("[Not supported on this level]")));
         }
      }
   }
}
