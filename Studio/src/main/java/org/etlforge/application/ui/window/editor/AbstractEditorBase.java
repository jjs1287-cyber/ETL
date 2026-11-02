package org.etlforge.application.ui.window.editor;

import com.google.common.base.Preconditions;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.Icon;
import javax.swing.JComponent;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.AbstractWindowElement;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.RequestFocusResource;
import org.etlforge.application.ui.window.WindowElementStateSpan;
import org.etlforge.ui.UiCanvas;

public abstract class AbstractEditorBase extends AbstractWindowElement implements Editor {
   private boolean dirty;
   private final PropertyChangeSupport propChangeSupport;
   private EditorController controller = new EditorControllerAdapter() {};
   private String[] mementoPath;
   private Icon icon;
   private EditorOwner owner;
   private EditorCategory category;

   protected AbstractEditorBase(String var1) {
      super(var1);
      this.propChangeSupport = new PropertyChangeSupport(this);
      this.mementoPath = new String[]{var1};
      this.installFocusPolicy();
   }

   private void installFocusPolicy() {
      WindowElementStateSpan var1 = this.managesItsOwnFocusPolicy() ? WindowElementStateSpan.WHEN_OPEN : WindowElementStateSpan.WHEN_ACTIVE;
      this.addResource(new RequestFocusResource(this, var1));
   }

   protected boolean managesItsOwnFocusPolicy() {
      return false;
   }

   public final EditorController getController() {
      return this.controller;
   }

   public final void setController(EditorController var1) {
      this.controller = Preconditions.checkNotNull(var1);
   }

   @Override
   public final void setOwner(EditorOwner var1) {
      this.owner = var1;
   }

   @Override
   public final void addPropertyChangeListener(PropertyChangeListener var1) {
      this.propChangeSupport.addPropertyChangeListener(var1);
   }

   @Override
   public final void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.propChangeSupport.addPropertyChangeListener(var1, var2);
   }

   @Override
   public final void removePropertyChangeListener(PropertyChangeListener var1) {
      this.propChangeSupport.removePropertyChangeListener(var1);
   }

   @Override
   public final void removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.propChangeSupport.removePropertyChangeListener(var1, var2);
   }

   @Override
   public UndoService getUndoService() {
      return new NoUndoService();
   }

   @Override
   public Icon getSmallIcon() {
      return this.icon;
   }

   public final void setIcon(Icon var1) {
      this.icon = var1;
   }

   @Override
   public boolean isDirty() {
      return this.dirty;
   }

   public void setDirty(boolean var1) {
      this.dirty = var1;
   }

   @Override
   public boolean isSaveAsAllowed() {
      return false;
   }

   @Override
   public void save() throws SaveEditorException {
   }

   @Override
   public void saveAs() throws SaveEditorException {
      throw new SaveEditorException(this, "Save As is not supported");
   }

   protected final void firePropertyChange(PropertyChangeEvent var1) {
      this.propChangeSupport.firePropertyChange(var1);
   }

   protected final void firePropertyChange(String var1, Object var2, Object var3) {
      this.propChangeSupport.firePropertyChange(var1, var2, var3);
   }

   @Override
   public void requestFocus() {
   }

   @Override
   public final boolean close() {
      if (this.owner != null) {
         if (this.getWindow() == null) {
            return true;
         }

         boolean var2 = this.owner.closeEditor(this);
         if (var2) {
            this.owner = null;
         }

         return var2;
      } else {
         ErrorLog.log(this, null, new Exception("Editor without an owner."));
         ApplicationWindow var1 = this.getWindow();
         return var1 != null ? var1.getEditorService().closeEditor(this) : true;
      }
   }

   @Override
   public EditorHistoryPoint getHistoryPoint() {
      return null;
   }

   @Override
   public String[] getMementoPath() {
      return (String[])this.mementoPath.clone();
   }

   public final void setMementoPath(String[] var1) {
      this.mementoPath = var1 != null ? (String[])var1.clone() : new String[0];
   }

   @Override
   public Image render(Dimension var1) {
      return null;
   }

   @Override
   public boolean openSubEditor(Editor var1) {
      return false;
   }

   public final void setCategory(EditorCategory var1) {
      this.category = var1;
   }

   protected final void useDefaultCategory() {
      this.category = EditorCategory.defaultCategory(this);
   }

   @Override
   public final EditorCategory getCategory() {
      return this.category;
   }

   protected static Image render(Dimension var0, Rectangle var1, JComponent var2) {
      if (var0.width <= 0) {
         var0.width = (int)(1.0 * var0.height * var1.width / var1.height);
      }

      if (var0.height <= 0) {
         var0.height = (int)(1.0 * var0.width * var1.height / var1.width);
      }

      BufferedImage var3 = new BufferedImage(var0.width, var0.height, 1);
      Graphics2D var4 = var3.createGraphics();
      double var5 = 1.0 * var0.width / var1.width;
      double var7 = 1.0 * var0.height / var1.height;
      var4.scale(var5, var7);
      var4.setColor(var2.getBackground());
      var4.fill(var1);
      var4.setClip(var1);
      var2.paint(var4);
      var4.dispose();
      return var3;
   }

   @Override
   public boolean decorateWithoutToolbar(UiCanvas var1) {
      this.decorate(var1);
      return false;
   }
}
