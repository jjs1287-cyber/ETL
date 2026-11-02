package org.etlforge.ui.input;

import javax.swing.event.UndoableEditListener;

public interface ChangeSource {
   void addInputChangeListener(InputChangeListener var1);

   void removeInputChangeListener(InputChangeListener var1);

   boolean isSendingInputChangeEvents();

   void setSendInputChangeEvents(boolean var1);

   void addUndoableEditListener(UndoableEditListener var1);

   void removeUndoableEditListener(UndoableEditListener var1);

   boolean isSendingUndoEvents();

   void setSendUndoEvents(boolean var1);
}
