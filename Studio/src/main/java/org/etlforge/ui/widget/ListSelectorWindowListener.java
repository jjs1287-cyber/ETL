package org.etlforge.ui.widget;

public interface ListSelectorWindowListener<T> {
   void selectionChanged(T var1);

   void done(T var1);

   void cancelled();
}
