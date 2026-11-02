package org.etlforge.application.ui.window.editor;

public interface EditorSelectorListener {
   void temporarySelection(Editor var1);

   void finalSelection(Editor var1);

   void closeRequested(Editor var1);

   void closeOthersRequested(Editor var1);
}
