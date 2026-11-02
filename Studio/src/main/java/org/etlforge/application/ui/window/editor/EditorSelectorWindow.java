package org.etlforge.application.ui.window.editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.util.Collection;
import org.etlforge.system.Disposable;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.widget.window.WindowCornerPosition;

public interface EditorSelectorWindow extends Disposable {
   void open(Window var1, WindowCornerPosition var2);

   void close();

   void setSelectedEditor(Editor var1);

   void setNotifications(Collection<Editor> var1);

   Dimension getSize();

   void setActiveArea(Component var1);

   void addBlindSpot(Component var1);

   void setDialog(KongaDialog var1);

   void restoreOriginalEditor();

   boolean containsPoint(Point var1);
}
