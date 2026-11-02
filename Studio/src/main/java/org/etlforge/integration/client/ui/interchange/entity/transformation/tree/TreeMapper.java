package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.RepaintManager;
import javax.swing.event.MouseInputAdapter;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.entity.settings.EntitySettings;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.client.ui.interchange.entity.transformation.TransformationTreeTitleGenerator;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.RememberTreeMapperTestDataPreference;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.dnd.SourceTreeFileDropStrategy;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaDisplayer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.grammar.CheckingException;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.grammar.TLNOptionViewer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.MappingErrorHandler;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.MappingManager;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.NodeMapping;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.NodeMappingFactory;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.SuggestMappingsAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.migration.Migration;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.migration.MigrationInitializationException;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.panel.ButtonPanel;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.panel.MappingConflictChecker;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.prefs.DrawAttributeMappingsPreference;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.prefs.DrawErrorMappingsPreference;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.prefs.DrawGeneratorMappingsPreference;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.search.TreeMapperSearchUi;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.DataTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.MapArea;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.NodeNameUtils;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.SimpleSfdcStructureRootLocator;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TipWindow;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponentScrollPane;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Utils;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.TransformationVariables;
import org.etlforge.integration.data.script.ScriptUtils;
import org.etlforge.integration.data.script.Transform;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.debug.client.BreakLocation;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.structure.mapping.MapStringFormat;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.MappingFunctions;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.integration.structure.reader.JbNode;
import org.etlforge.integration.structure.reader.TextDocReader;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.lang.Intervals;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.print.HasPrintable;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.util.persist.Persistor;
import org.etlforge.xml.ExpandedName;

public final class TreeMapper extends StagingPanel implements Disposable, HasPrintable {
   public static final String MODE_PROPERTY = "mode";
   private static final String NODE_EXPANSION_MODE_KEY = "TreeMapper.NodeExpansionMode";
   private static final int g_dxStop = 85;
   private static final int g_treePanelHeightMin = 150;
   public final Transformation m_Transformation;
   private EnumSet<MapAreaDisplayOption> mapAreaDisplay;
   private final InputAndOutputMappingTreeStructureCache m_structureCache;
   private final EntitySettings m_transformationSettings;
   public final TreeMapperSourceTree m_sourceTreeComponent;
   public final TreeMapperTargetTree m_targetTreeComponent;
   private int m_sourceScrollPart = 40;
   private int m_mapAreaPart = 20;
   private int m_targetScrollPart = 40;
   private final MapAction mapAction;
   private final MultipleMapAction multipleMapAction;
   private final KongaButton mapButton;
   private final AutoMapperAction autoMapperAction;
   public final TipWindow m_tip;
   public Transform m_transform;
   public List<Transform.Token> m_stackVector;
   private List<String> m_deVector;
   private boolean m_bDisplayMapArea = true;
   public MapArea m_mapArea;
   public final Map<Node, Set<Node>> m_mapAreaTable = Maps.newHashMap();
   public final Map<Node, Set<Node>> m_conflictMap = Maps.newHashMap();
   public final Map<Node, Node> m_generatorMap = Maps.newHashMap();
   public final Map<Node, String> m_sourceExpressionTable = Maps.newHashMap();
   private NodeExpansionMode m_nodeExpansionMode;
   public TreeMapper.TreePanel m_treePanel;
   private final ButtonPanel m_buttonPanel;
   public TreeComponentScrollPane m_sourceScroll;
   public TreeComponentScrollPane m_targetScroll;
   private String m_sourceTitle;
   private String m_targetTitle;
   public int m_appletWidth;
   private int m_appletHeight;
   private int m_scrollHeights;
   private int m_treePanelHeight;
   private int m_sourceScrollWidth;
   private int m_mapAreaWidth;
   private int m_targetScrollWidth;
   public final JPanel m_dragImage;
   private JPanel m_horizontalLine;
   private JPanel m_verticalLine;
   private Migration m_Migration;
   private final TreeMapperContainer m_container;
   private UiJob loadStructuresJob;
   private boolean m_mappingsHaveBeenLoaded;
   private boolean m_bMappingMode;
   private NodeMappingFactory mappingFactory;
   private MappingManager mappingManager;
   private TreeMapperSearchUi searchUi;
   private KongaDialog searchDialog;
   private Map<String, String> renamedTargetNodes;
   private final TreeMapper.MappingTreeUpdater structureUpdater;
   public boolean bSkipPainting;
   public boolean bPrintingInitialized;
   private TreeMapperSourceBatchNodeDefiner batchNodeDefiner;
   private StructureLink inputLink;
   private StructureLink outputLink;
   private TreeMapperMode mode;
   private final DataTree sourceDataTree;
   private final DataTree targetDataTree;
   private final TransformationVariables projectVariables;
   private final Map<Node, Node> warnings = Maps.newHashMap();
   private final StraightMappingSourceExpressionFactory straightMappingFactory;
   int sourceScrollValue;
   int targetScrollValue;
   private Node source_saved;
   private NodeMapping mp_saved;
   private int iSource_saved;
   private int iTarget_saved;

   public TreeMapper(Transformation var1, TreeMapperContainer var2) {
      this.m_tip = new TipWindow(this);
      this.m_Transformation = var1;
      this.m_container = var2;
      this.m_structureCache = this.lookupStructureCacheInstance();
      this.m_transformationSettings = this.lookupTransformationSettings();
      this.applyPreferredTextSize();
      this.m_dragImage = new JPanel();
      this.m_dragImage.setVisible(false);
      this.m_nodeExpansionMode = this.getInitialExpansionMode();
      this.renamedTargetNodes = var1.getRenamedNodes();
      this.structureUpdater = new TreeMapper.MappingTreeUpdater();
      this.mapAreaDisplay = this.restoreMapDisplaySettings();
      this.m_bDisplayMapArea = !this.mapAreaDisplay.isEmpty();
      this.inputLink = StructureLink.none();
      this.outputLink = StructureLink.none();
      this.mode = TreeMapperMode.SIMPLE;
      this.sourceDataTree = new DataTree(this);
      this.targetDataTree = new DataTree(this);
      this.straightMappingFactory = new StraightMappingSourceExpressionFactory(var1);
      GlobalDataElements var3 = this.getTreeMapperContainer().getProject().getGlobalDataElements();
      this.projectVariables = new TransformationVariables(var3);
      BooleanSourceAndTargetSetting var4 = this.restoreSimpleNodeDisplaySettings();
      BooleanSourceAndTargetSetting var5 = this.restoreSimpleXmlElementDisplaySettings();
      BooleanSourceAndTargetSetting var6 = this.restoreShowMappedNodesOnlySettings();
      this.m_sourceTreeComponent = new TreeMapperSourceTree(this, var4.forSource(), var5.forSource(), var6.forSource());
      this.m_targetTreeComponent = new TreeMapperTargetTree(this, var4.forTarget(), var5.forTarget(), var6.forTarget());
      installMappingSuggestionTrigger(this.m_sourceTreeComponent, this.m_targetTreeComponent);
      installMappingSuggestionTrigger(this.m_targetTreeComponent, this.m_sourceTreeComponent);
      this.mapAction = new MapAction(this);
      this.multipleMapAction = new MultipleMapAction(this);
      this.mapButton = createButtonFor(this.mapAction);
      this.autoMapperAction = new AutoMapperAction(this);
      this.m_buttonPanel = ClientInfo.isDataLoader()
         ? null
         : new ButtonPanel(
            this,
            this.mapButton,
            this.autoMapperAction,
            this.getPageAction("action.ic.tx.LoadSource"),
            this.getPageAction("action.ic.tx.TestTransformation"),
            this.getPageAction("action.ic.tx.TestOperation")
         );
      this.m_treePanel = new TreeMapper.TreePanel();
      var1.addPropertyChangeListener("MappingStructures", this.structureUpdater);
      this.listenToTargetTreeSelection();
      this.init();
   }

   private void listenToTargetTreeSelection() {
      this.m_targetTreeComponent
         .addSelectionListener(
            new SelectionListener() {
               @Override
               public void selectionChanged(SelectionChangedEvent var1) {
                  Node var2 = TreeMapper.this.m_targetTreeComponent.getHighlightedNode();
                  if (var2 == null) {
                     TreeMapper.this.mapButton.setAction(TreeMapper.this.mapAction);
                  } else if (!var2.isFolder()) {
                     TreeMapper.this.mapButton.setAction(TreeMapper.this.mapAction);
                  } else if ((!TreeMapper.this.isTargetXml() || !var2.isLoopNode())
                     && !var2.isOptional()
                     && (TreeMapper.this.isTargetXml() || !var2.isFolder())
                     && !var2.isChildOfOneOfNode()) {
                     TreeMapper.this.mapButton.setAction(TreeMapper.this.mapAction);
                  } else {
                     TreeMapper.this.mapButton.setAction(TreeMapper.this.multipleMapAction);
                  }
               }
            }
         );
   }

   private static void installMappingSuggestionTrigger(TreeComponent var0, TreeComponent var1) {
      ActionKeyBinding.install(new SuggestMappingsAction(var0, var1), var0);
   }

   public Action getPageAction(String var1) {
      return this.m_container.getPageAction(var1);
   }

   private static KongaButton createButtonFor(Action var0) {
      KongaButton var1 = KongaButton.forToolbar(var0);
      var1.setUseMediumActionIcon(true);
      return var1;
   }

   public Transformation getTransformation() {
      return this.m_Transformation;
   }

   public TransformationVariables getDataElements() {
      return this.projectVariables;
   }

   private void init() {
      this.setFont(m_font11Plain);
      this.setLayout(null);
      this.add(this.m_dragImage);
      this.generateTreeTitles();
      this.setDirty(false, false);
      this.m_sourceTreeComponent.setDropStrategy(new SourceTreeFileDropStrategy(this));
      AdjustmentListener var1 = new AdjustmentListener() {
         @Override
         public void adjustmentValueChanged(AdjustmentEvent var1) {
            TreeMapper.this.m_mapArea.repaint();
         }
      };
      this.m_sourceScroll = new TreeComponentScrollPane(this.m_sourceTreeComponent);
      this.m_sourceScroll.getVerticalScrollBar().addAdjustmentListener(var1);
      this.m_targetScroll = new TreeComponentScrollPane(this.m_targetTreeComponent);
      this.m_targetScroll.getVerticalScrollBar().addAdjustmentListener(var1);
      this.m_sourceTreeComponent.m_scroll = this.m_sourceScroll;
      this.m_targetTreeComponent.m_scroll = this.m_targetScroll;
      this.m_sourceTreeComponent.init();
      this.m_targetTreeComponent.init();
      this.mappingFactory = new NodeMappingFactory(this);
      this.mappingManager = new MappingManager(this);
      this.m_horizontalLine = new TreeMapper.LinePanel();
      this.add(this.m_horizontalLine);
      this.m_verticalLine = new TreeMapper.LinePanel();
      this.add(this.m_verticalLine);
      if (this.m_sourceTreeComponent.m_flat) {
      }

      this.m_mapArea = new MapArea(this);
      this.m_treePanel.add(this.m_mapArea);
      this.installFocusKeyBindings();
      this.start();
      this.loadStructures(
         true,
         new RefreshStructuresCallback() {
            @Override
            public void cancelled() {
            }

            @Override
            public void done() {
               EventQueue.invokeLater(
                  new Runnable() {
                     @Override
                     public void run() {
                        this.createInputAndOutputLinks();
                        this.restoreExpansionState();
                        if (RememberTreeMapperTestDataPreference.get()) {
                           this.restoreSourceData();
                           this.restoreTargetData();
                        }

                        this.checkInitialDisplayMode();
                        TreeMapper.this.checkSimpleSfdcRepresentation();
                     }

                     private void createInputAndOutputLinks() {
                        StructureLinkFactory var1x = new StructureLinkFactory(TreeMapper.this.m_Transformation);
                        TreeMapper.this.inputLink = var1x.inputLink();
                        TreeMapper.this.outputLink = var1x.outputLink();
                     }

                     private void restoreExpansionState() {
                        try {
                           Persistor var1x = TreeMapper.this.m_transformationSettings.getEntitySettings(TreeMapper.this.m_Transformation);
                           NodeExpansionMemento var2 = new NodeExpansionMemento(TreeMapper.this, var1x);
                           var2.restore();
                        } catch (Exception var3) {
                           ErrorLog.log(
                              TreeMapper.class,
                              "Failed to restore the node expansion state for the transformation \"" + TreeMapper.this.m_Transformation.getName(),
                              var3
                           );
                        }
                     }

                     private void restoreSourceData() {
                     }

                     private void restoreTargetData() {
                     }

                     private void checkInitialDisplayMode() {
                        TreeMapperMode var1x = TreeMapperMode.SIMPLE;
                        ExpandedName var2 = TreeMapper.this.m_Transformation.getSalesforceObject();
                        if (var2 != null && var2.getLocalName().equals("AggregateResult")) {
                           var1x = TreeMapperMode.FULL;
                        }

                        TreeMapper.this.mode = var1x;
                     }
                  }
               );
            }

            @Override
            public MappingErrorHandler getMappingErrorHandler() {
               return MappingErrorHandler.IGNORE;
            }
         }
      );
   }

   private void checkSimpleSfdcRepresentation() {
      if (this.isPartOfSalesforceWizard()) {
         SimpleSfdcStructureRootLocator var1 = new SimpleSfdcStructureRootLocator(
            this.getTransformation(), this.m_sourceTreeComponent, this.m_targetTreeComponent
         );
         var1.locateSimpleRepresentationRoot();
      }
   }

   private boolean isPartOfSalesforceWizard() {
      return this.m_Transformation.isPartOfSalesforceWizard();
   }

   private void generateTreeTitles() {
      TransformationTreeTitleGenerator var1 = new TransformationTreeTitleGenerator(this.m_Transformation);
      this.m_sourceTitle = var1.getSourceTreeTitle();
      if (this.m_sourceTitle == null) {
         this.m_sourceTitle = "SOURCE: ";
      } else {
         this.m_sourceTitle = "SOURCE: " + this.m_sourceTitle;
      }

      this.m_targetTitle = var1.getTargetTreeTitle();
      if (this.m_targetTitle == null) {
         this.m_targetTitle = "TARGET: ";
      } else {
         this.m_targetTitle = "TARGET: " + this.m_targetTitle;
      }
   }

   public boolean hasSimpleDisplay() {
      return this.isPartOfSalesforceWizard();
   }

   public void setMode(TreeMapperMode var1) {
      if (var1 != this.mode) {
         TreeMapperMode var2 = this.mode;
         this.mode = var1;
         this.m_sourceTreeComponent.m_root.updateHiddenFlags();
         this.m_targetTreeComponent.m_root.updateHiddenFlags();
         FormulaDisplayer var3 = this.m_container.getFormulaDisplayer();
         var3.updateSourceTreeDisplayMode(var1);
         this.m_sourceTreeComponent.refresh();
         this.m_targetTreeComponent.refresh();
         this.repaint();
         this.firePropertyChange("mode", var2, var1);
      }
   }

   public TreeMapperMode getMode() {
      return this.mode;
   }

   public void updateMapAreaTables(Mapping var1) {
      this.updateMapAreaTables(var1.getTargetPath().toString(), var1.getExpression());
   }

   public void updateMapAreaTables(String var1, String var2) {
      Node var3 = this.m_targetTreeComponent.findNode(var1);
      if (var2 != null && var2.length() != 0) {
         Set var4 = Utils.getSourceExpressionList(this.m_sourceTreeComponent, var2, this.m_transform);
         if (var4 == null) {
            var4 = Sets.newHashSet();
         }

         this.m_mapAreaTable.put(var3, var4);
         this.m_sourceExpressionTable.put(var3, var2);
      } else {
         this.m_mapAreaTable.remove(var3);
         this.m_sourceExpressionTable.remove(var3);
      }
   }

   public InputAndOutputMappingTreeStructureCache getTreeStructureCache() {
      return this.m_structureCache;
   }

   public void refreshTreeStructures(RefreshStructuresCallback var1) {
      ApplicationUiUtils.logIfNotOnEDT(TreeMapper.class, "refreshTreeStructures");
      this.m_mapAreaTable.clear();
      this.applyCurrentFormulas();
      this.loadStructures(false, var1);
   }

   public void applyCurrentFormulas() {
      this.m_container.getFormulaDisplayer().applyCurrentFormulas();
   }

   public void prepareStructureRestoration() {
      this.m_mappingsHaveBeenLoaded = false;
   }

   private InputAndOutputMappingTreeStructureCache lookupStructureCacheInstance() {
      ManagedProject var1 = this.getManagedProject();
      return var1.getTransformationMappingStructureCache();
   }

   private ManagedProject getManagedProject() {
      return ProjectUtils.getManagedProject(this.m_Transformation);
   }

   private EntitySettings lookupTransformationSettings() {
      ManagedProject var1 = this.getManagedProject();
      return var1.getEntityResources().getSettings();
   }

   private void applyPreferredTextSize() {
      Persistor var1 = this.m_transformationSettings.getClassSettings(EntityType.Transformation);
      Integer var2 = var1.getInteger("MappingTree.Zoom");
      if (var2 == null) {
         var2 = 11;
         var1.putInteger("MappingTree.Zoom", var2);
      }

      if (!var2.equals(m_textSize)) {
         updateFonts(var2);
      }
   }

   private void loadStructures(boolean var1, RefreshStructuresCallback var2) {
      this.m_targetTreeComponent.m_deHashtable.clear();
      this.m_sourceTreeComponent.m_deHashtable.clear();
      TreeMapper.StructureLoader var3 = new TreeMapper.StructureLoader(var2);
      var3.load(var1);
   }

   public void prepareForRemoval() {
      this.cancelStructureLoadingJob();
      this.saveDirtyTreeStructures();
   }

   private void cancelStructureLoadingJob() {
      if (this.loadStructuresJob != null && this.loadStructuresJob.isRunning()) {
         ApplicationUi.getJobManager().cancelJob(this.loadStructuresJob);
      }
   }

   public void saveDirtyTreeStructures() {
      UiThreadSupplier.call(new Supplier<Void>() {
         public Void get() {
            TreeMapper.this.m_sourceTreeComponent.writeToCache(TreeMapper.this.m_structureCache);
            TreeMapper.this.m_targetTreeComponent.writeToCache(TreeMapper.this.m_structureCache);
            return null;
         }
      });
   }

   private void displayLoadedStructures(
      final Optional<MappingTreeStructure> var1, final Optional<MappingTreeStructure> var2, final RefreshStructuresCallback var3
   ) {
      Runnable var4 = new Runnable() {
         @Override
         public void run() {
            TreeMapper.this.generateTreeTitles();
            List var1x = TreeMapper.this.m_mappingsHaveBeenLoaded ? TreeMapper.this.getCurrentMappings() : TreeMapper.this.m_Transformation.getMapList();
            if (var1.isPresent()) {
               TreeMapper.this.m_sourceTreeComponent.m_deHashtable.clear();
               TreeMapper.this.m_sourceTreeComponent.readMappingStructure((MappingTreeStructure)var1.get(), TreeMapper.this.m_sourceTitle);
               TreeMapper.this.getTreeMapperContainer()
                  .getFormulaDisplayer()
                  .refreshSourceTree((MappingTreeStructure)var1.get(), TreeMapper.this.m_sourceTitle);
            }

            if (var2.isPresent()) {
               TreeMapper.this.m_targetTreeComponent.m_deHashtable.clear();
               TreeMapper.this.m_targetTreeComponent.readMappingStructure((MappingTreeStructure)var2.get(), TreeMapper.this.m_targetTitle);
            }

            TreeMapper.this.m_transform = new Transform();
            TreeMapper.this.m_transform.setSourceDataElementContainer(new SourceTreeDataElementContainer(TreeMapper.this.m_sourceTreeComponent));

            try {
               TreeMapper.this.setMapList(var1x, false);
            } catch (TreeMapperException var3x) {
               var3.getMappingErrorHandler().caught(var3x);
            }

            TreeMapper.this.updateHiddenNodesOnMapChange();
            TreeMapper.this.m_sourceTreeComponent.refreshBatchNode();
            TreeMapper.this.m_sourceTreeComponent.refresh();
            TreeMapper.this.m_targetTreeComponent.refresh();
            var3.done();
         }
      };
      EventQueue.invokeLater(var4);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TreeMapper.this.updateRenamedTargetNodes();
            TreeMapper.this.checkSimpleSfdcRepresentation();
            TreeMapper.this.m_targetTreeComponent.repaint();
         }
      });
   }

   private void updateRenamedTargetNodes() {
      for (Entry var2 : this.renamedTargetNodes.entrySet()) {
         String var3 = (String)var2.getKey();
         Node var4 = this.m_targetTreeComponent.findNode_expandTreeIfNotExist(var3);
         if (var4 == null) {
            var4 = this.m_targetTreeComponent.findNode(var3);
         }

         if (var4 != null) {
            NodeNameUtils.setUserDefinedName(this.m_targetTreeComponent, var4, (String)var2.getValue());
         }
      }
   }

   public void setMapList(List<String> var1, boolean var2) throws TreeMapperException {
      try {
         this.mappingManager.reset(var1);
      } finally {
         this.m_mappingsHaveBeenLoaded = true;
         this.updateMappingFlag();
         this.m_container.treeMapperLoaded();
         if (var2) {
            this.notifyContainerOfChange(true);
         }
      }
   }

   public void updateHiddenNodesOnMapChange() {
      updateHiddenNodesOnMapChange(this.m_sourceTreeComponent);
      updateHiddenNodesOnMapChange(this.m_targetTreeComponent);
   }

   private static void updateHiddenNodesOnMapChange(TreeComponent var0) {
      if (var0.isShowMappedNodesOnly()) {
         var0.updateHiddenNodes();
      }
   }

   public void initComponentSize() {
      if (!this.bSkipPainting) {
         this.initComponentSize(this.getSize().width, this.getSize().height);
      }
   }

   private void initComponentSize(int var1, int var2) {
      this.m_appletWidth = var1;
      this.m_appletHeight = var2;
      if (this.m_sourceScrollWidth < 85) {
         this.m_sourceScrollWidth = 85;
      }

      if (this.m_bDisplayMapArea) {
         this.m_mapAreaWidth = (int)(this.m_appletWidth * this.m_mapAreaPart / 100.0);
         this.m_sourceScrollWidth = (int)(this.m_appletWidth * this.m_sourceScrollPart / 100.0);
         this.m_targetScrollWidth = (int)(this.m_appletWidth * this.m_targetScrollPart / 100.0);
      } else {
         this.m_mapAreaWidth = 0;
         this.m_sourceScrollWidth = (int)(this.m_appletWidth * (this.m_sourceScrollPart + this.m_mapAreaPart / 2) / 100.0);
         this.m_targetScrollWidth = (int)(this.m_appletWidth * (this.m_targetScrollPart + this.m_mapAreaPart / 2) / 100.0);
      }

      if (this.m_targetScrollWidth < 85) {
         this.m_targetScrollWidth = 85;
      }

      this.m_treePanelHeight = this.m_appletHeight - ButtonPanel.g_buttonPanelHeight;
      if (this.m_treePanelHeight < 150) {
         this.m_treePanelHeight = 150;
      }

      this.adjustComponentSize();
   }

   private void adjustComponentSize() {
      this.m_scrollHeights = this.m_treePanelHeight - 6;
      this.m_treePanel.setSize(this.m_appletWidth, this.m_treePanelHeight);
      this.m_treePanel.setLocation(0, 0);
      if (this.m_buttonPanel != null) {
         this.m_buttonPanel.setSize(this.m_appletWidth, ButtonPanel.g_buttonPanelHeight);
         this.m_buttonPanel.setLocation(0, this.m_treePanelHeight);
         this.m_buttonPanel.adjustComponentSize(this.m_appletWidth);
      }

      this.m_sourceScroll.setBounds(3, 3, this.m_sourceScrollWidth - 6, this.m_scrollHeights);
      if (this.m_bDisplayMapArea) {
         this.m_mapArea.setVisible(true);
         this.m_mapArea.setBounds(this.m_sourceScrollWidth + 1, 3, this.m_mapAreaWidth, this.m_scrollHeights);
      } else {
         this.m_mapArea.setVisible(false);
      }

      this.m_targetScroll.setBounds(3 + this.m_sourceScrollWidth + this.m_mapAreaWidth, 3, this.m_targetScrollWidth - 6, this.m_scrollHeights);
      if (this.m_appletWidth > 200) {
         if (this.m_bDisplayMapArea) {
            this.m_sourceScrollPart = (int)(100.0 * this.m_sourceScrollWidth / this.m_appletWidth + 0.5);
            this.m_targetScrollPart = (int)(100.0 * this.m_targetScrollWidth / this.m_appletWidth + 0.5);
         } else {
            this.m_sourceScrollPart = (int)((100.0 - this.m_mapAreaPart) * this.m_sourceScrollWidth / this.m_appletWidth + 0.5);
            this.m_targetScrollPart = (int)((100.0 - this.m_mapAreaPart) * this.m_targetScrollWidth / this.m_appletWidth + 0.5);
         }
      } else {
         this.m_sourceScrollPart = 40;
         this.m_mapAreaPart = 20;
         this.m_targetScrollPart = 40;
      }

      this.m_sourceScrollPart = Intervals.valueWithin(this.m_sourceScrollPart, 20, 60);
      this.m_targetScrollPart = Intervals.valueWithin(this.m_targetScrollPart, 20, 60);
      this.m_mapAreaPart = 100 - this.m_sourceScrollPart - this.m_targetScrollPart;
      this.validate();
   }

   private void start() {
      this.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent var1) {
            TreeMapper.this.initComponentSize();
         }
      });
      TreeMapper.ResizeHandler var1 = new TreeMapper.ResizeHandler();
      this.m_treePanel.addMouseListener(var1);
      this.m_treePanel.addMouseMotionListener(var1);
      this.add(this.m_treePanel);
      if (this.m_buttonPanel != null) {
         this.add(this.m_buttonPanel);
         this.m_buttonPanel.addMouseListener(var1);
         this.m_buttonPanel.addMouseMotionListener(var1);
      }

      this.m_treePanel.add(this.m_sourceScroll);
      this.m_treePanel.add(this.m_targetScroll);
      this.setColors();
   }

   public boolean checkIfFormula() {
      if (this.m_stackVector == null) {
         return false;
      }

      int var1 = this.m_stackVector.size();
      if (var1 == 0) {
         return false;
      }

      int var2 = this.m_stackVector.get(0).m_id;
      return var1 != 1 || var2 != 401 && var2 != 402 && var2 != 403 && var2 != 404 && var2 != 405 && var2 != 406;
   }

   private void setColors() {
      this.m_treePanel.setBackground(m_clrLightBgd);
      if (this.m_buttonPanel != null) {
         this.m_buttonPanel.setBackground(m_clrLightBgd);
      }

      this.m_sourceScroll.setBackground(m_clrBgdTree);
      this.m_targetScroll.setBackground(m_clrBgdTree);
      this.setBackground(m_clrLightBgd);
   }

   private int getRegion(MouseEvent var1) {
      byte var2 = 0;
      int var3 = var1.getX();
      int var4 = var1.getY();
      Object var5 = var1.getSource();
      if (var5 == this.m_treePanel) {
         if (var4 <= 4) {
            var2 = 0;
         } else if (var4 <= this.m_scrollHeights + 4) {
            if (var3 < 3) {
               var2 = 0;
            } else if (var3 <= this.m_sourceScrollWidth - 2) {
               var2 = 1;
            } else if (var3 <= this.m_sourceScrollWidth + 2) {
               var2 = 2;
            } else if (this.m_bDisplayMapArea && var3 <= this.m_sourceScrollWidth + this.m_mapAreaWidth - 2) {
               var2 = 3;
            } else if (this.m_bDisplayMapArea && var3 <= this.m_sourceScrollWidth + this.m_mapAreaWidth + 2) {
               var2 = 4;
            } else if (var3 <= this.getSize().width - 3) {
               var2 = 5;
            } else {
               var2 = 0;
            }
         }
      } else if (var5 == this.m_buttonPanel) {
         if (var4 <= 4) {
            var2 = 1;
         } else if (var4 <= ButtonPanel.g_buttonPanelHeight - 4) {
            var2 = 0;
         }

         var2 += 10;
      }

      return var2;
   }

   void addOption(Node var1) {
      if (var1.isTypeDefinition() && var1.m_parent.isDerivedTypeHolder()
         || var1.isSubstitutionGroup()
         || var1.isOptional()
         || var1.isLoopNode()
         || var1.isChildOfOneOfNode()
         || var1.isFolder() && !this.isTargetXml()) {
         TLNOptionViewer var2 = new TLNOptionViewer(this.m_targetTreeComponent, var1);
         var2.open();
      }
   }

   public void doMap(Node var1, boolean var2) {
      this.doMap(var1, var2, null);
   }

   public void doMap(Node var1, boolean var2, TestResult var3) {
      if (this.isValidTargetNodeForMapping(var1)) {
         this.DoMap_(var1, var2, var3);
         this.m_sourceTreeComponent.setHighlightedNode(null);
         this.m_sourceTreeComponent.repaint();
      }
   }

   private boolean isValidTargetNodeForMapping(Node var1) {
      MappableStatus var2 = MappableStatus.of(var1);
      if (var2.isMappable()) {
         return true;
      }

      String var3 = var2.explainWhyNotMappable();
      NonBlockingAlert.error(this, var3, "Illegal Mapping");
      return false;
   }

   private void DoMap_(Node var1, boolean var2, TestResult var3) {
      try {
         String var4 = var1.m_deName;
         if (var4 == null || var4.length() == 0) {
            Alert.error(this, "The selected target node cannot be mapped to.", "Illegal Mapping");
            return;
         }

         String var5 = "";
         NodeMapping var6 = this.mappingManager.findMapping(var4);
         if (var6 != null) {
            var5 = var6.getExpression();
         }

         if (this.m_sourceTreeComponent.getHighlightedNode() != null && var5.isEmpty()) {
            Node var7 = this.m_sourceTreeComponent.getHighlightedNode();
            if (!this.isValidSourceNodeForMapping(var1, var7)) {
               return;
            }

            if (var4.length() == 0) {
               Alert.error(this, "This target data element cannot be mapped.", "Illegal Mapping");
               return;
            }

            if (var7.isFolder()) {
               this.showFormula("", var4, var6, var3);
               return;
            }

            NodeMapping var8 = this.addMappingImpl(var7, var1);
            if (var2) {
               this.showFormula(var5, var4, var8, var3);
            } else {
               this.m_sourceTreeComponent.repaint(0, this.m_sourceTreeComponent.m_ymin, this.m_sourceTreeComponent.m_xmax, this.m_sourceTreeComponent.m_ymax);
            }
         } else {
            this.showFormula(var5, var4, var6, var3);
         }
      } catch (Exception var9) {
         ErrorLog.log(TreeMapper.class, var9.getMessage(), var9);
      }
   }

   private NodeMapping addMappingImpl(Node var1, Node var2) {
      String var3 = this.createSourceExpressionForNodeToNodeMapping(var1, var2);
      NodeMapping var4 = this.mappingFactory.newMapping(var2.m_deName, var3);
      this.m_stackVector = this.m_transform.getRpnTokenVector(var3);
      this.mappingManager.replaceMapping(var3, var2.m_deName);
      this.updateMappingFlag();
      this.updateMapAreaTables(var2.m_deName, var3);
      this.mappingManager.changeHighLighted(var4);
      this.setDirty(true, true);
      return var4;
   }

   private String createSourceExpressionForNodeToNodeMapping(Node var1, Node var2) {
      return this.straightMappingFactory.getSourceExpression(var1, var2);
   }

   private boolean isValidSourceNodeForMapping(Node var1, Node var2) {
      if (var2.isUndefinedDataType()) {
         NonBlockingAlert.error(this, "The data type for the source data element is undefined. The mapping cannot be done.", "Illegal Mapping");
         return false;
      } else {
         return true;
      }
   }

   public void addMapping(Node var1, Node var2) {
      if (this.isValidSourceNodeForMapping(var2, var1)) {
         if (this.isValidTargetNodeForMapping(var2)) {
            String var3 = var2.m_deName;
            NodeMapping var4 = this.mappingManager.findMapping(var3);
            if (var4 != null) {
               String var5 = var4.getExpression();
               this.showFormula(var5, var3, var4, null);
            } else {
               this.addMappingImpl(var1, var2);
            }
         }
      }
   }

   public void saveMaps() {
      Node var1 = this.m_targetTreeComponent.m_root;
      if (this.getCurrentMappings().size() > 0) {
         this.saveGeneratorMappings(var1);
      }

      this.m_Transformation.setMapList(this.getCurrentMappings());
      this.m_Transformation.setInvalidMappings(Arrays.asList(this.mappingManager.getInvalidMappings()));
      this.m_Transformation.setRenamedNodes(this.renamedTargetNodes);
      this.saveDirtyTreeStructures();
   }

   private void saveGeneratorMappings(Node var1) {
      if (var1.isRootLoopNode()) {
         try {
            if (!var1.isRoot()) {
               String var2 = var1.m_mappingKits.getGeneratorString();
               NodeMapping var3 = this.getNodeMappingFactory().newMapping(var1.m_deName, var2);
               this.getMappingManager().addMapping(var3);
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      for (Node var6 : var1.getChildren()) {
         if (var6.isFolder()) {
            this.saveGeneratorMappings(var6);
         }
      }
   }

   public List<String> getCurrentMappings() {
      LinkedHashSet var1 = Sets.newLinkedHashSet();

      for (NodeMapping var3 : this.mappingManager.getMappings()) {
         var1.add(MapStringFormat.format(var3));
      }

      return Lists.newArrayList(var1);
   }

   public Node findSourceNode(String var1) {
      return this.m_sourceTreeComponent.m_deHashtable.get(ScriptUtils.stripInstanceTag(var1));
   }

   public String verifyExpression(String var1) {
      String var2 = "";
      if (this.m_transform == null) {
         this.m_transform = new Transform();
      }

      this.m_transform.setSourceDataElementContainer(new SourceTreeDataElementContainer(this.m_sourceTreeComponent));
      this.m_stackVector = this.m_transform.getRpnTokenVector(var1);
      if (this.m_stackVector == null) {
         if (this.m_transform.m_err.getLineNumber() > 0) {
            var2 = "Error at line " + this.m_transform.m_err.getLineNumber();
         } else {
            var2 = "Syntax error";
         }

         return var2 + ": " + this.m_transform.m_err.getErrorMessage();
      } else {
         this.m_deVector = Transform.getDEVector(this.m_stackVector);
         int var4 = 0;
         int var6 = this.m_deVector.size();

         for (int var5 = 0; var5 < var6; var5++) {
            String var3;
            if (this.m_sourceTreeComponent.m_deHashtable.get(var3 = ScriptUtils.stripInstanceTag(this.m_deVector.get(var5))) == null
               && this.m_sourceTreeComponent.findNode_expandTreeIfNotExist(var3) == null
               && var2.indexOf(";" + var3 + ";") < 0
               && !var2.startsWith(var3 + ";")) {
               var2 = var2 + var3;
               var2 = var2 + ";";
               var4++;
            }
         }

         if (var4 == 1) {
            var2 = "Undefined data element: " + var2;
         } else if (var4 > 1) {
            var2 = "Undefined data elements: " + var2;
         }

         return var2;
      }
   }

   private void showFormula(final String var1, final String var2, final NodeMapping var3, final TestResult var4) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TreeMapper.this.m_container.getFormulaDisplayer().showMapping(TreeMapper.this, var2, var3, var1, var4);
         }
      });
   }

   public Node findTargetNode(NodePath var1) {
      return this.findTargetNode(var1.toString());
   }

   public Node findTargetNode(String var1) {
      return this.m_targetTreeComponent.m_deHashtable.get(var1);
   }

   public void removeTargetNode(Node var1) {
      this.m_targetTreeComponent.m_deHashtable.remove(var1.m_deName);
      if (this.renamedTargetNodes.remove(var1.m_deName) != null) {
         this.setDirty(true, false);
      }

      if (var1.isRootLoopNode()) {
         this.m_generatorMap.remove(var1);
         this.m_conflictMap.remove(var1);
      }
   }

   public void setDirty(boolean var1) {
      this.setDirty(var1, var1);
   }

   public void setDirty(boolean var1, boolean var2) {
      if (var1) {
         this.notifyContainerOfChange(var2);
      }
   }

   public void startBulkChange() {
      this.m_container.startBulkChange();
   }

   public void stopBulkChange() {
      this.m_container.stopBulkChange();
   }

   private void notifyContainerOfChange(boolean var1) {
      this.m_container.notifyOfChange(var1);
   }

   @Override
   public void paint(Graphics var1) {
      if (!this.bSkipPainting) {
         super.paint(var1);
      }
   }

   @Override
   public void repaint() {
      if (!this.bSkipPainting) {
         super.repaint();
      }
   }

   public void startPrinting() {
      this.bPrintingInitialized = false;
      this.sourceScrollValue = this.m_sourceScroll.getVerticalScrollBar().getValue();
      this.targetScrollValue = this.m_targetScroll.getVerticalScrollBar().getValue();
   }

   public void finishPrinting() {
      RepaintManager var1 = RepaintManager.currentManager(this.m_treePanel);
      var1.setDoubleBufferingEnabled(true);
      this.bSkipPainting = false;
      this.initComponentSize();
      this.adjustComponentSize();
      this.m_sourceScroll.getVerticalScrollBar().setValue(this.sourceScrollValue);
      this.m_targetScroll.getVerticalScrollBar().setValue(this.targetScrollValue);
   }

   public void migration(final Transformation var1) {
      (new BusyWorker(this.m_container.getWaitService()) {
         @Override
         protected void doWork() {
            try {
               TreeMapper.this.m_Migration = new Migration(TreeMapper.this, var1);
               TreeMapper.this.m_Migration.setSize(new Dimension(900, 800));
               TreeMapper.this.m_Migration.init();
            } catch (MigrationInitializationException var2) {
               ErrorLog.attention(TreeMapper.class, "Failed to start the migration tool.", var2);
            }
         }
      }).run();
   }

   public void closeTreeMapperMigrationFrame() {
      this.m_Migration.dispose();
      this.m_Migration = null;
   }

   @Override
   public Printable getPrintable() {
      return new Printable() {
         @Override
         public int print(Graphics var1, PageFormat var2, int var3) throws PrinterException {
            int var4 = Math.max(
                  TreeMapper.this.m_sourceTreeComponent.getPreferredSize().height, TreeMapper.this.m_targetTreeComponent.getPreferredSize().height
               )
               + 100;
            if (var3 * var2.getImageableHeight() > var4) {
               return 1;
            }

            Graphics2D var5 = (Graphics2D)var1;
            UiUtils.makeAntiAliased(var5);
            var5.translate(var2.getImageableX(), var2.getImageableY() - var3 * var2.getImageableHeight());
            RepaintManager var6 = RepaintManager.currentManager(TreeMapper.this.m_treePanel);
            var6.setDoubleBufferingEnabled(false);
            if (!TreeMapper.this.bPrintingInitialized) {
               TreeMapper.this.initComponentSize((int)var2.getImageableWidth(), var4);
               TreeMapper.this.bPrintingInitialized = true;
            }

            TreeMapper.this.bSkipPainting = false;
            TreeMapper.this.m_treePanel.paint(var5);
            TreeMapper.this.bSkipPainting = true;
            return 0;
         }
      };
   }

   public void startWait() {
      Runnable var1 = new Runnable() {
         @Override
         public void run() {
            TreeMapper.this.m_container.startWait();
            Cursor var1x = Cursor.getPredefinedCursor(3);
            TreeMapper.this.setCursor(var1x);
            TreeMapper.this.m_sourceTreeComponent.setCursor(var1x);
            TreeMapper.this.m_targetTreeComponent.setCursor(var1x);
         }
      };
      UiUtils.runOnEventThread(var1);
   }

   public void stopWait() {
      Runnable var1 = new Runnable() {
         @Override
         public void run() {
            TreeMapper.this.m_container.stopWait();
            Cursor var1x = Cursor.getDefaultCursor();
            TreeMapper.this.setCursor(var1x);
            TreeMapper.this.m_sourceTreeComponent.setCursor(var1x);
            TreeMapper.this.m_targetTreeComponent.setCursor(var1x);
         }
      };
      UiUtils.runOnEventThread(var1);
   }

   public TreeMapperContainer getTreeMapperContainer() {
      return this.m_container;
   }

   public MappingStructureFactory getMappingStructureFactory() {
      return this.m_container.getMappingStructureFactory();
   }

   public void lookForMissingRequiredMappingsAndMappingConflicts() throws CheckingException {
      if (this.m_targetTreeComponent.m_root.missingRequiredMappings()) {
         throw new CheckingException("Missing some required mappings");
      }

      ArrayList var1 = Lists.newArrayList();
      this.m_targetTreeComponent.m_root.collectSourcePathConflict(var1);

      for (Object __etlItem1173 : var1) {
         MappingConflictChecker var3 = (MappingConflictChecker) __etlItem1173;
         var3.checkNotConflict();
      }
   }

   public Future<?> doStraightMappings(Node var1, Node var2) {
      var1.getTreeComponent().setCursor(Cursor.getPredefinedCursor(3));
      var2.getTreeComponent().setCursor(Cursor.getPredefinedCursor(3));
      StraightMappingJob var3 = new StraightMappingJob(this, var1, var2);
      var3.setUseProgressDialog(250);
      return var3.submit();
   }

   boolean addMapPair(Node var1, Node var2) {
      if (var2.isLinkKey()) {
         return false;
      }

      if (var2.isUndefinedDataType()) {
         return false;
      }

      if (var2.isPopulatedAutomatically()) {
         return false;
      }

      String var3 = var2.m_deName;
      if (var3.length() == 0) {
         return false;
      }

      String var4 = this.createSourceExpressionForNodeToNodeMapping(var1, var2);
      NodeMapping var5 = this.mappingFactory.newMapping(var3, var4);
      this.m_stackVector = this.m_transform.getRpnTokenVector(var4);
      this.mappingManager.addMapping(var5);
      this.updateMapAreaTables(var3, var4);
      return true;
   }

   public TreeMapperNode getSourceRoot() {
      return this.m_sourceTreeComponent.asTreeMapperNode();
   }

   public TreeMapperNode getTargetRoot() {
      return this.m_targetTreeComponent.asTreeMapperNode();
   }

   public void setNodeExpansionMode(NodeExpansionMode var1) {
      if (var1 != this.m_nodeExpansionMode) {
         Preconditions.checkNotNull(var1, "mode");
         this.m_nodeExpansionMode = var1;
         this.m_sourceTreeComponent.xsdExpandMinimum = var1 == NodeExpansionMode.MINIMAL;
         this.m_targetTreeComponent.xsdExpandMinimum = var1 == NodeExpansionMode.MINIMAL;
         Persistor var2 = this.m_transformationSettings.getEntitySettings(this.m_Transformation);
         var2.putString("TreeMapper.NodeExpansionMode", var1.name());
      }
   }

   public NodeExpansionMode getNodeExpansionMode() {
      return this.m_nodeExpansionMode;
   }

   private NodeExpansionMode getInitialExpansionMode() {
      Persistor var1 = this.m_transformationSettings.getEntitySettings(this.m_Transformation);
      String var2 = var1.getString("TreeMapper.NodeExpansionMode");
      if (var2 != null) {
         try {
            return NodeExpansionMode.valueOf(var2);
         } catch (RuntimeException var4) {
         }
      }

      return NodeExpansionMode.MINIMAL;
   }

   public boolean isTargetXml() {
      return isXmlType(this.m_Transformation.getOutputStructure());
   }

   private static boolean isXmlType(DataStructure var0) {
      return var0 != null ? var0.getStructureType().isXmlBased() : false;
   }

   public boolean deleteMapping(NodePath var1) {
      if (this.mappingManager.deleteMapping(var1)) {
         this.m_mapArea.repaint();
         return true;
      } else {
         return false;
      }
   }

   public void deleteAllMappings() {
      this.deleteAllMappings(this.m_targetTreeComponent.m_root);
   }

   public void deleteAllMappings(Node var1) {
      List var2 = var1.collectAllTargetPaths();
      if (this.mappingManager.deleteMappings(var2)) {
         this.m_mapArea.repaint();
      }
   }

   public void deleteMappings(Collection<? extends Mapping> var1) {
      List var2 = KongaListUtils.transform(var1, MappingFunctions.TO_TARGET_PATH);
      if (this.mappingManager.deleteMappings(var2)) {
         this.m_mapArea.repaint();
      }
   }

   public void selectSourceNode(Node var1, boolean var2) {
      Node var3 = var1 != null && var1.isSourceUsed() ? var1 : null;
      this.m_targetTreeComponent.updateUnderlineNodes(var3);
      int var4 = this.m_targetTreeComponent.m_selectedNodes.size();
      if (var3 != null && var4 > 0 && this.m_bMappingMode == var2) {
         if (var3 == this.source_saved) {
            this.iTarget_saved++;
         } else {
            this.iTarget_saved = 0;
         }

         this.iTarget_saved %= var4;
         this.source_saved = var3;
         Node var5 = this.m_targetTreeComponent.m_selectedNodes.get(this.iTarget_saved);
         this.m_targetTreeComponent.showNode(var5, false);
      }

      this.m_sourceTreeComponent.showNode(var1, true);
   }

   public void selectTargetNode(Node var1, boolean var2) {
      NodeMapping var3 = this.mappingManager.showMapping(var1 != null && var1.isTargetMapped() ? var1.m_deName : null);
      this.m_sourceTreeComponent.updateUnderlineNodes(var1);
      int var4 = this.m_sourceTreeComponent.m_selectedNodes.size();
      if (var4 > 0 && this.m_bMappingMode == var2) {
         if (var3 == this.mp_saved) {
            this.iSource_saved++;
         } else {
            this.iSource_saved = 0;
         }

         this.iSource_saved %= var4;
         this.mp_saved = var3;
         Node var5 = this.m_sourceTreeComponent.m_selectedNodes.get(this.iSource_saved);
         this.m_sourceTreeComponent.showNode(var5, false);
      }

      this.m_targetTreeComponent.showNode(var1, true);
   }

   public void showFormulaBuilder(Node var1) {
      this.showFormulaBuilder(var1, null);
   }

   public void showFormulaBuilder(Node var1, TestResult var2) {
      this.m_container.getFormulaDisplayer().showTestResult(this, var1, var2);
   }

   public NodeMappingFactory getNodeMappingFactory() {
      return this.mappingFactory;
   }

   public void updateMappingFlag() {
      this.m_targetTreeComponent.m_root.clearRequireMappingFlag();

      for (NodeMapping var2 : this.mappingManager.getMappings()) {
         Node var3 = this.m_targetTreeComponent.findNode(var2.getTargetPath());
         if (var3 != null && !var3.isFolder()) {
            var3.setRequireMappingFolders();
            var3.setMapped();
         }
      }

      this.m_targetTreeComponent.setLinkedColumnFlag("TreeMapper.updateMappingFlag");
      this.m_targetTreeComponent.m_root.updateRequireMappingFlag(new HashSet<>());
      this.m_targetTreeComponent.m_root.clearMappedHiddenTypes();
      this.m_sourceTreeComponent.m_root.clearMappedHiddenTypes();
   }

   public MappingManager getMappingManager() {
      return this.mappingManager;
   }

   public boolean isMapped(String var1) {
      return this.mappingManager.findMapping(var1) != null;
   }

   public void searchAndReplace() {
      if (this.searchDialog != null) {
         this.searchDialog.toFront();
      } else {
         if (this.searchUi == null) {
            this.searchUi = new TreeMapperSearchUi(this);
         } else {
            this.searchUi.reset();
         }

         JComponent var1 = WizardStyleDialog.wrapUi(this.searchUi);
         this.searchDialog = new WizardStyleDialog(this.getOwner(), "Search & Replace", var1, KongaDialog.CLOSE_ONLY, false) {
            @Override
            protected void onDialogClose() {
               TreeMapper.this.searchDialog = null;
               super.onDialogClose();
            }
         };
         this.searchDialog.setFocusedComponent(this.searchUi);
         this.searchDialog.manageLocation(TreeMapperSearchUi.class);
         this.searchDialog.setVisible(true);
      }
   }

   public void targetNodeWasRenamed(Node var1, String var2) {
      String var3 = var1.m_deName;
      String var4 = this.renamedTargetNodes.get(var3);
      if (var2 != null) {
         this.renamedTargetNodes.put(var3, var2);
      } else {
         this.renamedTargetNodes.remove(var3);
      }

      if (!equals(var4, var2)) {
         this.setDirty(true, true);
      }
   }

   public Map<String, String> getRenamedTargetNodes() {
      return Maps.newHashMap(this.renamedTargetNodes);
   }

   public void setRenamedTargetNodes(Map<String, String> var1) {
      this.renamedTargetNodes = Maps.newHashMap(var1);
      this.updateRenamedTargetNodes();
      this.m_targetTreeComponent.repaint();
   }

   @Override
   public void dispose() {
      this.m_container.dispose();
      this.m_Transformation.removePropertyChangeListener("MappingStructures", this.structureUpdater);
      if (this.searchUi != null) {
         this.searchUi.dispose();
      }

      this.saveNodeExpansionState();
      this.saveMapDisplaySettings();
      this.saveSimpleNodeDisplaySettings();
      this.saveSimpleXmlElementDisplaySettings();
      this.saveShowMappedNodesOnlySettings();
   }

   private void saveNodeExpansionState() {
      NodeExpansionMemento var1 = new NodeExpansionMemento(this);
      var1.persist(this.m_transformationSettings.getEntitySettings(this.m_Transformation));
   }

   private void saveMapDisplaySettings() {
      Persistor var1 = this.m_transformationSettings.getEntitySettings(this.m_Transformation);
      var1.removeChildren("MapAreaDisplay");
      Persistor var2 = var1.createChild("MapAreaDisplay");

      for (MapAreaDisplayOption var4 : this.mapAreaDisplay) {
         var2.putBoolean(var4.name(), Boolean.TRUE);
      }
   }

   private void saveSimpleNodeDisplaySettings() {
      BooleanSourceAndTargetSetting var1 = new BooleanSourceAndTargetSetting(
         "SimpleNodeDisplay", false, this.m_transformationSettings.getEntitySettings(this.m_Transformation)
      );
      var1.save(this.m_sourceTreeComponent.isSimpleNodeDisplay(), this.m_targetTreeComponent.isSimpleNodeDisplay());
   }

   private void saveSimpleXmlElementDisplaySettings() {
      BooleanSourceAndTargetSetting var1 = new BooleanSourceAndTargetSetting(
         "SimpleXmlElementDisplay", true, this.m_transformationSettings.getEntitySettings(this.m_Transformation)
      );
      var1.save(this.m_sourceTreeComponent.isSimpleXmlElementDisplay(), this.m_targetTreeComponent.isSimpleXmlElementDisplay());
   }

   private void saveShowMappedNodesOnlySettings() {
      BooleanSourceAndTargetSetting var1 = new BooleanSourceAndTargetSetting(
         "ShowMappedNodesOnly", false, this.m_transformationSettings.getEntitySettings(this.m_Transformation)
      );
      var1.save(this.m_sourceTreeComponent.isShowMappedNodesOnly(), this.m_targetTreeComponent.isShowMappedNodesOnly());
   }

   private BooleanSourceAndTargetSetting restoreSimpleNodeDisplaySettings() {
      return this.restoreBooleanSetting("SimpleNodeDisplay", ClientInfo.isDataLoader());
   }

   private BooleanSourceAndTargetSetting restoreSimpleXmlElementDisplaySettings() {
      return this.restoreBooleanSetting("SimpleXmlElementDisplay", true);
   }

   private BooleanSourceAndTargetSetting restoreBooleanSetting(String var1, boolean var2) {
      return new BooleanSourceAndTargetSetting(var1, var2, this.m_transformationSettings.getEntitySettings(this.m_Transformation));
   }

   private BooleanSourceAndTargetSetting restoreShowMappedNodesOnlySettings() {
      return this.restoreBooleanSetting("ShowMappedNodesOnly", false);
   }

   private EnumSet<MapAreaDisplayOption> restoreMapDisplaySettings() {
      EnumSet var1 = EnumSet.noneOf(MapAreaDisplayOption.class);
      Persistor var2 = this.m_transformationSettings.getEntitySettings(this.m_Transformation).getFirstChild("MapAreaDisplay");
      if (var2 != null) {
         for (MapAreaDisplayOption var6 : MapAreaDisplayOption.values()) {
            if (Boolean.TRUE.equals(var2.getBoolean(var6.name()))) {
               var1.add(var6);
            }
         }
      } else {
         if (DrawAttributeMappingsPreference.value()) {
            var1.add(MapAreaDisplayOption.ATTRIBUTES);
         }

         if (DrawGeneratorMappingsPreference.value()) {
            var1.add(MapAreaDisplayOption.GENERATORS);
         }

         if (DrawErrorMappingsPreference.value()) {
            var1.add(MapAreaDisplayOption.ERRORS);
         }
      }

      return var1;
   }

   public void setMapAreayDisplay(EnumSet<MapAreaDisplayOption> var1) {
      if (!this.mapAreaDisplay.equals(var1)) {
         this.mapAreaDisplay = var1.clone();
         if (this.m_bDisplayMapArea != !this.mapAreaDisplay.isEmpty()) {
            this.m_bDisplayMapArea = !this.m_bDisplayMapArea;
            this.initComponentSize();
         }

         try {
            this.refreshMappings();
         } catch (TreeMapperException var3) {
            ErrorLog.log(TreeMapper.class, "Invalid mappings encountered", var3);
         }
      }
   }

   private void refreshMappings() throws TreeMapperException {
      this.mappingManager.reset(this.m_Transformation.getMapList());
   }

   public boolean isMapDisplayFlagOn(MapAreaDisplayOption var1) {
      return this.mapAreaDisplay.contains(var1);
   }

   public void registerKeyboardShortCut(KeyStroke var1, String var2, Action var3) {
      this.m_container.registerKeyboardShortCut(var1, var2, var3);
   }

   public void showPopupMenu(JPopupMenu var1) {
      Dimension var2 = var1.getPreferredSize();
      JPanel var3 = this.m_mapArea.isVisible() ? this.m_mapArea : this;
      int var4 = var3.getWidth() / 2 - var2.width / 2;
      byte var5 = 50;
      var1.show(var3, var4, var5);
   }

   public void showTestResult(TestResult var1) {
      BreakLocation var2 = var1.getCurrentLocation();
      String var3 = var2.getTargetNodePath();
      Node var4 = this.m_targetTreeComponent.findNode_expandTreeIfNotExist(var3);
      if (var4 != null) {
         this.showFormulaBuilder(var4, var1);
      }
   }

   private void installFocusKeyBindings() {
      KeyBindingInstaller var1 = new KeyBindingInstaller(this, 2);
      var1.addBinding(KeyUtils.getAltDown(83), "focus-source", new TreeMapper.FocusTreeComponentAction(this.m_sourceTreeComponent));
      var1.install();
   }

   private static boolean equals(String var0, String var1) {
      return var0 != null ? var0.equals(var1) : var1 == null;
   }

   public boolean isMappingMode() {
      return this.m_bMappingMode;
   }

   public void editBatchNode(String var1) {
      if (this.batchNodeDefiner == null) {
         this.batchNodeDefiner = new TreeMapperSourceBatchNodeDefiner(this);
      }

      this.batchNodeDefiner.editBatchNode(var1);
   }

   public StructureLink getInputLink() {
      return this.inputLink;
   }

   public StructureLink getOutputLink() {
      return this.outputLink;
   }

   public DataTree getSourceDataTree() {
      return this.sourceDataTree;
   }

   public DataTree getTargetDataTree() {
      return this.targetDataTree;
   }

   public boolean isTypeConflict(Node var1) {
      return this.warnings.get(var1) != null;
   }

   public void checkDataTypeWarningForOneToOneMapping(Node var1) throws TreeMapperException {
      assert var1 != null : "checkIfOneToOneMapping() should not be called with target being null.";
      if (!var1.isFolder() && this.m_stackVector != null && this.m_stackVector.size() == 1) {
         Transform.Token var2 = this.m_stackVector.get(0);
         if (var2.m_id == 405) {
            String var3 = var2.m_str;
            Node var4 = this.m_sourceTreeComponent.findNode(var3);
            if (var4 != null && !var4.isFolder()) {
               this.setDataTypeWarning(var1, var4);
               return;
            }
         }
      }

      this.removeDataTypWarning(var1);
   }

   private void setDataTypeWarning(Node var1, Node var2) throws TreeMapperException {
      if (this.hasDataTypeWarning(var1, var2)) {
         this.addDataTypWarning(var1, var2);
      } else {
         this.removeDataTypWarning(var1);
      }
   }

   private boolean hasDataTypeWarning(Node var1, Node var2) {
      assert var1 != null && var2 != null : "should not call checkWarning() if source or target is null.";
      return WarningRules.hasDataTypeWarning(var1, var2);
   }

   private void addDataTypWarning(Node var1, Node var2) {
      assert var1 != null && var2 != null : "should not call addWarning() if source or target is null.";
      this.warnings.put(var1, var2);
   }

   private void removeDataTypWarning(Node var1) {
      assert var1 != null : "should not call removeWarning(target) for target==null.";
      this.warnings.remove(var1);
   }

   public int[] preprocessLinkFile(File var1) {
      TextDocReader var2 = null;
      int[] var3 = new int[]{0, -1, 0, -1, 0};

      try {
         var2 = new TextDocReader(var1, (String)null);
         long var4 = 0L;

         while (var4 < 100L) {
            long var6 = var2.readRecord(var4);
            if (var6 <= 0L) {
               break;
            }

            String var8 = var2.getStr(var4, (int)(var6 - var4));
            if (var8.startsWith("$$$ SkipTargetLine ")) {
               String var9 = var8.substring(19);
               var3[2] = Integer.parseInt(var9);
               var3[4]++;
            } else if (var8.startsWith("$$$ SkipSourceLine ")) {
               String var15 = var8.substring(19);
               var3[0] = Integer.parseInt(var15);
               var3[4]++;
            } else {
               if (!var8.startsWith("$$$ NumberOfSourceRows ")) {
                  break;
               }

               String var16 = var8.substring(23);
               var3[1] = Integer.parseInt(var16);
               var3[4]++;
            }

            var4 = var6;
         }
      } catch (Exception var13) {
         Alert.message(this, "An error occurred while preprocessing the source-target link file.\n" + var13.getMessage(), "Link", Alert.Type.WARNING);
      } finally {
         KongaIoUtils.close(var2);
      }

      return var3;
   }

   private void processLinkFile(File var1, int var2) {
      TextDocReader var3 = null;
      boolean var4 = this.m_targetTreeComponent.m_flat;

      try {
         var3 = new TextDocReader(var1, (String)null);
         JbNode var5 = var3.parse_all(var2, -1);
         List var6 = var5.getChildren();
         boolean var7 = true;

         for (Object __etlItem1682 : var6) {
            JbNode var9 = (JbNode) __etlItem1682;
            if (var7) {
               String var10 = var9.getStr(var3);
               if (var10.startsWith("$$$")) {
                  continue;
               }

               var7 = false;
            }

            List var23 = var3.parser.splitValues(var9, true);
            if (var23 != null && var23.size() >= 4) {
               String var11 = (String)var23.get(0);
               if (var11 == null || var11 == "NULL") {
                  var11 = "";
               }

               String var12 = (String)var23.get(1);
               String var13 = (String)var23.get(2);
               if (var13 == null || var13 == "NULL") {
                  var13 = "";
               }

               String var14 = (String)var23.get(3);
               if (var14 != null && !var14.equals("skip")) {
                  Node var15 = this.m_targetTreeComponent.findNode("[" + var13 + "]");
                  if (var15 == null && var4) {
                     var15 = this.m_targetTreeComponent.m_root.getFirstChildFolder();
                  }

                  if (var15 != null && var15.isLoopNode()) {
                     JbNode var16 = this.m_sourceTreeComponent.findJbNode(var11, var12);
                     JbNode var17 = this.m_targetTreeComponent.findJbNode(var13, var14);
                     if (var17 != null) {
                        var17.m_source = var16;
                     }
                  }
               }
            }
         }

         this.m_targetTreeComponent.m_isDataLinked = true;
         this.m_targetTreeComponent.loadDataTree();
         this.m_sourceTreeComponent.refresh();
      } catch (Exception var21) {
         Alert.message(this, "An error occurred while processing the source-target link file.\n" + var21.getMessage(), "Link", Alert.Type.WARNING);
      } finally {
         KongaIoUtils.close(var3);
      }
   }

   public void openSourceDataFile(File var1, int var2, int var3) {
      this.m_sourceTreeComponent.loadDataFile(var1, null, false, var2, var3);
   }

   public void openTargetDataFile(File var1, File var2, int var3, int var4, int var5) {
      this.m_targetTreeComponent.loadDataFile(var1, var2, true, var3, var4);
      this.processLinkFile(var2, var5);
   }

   public void removeSourceAndTargetData() {
      this.m_sourceTreeComponent.closeDocReader();
      this.removeTargetData();
   }

   public void removeTargetData() {
      this.m_targetTreeComponent.closeDocReader();
   }

   void changeFontSize(int var1) {
      int var2 = m_textSize + var1;
      if (var2 > 24) {
         var2 = 24;
      }

      if (var2 < 7) {
         var2 = 7;
      }

      if (var2 != m_textSize) {
         updateFonts(var2);
         this.m_transformationSettings.getClassSettings(EntityType.Transformation).putInteger("MappingTree.Zoom", m_textSize);
         this.setFont(m_font11Plain);
         this.m_sourceTreeComponent.repaint();
         this.m_targetTreeComponent.repaint();
      }
   }

   private static final class FocusTreeComponentAction extends AbstractAction {
      private final TreeComponent tree;

      public FocusTreeComponentAction(TreeComponent var1) {
         this.tree = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.tree.requestFocusInWindow();
      }
   }

   private static final class LinePanel extends JPanel {
      public LinePanel() {
         this.setBackground(Color.gray);
         this.setVisible(false);
      }

      @Override
      public void setOpaque(boolean var1) {
         super.setOpaque(true);
      }
   }

   private final class MappingTreeUpdater implements PropertyChangeListener {
      private MappingTreeUpdater() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         assert "MappingStructures".equals(var1.getPropertyName());
         final NodeExpansionMemento var2 = new NodeExpansionMemento(TreeMapper.this);
         MappingTreeStructure[] var3 = (MappingTreeStructure[])var1.getNewValue();
         TreeMapper.this.m_mapAreaTable.clear();
         TreeMapper.this.displayLoadedStructures(Optional.fromNullable(var3[0]), Optional.fromNullable(var3[1]), RefreshStructuresCallback.IGNORE);
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               var2.restore();
               TreeMapper.this.checkSimpleSfdcRepresentation();
            }
         });
      }
   }

   private final class ResizeHandler extends MouseInputAdapter {
      private final Cursor m_WMoveCursor = Cursor.getPredefinedCursor(10);
      private final Cursor m_NMoveCursor = Cursor.getPredefinedCursor(8);
      private final Cursor m_DefaultCursor = Cursor.getPredefinedCursor(0);
      private int m_saved;
      private int m_region;
      private int m_regionSaved;
      private int m_xmin;
      private int m_xmax;

      public ResizeHandler() {
      }

      @Override
      public void mousePressed(MouseEvent var1) {
         if (var1.isPopupTrigger()) {
            TreeMapper.this.getParent().dispatchEvent(var1);
         } else {
            switch (this.m_regionSaved = TreeMapper.this.getRegion(var1)) {
               case 2:
               case 4:
                  this.m_saved = var1.getX();
                  if (this.m_regionSaved == 2) {
                     this.m_xmin = 85;
                     if (TreeMapper.this.m_bDisplayMapArea) {
                        this.m_xmax = TreeMapper.this.m_sourceScrollWidth + TreeMapper.this.m_mapAreaWidth - 85;
                     } else {
                        this.m_xmax = TreeMapper.this.m_sourceScrollWidth + TreeMapper.this.m_targetScrollWidth - 85;
                     }
                  } else {
                     this.m_xmin = TreeMapper.this.m_sourceScrollWidth + 85;
                     this.m_xmax = TreeMapper.this.m_sourceScrollWidth + TreeMapper.this.m_mapAreaWidth + TreeMapper.this.m_targetScrollWidth - 85;
                  }

                  TreeMapper.this.m_verticalLine.setVisible(true);
                  break;
               case 11:
                  this.m_saved = var1.getY();
                  TreeMapper.this.m_horizontalLine.setVisible(true);
            }

            this.mouseDragged(var1);
         }
      }

      @Override
      public void mouseDragged(MouseEvent var1) {
         int var2 = var1.getX();
         int var3 = var1.getY();
         this.m_region = TreeMapper.this.getRegion(var1);
         switch (this.m_region) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
               if (var2 >= this.m_xmin && var2 <= this.m_xmax) {
                  TreeMapper.this.m_verticalLine.setBounds(var2, 0, 3, TreeMapper.this.m_scrollHeights);
               }
               break;
            case 10:
            case 11:
               var3 += TreeMapper.this.m_treePanelHeight - 2;
               TreeMapper.this.m_horizontalLine.setBounds(0, var3, TreeMapper.this.m_appletWidth, 3);
            case 6:
            case 7:
            case 8:
            case 9:
            default:
               byte var6 = -1;
               byte var4 = -1;
         }
      }

      @Override
      public void mouseReleased(MouseEvent var1) {
         if (var1.isPopupTrigger()) {
            TreeMapper.this.getParent().dispatchEvent(var1);
         } else {
            int var2 = TreeMapper.this.m_verticalLine.getLocation().x;
            switch (this.m_regionSaved) {
               case 2:
                  this.mouseReleasedInSecondRegion(var2);
                  break;
               case 4:
                  this.mouseReleasedInFourthRegion(var2);
            }

            if (TreeMapper.this.m_verticalLine.isVisible()) {
               TreeMapper.this.m_verticalLine.setVisible(false);
            }

            if (TreeMapper.this.m_horizontalLine.isVisible()) {
               TreeMapper.this.m_horizontalLine.setVisible(false);
            }

            this.m_region = 0;
            this.m_regionSaved = 0;
            if (TreeMapper.this.getCursor().getType() != 0) {
               TreeMapper.this.setCursor(this.m_DefaultCursor);
            }

            if (var1.isPopupTrigger()) {
               TreeMapper.this.getParent().dispatchEvent(var1);
            }
         }
      }

      private void mouseReleasedInSecondRegion(int var1) {
         int var2 = var1 - this.m_saved;
         TreeMapper.this.m_sourceScrollWidth = TreeMapper.this.m_sourceScrollWidth + var2;
         if (TreeMapper.this.m_bDisplayMapArea) {
            TreeMapper.this.m_mapAreaWidth = TreeMapper.this.m_mapAreaWidth - var2;
         } else {
            TreeMapper.this.m_targetScrollWidth = TreeMapper.this.m_targetScrollWidth - var2;
         }

         TreeMapper.this.adjustComponentSize();
         if (TreeMapper.this.m_bDisplayMapArea) {
            TreeMapper.this.m_mapArea.repaint();
         }
      }

      private void mouseReleasedInFourthRegion(int var1) {
         int var2 = var1 - this.m_saved;
         TreeMapper.this.m_mapAreaWidth = TreeMapper.this.m_mapAreaWidth + var2;
         TreeMapper.this.m_targetScrollWidth = TreeMapper.this.m_targetScrollWidth - var2;
         TreeMapper.this.adjustComponentSize();
         TreeMapper.this.m_mapArea.repaint();
      }

      @Override
      public void mouseMoved(MouseEvent var1) {
         int var2 = TreeMapper.this.getRegion(var1);
         if (var2 != this.m_region) {
            this.m_region = var2;
            JPanel var3 = (JPanel)var1.getSource();
            if (var3 == null) {
               return;
            }

            switch (this.m_region) {
               case 2:
               case 4:
                  var3.setCursor(this.m_WMoveCursor);
                  break;
               case 11:
                  var3.setCursor(this.m_NMoveCursor);
                  break;
               default:
                  var3.setCursor(this.m_DefaultCursor);
            }
         }
      }

      @Override
      public void mouseEntered(MouseEvent var1) {
         int var2 = TreeMapper.this.getRegion(var1);
         this.m_region = var2;
         JPanel var3 = (JPanel)var1.getSource();
         if (var3 != null) {
            switch (this.m_region) {
               case 2:
               case 4:
                  var3.setCursor(this.m_WMoveCursor);
                  break;
               case 11:
                  var3.setCursor(this.m_NMoveCursor);
                  break;
               default:
                  var3.setCursor(this.m_DefaultCursor);
            }
         }
      }

      @Override
      public void mouseExited(MouseEvent var1) {
         this.m_region = 0;
         JPanel var2 = (JPanel)var1.getSource();
         if (var2 != null) {
            var2.setCursor(this.m_DefaultCursor);
         }
      }
   }

   private final class StructureLoader implements LoadStructureInvoker {
      private final RefreshStructuresCallback callback;

      public StructureLoader(RefreshStructuresCallback var2) {
         this.callback = var2 != null ? var2 : RefreshStructuresCallback.IGNORE;
      }

      public synchronized void load(boolean var1) {
         LoadStructuresJob var2 = new LoadStructuresJob(this, TreeMapper.this.getMappingStructureFactory(), TreeMapper.this.m_structureCache, var1);
         var2.submit();
         this.jobHasBeenInitialized();
         TreeMapper.this.loadStructuresJob = var2;
      }

      @Override
      public void cancelled() {
         this.callback.cancelled();
      }

      @Override
      public void displayLoadedStructures(MappingTreeStructure var1, MappingTreeStructure var2) {
         TreeMapper.this.displayLoadedStructures(Optional.of(var1), Optional.of(var2), this.callback);
      }

      @Override
      public Transformation getTransformation() {
         return TreeMapper.this.m_Transformation;
      }

      @Override
      public boolean isAskingForInputStructure() {
         return true;
      }

      @Override
      public boolean isAskingForOutputStructure() {
         return true;
      }

      @Override
      public void jobHasBeenInitialized() {
         ActionUtils.setEnabledSafely(TreeMapper.this.autoMapperAction, false);
         TreeMapper.this.startWait();
      }

      @Override
      public void jobHasCompleted() {
         TreeMapper.this.stopWait();
         ActionUtils.setEnabledSafely(TreeMapper.this.autoMapperAction, true);
         TreeMapper.this.m_sourceTreeComponent.refreshBatchNode();
      }
   }

   public final class TreePanel extends JPanel {
      public TreePanel() {
         super(null);
         this.setBackground(StagingPanel.m_clrLightBgd);
      }

      @Override
      public void paintComponent(Graphics var1) {
         super.paintComponent(var1);
         Color var2 = var1.getColor();
         var1.setColor(StyleSheet.GenericEditorPanelClass.getBackground());
         if (TreeMapper.this.m_sourceTreeComponent.m_gotFocus) {
            var1.drawRect(0, 0, TreeMapper.this.m_sourceScrollWidth + 7, TreeMapper.this.m_scrollHeights + 3);
            var1.drawRect(1, 1, TreeMapper.this.m_sourceScrollWidth + 5, TreeMapper.this.m_scrollHeights + 1);
            var1.drawRect(2, 2, TreeMapper.this.m_sourceScrollWidth + 3, TreeMapper.this.m_scrollHeights - 1);
         } else if (TreeMapper.this.m_targetTreeComponent.m_gotFocus) {
            var1.drawRect(TreeMapper.this.m_sourceScrollWidth + 5, 0, TreeMapper.this.m_targetScrollWidth + 7, TreeMapper.this.m_scrollHeights + 3);
            var1.drawRect(TreeMapper.this.m_sourceScrollWidth + 6, 1, TreeMapper.this.m_targetScrollWidth + 5, TreeMapper.this.m_scrollHeights + 1);
            var1.drawRect(TreeMapper.this.m_sourceScrollWidth + 7, 2, TreeMapper.this.m_targetScrollWidth + 3, TreeMapper.this.m_scrollHeights - 1);
         }

         var1.setColor(var2);
      }
   }
}
