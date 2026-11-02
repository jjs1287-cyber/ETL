package org.etlforge.integration.activity.ui.log;

import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.util.DeletableTableAction;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.SelectionListener;

public interface LogEntryWidget extends UiProvider {
   LogDisplayMode getMode();

   void dispose();

   OperationLogEntry getSelectedLogEntry();

   void addSelectionListener(SelectionListener var1);

   void removeSelectionListener(SelectionListener var1);

   void setBulkSelectionActions(DeletableTableAction var1, DeletableTableAction var2);
}
