package org.etlforge.application.ui.window.editor;

import java.util.EventListener;

public interface EditorServiceListener extends EventListener {
   void editorOpened(EditorServiceEvent var1);

   void editorClosed(EditorServiceEvent var1);

   void activeEditorChanged(EditorServiceEvent var1);

   void dirtyStateChanged(EditorServiceEvent var1);
}
