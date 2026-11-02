package org.etlforge.ui.widget.list;

import java.util.Collection;
import javax.swing.JList;
import javax.swing.ListModel;

public interface ListFactory {
   JList newList();

   JList newList(ListModel var1);

   JList newList(Object[] var1);

   JList newList(Collection<?> var1);
}
