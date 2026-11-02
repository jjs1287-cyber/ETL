package org.etlforge.ui.widget.list;

import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.MutableSelectionSource;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionSourceWithDefaultAction;
import org.etlforge.ui.util.focus.Focusable;
import org.etlforge.ui.widget.selection.SelectionWidget;

public interface KongaList extends UiProvider, Focusable, MutableSelectionSource, SelectionSourceWithDefaultAction, SelectionWidget {
   ListModel getModel();

   void setModel(ListModel var1);

   void setEnabled(boolean var1);

   @Override
   void setDefaultAction(SelectionAction var1);

   IndexedSelection getSelection();

   @Override
   void setSelection(Selection var1);

   void setSelectedIndex(int var1);

   @Override
   void selectFirst();

   Disposable installKeyTraversalBindings(JComponent var1);

   void setTransient(boolean var1);

   boolean processKeyEvent(KeyEvent var1);

   void ensureSelectedIndexVisible();

   void repaint();

   void addKeyBinding(KeyStroke var1, String var2, Action var3);
}
