package com.etlforge.integration.client.ui.sfdc.wizard.soql.relational;

import java.awt.event.ActionEvent;
import javax.swing.DefaultListModel;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListSelectionMode;
import org.etlforge.ui.widget.list.ListUtils;

final class ParentRelationshipList implements UiProvider {
   private final Controller controller;
   private final KongaList list;
   private final ParentRelationshipList.SelectParentAction selectParentAction = new ParentRelationshipList.SelectParentAction();

   public ParentRelationshipList(RelationalFieldSelectorModel var1, Controller var2) {
      this.controller = var2;
      this.list = this.createEmptyList();
      this.list.setDefaultAction(this.selectParentAction);
      ParentRelationshipList.SelectedObjectBinding var3 = new ParentRelationshipList.SelectedObjectBinding(var1);
      var3.syncUi();
   }

   private KongaList createEmptyList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setVisibleRowCount(4);
      var1.setLabel(KongaLabel.withMnemonic("&Parent relationships - double click to view fields"));
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

   private class SelectParentAction extends AbstractSingleObjectSelectionAction<Relationship> {
      private FieldOwner child;

      public SelectParentAction() {
         super(Relationship.class);
      }

      protected void run(Relationship var1, ActionEvent var2) {
         ParentRelationshipList.this.controller.selectParent(this.child, var1);
      }

      protected boolean isEnabledFor(Relationship var1) {
         return this.child != null;
      }

      public void setChild(FieldOwner var1) {
         this.child = var1;
         this.setEnabled(var1 != null && this.getSelectedObject() != null);
      }
   }

   private class SelectedObjectBinding extends AbstractBinding<FieldOwner> {
      public SelectedObjectBinding(RelationalFieldSelectorModel var2) {
         super(var2, "selectedObject");
      }

      protected void updateUiWithValue(FieldOwner var1) {
         this.updateList(var1);
         ParentRelationshipList.this.selectParentAction.setChild(var1);
      }

      private void updateList(FieldOwner var1) {
         DefaultListModel var2 = ListUtils.createModel(var1.getParentRelationships());
         ParentRelationshipList.this.list.setModel(var2);
      }
   }
}
