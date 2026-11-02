package org.etlforge.integration.client.ui.interchange.entity.transformation.data.db;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.StructureNature;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataInput;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextArea;

final class TreeStructureFilterDefiner implements FilterDefiner {
   private static final Map<DatabaseStructure, String> lastWhereClauses = Maps.newIdentityHashMap();
   private JLabel captionLabel;
   private JLabel rootLabel;
   private JTextArea whereClauseArea;
   private JComponent displayer;
   private final DatabaseStructure dbStructure;
   private final String rootName;

   public TreeStructureFilterDefiner(DatabaseStructure var1) {
      Preconditions.checkArgument(var1.getStructureNature() == StructureNature.Tree, "dbStruct must define a Tree structure");
      this.dbStructure = var1;
      this.rootName = var1.getRelations().getRootName();
      assert this.rootName != null : "rootName cannot be null";
      this.createComponents();
      this.layoutComponents();
   }

   @Override
   public LoadSourceDataInput getInput() {
      return new LoadDatabaseSourceDataInput(this.getWhereClause());
   }

   private String getWhereClause() {
      String var1 = this.whereClauseArea.getText().trim();
      lastWhereClauses.put(this.dbStructure, var1);
      if (var1.length() == 0) {
         return "";
      }

      BeginEndQuote var2 = this.dbStructure.getBeginEndQuote();
      return var2.escapeName(this.rootName) + "." + var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.displayer);
   }

   @Override
   public void requestFocus() {
      this.whereClauseArea.requestFocusInWindow();
   }

   private void createComponents() {
      this.captionLabel = new JLabel(Strings.get("TreeFilterDefiner.Caption"));
      this.rootLabel = TextStyles.AdminHint.makeLabel(getRootLabel(this.rootName));
      String var1 = lastWhereClauses.get(this.dbStructure);
      this.whereClauseArea = new KongaTextArea(var1, 5, 40).disableTabs();
   }

   private void layoutComponents() {
      TreeStructureFilterDefiner.Layout var1 = new TreeStructureFilterDefiner.Layout();
      var1.addRow(this.captionLabel);
      var1.addRow(this.rootLabel);
      KongaLabel var2 = TextStyles.DefaultBold.makeLabel(Strings.get("TreeFilterDefiner.Where"));
      var1.addTextArea(var2, this.whereClauseArea);
      this.displayer = var1.getUi();
   }

   private static String getRootLabel(String var0) {
      return Strings.format("TreeFilterDefiner.Root", var0);
   }

   private static final class Layout {
      private final TwoColumnPanel layout = new TwoColumnPanel();

      public Layout() {
         this.layout.withAnchor(TwoColumnPanel.NORTHWEST);
         this.layout.withInsets(5, 5, 15, 5);
         this.layout.withLeftWeights(1.0, 0.0);
         this.layout.withRightWeights(1.0, 0.0);
      }

      public void addRow(JComponent var1) {
         this.layout.addRow(var1);
      }

      public void addTextArea(JComponent var1, JComponent var2) {
         this.layout.withRightFill(TwoColumnPanel.BOTH);
         this.layout.withRightWeights(1.0, 1.0);
         this.layout.addRow(var1, new JScrollPane(var2));
      }

      public JComponent getUi() {
         this.layout.setOpaque(false, true).withBorder(Empty.border(10, 10, 10, 10));
         return this.layout.invisible();
      }
   }
}
