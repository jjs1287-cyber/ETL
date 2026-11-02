package org.etlforge.application.ui.widget;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.etlforge.application.UnsupportedPlatformException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.browser.WebBrowser;
import org.etlforge.application.ui.browser.WebBrowserFactory;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.system.Disposable;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.widget.DocumentContentChangeListener;

public final class UrlTextFieldDecorator implements Disposable {
   private static WebBrowser browser;
   private final JTextField textField;
   private final Color savedForeground;
   private final MouseListener mouseListener;
   private final DocumentListener documentListener;

   private UrlTextFieldDecorator(JTextField var1) {
      this.textField = var1;
      this.mouseListener = new UrlTextFieldDecorator.WebPageLauncher();
      var1.addMouseListener(this.mouseListener);
      this.documentListener = new UrlTextFieldDecorator.CursorChanger();
      var1.getDocument().addDocumentListener(this.documentListener);
      this.savedForeground = var1.getForeground();
      var1.setForeground(Color.BLUE);
      this.updateCursor();
   }

   public static Disposable install(JTextField var0) {
      return new UrlTextFieldDecorator(var0);
   }

   @Override
   public void dispose() {
      this.textField.setForeground(this.savedForeground);
      this.textField.removeMouseListener(this.mouseListener);
      this.textField.getDocument().removeDocumentListener(this.documentListener);
      this.textField.setCursor(Cursor.getPredefinedCursor(2));
   }

   private void launchWebPage() {
      String var1 = this.textField.getText().trim();
      if (var1.length() != 0) {
         WebBrowser var2 = getBrowser();
         var2.open(var1);
      }
   }

   private static WebBrowser getBrowser() {
      if (browser == null) {
         try {
            WebBrowserFactory var0 = WebBrowserFactory.newInstance();
            browser = var0.createBrowser();
         } catch (UnsupportedPlatformException var2) {
            String var1 = ApplicationResources.STRINGS.getString("TextComponentFactory.WebBrowser.NotSupported");
            ErrorLog.attention(UrlTextFieldDecorator.class, var1, var2);
            return null;
         }
      }

      return browser;
   }

   private void updateCursor() {
      if (this.isEmpty()) {
         this.textField.setCursor(Cursor.getDefaultCursor());
      } else {
         this.textField.setCursor(Cursor.getPredefinedCursor(12));
      }
   }

   private boolean isEmpty() {
      return this.textField.getDocument().getLength() == 0;
   }

   private final class CursorChanger extends DocumentContentChangeListener {
      private CursorChanger() {
      }

      @Override
      public void contentChanged(DocumentEvent var1) {
         UrlTextFieldDecorator.this.updateCursor();
      }
   }

   private final class WebPageLauncher extends MouseActionListener {
      private WebPageLauncher() {
      }

      @Override
      protected void onSingleClick(MouseEvent var1) {
         UrlTextFieldDecorator.this.launchWebPage();
      }
   }
}
