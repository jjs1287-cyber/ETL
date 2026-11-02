package org.etlforge.application.ui.text.editor;

import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.ui.widget.text.TextDecorator;

public interface TextEditor extends Editor {
   void setEditable(boolean var1);

   void decorateText(TextDecorator var1);

   void setMementoPath(String[] var1);
}
