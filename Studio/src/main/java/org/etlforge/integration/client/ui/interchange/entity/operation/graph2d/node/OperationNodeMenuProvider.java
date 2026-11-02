package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import javax.swing.JPopupMenu;

public interface OperationNodeMenuProvider {
   JPopupMenu getActionMenuFor(OperationWrapperNode var1);

   JPopupMenu getScheduleMenuFor(OperationWrapperNode var1);

   JPopupMenu getHttpEndpointMenuFor(OperationWrapperNode var1);

   JPopupMenu getSfdcWizardMenuFor(OperationWrapperNode var1);
}
