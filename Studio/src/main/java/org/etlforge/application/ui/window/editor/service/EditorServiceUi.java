package org.etlforge.application.ui.window.editor.service;

import java.util.List;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.ui.UiProvider;

public interface EditorServiceUi extends UiProvider {
   void addEditor(Editor var1);

   Editor getActiveEditor();

   boolean isDisplayingEditor(Editor var1);

   void selectEditor(Editor var1);

   int getNumberOfOpenEditors();

   List<Editor> getOpenEditors();

   void updateTitleAndIcon(Editor var1);

   void removeEditor(Editor var1, Editor var2);

   void addDetachedEditor(Editor var1);

   void detachEditor(Editor var1);

   void addNotification(Editor var1);

   void openQuickSelector();

   void prepareForUiSwitch();
}
