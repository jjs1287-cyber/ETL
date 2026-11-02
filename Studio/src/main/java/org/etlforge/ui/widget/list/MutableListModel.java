package org.etlforge.ui.widget.list;

import javax.swing.ListModel;

public interface MutableListModel extends ListModel {
   void addElement(Object var1);

   void addElementAt(Object var1, int var2);

   boolean removeElement(Object var1);

   Object removeItemAt(int var1);

   void removeRange(int var1, int var2);

   int indexOf(Object var1);

   void clear();
}
