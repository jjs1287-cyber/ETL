package org.etlforge.application.ui.window.editor.service;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorCategory;
import org.etlforge.application.ui.window.editor.EditorSelectorWindow;
import org.etlforge.ui.dialog.KongaDialog;

interface CategoryControlView {
   void openGlobalEditorSelector();

   EditorSelectorWindow openEditorSelector(ApplicationWindow var1, EditorCategory var2, KongaDialog var3);
}
