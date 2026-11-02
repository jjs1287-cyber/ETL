package org.etlforge.application.ui.filestore;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.filestore.FileCollisionResolution;
import org.etlforge.application.filestore.FileCollisionResolver;
import org.etlforge.application.filestore.FileStoreFile;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.option.OptionSelector;
import org.etlforge.ui.util.option.ToggleButtonOptionSelector;
import org.etlforge.ui.widget.button.KongaRadioButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.ui.widget.text.TextComponentUtils;

final class FileCollisionResolverImpl implements FileCollisionResolver {
   private static final ResolutionOption DEFAULT_OPTION = ResolutionOption.REPLACE_EXISTING;
   private final ApplicationWindow owner;

   public FileCollisionResolverImpl(ApplicationWindow var1) {
      this.owner = var1;
   }

   @Override
   public FileCollisionResolution resolve(FileStoreFile var1, String var2) {
      return this.resolveImpl(var1);
   }

   @Override
   public FileCollisionResolution resolve(FileStoreFile var1, byte[] var2) {
      return this.resolveImpl(var1);
   }

   private FileCollisionResolution resolveImpl(FileStoreFile var1) {
      FileCollisionResolverImpl.Ui var2 = new FileCollisionResolverImpl.Ui(this.owner != null ? this.owner.getFrame() : UiUtils.getActiveFrame(), var1);
      var2.publish();
      return var2.getResolution();
   }

   private static final class Ui extends KongaDialog {
      private List<OptionSelector<ResolutionOption>> optionSelectors;
      private JRadioButton renameNewButton;
      private JTextField newNameField;

      public Ui(JFrame var1, FileStoreFile var2) {
         super(var1, Strings.get("FileStore.Resolver.Title"), true);
         this.setCancelOnEscape(false);
         this.createComponents(var2);
         this.setResizable(false);
         this.manageLocation();
      }

      public void publish() {
         if (EventQueue.isDispatchThread()) {
            this.setVisible(true);
         } else {
            try {
               EventQueue.invokeAndWait(new Runnable() {
                  @Override
                  public void run() {
                     Ui.this.setVisible(true);
                  }
               });
            } catch (InterruptedException var2) {
               Thread.currentThread().interrupt();
               ErrorLog.log(FileCollisionResolverImpl.class, "The thread was interrupted while waiting for file collision resolver dialog to complete", null);
            } catch (InvocationTargetException var3) {
               ErrorLog.log(FileCollisionResolverImpl.class, "An error occurred in the file collision resolver dialog", var3.getCause());
            }
         }
      }

      public FileCollisionResolution getResolution() {
         ResolutionOption var1 = this.getSelectedOption();
         FileCollisionResolution.Action var2 = var1.getAction();
         String var3 = var2 == FileCollisionResolution.Action.RENAME_AND_USE_NEW ? this.newNameField.getText() : null;
         return this.getResolution(var2, var3);
      }

      private ResolutionOption getSelectedOption() {
         for (OptionSelector var2 : this.optionSelectors) {
            if (var2.isSelected()) {
               return (ResolutionOption)var2.getOption();
            }
         }

         return null;
      }

      private FileCollisionResolution getResolution(final FileCollisionResolution.Action var1, final String var2) {
         return new FileCollisionResolution() {
            @Override
            public FileCollisionResolution.Action getAction() {
               return var1;
            }

            @Override
            public String getNewName() {
               return var2;
            }
         };
      }

      private void createComponents(FileStoreFile var1) {
         JComponent var2 = this.createDisplayPanel();
         GridBagConstraints var3 = this.getInitialConstraints();
         this.createCaptions(var1, var2, var3);
         this.createSelectors(var2, var3);
         this.createTextField(var1, var2, var3);
         UiUtils.setOpaqueRecursivelySkipDefaults(var2, false);
         this.standardLayout(var2, OK_ONLY);
         this.setOkButtonAsDefault();
      }

      private JComponent createDisplayPanel() {
         return new JPanel(new GridBagLayout());
      }

      private GridBagConstraints getInitialConstraints() {
         GridBagConstraints var1 = new GridBagConstraints();
         var1.anchor = 18;
         var1.insets = new Insets(5, 5, 5, 5);
         var1.fill = 2;
         var1.weightx = 1.0;
         var1.gridx = 0;
         return var1;
      }

      private void createCaptions(FileStoreFile var1, JComponent var2, GridBagConstraints var3) {
         KongaLabel var4 = TextStyles.AdminHintBold.makeLabel(getCaption1(var1));
         var2.add(var4, var3);
         KongaLabel var5 = TextStyles.AdminHint.makeLabel(Strings.get("FileStore.Resolver.Caption2"));
         var2.add(var5, var3);
      }

      private static String getCaption1(FileStoreFile var0) {
         return Strings.format("FileStore.Resolver.Caption1", var0.getPath());
      }

      private void createSelectors(JComponent var1, GridBagConstraints var2) {
         ButtonGroup var3 = new ButtonGroup();
         this.optionSelectors = Lists.newArrayList();
         this.optionSelectors.add(this.createSelector(ResolutionOption.USE_EXISTING, var3, var1, var2));
         this.optionSelectors.add(this.createSelector(ResolutionOption.REPLACE_EXISTING, var3, var1, var2));
         this.optionSelectors.add(this.createSelector(ResolutionOption.RENAME_NEW, var3, var1, var2));
      }

      private OptionSelector<ResolutionOption> createSelector(ResolutionOption var1, ButtonGroup var2, JComponent var3, GridBagConstraints var4) {
         KongaRadioButton var5 = new KongaRadioButton("", var1 == FileCollisionResolverImpl.DEFAULT_OPTION);
         var2.add(var5);
         if (var1 == ResolutionOption.RENAME_NEW) {
            this.customizeRenameNewRadioButton(var5);
         }

         var3.add(var5, var4);
         return new ToggleButtonOptionSelector<>(var1, var5);
      }

      private void customizeRenameNewRadioButton(final JRadioButton var1) {
         var1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1x) {
               if (var1.isSelected()) {
                  Ui.this.newNameField.requestFocusInWindow();
               }
            }
         });
         this.renameNewButton = var1;
      }

      private void createTextField(FileStoreFile var1, JComponent var2, GridBagConstraints var3) {
         this.newNameField = new KongaTextField(new File(var1.getPath()).getName(), 30);
         this.newNameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent var1) {
               Ui.this.renameNewButton.setSelected(true);
            }
         });
         TextComponentUtils.selectAllWhenFocused(this.newNameField);
         Insets var4 = var3.insets;
         double var5 = var3.weightx;
         int var7 = var3.fill;
         var3.insets = new Insets(var4.top, var4.left + 15, var4.bottom, var4.right);
         var3.weightx = 0.0;
         var3.fill = 0;
         var2.add(this.newNameField, var3);
         var3.insets = var4;
         var3.weightx = var5;
         var3.fill = var7;
      }
   }
}
