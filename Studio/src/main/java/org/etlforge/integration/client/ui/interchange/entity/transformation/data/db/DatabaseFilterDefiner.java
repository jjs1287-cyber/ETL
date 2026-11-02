package org.etlforge.integration.client.ui.interchange.entity.transformation.data.db;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataInput;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;

public final class DatabaseFilterDefiner {
   private final FilterDefiner definer;

   private DatabaseFilterDefiner(DatabaseStructure var1) {
      this.definer = this.createDelegate(var1);
   }

   public FilterDefiner getDefiner() {
      return this.definer;
   }

   public static LoadSourceDataInput getInput(DatabaseStructure var0) {
      assert EventQueue.isDispatchThread() : "This method must be called on the EDT";
      DatabaseFilterDefiner var1 = new DatabaseFilterDefiner(var0);
      KongaDialog var2 = createDialog(UiUtils.getActiveFrame(), var1.getDefiner());
      var2.setVisible(true);
      return var2.wasOkPressed() ? var1.getDefiner().getInput() : null;
   }

   private FilterDefiner createDelegate(DatabaseStructure var1) {
      return var1.isFlat() ? new FlatStructureFilterDefiner(var1) : new TreeStructureFilterDefiner(var1);
   }

   private static KongaDialog createDialog(JFrame var0, UiProvider var1) {
      JPanel var2 = BorderLayoutBuilder.fillWith(var1);
      var2.setBorder(Empty.border(10));
      var2.setOpaque(true);
      UiUtils.setPanelBackgrounds(var2, Colors.NEUTRAL_BACKGROUND);
      WizardStyleDialog var3 = new WizardStyleDialog(var0, Strings.get("SourceFilterDialog.Title"), var2);
      var3.setResizable(false);
      var3.manageLocation(DatabaseFilterDefiner.class);
      var3.setOkButtonAsDefault();
      return var3;
   }
}
