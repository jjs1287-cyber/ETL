package org.etlforge.integration.client.ui.structure.editor.ldap.source;

import javax.swing.JLabel;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.text.KongaTextField;

final class BaseNameField implements UiProvider {
   private final JLabel basePathLabel;
   private final KongaTextField field = new KongaTextField();

   public BaseNameField(String var1) {
      this.field.setSelectAllWhenFocused(true);
      this.basePathLabel = new JLabel(getSafeBasePath(var1));
      this.basePathLabel.setToolTipText(PackageResources.SearchParamsDefiner.BASE_PATH_TOOLTIP);
   }

   public void setNewBasePath(final String var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            BaseNameField.this.basePathLabel.setText(BaseNameField.getSafeBasePath(var1));
         }
      });
   }

   public String getPath() {
      return this.field.getText().trim();
   }

   public void setPath(final String var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            BaseNameField.this.field.setText(var1);
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.add(this.field).strut(5).add(this.basePathLabel);
      CaptionLayout.of(PackageResources.SearchParamsDefiner.BASE_DN, var2).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.field.requestFocusInWindow();
   }

   private static String getSafeBasePath(String var0) {
      return var0 != null && var0.length() != 0 ? "," + var0 : "";
   }
}
