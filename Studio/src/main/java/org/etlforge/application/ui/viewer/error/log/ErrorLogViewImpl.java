package org.etlforge.application.ui.viewer.error.log;

import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.errorlog.ErrorLogEntry;
import org.etlforge.application.errorlog.ErrorLogListener;
import org.etlforge.application.ui.help.HelpButtonResource;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.AbstractWindowSectionContent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.label.KongaLabel;

final class ErrorLogViewImpl extends AbstractWindowSectionContent implements ErrorLogView {
   private static final String HELP_ID = "consoles.error";
   private ErrorLogDisplayer errorDisplayer;
   private final JButton helpButton = HelpButtonResource.forToolbar(this, "consoles.error");
   private final ErrorLogViewImpl.TitleLabel titleLabel = new ErrorLogViewImpl.TitleLabel();

   public ErrorLogViewImpl(WindowSection var1) {
      super("error_log", var1);
   }

   @Override
   public String getTitle() {
      return ApplicationResources.STRINGS.getString("ErrorLog.View.Title");
   }

   @Override
   protected JComponent getAdditionalTitleComponent() {
      return this.titleLabel.label;
   }

   @Override
   public Icon getIcon() {
      return CommonIcons.ERROR_16;
   }

   @Override
   public void decorate(JComponent var1) {
      BorderLayoutBuilder var2 = BorderLayoutBuilder.from(var1);
      var1.setBackground(StyleSheet.GenericEditorPanelClass.getBackground());
      JComponent var3 = this.getTitlePanel();
      KongaScrollPane var4 = new KongaScrollPane(this.errorDisplayer);
      var4.getViewport().setBackground(javax.swing.UIManager.getColor("Panel.background"));
      var2.north(var3).center(var4);
      this.setInitialFocus();
   }

   private void setInitialFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ErrorLogViewImpl.this.errorDisplayer.requestFocus();
         }
      });
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.errorDisplayer = this.createErrorDisplayer();
      this.addResource(new ErrorLogViewImpl.ErrorDisplayerResource());
      this.addResource(new ErrorLogViewImpl.CountUpdaterResource(this.titleLabel));
   }

   private ErrorLogDisplayer createErrorDisplayer() {
      DetailedErrorLogDisplayer var1 = new DetailedErrorLogDisplayer();
      var1.setDynamic(true);
      return var1;
   }

   @Override
   protected void populateToolBar(JToolBar var1) {
      var1.add(this.helpButton);
      super.populateToolBar(var1);
   }

   private static class CountUpdaterResource extends AbstractWindowElementResource {
      private final ErrorLogViewImpl.TitleLabel label;
      private final ErrorLogListener listener = new ErrorLogListener() {
         @Override
         public void errorOccurred(ErrorLogEntry var1) {
            this.updateLabel();
         }

         private void updateLabel() {
            int var1 = ErrorLog.getNumberOfErrors();
            CountUpdaterResource.this.label.setCount(var1);
         }
      };

      public CountUpdaterResource(ErrorLogViewImpl.TitleLabel var1) {
         this.label = var1;
         ErrorLog.addListener(this.listener);
      }

      @Override
      public void dispose(WindowElement var1) {
         ErrorLog.removeListener(this.listener);
      }
   }

   private class ErrorDisplayerResource extends AbstractWindowElementResource {
      private ErrorDisplayerResource() {
      }

      @Override
      public void dispose(WindowElement var1) {
         ErrorLogViewImpl.this.errorDisplayer.setDynamic(false);
      }
   }

   private static class TitleLabel {
      public final KongaLabel label = new KongaLabel();

      public TitleLabel() {
         this.setCount(ErrorLog.getNumberOfErrors());
      }

      public void setCount(int var1) {
         final String var2 = this.getText(var1);
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               TitleLabel.this.label.setText(var2);
            }
         });
      }

      private String getText(int var1) {
         return var1 == 0
            ? "0 errors"
            : String.format("%d %s. Double-click on %s error to view details.", var1, var1 == 1 ? "error" : "errors", var1 == 1 ? "the" : "an");
      }
   }
}

