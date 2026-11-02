package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import com.google.common.collect.ImmutableSet;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.HttpEndpoint;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.util.name.NameSorter;

final class HttpEndpointIndicator extends OperationNodeToolBarDropDown {
   public HttpEndpointIndicator(AbstractOperationNodeUi var1) {
      super(var1);
      this.addProperty("httpEndpoints");
   }

   @Override
   protected JButton createButton() {
      Icon var1 = EntityIcons.forType(EntityType.HttpEndpoint).getDefaultIcon(EntityIcons.Size.MEDIUM);
      return new KongaButton(var1).makeToolbarButton();
   }

   @Override
   protected JPopupMenu getPopupMenu(OperationNodeController var1) {
      return var1.getMenuProvider().getHttpEndpointMenuFor(this.getNode());
   }

   @Override
   protected boolean isActive() {
      return this.getNode().getDataObject().isTriggeredByHttpEndpoint();
   }

   @Override
   public String getToolTipText() {
      if (!this.isActive()) {
         return null;
      }

      ImmutableSet var1 = this.getNode().getDataObject().getHttpEndpointTriggers();
      StringBuilder var2 = new StringBuilder("<html>Triggered by:");

      for (Object __etlItem45 : Iterables2.sortedCopy(var1, NameSorter.ignoreCase())) {
         HttpEndpoint var4 = (HttpEndpoint) __etlItem45;
         var2.append("<br>").append(var4.getName());
      }

      return var2.append("</html>").toString();
   }
}
