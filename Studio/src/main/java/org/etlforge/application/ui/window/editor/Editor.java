package org.etlforge.application.ui.window.editor;

import java.awt.Dimension;
import java.awt.Image;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

public interface Editor extends WindowElement, UiProvider {
   String DIRTY_PROPERTY = "dirty";

   String getTitle();

   Icon getSmallIcon();

   EditorCategory getCategory();

   void setOwner(EditorOwner var1);

   void addPropertyChangeListener(PropertyChangeListener var1);

   void addPropertyChangeListener(String var1, PropertyChangeListener var2);

   void removePropertyChangeListener(PropertyChangeListener var1);

   void removePropertyChangeListener(String var1, PropertyChangeListener var2);

   boolean isDirty();

   void save() throws SaveEditorException;

   boolean isSaveAsAllowed();

   void saveAs() throws SaveEditorException;

   boolean close();

   Object getObject();

   UndoService getUndoService();

   EditorHistoryPoint getHistoryPoint();

   String[] getMementoPath();

   Image render(Dimension var1);

   boolean openSubEditor(Editor var1);

   boolean decorateWithoutToolbar(UiCanvas var1);
}
