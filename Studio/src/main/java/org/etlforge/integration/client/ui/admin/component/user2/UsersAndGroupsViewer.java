package org.etlforge.integration.client.ui.admin.component.user2;

import com.google.common.base.Supplier;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionSource;

public interface UsersAndGroupsViewer extends UiProvider, SelectionSource, Disposable {
   void setDefaultAction(SelectionAction var1);

   void addKeyBinding(Object var1, Action var2, KeyStroke var3);

   @Override
   Selection getSelection();

   void setContextMenuProvider(Supplier<JPopupMenu> var1);
}
