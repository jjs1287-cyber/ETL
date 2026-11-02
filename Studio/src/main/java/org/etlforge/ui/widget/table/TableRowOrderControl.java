package org.etlforge.ui.widget.table;

public interface TableRowOrderControl {
   boolean canRowMoveUp(int var1);

   void moveRowUp(int var1);

   boolean canRowMoveDown(int var1);

   void moveRowDown(int var1);
}
