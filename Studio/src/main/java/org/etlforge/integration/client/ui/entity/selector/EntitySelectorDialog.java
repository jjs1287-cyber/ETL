package org.etlforge.integration.client.ui.entity.selector;

import java.awt.Window;
import javax.swing.JComponent;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.selection.DialogSelectionSupport;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.widget.label.KongaLabel;

public final class EntitySelectorDialog<T extends IntegrationEntity> {
   private final KongaDialog dialog;
   private final EntitySelector<T> selector;
   private final DialogSelectionSupport selectionSupport;

   public static <E extends IntegrationEntity> E selectOne(EntitySelector<E> var0, Window var1, String var2, String var3) {
      TextStyle var4 = TextStyles.InfoText;
      KongaLabel var5 = var3 != null ? var4.makeLabel(var3) : null;
      return selectOne(var0, var1, var2, var5);
   }

   public static <E extends IntegrationEntity> E selectOne(EntitySelector<E> var0, Window var1, String var2, JComponent var3) {
      EntitySelectorDialog var4 = new EntitySelectorDialog(var1, var2, var0, var3);
      return (E)var4.selectOne();
   }

   private EntitySelectorDialog(Window var1, String var2, EntitySelector<T> var3, JComponent var4) {
      this.selector = var3;
      this.dialog = this.createDialog(var1, var2, this.createContent(var3, var4));
      this.dialog.manageLocation(EntitySelectorDialog.class);
      this.dialog.setOkButtonAsDefault();
      this.selectionSupport = new DialogSelectionSupport(var3.getSelectionSource(), this.dialog) {
         @Override
         protected boolean isEnabledFor(Selection var1) {
            return EntitySelectorDialog.this.selector.getSelectedEntity() != null;
         }
      };
   }

   private T selectOne() {
      this.displayDialogAndRemoveListenerWhenClosed();
      return this.dialog.wasOkPressed() ? this.selector.getSelectedEntity() : null;
   }

   private void displayDialogAndRemoveListenerWhenClosed() {
      this.dialog.setVisible(true);
      this.selectionSupport.uninstall();
   }

   private UiProvider createContent(EntitySelector<T> var1, JComponent var2) {
      return CaptionLayout.of(var2, var1);
   }

   private KongaDialog createDialog(Window var1, String var2, UiProvider var3) {
      KongaDialog var4 = new KongaDialog(var1, var2) {
         @Override
         protected void onDialogOpened() {
            EntitySelectorDialog.this.selector.requestFocus();
         }
      };
      var4.standardLayout(var3, KongaDialog.OK_CANCEL);
      return var4;
   }
}
