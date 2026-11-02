package org.etlforge.application.ui.window.editor;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.help.ApplicationUiHelp;
import org.etlforge.application.ui.help.DocumentedResource;
import org.etlforge.application.ui.help.HelpButtonResource;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.widget.ApplicationToolBar;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.DisposableWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowElementResource;
import org.etlforge.application.ui.window.WindowElementState;
import org.etlforge.application.ui.window.WindowElementStateSpan;
import org.etlforge.application.ui.window.editor.service.EditorServiceUiMode;
import org.etlforge.application.ui.window.editor.service.EditorServiceUiModePreference;
import org.etlforge.cli.CommandModule;
import org.etlforge.cli.ui.CommandConsole;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.event.EventUtils;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneRowPanel;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.focus.FocusMemento;
import org.etlforge.ui.util.focus.FocusUtils;
import org.etlforge.ui.wait.ContainerWaitService;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.button.PopupMenuButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayContainerEvent;
import org.etlforge.ui.widget.overlay.OverlayContainerListener;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public class GenericEditor extends AbstractEditorBase implements UndoableEditListener, DocumentedResource {
   public static final int DO_NOTHING_ON_REMOVAL = 0;
   public static final int SAVE_SILENTLY_ON_REMOVAL = 1;
   public static final int PROMPT_FOR_SAVE_ON_REMOVAL = 2;
   public static final int ALWAYS_VETO_REMOVAL = 3;
   private static final boolean USE_REPRESENTATION_NAMES_FOR_UNDO_AND_REDO = true;
   private String mainTitle;
   private String subTitle;
   private Icon titleIcon;
   private GenericEditor.TitlePanel titlePanel;
   private Component additionalTitleDecoration;
   private JComponent mainPanel;
   private final JPanel editorPanel = new JPanel(new BorderLayout());
   private final KongaLayeredPane overlayContainer;
   private int removePolicyWhenDirty = 2;
   private boolean displayed;
   private JButton helpButton;
   private final UndoManager undoManager = new UndoManager();
   private volatile boolean isCompoundEdit;
   private volatile boolean isCompoundUndo;
   private volatile CompoundEdit compoundEdit;
   private final GenericEditor.UndoServiceImpl undoService;
   private final Stack<Integer> savePointDistances = new Stack<>();
   private int currentSavePointDistance;
   private MouseListener mouseListener;
   private final WaitService waitService;
   private boolean contributeGlobalUndoRedo;
   private FocusMemento focusMemento;
   private String helpId;
   private List<Action> toolbarPopupMenuActions = Lists.newArrayList();
   private List<JButton> toolbarButtons = Lists.newArrayList();
   private boolean includeCloseButton = true;
   private CommandModule commands;
   private Border editorBorder = StyleSheet.GenericEditorPanelClass.getBorder();
   private boolean useScroll = true;

   public GenericEditor(String var1, String var2) {
      this(var1, var2, "");
   }

   public GenericEditor(String var1, String var2, String var3) {
      super(var1);
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "<init>");
      this.mainTitle = var2;
      this.subTitle = var3;
      if (this.useLayeredPane()) {
         this.overlayContainer = new KongaLayeredPane();
         this.waitService = this.overlayContainer;
      } else {
         this.overlayContainer = null;
         this.waitService = new ContainerWaitService(this.editorPanel);
      }

      this.undoService = new GenericEditor.UndoServiceImpl(this.undoManager);
      this.addResource(new GenericEditor.KeyDispatchResource(new GenericEditor.KeyStrokeListener()));
   }

   protected final void createTitlePanel() {
      this.titlePanel = new GenericEditor.TitlePanel();
      this.titlePanel.setIcon(this.titleIcon);
   }

   public void setContributeGlobalUndoRedo() {
      Preconditions.checkState(!this.isAddedToWindow(), "setContributeGlobalUndoRedo must be called before the editor is added to the application window");
      this.contributeGlobalUndoRedo = true;
   }

   @Override
   public String getTitle() {
      return this.mainTitle;
   }

   protected boolean includeMainTitleInTitleBar() {
      return true;
   }

   public String getSubTitle() {
      return this.subTitle;
   }

   public void setTitle(String var1) {
      this.mainTitle = var1;
      if (this.titlePanel != null) {
         this.titlePanel.layoutPanel();
      }
   }

   public void setSubTitle(String var1) {
      this.subTitle = var1;
      if (this.titlePanel != null) {
         this.titlePanel.layoutPanel();
      }
   }

   public void setSubTitleStyle(TextStyle var1) {
      if (this.titlePanel == null) {
         throw new NotImplementedYetException("You must create the title panel first");
      }

      this.titlePanel.setSubTitleStyle(var1);
   }

   public void setTitleIcon(Icon var1) {
      this.titleIcon = var1;
      if (this.titlePanel != null) {
         this.titlePanel.setIcon(var1);
      }
   }

   public final void setMainPanel(JComponent var1) {
      this.mainPanel = var1;
      this.mainPanel.setBackground(StyleSheet.GenericEditorPanelClass.getBackground());
      this.mainPanel.setBorder(this.editorBorder);
      this.installMainPanelMouseListener();
   }

   public final JComponent getMainPanel() {
      return this.mainPanel;
   }

   public final void setAdditionalTitleDecoration(Component var1) {
      if (var1 != this.additionalTitleDecoration) {
         this.additionalTitleDecoration = var1;
         if (this.titlePanel != null) {
            this.titlePanel.layoutPanel();
         }
      }
   }

   public final void setFocusTraversalPolicy(FocusTraversalPolicy var1) {
      this.editorPanel.setFocusTraversalPolicy(var1);
      this.editorPanel.setFocusCycleRoot(var1 != null);
   }

   public WaitService getWaitService() {
      return this.waitService;
   }

   @Override
   public UndoService getUndoService() {
      return this.contributeGlobalUndoRedo ? this.undoService : new NoUndoService();
   }

   @Override
   public final void decorate(UiCanvas var1) {
      this.decorateImpl(var1, true);
   }

   @Override
   public final boolean decorateWithoutToolbar(UiCanvas var1) {
      this.decorateImpl(var1, false);
      return true;
   }

   private void decorateImpl(UiCanvas var1, boolean var2) {
      if (this.editorPanel.getComponentCount() == 0) {
         this.layoutEditorPanel(var2);
         if (this.overlayContainer != null) {
            this.configureLayeredPane();
         }
      }

      if (this.overlayContainer != null) {
         var1.display(this.overlayContainer);
      } else {
         var1.display(this.editorPanel);
      }
   }

   private void configureLayeredPane() {
      assert this.useLayeredPane();
      this.overlayContainer.setBaseComponent(this.editorPanel);
      this.overlayContainer.setBaseInsets(new Insets(20, 5, 5, 5));
      this.overlayContainer.addListener(new GenericEditor.OverlayNotificationSender());
   }

   public final void setUseScroll(boolean var1) {
      this.useScroll = var1;
   }

   protected boolean useLayeredPane() {
      return true;
   }

   public OverlayContainer getOverlayContainer() {
      return this.overlayContainer;
   }

   private void layoutEditorPanel(boolean var1) {
      Preconditions.checkState(this.mainPanel != null, "The main panel has not been added.");
      StyleSheet.GenericEditorCanvas.makeOver(this.editorPanel);
      if (var1 && this.titlePanel != null) {
         this.titlePanel.ui.setOpaque(false);
         this.editorPanel.add(this.titlePanel.ui, "North");
         FocusUtils.makeComponentMouseFocusable(this.titlePanel.ui);
      }

      this.mainPanel.setOpaque(true);
      JComponent var2;
      if (this.useScroll) {
         KongaScrollPane var3 = new KongaScrollPane(this.mainPanel);
         var3.setBorder(null);
         var2 = var3;
      } else {
         var2 = this.mainPanel;
         if (!var1) {
            this.mainPanel.setBorder(null);
         }
      }

      this.editorPanel.add(var2, "Center");
      FocusUtils.makeComponentMouseFocusable(this.editorPanel);
      FocusUtils.makeComponentMouseFocusable(this.mainPanel);
   }

   public final void setEditorBorder(Border var1) {
      this.editorBorder = var1;
      if (this.mainPanel != null) {
         this.mainPanel.setBorder(var1);
      }
   }

   public void setDirty() {
      boolean var1 = this.isDirty();
      super.setDirty(true);
      if (!var1) {
         this.firePropertyChange(new PropertyChangeEvent(this, "dirty", Boolean.FALSE, Boolean.TRUE));
      }
   }

   public void setClean() {
      boolean var1 = this.isDirty();
      super.setDirty(false);
      if (var1) {
         this.firePropertyChange(new PropertyChangeEvent(this, "dirty", Boolean.TRUE, Boolean.FALSE));
      }

      if (this.currentSavePointDistance > 0) {
         this.savePointDistances.add(this.currentSavePointDistance);
         this.currentSavePointDistance = 0;
      }
   }

   @Override
   protected void activated() {
      super.activated();
      if (this.managesItsOwnFocusPolicy()) {
         this.restoreFocus(0);
      }

      if (this.commands != null) {
         this.getCommandConsole().addCommand(this.commands);
      }
   }

   @Override
   protected boolean managesItsOwnFocusPolicy() {
      return EditorServiceUiModePreference.get() != EditorServiceUiMode.FLOATING;
   }

   public final void restoreFocus(int var1) {
      if (this.focusMemento != null) {
         this.focusMemento.restore(var1);
      }
   }

   private CommandConsole getCommandConsole() {
      ApplicationWindow var1 = this.getWindow();
      return var1.getCommandConsole();
   }

   @Override
   protected void deactivated() {
      super.deactivated();
      if (this.commands != null) {
         this.getCommandConsole().removeCommand(this.commands);
      }
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      if (!this.displayed) {
         this.installFocusMemento();
         super.addToWindow(var1);
         this.displayed = true;
         if (this.mainPanel != null) {
            this.installMainPanelMouseListener();
         }
      }
   }

   private void installFocusMemento() {
      this.focusMemento = new FocusMemento(this.editorPanel);
      this.addResource(new DisposableWindowElementResource(this.focusMemento));
   }

   private void prepareForRemoval() {
      if (this.displayed) {
         this.uninstallMainPanelMouseListener();
         this.displayed = false;
      }
   }

   private void installMainPanelMouseListener() {
      this.mouseListener = new GenericEditor.MouseListenerImpl();
      this.mainPanel.addMouseListener(this.mouseListener);
   }

   private void uninstallMainPanelMouseListener() {
      if (this.mainPanel != null && this.mouseListener != null) {
         this.mainPanel.removeMouseListener(this.mouseListener);
      }
   }

   private boolean triggerSave() {
      try {
         this.save();
         return true;
      } catch (SaveEditorException var2) {
         ErrorLog.attention(this.getClass(), "Failed to save", var2);
         return false;
      }
   }

   @Override
   public boolean removeFromWindow(boolean var1) {
      if (!var1 && !this.allowsRemoval()) {
         return false;
      }

      this.prepareForRemoval();
      return super.removeFromWindow(var1);
   }

   private boolean allowsRemoval() {
      if (this.isDirty()) {
         switch (this.removePolicyWhenDirty) {
            case 0:
               return true;
            case 1:
               return this.triggerSave();
            case 2:
               return this.promptForSave();
            case 3:
               return false;
         }
      }

      return true;
   }

   private boolean promptForSave() {
      Answer var1 = Ask.yesNoOrCancel(this.editorPanel, "Do you want to save your changes?", "Save?");
      if (var1.isYes()) {
         return this.triggerSave();
      } else if (var1.isNo()) {
         return true;
      } else if (var1.isCancel()) {
         return false;
      } else {
         throw new IllegalStateException("Unexpected answer: " + var1);
      }
   }

   public void setRemovalPolicyWhenDirty(int var1) {
      switch (var1) {
         case 0:
         case 1:
         case 2:
         case 3:
            this.removePolicyWhenDirty = var1;
            return;
         default:
            throw new IllegalArgumentException(
               "policy must be one of DO_NOTHING_ON_REMOVAL, SAVE_SILENTLY_ON_REMOVAL, PROMPT_FOR_SAVE_ON_REMOVAL, or ALWAYS_VETO_REMOVAL."
            );
      }
   }

   public int getRemovalPolicyWhenDirty() {
      return this.removePolicyWhenDirty;
   }

   public final void setToolbarPopupMenuActions(List<Action> var1) {
      this.toolbarPopupMenuActions = Lists.newArrayList(var1);
   }

   protected final PopupMenuButton createPopupMenuToolBarButton() {
      JPopupMenu var1 = this.getToolbarPopupMenu();
      return var1 == null ? null : createDropDownMenuButton(var1);
   }

   protected JPopupMenu getToolbarPopupMenu() {
      if (this.toolbarPopupMenuActions.isEmpty()) {
         return null;
      }

      KongaPopupMenu var1 = new KongaPopupMenu();
      var1.addActions(this.toolbarPopupMenuActions);
      return var1;
   }

   @Override
   public String getHelpId() {
      return this.helpId;
   }

   public final void setHelpId(String var1) {
      if (!StringUtils.equals(this.helpId, var1)) {
         this.helpId = var1;
         this.rebindHelpButton();
      }
   }

   protected final void rebindHelpButton() {
      if (this.helpButton != null) {
         ApplicationUiHelp.getHelp().enableHelpOnButton(this.helpButton, this.getHelpId());
      }
   }

   private JButton createCloseButton() {
      return createToolBarButton(new CloseEditorAction(this));
   }

   protected JButton createHelpButton(String var1) {
      if (var1 != null) {
         this.helpButton = HelpButtonResource.forToolbar(this, var1);
      }

      return this.helpButton;
   }

   protected ApplicationToolBar createToolBar() {
      ArrayList var1 = Lists.newArrayList(this.toolbarButtons);
      String var2 = this.getHelpId();
      if (var2 != null) {
         JButton var3 = this.createHelpButton(var2);
         if (var3 != null) {
            var1.add(var3);
            var1.add(null);
         }
      }

      if (this.includeCloseButton) {
         var1.add(this.createCloseButton());
      }

      return !var1.isEmpty() ? new ApplicationToolBar((JButton[]) var1.toArray(new JButton[var1.size()])) : null;
   }

   public final List<JButton> getToolbarButtons() {
      return Lists.newArrayList(this.toolbarButtons);
   }

   public final void setToolbarButtons(List<JButton> var1) {
      this.toolbarButtons = Lists.newArrayList(var1);
   }

   public final void addToolbarButton(JButton var1) {
      this.toolbarButtons.add(var1);
   }

   public final void setIncludeCloseButton(boolean var1) {
      this.includeCloseButton = var1;
   }

   @Override
   public Object getObject() {
      return null;
   }

   public boolean canRedo() {
      return this.undoManager.canRedo();
   }

   public boolean canUndo() {
      return this.undoManager.canUndo();
   }

   public void redoEdit() {
      if (this.canRedo()) {
         this.undoManager.redo();
         this.currentSavePointDistance++;
         this.setDirty();
      }
   }

   public void undoEdit() {
      if (this.canUndo()) {
         this.undoManager.undo();
         if (this.currentSavePointDistance == 0) {
            if (this.savePointDistances.isEmpty()) {
               this.setClean();
            } else {
               this.currentSavePointDistance = this.savePointDistances.pop();
               this.setDirty();
            }
         } else {
            this.currentSavePointDistance--;
            if (this.currentSavePointDistance == 0) {
               this.setClean();
            } else {
               this.setDirty();
            }
         }
      }
   }

   @Override
   public void undoableEditHappened(UndoableEditEvent var1) {
      if (this.contributeGlobalUndoRedo) {
         if (!this.isCompoundUndo) {
            UndoableEdit var2 = var1.getEdit();
            if (this.isCompoundEdit) {
               this.addToCompoundEdit(var2);
            } else {
               this.addEditToUndoManager(var2);
            }
         }
      }
   }

   private void addToCompoundEdit(UndoableEdit var1) {
      if (this.compoundEdit == null) {
         this.compoundEdit = new GenericEditor.EditorCompoundEdit();
      }

      this.compoundEdit.addEdit(var1);
   }

   private void addEditToUndoManager(UndoableEdit var1) {
      this.undoManager.addEdit(var1);
      this.undoService.undoableEditHappened();
      this.currentSavePointDistance++;
   }

   public final void startCompoundEdit() {
      this.isCompoundEdit = true;
   }

   public final void endCompoundEdit() {
      if (this.isCompoundEdit) {
         this.isCompoundEdit = false;
         if (this.compoundEdit != null) {
            this.compoundEdit.end();
            this.addEditToUndoManager(this.compoundEdit);
            this.compoundEdit = null;
         }
      }
   }

   protected final void setToolBar(ApplicationToolBar var1) {
      if (this.titlePanel != null) {
         this.titlePanel.setToolBar(var1);
      }
   }

   protected ApplicationToolBar getToolBar() {
      return this.titlePanel == null ? null : this.titlePanel.getToolBar();
   }

   public final void setCommands(CommandModule var1) {
      if (var1 != this.commands) {
         if (this.getWindow() != null && this.getState().contains(WindowElementState.ACTIVE)) {
            if (this.commands != null) {
               this.getCommandConsole().removeCommand(this.commands);
            }

            if (var1 != null) {
               this.getCommandConsole().addCommand(var1);
            }
         }

         this.commands = var1;
      }
   }

   @Override
   public Image render(Dimension var1) {
      Preconditions.checkArgument(var1.width > 0 || var1.height > 0, "At least one of width or height must be > 0");
      Dimension var2 = this.getImageClipSize();
      Rectangle var3 = new Rectangle(0, 0, var2.width, var2.height);
      JComponent var4 = this.mainPanel != null ? this.mainPanel : this.editorPanel;
      return render(var1, var3, var4);
   }

   private Dimension getImageClipSize() {
      Dimension var1 = this.editorPanel.getSize();
      if (var1.width == 0 || var1.height == 0) {
         var1 = new Dimension(100, 100);
      }

      return var1;
   }

   protected static final JButton createToolBarButton(Action var0) {
      return KongaButton.forToolbar(var0).setUseSmallActionIcon(true);
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder(this.getClass().getSimpleName());
      var1.append(" [").append(this.mainTitle);
      if (this.subTitle != null && this.subTitle.length() > 0) {
         var1.append(" (").append(this.subTitle).append(")");
      }

      var1.append("]");
      return var1.toString();
   }

   private static PopupMenuButton createDropDownMenuButton(JPopupMenu var0) {
      PopupMenuButton var1 = new PopupMenuButton("", ApplicationResources.ICONS.getIcon("ArrowDown.16"), var0);
      var1.setToolTipText(ApplicationResources.STRINGS.getString("Menu"));
      var1.setOpaque(false);
      ButtonUtils.enableRolloverForButton(var1);
      return var1;
   }

   private class EditorCompoundEdit extends CompoundEdit {
      private final Stack<UndoableEdit> edits = new Stack<>();

      private EditorCompoundEdit() {
      }

      @Override
      public void undo() throws CannotUndoException {
         GenericEditor.this.isCompoundUndo = true;

         try {
            super.undo();
         } finally {
            GenericEditor.this.isCompoundUndo = false;
         }
      }

      @Override
      public boolean addEdit(UndoableEdit var1) {
         if (this.isInProgress()) {
            this.edits.add(var1);
            return true;
         } else {
            return super.addEdit(var1);
         }
      }

      @Override
      public void end() {
         while (!this.edits.isEmpty()) {
            super.addEdit(this.edits.pop());
         }

         super.end();
      }
   }

   private class KeyDispatchResource implements WindowElementResource {
      private final KeyEventDispatcher keyDispatcher;

      public KeyDispatchResource(KeyEventDispatcher var2) {
         this.keyDispatcher = var2;
      }

      @Override
      public WindowElementStateSpan getLifeSpan() {
         return WindowElementStateSpan.WHEN_ACTIVE;
      }

      @Override
      public void create(WindowElement var1) {
         KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.keyDispatcher);
      }

      @Override
      public void dispose(WindowElement var1) {
         KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.keyDispatcher);
      }
   }

   private class KeyStrokeListener implements KeyEventDispatcher {
      private KeyStrokeListener() {
      }

      @Override
      public boolean dispatchKeyEvent(KeyEvent var1) {
         return EventUtils.isSourceDescendingFrom(GenericEditor.this.mainPanel, var1)
            ? GenericEditor.this.getController().processKeyStroke(GenericEditor.this, KeyStroke.getKeyStrokeForEvent(var1))
            : false;
      }
   }

   private class MouseListenerImpl implements MouseListener {
      private MouseListenerImpl() {
      }

      @Override
      public void mouseClicked(MouseEvent var1) {
         GenericEditor.this.getController().processMouseEvent(GenericEditor.this, var1);
      }

      @Override
      public void mouseEntered(MouseEvent var1) {
         GenericEditor.this.getController().processMouseEvent(GenericEditor.this, var1);
      }

      @Override
      public void mouseExited(MouseEvent var1) {
         GenericEditor.this.getController().processMouseEvent(GenericEditor.this, var1);
      }

      @Override
      public void mousePressed(MouseEvent var1) {
         GenericEditor.this.getController().processMouseEvent(GenericEditor.this, var1);
      }

      @Override
      public void mouseReleased(MouseEvent var1) {
         GenericEditor.this.getController().processMouseEvent(GenericEditor.this, var1);
      }
   }

   private class OverlayNotificationSender extends OverlayContainerListener.Adapter {
      private OverlayNotificationSender() {
      }

      @Override
      public void overlayAdded(OverlayContainerEvent var1) {
         if (var1.getOverlay().getClientProperty("konga.overlay.blocking") == Boolean.TRUE) {
            GenericEditor.this.firePropertyChange("notification", Boolean.FALSE, Boolean.TRUE);
         }
      }
   }

   private class TitlePanel {
      private JLabel iconLabel;
      private ApplicationToolBar toolbar;
      private final JPanel ui;
      public JLabel subTitleLabel;
      private TextStyle subTitleStyle = TextStyles.GenericEditorTitleSub;

      TitlePanel() {
         this.ui = new JPanel();
         this.layoutPanel();
         this.ui.setBorder(StyleSheet.GenericEditorTitlePanel.getBorder());
      }

      private void layoutPanel() {
         this.ui.removeAll();
         this.ui.setLayout(new GenericEditorTitlePanelLayout());
         this.ui.add(this.getLabelPanel(), "labels");
         if (GenericEditor.this.additionalTitleDecoration != null) {
            this.ui.add(this.getAdditionalDecoration(), "additional");
         }

         if (this.toolbar != null) {
            this.ui.add(this.toolbar.getPanel(), "toolbar");
         }

         this.ui.validate();
         this.ui.repaint();
      }

      private JComponent getAdditionalDecoration() {
         OneRowPanel var1 = new OneRowPanel();
         var1.setFillOut();
         var1.add(Box.createHorizontalGlue());
         var1.setFillNone();
         var1.setAnchor(OneRowPanel.EAST);
         var1.add(GenericEditor.this.additionalTitleDecoration);
         var1.setOpaque(false, false);
         return BorderLayoutBuilder.fillWith(var1);
      }

      public void setToolBar(ApplicationToolBar var1) {
         this.toolbar = var1;
         this.layoutPanel();
      }

      public ApplicationToolBar getToolBar() {
         return this.toolbar;
      }

      public void setIcon(Icon var1) {
         this.iconLabel = new JLabel(var1);
         this.layoutPanel();
      }

      public void setSubTitleStyle(TextStyle var1) {
         this.subTitleStyle = var1;
         if (this.subTitleLabel != null) {
            var1.makeOver(this.subTitleLabel);
         }
      }

      private JComponent getLabelPanel() {
         JPanel var1 = new JPanel(new GenericEditorTitleLabelLayout());
         if (this.iconLabel != null) {
            var1.add(this.iconLabel);
            var1.add(Box.createHorizontalStrut(5));
         }

         if (GenericEditor.this.includeMainTitleInTitleBar()) {
            KongaLabel var2 = TextStyles.GenericEditorTitleMain.makeLabel(GenericEditor.this.mainTitle);
            var1.add(var2);
            if (this.hasBothMainAndSubTitle()) {
               var1.add(TextStyles.GenericEditorTitleMain.makeLabel(":  "));
            }
         }

         if (StringUtils.isNotEmpty(GenericEditor.this.subTitle)) {
            this.subTitleLabel = this.subTitleStyle.makeLabel(GenericEditor.this.subTitle);
            this.subTitleLabel.setToolTipText(GenericEditor.this.subTitle);
            var1.add(this.subTitleLabel);
         }

         var1.setOpaque(false);
         return var1;
      }

      private boolean hasBothMainAndSubTitle() {
         return GenericEditor.this.includeMainTitleInTitleBar()
            && StringUtils.isNotEmpty(GenericEditor.this.mainTitle)
            && StringUtils.isNotEmpty(GenericEditor.this.subTitle);
      }
   }

   private class UndoServiceImpl extends DefaultUndoService {
      public UndoServiceImpl(UndoManager var2) {
         super(var2);
      }

      @Override
      public String getRedoRepresentationName() {
         return super.getRedoRepresentationName();
      }

      @Override
      public String getUndoRepresentationName() {
         return super.getUndoRepresentationName();
      }

      @Override
      public void redoEdit() throws CannotRedoException {
         if (super.canRedo()) {
            GenericEditor.this.redoEdit();
            this.fireStateChanged();
         }
      }

      @Override
      public void undoEdit() throws CannotUndoException {
         if (super.canUndo()) {
            GenericEditor.this.undoEdit();
            this.fireStateChanged();
         }
      }

      public void undoableEditHappened() {
         this.fireStateChanged();
      }

      @Override
      public void discardAllEdits() {
         super.discardAllEdits();
         this.fireStateChanged();
      }
   }
}
