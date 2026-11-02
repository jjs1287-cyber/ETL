package org.etlforge.application.ui.window.editor;

import java.util.Collection;
import org.etlforge.application.ui.window.editor.service.BackwardForwardStack;
import org.etlforge.application.ui.window.editor.service.EditorMemento;
import org.etlforge.application.ui.window.event.ActiveObjectProvider;
import org.etlforge.ui.UiProvider;

public interface EditorService extends EditorDisplayer, ActiveObjectProvider, EditorOwner {
   void activate();

   void deactivate();

   Editor getActiveEditor();

   @Override
   void openEditor(Editor var1);

   void openEditor(Editor var1, String var2);

   void openEditorDetached(Editor var1);

   int getNumberOfEditors();

   @Override
   Editor[] getOpenEditors();

   Editor getEditor(String var1);

   boolean closeAllEditors();

   boolean closeEditors(Collection<Editor> var1);

   boolean closeEditors(String var1);

   @Override
   boolean closeEditor(Editor var1);

   boolean closeOtherEditors(Editor var1);

   BackwardForwardStack getBackwardForwardStack();

   void addEditorServiceListener(EditorServiceListener var1);

   void removeEditorServiceListener(EditorServiceListener var1);

   UiProvider getUi();

   Editor[] getNavigationOrder();

   EditorMemento getEditorMemento();

   void openQuickSelector();
}
