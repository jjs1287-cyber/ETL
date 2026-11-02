package org.etlforge.ui.selection;

import javax.swing.Action;

public interface SelectionAction extends Action {
   void setSelection(Selection var1);

   Selection getSelection();

   void setSelectionSource(SelectionSource var1);
}
