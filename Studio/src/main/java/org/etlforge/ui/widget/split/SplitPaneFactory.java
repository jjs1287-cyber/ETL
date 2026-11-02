package org.etlforge.ui.widget.split;

import java.awt.Component;

public interface SplitPaneFactory {
   KongaSplitPane newSplitPane();

   KongaSplitPane newSplitPane(int var1, boolean var2, Component var3, Component var4);

   KongaSplitPane newSplitPane(int var1, boolean var2);

   KongaSplitPane newSplitPane(int var1, Component var2, Component var3);

   KongaSplitPane newSplitPane(int var1);
}
