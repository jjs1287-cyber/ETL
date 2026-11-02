package org.etlforge.application.ui.window.editor;

import com.google.common.base.Preconditions;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;

public class UiProviderEditor extends GenericEditor {
   private final UiProvider ui;

   public UiProviderEditor(UiProvider var1, String var2, String var3) {
      super(var2, var3 != null ? var3 : "");
      Preconditions.checkNotNull(var1, "ui");
      JComponent var4 = this.createMainPanel(var1);
      this.setMainPanel(var4);
      this.ui = var1;
   }

   private JComponent createMainPanel(UiProvider var1) {
      JPanel var2 = BorderLayoutBuilder.fillWith(var1);
      UiUtils.setPanelBackgrounds(var2, Colors.NEUTRAL_BACKGROUND);
      return var2;
   }

   @Override
   public Object getObject() {
      return this.ui;
   }
}
