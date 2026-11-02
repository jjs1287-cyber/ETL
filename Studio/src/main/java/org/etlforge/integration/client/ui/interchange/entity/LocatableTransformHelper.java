package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.base.Optional;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.data.CC;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.LoginProtectedResource;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.text.KongaPasswordField;

final class LocatableTransformHelper {
   public static DataLocation cloneLocation(Locatable var0) {
      SourceTarget var1 = var0 instanceof Source ? SourceTarget.Target : SourceTarget.Source;
      DataLocation var2 = var0.getLocation();
      DataLocation var3 = var2.getCopyForSourceOrTarget(var1);
      if (isPasswordMissing(var2)) {
         String var4 = askForPassword((LoginProtectedResource)var0.getLocation());
         if (var4 != null) {
            String var5 = ((LoginProtectedResource)var3).getPasswordPropertyName();
            String var6 = CC.hide(var4);
            var3.setProperty(var5, var6);
         }
      }

      return var3;
   }

   private static boolean isPasswordMissing(DataLocation var0) {
      if (var0 instanceof LoginProtectedResource) {
         LoginProtectedResource var1 = (LoginProtectedResource)var0;
         String var2 = var1.getLogin();
         if (StringUtils.isNotEmpty(var2)) {
            Optional var3 = var1.getPassword();
            return !var3.isPresent();
         }
      }

      return false;
   }

   private static String askForPassword(LoginProtectedResource var0) {
      KongaPasswordField var1 = new KongaPasswordField(20);
      OneColumnPanel var2 = layoutQuestion(var0, var1);
      KongaDialog var3 = createDialog(var1, var2);
      var3.setVisible(true);
      return var3.wasOkPressed() ? new String(var1.getPassword()) : null;
   }

   private static OneColumnPanel layoutQuestion(LoginProtectedResource var0, KongaPasswordField var1) {
      OneColumnPanel var2 = new OneColumnPanel();
      var2.withInsets(0, 0, 10, 0);
      var2.setWeightX(1.0);
      var2.add(WizardStyleSheet.SMALL_CAPTION_STYLE.makeLabel("Password?"));
      var2.withInsets(0, 0, 5, 0);
      String var3 = var0.getLogin();
      var2.add("The password for the user \"" + var3 + "\" may have been blanked out.");
      var2.add("Enter the password here:");
      var2.add(var1);
      return var2;
   }

   private static KongaDialog createDialog(KongaPasswordField var0, UiProvider var1) {
      final KongaDialog var2 = new KongaDialog(ApplicationUi.getAppWinFrame(), "Password?", true);
      var2.setFixedButtonSize(false);
      var2.standardLayout(var1, KongaDialog.CONTINUE_CANCEL);
      var2.getCancelButton().setText("I'll provide the password later");
      var0.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            var2.getContinueButton().doClick();
         }
      });
      var2.setContinueButtonAsDefault();
      var2.setLocationRelativeTo(null);
      var2.setResizable(false);
      var2.setFocusedComponent(var0);
      return var2;
   }

   private LocatableTransformHelper() {
   }
}
