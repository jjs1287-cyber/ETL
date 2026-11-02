package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.event.MouseInputAdapter;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.CompoundNodeVisibilityFilter;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.NodeExpansionMode;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.NodeVisibilityFilter;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.NodesWithDataVisibiltyFilter;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.PathBasedNodeVisibilityFilter;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.StagingPanel;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperException;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperMode;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperNode;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperSourceTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.json.JsonNodeVisibilityFilter;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.MappingManager;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.NodeMapping;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.migration.Migration;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.TreeStrings;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.search.NodeSearchResult;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.search.NodeSearchUi;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.xml.DerivedTypesSelector;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.xml.SubstitutionGroupsSelector;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.MappingTreeNodePathFilter;
import org.etlforge.integration.data.entity.transformation.SalesforceWizardRole;
import org.etlforge.integration.data.script.ScriptUtils;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.data.structure.JsonStructure;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROMHeader;
import org.etlforge.integration.structure.crom.CROM_Tree;
import org.etlforge.integration.structure.crom.CromType;
import org.etlforge.integration.structure.crom.LinkIterator;
import org.etlforge.integration.structure.mapping.CromBasedMappingTreeStructure;
import org.etlforge.integration.structure.mapping.CromToMappingStructureWriter;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.integration.structure.mapping.PathSeparator;
import org.etlforge.integration.structure.reader.DocReader;
import org.etlforge.integration.structure.reader.JbNode;
import org.etlforge.integration.structure.reader.TextDocReader;
import org.etlforge.integration.structure.reader.XmlReader;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.UIManager2;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionListeners;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.widget.popup.PopupMenuUtils;
import org.etlforge.util.clipboard.ClipboardUtils;

public abstract class TreeComponent extends JPanel implements SelectionSource {
   private static final String INSTANCE_STRING = "instance ";
   private static final boolean TIP_ON = true;
   public static final int c_TreeMapperTarget = 2;
   public static final int c_Other = 77;
   protected static final String cmd_select_instance = "Select data instance";
   protected static final String cmd_select_substitution_group = "Select substitution group...";
   protected static final String cmd_select_derived_type = "Select derived type...";
   protected static final String cmd_remove_condition = "Remove condition";
   protected static final String cmd_remove_script = "Remove script";
   protected static final String cmd_add_condition = "Add condition";
   protected static final String cmd_add_scriptn = "Add script";
   protected static final String cmd_remove_multiple_mapping = "Remove current folder";
   protected static final String cmd_remove_fix_generator = "Remove fixed source loop node";
   protected static final String cmd_add_multiple_mapping = "Create extra folder for mappings";
   protected static final String cmd_rename_multiple_mapping = "Rename";
   protected static final String cmd_delete_mapping = "Delete mapping of this node";
   protected static final String cmd_delete_folder_mapping = "Clear the mappings under this folder";
   protected static final String cmd_find_instance_ = "Select data instance...";
   protected static final String cmd_ldap_entry_add = "Ldap add entry";
   protected static final String cmd_ldap_entry_delete = "Ldap delete entry";
   protected static final String cmd_ldap_entry_update = "Ldap update or add entry";
   protected static final String cmd_ldap_entry_update_only = "Ldap update entry only";
   protected static final String cmd_ldap_attribute_update = "Ldap update attribute";
   protected static final String cmd_ldap_attribute_remove = "Ldap remove attribute";
   protected static final String cmd_ldap_attribute_replace_all = "Ldap attribute replace all";
   protected static final String cmd_define_generator = "Define fixed source loop node";
   protected static final String cmd_do_straight_mapping = "Auto map";
   protected static final String cmd_copy_value = "Copy value to clipboard";
   protected static final String cmd_copy_node_name = "Copy node name to clipboard";
   protected static final String cmd_copy_field_name = "Copy field name to clipboard";
   protected static final String cmd_delete_target_translate = "Delete translation of target nodes";
   protected static final String cmd_delete_source_translate = "Delete translation of source nodes";
   protected static final String cmd_define_batch_node = "Define batch transformation...";
   protected static final String cmd_turn_on_evnode = "Turn on short display mode";
   protected static final String cmd_turn_off_evnode = "Turn off short display mode";
   public static final String SHOW_DATA_NODES_ONLY = "showDataNodesOnly";
   public static final String SIMPLE_NODE_DISPLAY = "simpleNodeDisplay";
   public static final String SIMPLE_XML_DISPLAY = "simpleXmlDisplay";
   protected final TreeMapper m_TreeMapper;
   public DocReader m_DocReader;
   protected final Migration m_TreeMapperMigration;
   public CROM_Tree m_CROM_Tree;
   CROM sfdcNode;
   public final int m_where;
   private final TipWindow m_tip;
   private int m_preferYmax;
   public boolean m_checkMapMode;
   public boolean m_isDataLinked;
   Color m_textColor;
   Color m_folderTextColor;
   private final Color m_bgdTreeColor;
   public JScrollPane m_scroll;
   public Node m_root;
   Node m_showNode;
   int m_showNodePos;
   int m_signRectSize;
   int m_signOffSet;
   int m_y;
   public int m_xmax = -1;
   List<Node.NodeLocation> m_savedPointsNode;
   public List<Node> m_selectedNodes = Lists.newArrayList();
   private int m_cursorIndex;
   public Node m_getLocationNode;
   private Node m_highLightedNode;
   public final Map<String, Node> m_deHashtable;
   public int m_expandMode = 0;
   public boolean m_flat;
   public final boolean m_bTarget;
   public boolean m_bIsLdapTarget;
   int m_counter;
   private boolean m_nodeEntered_save;
   private final Dimension m_size;
   public boolean m_gotFocus;
   private JPanel m_dragImage;
   protected final JPopupMenu m_popup = new JPopupMenu();
   private final JPopupMenu m_popup1 = new JPopupMenu();
   Color m_clrPreconditionText;
   Color m_clrYellowUnderLine;
   public String m_title;
   private File m_localFile;
   Set<Node> m_leftConnectedNodeSet;
   Set<Node> m_rightConnectedNodeSet;
   public int m_ymin;
   public int m_ymax;
   private final Point m_lastPoint = new Point();
   private boolean m_dragged;
   private int m_xSaved;
   private int m_ySaved;
   private boolean m_dirty;
   public boolean xsdExpandMinimum;
   private boolean m_bSourceDeAvailable = true;
   private NodeSearchUi nodeSearchUi;
   private NodeSearchResult searchResult;
   protected Node m_rightClickNode;
   private Node dropNode;
   private String searchTitle = "Search Tree";
   private final NodeKeyNavigator nodeNavigator;
   protected final ActionListener actionListener;
   private final FocusListener focusListener;
   private int locationStatus_saved;
   protected boolean insertOnDoubleClick;
   private CROM simpleSalesforceCrom;
   private Node simpleSalesforceNode;
   private final NodeRenderer nodeRenderer;
   public String m_hight_light_string;
   public JbNode m_jbRoot;
   private boolean m_LinkedColumnMarked = false;
   final Map<Node, String> actualNodeDisplayNames = Maps.newHashMap();
   private final TreeComponent.MouseHandler mouseHandler;
   private boolean simpleXmlElementDisplay = true;
   private boolean simpleNodeDisplay;
   private MappingTreeNodePathFilter nodePathFilter;
   private boolean showDataNodesOnly;
   private boolean showMappedNodesOnly;
   private NodeVisibilityFilter nodeVisibilityFilter;
   private final TreeComponent.SelectionDispatcher selectionDispatcher = new TreeComponent.SelectionDispatcher();

   public void setLinkedColumnFlag(String var1) {
      if (!this.m_LinkedColumnMarked && this.m_bTarget && this.m_root != null) {
         this.m_root.findLinkedColumns();
         this.m_LinkedColumnMarked = true;
      }
   }

   public TreeComponent(Object var1, int var2) {
      this(var1, var2, false, true, false, null);
   }

   public TreeComponent(Object var1, int var2, boolean var3, boolean var4, boolean var5, MappingTreeNodePathFilter var6) {
      this(
         var1,
         var2,
         UIManager2.getColor("etlforge.mappingtree.attributeColor", Colors.THEME_TEXT_COLOR),
         UIManager2.getColor("etlforge.mappingtree.folderColor", Colors.THEME_TEXT_COLOR),
         UIManager2.getColor("etlforge.mappingtree.backgroundColor", Color.WHITE),
         var3,
         var4,
         var5,
         var6
      );
   }

   private TreeComponent(Object var1, int var2, Color var3, Color var4, Color var5, boolean var6, boolean var7, boolean var8, MappingTreeNodePathFilter var9) {
      this.m_where = var2;
      this.m_bTarget = var2 == 2;
      this.simpleNodeDisplay = var6;
      this.simpleXmlElementDisplay = var7;
      if (var1 instanceof Migration) {
         this.m_TreeMapperMigration = (Migration)var1;
         this.m_TreeMapper = null;
         this.m_tip = new TipWindow(this);
      } else if (var1 instanceof TreeMapper) {
         this.m_TreeMapper = (TreeMapper)var1;
         this.m_tip = this.m_TreeMapper.m_tip;
         this.m_TreeMapperMigration = null;
      } else {
         this.m_TreeMapper = null;
         this.m_TreeMapperMigration = null;
         this.m_tip = new TipWindow(this);
      }

      this.m_size = new Dimension(800, 400);
      this.actionListener = new TreeComponent.ActionListenerImpl();
      this.m_savedPointsNode = Lists.newArrayList();
      this.m_deHashtable = Maps.newHashMap();
      this.m_textColor = var3;
      this.m_folderTextColor = var4;
      this.m_bgdTreeColor = var5;
      this.nodeRenderer = new NodeRenderer(this);
      this.setBackground(this.m_bgdTreeColor);
      this.mouseHandler = new TreeComponent.MouseHandler();
      this.addMouseMotionListener(this.mouseHandler);
      this.addMouseListener(this.mouseHandler);
      this.addKeyListener(new TreeComponent.KeyListenerImpl());
      this.focusListener = new TreeComponent.FocusListenerImpl();
      this.addFocusListener(this.focusListener);
      if (this.m_TreeMapper != null) {
         this.m_dragImage = this.m_TreeMapper.m_dragImage;
         this.add(this.m_popup);
         this.add(this.m_popup1);
      } else if (this.m_TreeMapperMigration != null) {
         this.m_dragImage = this.m_TreeMapperMigration.m_dragImg;
      }

      this.m_counter = 0;
      this.m_root = this.create_node(null, "", null, "", true);
      this.addMouseWheelListener(new TreeComponent.MouseWheelListenerImpl());
      if (this.m_TreeMapper != null) {
         this.xsdExpandMinimum = this.m_TreeMapper.getNodeExpansionMode() == NodeExpansionMode.MINIMAL;
      }

      this.nodeNavigator = new NodeKeyNavigator(this);
      this.showMappedNodesOnly = var8;
      this.nodePathFilter = var9;
      this.updateNodeVisibility(false);
   }

   private final void updateNodeVisibility(boolean var1) {
      CompoundNodeVisibilityFilter var2 = new CompoundNodeVisibilityFilter();
      if (this.getRepresentedStructure() instanceof JsonStructure) {
         var2.addFilter(new JsonNodeVisibilityFilter());
      }

      if (this.hasEffectivePathFilter()) {
         var2.addFilter(new PathBasedNodeVisibilityFilter(this.m_TreeMapper, this.nodePathFilter));
      }

      if (this.showMappedNodesOnly) {
         var2.addFilter(this.createMappedNodesOnlyFilter());
      }

      if (this.showDataNodesOnly) {
         var2.addFilter(new NodesWithDataVisibiltyFilter(this));
      }

      this.nodeVisibilityFilter = var2.isEmpty() ? NodeVisibilityFilter.NOTHING_HIDDEN : var2;
      if (this.m_root != null) {
         this.m_root.updateHiddenFlags();
      }

      if (var1) {
         this.refresh();
      }
   }

   protected final boolean hasEffectivePathFilter() {
      return this.m_TreeMapper != null && this.nodePathFilter != null && this.nodePathFilter != MappingTreeNodePathFilter.TRANSPARENT;
   }

   public final TreeMapper getTreeMapper() {
      return this.m_TreeMapper;
   }

   public final boolean isSimpleMode() {
      return this.m_TreeMapper != null && this.m_TreeMapper.getMode() == TreeMapperMode.SIMPLE;
   }

   public final boolean hasData() {
      return this.m_jbRoot != null;
   }

   public void setTables(Set<Node> var1, Set<Node> var2) {
      this.m_leftConnectedNodeSet = var1;
      this.m_rightConnectedNodeSet = var2;
   }

   public boolean draw_leftExtentionLine(Node var1) {
      return this.m_leftConnectedNodeSet != null && this.m_leftConnectedNodeSet.contains(var1);
   }

   public boolean draw_rightExtentionLine(Node var1) {
      return this.m_rightConnectedNodeSet != null && this.m_rightConnectedNodeSet.contains(var1);
   }

   @Override
   public final Dimension getPreferredSize() {
      JScrollBar var1 = this.m_scroll.getVerticalScrollBar();
      var1.setBlockIncrement(this.m_scroll.getSize().height - 60);
      var1.setUnitIncrement(25);
      return this.m_size;
   }

   public final void setDirty() {
      this.m_dirty = true;
   }

   public final boolean isDirty() {
      return this.m_dirty;
   }

   public void init() {
      this.m_scroll.addFocusListener(this.focusListener);
      this.m_clrPreconditionText = StagingPanel.m_clrPreconditionText;
      this.m_clrYellowUnderLine = StagingPanel.m_clrUnderLine;
      this.m_y = Node.m_dy - Node.m_textHeight;
      this.m_savedPointsNode = Lists.newArrayList();
      this.m_root.m_p = new Point((int)(Node.m_dx / 4 + 0.5) - Node.m_dx, this.m_y);
      this.nodeRenderer.paint(null, this.m_root);
      int var1 = this.m_y + Node.m_dy + this.m_signRectSize;
      this.m_size.setSize(this.m_xmax, var1);
   }

   public final void setSimpleSfdcRepresentationRoot(Node var1) {
      this.simpleSalesforceNode = var1;
      this.simpleSalesforceCrom = var1 != null ? var1.getCROM() : null;
   }

   public final Node getSalesForceNode() {
      return this.simpleSalesforceNode;
   }

   public final Node getHighlightedNode() {
      return this.m_highLightedNode;
   }

   public final void setHighlightedNode(Node var1) {
      this.m_highLightedNode = var1;
      this.selectionDispatcher.setSelectedNode(var1);
   }

   public final void myPaint(Graphics var1) {
      if (this.m_TreeMapperMigration != null) {
         if (this == this.m_TreeMapperMigration.m_newSourceTree) {
            this.m_rightConnectedNodeSet = this.m_TreeMapperMigration.m_sourceMapArea.m_table_new2old.keySet();
         } else if (this == this.m_TreeMapperMigration.m_oldSourceTree) {
            this.m_leftConnectedNodeSet = this.m_TreeMapperMigration.m_sourceMapArea.m_table_old2new.keySet();
         } else if (this == this.m_TreeMapperMigration.m_oldTargetTree) {
            this.m_rightConnectedNodeSet = new HashSet<>();
            this.m_rightConnectedNodeSet.addAll(this.m_TreeMapperMigration.m_targetMapArea.m_table_new2old.values());
         } else if (this == this.m_TreeMapperMigration.m_newTargetTree) {
            this.m_leftConnectedNodeSet = this.m_TreeMapperMigration.m_targetMapArea.m_table_new2old.keySet();
         }
      } else {
         this.sfdcNode = this.isSimpleMode() ? this.simpleSalesforceCrom : null;
      }

      Node.m_textHeight = StagingPanel.m_font11Plain.getSize();
      Node.m_dx = (int)(Node.m_textHeight * 5 / 3 + 1.5);
      Node.m_dy = Node.m_dx + 1;
      this.m_cursorIndex = -1;
      Point var2 = new Point(this.m_scroll.getHorizontalScrollBar().getValue(), this.m_scroll.getVerticalScrollBar().getValue());
      this.m_ymin = var2.y - 100;
      this.m_ymax = var2.y + this.m_scroll.getSize().height + 100;
      this.m_signRectSize = (int)(Node.m_dx / 2 + 1.0);
      this.m_signOffSet = (int)(this.m_signRectSize / 4 + 1.0);
      if (var1 != null) {
         var1.setColor(this.m_textColor);
      }

      this.m_y = Node.m_dy - Node.m_textHeight;
      int var3 = (int)(Node.m_dx / 4 + 0.5) - Node.m_dx;
      this.m_savedPointsNode = Lists.newArrayList();
      this.m_root.m_p = new Point(var3, this.m_y);
      this.nodeRenderer.paint(var1, this.m_root);
      int var4 = this.m_y + Node.m_dy + this.m_signRectSize;
      if (var4 < this.m_preferYmax) {
         var4 = this.m_preferYmax;
      }

      this.m_size.setSize(this.m_xmax, var4);
      this.m_cursorIndex = 0;
      if (this.m_TreeMapperMigration != null) {
         if (this == this.m_TreeMapperMigration.m_newSourceTree) {
            this.m_TreeMapperMigration.m_sourceMapArea.repaint();
         } else if (this == this.m_TreeMapperMigration.m_oldSourceTree) {
            this.m_TreeMapperMigration.m_sourceMapArea.repaint();
            this.m_TreeMapperMigration.m_mapArea.repaint();
         } else if (this == this.m_TreeMapperMigration.m_oldTargetTree) {
            this.m_TreeMapperMigration.m_mapArea.repaint();
            this.m_TreeMapperMigration.m_targetMapArea.repaint();
         } else if (this == this.m_TreeMapperMigration.m_newTargetTree) {
            this.m_TreeMapperMigration.m_targetMapArea.repaint();
         }
      }
   }

   @Override
   public void paintComponent(Graphics var1) {
      if (var1 != null) {
         super.paintComponent(var1);
      }

      this.myPaint(var1);
   }

   public final void changeShowChildren() {
      if (this.m_TreeMapper != null) {
         if (this.m_expandMode == 0) {
            this.m_expandMode = 1;
         } else if (this.m_expandMode == 1) {
            this.m_expandMode = 0;
         }

         this.applyCurrentExpansionMode();
      }
   }

   public final void applyCurrentExpansionMode() {
      if (this.m_root.getChildren() != null) {
         for (Node var2 : this.m_root.getChildren()) {
            if (var2.isFolder()) {
               var2.changeShowChildren(this.m_expandMode);
            }
         }
      } else {
         this.m_root.changeShowChildren(this.m_expandMode);
      }
   }

   public final void changeShowChildren(boolean var1) {
      this.m_expandMode = var1 ? 1 : 0;
      if (this.m_root.hasChildren()) {
         for (Node var3 : this.m_root.getChildren()) {
            if (var3.isFolder()) {
               var3.changeShowChildren(var1);
            }
         }
      } else {
         this.m_root.changeShowChildren(var1);
      }
   }

   protected void processSingleClick(Node var1, int var2, MouseEvent var3) {
      boolean var4 = var3.isControlDown();
      switch (var2) {
         case 0:
            if (this.m_TreeMapper != null && this.m_highLightedNode != null) {
               this.setHighlightedNode(null);
               this.onSelectionCleared();
               this.repaint(0, this.m_ymin, this.m_xmax + 100, this.m_ymax);
            }
            break;
         case 1:
         case 2:
            if (var1 != null && var1.isFolder()) {
               if (!var1.isRoot() || this.m_bSourceDeAvailable) {
                  this.m_preferYmax = this.m_scroll.getVerticalScrollBar().getValue() + this.m_scroll.getSize().height;
                  this.toggleNodeExpansion(var1);
               }
               break;
            }
         case 3:
            if (this.m_TreeMapper != null) {
               this.setHighlightedNode(var1);
               if (this.m_where == 2) {
                  this.m_TreeMapper.selectTargetNode(this.m_highLightedNode, var4);
               }

               this.onNodeSelected(var1, var4);
               this.repaint(0, this.m_ymin, this.m_xmax + 100, this.m_ymax);
            } else if (this.m_TreeMapperMigration != null) {
               this.setHighlightedNode(var1);
            }
         case 4:
      }
   }

   private void stepThroughDataInstances(Node var1, boolean var2) {
      if (this.m_TreeMapper != null) {
         if (var1 != null && var1.m_jbNode != null) {
            Node var3 = var1;
            if (var1 == this.getSalesForceNode() && var1.m_parent != null && var1.m_parent.isLoopNode()) {
               var3 = var1.m_parent;
            }

            if (var3.isLoopNode()) {
               DataTree var4 = this.getDataTree();
               if (var2) {
                  var4.previousInstance(this, var3);
               } else {
                  var4.nextInstance(this, var3);
               }
            }
         }

         this.repaint(0, this.m_ymin, this.m_xmax + 100, this.m_ymax);
      }
   }

   private void processDoubleClicks(Node var1, int var2) {
      if (this.m_TreeMapper != null && var1 != null) {
         if (var1.isEmptyDerivedTypeHolder()) {
            this.selectDerivedType(var1);
            this.refresh();
         } else if (var1.isEmptySubstitutionGroupHeader()) {
            this.selectSubstitutionGroup(var1);
            this.refresh();
         } else if (var1.isFolder() && var1.m_parent != null && !this.insertOnDoubleClick) {
            this.toggleNodeExpansion(var1);
         } else {
            this.handleDoubleClick(var1, var2, this.locationStatus_saved);
            this.repaint(0, this.m_ymin, this.m_xmax + 100, this.m_ymax);
         }
      }
   }

   protected void handleDoubleClick(Node var1, int var2, int var3) {
   }

   protected void processRightClickOnNode(Node var1) {
      if (this.m_TreeMapper != null) {
         JPopupMenu var2 = new JPopupMenu();
         MenuBuilder var3 = new MenuBuilder(this.actionListener, var2);
         this.m_rightClickNode = var1;
         if (this.m_bIsLdapTarget) {
            if (LdapUtils.isLdapClassEntry(this, var1) || LdapUtils.isLdapClassEntryCommand(this, var1)) {
               var3.addMenuItem("Ldap add entry");
               var3.addMenuItem("Ldap update or add entry");
               var3.addMenuItem("Ldap update entry only");
               var3.addMenuItem("Ldap delete entry");
               var3.addMenuSeparator();
            } else if (LdapUtils.isLdapAttribute(this, var1)) {
               boolean var4 = LdapUtils.isLdapAttributeDelete(this, var1);
               boolean var5 = !var4 && LdapUtils.isLdapAttributeReplaceAll(this, var1);
               var3.addRadioButtonMenuItem("Ldap remove attribute", var4);
               var3.addRadioButtonMenuItem("Ldap update attribute", !var4 && !var5);
               if (var1.isLoopNode()) {
                  var3.addRadioButtonMenuItem("Ldap attribute replace all", var5);
               }

               var3.endRadioButtonMenuItem();
               var3.addMenuSeparator();
            }
         }

         if (var1.isFolder()) {
            var3.addMenuItem("Copy node name to clipboard");
            if (var1 == this.getSalesForceNode() && !this.getTreeMapper().getTransformation().isPartOfSalesforcePartnerWizard()) {
               var1 = var1.m_parent;
            }

            if (var1.m_parent == null) {
               Action var8 = this.getEditStructureAction();
               if (var8 != null) {
                  var3.addAction(var8);
               }
            }

            if (var1.isLoopNode()) {
               if (this.isDataLoaded()) {
                  JbNode var9 = var1.m_jbNode;
                  if (var9 != null && var9.isInstance()) {
                     int var12 = var9.getParent().getChildren().size();
                     if (var12 > 1) {
                        if (var12 > 5) {
                           var3.addMenuItem("Select data instance...");
                        } else {
                           var3.addMenu("Select data instance");

                           for (int var6 = 0; var6 < var12; var6++) {
                              var3.addMenuItem("instance " + (var6 + 1));
                           }

                           var3.endOfCurrentMenu();
                        }
                     }
                  }
               }

               if (this instanceof TreeMapperSourceTree) {
                  var3.addMenuItem("Define batch transformation...");
               }
            }

            this.populateFolderNodeContextMenu(var1, var3);
            if (var1.isSubstitutionGroupHeader()) {
               var3.addMenuItem("Select substitution group...");
            }

            if (this.m_rightClickNode.isDerivedTypeHolder()) {
               var3.addMenuItem("Select derived type...");
            }

            if (this.m_where == 2 && this.m_TreeMapper != null) {
               var3.addMenuItem("Clear the mappings under this folder");
            }
         } else {
            var3.addMenuItem("Copy field name to clipboard");
            if (var1.hasValue()) {
               var3.addMenuItem("Copy value to clipboard");
            }

            if (this.m_where == 2 && this.m_TreeMapper != null && this.m_TreeMapper.getMappingManager().findMapping(var1.m_deName) != null) {
               var3.addMenuItem("Delete mapping of this node");
            } else if (var1.isUndefinedDataType() && !this.m_bIsLdapTarget) {
               Answer var10 = Ask.okOrCancel("Change data type to XMLType?", "Undefined data type");
               if (var10.isOk()) {
                  int var13 = var1.getDisplay().indexOf("(Unknown)");
                  int var15 = var13 + "(Unknown)".length();
                  String var7 = var1.getDisplay().substring(0, var13) + "(XMLType)" + var1.getDisplay().substring(var15);
                  var1.setDisplay(var7);
               }

               this.repaint(0, this.m_ymin, this.m_xmax + 100, this.m_ymax);
            }
         }

         if (org.etlforge.integration.client.ui.interchange.entity.transformation.tree.xml.XmlNodeSettings.supports(this, this.m_rightClickNode)) {
            var3.addMenuSeparator();
            var3.addMenuItem("XML node settings...");
         }
         JMenuItem var11 = var3.getSingleCmd();
         if (var11 != null) {
            String var14 = var11.getText();
            if (var14.equals("Select derived type...")
               || var14.equals("Select substitution group...")
               || var14.equals("Select data instance")
               || var14.equals("Select data instance...")) {
               var11.doClick();
               return;
            }
         }

         var3.show(this, this.m_rightClickNode.m_p.x + 5 + this.m_scroll.getHorizontalScrollBar().getValue(), this.m_rightClickNode.m_p.y + Node.m_textHeight);
      }
   }

   protected Action getEditStructureAction() {
      return null;
   }

   protected void populateFolderNodeContextMenu(Node var1, MenuBuilder var2) {
   }

   protected void onDragStopped(MenuBuilder var1, int var2, int var3) {
   }

   protected void setDropNode(Node var1) {
      this.dropNode = var1;
   }

   final void toggleNodeExpansion(Node var1) {
      var1.setNextShowMode();
      var1.expandNode();
      if (var1.isExpanded()) {
         if (var1.isEmptyDerivedTypeHolder()) {
            this.selectDerivedType(var1);
         } else if (var1.isEmptySubstitutionGroupHeader()) {
            this.selectSubstitutionGroup(var1);
         }
      }

      this.refresh();
      this.m_scroll.validate();
   }

   private Node create_node(Node var1, String var2, String var3, String var4, boolean var5) {
      boolean var6 = var5;
      return new Node(this, var1, -1, var2, var6, var3, var4, var5);
   }

   Map<String, CROM> getElementTypeMap() {
      return this.m_CROM_Tree != null ? this.m_CROM_Tree.getElementTypeMap() : new HashMap<>();
   }

   Map<String, CROM> getElementMap() {
      return this.m_CROM_Tree != null ? this.m_CROM_Tree.getElements() : null;
   }

   boolean hasElementTypeMap() {
      return this.m_CROM_Tree.getElementTypeMap().size() > 0;
   }

   public final CROM get_element_type(String var1) {
      return this.getElementTypeMap().get(var1);
   }

   public final CROM get_global_element(String var1) {
      return this.getElementMap().get(var1);
   }

   public final int getLocationStatus(int var1, int var2, boolean var3) {
      int var4 = 0;
      this.m_getLocationNode = null;
      if (this.m_root != null) {
         int var5 = (var2 - this.m_root.m_p.y) / Node.m_dy;
         int var6 = this.m_savedPointsNode.size();
         if (var5 >= 0 && var5 < var6) {
            Node.NodeLocation var7 = this.m_savedPointsNode.get(var5);
            Node var8 = var7.m_node;
            if (var8 != null) {
               if (var8.getDisplay().endsWith("No source is specified")) {
                  return 0;
               }

               Rectangle var9 = var7.m_rect;
               if ((var8 != this.m_root || var3) && var9.y <= var2 && var9.y + var9.height >= var2) {
                  this.m_getLocationNode = var8;
                  var4 = var8.getLocationStatus(var1);
               }
            }
         }
      }

      return var4;
   }

   public final void showNode(Node var1, boolean var2) {
      this.m_showNode = var1;
      if (this.m_showNode != null) {
         Node var3 = this.m_showNode.m_parent;
         boolean var4 = true;

         while (var3 != null) {
            if (var3.showChildrenMode() == 0) {
               var3.setShowMode(1);
               var4 = true;
            }

            var3 = var3.m_parent;
         }

         if (var2) {
            this.setHighlightedNode(this.m_showNode);
         }

         this.myPaint(null);
         int var5 = this.m_showNode.m_p.y - this.m_scroll.getVerticalScrollBar().getValue();
         if (var4 || var5 < 0 || var5 > this.m_scroll.getSize().height) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  TreeComponent.this.m_showNodePos = TreeComponent.this.m_showNode.m_p.y - 25;
                  Rectangle var1x = TreeComponent.this.m_scroll.getViewport().getViewRect();
                  if (TreeComponent.this.m_showNode.m_p.y + 20 < var1x.y || TreeComponent.this.m_showNode.m_p.y > var1x.y + var1x.height) {
                     TreeComponent.this.m_scroll.getVerticalScrollBar().setMaximum(TreeComponent.this.m_size.height);
                     TreeComponent.this.m_scroll.getHorizontalScrollBar().setValue(TreeComponent.this.m_showNode.m_p.x - 10);
                     TreeComponent.this.m_scroll.getVerticalScrollBar().setValue(TreeComponent.this.m_showNodePos);
                     TreeComponent.this.m_scroll.doLayout();
                     TreeComponent.this.revalidate();
                  }

                  TreeComponent.this.repaint();
               }
            });
         }
      }
   }

   protected void moveByMouseWheel(int var1) {
      int var2 = this.m_scroll.getVerticalScrollBar().getValue();
      int var3 = StagingPanel.m_textSize;
      if (var3 < 10) {
         var3 = 10;
      }

      this.m_scroll.getVerticalScrollBar().setValue(var2 + var1 * var3);
   }

   public final Node findNode(NodePath var1) {
      return this.findNode(var1.toString());
   }

   public final Node findNode(JbNode var1) {
      CROM var2 = var1.getCrom();
      if (var2 == null) {
         return null;
      }

      Node var3 = this.findNode(var2.getPath(this.m_flat, this.m_bTarget));
      if (var3 == null) {
         var3 = this.findNode(var2.getPath_(this.m_flat));
      }

      return var3;
   }

   public final Node findNode(String var1) {
      return this.m_deHashtable.get(var1);
   }

   public final void clearSourcePaths() {
      this.m_root.clearSourcePaths();
   }

   public final void clearSourcePathList() {
      this.m_root.clearPathListRecursively();
   }

   public final void clearUsedFlags() {
      this.m_root.clearUsedFlags();
   }

   public final void clearCheckFlags() {
      this.m_root.clearCheckFlags();
   }

   public final void updateHiddenNodes() {
      this.m_root.updateHiddenFlags();
   }

   public final void search() {
      NodeSearchUi var1 = this.getNodeSearchUi();
      var1.clearPreviousResult();
      Window var2 = null;
      if (this.m_TreeMapper != null) {
         var2 = this.m_TreeMapper.getOwner();
      } else if (this.m_TreeMapperMigration != null) {
         var2 = this.m_TreeMapperMigration;
      }

      UiProviderDialogDisplayer.display(var2, var1, "Find Nodes", KongaDialog.CLOSE_ONLY, NodeSearchUi.class);
   }

   protected void paintFocus() {
      if (this.m_TreeMapperMigration != null) {
         this.m_TreeMapperMigration.m_panel.repaint();
      }
   }

   public final Node getDtdRoot() {
      return this.m_root.m_CROM != null && (this.m_root.m_CROM.getType() & 1L) != 0L ? this.m_root : this.m_root.getFirstChild();
   }

   public final Node findNode_expandTreeIfNotExist(String var1) {
      Node var2 = this.findNode(var1);
      if (var2 == null) {
         try {
            var2 = this.expandTreeAndLookForNode(var1);
         } catch (TreeMapperException var4) {
         }
      }

      return var2;
   }

   public Node expandTreeAndLookForNode(String var1) throws TreeMapperException {
      return null;
   }

   public final void removeDataNodes() {
      this.m_jbRoot = null;
      this.m_DocReader = null;
      this.m_root.removeDataNodes();
      this.refresh();
   }

   private void nodeEnterTip(String var1, Component var2, Object var3, int var4, int var5) {
      if (var3 != this.m_tip.m_obj) {
         Point var6 = ((TreeComponent)var2).m_scroll.getLocationOnScreen();
         Point var7 = this.m_tip.getParent().getLocationOnScreen();
         int var8 = var4 + var6.x - var7.x;
         int var9 = var5 + var6.y - var7.y + 25;
         this.m_tip.showItAtXY(var1, var8, var9, var3);
      }
   }

   public void readMappingStructure(MappingTreeStructure var1, String var2) {
      if (var1 != null) {
         if (var1 instanceof CromBasedMappingTreeStructure) {
            this.m_CROM_Tree = ((CromBasedMappingTreeStructure)var1).toCromTree();
         } else {
            CROM var3 = var1.getRoot();
            if (var3 == null) {
               this.m_CROM_Tree = CROM_Tree.parse(var1);
            } else {
               this.m_CROM_Tree = new CROM_Tree(var3);
            }
         }

         this.createTreeFromCromTree(this.m_CROM_Tree, var2);
      }
   }

   public void createTreeFromCromTree(CROM_Tree var1, String var2) {
      this.replaceWrongNameForFlatTextStructureSegments(var1.getRoot());
      this.m_title = var2 == null ? "No title" : var2;
      this.m_flat = this.getRepresentedStructure().isFlat();
      Node var3 = this.create_node(null, var2, "", null, true);
      var3.setCromName("");
      this.m_root = var3;
      this.m_root.setFullyExpanded();
      this.m_deHashtable.clear();
      this.m_deHashtable.put(var3.m_deName, var3);
      this.m_CROM_Tree = var1;
      CROM var4 = var1.getRoot();
      if (var4 != null) {
         var4.addType(32768L);
         if (this.m_where == 2) {
            this.m_bIsLdapTarget = this.m_TreeMapper.m_Transformation.getOutputStructure().getStructureType() == DataStructureType.LDAP;
         }

         CromType var5 = CromType.fromName(var4.cromType.name);
         if ((var5.equals(CromType.CROM_t_Text) || var5.equals(CromType.CROM_t_DB)) && var4.getName().isEmpty()) {
            var3.m_CROM = var4;

            for (CROM var7 : var4.getChildren()) {
               var3.createChildrenFromCROM(var7, null, "");
            }
         } else {
            var3.createChildrenFromCROM(var4, null, "");
            if (this.m_bIsLdapTarget) {
               UnknownLdapTypeConverter.convert(this.m_root);
            }
         }
      }
   }

   private void replaceWrongNameForFlatTextStructureSegments(CROM var1) {
      if (this.getTreeMapper() != null) {
         if (var1.hasChildren()) {
            DataStructure var2 = this.getRepresentedStructure();
            if (var2 instanceof SimpleTextStructure) {
               Transformation var3 = this.getTreeMapper().getTransformation();
               if (var3.isPartOfSalesforcePartnerWizard()) {
                  SalesforceWizardRole var4 = var3.getSalesforceWizardRole();
                  if (!var4.isResponse()) {
                     CROM var5 = var1.getChild(0);
                     if ("FileFormat".equals(var5.getName())) {
                        var5.setName("");
                        var5.setDisplayName("");
                        var5.rebuildPaths();
                     }
                  }
               }
            }
         }
      }
   }

   public void refresh() {
      this.myPaint(null);
      this.revalidate();
      this.repaint();
      this.selectionDispatcher.setSelectedNode(null);
   }

   @Override
   public final void setOpaque(boolean var1) {
      super.setOpaque(true);
   }

   public final TreeMapperNode asTreeMapperNode() {
      return this.m_root.asTreeMapperNode(null);
   }

   public final void updateUnderlineNodes(Node var1) {
      if (this.m_selectedNodes.size() > 0) {
         for (Node var3 : this.m_selectedNodes) {
            if (var3 != null) {
               var3.setSelectedFlag(false);
            }
         }

         this.m_selectedNodes.clear();
      }

      if (var1 != null && !var1.isFolder()) {
         for (String var6 : var1.getPathList()) {
            Node var4 = this.findNode(ScriptUtils.stripInstanceTag(var6));
            if (var4 != null && this.m_selectedNodes.indexOf(var4) < 0) {
               this.m_selectedNodes.add(var4);
               var4.setSelectedFlag(true);
            }
         }
      }

      this.repaint();
   }

   public final void setSourceDeAvailable(boolean var1) {
      this.m_bSourceDeAvailable = var1;

      for (Node var3 : this.m_root.getChildren()) {
         if (var3.isRoot()) {
            var3.setShowMode(0);
         }
      }
   }

   Migration getTreeMapperMigration() {
      return this.m_TreeMapperMigration;
   }

   public final NodeSearchUi getNodeSearchUi() {
      if (this.nodeSearchUi == null) {
         this.nodeSearchUi = new NodeSearchUi(this, this.searchTitle, new Receiver<NodeSearchResult>() {
            public void handle(NodeSearchResult var1) {
               TreeComponent.this.searchResult = var1;
            }
         });
      }

      return this.nodeSearchUi;
   }

   public final void highlightSearchResult(Node var1) {
      if (var1 != null) {
         this.showNode(var1, true);
         if (this.m_TreeMapper != null) {
            this.onSearchResultHighlighted(var1);
         }
      }
   }

   protected void onSearchResultHighlighted(Node var1) {
   }

   protected boolean canHaveData() {
      return false;
   }

   protected boolean isDataLoaded() {
      return this.m_jbRoot != null;
   }

   protected void onNodeSelected(Node var1, boolean var2) {
   }

   protected void onSelectionCleared() {
   }

   protected void onDataLoopNodeClicked() {
   }

   public final void setSearchTitle(String var1) {
      this.searchTitle = var1;
   }

   protected boolean supportsToolTip() {
      return false;
   }

   protected String getToolTipForLocation(boolean var1, int var2) {
      assert this.supportsToolTip() : "getToolTipForLocation() is not supported";
      if (var1) {
         return this.m_getLocationNode != null ? this.getSourcePathToolTip() : null;
      }

      String var3 = "";
      if (var2 == 4 && this.m_getLocationNode.hasValue() && this.m_getLocationNode.m_x8 > this.m_scroll.getSize().width) {
         var3 = Utils.replaceString(this.m_getLocationNode.getRawValue(), "\\n", "\n");
      }

      if (var2 == 3) {
         var3 = "";
         if (this.m_getLocationNode.hasMapWarning()) {
            if (this.m_getLocationNode.isFolder()) {
               var3 = "Some mappings are missing under this folder!\n";
            } else if (this.m_getLocationNode.isLinkedColumn()) {
               var3 = "A linked column, must be mapped.\n";
            } else {
               var3 = "Must be mapped.\n";
            }
         }

         if (this.m_getLocationNode.isDerivedTypeHolder()) {
            var3 = var3 + "Right click to select/unselect type(s)";
         } else if (this.m_getLocationNode.isSubstitutionGroupHeader()) {
            var3 = var3 + "Right click to select/unselect substitution node";
         }

         if (StringUtils.isNotEmpty(this.m_getLocationNode.getDomainString())) {
            var3 = var3 + "- domain -\n" + this.m_getLocationNode.getDomainString();
         }
      }

      if (var2 == 4 && this.m_getLocationNode.m_jbNode != null) {
         var3 = "Click on "
            + this.m_getLocationNode.getValue()
            + " to display next instance\nhold down shift to click to previous instance\nleft click to choose instance to display";
      }

      return var3;
   }

   private String getSourcePathToolTip() {
      StringBuilder var1 = new StringBuilder(this.getDomainString());
      if (this.m_getLocationNode.isLoopNode()) {
         var1.append("sourcePathList:\n");

         for (String var3 : this.m_getLocationNode.getPathList()) {
            var1.append(var3 + "\n");
         }
      }

      return var1.toString();
   }

   protected String getDomainString() {
      return "";
   }

   protected boolean canHaveSourcePathTip() {
      return false;
   }

   protected abstract DataStructure getRepresentedStructure();

   public DataTree getDataTree() {
      return null;
   }

   public final Node getVisibleRoot() {
      if (this.isSimpleMode() && this.simpleSalesforceNode != null) {
         return this.simpleSalesforceNode;
      } else {
         return this.m_root.getChildCount() == 1 ? this.m_root.getChildAt(0) : null;
      }
   }

   public final boolean isAncestorOfVisibleRoot(Node var1) {
      Node var2 = this.getVisibleRoot();
      return var2 == null ? false : var1.isAncestorOf(var2);
   }

   public final void writeToCache(InputAndOutputMappingTreeStructureCache var1) {
      if (this.isDirty()) {
         BufferedWriter var2 = null;

         try {
            Writer var3 = this.getCacheWriter(var1);
            if (var3 != null) {
               var2 = new BufferedWriter(var3);
               this.writeJtrToOutput(var2);
            }

            this.m_dirty = false;
         } catch (IOException var7) {
            this.handleCachingException(var7);
         } finally {
            KongaIoUtils.close(var2);
         }
      }
   }

   protected Writer getCacheWriter(InputAndOutputMappingTreeStructureCache var1) throws IOException {
      return null;
   }

   private void writeJtrToOutput(Writer var1) throws IOException {
      assert this.m_CROM_Tree != null : "m_CROM_Tree should be defined already.";
      CROM var2 = this.m_CROM_Tree.getRoot();
      CromToMappingStructureWriter var3 = new CromToMappingStructureWriter(
         var2, new CROMHeader(var2.cromType.name, this.m_CROM_Tree.getBeginQuote(), this.m_CROM_Tree.getEndQuote())
      );
      this.m_CROM_Tree.writeXml(var1, var3);
   }

   private final void handleCachingException(Exception var1) {
      ApplicationLog.getMainLogger().warning(var1.getMessage());
   }

   protected void handleDeleteRequest(Node var1) {
   }

   public final void setDocReader(DocReader var1) {
      this.m_DocReader = var1;
   }

   public final boolean isXml() {
      DataStructure var1 = this.getRepresentedStructure();
      return var1.getStructureType().isXmlBased();
   }

   public final boolean isJson() {
      return this.getRepresentedStructure() instanceof JsonStructure;
   }

   private void selectDerivedType(Node var1) {
      if (var1 != null && var1.isDerivedTypeHolder()) {
         var1.expandNode();
         if (var1.m_CROM.getTypeExtentions() != null) {
            CROM var2 = var1.m_CROM;
            ArrayList var3 = Lists.newArrayList(var2.getTotalTypeExtentions(this.getElementTypeMap()));
            if (!var2.isAbstract()) {
               String var4 = var2.getSchemaTypeExpandedName();
               CROM var5 = this.get_element_type(var4);
               if (var5 != null && !var5.isAbstract()) {
                  var3.add(var4);
               }
            }

            if (!var3.isEmpty()) {
               DerivedTypesSelector var6 = new DerivedTypesSelector(this, var1, var3);
               var6.open(TreeStrings.get("Xml.DerivedTypes.Title"), TreeStrings.get("Xml.DerivedTypes.Caption"));
            }
         }
      }
   }

   private void selectSubstitutionGroup(Node var1) {
      if (var1 != null && var1.isSubstitutionGroupHeader()) {
         Set var2 = var1.m_CROM.getSubstitutionGroup();
         if (var2 != null && !var2.isEmpty()) {
            new SubstitutionGroupsSelector(this, var1, var1.m_CROM.getSubstitutionGroup())
               .open(TreeStrings.get("Xml.SubGroup.Title"), TreeStrings.get("Xml.SubGroup.Caption"));
         }
      }
   }

   public final Node insertPreconditionNode(Node var1) {
      if (this.m_TreeMapper == null && this.m_TreeMapperMigration == null) {
         return null;
      }

      try {
         String var2 = this.getPreconditionPath(var1);
         Node var3 = this.findNode(var2);
         if (var3 == null) {
            var3 = var1.createPreConditionNode(var2);
         }

         if (!var1.canHaveCondition()) {
            String var4 = var3.m_CROM.getName();
            var4 = var4.replace("/PRECONDITION/", "/PRESCRIPT/");
            var3.m_CROM.setName(var4);
         }

         var1.setHasPreconditionFlag(true);
         this.m_root.reOrder(0);
         if (this.m_TreeMapper != null) {
            this.m_TreeMapper.getMappingManager().reOrder();
         }

         this.repaint();
         return var3;
      } catch (Exception var5) {
         var5.printStackTrace();
         return null;
      }
   }

   private String getPreconditionPath(Node var1) {
      String var2 = var1.canHaveCondition() ? "/PRECONDITION/" : "/PRESCRIPT/";
      String var3 = var1.m_deName;
      if (this.m_flat) {
         int var4 = var3.indexOf(35);
         if (var4 < 0) {
            var3 = var2;
            if (var1.m_parent == null) {
               var3 = "/" + var3;
            }
         } else {
            int var5 = var3.indexOf(93);
            if (var5 < 0) {
               var5 = var3.length();
            }

            var3 = var2 + var3.substring(var4, var5);
         }
      } else {
         var3 = KongaStringUtils.unwrap(var3) + var2;
      }

      return var3;
   }

   public final OrderedSelection getSelection() {
      return this.selectionDispatcher.getSelection();
   }

   @Override
   public final void addSelectionListener(SelectionListener var1) {
      this.selectionDispatcher.addListener(var1);
   }

   @Override
   public final void removeSelectionListener(SelectionListener var1) {
      this.selectionDispatcher.removeListener(var1);
   }

   public final boolean isShowDataNodesOnly() {
      return this.showDataNodesOnly;
   }

   public final void setShowDataNodesOnly(boolean var1) {
      if (var1 != this.showDataNodesOnly) {
         this.showDataNodesOnly = var1;
         this.updateNodeVisibility(true);
         this.firePropertyChange("showDataNodesOnly", !var1, var1);
      }
   }

   public final boolean isShowMappedNodesOnly() {
      return this.showMappedNodesOnly;
   }

   public final void setShowMappedNodesOnly(boolean var1) {
      if (var1 != this.showMappedNodesOnly) {
         this.showMappedNodesOnly = var1;
         this.updateNodeVisibility(true);
      }
   }

   protected NodeVisibilityFilter createMappedNodesOnlyFilter() {
      throw new UnsupportedOperationException();
   }

   public final void setNodeVisibilityFilter(NodeVisibilityFilter var1) {
      this.nodeVisibilityFilter = var1 != null ? var1 : NodeVisibilityFilter.NOTHING_HIDDEN;
   }

   public final NodeVisibilityFilter getNodeVisibilityFilter() {
      return this.nodeVisibilityFilter;
   }

   public final void setNodePathFilter(MappingTreeNodePathFilter var1) {
      this.nodePathFilter = var1;
      this.updateNodeVisibility(true);
   }

   protected final MappingTreeNodePathFilter getNodePathFilter() {
      return this.nodePathFilter;
   }

   public boolean isHidden(Node var1) {
      return this.nodeVisibilityFilter.isHidden(var1);
   }

   public JbNode findJbNode(String var1, String var2) throws Exception {
      if (this.m_jbRoot == null) {
         return null;
      }

      StringTokenizer var3 = PathSeparator.tokenize(var1, true);
      StringTokenizer var4 = new StringTokenizer(var2, ";", false);
      String var5 = "";
      JbNode var6 = this.m_jbRoot;
      if (this.m_flat) {
         List var13 = this.m_jbRoot.getChildren();
         if (var13 == null) {
            return null;
         }

         if (!var4.hasMoreTokens()) {
            return null;
         }

         JbNode var15 = null;

         for (JbNode var18 : this.m_jbRoot.getChildren()) {
            CROM var19 = var18.getCrom();
            if (var19.getName().equals(var1)) {
               var15 = var18;
               break;
            }
         }

         int var17 = Integer.parseInt(var4.nextToken());
         if (var15 != null) {
            var13 = var15.getChildren();
            if (var13 != null && var17 > 0 && var17 <= var13.size()) {
               return (JbNode)var13.get(var17 - 1);
            }
         }

         return null;
      } else {
         while (var3.hasMoreTokens()) {
            String var7 = var3.nextToken();
            var5 = var5 + var7;
            if (PathSeparator.starts(var7)) {
               String var8 = "[" + var5 + "]";
               Node var9 = this.m_deHashtable.get(var8);
               if (var9 == null) {
                  assert this.m_deHashtable.get(var5).isEVNode();
                  return null;
               }

               CROM var10 = var9.m_CROM;
               if (var6 != null && var10.getParent() != null) {
                  var6 = var6.getChild(var10);
                  if (var6 == null) {
                     return null;
                  }

                  if (PathSeparator.startsWithLoopSeparator(var7) && var4.hasMoreTokens()) {
                     String var11 = var4.nextToken();
                     int var12 = Integer.parseInt(var11) - 1;
                     if (var12 < 0 || var12 >= var6.getChildren().size()) {
                        return null;
                     }

                     var6 = var6.getChildren().get(var12);
                  }
               }
            }
         }

         return var6;
      }
   }

   public void loadDataFile(File var1, File var2, boolean var3, int var4, int var5) {
      try {
         CROM var6 = this.m_root.getRootDtd();
         if (this.m_DocReader != null) {
            this.m_DocReader.close();
         }

         if (!this.isXml() && !this.isJson()) {
            this.m_DocReader = new TextDocReader(var1, this.m_flat, var6);
         } else {
            XmlReader var7 = new XmlReader(var1, var6);
            if (this.isJson()) {
               var7.useForJson();
            }

            this.m_DocReader = var7;
         }

         LinkIterator var17 = new LinkIterator(var2, this.m_CROM_Tree, this.m_flat);

         try {
            this.m_jbRoot = this.m_DocReader.parse_all(var4, var5);
            StringBuilder var8 = new StringBuilder();
            this.m_DocReader.assignToStructure(this.m_CROM_Tree, var17, var8);
            if (var8.length() > 0) {
               String var20 = var3 ? "Target Data Warning" : "Source Data Warning";
               this.m_TreeMapper.getTreeMapperContainer().showDataLoadWarning(var20, var8.toString());
            }
         } catch (Exception var14) {
            String var9 = var3 ? "Failed to load the target file: " : "Failed to load the source file: ";
            ErrorLog.attention(this.getClass(), var9 + var14.getMessage(), var14);
         } finally {
            KongaIoUtils.close(var17);
         }

         if (this.m_jbRoot == null) {
            this.m_localFile = null;
         } else {
            Node var18 = this.m_root;
            JbNode var21;
            if (this.m_flat) {
               var21 = null;
               var18 = this.m_root.getFirstChildFolder();
               if (this.m_jbRoot.getChildren().size() > 0) {
                  var21 = this.m_jbRoot.getChildren().get(0);
                  if (!var21.isInstance() && var21.getChildren().size() > 0) {
                     var21 = var21.getChildren().get(0);
                  } else {
                     var21 = null;
                  }
               }
            } else {
               var21 = this.m_jbRoot;
               var18 = this.findNode(var21);
            }

            var18.setJbNode_resursive(var21, null);
            this.setDataValues_recursive(var18);
            DataTree var10 = var3 ? this.getTreeMapper().getTargetDataTree() : this.getTreeMapper().getSourceDataTree();
            var10.setDataRoot(this, this.m_jbRoot);
            this.refresh();
            this.m_localFile = var1;
         }
      } catch (IOException var16) {
         throw new RuntimeException("loadDataFile(\"" + var1 + "\") failed.\nlocalFile:" + var1 + "\n" + var16);
      }
   }

   public void loadDataTree() {
      try {
         Node var1 = this.m_flat ? this.m_root : this.findNode(this.m_jbRoot);
         var1.setJbNode_resursive(this.m_jbRoot, null);
         this.setDataValues_recursive(var1);
         if (this.m_jbRoot != null && this.isShowDataNodesOnly()) {
            this.updateHiddenNodes();
         }

         this.refresh();
      } catch (IOException var2) {
         throw new RuntimeException("TreeComponent.loadDataTree() failed.\n" + var2 + "\n");
      }
   }

   void load(DocReader var1, JbNode var2) {
      this.m_DocReader = var1;
      this.m_jbRoot = var2;
      this.loadDataTree();
   }

   public boolean isDataFileLoaded(String var1) {
      return this.m_localFile != null;
   }

   public void loadLinkedJbNode(Node var1) throws Exception {
      if (!this.m_bTarget) {
         JbNode var2 = var1.m_jbNode;
         if (var2 != null) {
            JbNode var3 = var2.m_source;
            if (var3 != null) {
               Node var4 = this.findNode(var3);
               if (var4.m_jbNode == var3) {
                  return;
               }

               var4.setJbNode_resursive(var3, null);
            }
         }

         for (Node var6 : var1.getChildren()) {
            if (var6.isFolder() && var6.isLoopNode()) {
               this.loadLinkedJbNode(var6);
            }
         }
      }
   }

   public void setDataValues_recursive(Node var1) throws IOException {
      if (var1 != null && var1.m_jbNode != null) {
         if (!var1.hasChildren()) {
            var1.expandNode();
         }

         var1.setElementAttributes(this.m_DocReader);

         for (Node var3 : var1.getChildren()) {
            if (var3.isFolder()) {
               this.setDataValues_recursive(var3);
            } else if (var3.isEVNode()) {
               var1.setEVNodeValue(this.m_DocReader, var3);
            }
         }
      }
   }

   public void closeDocReader() {
      try {
         if (this.m_DocReader != null) {
            this.m_DocReader.close();
            this.m_DocReader = null;
            DataTree var1 = this.getDataTree();
            var1.removeData();
         }
      } catch (IOException var2) {
         ErrorLog.internal(TreeMapper.class, null, var2);
      }
   }

   public void displayJbNode(JbNode var1, CROM var2, String var3) {
      Node var4 = this.findNode(var1);
      this.m_hight_light_string = var3;
      this.getDataTree().displayInstance(this, var4, var1);
      if (this == this.m_TreeMapper.m_targetTreeComponent) {
         TreeMapperSourceTree var5 = this.m_TreeMapper.m_sourceTreeComponent;
         JbNode var6 = null;

         for (JbNode var7 = var1; var7 != null; var7 = var7.getParent()) {
            var6 = var7.m_source;
            if (var7.m_source != null) {
               break;
            }
         }

         if (var6 != null) {
            Node var9 = var5.findNode(var6);
            if (var9.m_jbNode != var6) {
               var5.displayJbNode(var6, null, null);
               var5.showNode(var9, true);
            }
         }
      }

      if (var2 != null) {
         Node var8 = var4.findLeaf(var2.getName());
         if (var8 == null && var4.isEVNode()) {
            var8 = var4;
         }

         this.showNode(var8, true);
      }

      this.refresh();
   }

   public String findGenratorPath(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         int var2 = var1.indexOf(35);
         if (var2 < 0) {
            var2 = var1.length();
         }

         int var3 = var1.lastIndexOf(".", var2);
         byte var4 = 0;
         if (var1.startsWith("[")) {
            var4 = 1;
         }

         return var3 > 0 ? var1.substring(var4, var3 + 1) : this.m_CROM_Tree.getRoot().getPath_(this.m_flat);
      } else {
         return "";
      }
   }

   public Node convertToSfdcNode(Node var1) {
      Node var2 = var1;
      if (this.sfdcNode != null && var1.getCROM() == this.sfdcNode.getParent()) {
         var2 = var1.findChildFolder(this.sfdcNode.getName());
      }

      return var2;
   }

   public final boolean isSimpleXmlElementDisplay() {
      return this.simpleXmlElementDisplay;
   }

   public void setSimpleXmlElementDisplay(boolean var1) {
      if (var1 != this.simpleXmlElementDisplay) {
         HashSet var2 = new HashSet();
         this.m_root.addExpandedNodes(var2);
         this.simpleXmlElementDisplay = var1;
         this.createTreeFromCromTree(this.m_CROM_Tree, this.m_title);
         if (this.m_TreeMapper != null) {
            try {
               this.m_TreeMapper.m_mapAreaTable.clear();
               this.m_TreeMapper.setMapList(this.m_TreeMapper.getCurrentMappings(), false);
               this.m_TreeMapper.updateHiddenNodesOnMapChange();
            } catch (TreeMapperException var4) {
               ErrorLog.internal(this.getClass(), null, var4);
            }
         }

         this.m_root.restoreExpandedNodes(var2, 0);
         this.refresh();
         this.firePropertyChange("simpleXmlDisplay", !var1, var1);
      }
   }

   public boolean isSimpleNodeDisplay() {
      return this.simpleNodeDisplay;
   }

   public void setSimpleNodeDisplay(boolean var1) {
      if (this.simpleNodeDisplay != var1) {
         this.simpleNodeDisplay = var1;
         this.refresh();
         this.firePropertyChange("simpleNodeDisplay", !var1, var1);
      }
   }

   @Override
   public String toString() {
      return this.m_title;
   }

   private class ActionListenerImpl implements ActionListener {
      private ActionListenerImpl() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (TreeComponent.this.m_TreeMapperMigration != null) {
            TreeComponent.this.handleDeleteRequest(TreeComponent.this.m_rightClickNode);
         } else {
            String var2 = var1.getActionCommand();
            if (var2.equals("Add condition") || var2.equals("Add script")) {
               TreeComponent.this.insertPreconditionNode(TreeComponent.this.m_rightClickNode);
               TreeComponent.this.refresh();
            } else if (var2.equals("Delete mapping of this node")) {
               TreeComponent.this.m_TreeMapper.deleteMapping(new NodePath(TreeComponent.this.m_rightClickNode.m_deName));
            } else if (var2.equals("Clear the mappings under this folder")) {
               TreeComponent.this.m_TreeMapper.deleteAllMappings(TreeComponent.this.m_rightClickNode);
            } else if (var2.equals("Remove condition") || var2.equals("Remove script")) {
               TreeComponent.this.m_rightClickNode.deletePreCondition();
               TreeComponent.this.refresh();
            } else if (var2.equals("Remove fixed source loop node")) {
               try {
                  if (TreeComponent.this.m_rightClickNode.isSfdcNode()) {
                     TreeComponent.this.m_rightClickNode.m_parent.m_mappingKits.removeFixedGenerator();
                  } else {
                     TreeComponent.this.m_rightClickNode.m_mappingKits.removeFixedGenerator();
                  }

                  TreeComponent.this.m_TreeMapper.setDirty(true, true);
                  TreeComponent.this.refresh();
               } catch (Exception var10) {
                  var10.printStackTrace();
               }
            } else if (var2.equals("Select derived type...")) {
               TreeComponent.this.selectDerivedType(TreeComponent.this.m_rightClickNode);
               TreeComponent.this.refresh();
            } else if (var2.equals("Select substitution group...")) {
               TreeComponent.this.selectSubstitutionGroup(TreeComponent.this.m_rightClickNode);
               TreeComponent.this.refresh();
            } else if (var2.equals("Select data instance")) {
               this.selectInstance(TreeComponent.this.m_rightClickNode);
               TreeComponent.this.refresh();
            } else if (var2.equals("Create extra folder for mappings")) {
               Node var3 = TreeComponent.this.m_rightClickNode.findLastMultipleMapping(-1);
               int var4 = var3.getMultipleMappingNumber();
               Node var5 = TreeComponent.this.m_rightClickNode.insertMultipleMapping(var4 + 1);
               if (var5 != null) {
                  NodeNameUtils.askUserForNewName(TreeComponent.this, var5);
               }

               TreeComponent.this.refresh();
               TreeComponent.this.setDirty();
            } else if (var2.equals("Remove current folder")) {
               TreeComponent.this.m_rightClickNode.deleteMultipleMapped();
               TreeComponent.this.refresh();
               TreeComponent.this.setDirty();
            } else if (var2.equals("Rename")) {
               NodeNameUtils.askUserForNewName(TreeComponent.this, TreeComponent.this.m_rightClickNode);
            } else if (var2.equals("Select data instance...")) {
               Node var11 = TreeComponent.this.m_rightClickNode;
               if (TreeComponent.this.m_rightClickNode == TreeComponent.this.getSalesForceNode()) {
                  var11 = TreeComponent.this.m_rightClickNode.m_parent;
               }

               assert var11.isLoopNode();
               JbNode var15 = var11.m_jbNode;
               if (var15 == null) {
                  return;
               }

               List var18 = var15.getParent().getChildren();
               int var6 = var18.size();
               int var7 = InstanceDialog.getInstance(TreeComponent.this.m_TreeMapper.getOwner(), var6);
               if (var7 > 0) {
                  DataTree var8 = TreeComponent.this.getDataTree();
                  var8.displayInstance(TreeComponent.this, var11, (JbNode)var18.get(var7 - 1));
               }
            } else if (var2.startsWith("instance ")) {
               String var12 = var2.substring("instance ".length());
               int var16 = Integer.parseInt(var12);
               DataTree var19 = TreeComponent.this.getDataTree();
               Node var20 = TreeComponent.this.m_rightClickNode;
               if (var20.getCROM() == TreeComponent.this.simpleSalesforceCrom) {
                  var20 = var20.m_parent;
               }

               List var21 = null;
               if (var20.m_jbNode != null) {
                  var21 = var20.m_jbNode.getParent().getChildren();
               }

               Object var22 = null;
               if (var21 != null && var16 > 0 && var16 <= var21.size()) {
                  var21.get(var16 - 1);
               }

               var19.displayInstance(TreeComponent.this, var20, (JbNode)var22);
            } else if (var2.equals("Ldap add entry")) {
               this.updateLdapCommand(TreeComponent.this.m_rightClickNode, "Entry command", "Add");
            } else if (var2.equals("Ldap delete entry")) {
               this.updateLdapCommand(TreeComponent.this.m_rightClickNode, "Entry command", "Delete");
            } else if (var2.equals("Ldap update or add entry")) {
               this.updateLdapCommand(TreeComponent.this.m_rightClickNode, "Entry command", "Update");
            } else if (var2.equals("Ldap update entry only")) {
               this.updateLdapCommand(TreeComponent.this.m_rightClickNode, "Entry command", "Update Only");
            } else if (var2.equals("Ldap update attribute")) {
               LdapUtils.setLdapAttributeRemove(TreeComponent.this, TreeComponent.this.m_rightClickNode, false);
               TreeComponent.this.setDirty();
            } else if (var2.equals("Ldap remove attribute")) {
               LdapUtils.setLdapAttributeRemove(TreeComponent.this, TreeComponent.this.m_rightClickNode, true);
               TreeComponent.this.setDirty();
            } else if (var2.equals("Ldap attribute replace all")) {
               LdapUtils.setLdapAttributeReplaceAll(TreeComponent.this, TreeComponent.this.m_rightClickNode);
               TreeComponent.this.setDirty();
            } else if (var2.equals("Define fixed source loop node")) {
               Node var13 = TreeComponent.this.dropNode;
               Node var17 = TreeComponent.this.m_highLightedNode;
               if (TreeComponent.this.m_where == 2) {
                  var13 = TreeComponent.this.m_highLightedNode;
                  var17 = TreeComponent.this.dropNode;
               }

               try {
                  if (var13.isSfdcNode()) {
                     var13 = var13.m_parent;
                  }

                  var13.m_mappingKits.fixGeneratorPath(var17.m_deName);
                  TreeComponent.this.m_TreeMapper.m_sourceTreeComponent.repaint();
                  TreeComponent.this.m_TreeMapper.m_targetTreeComponent.repaint();
                  TreeComponent.this.m_TreeMapper.setDirty(true);
               } catch (Exception var9) {
                  var9.printStackTrace();
               }
            } else if (var2.equals("Auto map")) {
               if (TreeComponent.this.m_where == 2) {
                  TreeComponent.this.m_TreeMapper.doStraightMappings(TreeComponent.this.dropNode, TreeComponent.this.m_highLightedNode);
               } else {
                  TreeComponent.this.m_TreeMapper.doStraightMappings(TreeComponent.this.m_highLightedNode, TreeComponent.this.dropNode);
               }

               TreeComponent.this.m_TreeMapper.m_sourceTreeComponent.repaint();
               TreeComponent.this.m_TreeMapper.m_targetTreeComponent.repaint();
            } else if (var2.equals("Copy value to clipboard")) {
               TreeComponent.this.m_rightClickNode.copyValueToClipboard();
            } else if (var2.equals("Copy field name to clipboard")) {
               if (TreeComponent.this.m_rightClickNode.isEVNode()) {
                  ClipboardUtils.copyToSystemClipboard(TreeComponent.this.m_rightClickNode.m_CROM.getParent().getName());
               } else {
                  TreeComponent.this.m_rightClickNode.copyNodeNameToClipboard();
               }
            } else if (var2.equals("Copy node name to clipboard")) {
               TreeComponent.this.m_rightClickNode.copyNodeNameToClipboard();
            } else if (var2.equals("XML node settings...")) {
               org.etlforge.integration.client.ui.interchange.entity.transformation.tree.xml.XmlNodeSettings.show(TreeComponent.this, TreeComponent.this.m_rightClickNode);
            } else if (var2.equals("Define batch transformation...")) {
               String var14 = TreeComponent.this.m_rightClickNode.getCROM().getPath(TreeComponent.this.m_TreeMapper.m_sourceTreeComponent.m_flat, false);
               if (StringUtils.isNotEmpty(var14)) {
                  TreeComponent.this.m_TreeMapper.editBatchNode(var14);
               }
            }
         }
      }

      private void selectInstance(Node var1) {
         if (var1.getCROM() == TreeComponent.this.simpleSalesforceCrom) {
            var1 = var1.m_parent;
         }

         JbNode var2 = var1.m_jbNode;
         if (var2 != null && var2.isInstance()) {
            List var3 = var2.getParent().getChildren();
            int var4 = var3.size();
            if (var4 >= 2) {
               if (var4 > 5) {
                  int var5 = InstanceDialog.getInstance(TreeComponent.this.m_TreeMapper.getOwner(), var4);
                  if (var5 > 0) {
                     DataTree var6 = TreeComponent.this.getDataTree();
                     var6.displayInstance(TreeComponent.this, var1, (JbNode)var3.get(var5 - 1));
                  }
               } else {
                  TreeComponent.this.m_popup1.removeAll();

                  for (int var7 = 0; var7 < var4; var7++) {
                     JMenuItem var8 = new JMenuItem("instance " + (var7 + 1));
                     TreeComponent.this.m_popup1.add(var8);
                     var8.addActionListener(this);
                  }

                  TreeComponent.this.m_popup1
                     .show(
                        TreeComponent.this, var1.m_p.x + 40 + TreeComponent.this.m_scroll.getHorizontalScrollBar().getValue(), var1.m_p.y + Node.m_textHeight
                     );
               }
            }
         }
      }

      private void updateLdapCommand(Node var1, String var2, String var3) {
         Node var4 = var1.isFolder() ? var1.findLeaf(var2) : var1;
         var4.setCmd(var3);
         TreeComponent.this.m_TreeMapper.deleteMapping(new NodePath(var4.m_deName));
         NodeMapping var5 = TreeComponent.this.m_TreeMapper.getNodeMappingFactory().newMapping(var4.m_deName, var3);
         MappingManager var6 = TreeComponent.this.m_TreeMapper.getMappingManager();
         var6.addMapping(var5);
         TreeComponent.this.m_TreeMapper.updateMappingFlag();
         TreeComponent.this.m_TreeMapper.updateMapAreaTables(var4.m_deName, var3);
         var6.changeHighLighted(var5);
         var4.updateMappingKits(var3);
         TreeComponent.this.m_TreeMapper.setDirty(true);
      }
   }

   private class FocusListenerImpl implements FocusListener {
      private FocusListenerImpl() {
      }

      @Override
      public void focusGained(FocusEvent var1) {
         TreeComponent.this.m_gotFocus = true;
         TreeComponent.this.paintFocus();
         DelayedJob.createAndSchedule(100, new Runnable() {
            @Override
            public void run() {
               if (TreeComponent.this.m_highLightedNode == null && !TreeComponent.this.mouseHandler.checkAndResetMouseWasPressedFlag()) {
                  TreeComponent.this.showNode(TreeComponent.this.m_root, true);
               }
            }
         });
      }

      @Override
      public void focusLost(FocusEvent var1) {
         TreeComponent.this.m_gotFocus = false;
         TreeComponent.this.repaint();
      }
   }

   private class KeyListenerImpl extends KeyAdapter {
      private KeyListenerImpl() {
      }

      @Override
      public void keyPressed(KeyEvent var1) {
         if (TreeComponent.this.m_TreeMapperMigration != null && var1.getKeyCode() == 127) {
            TreeComponent.this.handleDeleteRequest(TreeComponent.this.m_highLightedNode);
         } else if (TreeComponent.this.m_TreeMapper != null && !TreeComponent.this.m_TreeMapper.bSkipPainting) {
            int var2 = var1.getKeyCode();
            switch (var2) {
               case 10:
                  TreeComponent.this.processDoubleClicks(TreeComponent.this.m_highLightedNode, 3);
                  break;
               case 113:
                  this.processRenameRequest(TreeComponent.this.m_highLightedNode);
                  break;
               case 114:
                  this.findNextSearchMatch();
                  break;
               case 121:
                  if (var1.isControlDown()) {
                     var1.consume();
                     TreeComponent.this.processRightClickOnNode(TreeComponent.this.m_highLightedNode);
                  }
                  break;
               case 127:
                  TreeComponent.this.handleDeleteRequest(TreeComponent.this.m_highLightedNode);
                  break;
               default:
                  if (var2 != 17 && !var1.isControlDown()) {
                     TreeComponent.this.nodeNavigator.checkNavigation(var1);
                  } else if (TreeComponent.this.canHaveSourcePathTip() && TreeComponent.this.m_nodeEntered_save) {
                     this.sourcePathTip(TreeComponent.this.m_xSaved, TreeComponent.this.m_ySaved);
                  }
            }
         }
      }

      private void processRenameRequest(Node var1) {
         if (this.canNodeBeRenamed(var1)) {
            NodeNameUtils.askUserForNewName(TreeComponent.this, var1);
         }
      }

      private boolean canNodeBeRenamed(Node var1) {
         return var1 != null && var1.isMultipleMap();
      }

      private void findNextSearchMatch() {
         if (TreeComponent.this.searchResult != null) {
            Node var1 = TreeComponent.this.searchResult.nextNode();
            TreeComponent.this.highlightSearchResult(var1);
         }
      }

      private void sourcePathTip(int var1, int var2) {
         String var3 = TreeComponent.this.getSourcePathToolTip();
         if (!StringUtils.isEmpty(var3)) {
            TreeComponent.this.nodeEnterTip(var3, TreeComponent.this, TreeComponent.this.m_getLocationNode, var1, var2);
         }
      }

      @Override
      public void keyReleased(KeyEvent var1) {
         if (TreeComponent.this.m_TreeMapper != null && !TreeComponent.this.m_TreeMapper.bSkipPainting) {
            if (var1.getKeyCode() == 84 && var1.isControlDown() && var1.isShiftDown()) {
               TreeComponent.this.m_root.write("");
            }
         }
      }
   }

   private class MouseHandler extends MouseInputAdapter {
      public boolean mouseWasPressed;
      private Node mousePressedNode;

      private MouseHandler() {
      }

      @Override
      public void mouseReleased(MouseEvent var1) {
         if (TreeComponent.this.m_TreeMapper == null || !TreeComponent.this.m_TreeMapper.bSkipPainting || TreeComponent.this.m_TreeMapperMigration == null) {
            if (var1.isMetaDown() && TreeComponent.this.m_dragImage != null) {
               TreeComponent.this.m_dragImage.setVisible(false);
            }

            if (PopupMenuUtils.isPopupTrigger(var1) && TreeComponent.this.m_getLocationNode != null) {
               TreeComponent.this.processRightClickOnNode(TreeComponent.this.m_getLocationNode);
            } else if (TreeComponent.this.m_dragImage != null) {
               TreeComponent.this.m_dragImage.setVisible(false);
               TreeComponent.this.m_popup.removeAll();
               MenuBuilder var2 = new MenuBuilder(TreeComponent.this.actionListener, TreeComponent.this.m_popup);
               TreeComponent.this.setHighlightedNode(this.mousePressedNode);
               if (TreeComponent.this.m_dragged) {
                  if (TreeComponent.this.m_TreeMapper != null) {
                     this.mouseDragged(var1);
                     Rectangle var3 = TreeComponent.this.m_dragImage.getBounds();
                     Point var4 = TreeComponent.this.m_TreeMapper.getLocationOnScreen();
                     int var5 = var3.x + var3.width / 2 + var4.x;
                     int var6 = var3.y + var3.height / 2 + var4.y;
                     TreeComponent.this.onDragStopped(var2, var5, var6);
                     TreeComponent.this.m_TreeMapper.m_sourceTreeComponent.repaint();
                     TreeComponent.this.m_TreeMapper.m_targetTreeComponent.repaint();
                  } else if (TreeComponent.this.m_TreeMapperMigration != null) {
                     this.mouseDragged(var1);
                     Rectangle var12 = TreeComponent.this.m_dragImage.getBounds();
                     Point var15 = TreeComponent.this.m_TreeMapperMigration.getContentPane().getLocationOnScreen();
                     int var17 = var12.x + var12.width / 2 + var15.x;
                     int var18 = var12.y + var12.height / 2 + var15.y;
                     if (TreeComponent.this == TreeComponent.this.m_TreeMapperMigration.m_newSourceTree
                        || TreeComponent.this == TreeComponent.this.m_TreeMapperMigration.m_oldSourceTree) {
                        TreeComponent var19 = TreeComponent.this.m_TreeMapperMigration.m_oldSourceTree;
                        TreeComponent var20 = TreeComponent.this.m_TreeMapperMigration.m_newSourceTree;
                        int var21;
                        if (TreeComponent.this == TreeComponent.this.m_TreeMapperMigration.m_newSourceTree) {
                           Point var24 = var19.getLocationOnScreen();
                           var21 = var19.getLocationStatus(var17 - var24.x, var18 - var24.y, false);
                        } else {
                           Point var25 = var20.getLocationOnScreen();
                           var21 = var20.getLocationStatus(var17 - var25.x, var18 - var25.y, false);
                        }

                        if (var21 != 0) {
                           Node var26 = var19.m_getLocationNode;
                           Node var27 = var20.m_getLocationNode;
                           TreeComponent.this.m_TreeMapperMigration.addSourceMapPair(var26, var27, 0);
                           TreeComponent.this.m_TreeMapperMigration.m_newSourceTree.repaint();
                           TreeComponent.this.m_TreeMapperMigration.m_oldSourceTree.repaint();
                        }
                     } else if (TreeComponent.this == TreeComponent.this.m_TreeMapperMigration.m_newTargetTree
                        || TreeComponent.this == TreeComponent.this.m_TreeMapperMigration.m_oldTargetTree) {
                        TreeComponent var7 = TreeComponent.this.m_TreeMapperMigration.m_newTargetTree;
                        TreeComponent var8 = TreeComponent.this.m_TreeMapperMigration.m_oldTargetTree;
                        int var9;
                        if (TreeComponent.this == TreeComponent.this.m_TreeMapperMigration.m_newTargetTree) {
                           Point var10 = var8.getLocationOnScreen();
                           var9 = var8.getLocationStatus(var17 - var10.x, var18 - var10.y, false);
                        } else {
                           Point var22 = var7.getLocationOnScreen();
                           var9 = var7.getLocationStatus(var17 - var22.x, var18 - var22.y, false);
                        }

                        if (var9 != 0) {
                           Node var23 = var8.m_getLocationNode;
                           Node var11 = var7.m_getLocationNode;
                           TreeComponent.this.m_TreeMapperMigration.addTargetMapPair(var23, var11, 0);
                           TreeComponent.this.m_TreeMapperMigration.m_newTargetTree.repaint();
                           TreeComponent.this.m_TreeMapperMigration.m_oldTargetTree.repaint();
                        }
                     }
                  }
               } else {
                  int var13 = var1.getClickCount();
                  if (TreeComponent.this.locationStatus_saved == 4) {
                     TreeComponent.this.stepThroughDataInstances(TreeComponent.this.m_getLocationNode, var1.isShiftDown());
                  } else if (var13 == 1) {
                     TreeComponent.this.processSingleClick(TreeComponent.this.m_getLocationNode, TreeComponent.this.locationStatus_saved, var1);
                  } else if (var13 == 2) {
                     TreeComponent.this.processDoubleClicks(TreeComponent.this.m_getLocationNode, TreeComponent.this.locationStatus_saved);
                  }
               }

               if (TreeComponent.this.m_popup.getComponentCount() > 1) {
                  int var14 = var1.getX();
                  int var16 = var1.getY();
                  var2.show(TreeComponent.this, var14, var16 + 5);
               }

               TreeComponent.this.m_dragged = false;
               TreeComponent.this.m_dragImage.removeAll();
               TreeComponent.this.m_dragImage.setVisible(false);
               this.mousePressedNode = null;
            }
         }
      }

      @Override
      public void mouseDragged(MouseEvent var1) {
         if (TreeComponent.this.m_TreeMapper == null || !TreeComponent.this.m_TreeMapper.bSkipPainting) {
            if (TreeComponent.this.m_TreeMapper != null && this.mousePressedNode != null) {
               if (!TreeComponent.this.m_dragged) {
                  this.initDragAndDrop(this.mousePressedNode, TreeComponent.this.m_TreeMapper);
                  TreeComponent.this.m_dragged = true;
               }
            } else {
               if (TreeComponent.this.m_TreeMapperMigration == null || this.mousePressedNode == null) {
                  return;
               }

               if (!TreeComponent.this.m_dragged) {
                  this.initDragAndDrop(this.mousePressedNode, TreeComponent.this.m_TreeMapperMigration.getContentPane());
                  TreeComponent.this.m_dragged = true;
               }
            }

            int var2 = var1.getX();
            int var3 = var1.getY();
            Point var4 = TreeComponent.this.m_dragImage.getLocation();
            TreeComponent.this.m_dragImage.setLocation(var4.x + var2 - TreeComponent.this.m_lastPoint.x, var4.y + var3 - TreeComponent.this.m_lastPoint.y);
            TreeComponent.this.m_lastPoint.x = var2;
            TreeComponent.this.m_lastPoint.y = var3;
         }
      }

      private void initDragAndDrop(Node var1, Container var2) {
         TreeComponent.this.m_dragImage.removeAll();
         TreeComponent.this.m_dragImage.setLayout(null);
         JLabel var3 = new JLabel(var1.getDisplay());
         if (var1.isFolder()) {
            var3.setFont(StagingPanel.m_font12Bold);
         } else {
            var3.setFont(StagingPanel.m_font11Plain);
         }

         var3.setForeground(StagingPanel.m_clrHighLightText);
         int var4 = TreeComponent.this.getFontMetrics(var3.getFont()).stringWidth(var1.getDisplay());
         var3.setBounds(0, 0, var4 + 5, Node.m_dy);
         TreeComponent.this.m_dragImage.add(var3);
         int var5 = var1.m_p.x;
         int var6 = var1.m_p.y;
         var5 += 2 * Node.m_dx + 3;
         Point var7 = TreeComponent.this.getLocationOnScreen();
         Point var8 = var2.getLocationOnScreen();
         TreeComponent.this.m_dragImage.setBackground(TreeComponent.this.getBackground());
         TreeComponent.this.m_dragImage.setBounds(var5 + var7.x - var8.x - Node.m_dx / 5, var6 + var7.y - var8.y - Node.m_dy / 5, var4 + 2, Node.m_dy);
         TreeComponent.this.m_dragImage.setVisible(true);
      }

      @Override
      public void mouseMoved(MouseEvent var1) {
         if (TreeComponent.this.m_TreeMapper == null || !TreeComponent.this.m_TreeMapper.bSkipPainting) {
            int var2 = var1.getX();
            int var3 = var1.getY();
            int var4 = TreeComponent.this.getLocationStatus(var2, var3, false);
            boolean var5 = false;
            if (var4 == 3 || var4 == 4) {
               Point var6 = new Point(
                  TreeComponent.this.m_scroll.getHorizontalScrollBar().getValue(), TreeComponent.this.m_scroll.getVerticalScrollBar().getValue()
               );
               var2 = TreeComponent.this.m_getLocationNode.m_p.x - var6.x + 40;
               var3 = TreeComponent.this.m_getLocationNode.m_p.y - var6.y - 10;
               TreeComponent.this.m_xSaved = var2;
               TreeComponent.this.m_ySaved = var3;
               if (TreeComponent.this.supportsToolTip()) {
                  var5 = true;
                  String var7 = TreeComponent.this.getToolTipForLocation(var1.isControlDown(), var4);
                  if (!StringUtils.isEmpty(var7)) {
                     TreeComponent.this.nodeEnterTip(var7, TreeComponent.this, TreeComponent.this.m_getLocationNode, var2, var3);
                  }
               } else if (TreeComponent.this.m_nodeEntered_save) {
                  this.nodeExitTip();
               }
            } else if (TreeComponent.this.m_nodeEntered_save) {
               this.nodeExitTip();
            }

            TreeComponent.this.m_nodeEntered_save = var5;
            if (TreeComponent.this.m_cursorIndex >= 0 && var4 != TreeComponent.this.m_cursorIndex) {
               switch (var4) {
                  case 1:
                  case 2:
                  case 3:
                     TreeComponent.this.setCursor(Cursor.getPredefinedCursor(12));
                     break;
                  default:
                     TreeComponent.this.setCursor(Cursor.getPredefinedCursor(0));
               }

               TreeComponent.this.m_cursorIndex = var4;
            }
         }
      }

      private void nodeExitTip() {
         TreeComponent.this.m_tip.setVisible(false);
         TreeComponent.this.m_tip.m_obj = null;
      }

      @Override
      public void mouseExited(MouseEvent var1) {
         if (TreeComponent.this.m_nodeEntered_save) {
            this.nodeExitTip();
            TreeComponent.this.m_nodeEntered_save = false;
         }
      }

      @Override
      public void mousePressed(MouseEvent var1) {
         this.mouseWasPressed = true;
         if (TreeComponent.this.m_TreeMapper == null || !TreeComponent.this.m_TreeMapper.bSkipPainting) {
            if (PopupMenuUtils.isPopupTrigger(var1) && TreeComponent.this.m_getLocationNode != null) {
               TreeComponent.this.processRightClickOnNode(TreeComponent.this.m_getLocationNode);
            } else {
               int var2 = var1.getX();
               int var3 = var1.getY();
               if (var1.getClickCount() == 1) {
                  TreeComponent.this.locationStatus_saved = TreeComponent.this.getLocationStatus(var2, var3, true);
               }

               this.mousePressedNode = TreeComponent.this.m_getLocationNode;
               if (this.mousePressedNode == TreeComponent.this.m_root) {
                  this.mousePressedNode = null;
               }

               TreeComponent.this.requestFocusInWindow();
               if (TreeComponent.this.m_nodeEntered_save) {
                  this.nodeExitTip();
                  TreeComponent.this.m_nodeEntered_save = false;
               }

               TreeComponent.this.m_lastPoint.x = var2;
               TreeComponent.this.m_lastPoint.y = var3;
            }
         }
      }

      public boolean checkAndResetMouseWasPressedFlag() {
         boolean var1 = this.mouseWasPressed;
         this.mouseWasPressed = false;
         return var1;
      }
   }

   private class MouseWheelListenerImpl implements MouseWheelListener {
      private MouseWheelListenerImpl() {
      }

      @Override
      public void mouseWheelMoved(MouseWheelEvent var1) {
         int var2 = var1.getUnitsToScroll();
         TreeComponent.this.moveByMouseWheel(var2);
      }
   }

   private class SelectionDispatcher {
      private final SelectionListeners listeners = new SelectionListeners();
      private Node selectedNode;

      private SelectionDispatcher() {
      }

      public void setSelectedNode(Node var1) {
         if (var1 != this.selectedNode) {
            this.selectedNode = var1;
            SelectionChangedEvent var2 = new SelectionChangedEvent(TreeComponent.this, this.getSelection());
            this.listeners.call().selectionChanged(var2);
         }
      }

      public OrderedSelection getSelection() {
         return this.selectedNode == null ? OrderedSelection.EMPTY : SelectionFactory.newOrderedSelection(this.selectedNode);
      }

      public void addListener(SelectionListener var1) {
         this.listeners.add(var1);
      }

      public void removeListener(SelectionListener var1) {
         this.listeners.remove(var1);
      }
   }
}
