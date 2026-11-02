package org.etlforge.application.ui.window.editor;

import javax.swing.Icon;
import org.etlforge.system.Disposable;

public interface EditorHistoryPoint extends Disposable {
   String getId();

   boolean isAvailable();

   @Override
   void dispose();

   boolean isDisposed();

   String getDisplayString();

   Icon getIcon();

   void restore(EditorService var1);
}
