package org.etlforge.integration.client.ui.script.builder.layout;

import javax.swing.Action;
import javax.swing.JComponent;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderSection;
import org.etlforge.integration.client.ui.script.builder.ScriptTestUi;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

public interface ScriptBuilderLayout {
   void addSection(ScriptBuilderSection var1);

   void setDebugUi(ScriptTestUi var1);

   void setSourceTree(UiProvider var1);

   void decorate(UiCanvas var1);

   void setButtonPanel(JComponent var1);

   void selectTestAndDebuggingTab();

   void installTestActionShortcut(Action var1);

   void setPostDisplayDelay(int var1);
}
