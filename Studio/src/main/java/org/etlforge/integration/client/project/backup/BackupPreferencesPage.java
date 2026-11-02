package org.etlforge.integration.client.project.backup;

import java.awt.Container;
import javax.swing.JComponent;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.IntPreferenceUi;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.util.html.HtmlListGenerator;
import org.etlforge.util.html.HtmlStyleTag;

public class BackupPreferencesPage extends AbstractPreferencesPage {
   private final IntPreferenceUi referenceFreqField = new IntPreferenceUi(
      BackupPreferences.referenceFrequencyInDays(), "Reference backup frequency (in days):", 4
   );
   private final IntPreferenceUi maxIntermediateField;

   public BackupPreferencesPage() {
      super(BackupPreferences.PATH);
      this.referenceFreqField.setSelectAllWhenFocused(true);
      this.maxIntermediateField = new IntPreferenceUi(BackupPreferences.maxNumberOfIntermediateBackups(), "Maximum number of intermediate backups:", 4);
      this.maxIntermediateField.setSelectAllWhenFocused(true);
      this.addControl(this.referenceFreqField);
      this.addControl(this.maxIntermediateField);
   }

   @Override
   public void display(Container var1) {
      TwoColumnPanel var2 = new TwoColumnPanel();
      this.referenceFreqField.layout(var2);
      this.maxIntermediateField.layout(var2);
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 10);
      var3.center(getCaption()).south(InvisiblePanel.of(var2));
      var3.deeplyNonOpaque();
      BorderLayoutBuilder.from(var1).north(var3).withEmptyBorder(10);
   }

   private static JComponent getCaption() {
      StringBuilder var0 = new StringBuilder(
         "<html><table width=500px>Everytime a project is deployed to a ETLForge server, a backup Jitterpak is uploaded to the server. A project backup is also stored locally on the client. There are two types of backups:<br><br>"
      );
      HtmlListGenerator var1 = HtmlListGenerator.unordered();
      var1.escapeAndAdd("Reference backups: ", HtmlStyleTag.BOLD);
      var1.escapeAndAdd(
         "These backups are considered to be permanent; a permanent backup will never be deleted by the system. They provide snapshots of the project at a regular time interval, e.g. once a week."
      );
      var1.newItem();
      var1.escapeAndAdd("Intermediate backups: ", HtmlStyleTag.BOLD);
      var1.escapeAndAdd(
         "These backups are considered to be transient; they provide snapshots of the ongoing work on a project, typically within a single client session."
      );
      var0.append(var1.toHtml()).append("<br>");
      var0.append("The backup policy is defined by two parameters:<br><br>");
      var1 = HtmlListGenerator.unordered();
      var1.escapeAndAdd("Reference frequency (in days): ", HtmlStyleTag.BOLD);
      var1.escapeAndAdd(
         "When a new backup is made, the system checks how long time ago the last reference backup was made. If the last reference backup is older than the number of days defined by this property, the new backup is flagged as a reference backup. Otherwise the new backup is flagged as an intermediate backup. If this value is <= 0, all backups will be regarded as intermediate."
      );
      var1.newItem();
      var1.escapeAndAdd("Max number of intermediate backups: ", HtmlStyleTag.BOLD);
      var1.escapeAndAdd(
         "This property defines the maximum number of intermediate (non-reference) backups to keep on the client. Once this limit is exceeded, the oldest intermediate backups will be deleted. If this value is <= 0, no intermediate backups will be made."
      );
      var0.append(var1.toHtml()).append("<br>");
      var0.append("By setting both these parameters to 0, no backups will be created.");
      var0.append("<br><br>");
      var0.append(HtmlStyleTag.BOLD.escapeAndWrap("NOTE: "));
      var0.append(
         "These parameters control the policy of the local backups that are stored on the client. Each ETLForge server has its own backup policy (in the current version controlled by the configuration file <code>etlforge.conf</code>) to decide how to handle the backups that are sent to it from a client. However, if the client backup policy disables backups, i.e. by setting both parameters to 0, no backups are sent to the server regardless of the current server settings."
      );
      var0.append("<br><br><hr></table></html>");
      return TextStyles.InfoText.makeLabel(var0.toString());
   }
}
