package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import com.etlforge.integration.client.ui.sfdc.wizard.LocalFileBrowser;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.undo.UndoRedoControllers;
import org.etlforge.application.ui.widget.ApplicationFileChooserFactory;
import org.etlforge.collections.Collections2;
import org.etlforge.integration.client.ui.entity.LastLocalFile;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Size;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.input.DefaultNonEmptyTextInputField;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.text.NonEmptyTextEnablerCondition;
import org.etlforge.ui.util.file.DefaultFileChooser;
import org.etlforge.ui.util.file.SuffixFileChooserFilter;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;

public final class DefaultSourceHeaderUpdater implements SourceHeaderUpdater {
   private final DefaultNonEmptyTextInputField manualEditor = new DefaultNonEmptyTextInputField("Header", new KongaTextField(Size.columns(30)));
   private Component invoker;

   public DefaultSourceHeaderUpdater() {
      this.manualEditor.setAutoTrim(true);
      ((KongaTextField)this.manualEditor.getInputComponent()).setLabel("&Header:");
   }

   public void setInvoker(Component var1) {
      this.invoker = var1;
   }

   @Override
   public List<String> getSourceHeader(List<String> var1) {
      this.manualEditor.setValue(Collections2.join(var1, ","));
      KongaDialog var2 = this.createDialog();
      Disposable var3 = NonEmptyTextEnablerCondition.enableComponentWhenFieldNotEmpty(var2.getOKButton(), this.manualEditor.getInputComponent());
      var2.setVisible(true);
      var3.dispose();
      if (var2.wasOkPressed()) {
         String var4 = this.manualEditor.getValue();
         return Arrays.asList(var4.split(","));
      } else {
         return var1;
      }
   }

   private KongaDialog createDialog() {
      KongaDialog var1 = new KongaDialog(this.getDialogOwner(), "Edit Source Header");
      var1.standardLayout(this.createLayout(), KongaDialog.OK_CANCEL);
      var1.manageLocation(this.getClass());
      var1.setOkButtonAsDefault();
      var1.enableUndoRedoForTextComponents(UndoRedoControllers.textFactory());
      return var1;
   }

   private Window getDialogOwner() {
      return this.invoker != null ? SwingUtilities.getWindowAncestor(this.invoker) : ApplicationUi.getAppWinFrame();
   }

   private JComponent createLayout() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.setWeightX(1.0).setFill(GridBagLayoutPanel.Fill.HORIZONTAL).withInsets(0, 0, 10, 0);
      var1.add("Change the header by hand, or select and read the header from a file.");
      var1.add(((KongaTextField)this.manualEditor.getInputComponent()).asUiProvider());
      var1.add(InvisiblePanel.of(new DefaultSourceHeaderUpdater.BrowseAction()));
      return var1.container();
   }

   private class BrowseAction extends AbstractAction {
      public BrowseAction() {
         super(ApplicationResources.getString("Browse.Action"));
         this.putValue("ShortDescription", "Select and read the header from a file.");
      }

      @Override
      public void actionPerformed(final ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Object var1x = var1.getSource();
               Component var2 = var1x instanceof Component ? (Component)var1x : DefaultSourceHeaderUpdater.this.manualEditor.getInputComponent();
               BrowseAction.this.browse(var2);
            }
         });
      }

      private void browse(Component var1) {
         DefaultFileChooser var2 = new DefaultFileChooser(ApplicationFileChooserFactory.getFactory());
         LastLocalFile var3 = LocalFileBrowser.lastSourceFile();
         File var4 = var2.getFileToOpen(var1, new SuffixFileChooserFilter(".csv", ".txt"), var3.getRecentFileStore());
         if (var4 != null) {
            this.extractHeader(var4);
         }
      }

      private void extractHeader(File var1) {
         SourceFileHeaderExtractor var2 = new SourceFileHeaderExtractor(var1);
         Computation var3 = Computation.create(var2, new Receiver<String>() {
            public void handle(String var1) {
               DefaultSourceHeaderUpdater.this.manualEditor.setValue(var1.trim());
            }
         });
         var3.swingWorkerBuilder().withErrorLog(ErrorLevel.ATTENTION, DefaultSourceHeaderUpdater.class).buildAndExecute();
      }
   }
}
