package org.etlforge.application.ui.window.editor.service;

import org.etlforge.application.ui.window.editor.Editor;

interface EditorsToSaveCollector {
   Editor[] collect();

   boolean wasCancelled();
}
