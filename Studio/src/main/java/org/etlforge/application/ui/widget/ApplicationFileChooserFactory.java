package org.etlforge.application.ui.widget;

import java.io.File;
import javax.swing.JFileChooser;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.ui.widget.DefaultFileChooserFactory;
import org.etlforge.ui.widget.FileChooserFactory;
import org.etlforge.ui.widget.FileViewFactory;

public final class ApplicationFileChooserFactory implements FileChooserFactory {
   public static final String FILEVIEW_FACTORY_KEY = "FileChooser.ViewFactory";
   private static final ApplicationFileChooserFactory INSTANCE = new ApplicationFileChooserFactory();
   private final FileChooserFactory delegate = new DefaultFileChooserFactory();
   private final FileViewFactory viewFactory = this.loadFileViewFactory();

   public static ApplicationFileChooserFactory getFactory() {
      return INSTANCE;
   }

   private ApplicationFileChooserFactory() {
   }

   @Override
   public JFileChooser newFileChooser() {
      JFileChooser var1 = this.delegate.newFileChooser();
      this.customize(var1);
      return var1;
   }

   @Override
   public JFileChooser newFileChooser(File var1) {
      JFileChooser var2 = this.delegate.newFileChooser(var1);
      this.customize(var2);
      return var2;
   }

   private void customize(JFileChooser var1) {
      if (this.viewFactory != null) {
         var1.setFileView(this.viewFactory.createView());
      }
   }

   private FileViewFactory loadFileViewFactory() {
      try {
         Class var1 = ApplicationConfiguration.getClass("FileChooser.ViewFactory", FileViewFactory.class);
         return var1 != null ? (FileViewFactory)var1.newInstance() : null;
      } catch (Exception var2) {
         ErrorLog.log(ApplicationFileChooserFactory.class, "Invalid application configuration: failed to instantiate the FileViewFactory", var2);
         return null;
      }
   }
}
