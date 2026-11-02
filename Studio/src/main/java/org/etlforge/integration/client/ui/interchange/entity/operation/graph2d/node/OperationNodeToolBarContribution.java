package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

interface OperationNodeToolBarContribution {
   JComponent getUi();

   boolean handleMouseEvent(MouseEvent var1);

   boolean isToolTipContributor(Point var1);

   String getToolTipText();

   boolean isAffectedByProperty(String var1);
}
