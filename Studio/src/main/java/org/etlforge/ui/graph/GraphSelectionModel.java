package org.etlforge.ui.graph;

import java.util.Collection;
import java.util.List;

public interface GraphSelectionModel {
   boolean isSelected(Object var1);

   void addToSelection(Collection<?> var1);

   void removeFromSelection(Collection<?> var1);

   void flipSelection(Collection<?> var1);

   void setSelection(Collection<?> var1);

   int getNumberOfSelectedObjects();

   List<Object> getSelection();

   void clearSelection();

   void addSelectionListener(GraphSelectionListener var1);

   void removeSelectionListener(GraphSelectionListener var1);
}
