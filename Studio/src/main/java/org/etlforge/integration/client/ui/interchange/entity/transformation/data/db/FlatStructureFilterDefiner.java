package org.etlforge.integration.client.ui.interchange.entity.transformation.data.db;

import com.google.common.base.Preconditions;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.SqlScript;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataInput;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.text.KongaTextArea;

final class FlatStructureFilterDefiner implements FilterDefiner {
   private static final String FILTER_MARKER = " where ";
   private static final int FILTER_MARKER_LENGTH = " where ".length();
   private final JTextArea sqlArea;
   private final JTextArea whereClauseArea;
   private JComponent displayer;

   public FlatStructureFilterDefiner(DatabaseStructure var1) {
      Preconditions.checkArgument(var1.isFlat(), "dbStruct must define a Flat structure.");
      String var2 = getSqlStatement(var1);
      this.sqlArea = KongaTextArea.viewer(var2, Size.rows(4), Size.columns(40));
      this.whereClauseArea = new KongaTextArea(Size.rows(4), Size.columns(40)).disableTabs();
      if (!var1.hasManualSqlStatement()) {
         String var3 = getOriginalFilter(var2);
         this.whereClauseArea.setText(var3);
      }

      this.layoutComponents();
   }

   private static String getSqlStatement(DatabaseStructure var0) {
      SqlScript var1 = var0.getSqlScript();
      String var2 = var1.getManualSqlStatement();
      if (var2 == null) {
         var2 = var1.getConstructedSqlStatement();
      }

      return var2;
   }

   private static String getOriginalFilter(String var0) {
      int var1 = var0.toLowerCase().indexOf(" where ");
      return var1 >= 0 ? var0.substring(var1 + FILTER_MARKER_LENGTH) : "";
   }

   @Override
   public LoadSourceDataInput getInput() {
      return new LoadDatabaseSourceDataInput(this.getWhereClause());
   }

   private String getWhereClause() {
      String var1 = this.whereClauseArea.getText().trim();
      return StringUtils.removeStartIgnoreCase(var1, " where ");
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.displayer);
   }

   @Override
   public void requestFocus() {
      this.whereClauseArea.requestFocusInWindow();
   }

   private void layoutComponents() {
      FlatStructureFilterDefiner.Layout var1 = new FlatStructureFilterDefiner.Layout();
      var1.addCaption(Strings.get("FlatFilterDefiner.OriginalSQL"));
      JScrollPane var2 = new JScrollPane(this.sqlArea);
      UiUtils.setOpaqueRecursively(var2, false);
      var1.addTextComponent(var2, false);
      var1.addVerticalSpace();
      var1.addCaption(Strings.get("FlatFilterDefiner.FilterToTest"));
      var1.addTextComponent(this.whereClauseArea, true);
      this.displayer = var1.getUi();
   }

   private static final class Layout {
      private final OneColumnPanel layout = new OneColumnPanel();

      public Layout() {
         this.layout.withInsets(new Insets(2, 0, 2, 0));
      }

      public void addCaption(String var1) {
         this.layout.add(var1);
      }

      public void addTextComponent(JComponent var1, boolean var2) {
         JComponent var3 = var2 ? new JScrollPane(var1) : var1;
         this.layout.add(var3);
      }

      public void addVerticalSpace() {
         this.layout.add(" ");
      }

      public JComponent getUi() {
         return this.layout.deeplyNonOpaque().invisible();
      }
   }
}
