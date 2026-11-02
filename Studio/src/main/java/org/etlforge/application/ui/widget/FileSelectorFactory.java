package org.etlforge.application.ui.widget;

import com.google.common.base.Preconditions;
import javax.swing.JComboBox;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.util.file.AbstractFileSelectorPanel;
import org.etlforge.ui.util.file.DefaultFileSelectorPanel;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.util.file.FileSelectorPanelFactory;
import org.etlforge.ui.util.file.RecentFileSelectorPanel;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.widget.LabelDecorator;

public final class FileSelectorFactory extends FileSelectorPanelFactory {
   private static final FileSelectorPanelFactory INSTANCE = new FileSelectorFactory();

   private FileSelectorFactory() {
   }

   public static FileSelectorPanelFactory getFactory() {
      return INSTANCE;
   }

   @Override
   public FileSelectorPanel singleFilePanel(FileChooser var1, FileSelectorPanel.Mode var2, String var3, RecentFileStore var4) {
      DefaultFileSelectorPanel var5 = new DefaultFileSelectorPanel(var1, var2, var3, var4);
      this.customizeTitleAndButton(var5);
      return var5;
   }

   @Override
   public FileSelectorPanel recentFilesPanel(FileChooser var1, FileSelectorPanel.Mode var2, String var3, RecentFileStore var4) {
      Preconditions.checkNotNull(var4, "recentFiles");
      RecentFileSelectorPanel var5 = new RecentFileSelectorPanel(var1, var2, var3, var4);
      this.customizeTitleAndButton(var5);
      this.customizeComboBox(var5);
      return var5;
   }

   private void customizeTitleAndButton(AbstractFileSelectorPanel var1) {
      LabelDecorator var2 = TextStyles.InputLabelText.asLabelDecorator(null);
      var1.decorateTitle(var2);
      var1.decorateBrowseButton(LabelDecorators.BROWSE_BUTTON);
   }

   private void customizeComboBox(RecentFileSelectorPanel var1) {
      org.etlforge.ui.widget.ComponentCustomizer var2 = new org.etlforge.ui.widget.ComponentCustomizer<JComboBox>() {
         public JComboBox customize(JComboBox var1) {
            TextStyles.DefaultComboBoxText.makeOver(var1);
            return var1;
         }
      };
      var1.customizeComboBox(var2);
   }
}
