package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import javax.swing.Icon;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.ElementCardinality;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.NodeExpansionMode;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.StagingPanel;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeElement;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperIcons;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperNode;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperSourceTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.MappingManager;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.NodeMapping;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.migration.Migration;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.panel.MappingConflictChecker;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.SalesforceWizardRole;
import org.etlforge.integration.data.script.Transform;
import org.etlforge.integration.dataelement.GlobalDE;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CromType;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.integration.structure.reader.DocReader;
import org.etlforge.integration.structure.reader.JbNode;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.util.clipboard.ClipboardUtils;

public final class Node {
   private static final int c_isLinkedColumn = 64;
   private static final int c_isBold = 128;
   private static final int c_hasPrecondition = 512;
   private static final int c_formula = 2048;
   private static final int c_targetMapped = 4096;
   private static final int c_usedInExpr = 8192;
   private static final int c_multipleMapped = 16384;
   private static final int c_hasMultipleMap = 32768;
   private static final int c_warnMappingFlag = 65536;
   private static final int c_mapped = 131072;
   private static final int c_requireMappingFlag = 262144;
   private static final int c_selectedByGroup = 524288;
   private static final int c_isBatchNode = 1048576;
   private static final int c_hide = 2097152;
   private static final int c_identity = 4194304;
   private static final int c_sfdc_create_request_id = 8388608;
   private static final int c_sfdc_create_request_not_id = 16777216;
   private static final int c_mappingFlags = 458752;
   public static final int c_fullyExpanded = 1;
   public static final int c_partiallyExpanded = 2;
   public static final int c_notExpanded = 0;
   private static final int c_showChildren = 3;
   static final int ICON_WIDTH = 14;
   public static final boolean f_showPartial = false;
   public static final String c_blankString = "-blank-";
   public static final String c_sPrecondition = "/PRECONDITION/";
   public static final String c_sPrescript = "/PRESCRIPT/";
   public static final String c_sCondition = "Condition";
   public static final String c_sScript = "Script";
   public static int m_textHeight = StagingPanel.m_font11Plain.getSize();
   static int m_dx = (int)((m_textHeight * 5 + 3) / 3 + 0.5);
   static int m_dy = m_dx + 1;
   private static final List<Node> EMPTY_CHILD_LIST = Collections.emptyList();
   private final TreeComponent m_TreeComponent;
   public Point m_p;
   private int m_status;
   private String m_display;
   CROM m_CROM;
   public Node m_parent;
   private List<Node> m_children = EMPTY_CHILD_LIST;
   public final MappingKits m_mappingKits;
   public final String m_deName;
   public JbNode m_jbNode;
   int m_idatanode = -1;
   public int m_order;
   short m_x0;
   short m_x2;
   short m_x3;
   short m_x5;
   short m_x6;
   short m_x7;
   short m_x8;
   String m_cmd;
   private NodeValue m_value = NodeValue.create("");
   private Set<String> m_pathList = Collections.emptySet();

   public Node(TreeComponent var1, Node var2, int var3, String var4, boolean var5, String var6, boolean var7) {
      this.m_TreeComponent = var1;
      CROM var8 = var2 == null ? null : var2.m_CROM;
      this.m_CROM = new CROM(var8, var7, var4, var3, CromType.CROM_t_Unknown);
      this.m_parent = var2;
      if (this.m_parent != null) {
         this.m_parent.addChild(this);
      }

      this.setBoldFlag(var5);
      this.setTargetMappedFlag(false);
      this.m_p = new Point();
      this.m_display = var4;
      int var9 = this.m_display.indexOf(93) + 1;
      int var10 = this.m_display.indexOf(" (");
      this.setCromName(this.m_display);
      if (var10 > var9) {
         this.setCromName(this.m_display.substring(var9, var10));
      }

      if (this.isFolder()) {
         this.m_deName = var6 == null ? null : "[" + var6 + "]";
      } else {
         this.m_deName = var6;
      }

      if (this.m_deName != null && this.m_deName.length() > 0 && !var7) {
         int var11 = this.m_deName.indexOf(40) + 1;
         if (var11 > 0) {
            this.m_TreeComponent.m_deHashtable.put(this.m_deName.substring(0, var11).toUpperCase(), this);
         } else {
            this.m_TreeComponent.m_deHashtable.put(this.m_deName, this);
         }
      }

      this.m_mappingKits = this.m_TreeComponent.m_bTarget ? new MappingKits(this) : null;
   }

   public Node(TreeComponent var1, Node var2, int var3, String var4, boolean var5, String var6, String var7, boolean var8) {
      this.m_TreeComponent = var1;
      this.m_parent = var2;
      if (this.m_parent != null) {
         if (var3 < 0) {
            this.m_parent.addChildLast(this);
         } else {
            this.m_parent.addChildAtPosition(var3, this);
         }
      }

      this.setBoldFlag(var5);
      this.setTargetMappedFlag(false);
      this.m_p = new Point();
      CROM var9 = var2 == null ? null : var2.m_CROM;
      this.m_CROM = new CROM(var9, var8, var4, var3, CromType.CROM_t_Unknown);
      this.m_CROM.setValueType(this.getDataTypeFromDisplayText(var4));
      if (var6 == null) {
         var6 = "";
      }

      if (this.isFolder()) {
         this.m_deName = "[" + var6 + "]";
      } else {
         this.m_deName = var6;
      }

      if (var7 != null) {
         int var10 = var7.length();

         for (int var11 = 0; var11 < var10; var11++) {
            switch (var7.charAt(var11)) {
               case '$':
                  this.m_CROM.setOneToOneNode();
                  break;
               case '*':
                  this.m_CROM.setOuterJoin();
                  break;
               case '+':
                  this.m_CROM.setAtLeastOneNode();
                  break;
               case '?':
                  this.m_CROM.setOptionalNode();
                  break;
               case 'P':
                  assert var2 != null;
                  this.setPreconditionFlag();
                  String var12 = "Script".equals(var4) ? "/PRESCRIPT/" : "/PRECONDITION/";
                  if (var2 != null && var2.m_parent == null) {
                     var12 = "/" + var12;
                  }

                  this.m_CROM.setName(var12);
                  break;
               case 'f':
                  this.m_CROM.setLinkKey();
                  break;
               case 'p':
                  this.m_CROM.setPrimaryKey();
            }
         }
      }

      if (this.isFolder() && !this.m_TreeComponent.m_flat) {
         this.m_CROM.setElementNode();
         this.m_display = var4;
         if (var2 != null) {
            this.m_display = this.m_display + this.getCardinalityPostFix();
         }
      } else {
         this.m_CROM.setAttributeNode();
         this.m_display = var4;
         if (var2 != null) {
            this.m_display = this.translateLinkMarkerForDisplayName(var4);
         }
      }

      if (var4.startsWith("Condition")) {
         this.setPreconditionFlag();
      }

      if (this.m_deName != null && this.m_deName.length() > 0) {
         this.m_TreeComponent.m_deHashtable.put(this.m_deName, this);
      }

      this.m_order = this.m_TreeComponent.m_counter++;
      this.m_mappingKits = this.m_TreeComponent.m_bTarget ? new MappingKits(this) : null;
      this.setIdentityFlag();
   }

   private void addChildLast(Node var1) {
      this.prepareChildListForNewChild();
      this.m_children.add(var1);
   }

   private void prepareChildListForNewChild() {
      if (this.m_children == EMPTY_CHILD_LIST) {
         this.m_children = Lists.newArrayList();
      }
   }

   private void addChildAtPosition(int var1, Node var2) {
      this.prepareChildListForNewChild();
      this.m_children.add(var1, var2);
   }

   private String translateLinkMarkerForDisplayName(String var1) {
      String var2 = "[Link ";
      if (var1.contains(var2)) {
         var1 = StringUtils.substringAfter(var1, var2);
         return (this.isOuterJoin() ? "Outer join on " : "Join on ") + var1;
      } else {
         return var1;
      }
   }

   private Node(TreeComponent var1, Node var2, Node var3, CROM var4, int var5) {
      this.m_TreeComponent = var1;
      CROM var6 = var3.m_CROM;
      this.m_CROM = new CROM(var2.m_CROM, var6, var4);
      this.m_status = var3.m_status;
      this.setIdentityFlag();
      this.setTargetMappedFlag(false);
      this.m_p = new Point();
      String var7 = var3.getMultipleMappingName(var5);
      this.setCromName(var7);
      this.m_display = var3.m_display;
      this.setDefaultValue(var3.getDefaultValue());
      this.m_parent = var2;
      this.m_deName = this.m_CROM.getPath(this.m_TreeComponent.m_flat, this.m_TreeComponent.m_bTarget);
      if (var3.m_CROM.isPrecondition()) {
         this.setPreconditionFlag();
         var2.setHasPreconditionFlag(true);
      }

      this.m_TreeComponent.m_deHashtable.put(this.m_deName, this);
      this.m_mappingKits = this.m_TreeComponent.m_bTarget ? new MappingKits(this) : null;
   }

   Node(TreeComponent var1, Node var2, CROM var3) {
      this.m_TreeComponent = var1;
      this.m_CROM = var3;
      boolean var4 = !var3.isLeaf();
      boolean var5 = var4;
      this.setBoldFlag(var5);
      this.setTargetMappedFlag(false);
      this.m_p = new Point();
      this.m_parent = var2;
      if (this.m_parent != null) {
         this.m_parent.addChild(this);
      }

      this.m_deName = var3.getPath(this.m_TreeComponent.m_flat, this.m_TreeComponent.m_bTarget);
      this.setHidden(this.m_TreeComponent.isHidden(this));
      if (var3.isPrecondition()) {
         this.setPreconditionFlag();
      }

      if (var4) {
         if (!var3.isTypeDefinition() && var3.hasDerivedType()) {
            var3.setDerivedTypeHolder();
         }

         if (var3.hasSubstitutionGroup()) {
            var3.setSubstitutionGroupHead();
         }
      }

      this.m_display = var3.getDisplayText();
      if (this.isShortValueDisplayWithValueNodeOnly(var3)) {
         if (!this.isOptional() && !var3.isParentOptionalNode()) {
            this.m_display = "[EV] " + var3.getParent().getDisplayName() + this.m_display.substring(4);
         } else {
            this.m_display = "[EV?] " + var3.getParent().getDisplayName() + this.m_display.substring(4);
         }
      }

      if (!this.m_CROM.isXml()) {
         if (this.isFolder()) {
            if (!this.m_TreeComponent.m_flat) {
               this.m_display = this.m_display + this.getCardinalityPostFix();
            }
         } else {
            this.m_display = this.translateLinkMarkerForDisplayName(this.m_display);
         }
      }

      String var6 = var3.getName();
      int var7;
      if (var4 && var6 != null && (var7 = var6.indexOf(35)) >= 0) {
         this.setMultipleMapFlag();
         this.m_display = this.m_display + var6.substring(var7);
         String var8 = var6.substring(0, var7);
         Node var9 = this.m_parent.findChildFolder(var8);
         var9.setHasMultipleMapFlag();
      }

      if (var3.isPrecondition() && var2 != null) {
         var2.setHasPreconditionFlag(true);
      }

      if (this.m_deName != null && this.m_deName.length() > 0) {
         this.m_TreeComponent.m_deHashtable.put(this.m_deName, this);
      }

      this.m_order = this.m_TreeComponent.m_counter++;
      this.m_mappingKits = this.m_TreeComponent.m_bTarget ? new MappingKits(this) : null;
      this.setIdentityFlag();
   }

   public CROM getCROM() {
      return this.m_CROM;
   }

   String getCardinalityPostFix() {
      if (this.isOptional()) {
         return " (Zero or One)";
      } else if (this.isOuterJoin()) {
         return " (Zero or More)";
      } else if (this.isOneToOne()) {
         return " (Once Only)";
      } else {
         return this.isAtLeastOne() ? " (One or More)" : "";
      }
   }

   private boolean isShortValueDisplayWithValueNodeOnly(CROM var1) {
      return this.getTreeComponent().isSimpleXmlElementDisplay() && var1.hasOnlyValueNode();
   }

   boolean isShortValueDisplayWithValueNodeOnly() {
      return this.isShortValueDisplayWithValueNodeOnly(this.m_CROM);
   }

   public TreeMapper getTreeMapper() {
      return this.m_TreeComponent.getTreeMapper();
   }

   String getDefaultValue() {
      return this.m_CROM.getDefaultValue();
   }

   boolean hasDefaultValue() {
      return StringUtils.isNotEmpty(this.getDefaultValue());
   }

   private void addChild(Node var1) {
      this.prepareChildListForNewChild();
      int var2 = 0;
      if (this.m_children.size() > 0 && !var1.isPrecondition()) {
         if (this.isShortValueDisplayWithValueNodeOnly(var1.m_CROM)) {
            var2 = this.m_children.size();
         } else if (var1.m_CROM.isValueLeaf()) {
            if (this.m_children.get(0).isPrecondition()) {
               var2 = 1;
            }
         } else if (var1.m_CROM.isAttributeNode()) {
            for (Node var4 : this.m_children) {
               if (!var4.m_CROM.isLeaf()) {
                  break;
               }

               var2++;
            }
         } else {
            var2 = this.m_children.size();
         }
      }

      this.m_children.add(var2, var1);
   }

   public Iterable<Node> getChildren() {
      return this.m_children;
   }

   public int getChildCount() {
      return this.m_children.size();
   }

   public boolean hasChildren() {
      return !this.m_children.isEmpty();
   }

   int getIndexOfChild(Node var1) {
      return this.m_children.indexOf(var1);
   }

   public Node getChildAt(int var1) {
      return this.m_children.get(var1);
   }

   public Node getFirstChild() {
      return this.m_children.isEmpty() ? null : this.m_children.get(0);
   }

   public Node getFirstChildFolder() {
      for (Node var2 : this.m_children) {
         if (var2.isFolder()) {
            return var2;
         }
      }

      return null;
   }

   Node getLastChild() {
      return this.m_children.isEmpty() ? null : this.m_children.get(this.m_children.size() - 1);
   }

   Node getNextSibling() {
      if (this.m_parent != null && this.m_parent.getLastChild() != this) {
         int var1 = this.m_parent.getIndexOfChild(this) + 1;
         return this.m_parent.getChildAt(var1);
      } else {
         return null;
      }
   }

   Node getPreviousSibling() {
      if (this.m_parent != null && this.m_parent.getFirstChild() != this) {
         int var1 = this.m_parent.getIndexOfChild(this) - 1;
         return this.m_parent.getChildAt(var1);
      } else {
         return null;
      }
   }

   public String getDataType() {
      return this.m_CROM.getValueType();
   }

   public int getDbColType() {
      return this.m_CROM.getSqlDataType();
   }

   public boolean canAddCondition() {
      if (ClientInfo.isDataLoader()) {
         return false;
      } else if (this.getChildCount() == 0) {
         return false;
      } else if (this.getName().equals("_Root")) {
         return false;
      } else {
         return !this.isFolder() ? false : this.isOptional() || this.isLoopNode();
      }
   }

   private String getDataTypeFromDisplayText(String var1) {
      String var2 = StringUtils.substringBetween(var1, "(", ")");
      if (!StringUtils.isNotEmpty(var2)) {
         return null;
      }

      String var3 = "";
      int var4 = var2.indexOf(91);
      int var5 = var2.indexOf(93);
      if (var5 > var4 && var4 >= 0) {
         if (var4 > 0) {
            var3 = var3 + var2.substring(0, var4);
         }

         if (var5 < var2.length() - 1) {
            var3 = var3 + var2.substring(var5 + 1);
         }
      } else {
         int var6 = 0;
         var4 = var2.indexOf(60);
         var5 = var2.indexOf(62);
         if (var5 > var4 && var4 > 0) {
            var3 = var2.substring(0, var4);
            var6 = Integer.parseInt(var2.substring(var4 + 1, var5));
            this.m_CROM.setSqlDataType(var6);
         } else {
            var3 = var2;
         }
      }

      return var3;
   }

   void setCromName(String var1) {
      this.m_CROM.setName(var1);
   }

   public String getDisplayName() {
      return this.m_CROM.getDisplayName();
   }

   public String getName() {
      return this.m_CROM.getName();
   }

   public String getAttributeName() {
      return this.isEVNode() ? this.m_CROM.getParent().getName() : this.getName();
   }

   private void setDefaultValue(String var1) {
      this.m_CROM.setDefaultValue(var1);
   }

   private static boolean eq(String var0, String var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }

   boolean equals(Node var1) {
      if (!var1.m_deName.equals(this.m_deName)) {
         return false;
      }

      if (!eq(var1.getDefaultValue(), this.getDefaultValue())) {
         return false;
      }

      if (!eq(var1.getDomainString(), this.getDomainString())) {
         return false;
      }

      if (!eq(var1.getName(), this.getName())) {
         return false;
      }

      if (this.m_CROM.getType() != var1.m_CROM.getType()) {
         return false;
      }

      Iterator var2 = var1.getChildren().iterator();

      for (Node var4 : this.m_children) {
         if (!var2.hasNext()) {
            return false;
         }

         if (!var4.equals((Node)var2.next())) {
            return false;
         }
      }

      return true;
   }

   public Icon getNodeIcon() {
      assert !this.isFolder() : "Node.getNodeIcon is only implemented for leaf nodes at the moment.";
      if (this.getTreeMapper() != null && (this.isTargetMapped() || this.isSourceUsed())) {
         return this.isFormula() ? TreeMapperIcons.FUNCTION_NODE : TreeMapperIcons.CHECK;
      } else {
         return TreeMapperIcons.LEAF_NODE;
      }
   }

   private boolean isFormula() {
      return (this.m_status & 2048) != 0;
   }

   public String getDomainString() {
      return this.m_CROM.getDomain();
   }

   public String getFormatString() {
      return this.m_CROM.getFormat();
   }

   public int countNodes() {
      int var1 = 1;

      for (Node var3 : this.m_children) {
         var1 += var3.countNodes();
      }

      return var1;
   }

   public boolean isEmpty() {
      if (this.isFolder()) {
         for (Node var2 : this.m_children) {
            if (!var2.isEmpty()) {
               return false;
            }
         }

         return true;
      } else {
         return (this.m_value == null || this.m_value.get().equals("")) && !this.isSelected() && !this.isSourceUsed() && !this.isTargetMapped();
      }
   }

   void changeShowChildren(int var1) {
      this.setShowMode(var1);
      if (this.isFolder() && this.m_children.size() == 0 && var1 == 1) {
         this.setNotExpanded();
      }

      for (Node var3 : this.m_children) {
         if (var3.isFolder()) {
            var3.changeShowChildren(var1);
         }
      }
   }

   void changeShowChildren(boolean var1) {
      this.setShowMode(var1 ? 1 : 2);

      for (Node var3 : this.m_children) {
         if (var3.isFolder()) {
            var3.changeShowChildren(var1);
         }
      }
   }

   void clearSourcePaths() {
      if (!this.isRoot()) {
         this.setSourcePath(null);
      }

      for (Node var2 : this.m_children) {
         var2.clearSourcePaths();
      }
   }

   void clearUsedFlags() {
      this.setSourceUsedFlag(false);

      for (Node var2 : this.m_children) {
         var2.clearUsedFlags();
      }
   }

   void clearCheckFlags() {
      this.setTargetMappedFlag(false);

      for (Node var2 : this.m_children) {
         var2.clearCheckFlags();
      }
   }

   public boolean missingRequiredMappings() {
      if (this.hasMapWarning()) {
         return true;
      }

      for (Node var2 : this.m_children) {
         if (var2.missingRequiredMappings()) {
            return true;
         }
      }

      return false;
   }

   public void collectSourcePathConflict(List<MappingConflictChecker> var1) {
      if (this.m_deName != null && this.isLoopNode() || this.isRoot()) {
         if (!this.hasMappings()) {
            return;
         }

         MappingConflictChecker var2 = new MappingConflictChecker(this.getTreeMapper(), this);
         var1.add(var2);
      }

      for (Node var3 : this.m_children) {
         if (var3.isFolder()) {
            var3.collectSourcePathConflict(var1);
         }
      }
   }

   private boolean hasMappings() {
      if (this.m_children == null) {
         return false;
      }

      if (!this.m_pathList.isEmpty()) {
         return true;
      }

      for (Node var2 : this.m_children) {
         if (var2.isFolder() && var2.hasMappings()) {
            return true;
         }
      }

      return false;
   }

   int reOrder(int var1) {
      this.m_order = ++var1;
      if (this.isFolder()) {
         for (Node var3 : this.m_children) {
            var1 = var3.reOrder(var1);
         }
      }

      return var1;
   }

   boolean canHavePrecondition() {
      if (!this.isFolder()) {
         return false;
      } else {
         return this.hasPrecondition() ? false : this.m_deName.startsWith("[") && this.m_deName.endsWith("]");
      }
   }

   Node createPreConditionNode(String var1) {
      String var2 = this.getConditionOrScriptName();
      Node var3 = new Node(this.m_TreeComponent, this, 0, var2, false, var1, "P", false);
      var3.setPreconditionFlag();
      return var3;
   }

   public Node insertDerivedTypeNode(CROM var1, String var2) {
      if (!this.isFolder()) {
         return null;
      }

      Stack var3 = new Stack();
      CROM var4 = new CROM(this.m_CROM, var1, this.m_TreeComponent.xsdExpandMinimum, this.m_TreeComponent.m_CROM_Tree, var3, "");
      var4.resetSubstitutionGroupHead();
      if (var2 != null) {
         var4.setName(var2);
      }

      Node var5 = this.createChildrenFromCROM(var4, var3, "");
      this.setFullyExpanded();
      var5.setFullyExpanded();
      this.m_TreeComponent.setDirty();
      if (this.getTreeMapper() != null) {
         this.getTreeMapper().setDirty(true);
      }

      this.m_TreeComponent.repaint();
      return var5;
   }

   public Node insertSubstitutionGroupNode(CROM var1, String var2) {
      if (!this.isFolder()) {
         return null;
      }

      Stack var3 = new Stack();
      CROM var4 = new CROM(this.m_CROM, var1, this.m_TreeComponent.xsdExpandMinimum, this.m_TreeComponent.m_CROM_Tree, var3, "");
      var4.setType(1L);
      if (var2 != null) {
         var4.setName(var2);
      }

      Node var5 = this.createChildrenFromCROM(var4, var3, "");
      this.setFullyExpanded();
      var5.setFullyExpanded();
      this.m_TreeComponent.setDirty();
      if (this.getTreeMapper() != null) {
         this.getTreeMapper().setDirty(true);
      }

      this.m_TreeComponent.repaint();
      return var5;
   }

   public Node expandHoldersTypeNode(String var1, String var2) {
      if (this.getTreeMapper() == null && this.getTreeMapperMigration() == null) {
         return null;
      }

      if (!this.isFolder()) {
         return null;
      }

      Node var3 = null;
      if (this.isSubstitutionGroupHeader()) {
         CROM var4 = CROM.getSubstitutionGroupType(this.m_TreeComponent.m_CROM_Tree, var1);
         if (var4 != null) {
            var3 = this.insertSubstitutionGroupNode(var4, var2);
         }
      } else if (this.isDerivedTypeHolder()) {
         CROM var5 = CROM.getDerivedType(this.m_TreeComponent.m_CROM_Tree, var1);
         if (var5 != null) {
            var3 = this.insertDerivedTypeNode(var5, var2);
         }
      }

      return var3;
   }

   private void removeFromHashTable(Node var1) {
      String var2 = var1.m_deName;
      if (var2 != null) {
         this.m_TreeComponent.m_deHashtable.remove(var2);
         if (var1.m_children != null) {
            for (Node var4 : var1.m_children) {
               this.removeFromHashTable(var4);
            }
         }
      }
   }

   public void removeChild(Node var1) {
      if (var1.m_children != null) {
         this.removeFromHashTable(var1);
         this.m_CROM.removeChild(var1.m_CROM);
         this.m_children.remove(var1);
         this.m_TreeComponent.setDirty();
      }
   }

   public void deletePreCondition() {
      if (this.getTreeMapper() != null) {
         Node var1 = this.getPrecondition();
         if (var1 != null) {
            if (!var1.isTargetMapped()) {
               this.m_children.remove(var1);
               this.m_CROM.removeChild(var1.m_CROM);
               this.setHasPreconditionFlag(false);
               this.getTreeMapper().removeTargetNode(var1);
               this.m_TreeComponent.setDirty();
               this.m_TreeComponent.repaint();
            } else {
               MappingManager var2 = this.getTreeMapper().getMappingManager();
               NodeMapping var3 = var2.findMapping(var1.m_deName);
               var2.changeHighLighted(var3);
               this.deleteMappedPreCondition(var3);
            }
         }
      }
   }

   private Node getPrecondition() {
      for (Node var2 : this.m_children) {
         if (var2.isPrecondition()) {
            return var2;
         }
      }

      return null;
   }

   private void deleteMappedPreCondition(NodeMapping var1) {
      TreeMapper var2 = var1.getTreeMapper();
      NodePath var3 = var1.getTargetPath();
      Node var4 = var2.findTargetNode(var3);
      Node var5 = var4.m_parent;
      var2.getMappingManager().deleteMapping(var1);
      var2.updateMapAreaTables(var3.toString(), null);
      Node var6 = var5.getPrecondition();

      try {
         var6.updateMappingKits("");
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      if (var6 != null) {
         var5.m_children.remove(var6);
         var5.m_CROM.removeChild(var6.m_CROM);
         var5.setHasPreconditionFlag(false);
         var2.removeTargetNode(var6);
         var2.m_targetTreeComponent.repaint();
      }
   }

   private String getMultipleMappingName(int var1) {
      String var2 = StringUtils.substringBefore(this.getName(), "#");
      return var1 == 0 ? var2 : var2 + "#" + var1;
   }

   Node insertElement(Node var1, int var2, int var3) {
      this.prepareChildListForNewChild();
      Node var4 = var2 <= 0 ? null : this.m_children.get(var2 - 1);
      CROM var5 = null;
      if (var4 != null) {
         var5 = var4.m_CROM;
         if (this.isShortValueDisplayWithValueNodeOnly(var5)) {
            var5 = var5.getParent();
         }
      }

      Node var6 = new Node(this.m_TreeComponent, this, var1, var5, var3);
      this.m_children.add(var2, var6);
      if (!var1.isSubstitutionGroupHeader() && !var1.isDerivedTypeHolder()) {
         int var7 = 0;
         if (this.m_TreeComponent.m_flat) {
            for (Node var9 : var1.m_children) {
               Node var10 = new Node(this.m_TreeComponent, var6, var9, var6.m_CROM.getChild(var7), var3);
               var6.addChildAtPosition(var7, var10);
               var7++;
            }
         } else {
            for (Node var14 : var1.m_children) {
               if (!var14.isFolder()) {
                  CROM var15 = var14.getCROM();
                  if (this.isShortValueDisplayWithValueNodeOnly(var15)) {
                     CROM var11 = new CROM(var6.m_CROM, var15.getParent(), var6.m_CROM.getLastChild());
                     CROM var12 = new CROM(var11, var15, null);
                     new Node(this.m_TreeComponent, var6, var12);
                  } else {
                     Node var17 = new Node(this.m_TreeComponent, var6, var14, var6.m_CROM.getChild(var7), 0);
                     var6.addChildAtPosition(var7, var17);
                  }

                  var7++;
               } else if (!var14.isMultipleMap()) {
                  Node var16 = var6.insertElement(var14, var7, 0);
                  if (var16 == null) {
                     return null;
                  }

                  var7++;
               }
            }
         }

         return var6;
      } else {
         return var6;
      }
   }

   public Node findMultipleMappingExist(int var1) {
      String var2 = this.getDisplayName();

      for (Node var4 : this.m_parent.m_children) {
         if (var4.getDisplayName().equals(var2) && var4.getMultipleMappingNumber() == var1) {
            return var4;
         }
      }

      return null;
   }

   public int getMultipleMappingNumber() {
      String var1 = StringUtils.substringAfter(this.getName(), "#");
      return StringUtils.isEmpty(var1) ? 0 : Integer.parseInt(var1);
   }

   public Node findLastMultipleMapping(int var1) {
      String var2 = this.getDisplayName();
      if (this.m_parent == null) {
         return null;
      }

      int var3 = 0;
      Node var4 = null;

      for (Node var6 : this.m_parent.m_children) {
         String var7 = var6.getDisplayName();
         if (var2.equals(var7)) {
            if (var4 == null) {
               var4 = var6;
               var3 = var6.getMultipleMappingNumber();
            } else {
               int var8 = var6.getMultipleMappingNumber();
               if (var8 == var1) {
                  return null;
               }

               if (var8 >= var3 && (var1 < 0 || var8 < var1)) {
                  var3 = var8;
                  var4 = var6;
               }
            }
         }
      }

      return var4;
   }

   public Node insertMultipleMapping(int var1) {
      Node var2 = this.findLastMultipleMapping(var1);
      if (var2 == null) {
         return null;
      }

      int var3 = this.m_parent.getIndexOfChild(var2) + 1;
      Node var4 = this.m_parent.insertElement(this, var3, var1);
      String var5 = this.m_display;
      int var6 = 0;
      int var8 = 0;
      int var7;
      if ((var7 = var5.indexOf(35)) >= 0) {
         var8 = var5.length();
         var6 = var7 + 1;

         while (var6 < var8 && Character.isDigit(var5.charAt(var6))) {
            var6++;
         }

         if (var6 == var8 && var8 > var7) {
            var5 = var5.substring(0, var7);
         }
      }

      var4.m_display = var5 + "#" + var1;
      this.m_TreeComponent.m_root.reOrder(0);
      if (this.getTreeMapper() != null) {
         this.getTreeMapper().getMappingManager().reOrder();
      }

      var4.setMultipleMapFlag();
      this.setHasMultipleMapFlag();
      this.m_TreeComponent.setDirty();
      this.m_TreeComponent.repaint();
      return var4;
   }

   public void deleteMultipleMapped() {
      if (this.getTreeMapper() != null) {
         if (this.isMultipleMap()) {
            TreeMapper var1 = this.getTreeMapper();
            this.removeMappingsUnderFolder();
            this.m_parent.m_CROM.removeChild(this.m_CROM);
            this.m_parent.m_children.remove(this);
            var1.removeTargetNode(this);
         }

         this.m_TreeComponent.repaint();
      }
   }

   public void removeMappingsUnderFolder() {
      TreeMapper var1 = this.getTreeMapper();
      if (this.anyChildUsed()) {
         this.removeEntireMappedFolder();
         var1.updateMappingFlag();
      } else {
         if (var1.getMappingManager().deleteMapping(new NodePath(this.m_deName))) {
            var1.updateMappingFlag();
            var1.updateMapAreaTables(this.m_deName, null);
         }

         this.m_TreeComponent.setDirty();
      }
   }

   private boolean anyChildUsed() {
      if (this.getTreeMapper() == null) {
         return false;
      }

      for (Node var2 : this.m_children) {
         if (!var2.isFolder() && var2.isTargetMapped()) {
            return true;
         }

         if (var2.isFolder() && var2.anyChildUsed()) {
            return true;
         }
      }

      return false;
   }

   private void removeEntireMappedFolder() {
      if (this.getTreeMapper() != null) {
         if (this.getTreeMapper().deleteMapping(new NodePath(this.m_deName))) {
            this.getTreeMapper().updateMapAreaTables(this.m_deName, null);
         }

         for (Node var2 : this.m_children) {
            if (!var2.isFolder() && var2.isTargetMapped()) {
               this.getTreeMapper().deleteMapping(new NodePath(var2.m_deName));
               this.getTreeMapper().updateMapAreaTables(var2.m_deName, null);
               var2.updateMappingKits("");
            } else if (var2.isFolder()) {
               var2.removeEntireMappedFolder();
            }
         }
      }
   }

   Node getImmediateRootLoopNode() {
      Node var1 = this.m_parent;
      if (var1 == null) {
         return null;
      }

      while (!var1.isLoopNode() && var1.m_parent != null) {
         var1 = var1.m_parent;
      }

      return var1;
   }

   public Node getRootLoopNode() {
      Node var1 = this.isFolder() ? this : this.m_parent;

      while (var1 != null && !var1.isLoopNode()) {
         var1 = var1.m_parent;
      }

      if (var1 != null) {
         return var1;
      }

      if (GlobalDE.isGlobalDE(this.m_deName)) {
         return null;
      }

      if (this.m_TreeComponent.m_root.isRoot()) {
         return this.m_TreeComponent.m_root;
      }

      for (Node var3 : this.m_TreeComponent.m_root.m_children) {
         if (var3.isRoot()) {
            return var3;
         }
      }

      return null;
   }

   public boolean isFolder() {
      return !this.m_CROM.isLeaf();
   }

   boolean isBold() {
      return (this.m_status & 128) != 0;
   }

   private void setBoldFlag(boolean var1) {
      if (var1) {
         this.m_status |= 128;
      } else {
         this.m_status &= -129;
      }
   }

   boolean isBatchNode() {
      return (this.m_status & 1048576) != 0;
   }

   public void setBatchNodeFlag(boolean var1) {
      if (var1) {
         this.m_status |= 1048576;
      } else {
         this.m_status &= -1048577;
      }
   }

   public boolean isLoopNode() {
      return this.m_CROM.isLoopNode();
   }

   public boolean isRootLoopNode() {
      return this.m_CROM.isRootLoopNode();
   }

   public boolean isSourceUsed() {
      return (this.m_status & 8192) != 0;
   }

   void setSourceUsedFlag(boolean var1) {
      if (var1) {
         this.m_status |= 8192;
      } else {
         this.m_status &= -8193;
      }
   }

   void updateUsedFlag() {
      this.setSourceUsedFlag(!this.m_pathList.isEmpty());
   }

   public boolean isTargetMapped() {
      return (this.m_status & 4096) != 0;
   }

   public void setTargetMappedFlag(boolean var1) {
      if (var1) {
         this.m_status |= 4096;
      } else {
         this.m_status &= -4097;
      }
   }

   void setMultipleMapFlag() {
      this.m_status |= 16384;
   }

   void setHasMultipleMapFlag() {
      this.m_status |= 32768;
   }

   public boolean isMultipleMap() {
      return (this.m_status & 16384) != 0;
   }

   public boolean hasMultipleMap() {
      return (this.m_status & 32768) != 0;
   }

   public boolean isAtLeastOne() {
      return this.m_CROM.isAtLeastOneNode();
   }

   public boolean isOneOf() {
      return this.m_CROM.isOneOfNode();
   }

   public boolean isDerivedTypeHolder() {
      return this.m_CROM.isDerivedTypeHolder();
   }

   public boolean isEmptyDerivedTypeHolder() {
      return this.isDerivedTypeHolder() && (!this.hasChildren() || this.hasOnlyXsiNilChild());
   }

   public boolean isTypeDefinition() {
      return this.m_CROM.isTypeDefinition();
   }

   public boolean isSubstitutionGroupHeader() {
      return this.m_CROM.isSubstitutionGroupHead();
   }

   public boolean isEmptySubstitutionGroupHeader() {
      return this.isSubstitutionGroupHeader() && (!this.hasChildren() || this.hasOnlyXsiNilChild());
   }

   boolean isDeleteAttribute() {
      return this.m_TreeComponent.m_bIsLdapTarget && (this.m_CROM.getType() & 256L) != 0L;
   }

   public boolean isSubstitutionGroup() {
      return this.m_CROM.isSubstitutionGroup();
   }

   public boolean isOptional() {
      return this.m_CROM.isOptionalNode();
   }

   public void setMapped() {
      this.m_status |= 131072;
   }

   void clearMapped() {
      this.m_status &= -131073;
   }

   private boolean mapped() {
      return (this.m_status & 131072) != 0;
   }

   private void setMapWarning() {
      this.m_status |= 65536;
   }

   private void resetMapWarning() {
      this.m_status &= -65537;

      for (Node var2 : this.m_children) {
         if (var2.isFolder()) {
            var2.resetMapWarning();
         } else {
            var2.m_status &= -65537;
         }
      }
   }

   boolean hasMapWarning() {
      return (this.m_status & 65536) != 0;
   }

   private void setIdentityFlag() {
      if (!this.isFolder()) {
         String var1 = this.getDataType();
         if (var1 != null && (var1.indexOf("identity") >= 0 || var1.indexOf("serial") >= 0)) {
            this.m_status |= 4194304;
         }
      }
   }

   private void setHidden(boolean var1) {
      if (var1) {
         this.m_status |= 2097152;
      } else {
         this.m_status &= -2097153;
      }
   }

   public int findLinkedColumns() {
      int var1 = 0;

      for (Node var3 : this.getChildren()) {
         if (var3.isFolder()) {
            break;
         }

         if (var3.isLinkKey()) {
            CROM var4 = var3.getCROM();
            String var5 = var4.getLinkToPrimaryKey();
            Node var6 = this.m_parent.findLeaf(var5);
            assert var6 != null;
            if (!var6.isLinkedColumn()) {
               var1++;
               var6.setLinkedColumn();
            }
         }
      }

      for (Node var8 : this.getChildren()) {
         if (var8.isFolder()) {
            var1 += var8.findLinkedColumns();
         }
      }

      return var1;
   }

   public boolean isHidden() {
      return (this.m_status & 2097152) != 0;
   }

   public void clearRequireMappingFlag() {
      this.m_status &= -458753;

      for (Node var2 : this.m_children) {
         var2.clearRequireMappingFlag();
      }
   }

   public void setRequireMappingFolders() {
      for (Node var1 = this.m_parent; var1 != null; var1 = var1.m_parent) {
         var1.m_status |= 262144;
         var1.setRequireMappingFolders();
      }
   }

   private boolean mapped_() {
      if (!this.mapped() && !this.isSourceUsed()) {
         for (Node var2 : this.m_children) {
            if (var2.mapped_()) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   public void clearMappedHiddenTypes() {
      if (this.isTypeDefinition() || this.isSubstitutionGroup()) {
         boolean var1 = this.mapped_();
         if (var1) {
            this.setHidden(false);
         }
      }

      for (Node var2 : this.m_children) {
         var2.clearMappedHiddenTypes();
      }
   }

   private boolean isRequireMapping() {
      if (this.isOptional()) {
         return false;
      } else if (this.getDefaultValue() != null && !this.getDefaultValue().isEmpty()) {
         return false;
      } else if (this.isLinkKey()) {
         return false;
      } else if (this.isPrecondition()) {
         return false;
      } else if (this.isPrimaryKey()) {
         return false;
      } else {
         return this.isIdentity() ? false : !this.isEVNode() || !this.m_CROM.isParentOptionalNode();
      }
   }

   public boolean updateRequireMappingFlag(Set<String> var1) {
      boolean var2 = false;
      int var3 = 0;
      String var4 = null;
      if (this == this.m_TreeComponent.m_root || this.requireMapping() || !this.isOptional()) {
         if (this.m_children.isEmpty() && !var1.contains(this.getName())) {
            this.expandNode();
            var4 = this.getName();
            var1.add(var4);
         }

         for (Node var6 : this.m_children) {
            if (var6.isFolder()) {
               if (var6.requireMapping() || !var6.isOptional() && !var6.isOuterJoin()) {
                  boolean var7 = var6.updateRequireMappingFlag(var1);
                  if (var7) {
                     var2 = true;
                  } else {
                     var3++;
                  }
               }
            } else if (!var6.isRequireMapping() && !var6.isLinkedColumn()) {
               var3++;
            } else {
               var6.m_status |= 262144;
               if (!var6.mapped() && !var6.isOptional() && !var6.isLinkedColumn()) {
                  var6.setMapWarning();
                  var2 = true;
               } else {
                  var3++;
               }
            }
         }

         if ((this.isOneOf() || this.isDerivedTypeHolder() || this.isSubstitutionGroupHeader()) && var3 > 0) {
            var2 = false;

            for (Node var9 : this.m_children) {
               var9.resetMapWarning();
            }
         }
      }

      if (var2) {
         this.setMapWarning();
      }

      if (var4 != null) {
         var1.remove(var4);
      }

      return var2;
   }

   public int printLinkedColumn(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         System.out.println("printLinkedColumn_start - " + var1);
      }

      int var2 = 0;
      if (this.isLinkedColumn()) {
         var2++;
         System.out.println("linkColumn - " + this.m_deName);
      }

      for (Node var4 : this.getChildren()) {
         var2 += var4.printLinkedColumn("");
      }

      if (var1 != null && !var1.isEmpty()) {
         System.out.println("printLinkedColumn_end - " + var2 + " links");
      }

      return var2;
   }

   private boolean requireMapping() {
      return (this.m_status & 262144) != 0;
   }

   private boolean isIdentity() {
      return (this.m_status & 4194304) != 0;
   }

   private boolean isPrimaryKey() {
      return this.m_CROM.isPrimaryKey();
   }

   public boolean isLinkKey() {
      return this.m_CROM.isForeignKey();
   }

   public boolean isLinkedColumn() {
      return (this.m_status & 64) != 0;
   }

   public void setLinkedColumn() {
      this.m_status |= 64;
   }

   private boolean isOneToOne() {
      return this.m_CROM.isOneToOne();
   }

   private boolean isOuterJoin() {
      return this.m_CROM.isOuterJoin();
   }

   public void setHasPreconditionFlag(boolean var1) {
      if (var1) {
         this.m_status |= 512;
      } else {
         this.m_status &= -513;
      }
   }

   public boolean hasPrecondition() {
      return (this.m_status & 512) != 0;
   }

   private void setPreconditionFlag() {
      this.m_CROM.setPrecondition();
   }

   public boolean isPrecondition() {
      return this.m_CROM.isPrecondition();
   }

   public boolean isRoot() {
      return (this.m_CROM.getType() & 32768L) != 0L;
   }

   public CROM getRootDtd() {
      if (this.m_CROM != null && this.m_CROM.hasChildren()) {
         return this.m_CROM;
      }

      Node var1 = this.getFirstChild();
      return var1 != null && var1.m_CROM != null ? var1.m_CROM : null;
   }

   public void setFormulaFlag(boolean var1) {
      if (var1) {
         this.m_status |= 2048;
      } else {
         this.m_status &= -2049;
      }
   }

   void setSelectedFlag(boolean var1) {
      if (var1) {
         this.m_status |= 524288;
      } else {
         this.m_status &= -524289;
      }
   }

   boolean isSelected() {
      return (this.m_status & 524288) != 0;
   }

   void setFullyExpanded() {
      this.setShowMode(1);
   }

   void setNotExpanded() {
      this.setShowMode(0);
   }

   int showChildrenMode() {
      return this.m_status & 3;
   }

   void clearHiddenFlags() {
      for (Node var1 = this; var1 != null; var1 = var1.m_parent) {
         var1.setHidden(false);
      }
   }

   public void updateHiddenFlags() {
      boolean var1 = this.m_TreeComponent.isHidden(this);
      this.setHidden(var1);

      for (Node var3 : this.m_children) {
         var3.updateHiddenFlags();
      }
   }

   void setNextShowMode() {
      int var1 = ((this.m_status & 3) + 1) % 2;
      this.setShowMode(var1);
   }

   Node createChildrenFromCROM(CROM var1, Stack<String> var2, String var3) {
      if (this.m_TreeComponent.m_bIsLdapTarget && var1.isReadOnly()) {
         return null;
      }

      if (var2 == null) {
         var2 = new Stack();
      }

      if (this.isShortValueDisplayWithValueNodeOnly(var1)) {
         var1 = var1.getChild(0);
      } else if (!var1.hasChildren()) {
         String var4 = var1.getSchemaTypeExpandedName();
         if (var4 != null) {
            CROM var5 = this.m_TreeComponent.getElementTypeMap().get(var4);
            if (var5 != null) {
               TreeMapper var6 = this.getTreeMapper();
               boolean var7 = var6 == null ? false : var6.getNodeExpansionMode() == NodeExpansionMode.MINIMAL;
               int var8 = var1.explodeXmlComplexType(var5, var7, this.m_TreeComponent.m_CROM_Tree, var2, var3);
               if (var8 > 0) {
                  this.m_TreeComponent.setDirty();
               }
            }
         }
      }

      Node var9 = new Node(this.m_TreeComponent, this, var1);
      var9.createChildNodes(var3);
      return var9;
   }

   public boolean hasOnlyXsiNilChild() {
      if (this.m_children.size() == 1) {
         Node var1 = this.m_children.get(0);
         return var1.getName().equals("xsi:nil");
      } else {
         return false;
      }
   }

   private int createChildNodes(String var1) {
      if (this.m_children.size() > 1) {
         return 0;
      }

      if (this.m_children.size() == 1) {
         Node var2 = this.m_children.get(0);
         if (!var2.getName().equals("xsi:nil")) {
            return 0;
         }
      }

      CROM var8 = this.m_CROM.getChild(0);
      if (var8 == null || !var8.getName().equals("xsi:nil")) {
         var8 = null;
      } else if (this.m_children.size() == 0) {
         new Node(this.m_TreeComponent, this, var8);
      }

      if (!this.m_CROM.hasMoreThanNilChildren()) {
         return 0;
      }

      int var3 = 0;
      String var4 = var1 + "  ";

      for (CROM var6 : this.m_CROM.getChildren()) {
         if ((!this.m_TreeComponent.m_bIsLdapTarget || !var6.isReadOnly()) && var6 != var8) {
            var3++;
            if (this.isShortValueDisplayWithValueNodeOnly(var6)) {
               new Node(this.m_TreeComponent, this, var6.getChild(0));
            } else {
               Node var7 = new Node(this.m_TreeComponent, this, var6);
               if (var7.isFolder()) {
                  var3 += var7.createChildNodes(var4);
               }
            }
         }
      }

      return var3;
   }

   public void expandAll() {
      this.expandNode();
      System.out.println(this);

      for (Node var2 : this.getChildren()) {
         if (var2.isFolder()) {
            var2.expandAll();
         }
      }
   }

   public void expandNode() {
      if (this.isFolder()) {
         if (!this.hasOtherChildrenThanXsdNil()) {
            boolean var1 = this.getChildCount() == 1;
            this.m_CROM.expandNode(this.m_TreeComponent.m_CROM_Tree);
            if (this.m_CROM.isDerivedTypeHolder() || this.m_CROM.isSubstitutionGroupHead()) {
               this.setDisplay(this.m_CROM.getDisplayText());
            }

            if (this.isShortValueDisplayWithValueNodeOnly(this.m_CROM)) {
               this.turnOnShortDisplayEVNode();
            } else {
               for (CROM var3 : this.m_CROM.getChildren()) {
                  if ((!this.m_TreeComponent.m_bIsLdapTarget || !var3.isReadOnly()) && (!var1 || !var3.isNilAttributeCrom())) {
                     if (this.isShortValueDisplayWithValueNodeOnly(var3)) {
                        new Node(this.m_TreeComponent, this, var3.findValueNode());
                     } else {
                        new Node(this.m_TreeComponent, this, var3);
                     }
                  }
               }
            }
         }
      }
   }

   private boolean hasOtherChildrenThanXsdNil() {
      int var1 = this.m_children.size();
      switch (var1) {
         case 0:
            return false;
         case 1:
            CROM var2 = this.m_children.get(0).m_CROM;
            return var2 == null || !var2.getName().equals("xsi:nil");
         default:
            return true;
      }
   }

   public Node findChildFolder(String var1) {
      for (Node var3 : this.m_children) {
         if (var3.isFolder()) {
            if (var3.getName().equals(var1)) {
               return var3;
            }
         } else if (var3.m_CROM.isValueLeaf()) {
            CROM var4 = var3.m_CROM.getParent();
            if (this.getTreeComponent().isSimpleXmlElementDisplay() && var4 != null && !var4.isLoopNode() && var4.getName().equals(var1)) {
               return var3;
            }
         }
      }

      return null;
   }

   Node findLeaf(String var1) {
      if (!this.hasChildren()) {
         return null;
      }

      if (var1 == null || !var1.equals("/PRECONDITION/") && !var1.equals("/PRESCRIPT/")) {
         if (var1 != null && var1.length() != 0) {
            for (Node var3 : this.m_children) {
               if (!var3.isFolder() && (var3.getName().equals(var1) || var3.getDisplayName().equals(var1))) {
                  return var3;
               }
            }

            return null;
         } else {
            Node var4 = this.m_children.get(0);
            if (var4.isPrecondition()) {
               var4 = this.m_children.get(1);
            }

            return var4.getName().length() == 0 ? var4 : null;
         }
      } else {
         Node var2 = this.m_children.get(0);
         return var2.isPrecondition() ? var2 : null;
      }
   }

   void write(String var1) {
      System.out.println(var1 + "[Node]:" + this.m_display);
      var1 = "  " + var1;

      for (Node var3 : this.m_children) {
         if (!var3.isFolder()) {
            System.out.println(var1 + "[attr]:" + var3.m_display);
         } else {
            var3.write(var1);
         }
      }
   }

   public String getValue() {
      return this.m_value.get();
   }

   public String getRawValue() {
      return this.m_CROM.getValue();
   }

   public void copyValueToClipboard() {
      ClipboardUtils.copyToSystemClipboard(this.m_CROM.getValue());
   }

   public void copyNodeNameToClipboard() {
      ClipboardUtils.copyToSystemClipboard(this.m_CROM.getName());
   }

   public boolean hasValue() {
      return !this.m_value.isEmpty();
   }

   private void setValue(String var1) {
      this.m_CROM.setValue(var1);
      this.m_value = NodeValue.create(var1);
   }

   void resetDataNode() {
      for (Node var2 : this.m_children) {
         if (var2.isFolder()) {
            var2.m_idatanode = -1;
            var2.resetDataNode();
         }

         var2.setValue(null);
      }
   }

   void setEVNodeValue(DocReader var1, Node var2) throws IOException {
      assert var2 != null;
      CROM var3 = var2.m_CROM;
      assert var3 != null;
      CROM var4 = var3.getParent();
      String var5 = null;
      if (var4.isAnyElement()) {
         JbNode var6 = this.m_jbNode.getChild(var4.getName());
         var5 = var1.parser.getStr(var6.getPos(), var6.getLength());
      } else {
         JbNode var10 = null;
         JbNode var7 = var2.m_parent.m_jbNode;
         if (var7 != null) {
            var10 = var7.getChild(var3.getParent().getName());
         }

         JbNode var8 = null;
         if (var10 != null) {
            if (var3.isLdap()) {
               List var9 = var1.parser.splitValues(var10, false);
               if (var9 != null && var9.size() > 0) {
                  var5 = (String)var9.get(0);
               }
            } else {
               var8 = var10.getValueNode();
               if (var8 != null) {
                  var5 = var1.parser.getStr(var8.getPos(), var8.getLength());
               }
            }
         }
      }

      if (var5 != null && KongaStringUtils.hasXmlEscape(var5)) {
         var5 = StringEscapeUtils.unescapeXml(var5);
      }

      var3.setValue(var5);
      var2.m_value = NodeValue.create(var5);
   }

   void setElementAttributes(DocReader var1) throws IOException {
      if (this.m_CROM != null && var1 != null) {
         if (this.isLoopNode()) {
            if (this.m_jbNode != null && this.m_jbNode.getParent() != null && this.m_jbNode.getParent().getChildren() != null) {
               this.m_idatanode = this.m_jbNode.getInstanceNumber();
               this.m_value = NodeValue.create(" " + (this.m_idatanode + 1) + " of " + this.m_jbNode.getParent().getChildren().size());
            } else {
               this.m_CROM.setValue("");
               this.m_value = NodeValue.create("no instance");
            }
         }

         List var2 = null;
         if (this.m_jbNode != null) {
            var2 = var1.parser.splitValues(this.m_jbNode, false);
         }

         Iterator var3 = this.m_CROM.getChildren().iterator();
         CROM var4 = CROM.getNextValueAttribute(var3);
         if (var2 != null) {
            for (Object __etlItem1889 : var2) {
               String var6 = (String) __etlItem1889;
               if (var4 == null) {
                  break;
               }

               var4.setValue(var6);
               var4 = CROM.getNextValueAttribute(var3);
            }
         }

         while (var4 != null) {
            var4.setValue(null);
            var4 = CROM.getNextValueAttribute(var3);
         }

         for (Node var9 : this.m_children) {
            if (!var9.isFolder() && !var9.isPrecondition()) {
               if (var9.isEVNode()) {
                  this.setEVNodeValue(var1, var9);
               } else {
                  String var7 = null;
                  if (this.m_CROM.isAnyElement()) {
                     if (this.m_jbNode != null) {
                        var7 = var1.parser.getStr(this.m_jbNode.getPos(), this.m_jbNode.getLength());
                     }
                  } else {
                     var7 = var9.m_CROM.getValue();
                  }

                  var9.m_CROM.setValue(var7);
                  var9.m_value = NodeValue.create(var7);
               }
            }
         }
      }
   }

   private void initDataInstance_recursive() {
      if (this.m_CROM.isLoopNode()) {
         JbNode var1 = this.m_parent.m_jbNode;
         if (var1 == null) {
            this.m_jbNode = null;
         } else {
            JbNode var2 = var1.getChild(this.m_CROM.getName());
            if (var2 == null) {
               this.m_jbNode = null;
            } else if (var2.getChildren().size() > 0) {
               this.m_jbNode = var2.getChildren().get(0);
               this.m_idatanode = 0;
               this.expandNode();
               this.updateSourceLinkedJbNode();
            } else {
               this.m_jbNode = null;
            }
         }
      } else if (this.m_parent != null) {
         JbNode var3 = this.m_parent.m_jbNode;
         if (var3 == null) {
            this.m_jbNode = null;
         } else {
            this.m_jbNode = var3.getChild(this.m_CROM.getName());
            this.expandNode();
         }
      }

      if (this.getTreeComponent().m_bTarget && this.m_jbNode != null && this.m_jbNode.m_source != null && this.isLoopNode() && this.getTreeMapper() != null) {
         TreeMapperSourceTree var4 = this.getTreeMapper().m_sourceTreeComponent;
         Node var6 = var4.findNode(this.m_jbNode.m_source);
         if (var6 != null) {
            var6.setJbNode_resursive(this.m_jbNode.m_source, null);
         }
      }

      for (Node var7 : this.m_children) {
         if (var7.isFolder()) {
            var7.initDataInstance_recursive();
         }
      }
   }

   private void updateSourceLinkedJbNode() {
      JbNode var1 = this.m_jbNode.m_source;
      if (var1 != null) {
         TreeMapperSourceTree var2 = this.getTreeMapper().m_sourceTreeComponent;
         Node var3 = var2.findNode(var1);
         JbNode var4 = var3.m_jbNode;
         if (var1 != var4) {
            var2.getDataTree().displayInstance(var2, var3, var1);
         }
      }
   }

   void setJbNode_resursive(JbNode var1, Node var2) {
      this.m_jbNode = var1;
      if (this.m_jbNode != null && this.m_jbNode.isInstance()) {
         this.m_idatanode = this.m_jbNode.getInstanceNumber();
         this.updateSourceLinkedJbNode();
      } else {
         this.m_idatanode = -1;
      }

      for (Node var4 : this.m_children) {
         if (var4.isFolder() && var4 != var2) {
            var4.initDataInstance_recursive();
         }
      }
   }

   Node findCommonJbParent(JbNode var1) {
      if (this.m_parent == null) {
         return this;
      }

      if (var1 == null) {
         return null;
      }

      JbNode var2 = this.m_parent.m_jbNode;
      JbNode var3 = var1.getParent();
      if (var1.isInstance()) {
         var3 = var3.getParent();
      }

      if (var3 != var2) {
         this.m_parent.m_jbNode = var3;
         this.m_parent.setJbNode_resursive(var3, this);
         return this.m_parent.findCommonJbParent(var3);
      } else {
         return this;
      }
   }

   void removeDataNodes() {
      this.m_jbNode = null;
      this.setValue(null);

      for (Node var2 : this.m_children) {
         var2.removeDataNodes();
      }
   }

   int getLocationStatus(int var1) {
      byte var2 = 0;
      if (var1 >= this.m_x0 && var1 <= this.m_x2 && this.isFolder()) {
         var2 = 1;
      } else if (var1 >= this.m_x3 && var1 <= this.m_x5) {
         var2 = 2;
      } else if (var1 >= this.m_x6 && var1 < this.m_x7) {
         var2 = 3;
      } else if (var1 >= this.m_x7 && var1 <= this.m_x8) {
         var2 = 4;
      }

      return var2;
   }

   boolean isShown() {
      Node var1 = this;
      if (!this.isFolder()) {
         var1 = this.m_parent;
         if (var1.isNotExpanded()) {
            return false;
         }
      }

      while (var1 != null) {
         if (var1.isNotExpanded()) {
            return false;
         }

         var1 = var1.m_parent;
      }

      return true;
   }

   public TreeComponent getTreeComponent() {
      return this.m_TreeComponent;
   }

   TreeMapperNode asTreeMapperNode(TreeElement var1) {
      TreeMapperNode var2 = null;
      String var3 = this.getTreeMapperNodeName();
      if (this.isFolder()) {
         TreeElement var4 = null;
         if (var1 == null) {
            var4 = TreeElement.ROOT_FACTORY.newRoot(var3);
         } else {
            ElementCardinality var5 = ElementCardinality.fromTreeNode(this);
            var4 = var1.addElement(var3, var5);
         }

         for (Node var6 : this.m_children) {
            if (!var6.isPrecondition()) {
               var6.asTreeMapperNode(var4);
            }
         }

         var2 = var4;
      } else {
         if (var1 == null) {
            throw new IllegalArgumentException("asTreeMapperNode cannot be called with a null parent on a leaf node.");
         }

         var2 = var1.addAttribute(var3, "", 0, this.getDefaultValue(), true);
      }

      return var2;
   }

   private String getTreeMapperNodeName() {
      return this.getDisplayName();
   }

   public void updateMappingKits(String var1) {
      if (!this.isFolder()) {
         try {
            List var2 = this.getTreeMapper().m_transform.getRpnTokenVector(var1);
            this.getTreeMapper().m_stackVector = var2;
            if (var2 != null) {
               this.m_mappingKits.setSourcePaths(Sets.newHashSet(Transform.getDEVector(var2)));
            } else {
               this.m_mappingKits.removeMapping();
            }
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }
   }

   public void removeMapping() {
      if (!this.isFolder()) {
         try {
            this.m_mappingKits.removeMapping();
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }
   }

   private Migration getTreeMapperMigration() {
      return this.m_TreeComponent.getTreeMapperMigration();
   }

   public List<NodePath> collectAllTargetPaths() {
      ArrayList var1 = Lists.newArrayList();
      this.collectAllTargetPaths(var1);
      return var1;
   }

   private void collectAllTargetPaths(List<NodePath> var1) {
      var1.add(new NodePath(this.m_deName));

      for (Node var3 : this.m_children) {
         if (var3.isFolder()) {
            var3.collectAllTargetPaths(var1);
         }

         if (var3.m_deName != null && var3.m_deName.length() > 0) {
            var1.add(new NodePath(var3.m_deName));
         }
      }
   }

   boolean isXml() {
      return this.m_CROM.isXml();
   }

   @Override
   public String toString() {
      return this.m_deName;
   }

   public String getDisplay() {
      return this.m_display;
   }

   void setDisplay(String var1) {
      this.m_display = var1;
   }

   public void setCmd(String var1) {
      if (var1.equals("Add") || var1.equals("Delete") || var1.equals("Update") || var1.equals("Update Only")) {
         this.m_cmd = var1;
      }
   }

   public boolean isUndefinedDataType() {
      return this.getDisplay().indexOf("(Unknown)") >= 0;
   }

   public boolean isFixedValueAttribute() {
      return this.m_CROM.isFixedValueAttribute();
   }

   public boolean isPopulatedAutomatically() {
      return this.getDisplay().indexOf("(Auto)") >= 0 || this.isIdentifier();
   }

   public boolean isIdentifier() {
      return this.m_CROM != null && !this.isFolder() && this.m_CROM.getValueType() != null ? this.m_CROM.getValueType().equals("Identifier") : false;
   }

   boolean isEVNode() {
      return this.m_parent != null && this.m_CROM.getParent() == this.m_parent.m_CROM ? false : this.isShortValueDisplayWithValueNodeOnly(this.m_CROM);
   }

   public boolean mapDisabled() {
      return LdapUtils.mapDisabled(this.m_TreeComponent, this) || this.isFixedValueAttribute();
   }

   public void addExpandedNodes(Set<String> var1) {
      if (this.isFullyExpanded()) {
         var1.add(this.m_deName);
      }

      for (Node var3 : this.m_children) {
         if (var3.isFolder()) {
            var3.addExpandedNodes(var1);
         }
      }
   }

   boolean isFullyExpanded() {
      return (this.m_status & 1) != 0;
   }

   boolean isPartiallyExpanded() {
      return (this.m_status & 2) != 0;
   }

   boolean isNotExpanded() {
      return !this.isExpanded();
   }

   boolean isExpanded() {
      return (this.m_status & 3) != 0;
   }

   public boolean isRecursive() {
      return !this.isFolder() ? false : this.m_CROM.getParent_recursive(this.m_CROM.getName()) != null;
   }

   void setShowMode(int var1) {
      this.m_status &= -4;
      this.m_status |= var1 & 3;
   }

   public void restoreExpandedNodes(Set<String> var1, int var2) {
      if (var1.isEmpty()) {
         if (var2 < 2) {
            this.setShowMode(1);
         }
      } else if (var1.contains(this.m_deName)) {
         this.setShowMode(1);
      }

      for (Node var4 : this.m_children) {
         if (var4.isFolder()) {
            var4.restoreExpandedNodes(var1, var2 + 1);
         }
      }
   }

   public String getRootLoopGenerator() {
      try {
         return this.isRootLoopNode() ? this.m_mappingKits.getGenerator() : this.m_mappingKits.getRootLoopParent().getGenerator();
      } catch (Exception var2) {
         return "";
      }
   }

   public Node getValueNode() {
      for (Node var2 : this.m_children) {
         if (var2.m_CROM.isValueLeaf()) {
            return var2;
         }
      }

      return null;
   }

   public boolean isValueLeaf() {
      return this.m_CROM.isValueLeaf();
   }

   public String getMappingName() {
      String var1 = this.getName();
      assert var1 != null;
      if (var1.isEmpty()) {
         if (this.getDisplay().indexOf("[EV") >= 0) {
            var1 = this.getCROM().getParent().getName();
         } else if (this.m_parent.getName().equals("ids")) {
            var1 = "Id";
         }
      }

      int var2;
      if ((var2 = var1.indexOf(58)) > 0) {
         var1 = var1.substring(var2 + 1);
      }

      return var1;
   }

   Iterable<String> getPathList() {
      return this.m_pathList;
   }

   void collectPaths(Collection<String> var1) {
      var1.addAll(this.m_pathList);
   }

   public int getPathListSize() {
      return this.m_pathList.size();
   }

   void addPath(String var1) {
      if (this.m_pathList.isEmpty()) {
         this.m_pathList = Sets.newHashSet();
      }

      this.m_pathList.add(var1);
   }

   void removePath(String var1) {
      this.m_pathList.remove(var1);
      if (this.m_pathList.isEmpty()) {
         this.m_pathList = Collections.emptySet();
      }
   }

   void clearPathList() {
      this.m_pathList = Collections.emptySet();
   }

   void clearPathListRecursively() {
      this.clearPathList();

      for (Node var2 : this.m_children) {
         var2.clearPathListRecursively();
      }
   }

   Set<String> getPathsToRemove(Set<String> var1) {
      HashSet var2 = Sets.newHashSet(this.m_pathList);
      var2.removeAll(var1);
      return var2;
   }

   Set<String> getPathsToAdd(Set<String> var1) {
      HashSet var2 = Sets.newHashSet(var1);
      var2.removeAll(this.m_pathList);
      return var2;
   }

   public String getSourcePath() {
      return this.m_mappingKits.getSourcePath();
   }

   public void setSourcePath(String var1) {
      this.m_mappingKits.setSourcePath(var1);
   }

   public void collectAttributeArray(JbNode var1, List<Object> var2, List<String> var3, int var4) {
      String var5 = (String)var3.get(var4);
      String var6 = StringUtils.substringAfter(var5, "#");
      var5 = StringUtils.substringBefore(var5, "#");
      if (var4 == var3.size() - 1) {
         String var12 = "";
         var2.add(var12);
      } else {
         Node var7 = this.findChildFolder(var5);
         JbNode var8 = var1.getChild(var7.m_CROM);
         if (var8 != null) {
            if (var7.isLoopNode()) {
               ArrayList var9 = Lists.newArrayList();
               if (this.m_children != null) {
                  List var13 = var8.getChildren();
                  if (var6 != null) {
                     int var10;
                     if (var6.length() > 1 && var6.charAt(0) == '_') {
                        var10 = Integer.parseInt(var6.substring(1));
                        var10 = var13.size() - var10;
                     } else {
                        var10 = Integer.parseInt(var6) - 1;
                     }

                     if (var10 >= 0 && var10 < var13.size()) {
                        var2.add(var13.get(var10));
                     }
                  } else {
                     var2.add(var13);
                  }
               }
            } else {
               var7.collectAttributeArray(var8, var2, var3, var4 + 1);
            }
         }
      }
   }

   public Node turnOffShortDisplay() {
      CROM var1 = this.getCROM();
      CROM var2 = var1.getParent();
      Node var3 = new Node(this.getTreeComponent(), null, var2);
      int var4 = this.m_parent.m_children.indexOf(this);
      this.m_parent.m_children.remove(var4);
      this.m_parent.m_children.add(var4, var3);
      var3.m_parent = this.m_parent;
      this.m_parent = var3;
      var3.addChild(this);
      this.m_display = var1.getDisplayText();
      var3.m_display = var2.getDisplayText();
      return var3;
   }

   public Node createNilNodeToMap() {
      if (this.m_parent == null) {
         return null;
      }

      if (this.isFolder()) {
         return this.findLeaf("xsi:nil");
      }

      Node var1 = this.turnOffShortDisplay();
      CROM var2 = var1.getCROM().getNilCrom();
      Node var3 = new Node(this.getTreeComponent(), var1, var2);
      var3.m_display = var2.getDisplayText();
      return var3;
   }

   public boolean isSfdcNode() {
      CROM var1 = this.getTreeComponent().sfdcNode;
      return var1 != null && this.getCROM() == var1;
   }

   public boolean isAncestorOf(Node var1) {
      for (Node var2 = var1; var2 != null; var2 = var2.m_parent) {
         if (var2 == this) {
            return true;
         }
      }

      return false;
   }

   private Node turnOnShortDisplayEVNode() {
      CROM var1 = this.getCROM();
      Node var2 = new Node(this.getTreeComponent(), null, var1.findValueNode());
      int var3 = this.m_parent.m_children.indexOf(this);
      this.m_parent.m_children.remove(var3);
      this.m_parent.m_children.add(var3, var2);
      var2.m_parent = this.m_parent;
      this.m_parent = null;
      return var2;
   }

   public boolean isSfdcCreateId() {
      if ((this.m_status & 16777216) != 0) {
         return false;
      }

      if ((this.m_status & 8388608) != 0) {
         return true;
      }

      boolean var1 = false;
      TreeMapper var2 = this.getTreeMapper();
      if (var2 != null) {
         Transformation var3 = var2.getTransformation();
         SalesforceWizardRole var4 = var3.getSalesforceWizardRole();
         var1 = var4 != null && var4.method == SfdcMethod.CREATE && var4.position == SalesforceWizardRole.Position.REQUEST;
         if (var1) {
            if (!this.getName().isEmpty()) {
               var1 = this.getName().equals("Id");
            } else if (this.m_CROM != null && this.m_CROM.getParent() != null) {
               var1 = this.m_CROM.getParent().getName().equals("Id");
            }
         }
      }

      if (var1) {
         this.m_status |= 8388608;
      } else {
         this.m_status |= 16777216;
      }

      return var1;
   }

   public boolean isNonAutoMappableSfdcField() {
      CROM var1 = this.m_CROM;
      if (this.isValueLeaf()) {
         var1 = var1.getParent();
      }

      return var1.isNonAutoMappableSfdcField();
   }

   public boolean isChildOfOneOfNode() {
      return this.m_parent != null && this.m_parent.isOneOf();
   }

   public boolean canHaveCondition() {
      assert this.isFolder();
      return this.m_parent != null && this.isTypeDefinition() && this.m_parent.isDerivedTypeHolder()
         || this.isSubstitutionGroup()
         || this.isOptional()
         || this.isLoopNode()
         || this.isChildOfOneOfNode();
   }

   public String getConditionOrScriptName() {
      return this.canHaveCondition() ? "Condition" : "Script";
   }

   static class NodeLocation {
      public final Rectangle m_rect;
      public final Node m_node;

      public NodeLocation(Node var1, Rectangle var2) {
         this.m_rect = var2;
         this.m_node = var1;
      }
   }
}
