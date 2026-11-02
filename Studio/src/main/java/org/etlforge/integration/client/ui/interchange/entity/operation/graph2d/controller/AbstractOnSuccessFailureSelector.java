package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.JLabel;
import org.etlforge.integration.client.ui.entity.selector.EntitySelector;
import org.etlforge.integration.client.ui.entity.selector.EntityTreeSelector;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.operation.flow.RouteType;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;

public abstract class AbstractOnSuccessFailureSelector<T extends IntegrationEntity> implements UiProvider {
   private final OperationGraphController controller;
   private final OperationWrapperNode node;
   private final EntityType entityType;
   private final EntitySelector<T> selector;
   private final RouteType routeType;
   private Receiver<T> callback;

   protected AbstractOnSuccessFailureSelector(OperationGraphController var1, OperationWrapperNode var2, EntityType var3, Class<T> var4, RouteType var5) {
      this.controller = var1;
      this.node = var2;
      this.entityType = var3;
      this.routeType = var5;
      this.callback = NoReceiver.create();
      this.selector = new EntityTreeSelector<>(var1.getProject(), var4);
      IntegrationEntity var6 = this.getCurrentEntity(var4);
      this.selector.setSelectedEntity((T)var6);
      this.selector.setDefaultAction(new AbstractOnSuccessFailureSelector.SelectAction(var4));
   }

   private T getCurrentEntity(Class<T> var1) {
      IntegrationEntityId var2 = this.getCurrentEntityId(this.node);
      IntegrationEntityLookup var3 = this.controller.getEntityLookup();
      return var3.getEntity(var2, var1);
   }

   protected final void setFilter(EntityFilter var1) {
      this.selector.setFilter(var1);
   }

   public final String getTitle() {
      return "Select " + this.routeType + " " + this.entityType.getDisplayName();
   }

   public final void setCallback(Receiver<T> var1) {
      this.callback = var1;
   }

   public final void apply() {
      IntegrationEntity var1 = this.selector.getSelectedEntity();
      if (var1 != null) {
         this.apply(this.controller, this.node, (T)var1);
      }
   }

   protected abstract IntegrationEntityId getCurrentEntityId(OperationWrapperNode var1);

   protected abstract void apply(OperationGraphController var1, OperationWrapperNode var2, T var3);

   @Override
   public final void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 3);
      JLabel var3 = this.getCaption();
      var2.north(var3).center(this.selector);
      var2.decorate(var1);
   }

   private JLabel getCaption() {
      return new JLabel(this.getTitle(), this.getIcon(), 10);
   }

   private Icon getIcon() {
      switch (this.routeType) {
         case SUCCESS:
            return GraphResources.SUCCESS_16;
         case FAILURE:
            return GraphResources.FAILURE_16;
         default:
            throw new RuntimeException("Unexpected route type: " + this.routeType.name());
      }
   }

   @Override
   public final void requestFocus() {
      this.selector.requestFocus();
   }

   private final class SelectAction extends AbstractSingleObjectSelectionAction<T> {
      public SelectAction(Class<T> var2) {
         super(var2, "Select");
      }

      protected void run(T var1, ActionEvent var2) {
         AbstractOnSuccessFailureSelector.this.callback.handle((T)var1);
         AbstractOnSuccessFailureSelector.this.apply(AbstractOnSuccessFailureSelector.this.controller, AbstractOnSuccessFailureSelector.this.node, (T)var1);
      }
   }
}
