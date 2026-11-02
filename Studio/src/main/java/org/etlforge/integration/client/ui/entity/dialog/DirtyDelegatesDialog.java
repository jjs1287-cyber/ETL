package org.etlforge.integration.client.ui.entity.dialog;

import java.awt.Window;
import java.util.List;
import javax.swing.JComponent;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.entity.IntegrationEntityList;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.style.TextStyle;

public final class DirtyDelegatesDialog {
   public static final int SAVE = 0;
   public static final int CANCEL = 1;
   private final Window owner;
   private final UiProvider entityList;

   private DirtyDelegatesDialog(Window var1, List<IntegrationEntity> var2) {
      this.owner = var1;
      this.entityList = new IntegrationEntityList(var2);
   }

   public static int showSaveDialog(Window var0, List<IntegrationEntity> var1) {
      DirtyDelegatesDialog var2 = new DirtyDelegatesDialog(var0, var1);
      return var2.show();
   }

   private int show() {
      KongaDialog var1 = this.createDialog();
      var1.manageLocation(DirtyDelegatesDialog.class);
      var1.setVisible(true);
      return var1.wasOkPressed() ? 0 : 1;
   }

   private KongaDialog createDialog() {
      KongaDialog var1 = new KongaDialog(this.owner, PackageResources.DirtyDelegates.TITLE);
      this.layoutDialog(var1);
      var1.getRootPane().setDefaultButton(var1.getOKButton());
      return var1;
   }

   private void layoutDialog(KongaDialog var1) {
      var1.standardLayout(this.createContent(), KongaDialog.OK_CANCEL);
      var1.getOKButton().setText("Save");
   }

   private JComponent createContent() {
      TextStyle var1 = TextStyles.InfoText;
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 5);
      var2.north(var1.makeLabel(PackageResources.DirtyDelegates.Messages.TOP));
      var2.center(this.entityList);
      return var2.container();
   }
}
