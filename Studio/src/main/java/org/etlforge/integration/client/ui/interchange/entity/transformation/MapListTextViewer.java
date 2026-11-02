package org.etlforge.integration.client.ui.interchange.entity.transformation;

import com.google.common.base.Joiner;
import javax.swing.JLabel;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.text.editor.FindTextAction;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.CopyToClipboardButton;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class MapListTextViewer {
   public static void open(Transformation var0) {
      String var1 = Joiner.on(SystemUtils.LINE_SEPARATOR + SystemUtils.LINE_SEPARATOR).join(var0.getMapList());
      KongaTextArea var2 = createTextArea(var1);
      CopyToClipboardButton var3 = new CopyToClipboardButton(var1);
      FindTextAction var4 = new FindTextAction(var2);
      var4.putValue("Name", "&Find");
      UiProvider var5 = layoutDialog(var0, var2, var3, var4);
      KongaDialog var6 = new KongaDialog(ApplicationUi.getAppWinFrame(), "Mappings", true);
      var6.standardLayout(var5, KongaDialog.CLOSE_ONLY);
      var6.setLocationRelativeTo(null);
      var6.setCloseButtonAsDefault();
      var6.setVisible(true);
   }

   private static KongaTextArea createTextArea(String var0) {
      KongaTextArea var1 = KongaTextArea.viewer(Size.rows(20), Size.columns(100));
      var1.setWrapStyleWord(false);
      var1.setLineWrap(false);
      TextStyles.DefaultMonoSpaced.makeOver(var1);
      var1.setText(var0);
      var1.setCaretPosition(0);
      return var1;
   }

   private static UiProvider layoutDialog(Transformation var0, KongaTextArea var1, Object... var2) {
      JLabel var3 = new JLabel();
      RendererUtils.decorateRenderer(var3, var0, false);
      return CaptionLayout.of(Horizontal.box().add(var3).glue().addAllWithSpace(10, var2), var1.asUiProvider());
   }

   private MapListTextViewer() {
   }
}
