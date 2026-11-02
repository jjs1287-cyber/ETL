package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import javax.swing.undo.UndoableEdit;
import org.etlforge.ui.graph2d.GraphControllerListener;

public interface OperationGraphControllerListener extends GraphControllerListener {
   void graphDataChanged(UndoableEdit var1);
}
