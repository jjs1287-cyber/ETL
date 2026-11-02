package org.etlforge.integration.client.ui.entity.wizard;

import java.awt.Dimension;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.ui.window.editor.EditorCategory;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardDialogDisplayer;
import org.etlforge.application.ui.wizard.WizardPageDisplayer;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.ui.entity.ProjectPageGroup;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.wait.WaitLock;

public abstract class EntityWizardFactory {
   private boolean showInDialog = ApplicationConfiguration.getBoolean("Wizards.ShowInDialog", false);
   private String saveText;

   public abstract Wizard<?> createWizard(EntityWizardContext var1, IntegrationEntity var2, Folder var3);

   public final void setShowInDialog(boolean var1) {
      this.showInDialog = var1;
   }

   protected final void setSaveText(String var1) {
      this.saveText = var1;
   }

   protected final void createWizardDisplayer(Wizard var1, EntityWizardContext var2, EntityType var3) {
      Icon var4 = EntityIcons.forType(var3).getDefaultIcon(EntityIcons.Size.SMALL);
      this.createWizardDisplayer(var1, var2, var3, var4);
   }

   protected final void createWizardDisplayer(Wizard var1, EntityWizardContext var2, EntityType var3, Icon var4) {
      if (this.showInDialog) {
         this.createWizardDialogDisplayer(var1, var2, var4);
      } else {
         this.createWizardPageDisplayer(var1, var2, var3, var4);
      }
   }

   private void createWizardDialogDisplayer(Wizard var1, EntityWizardContext var2, Icon var3) {
      WizardDialogDisplayer var4 = this.createEmptyDialogDisplayer(var2.getWindow().startWait());
      if (var3 instanceof ImageIcon) {
         Image var5 = ((ImageIcon)var3).getImage();
         var4.setDialogImage(var5);
      }

      var4.setSections(this.getSections());
      if (this.saveText != null) {
         var4.setSaveText(this.saveText);
      }

      var4.setManageLocation(this.getClass());
      var4.setMinimumSize(new Dimension(600, 600));
      var4.setWizard(var1);
      var1.setDisplayer(var4);
   }

   protected WizardDialogDisplayer createEmptyDialogDisplayer(WaitLock var1) {
      return new WizardDialogDisplayer(var1);
   }

   private void createWizardPageDisplayer(Wizard var1, EntityWizardContext var2, EntityType var3, Icon var4) {
      WizardPageDisplayer var5 = new WizardPageDisplayer(var1, var2.getWindow().getEditorService());
      var5.setSections(this.getSections());
      EditorCategory var6 = this.createWizardPageCategory(var3, var4);
      var5.setCategory(var6);
      var5.setTitle("New " + this.getTypeDisplayName(var3));
      if (this.saveText != null) {
         var5.setSaveText(this.saveText);
      }

      String var7 = ProjectPageGroup.get(var2.getProject());
      var5.setPageGroup(var7);
      var5.setIcon(var4);
   }

   private String getTypeDisplayName(EntityType var1) {
      String var2 = var1.getDisplayName();
      if (ClientInfo.isDataLoader() && var2.startsWith("Salesforce ")) {
         var2 = StringUtils.substringAfter(var2, "Salesforce ");
      }

      return var2;
   }

   private EditorCategory createWizardPageCategory(EntityType var1, Icon var2) {
      String var3 = this.getTypeDisplayName(var1) + " Wizards";
      return new EditorCategory(var3, var2);
   }

   protected String[] getSections() {
      return new String[0];
   }
}
