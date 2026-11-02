package com.etlforge.integration.license.client.ui.prefs;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;
import org.etlforge.application.ui.prefs.IntPreferenceUi;
import org.etlforge.application.ui.prefs.PreferenceUi;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnPanel;

public final class LicensePreferencesPage extends AbstractPreferencesPage {
   public static final String PATH = "License";
   private final LicensePreferencesPage.CommercialSettings commercialSettings = new LicensePreferencesPage.CommercialSettings();
   private final LicensePreferencesPage.TrialSettings trialSettings;

   public LicensePreferencesPage() {
      super(new String[]{"License"});
      this.addControls(this.commercialSettings.getControls());
      this.trialSettings = new LicensePreferencesPage.TrialSettings();
      this.addControls(this.trialSettings.getControls());
   }

   @Override
   protected void layoutControls(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.north(this.commercialSettings);
      var2.withBorder(StyleSheet.getDefaultTitleBorder("License Notification Settings For Commercial Licenses"));
      BorderLayoutBuilder var3 = new BorderLayoutBuilder();
      var3.north(this.trialSettings);
      var3.withBorder(StyleSheet.getDefaultTitleBorder("License Notification Settings For Trial Licenses"));
      OneColumnPanel var4 = new OneColumnPanel().withInsets(0, 0, 20, 0).setFill(GridBagLayoutPanel.Fill.HORIZONTAL).addAll(var2, var3);
      var1.display(var4.invisible());
   }

   private static class CommercialSettings implements UiProvider {
      private final BooleanPreferenceUi giveWarningUi = new BooleanPreferenceUi(
         WarnWhenLicenseIsAboutToExpirePreference.INSTANCE, "Give a warning when logging in to a server with a license that is about to expire"
      );
      private final IntPreferenceUi expirationTresholdUi = new IntPreferenceUi(
         ExpirationWarningTresholdPreference.INSTANCE, "Number of days in advance to start giving warnings:", 3
      );
      private final IntPreferenceUi warningFrequencyUi;
      private final BooleanPreferenceUi showWarningOnEachLoginUi;

      public CommercialSettings() {
         this.expirationTresholdUi.setSelectAllWhenFocused(true);
         this.showWarningOnEachLoginUi = new BooleanPreferenceUi(ShowExpirationWarningOnEachLoginPreference.INSTANCE, "Show expiration warning on every login");
         this.warningFrequencyUi = new IntPreferenceUi(
            ExpirationWarningFrequencyPreference.INSTANCE, "How often to show expiration warning (e.g. every 3 days):", 2
         );
         this.warningFrequencyUi.setSelectAllWhenFocused(true);
         this.updateControlState();
         LicensePreferencesPage.CommercialSettings.ControlStateListener var1 = new LicensePreferencesPage.CommercialSettings.ControlStateListener();
         this.giveWarningUi.addChangeListener(var1);
         this.showWarningOnEachLoginUi.addChangeListener(var1);
      }

      private void updateControlState() {
         boolean var1 = this.giveWarningUi.isSelected();
         this.expirationTresholdUi.setEnabled(var1);
         this.showWarningOnEachLoginUi.setEnabled(var1);
         this.warningFrequencyUi.setEnabled(var1 && !this.showWarningOnEachLoginUi.isSelected());
      }

      public PreferenceUi[] getControls() {
         return new PreferenceUi[]{this.giveWarningUi, this.expirationTresholdUi, this.showWarningOnEachLoginUi, this.warningFrequencyUi};
      }

      @Override
      public void decorate(UiCanvas var1) {
         TwoColumnPanel var2 = new TwoColumnPanel().withLeftInsets(0, 0, 10, 10).withRightInsets(0, 0, 10, 0);
         var2.addRow(this.giveWarningUi);
         this.expirationTresholdUi.layout(var2);
         var2.addRow(this.showWarningOnEachLoginUi);
         this.warningFrequencyUi.layout(var2);
         var1.display(var2.invisible());
      }

      @Override
      public void requestFocus() {
         this.giveWarningUi.requestFocus();
      }

      private class ControlStateListener implements ChangeListener {
         private ControlStateListener() {
         }

         @Override
         public void stateChanged(ChangeEvent var1) {
            CommercialSettings.this.updateControlState();
         }
      }
   }

   private static class TrialSettings implements UiProvider {
      private final BooleanPreferenceUi showTrialInfoUi = new BooleanPreferenceUi(
         ShowTrialInfoOnLoginPreference.INSTANCE, "Show trial information when logging in to a trial server"
      );

      public TrialSettings() {
      }

      public PreferenceUi[] getControls() {
         return new PreferenceUi[]{this.showTrialInfoUi};
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.showTrialInfoUi.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.showTrialInfoUi.requestFocus();
      }
   }
}
