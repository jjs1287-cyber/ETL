package org.etlforge.application.ui.wizard;

import com.google.common.base.Preconditions;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FocusTraversalPolicy;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.SwingUtilities;
import javax.swing.Box.Filler;
import javax.swing.plaf.UIResource;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorIdGenerator;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.ui.window.editor.GenericEditor;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.DefaultMessageWaitService;
import org.etlforge.ui.wait.MessageWaitService;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;

public final class WizardPageDisplayer extends GenericEditor implements WizardDisplayer {
   private final Wizard wizard;
   private final EditorService editorService;
   private final KongaLayeredPane pageAreaContainer = new KongaLayeredPane();
   private final MessageWaitService waitService = new DefaultMessageWaitService(this.pageAreaContainer, WizardStyleSheet.MEDIUM_CAPTION_STYLE);
   private JComponent pageArea;
   private WizardPage currentPage;
   private final WizardDisplayerActionSupport actions;
   private NavigationButtons navigationToolBar;
   private boolean wizardDone;
   private String pageGroup;
   private WizardSectionsUi sections;

   public WizardPageDisplayer(Wizard var1, EditorService var2) {
      this(var1, var2, EditorIdGenerator.get(WizardPageDisplayer.class));
   }

   public WizardPageDisplayer(Wizard var1, EditorService var2, String var3) {
      super(var3, var1.getTitle());
      this.wizard = Preconditions.checkNotNull(var1, "wizard");
      this.editorService = Preconditions.checkNotNull(var2, "editorService");
      var1.setDisplayer(this);
      var1.addWizardListener(new WizardPageDisplayer.WizardListenerImpl());
      this.actions = new WizardDisplayerActionSupport(var1);
      this.setEditorBorder(null);
      this.setMementoPath(new String[]{var1.getClass().getSimpleName(), "Page"});
      this.updateTitle();
   }

   @Override
   public WizardPage getCurrentPage() {
      return this.currentPage;
   }

   @Override
   public void updateTitle() {
      this.firePropertyChange("title", null, this.wizard.getTitle());
   }

   @Override
   public Object getObject() {
      return this.wizard;
   }

   @Override
   public void handleContextMenuRequest(Component var1, int var2, int var3) {
      this.actions.handleContextMenuRequest(var1, var2, var3);
   }

   @Override
   public void showPage(final WizardPage var1) {
      Preconditions.checkNotNull(var1, "page");
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (var1 != WizardPageDisplayer.this.currentPage) {
               if (WizardPageDisplayer.this.currentPage == null) {
                  WizardPageDisplayer.this.editorService.openEditor(WizardPageDisplayer.this, WizardPageDisplayer.this.pageGroup);
               }

               WizardPageDisplayer.this.currentPage = var1;
               WizardPageDisplayer.this.addPageToDisplayArea(var1);
               WizardPageDisplayer.this.updateNavigationControls();
               WizardPageDisplayer.this.rebindHelpButton();
            }
         }
      });
   }

   @Override
   public void updateNavigationControls() {
      this.actions.update(this.currentPage);
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.navigationToolBar = this.actions.getToolBar();
      this.pageArea = this.createPageArea();
      this.pageAreaContainer.setBaseComponent(this.pageArea);
      this.setMainPanel(this.pageAreaContainer);
   }

   private JComponent createPageArea() {
      UiProvider var1 = this.layoutSections();
      UiProvider var2 = this.layoutNavigationButtons();
      BorderLayoutBuilder var3 = new BorderLayoutBuilder();
      var3.north(var1).south(var2);
      JComponent var4 = var3.container();
      WizardNavigationKeyBindings.install(this.wizard, var4);
      this.actions.installDefaultAction(var4);
      return var4;
   }

   private UiProvider layoutSections() {
      return new BorderLayoutBuilder().north(this.sections).withEmptyBorder(2, 0, 0, 0).withBackground(Colors.LIGHT_BLUE_BACKGROUND);
   }

   private UiProvider layoutNavigationButtons() {
      return Horizontal.Align.center(this.navigationToolBar).withEmptyBorder(5).withBackground(Colors.ETLFORGE_BLUE);
   }

   @Override
   protected void activated() {
      super.activated();
      this.navigationToolBar.applyDefaultButtonToRootContainer();
   }

   @Override
   protected void deactivated() {
      super.deactivated();
      this.navigationToolBar.resetDefaultButtonInRootContainer();
   }

   @Override
   public boolean removeFromWindow(boolean var1) {
      if (!var1 && !this.wizardDone && !this.wizard.cancelWizard()) {
         return false;
      }

      this.actions.dispose();
      return super.removeFromWindow(var1);
   }

   @Override
   public WaitService getWaitService() {
      return this.waitService;
   }

   @Override
   public String getHelpId() {
      String var1 = "";
      if (this.currentPage != null) {
         var1 = this.currentPage.getHelpId();
      }

      if (var1 == null || var1.length() == 0) {
         var1 = this.wizard.getHelpId();
      }

      return var1 != null ? var1 : "";
   }

   public void setPageGroup(String var1) {
      this.pageGroup = var1;
   }

   @Override
   public void setSections(String[] var1) {
      if (this.sections == null) {
         this.sections = new WizardSectionsUi(var1);
      } else {
         this.sections.setSections(var1);
      }
   }

   @Override
   public void setSection(String var1) {
      Preconditions.checkState(this.sections != null, "No wizard sections have been provided");
      this.sections.setSection(var1);
   }

   @Override
   public String getSection() {
      return this.sections.getSection();
   }

   public void setSaveText(String var1) {
      this.actions.setSaveText(var1);
   }

   @Override
   protected boolean useLayeredPane() {
      return false;
   }

   private void addPageToDisplayArea(final WizardPage var1) {
      this.updateTitle();
      BorderLayout var2 = (BorderLayout)this.pageArea.getLayout();
      Component var3 = var2.getLayoutComponent("Center");
      if (var3 != null) {
         this.pageArea.remove(var3);
      }

      var1.decorate(
         new UiCanvas() {
            @Override
            public void display(Component var1x) {
               WizardPageDisplayer.this.pageArea.add(var1x, "Center");
               JComponent var2x = (JComponent)var1x;
               WizardPageDisplayer.FocusTraversal var3x = WizardPageDisplayer.this.new FocusTraversal(
                  var1, var2x, var2x.getFocusTraversalPolicy(), WizardPageDisplayer.this.navigationToolBar.getFocusTraversalPolicy()
               );
               WizardPageDisplayer.this.setFocusTraversalPolicy(var3x);
            }
         }
      );
      this.pageArea.revalidate();
      this.pageArea.repaint();
      var1.requestFocus();
   }

   private class FocusTraversal extends FocusTraversalPolicy {
      private final WizardPage wpage;
      private final JComponent page;
      private final FocusTraversalPolicy pagePolicy;
      private final FocusTraversalPolicy navButtonsPolicy;

      public FocusTraversal(WizardPage var2, JComponent var3, FocusTraversalPolicy var4, FocusTraversalPolicy var5) {
         this.wpage = var2;
         this.page = var3;
         this.pagePolicy = var4 != null
            ? var4
            : new LayoutFocusTraversalPolicy() {
               @Override
               protected boolean accept(Component var1) {
                  return !(var1 instanceof JLabel)
                     && !(var1 instanceof JPanel)
                     && !(var1 instanceof UIResource)
                     && !(var1 instanceof JViewport)
                     && !(var1 instanceof JScrollPane)
                     && !(var1 instanceof KongaLayeredPane)
                     && !(var1 instanceof Filler);
               }
            };
         this.navButtonsPolicy = var5;
      }

      @Override
      public Component getComponentAfter(Container var1, Component var2) {
         if (SwingUtilities.isDescendingFrom(var2, this.page)) {
            return var2 == this.pagePolicy.getLastComponent(var1)
               ? WizardPageDisplayer.this.navigationToolBar.getFirstEnabledButton()
               : this.pagePolicy.getComponentAfter(var1, var2);
         }

         if (WizardPageDisplayer.this.navigationToolBar.contains(var2)) {
            if (var2 == WizardPageDisplayer.this.navigationToolBar.getLastEnabledButton()) {
               EventQueue.invokeLater(new Runnable() {
                  @Override
                  public void run() {
                     FocusTraversal.this.wpage.requestFocus();
                  }
               });
               return null;
            } else {
               return this.navButtonsPolicy.getComponentAfter(var1, var2);
            }
         } else {
            return null;
         }
      }

      @Override
      public Component getComponentBefore(Container var1, Component var2) {
         if (SwingUtilities.isDescendingFrom(var2, this.page)) {
            return var2 == this.pagePolicy.getFirstComponent(this.page)
               ? WizardPageDisplayer.this.navigationToolBar.getLastEnabledButton()
               : this.pagePolicy.getComponentBefore(var1, var2);
         } else {
            return var2 == WizardPageDisplayer.this.navigationToolBar.getFirstEnabledButton()
               ? this.pagePolicy.getLastComponent(this.page)
               : this.navButtonsPolicy.getComponentBefore(var1, var2);
         }
      }

      @Override
      public Component getDefaultComponent(Container var1) {
         return this.pagePolicy.getDefaultComponent(this.page);
      }

      @Override
      public Component getFirstComponent(Container var1) {
         return this.pagePolicy.getFirstComponent(var1);
      }

      @Override
      public Component getLastComponent(Container var1) {
         return this.navButtonsPolicy.getLastComponent(var1);
      }
   }

   private class WizardListenerImpl implements WizardListener {
      private WizardListenerImpl() {
      }

      @Override
      public void wizardCancelled() {
         this.close();
      }

      @Override
      public void wizardCompleted(Object var1) {
         this.close();
      }

      @Override
      public void wizardFailed(Throwable var1) {
         ErrorLog.attention(WizardPageDisplayer.this.wizard.getClass(), null, var1);
      }

      private void close() {
         WizardPageDisplayer.this.wizardDone = true;
         WizardPageDisplayer.this.setFocusTraversalPolicy(null);
         WizardPageDisplayer.this.close();
      }
   }
}
