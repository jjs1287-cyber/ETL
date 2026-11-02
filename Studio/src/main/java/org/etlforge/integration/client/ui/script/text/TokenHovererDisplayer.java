package org.etlforge.integration.client.ui.script.text;

import java.awt.event.MouseEvent;
import javax.swing.text.JTextComponent;
import org.etlforge.system.Disposable;
import org.etlforge.util.Interval;

public interface TokenHovererDisplayer {
   Disposable display(JTextComponent var1, String var2, Interval var3, MouseEvent var4);
}
