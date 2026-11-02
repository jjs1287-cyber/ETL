package org.jitterbit.application.ui;

import org.jitterbit.application.ui.window.editor.Editor;

public interface GettingStartedPageFactory {
   boolean isSupported();

   String getId();

   Editor newGettingStartedPage();
}
