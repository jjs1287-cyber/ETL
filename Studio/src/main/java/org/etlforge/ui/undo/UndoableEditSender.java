package org.etlforge.ui.undo;

import javax.swing.event.UndoableEditListener;

public interface UndoableEditSender {
   void dispose();

   void addUndoableEditListener(UndoableEditListener var1);

   void removeUndoableEditListener(UndoableEditListener var1);
}
