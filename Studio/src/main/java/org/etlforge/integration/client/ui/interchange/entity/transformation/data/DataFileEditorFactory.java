package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import java.io.File;
import java.nio.charset.Charset;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.text.editor.ChunkedTextFileEditor;
import org.etlforge.application.ui.text.editor.TextEditor;
import org.etlforge.application.ui.text.editor.TextEditorFactory;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;

final class DataFileEditorFactory {
   public static TextEditor newEditor(final File var0, Charset var1, String var2, String var3, int var4) {
      ChunkedTextFileEditor var5 = TextEditorFactory.openFileInChunks(var2, var0, var1, var4);
      var5.setSubTitle(var3);
      var5.setEditable(true);
      var5.decorateText(TextStyles.DefaultText.asTextDecorator());
      var5.addResource(new AbstractWindowElementResource() {
         @Override
         public void dispose(WindowElement var1) {
            var0.delete();
         }
      });
      return var5;
   }

   private DataFileEditorFactory() {
   }
}
