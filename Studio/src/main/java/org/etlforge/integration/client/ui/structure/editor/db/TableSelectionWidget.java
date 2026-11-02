package org.etlforge.integration.client.ui.structure.editor.db;

import javax.swing.Action;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.list.ListFilter;

interface TableSelectionWidget extends UiProvider {
   Receiver<ListFilter> getFilterReceiver();

   void setDefaultAction(Action var1);
}
