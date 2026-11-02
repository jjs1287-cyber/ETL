package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Optional;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.etlforge.application.ui.ApplicationUi;
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

final class OrderListUi implements UiProvider {
   private final AttributeOrderModel model;
   private final OrderListUi.EditAction editAction = new OrderListUi.EditAction();
   private final OrderListUi.DeleteAction deleteAction = new OrderListUi.DeleteAction();
   private final KongaList list;

   public OrderListUi(AttributeOrderModel var1) {
      this.model = var1;
      this.list = this.createList();
      this.bindToModel();
   }

   private KongaList createList() {
      KongaListFactory var1 = LayoutUtils.newListFactory();
      var1.setCellRenderer(new OrderListUi.Renderer());
      var1.addFocusKeyBinding(this.deleteAction, "delete");
      KongaList var2 = var1.newList();
      this.deleteAction.setSelectionSource(var2);
      var2.setDefaultAction(this.editAction);
      return var2;
   }

   private void bindToModel() {
      Property var1 = this.model.getProperty("order");
      ListBinding var2 = new ListBinding(var1, this.list);
      var2.setSelectionRetentionMode(ListBinding.SelectionRetentionMode.VALUES);
      var2.syncUi();
   }

   @Override
   public void decorate(UiCanvas var1) {
      ListProperty var2 = (ListProperty)this.model.<List>getProperty("order");
      UiProvider var3 = LayoutUtils.layoutListWithButtons(this.list, var2, new OrderListUi.AddAction(), this.editAction, this.deleteAction);
      var3.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
   }

   private class AddAction extends AbstractAction {
      public AddAction() {
         super("Add");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.addOrder();
      }

      private void addOrder() {
         OrderDefinitionUi var1 = new OrderDefinitionUi(OrderListUi.this.model.getAvailableAttributes());
         Optional var2 = DataEntryUiDialog.open(var1, ApplicationUi.getAppWinFrame(), "Add Order");
         if (var2.isPresent()) {
            OrderListUi.this.model.add((Order)var2.get());
         }
      }
   }

   private class DeleteAction extends AbstractSingleTypeSelectionAction<Order> {
      public DeleteAction() {
         super(Order.class, "Delete");
         this.putValue("AcceleratorKey", KeyUtils.DELETE);
      }

      @Override
      protected void run(final List<Order> var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               OrderListUi.this.model.remove(var1);
            }
         });
      }
   }

   private class EditAction extends AbstractSingleObjectSelectionAction<Order> {
      public EditAction() {
         super(Order.class, "Edit");
      }

      protected void run(final Order var1, ActionEvent var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EditAction.this.edit(var1);
            }
         });
      }

      private void edit(Order var1) {
         OrderDefinitionUi var2 = new OrderDefinitionUi(OrderListUi.this.model.getAvailableAttributes());
         var2.setOrder(var1);
         Optional var3 = DataEntryUiDialog.open(var2, ApplicationUi.getAppWinFrame(), "Edit Order");
         if (var3.isPresent() && !((Order)var3.get()).equals(var1)) {
            OrderListUi.this.model.replace(var1, (Order)var3.get());
         }
      }
   }

   private static class Renderer extends DefaultListCellRenderer {
      private Renderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         if (var2 instanceof Order) {
            this.render((Order)var2);
         }

         return this;
      }

      private void render(Order var1) {
         String var2 = var1.getAttribute() + " " + var1.getDirection().toString().toLowerCase();
         this.setText(var2);
      }
   }
}
