package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.application.ui.wizard.WizardAdapter;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.property.ListBinding;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;

final class LinkEntitiesListUi implements UiProvider {
   private final LinkEntitiesModel model;
   private final QueryBuilderController controller;
   private final LinkEntitiesListUi.EditLinkAction editAction = new LinkEntitiesListUi.EditLinkAction();
   private final LinkEntitiesListUi.DeleteLinksAction deleteAction = new LinkEntitiesListUi.DeleteLinksAction();
   private final KongaList list;

   public LinkEntitiesListUi(LinkEntitiesModel var1, QueryBuilderController var2) {
      this.model = var1;
      this.controller = var2;
      this.list = this.createList();
      this.bindWithModel();
   }

   private KongaList createList() {
      KongaListFactory var1 = LayoutUtils.newListFactory();
      var1.setCellRenderer(new LinkEntitiesListUi.Renderer());
      var1.addFocusKeyBinding(this.deleteAction, "delete");
      KongaList var2 = var1.newList();
      this.deleteAction.setSelectionSource(var2);
      var2.setDefaultAction(this.editAction);
      return var2;
   }

   private void bindWithModel() {
      Property var1 = this.model.getProperty("links");
      ListBinding var2 = new ListBinding(var1, this.list);
      var2.setSelectionRetentionMode(ListBinding.SelectionRetentionMode.VALUES);
      var2.syncUi();
   }

   @Override
   public void decorate(UiCanvas var1) {
      ListProperty var2 = (ListProperty)this.model.<List>getProperty("links");
      UiProvider var3 = LayoutUtils.layoutListWithButtons(this.list, var2, new LinkEntitiesListUi.AddLinkAction(), this.editAction, this.deleteAction);
      var3.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
   }

   private class AddLinkAction extends AbstractAction {
      public AddLinkAction() {
         super("Add");
         String var2;
         if (LinkEntitiesListUi.this.model.getObject().hasRelationships()) {
            var2 = "Add a new link entity";
         } else {
            this.setEnabled(false);
            var2 = "No relationships available";
         }

         this.putValue("ShortDescription", var2);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AddLinkAction.this.startWizard();
            }
         });
      }

      private void startWizard() {
         LinkEntityWizard var1 = new LinkEntityWizard(LinkEntitiesListUi.this.model, LinkEntitiesListUi.this.controller.clone());
         var1.addWizardListener(new WizardAdapter<LinkEntityModel>() {
            public void wizardCompleted(LinkEntityModel var1) {
               LinkEntitiesListUi.this.model.add(var1);
            }
         });
         var1.start();
      }
   }

   private class DeleteLinksAction extends AbstractSingleTypeSelectionAction<LinkEntityModel> {
      public DeleteLinksAction() {
         super(LinkEntityModel.class, "Delete");
         this.putValue("AcceleratorKey", KeyUtils.DELETE);
      }

      @Override
      protected void run(final List<LinkEntityModel> var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               LinkEntitiesListUi.this.model.removeAll(var1);
            }
         });
      }
   }

   private class EditLinkAction extends AbstractSingleObjectSelectionAction<LinkEntityModel> {
      public EditLinkAction() {
         super(LinkEntityModel.class, "Edit");
      }

      protected void run(final LinkEntityModel var1, ActionEvent var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EditLinkAction.this.editLink(var1);
            }
         });
      }

      private void editLink(LinkEntityModel var1) {
         LinkEntityModel var2 = new LinkEntityModel(var1);
         QueryBuilderController var3 = LinkEntitiesListUi.this.controller.clone();
         LinkEntityBuilderUi var4 = new LinkEntityBuilderUi(var2, var3);
         String var5 = "Edit Link Entity: " + var1.getTitle();
         WizardStyleDialog var6 = new WizardStyleDialog(ApplicationUi.getAppWinFrame(), var5, var4);
         UndoRedo.install(var4, var6.getRootPane());
         var6.manageLocation(this.getClass());
         var3.setWaitService(var6);
         var6.setOkButtonAsDefault();
         var6.setVisible(true);
         if (var6.wasOkPressed()) {
            LinkEntitiesListUi.this.model.replace(var1, var2);
         }
      }
   }

   private static class Renderer extends DefaultListCellRenderer {
      private Renderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         if (var2 instanceof LinkEntityModel) {
            this.render((LinkEntityModel)var2);
         }

         return this;
      }

      private void render(LinkEntityModel var1) {
         this.setText(var1.getDisplayName());
         this.setToolTipText(var1.getRelationship().toString());
      }
   }
}
