package org.etlforge.ui.selection;

public interface SelectionSource {
   Selection getSelection();

   void addSelectionListener(SelectionListener var1);

   void removeSelectionListener(SelectionListener var1);
}
