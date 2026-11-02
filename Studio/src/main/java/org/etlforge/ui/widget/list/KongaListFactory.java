package org.etlforge.ui.widget.list;

import com.google.common.base.Function;
import java.awt.event.MouseListener;
import java.util.Collection;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import org.etlforge.ui.selection.SelectionConverter;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.PopupMenuRequestHandler;

public interface KongaListFactory {
   KongaList newList();

   KongaList newList(ListModel var1);

   KongaList newList(Object[] var1);

   KongaList newList(Collection<?> var1);

   void setCellRenderer(ListCellRenderer var1);

   void setVisibleRowCount(int var1);

   void setSelectionMode(ListSelectionMode var1);

   void setPrototypeValue(Object var1);

   void setNextMatchFunction(Function<Object, String> var1);

   void addKeyBindings(KeyBindingInstaller var1);

   void addFocusKeyBinding(Action var1, String var2);

   void setPopupMenuRequestHandler(PopupMenuRequestHandler var1);

   void addMouseListener(MouseListener var1);

   void noScrolling();

   void setDragSource(boolean var1);

   void setTransferHandler(TransferHandler var1);

   void setStyle(TextStyle var1);

   void setBorder(Border var1);

   void setLabel(JLabel var1);

   void setSelectionConverter(SelectionConverter var1);
}
