package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.migration;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.widget.ApplicationFileChooser;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.structure.cache.CacheWriter;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.StagingPanel;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperException;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.MapArea;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.MapArea_Migration;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.MenuBuilder;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeScrollPane;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Utils;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.script.ScriptException;
import org.etlforge.integration.data.script.ScriptUtils;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.text.NonEmptyTextEnablerCondition;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.util.file.SuffixFileChooserFilter;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.text.KongaTextField;

public final class Migration extends JDialog {
   private static final int c_show_all = 0;
   private static final int c_hide_source = 1;
   private static final int c_hide_target = 2;
   private static final int m_dw = 140;
   private static final int m_scroll_bar_height = 17;
   private static final int[] m_min_widths = new int[]{140, 46, 140, 70, 140, 46, 140};
   private static final String sReadSourceMapping = "Read source mapping ...";
   private static final String sReadTargetMapping = "Read target mapping ...";
   private static final String sSaveSourceMapping = "Write source mapping ...";
   private static final String sSaveTargetMapping = "Write target mapping ...";
   private static final String sSave = "Migrate mappings";
   private static final String sExit = "Exit";
   private static final String sShowAllMapping = "Show all mappings";
   private static final String sHideSourceMapping = "Hide source mapping";
   private static final String sHideTargetMapping = "Hide target mapping";
   private static final String sExpandAllTrees = "Expand all trees";
   private static final String sAutoSrcMapping = "Auto source mapping";
   private static final String sAutoTrgMapping = "Auto target mapping";
   private static final String sRmSrcMapping = "Remove source mapping";
   private static final String sRmTrgMapping = "Remove target mapping";
   private static final String FILE_EXTENSION = ".mtb";
   private int show_mode = 0;
   private int iComponentStart = 0;
   private int iComponentEnd = 7;
   private final TreeMapper m_TreeMapper;
   private final Transformation m_old;
   private final Transformation m_new;
   public JScrollPane m_scroll;
   public JPanel m_panel;
   private TreeScrollPane m_newSourceScrollPane;
   private TreeScrollPane m_newTargetScrollPane;
   public TreeScrollPane m_oldSourceScrollPane;
   public TreeScrollPane m_oldTargetScrollPane;
   public TreeComponent m_newSourceTree;
   public TreeComponent m_oldSourceTree;
   public TreeComponent m_oldTargetTree;
   public TreeComponent m_newTargetTree;
   public MapArea_Migration m_sourceMapArea;
   public MapArea_Migration m_targetMapArea;
   public MapArea m_mapArea;
   private MappingTreeStructure m_oldSourceJtr;
   private MappingTreeStructure m_oldTargetJtr;
   private MappingTreeStructure m_newSourceJtr;
   private MappingTreeStructure m_newTargetJtr;
   public JPanel m_dragImg;
   private boolean m_dirty;
   private JMenuBar m_menu_bar;
   private JMenuItem m_search_nst;
   private JMenuItem m_search_ost;
   private JMenuItem m_search_ott;
   private JMenuItem m_search_ntt;
   private JMenuItem m_next_nst;
   private JMenuItem m_next_ost;
   private JMenuItem m_next_ott;
   private JMenuItem m_next_ntt;
   private Component[] m_Components;
   private final ApplicationWindow m_window;

   public Migration(TreeMapper var1, Transformation var2) throws MigrationInitializationException {
      super(SwingUtilities.getWindowAncestor(var1), "Mapping Migration", ModalityType.APPLICATION_MODAL);
      this.setDefaultCloseOperation(2);
      this.applyFrameImage();
      this.m_window = ApplicationUi.getAppWin();
      this.m_TreeMapper = var1;
      Transformation var3 = var1.m_Transformation;
      Transformation var4 = var2;
      if (!var3.getMapList().isEmpty()) {
         this.m_old = var3;
         this.m_new = var4;
      } else {
         Alert.message(
            this, "Since this transformation has no mappings, it will be used as the destination of the migration.", "Warning", Alert.Type.INFORMATION
         );
         this.m_old = var4;
         this.m_new = var3;
      }

      this.loadStructures(var1);
      this.m_dragImg = new JPanel();
      this.m_dragImg.setVisible(false);
      this.add(this.m_dragImg);
      this.setupMenu();
   }

   private void applyFrameImage() {
      this.setIconImage(ClientIcons.APPLICATION_16.getImage());
   }

   private void loadStructures(TreeMapper var1) throws MigrationInitializationException {
      InputAndOutputMappingTreeStructureCache var2 = var1.getTreeStructureCache();
      this.m_oldSourceJtr = this.getInputStructure(this.m_old, var2);
      this.m_oldTargetJtr = this.getOutputStructure(this.m_old, var2);
      this.m_newSourceJtr = this.getInputStructure(this.m_new, var2);
      this.m_newTargetJtr = this.getOutputStructure(this.m_new, var2);
   }

   private MappingTreeStructure getInputStructure(Transformation var1, InputAndOutputMappingTreeStructureCache var2) throws MigrationInitializationException {
      MappingTreeStructure var3 = null;

      try {
         var3 = var2.getInputStructure(var1.getID()).orNull();
      } catch (DataCacheException var5) {
      }

      if (var3 == null) {
         var3 = this.reloadInputStructure(var1);
         this.cacheReloadedInputStructure(var1, var2, var3);
      }

      return var3;
   }

   private void cacheReloadedInputStructure(Transformation var1, InputAndOutputMappingTreeStructureCache var2, MappingTreeStructure var3) {
      CacheWriter var4 = CacheWriter.newInputWriter();
      this.writeJtrToCache(var1, var2, var3, var4);
   }

   private void writeJtrToCache(Transformation var1, InputAndOutputMappingTreeStructureCache var2, MappingTreeStructure var3, CacheWriter var4) {
      var4.setErrorHandler(new LoggingExceptionHandler(this.getClass(), ErrorLevel.INTERNAL, "Failed to cache transformation structures."));
      var4.addToCache(var2, var1.getID(), var3);
   }

   private MappingTreeStructure getOutputStructure(Transformation var1, InputAndOutputMappingTreeStructureCache var2) throws MigrationInitializationException {
      MappingTreeStructure var3 = null;

      try {
         var3 = var2.getOutputStructure(var1.getID()).orNull();
      } catch (DataCacheException var5) {
      }

      if (var3 == null) {
         var3 = this.reloadOutputStructure(var1);
         this.cacheReloadedOutputStructure(var1, var2, var3);
      }

      return var3;
   }

   private void cacheReloadedOutputStructure(Transformation var1, InputAndOutputMappingTreeStructureCache var2, MappingTreeStructure var3) {
      CacheWriter var4 = CacheWriter.newOutputWriter();
      this.writeJtrToCache(var1, var2, var3, var4);
   }

   private MappingTreeStructure reloadInputStructure(Transformation var1) throws MigrationInitializationException {
      try {
         return this.m_TreeMapper.getMappingStructureFactory().getInputStructure(var1);
      } catch (MappingTreeStructureException var4) {
         String var3 = "Failed to load the source structure for the transformation \"" + var1.getName() + "\": ";
         throw new MigrationInitializationException(var3 + var4.getMessage(), var4);
      }
   }

   private MappingTreeStructure reloadOutputStructure(Transformation var1) throws MigrationInitializationException {
      try {
         return this.m_TreeMapper.getMappingStructureFactory().getOutputStructure(var1);
      } catch (MappingTreeStructureException var5) {
         String var4 = "Failed to load the target structure for the transformation \"" + var1.getName() + "\": ";
         throw new MigrationInitializationException(var4 + var5.getMessage(), var5);
      }
   }

   private void setupMenu() {
      this.m_menu_bar = new JMenuBar();
      this.setJMenuBar(this.m_menu_bar);
      MenuBuilder var1 = new MenuBuilder(new Migration.MenuEventHandler(), this.m_menu_bar);
      var1.addMenu("File");
      var1.addMenuItem("Migrate mappings");
      var1.addMenuItem("Exit");
      var1.addMenu("Mapping");
      var1.addMenuItem("Read source mapping ...");
      var1.addMenuItem("Read target mapping ...");
      var1.addMenuSeparator();
      var1.addMenuItem("Write source mapping ...");
      var1.addMenuItem("Write target mapping ...");
      var1.addMenuSeparator();
      var1.addMenuItem("Auto source mapping");
      var1.addMenuItem("Auto target mapping");
      var1.addMenuSeparator();
      var1.addMenuItem("Remove source mapping");
      var1.addMenuItem("Remove target mapping");
      var1.addMenu("View");
      var1.addMenuItem("Show all mappings");
      var1.addMenuItem("Hide source mapping");
      var1.addMenuItem("Hide target mapping");
      var1.addMenuItem("Expand all trees");
      JMenu var2 = var1.addMenu("Search");
      var1.addMenu("Start", var2);
      this.m_search_nst = var1.addMenuItem("New source tree");
      this.m_search_ost = var1.addMenuItem("Old source tree");
      this.m_search_ott = var1.addMenuItem("Old target tree");
      this.m_search_ntt = var1.addMenuItem("New target tree");
      var1.addMenu("Next", var2);
      this.m_next_nst = var1.addMenuItem("New source tree", false);
      this.m_next_ost = var1.addMenuItem("Old source tree", false);
      this.m_next_ott = var1.addMenuItem("Old target tree", false);
      this.m_next_ntt = var1.addMenuItem("New target tree", false);
   }

   public void init() {
      this.setLayout(null);
      this.m_panel = new JPanel() {
         @Override
         public Dimension getPreferredSize() {
            int var1 = 0;

            for (int var2 = Migration.this.iComponentStart; var2 < Migration.this.iComponentEnd; var2++) {
               var1 += Migration.this.m_Components[var2].getWidth() + 2;
            }

            var1 -= 5;
            return new Dimension(var1, 100);
         }
      };
      this.m_scroll = new JScrollPane(this.m_panel, 21, 32);
      this.add(this.m_scroll);
      this.m_panel.setLayout(null);
      Migration.MouseHandler var1 = new Migration.MouseHandler();
      this.m_panel.addMouseListener(var1);
      this.m_panel.addMouseMotionListener(var1);
      this.setBackground(StagingPanel.m_clrBgdTree);
      this.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent var1) {
            Migration.this.RESIZE();
         }
      });
      MigrationTree var2 = new MigrationTree(this, this.m_new.getInputStructure());
      var2.setSearchTitle("Search New Source Tree");
      this.m_newSourceScrollPane = new TreeScrollPane(this, var2, this.m_newSourceJtr, "new SOURCE");
      this.m_newSourceTree = this.m_newSourceScrollPane.m_tree;
      MigrationTree var3 = new MigrationTree(this, this.m_old.getInputStructure());
      var3.setSearchTitle("Search Old Source Tree");
      this.m_oldSourceScrollPane = new TreeScrollPane(this, var3, this.m_oldSourceJtr, "old SOURCE");
      this.m_oldSourceTree = this.m_oldSourceScrollPane.m_tree;
      MigrationTree var4 = new MigrationTree(this, this.m_old.getOutputStructure());
      var4.setSearchTitle("Search Old Target Tree");
      this.m_oldTargetScrollPane = new TreeScrollPane(this, var4, this.m_oldTargetJtr, "old TARGET");
      this.m_oldTargetTree = this.m_oldTargetScrollPane.m_tree;
      MigrationTree var5 = new MigrationTree(this, this.m_new.getOutputStructure());
      var5.setSearchTitle("Search New Target Tree");
      this.m_newTargetScrollPane = new TreeScrollPane(this, var5, this.m_newTargetJtr, "new TARGET");
      this.m_newTargetTree = this.m_newTargetScrollPane.m_tree;
      this.m_sourceMapArea = new MapArea_Migration(this.m_newSourceScrollPane, this.m_oldSourceScrollPane, false);
      this.m_targetMapArea = new MapArea_Migration(this.m_oldTargetScrollPane, this.m_newTargetScrollPane, true);
      this.m_mapArea = new MapArea(this, this.m_old);
      this.m_oldSourceTree.setTables(null, this.m_mapArea.m_mappedSourceSet);
      this.m_oldTargetTree.setTables(this.m_mapArea.m_mappedTargetSet, null);
      this.m_panel.add(this.m_newSourceScrollPane);
      this.m_panel.add(this.m_sourceMapArea);
      this.m_panel.add(this.m_oldSourceScrollPane);
      this.m_panel.add(this.m_mapArea);
      this.m_panel.add(this.m_oldTargetScrollPane);
      this.m_panel.add(this.m_targetMapArea);
      this.m_panel.add(this.m_newTargetScrollPane);
      this.m_Components = new Component[7];
      this.m_Components[0] = this.m_newSourceScrollPane;
      this.m_Components[1] = this.m_sourceMapArea;
      this.m_Components[2] = this.m_oldSourceScrollPane;
      this.m_Components[3] = this.m_mapArea;
      this.m_Components[4] = this.m_oldTargetScrollPane;
      this.m_Components[5] = this.m_targetMapArea;
      this.m_Components[6] = this.m_newTargetScrollPane;
      this.expandAllTrees();
      this.display();
   }

   private void display() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            Migration.this.start();
            if (Migration.this.m_newSourceTree.equals(Migration.this.m_oldSourceTree)) {
               Migration.this.automaticSourceMapping();
               Migration.this.changeView(1);
            }

            if (Migration.this.m_newTargetTree.equals(Migration.this.m_oldTargetTree)) {
               Migration.this.automaticTargetMapping();
               Migration.this.changeView(2);
            }

            Migration.this.RESIZE();
         }
      });
   }

   private void start() {
      Dimension var1 = this.getContentPane().getSize();
      int var2 = var1.width;
      int var3 = var1.height;
      int var4 = (var2 - 12) * 6 / 31;
      this.m_scroll.setSize(var2, var3);
      int var5 = 0;
      int[] var6 = new int[]{var4, var4 / 3, var4, var4 / 2, var4, var4 / 3, var4};

      for (int var7 = this.iComponentStart; var7 < this.iComponentEnd; var7++) {
         if (var6[var7] < m_min_widths[var7]) {
            var6[var7] = m_min_widths[var7];
         }

         var5 += var6[var7] + 2;
      }

      var5 -= 2;
      if (var5 < var2) {
         var6[3] += var2 - var5;
      }

      var5 = 0;
      byte var11 = 17;

      for (int var8 = this.iComponentStart; var8 < this.iComponentEnd; var8++) {
         this.m_Components[var8].setBounds(var5, 0, var6[var8], var3 - var11);
         var5 += var6[var8] + 2;
      }

      this.doLayout();
      this.setVisible(true);
   }

   private void RESIZE() {
      Dimension var1 = this.getContentPane().getSize();
      int var2 = this.m_panel.getPreferredSize().width;
      int var3 = var1.width;
      int var4 = var1.height;
      float var5 = (float)var3 / var2;
      int var6 = 0;
      int[] var7 = new int[7];

      for (int var8 = this.iComponentStart; var8 < this.iComponentEnd; var8++) {
         var7[var8] = (int)(this.m_Components[var8].getWidth() * var5);
         if (var7[var8] < m_min_widths[var8]) {
            var7[var8] = m_min_widths[var8];
         }

         var6 += var7[var8] + 2;
      }

      var6 -= 2;
      if (var6 < var3) {
         var7[3] += var3 - var6;
      }

      var6 = 0;
      byte var12 = 17;

      for (int var9 = this.iComponentStart; var9 < this.iComponentEnd; var9++) {
         this.m_Components[var9].setBounds(var6, 0, var7[var9], var4 - var12);
         var6 += var7[var9] + 2;
         if (this.m_Components[var9] instanceof TreeScrollPane) {
            ((TreeScrollPane)this.m_Components[var9]).refresh();
         }
      }

      this.m_scroll.setSize(var3, var4);
      this.validate();
   }

   public void addSourceMapPair(Node var1, Node var2, int var3) {
      if (var1.isFolder() == var2.isFolder()) {
         this.m_sourceMapArea.put(var1, var2);
         int var4 = var2.getChildCount();
         int var5 = var1.getChildCount();

         for (int var6 = 0; var6 < var4; var6++) {
            Node var7 = var2.getChildAt(var6);
            String var8 = var7.getAttributeName();

            for (int var9 = 0; var9 < var5; var9++) {
               Node var10 = var1.getChildAt(var9);
               if (var8.equalsIgnoreCase(var10.getAttributeName())) {
                  this.addSourceMapPair(var10, var7, var3 + 1);
                  break;
               }
            }
         }

         this.setDirty(true);
      }
   }

   public void addTargetMapPair(Node var1, Node var2, int var3) {
      if (var1 != null && var2 != null) {
         if (var1.isFolder() == var2.isFolder()) {
            this.m_targetMapArea.put(var1, var2);
            if (var1.hasPrecondition()) {
               this.m_newTargetTree.insertPreconditionNode(var2);
            }

            if (var1.hasMultipleMap()) {
               for (Node var6 : this.findMultipleMappingTargetList(var1)) {
                  String var7 = ScriptUtils.getInstanceTag(var6.m_deName);

                  try {
                     Utils.insertMultipleMapping(var2, Integer.parseInt(var7.substring(1)));
                  } catch (Exception var14) {
                     var14.printStackTrace();
                  }
               }
            }

            Node var15 = var1.getFirstChild();
            if (var15 != null && var15.isPrecondition()) {
               Node var16 = var2.getFirstChild();
               if (var16 != null && !var16.isPrecondition()) {
                  var16 = this.m_newTargetTree.insertPreconditionNode(var2);
               }

               if (var16 != null) {
                  this.addTargetMapPair(var15, var16, var3 + 1);
               }
            }

            for (Node var18 : var1.getChildren()) {
               if (var18.isMultipleMap()) {
                  Node var20 = Utils.getMultipleMappingOriginal(var18);
                  Node var21 = null;

                  for (Entry var10 : this.m_targetMapArea.m_table_new2old.entrySet()) {
                     if (var10.getValue() == var20) {
                        var21 = (Node)var10.getKey();
                        break;
                     }
                  }

                  String var23 = ScriptUtils.getInstanceTag(var18.m_deName);

                  for (Node var11 : var2.getChildren()) {
                     if (var11.isMultipleMap()) {
                        Node var12 = Utils.getMultipleMappingOriginal(var11);
                        String var13 = ScriptUtils.getInstanceTag(var11.m_deName);
                        if (var12 != null && var21 != null && var23.equals(var13) && var12.getName().equals(var21.getName())) {
                           this.addTargetMapPair(var18, var11, var3 + 1);
                           break;
                        }
                     }
                  }
               } else {
                  for (Node var8 : var2.getChildren()) {
                     String var9 = var8.getAttributeName();
                     if (var9 != null && var9.equalsIgnoreCase(var18.getAttributeName())) {
                        this.addTargetMapPair(var18, var8, var3 + 1);
                        break;
                     }
                  }
               }
            }

            this.setDirty(true);
         }
      }
   }

   private List<Node> findMultipleMappingTargetList(Node var1) {
      ArrayList var2 = Lists.newArrayList();

      for (Node var4 : var1.m_parent.getChildren()) {
         if (var4 != var1) {
            String var5 = ScriptUtils.stripInstanceTag(var4.m_deName);
            if (var1 == this.m_oldTargetTree.findNode(var5)) {
               var2.add(var4);
            }
         }
      }

      return var2;
   }

   private void automaticTargetMapping() {
      boolean var1 = false;

      for (Node var3 : this.m_oldTargetTree.m_root.getChildren()) {
         for (Node var5 : this.m_newTargetTree.m_root.getChildren()) {
            if (var3.m_deName.equalsIgnoreCase(var5.m_deName)) {
               this.addTargetMapPair(var3, var5, 0);
               var1 = true;
               break;
            }
         }
      }

      if (var1) {
         this.m_newTargetTree.repaint();
         this.m_oldTargetTree.repaint();
      }
   }

   private void automaticSourceMapping() {
      boolean var1 = false;

      for (Node var3 : this.m_oldSourceTree.m_root.getChildren()) {
         for (Node var5 : this.m_newSourceTree.m_root.getChildren()) {
            if (var3.m_deName.equalsIgnoreCase(var5.m_deName)) {
               this.addSourceMapPair(var3, var5, 0);
               var1 = true;
               break;
            }
         }
      }

      if (var1) {
         this.m_newSourceTree.repaint();
         this.m_oldSourceTree.repaint();
      }
   }

   private void removeSourceMapping() {
      this.m_sourceMapArea.removeMappings();
      this.m_newSourceTree.repaint();
      this.m_oldSourceTree.repaint();
      this.setDirty(true);
   }

   private void removeTargetMapping() {
      this.m_targetMapArea.removeMappings();
      this.m_newTargetTree.repaint();
      this.m_oldTargetTree.repaint();
      this.setDirty(true);
   }

   private void save() {
      List var1 = Utils.readMapFile(this.m_old.getMapList());
      ArrayList var2 = Lists.newArrayList();

      try {
         Map var3 = this.m_sourceMapArea.m_table_old2new;
         Map var4 = this.m_targetMapArea.m_table_new2old;
         MigrationUtils.migrateMapping(var1, var3, var4, this);

         for (Object __etlItem612 : var1) {
            String[] var6 = (String[]) __etlItem612;
            if (var6[0] != null && var6[0].length() > 0 && var6[1] != null && var6[1].length() > 0) {
               String var7 = var6[0] + "\t" + var6[1];
               var2.add(var7);
            }
         }
      } catch (ScriptException var9) {
         Alert.error(var9.getMessage(), "Error");
         return;
      }

      try {
         UiCompanion var10 = UiCompanion.of(this.m_new);
         Editor var11 = var10.findOpenPage();
         if (var11 instanceof TransformationPage) {
            TreeMapper var12 = ((TransformationPage)var11).getTreeMapper();
            var12.setMapList(var2, true);
            var12.updateHiddenNodesOnMapChange();
         }

         this.m_new.setMapList(var2);
         this.m_TreeMapper.getTreeMapperContainer().save(this.m_new);
         var10.openPage();
      } catch (TreeMapperException var8) {
         ErrorLog.attention(Migration.class, "Failed to apply the new mappings.", var8);
      }

      this.setDirty(false);
   }

   private void changeView(int var1) {
      if (this.show_mode != var1) {
         int var2 = this.show_mode;
         this.show_mode = var1;
         label35:
         switch (this.show_mode) {
            case 0:
               switch (var2) {
                  case 1:
                     this.m_panel.add(this.m_newSourceScrollPane);
                     this.m_panel.add(this.m_sourceMapArea);
                     break label35;
                  case 2:
                     this.m_panel.add(this.m_newTargetScrollPane);
                     this.m_panel.add(this.m_targetMapArea);
                  default:
                     break label35;
               }
            case 1:
               switch (var2) {
                  case 0:
                     this.m_panel.remove(this.m_newSourceScrollPane);
                     this.m_panel.remove(this.m_sourceMapArea);
                     break label35;
                  case 2:
                     this.m_panel.remove(this.m_newSourceScrollPane);
                     this.m_panel.remove(this.m_sourceMapArea);
                     this.m_panel.add(this.m_newTargetScrollPane);
                     this.m_panel.add(this.m_targetMapArea);
                  default:
                     break label35;
               }
            case 2:
               switch (var2) {
                  case 0:
                     this.m_panel.remove(this.m_newTargetScrollPane);
                     this.m_panel.remove(this.m_targetMapArea);
                     break;
                  case 1:
                     this.m_panel.add(this.m_newSourceScrollPane);
                     this.m_panel.add(this.m_sourceMapArea);
                     this.m_panel.remove(this.m_newTargetScrollPane);
                     this.m_panel.remove(this.m_targetMapArea);
               }
         }

         this.iComponentStart = this.show_mode != 1 ? 0 : 2;
         this.iComponentEnd = this.show_mode != 2 ? 7 : 5;
         this.RESIZE();
      }
   }

   public boolean draw_left_line(TreeComponent var1) {
      return var1 != this.m_oldSourceTree || this.show_mode != 1;
   }

   public boolean draw_right_line(TreeComponent var1) {
      return var1 != this.m_oldTargetTree || this.show_mode != 2;
   }

   private void saveMappings(boolean var1) {
      File var2 = ApplicationFileChooser.getInstance(this.m_window)
         .getFileToSave(this, new SuffixFileChooserFilter(".mtb", true), var1 ? "Write source mapping file" : "Write target mapping file");
      if (var2 != null) {
         Map var3 = this.m_sourceMapArea.m_table_old2new;
         Map var4 = this.m_targetMapArea.m_table_new2old;
         String[] var5 = new String[var3.size()];
         Map var6 = var1 ? var3 : var4;
         int var7 = 0;

         for (Object __etlItem712 : var6.entrySet()) {
            Entry var9 = (Entry) __etlItem712;
            var5[var7++] = ((Node)var9.getKey()).m_deName + "\t" + ((Node)var9.getValue()).m_deName;
         }

         try {
            KongaIoUtils.createAndWrite(var2, var5, true);
         } catch (IOException var10) {
            var10.printStackTrace();
         }
      }
   }

   private void expandAllTrees() {
      this.m_newSourceTree.changeShowChildren(true);
      this.m_oldSourceTree.changeShowChildren(true);
      this.m_oldTargetTree.changeShowChildren(true);
      this.m_newTargetTree.changeShowChildren(true);
   }

   public String manualConversion(String var1, String var2, String var3) {
      return ManualMigrationConversionDialog.getConvertedValue(this, var1, var2, var3);
   }

   public boolean checkToContinue(String var1, String var2, String var3, String var4) {
      return ContinueMigrationPrompt.prompt(this, var1, var2, var3, var4);
   }

   private void readMappings(boolean var1) {
      Migration.ReadMappingDialog var2 = new Migration.ReadMappingDialog(var1);
      var2.setVisible(true);
      if (var2.file != null) {
         boolean var3 = var2.invert;

         try {
            MapArea_Migration var4 = var1 ? this.m_sourceMapArea : this.m_targetMapArea;
            TreeComponent var5 = var1 ? this.m_oldSourceTree : this.m_oldTargetTree;
            TreeComponent var6 = var1 ? this.m_newSourceTree : this.m_newTargetTree;
            var4.removeMappings();
            String[] var7 = KongaIoUtils.readLines(var2.file);

            for (String var11 : var7) {
               int var12 = var11.indexOf(9);
               if (var12 >= 0) {
                  String var13 = var11.substring(0, var12);
                  String var14 = var11.substring(var12 + 1);
                  Node var15 = var5.findNode(var3 ? var14 : var13);
                  Node var16 = var6.findNode(var3 ? var13 : var14);
                  if (var15 != null && var16 != null) {
                     var4.m_table_old2new.put(var15, var16);
                     var4.m_table_new2old.put(var16, var15);
                  } else if (var15 == null && var16 == null) {
                     System.out.println("Can't find old_node:" + (var3 ? var14 : var13));
                     System.out.println("           new_node:" + (var3 ? var13 : var14));
                  } else if (var15 == null) {
                     System.out.println("Can't find old_node:" + (var3 ? var14 : var13));
                  } else {
                     System.out.println("Can't find new_node:" + (var3 ? var13 : var14));
                  }
               }
            }

            var4.repaint();
         } catch (IOException var17) {
            var17.printStackTrace();
         }
      }
   }

   private void setDirty(boolean var1) {
      this.m_dirty = var1;
   }

   private boolean isDirty() {
      return this.m_dirty;
   }

   @Override
   public void dispose() {
      if (this.isDirty()) {
         Answer var1 = Ask.yesNoOrCancel("Do you want to migrate the mappings?", "The migration page is dirty!");
         if (var1.isCancel()) {
            this.setVisible(true);
            return;
         }

         if (var1.isYes()) {
            this.save();
         }
      }

      super.dispose();
   }

   private class MenuEventHandler implements ActionListener {
      private MenuEventHandler() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         Object var2 = var1.getSource();
         if (var2 instanceof JMenuItem) {
            String var3 = var1.getActionCommand();
            if (var3.equals("Read source mapping ...")) {
               Migration.this.readMappings(true);
            } else if (var3.equals("Read target mapping ...")) {
               Migration.this.readMappings(false);
            } else if (var3.equals("Write source mapping ...")) {
               Migration.this.saveMappings(true);
            } else if (var3.equals("Write target mapping ...")) {
               Migration.this.saveMappings(false);
            } else if (var3.equals("Migrate mappings")) {
               Migration.this.save();
            } else if (var3.equals("Exit")) {
               Migration.this.m_TreeMapper.closeTreeMapperMigrationFrame();
            } else if (var3.equals("Auto source mapping")) {
               Migration.this.automaticSourceMapping();
            } else if (var3.equals("Auto target mapping")) {
               Migration.this.automaticTargetMapping();
            } else if (var3.equals("Remove source mapping")) {
               Migration.this.removeSourceMapping();
            } else if (var3.equals("Remove target mapping")) {
               Migration.this.removeTargetMapping();
            } else if (var3.equals("Show all mappings")) {
               Migration.this.changeView(0);
            } else if (var3.equals("Hide source mapping")) {
               Migration.this.changeView(1);
            } else if (var3.equals("Hide target mapping")) {
               Migration.this.changeView(2);
            } else if (var3.equals("Expand all trees")) {
               Migration.this.expandAllTrees();
            } else if (var2 == Migration.this.m_search_nst) {
               Migration.this.m_newSourceTree.search();
            } else if (var2 == Migration.this.m_search_ost) {
               Migration.this.m_oldSourceTree.search();
            } else if (var2 == Migration.this.m_search_ott) {
               Migration.this.m_oldTargetTree.search();
            } else if (var2 == Migration.this.m_search_ntt) {
               Migration.this.m_newTargetTree.search();
            } else if (var2 != Migration.this.m_next_nst
               && var2 != Migration.this.m_next_ost
               && var2 != Migration.this.m_next_ott
               && var2 == Migration.this.m_next_ntt) {
            }

            for (Component var7 : Migration.this.m_Components) {
               if (var7 instanceof TreeScrollPane) {
                  ((TreeScrollPane)var7).refresh();
               }
            }

            Migration.this.validate();
         }
      }
   }

   private class MouseHandler implements MouseInputListener {
      private int last_x;
      private int whichEdge;

      private MouseHandler() {
      }

      @Override
      public void mouseMoved(MouseEvent var1) {
      }

      @Override
      public void mousePressed(MouseEvent var1) {
         if (var1.getSource() == Migration.this.m_panel) {
            int var2 = var1.getX();
            this.whichEdge = -1;

            for (int var3 = 1; var3 < 7; var3++) {
               int var4 = Migration.this.m_Components[var3].getLocation().x - 2;
               int var5 = var4 + 4;
               if (var2 >= var4 && var2 <= var5) {
                  this.whichEdge = var3;
                  break;
               }
            }

            if (this.whichEdge > 0 && this.whichEdge < 7) {
               int var8 = Migration.this.m_scroll.getHorizontalScrollBar().getValue();
               var2 -= var8;
               Migration.this.m_dragImg.removeAll();
               Migration.this.m_dragImg.setLayout(null);
               JPanel var9 = new JPanel();
               var9.setBackground(Color.black);
               Dimension var10 = Migration.this.getContentPane().getSize();
               int var6 = var10.height;
               var9.setBounds(0, 0, 3, var6);
               Migration.this.m_dragImg.add(var9);
               Migration.this.m_dragImg.setBounds(var2 - 1, 0, 3, var6);
               Migration.this.m_dragImg.setVisible(true);
               this.last_x = var2;
            }
         }
      }

      @Override
      public void mouseDragged(MouseEvent var1) {
         if (var1.getSource() == Migration.this.m_panel && this.whichEdge >= 0) {
            int var2 = var1.getX();
            int var3 = Migration.this.m_scroll.getHorizontalScrollBar().getValue();
            var2 -= var3;
            Migration.this.m_dragImg.setLocation(var2 - 1, 0);
         }
      }

      @Override
      public void mouseReleased(MouseEvent var1) {
         if (this.whichEdge > 0 && this.whichEdge < 7) {
            Migration.this.m_dragImg.setVisible(false);
            int var2 = var1.getX();
            int var3 = Migration.this.m_scroll.getHorizontalScrollBar().getValue();
            var2 -= var3;
            int var4 = var2 - this.last_x;
            Rectangle var5 = Migration.this.m_Components[this.whichEdge - 1].getBounds();
            Rectangle var6 = Migration.this.m_Components[this.whichEdge].getBounds();
            Migration.this.m_Components[this.whichEdge - 1].setBounds(var5.x, var5.y, var5.width + var4, var5.height);
            Migration.this.m_Components[this.whichEdge].setBounds(var6.x + var4, var6.y, var6.width - var4, var6.height);
            this.whichEdge = 0;
            Migration.this.RESIZE();
         }
      }

      @Override
      public void mouseEntered(MouseEvent var1) {
         if (!Migration.this.getCursor().equals(Cursor.getPredefinedCursor(11))) {
            Migration.this.setCursor(Cursor.getPredefinedCursor(11));
         }
      }

      @Override
      public void mouseExited(MouseEvent var1) {
         if (!Migration.this.getCursor().equals(Cursor.getDefaultCursor())) {
            Migration.this.setCursor(Cursor.getDefaultCursor());
         }
      }

      @Override
      public void mouseClicked(MouseEvent var1) {
      }
   }

   private class ReadMappingDialog extends KongaDialog {
      private JRadioButton defaultButton;
      private JRadioButton invertedButton;
      private JTextField filePathTextField;
      private JButton browseButton;
      private boolean invert;
      private File file;

      public ReadMappingDialog(boolean var2) {
         super(Migration.this, var2 ? "Read source mapping file" : "Read target mapping file", ModalityType.APPLICATION_MODAL);
         this.createComponents();
         this.layoutComponents();
         this.pack();
         this.setResizable(false);
         this.manageLocation();
         NonEmptyTextEnablerCondition.enableComponentWhenFieldNotEmpty(this.getOKButton(), this.filePathTextField);
      }

      @Override
      protected void onOk() {
         this.invert = !this.defaultButton.isSelected();
         if (this.file != null && this.file.isFile()) {
            if (this.file.length() == 0L) {
               String var2 = "The input file is empty.";
               Alert.error(this, var2, "Empty file");
               this.file = null;
            }

            super.onOk();
         } else {
            String var1 = "The input file does not exist.";
            Alert.error(this, var1, "File not found");
         }
      }

      @Override
      protected void onCancel() {
         this.file = null;
         super.onCancel();
      }

      private void createComponents() {
         this.createRadioButtons();
         this.createFileInputComponents();
      }

      private void createRadioButtons() {
         this.defaultButton = new JRadioButton("Map new to old", true);
         this.invertedButton = new JRadioButton("Map old to new", false);
         TextStyles.DefaultRadioButtonText.makeOver(this.defaultButton, this.invertedButton);
         ButtonUtils.makeMutuallyExclusive(this.defaultButton, this.invertedButton);
      }

      private void createFileInputComponents() {
         this.filePathTextField = new KongaTextField(30);
         this.browseButton = new KongaButton(ApplicationResources.STRINGS.getString("Browse..."));
         this.browseButton
            .addActionListener(
               new ActionListener() {
                  @Override
                  public void actionPerformed(ActionEvent var1) {
                     ReadMappingDialog.this.file = ApplicationFileChooser.getInstance(Migration.this.m_window)
                        .getFileToOpen(ReadMappingDialog.this, new SuffixFileChooserFilter(".mtb", true), ReadMappingDialog.this.getTitle());
                     if (ReadMappingDialog.this.file != null) {
                        ReadMappingDialog.this.filePathTextField.setText(ReadMappingDialog.this.file.getAbsolutePath());
                     }
                  }
               }
            );
      }

      private void layoutComponents() {
         JPanel var1 = this.getButtonPanel(OK_CANCEL, 15);
         var1.setBorder(Empty.border(15, 0, 10, 0));
         BorderLayoutBuilder var2 = new BorderLayoutBuilder();
         var2.center(this.getInputPanel()).south(var1).withBorder(Empty.border(10, 10, 5, 10));
         var2.asContentPaneFor(this);
      }

      private JComponent getInputPanel() {
         JPanel var1 = new JPanel(new GridBagLayout());
         GridBagConstraints var2 = new GridBagConstraints();
         var2.anchor = 17;
         var2.insets = new Insets(3, 5, 3, 5);
         var2.fill = 2;
         var2.gridx = 0;
         var2.gridwidth = 2;
         var1.add(TextStyles.AdminHint.makeLabel("Mode:"), var2);
         var1.add(this.getRadioButtonPanel(), var2);
         var2.gridwidth = 1;
         var1.add(this.filePathTextField, var2);
         var2.gridx = 1;
         var1.add(this.browseButton, var2);
         var1.setBorder(new BorderBuilder().etched().empty(5));
         return var1;
      }

      private JComponent getRadioButtonPanel() {
         return Horizontal.box().of(this.defaultButton, this.invertedButton).container();
      }
   }
}
