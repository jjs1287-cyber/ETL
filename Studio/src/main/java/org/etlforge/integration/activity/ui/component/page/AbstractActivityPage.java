package org.etlforge.integration.activity.ui.component.page;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import org.etlforge.application.ui.help.HelpButtonResource;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.window.DisposableWindowElementResource;
import org.etlforge.application.ui.window.editor.AbstractEditorBase;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.focus.FocusUtils;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.overlay.OverlayContainer;

public abstract class AbstractActivityPage extends AbstractEditorBase implements WaitService, ActivityPage {
   private final ActivitySubject displayedObject;
   private JComponent pagePanel;
   private final KongaLayeredPane overlayContainer = new KongaLayeredPane();
   private JComponent displayPanel;
   private final PropertyChangeListener entityListener = new AbstractActivityPage.EntityListener();
   private Icon subjectIcon;
   private final KeyBindingInstaller keyBindings = new KeyBindingInstaller(1);

   protected AbstractActivityPage(String var1, ActivitySubject var2) {
      super(var1);
      this.displayedObject = var2;
      this.displayedObject.addNameChangeListener(this.entityListener);
      this.addResource(new DisposableWindowElementResource(this.displayedObject));
      this.setMementoPath(this.constructMementoPath(var2));
   }

   private String[] constructMementoPath(ActivitySubject var1) {
      String var2;
      switch (var1.getId().getType()) {
         case SERVER:
            var2 = "Server";
            break;
         case OPERATION:
         case FOLDER:
         case PROJECT:
            var2 = var1.getId().getGuid().toString();
            break;
         default:
            throw new RuntimeException("Unexpected subject type: " + var1.getId().getType());
      }

      return new String[]{var2, this.getMementoPathLeaf()};
   }

   @Override
   public abstract void retrieveRecords();

   protected abstract void displayedEntityNameChanged();

   protected abstract String getMementoPathLeaf();

   protected void setDisplayPanel(JComponent var1) {
      this.displayPanel = Preconditions.checkNotNull(var1);
   }

   protected final void addKeyBinding(KeyStroke var1, Action var2) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      this.keyBindings.addBinding(var1, var1, var2);
   }

   protected Color getPageBackground() {
      return StyleSheet.GenericEditorPanelClass.getBackground();
   }

   protected Border getPageBorder() {
      return StyleSheet.GenericEditorPanelClass.getBorder();
   }

   @Override
   public ActivitySubject getObject() {
      return this.displayedObject;
   }

   @Override
   public final void decorate(UiCanvas var1) {
      this.createPagePanel();
      var1.display(this.overlayContainer);
   }

   private void createPagePanel() {
      if (this.pagePanel == null) {
         Preconditions.checkState(this.displayPanel != null, "The display panel has not been added.");
         this.pagePanel = new BorderLayoutBuilder(this.displayPanel).container();
         StyleSheet.GenericEditorCanvas.makeOver(this.pagePanel);
         FocusUtils.makeComponentMouseFocusable(this.pagePanel);
         FocusUtils.makeComponentMouseFocusable(this.displayPanel);
         this.keyBindings.install(this.pagePanel);
         this.overlayContainer.setBaseComponent(this.pagePanel);
      }
   }

   protected final JButton createHelpButton(String var1) {
      return HelpButtonResource.forToolbar(this, var1);
   }

   @Override
   public final WaitLock startWait() {
      return this.overlayContainer.startWait();
   }

   @Override
   public final WaitLock startWait(int var1) {
      return this.overlayContainer.startWait(var1);
   }

   public final OverlayContainer getOverlayContainer() {
      return this.overlayContainer;
   }

   @Override
   public final Image render(Dimension var1) {
      Preconditions.checkArgument(var1.width > 0 || var1.height > 0, "At least one of width or height must be > 0");
      Rectangle var2 = new Rectangle(0, 0, this.pagePanel.getWidth(), this.pagePanel.getHeight());
      return render(var1, var2, this.displayPanel);
   }

   @Override
   public final void setSubjectIcon(Icon var1) {
      this.subjectIcon = var1;
   }

   protected final Icon getSubjectIcon() {
      return this.subjectIcon;
   }

   private class EntityListener implements PropertyChangeListener {
      private EntityListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               AbstractActivityPage.this.displayedEntityNameChanged();
               AbstractActivityPage.this.firePropertyChange("title", "0", "1");
            }
         });
      }
   }
}
