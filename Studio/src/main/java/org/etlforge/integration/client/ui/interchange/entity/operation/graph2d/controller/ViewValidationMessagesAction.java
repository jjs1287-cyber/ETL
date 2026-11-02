package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import org.etlforge.application.ui.widget.AnimationPreference;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.client.ui.validation.EntityValidationResultDisplayerFactory;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.validation.EntityCollectionValidationResult;
import org.etlforge.integration.data.validation.SingleEntityValidationResult;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.overlay.OverlayAbsolutePositioner;
import org.etlforge.ui.widget.overlay.OverlayDecorator;

public final class ViewValidationMessagesAction extends AbstractSingleObjectSelectionAction<Object> {
   public static final KeyStroke ACCELERATOR = KeyUtils.getCtrlShiftDown(86);
   private final Graph2D graph;
   private EntityCollectionValidationResult result;

   public ViewValidationMessagesAction(Graph2D var1) {
      super(Object.class, Strings.get("ViewValidationMessages.Name"));
      this.graph = var1;
      this.putValue("ShortDescription", Strings.get("ViewValidationMessages.ToolTip"));
      this.putValue("AcceleratorKey", ACCELERATOR);
   }

   public ViewValidationMessagesAction(Graph2D var1, Object var2) {
      this(var1);
      this.setSelection(SelectionFactory.newOrderedSelection(var2));
   }

   @Override
   protected boolean isEnabledFor(Object var1) {
      IntegrationEntity var2 = getSelectedEntity(var1);
      this.result = var2 != null ? new SingleEntityValidationResult(var2, var2.getCurrentValidationResult()) : null;
      return this.result != null && !this.result.isEverythingOk();
   }

   @Override
   protected void run(Object var1, ActionEvent var2) {
      if (this.result != null) {
         UiProvider var3 = EntityValidationResultDisplayerFactory.newListDisplayer(this.result);
         OverlayDecorator var4 = new OverlayDecorator(var3, Strings.get("ValidationMessages.Title"));
         var4.setBlocking(true);
         var4.setAlpha(0.75F);
         var4.setFadeIn(AnimationPreference.get());
         var4.useCloseOnly();
         Point var5 = this.graph.getLayout().getBounds(var1).getLocation();
         var5.x += 10;
         var5.y += 10;
         var4.show(this.graph.getOverlayContainer(), new OverlayAbsolutePositioner(var5));
      }
   }

   private static IntegrationEntity getSelectedEntity(Object var0) {
      if (var0 instanceof OperationWrapperNode) {
         return ((OperationWrapperNode)var0).getDataObject().getActivity();
      }

      if (var0 instanceof EmailWrapperNode) {
         return ((EmailWrapperNode)var0).getDataObject().getActivity();
      }

      if (var0 instanceof PipelineActivityNode) {
         PipelineActivity var1 = ((PipelineActivityNode)var0).getDataObject();
         Object var2 = var1.getContent();
         if (var2 instanceof IntegrationEntity) {
            return (IntegrationEntity)var2;
         }
      }

      return null;
   }
}
