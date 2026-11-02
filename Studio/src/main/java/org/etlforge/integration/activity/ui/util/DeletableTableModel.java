package org.etlforge.integration.activity.ui.util;

public interface DeletableTableModel {
   boolean hasDeletableEntries();

   void selectAll();

   void unselectAll();
}
