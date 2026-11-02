package org.etlforge.integration.client.ui.interchange.location.export;

import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionListener;

public interface ExportablePropertySelector extends UiProvider {
   OrderedSelection getSelection();

   void addSelectionListener(SelectionListener var1);

   void dispose();
}
