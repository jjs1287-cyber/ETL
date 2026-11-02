package org.etlforge.application.ui.window.editor;

import javax.swing.JComponent;
import org.etlforge.system.Disposable;

public interface EditorThumbnail extends Disposable {
   Editor getEditor();

   JComponent getUi();

   void setActive(boolean var1);

   void addNotification();
}
