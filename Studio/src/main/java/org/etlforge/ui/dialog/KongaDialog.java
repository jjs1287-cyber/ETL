package org.etlforge.ui.dialog;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.undo.UndoRedoController;
import org.etlforge.ui.util.ButtonOrder;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.wait.WindowWaitService;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.text.undo.TextUndoRedoControllerFactory;

public class KongaDialog extends JDialog implements WaitService {
   public static final KongaDialog.ButtonMode OK_ONLY = KongaDialog.ButtonMode.OK_ONLY_IMPL;
   public static final KongaDialog.ButtonMode OK_CANCEL = KongaDialog.ButtonMode.OK_CANCEL_IMPL;
   public static final KongaDialog.ButtonMode OK_CANCEL_APPLY = KongaDialog.ButtonMode.OK_CANCEL_APPLY_IMPL;
   public static final KongaDialog.ButtonMode CONTINUE_CANCEL = KongaDialog.ButtonMode.CONTINUE_CANCEL_IMPL;
   public static final KongaDialog.ButtonMode CLOSE_ONLY = KongaDialog.ButtonMode.CLOSE_ONLY_IMPL;
   public static final KongaDialog.ButtonMode CANCEL_ONLY = KongaDialog.ButtonMode.CANCEL_ONLY_IMPL;
   private static final String ESCAPE_ACTION = "pressed_escape";
   private static final DialogLocationManager locationManager = new DialogLocationManager();
   private JButton okButton;
   private JButton cancelButton;
   private JButton applyButton;
   private JButton continueButton;
   private JButton closeButton;
   private boolean okWasPressed;
   private boolean wasCancelled;
   private boolean noWasPressed;
   private Dimension buttonSize;
   private Object focusedComponent;
   private boolean fixedButtonSize = true;
   private boolean cancelOnEsc = false;
   private WaitService waitService;
   private boolean disposeWhenClosed;
   private List<Supplier<Boolean>> okConditions;

   public KongaDialog(String var1) {
      this((Frame)null, var1, false);
   }

   public KongaDialog(Frame var1, String var2) {
      this(var1, var2, false);
   }

   public KongaDialog(String var1, boolean var2) {
      this((Frame)null, var1, var2);
   }

   public KongaDialog(Frame var1, String var2, boolean var3) {
      super(var1 != null ? var1 : new JFrame(), var2, var3);
      this.init();
   }

   public KongaDialog(Dialog var1, String var2, Image var3, boolean var4) {
      super(var1, var2, var4);
      this.init();
   }

   public KongaDialog(Window var1, String var2) {
      this(var1, var2, ModalityType.APPLICATION_MODAL);
   }

   public KongaDialog(Window var1, String var2, ModalityType var3) {
      super(var1, var2, var3);
      this.init();
   }

   public KongaDialog(Window var1, String var2, ModalityType var3, GraphicsConfiguration var4) {
      super(var1, var2, var3, var4);
      this.init();
   }

   public final void setFocusedComponent(JComponent var1) {
      this.focusedComponent = var1;
   }

   public final void setFocusedComponent(UiProvider var1) {
      this.focusedComponent = var1;
   }

   public final void manageLocation() {
      locationManager.manageDialog(this, (String)null);
      locationManager.setInitialLocation(this);
   }

   public final void manageLocation(Class<?> var1) {
      locationManager.manageDialog(this, var1);
      locationManager.setInitialLocation(this);
   }

   public final void setFixedButtonSize(boolean var1) {
      this.fixedButtonSize = var1;
   }

   public final void setDefaultButton(Action var1) {
      JButton var2 = ButtonUtils.findButtonForAction(var1, this.getContentPane());
      this.getRootPane().setDefaultButton(var2);
   }

   public UndoRedoController enableUndoRedoForTextComponents(TextUndoRedoControllerFactory var1) {
      return DialogUtils.enableUndoRedoForTextComponents(this, var1);
   }

   private void init() {
      this.okConditions = new CopyOnWriteArrayList<>();
      this.disposeWhenClosed = true;
      this.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent var1) {
            KongaDialog.this.onDialogClose();
         }

         @Override
         public void windowOpened(WindowEvent var1) {
            KongaDialog.this.onDialogOpened();
         }
      });
      this.waitService = this.createWaitService();
      this.setCancelOnEscape(true);
      this.setButtonDimensions();
   }

   public final void setDisposeWhenClosed(boolean var1) {
      this.disposeWhenClosed = var1;
   }

   public final void setCancelOnEscape(boolean var1) {
      if (var1 != this.cancelOnEsc) {
         this.cancelOnEsc = var1;
         if (var1) {
            this.bindEscape();
         } else {
            this.unbindEscape();
         }
      }
   }

   private void bindEscape() {
      this.getRootPane().getInputMap(2).put(KeyUtils.ESCAPE, "pressed_escape");
      this.getRootPane().getActionMap().put("pressed_escape", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            for (Window var5 : Window.getWindows()) {
               if (var5.getOwner() == KongaDialog.this && var5.isVisible()) {
                  return;
               }
            }

            KongaDialog.this.onCancel();
         }
      });
   }

   private void unbindEscape() {
      this.getRootPane().getInputMap(1).remove(KeyUtils.ESCAPE);
      this.getRootPane().getActionMap().remove("pressed_escape");
   }

   public boolean wasOkPressed() {
      return this.okWasPressed;
   }

   protected final void setWasOkPressed(boolean var1) {
      this.okWasPressed = var1;
   }

   public boolean wasCancelled() {
      return this.wasCancelled;
   }

   protected final void setWasCancelled(boolean var1) {
      this.wasCancelled = var1;
   }

   public boolean wasNoPressed() {
      return this.noWasPressed;
   }

   @Override
   public WaitLock startWait() {
      return this.waitService.startWait();
   }

   @Override
   public WaitLock startWait(int var1) {
      return this.waitService.startWait(var1);
   }

   protected WaitService createWaitService() {
      return new WindowWaitService(this);
   }

   public JButton getOKButton() {
      return this.okButton;
   }

   public JButton getContinueButton() {
      return this.continueButton;
   }

   public JButton getCancelButton() {
      return this.cancelButton;
   }

   public JButton getApplyButton() {
      return this.applyButton;
   }

   public JButton getCloseButton() {
      return this.closeButton;
   }

   public JButton createOKButton() {
      AbstractAction var1 = new AbstractAction(this.getOkButtonText()) {
         @Override
         public void actionPerformed(ActionEvent var1) {
            KongaDialog.this.onOk();
         }
      };
      this.okButton = this.createStandardButtonImpl(var1);
      return this.okButton;
   }

   protected String getOkButtonText() {
      return UiResourceBundle.getString("OK");
   }

   public JButton createContinueButton() {
      AbstractAction var1 = new AbstractAction(UiResourceBundle.getString("Continue")) {
         @Override
         public void actionPerformed(ActionEvent var1) {
            KongaDialog.this.onContinue();
         }
      };
      this.continueButton = this.createStandardButtonImpl(var1);
      return this.continueButton;
   }

   public JButton createCancelButton() {
      AbstractAction var1 = new AbstractAction(UiResourceBundle.getString("Cancel")) {
         @Override
         public void actionPerformed(ActionEvent var1) {
            KongaDialog.this.onCancel();
         }
      };
      this.cancelButton = this.createStandardButtonImpl(var1);
      return this.cancelButton;
   }

   public JButton createApplyButton() {
      AbstractAction var1 = new AbstractAction(UiResourceBundle.getString("Apply")) {
         @Override
         public void actionPerformed(ActionEvent var1) {
            KongaDialog.this.onApply();
         }
      };
      this.applyButton = this.createStandardButtonImpl(var1);
      return this.applyButton;
   }

   public JButton createCloseButton() {
      AbstractAction var1 = new AbstractAction(UiResourceBundle.getString("Close")) {
         @Override
         public void actionPerformed(ActionEvent var1) {
            KongaDialog.this.onClose();
         }
      };
      this.closeButton = this.createStandardButtonImpl(var1);
      return this.closeButton;
   }

   private JButton createStandardButtonImpl(Action var1) {
      return this.fixedButtonSize ? getButton(var1, this.buttonSize) : new KongaButton(var1);
   }

   protected void onDialogClose() {
      this.removeFromLocationManager();
      if (this.disposeWhenClosed) {
         this.dispose();
      } else {
         this.setVisible(false);
      }
   }

   public final void removeFromLocationManager() {
      locationManager.removeDialog(this);
   }

   protected void onDialogOpened() {
      if (this.focusedComponent instanceof JComponent) {
         ((JComponent)this.focusedComponent).requestFocusInWindow();
      } else if (this.focusedComponent instanceof UiProvider) {
         ((UiProvider)this.focusedComponent).requestFocus();
      }
   }

   protected void onOk() {
      boolean var1 = this.askOkConditions();
      if (var1) {
         this.okWasPressed = true;
         this.onDialogClose();
      }
   }

   protected final boolean askOkConditions() {
      for (Supplier var2 : this.okConditions) {
         boolean var3 = (Boolean)var2.get();
         if (!var3) {
            return false;
         }
      }

      return true;
   }

   protected void onContinue() {
      this.onOk();
   }

   protected void onCancel() {
      this.wasCancelled = true;
      this.okWasPressed = false;
      this.onDialogClose();
   }

   protected void onApply() {
      this.onOk();
   }

   protected void onClose() {
      this.okWasPressed = false;
      this.onDialogClose();
   }

   public JPanel getButtonPanel(KongaDialog.ButtonMode var1, int var2) {
      return this.getButtonPanel(var1, -1, var2, 0);
   }

   public JPanel getButtonPanel(KongaDialog.ButtonMode var1, int var2, int var3, int var4) {
      List var5 = var1.getButtons(this);
      return this.getButtonPanel(var5, var2, var3, var4);
   }

   public JPanel getButtonPanel(List<? extends JButton> var1, int var2, int var3, int var4) {
      BoxBuilder var5 = BoxBuilder.horizontal();
      if (var2 < 0) {
         var5.glue();
      } else if (var2 > 0) {
         var5.strut(var2);
      }

      var5.addAllWithSpace(var3, var1);
      if (var4 < 0) {
         var5.glue();
      } else if (var4 > 0) {
         var5.strut(var4);
      }

      return var5.container();
   }

   public JPanel getButtonPanel(JButton[] var1, int var2, int var3, int var4) {
      return this.getButtonPanel(Arrays.asList(var1), var2, var3, var4);
   }

   private void setButtonDimensions() {
      JButton var1 = new JButton("X");
      FontMetrics var2 = this.getFontMetrics(var1.getFont());
      String var3 = UiResourceBundle.getString("Apply");
      String var4 = UiResourceBundle.getString("Cancel");
      String var5 = UiResourceBundle.getString("Continue");
      int var6 = 40 + Math.max(var2.stringWidth(var5), Math.max(var2.stringWidth(var3), var2.stringWidth(var4)));
      int var7 = var1.getPreferredSize().height;
      this.buttonSize = new Dimension(var6, var7);
   }

   private static JButton getButton(Action var0, final Dimension var1) {
      return new KongaButton(var0) {
         @Override
         public Dimension getPreferredSize() {
            return var1;
         }
      };
   }

   public void showLater() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            KongaDialog.this.setVisible(true);
         }
      });
   }

   public void setOkButtonAsDefault() {
      this.getRootPane().setDefaultButton(this.getOKButton());
   }

   public void setContinueButtonAsDefault() {
      this.getRootPane().setDefaultButton(this.getContinueButton());
   }

   public void setCloseButtonAsDefault() {
      this.getRootPane().setDefaultButton(this.getCloseButton());
   }

   public final BorderLayoutBuilder standardLayout(JComponent var1, KongaDialog.ButtonMode var2) {
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 20);
      var3.center(var1);
      return this.standardLayout(var3, var2);
   }

   public final BorderLayoutBuilder standardLayout(UiProvider var1, KongaDialog.ButtonMode var2) {
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 20);
      var3.center(var1);
      return this.standardLayout(var3, var2);
   }

   public final BorderLayoutBuilder standardLayout(BorderLayoutBuilder var1, KongaDialog.ButtonMode var2) {
      if (var2 != null) {
         JPanel var3 = this.getButtonPanel(var2, 10);
         var1.south(var3);
      }

      var1.withBorder(Empty.border(10));
      var1.asContentPaneFor(this);
      this.pack();
      return var1;
   }

   public final ActionListener getOkActionListener() {
      Preconditions.checkState(this.getOKButton() != null, "This dialog does not have an OK button.");
      return new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            if (KongaDialog.this.getOKButton().isEnabled()) {
               KongaDialog.this.onOk();
            }
         }
      };
   }

   public final void addOkCondition(Supplier<Boolean> var1) {
      this.okConditions.add(var1);
   }

   public final void removeOkConditin(Supplier<Boolean> var1) {
      this.okConditions.remove(var1);
   }

   @Override
   public final void dispose() {
      this.removeFromLocationManager();
      this.doAdditionalWorkOnDispose();
      super.dispose();
   }

   protected void doAdditionalWorkOnDispose() {
   }

   public abstract static class ButtonMode {
      private static final KongaDialog.ButtonMode OK_ONLY_IMPL = new KongaDialog.ButtonMode() {
         @Override
         List<JButton> getButtons(KongaDialog var1) {
            return Collections.singletonList(var1.createOKButton());
         }
      };
      private static final KongaDialog.ButtonMode OK_CANCEL_IMPL = new KongaDialog.ButtonMode() {
         @Override
         List<JButton> getButtons(KongaDialog var1) {
            JButton var2 = var1.createOKButton();
            JButton var3 = var1.createCancelButton();
            return ButtonOrder.okAndCancelOrder(var2, var3);
         }
      };
      private static final KongaDialog.ButtonMode CONTINUE_CANCEL_IMPL = new KongaDialog.ButtonMode() {
         @Override
         List<JButton> getButtons(KongaDialog var1) {
            JButton var2 = var1.createContinueButton();
            JButton var3 = var1.createCancelButton();
            return ButtonOrder.okAndCancelOrder(var2, var3);
         }
      };
      private static final KongaDialog.ButtonMode OK_CANCEL_APPLY_IMPL = new KongaDialog.ButtonMode() {
         @Override
         List<JButton> getButtons(KongaDialog var1) {
            return ButtonOrder.okCancelAndApplyOrder(var1.createOKButton(), var1.createCancelButton(), var1.createApplyButton());
         }
      };
      private static final KongaDialog.ButtonMode CLOSE_ONLY_IMPL = new KongaDialog.ButtonMode() {
         @Override
         List<JButton> getButtons(KongaDialog var1) {
            return Collections.singletonList(var1.createCloseButton());
         }
      };
      private static final KongaDialog.ButtonMode CANCEL_ONLY_IMPL = new KongaDialog.ButtonMode() {
         @Override
         List<JButton> getButtons(KongaDialog var1) {
            return Collections.singletonList(var1.createCancelButton());
         }
      };

      abstract List<JButton> getButtons(KongaDialog var1);

      private ButtonMode() {
      }
   }
}
