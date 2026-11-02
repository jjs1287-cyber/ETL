package org.etlforge.application.ui.window.editor.service;

import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorCategory;

public interface CategoryControlModelListener {
   void categoryOpened(EditorCategory var1);

   void editorOpened(EditorCategory var1, Editor var2);

   void editorClosed(EditorCategory var1, Editor var2);

   void categoryClosed(EditorCategory var1);

   void editorPropertyChanged(EditorCategory var1, Editor var2);
}
