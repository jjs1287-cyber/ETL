package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import com.google.common.collect.Sets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.etlforge.integration.data.entity.Operation;

abstract class OperationNodeToolBarButton implements OperationNodeToolBarContribution {
   private final AbstractOperationNodeUi nodeUi;
   private final JButton button;
   private final Set<String> properties;

   protected OperationNodeToolBarButton(AbstractOperationNodeUi var1) {
      this.nodeUi = var1;
      this.properties = Sets.newHashSet();
      this.button = this.createButton();
   }

   protected abstract JButton createButton();

   protected abstract void doClick();

   protected final OperationWrapperNode getNode() {
      return this.nodeUi.getNodeObject();
   }

   protected final Operation getOperation() {
      return this.getNode().getDataObject().getActivity();
   }

   protected final JButton getButton() {
      return this.button;
   }

   protected final AbstractOperationNodeUi getNodeUi() {
      return this.nodeUi;
   }

   protected final void addProperty(String var1) {
      this.properties.add(var1);
   }

   protected boolean isActive() {
      return true;
   }

   @Override
   public JComponent getUi() {
      return this.isActive() ? this.button : null;
   }

   @Override
   public boolean handleMouseEvent(MouseEvent var1) {
      Point var2 = var1.getPoint();
      if (this.isActive() && this.button.getBounds().contains(var2)) {
         this.doClick();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean isToolTipContributor(Point var1) {
      if (!this.isActive()) {
         return false;
      }

      Point var2 = new Point(var1);
      var2.translate(-this.button.getX(), -this.button.getY());
      return this.button.contains(var2);
   }

   @Override
   public boolean isAffectedByProperty(String var1) {
      return this.properties.contains(var1);
   }
}
