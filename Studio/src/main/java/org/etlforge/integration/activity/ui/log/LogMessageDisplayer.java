package org.etlforge.integration.activity.ui.log;

import javax.swing.Action;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.ui.UiProvider;

public interface LogMessageDisplayer extends UiProvider {
   void clear();

   void displayDetails(OperationLogEntry var1);

   Action getSearchAction();
}
