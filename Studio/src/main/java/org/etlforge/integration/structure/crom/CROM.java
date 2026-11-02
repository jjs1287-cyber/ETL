package org.etlforge.integration.structure.crom;

import com.google.common.collect.Sets;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.structure.mapping.JtrXmlFormatUtils;
import org.etlforge.integration.structure.mapping.StringIndexMap;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.util.name.Nameable;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.XmlNameFactory;
import org.xml.sax.Attributes;

public final class CROM implements Nameable {
   public static final String FLAT_TEXT_MARKER = "_flat_";
   public static final String ELEMENT = "CROM";
   public static final String TYPE = "Type";
   public static final String NAME = "Name";
   public static final String DISPLAY_NAME = "Display";
   public static final String COMMENT = "Comment";
   public static final long CROM_t_Unknown = 0L;
   public static final long CROM_t_Element = 1L;
   public static final long CROM_t_Value = 2L;
   public static final long CROM_t_Attribute = 4L;
   public static final long CROM_t_Collection = 8L;
   public static final long CROM_t_OneOf = 16L;
   public static final long CROM_t_Optional = 32L;
   public static final long CROM_t_AtLeastOne = 64L;
   public static final long CROM_t_OracleSeqNo = 128L;
   public static final long CROM_t_XmlAnyType = 128L;
   public static final long CROM_t_SimpleCromMatch = 256L;
   public static final long CROM_t_ComplexCromMatch = 512L;
   public static final long CROM_t_PrimaryKey = 256L;
   public static final long CROM_t_ForeignKey = 512L;
   public static final long CROM_t_UseDbFcn = 1024L;
   public static final long CROM_t_NonAutoMappableSfdcField = 1024L;
   public static final long CROM_t_Precondition = 2048L;
   public static final long CROM_t_SourceUpdateColumn = 8192L;
   public static final long CROM_t_CouldBeRoot = 8193L;
   public static final long CROM_t_Null = 16384L;
   public static final long CROM_t_Root = 32768L;
   public static final long CROM_t_LoopNode = 65536L;
   public static final long CROM_t_Added = 131072L;
   public static final long CROM_t_OneToOne = 262144L;
   public static final long CROM_t_UpdateKey = 262144L;
   public static final long CROM_t_OuterJoin = 524288L;
   public static final long CROM_t_SfdcRelatedObject = 524288L;
   public static final long CROM_t_Array = 1048576L;
   public static final long CROM_t_AllGroup = 2097152L;
   public static final long CROM_t_AnyElement = 4194304L;
   public static final long CROM_t_Hidden = 8388608L;
   public static final long CROM_t_HasDefault = 16777216L;
   public static final long CROM_t_AnonymousGroup = 33554432L;
   public static final long CROM_t_DerivedTypeHolder = 67108864L;
   public static final long CROM_t_TypeDefinition = 134217728L;
   public static final long CROM_t_SubstitutionGroupHead = 268435456L;
   public static final long CROM_t_Flat = 536870912L;
   public static final long CROM_t_ReadOnly = 1073741824L;
   public static final long CROM_t_FixedValue = 2147483648L;
   public static final long CROM_t_TypeExpanded = 2147483648L;
   public static final long CROM_t_AttValuePrecondition = 2054L;
   public static final long CROM_t_LdapAttributeRemove = 256L;
   public static final long CROM_t_LdapAttributeReplaceAll = 512L;
   public static final long CROM_t_AnyThing = 4095L;
   public static final long CROM_t_NonLeaf = 174063633L;
   public static final long CROM_t_Loop = 524360L;
   public static final long CROM_t_AttV = 6L;
   public static final long CROM_t_RootLoop = 557128L;
   public static final String PRECONDITION_TAG = "/PRECONDITION/";
   public static final String PRESCRIPT_TAG = "/PRESCRIPT/";
   private static final XmlNameFactory XML_NAME_FACTORY = XmlNameFactory.nonConforming();
   private static final List<CROM> NO_CHILDREN = Collections.unmodifiableList(new ArrayList<>());
   public static final int t_unknown = -1;
   public static final int t_int = 0;
   public static final int t_long = 1;
   public static final int t_float = 2;
   public static final int t_double = 3;
   public static final int t_date = 4;
   public static final int t_string = 5;
   public static final int t_bool = 6;
   public static final int t_timespan = 7;
   public static final int t_bit = 8;
   public static final int t_binary = 9;
   public static final int t_collection = 10;
   private CROM parent;
   private long type;
   private String name;
   private String path;
   private String displayName;
   private String comment;
   private CROM_ext cromExtension = NullExtension.INSTANCE;
   private boolean isNillable = false;
   public final CromType cromType;
   private List<CROM> children = NO_CHILDREN;

   public CROM(CromType var1, CROM var2) {
      this.cromType = var1;
      this.parent = var2;
      if (var2 != null) {
         var2.addChild(this);
      }
   }

   public CROM(CROM var1, boolean var2, String var3, int var4, CromType var5) {
      this.cromType = var5;
      this.parent = var1;
      if (var1 != null) {
         var1.insertChild(var4, this);
      }

      this.type = 0L;
      if (var2) {
         this.addType(1L);
      } else if (var3.equals("")) {
         this.addType(2L);
      } else {
         this.addType(4L);
      }

      if (var3 != null) {
         String var6 = var3;
         this.displayName = var6;
         this.name = CromUtils.convertIllegalCharacters(var6);
         if (StringUtils.isNotEmpty(this.name) && !this.isNameUniqueAmongSiblings(this.name)) {
            String var7 = this.name;
            boolean var8 = false;

            for (int var9 = 1; var9 < 100; var9++) {
               this.name = var7 + var9;
               var8 = this.isNameUniqueAmongSiblings(this.name);
               if (var8) {
                  break;
               }
            }

            assert var8;
            if (!var8) {
            }
         }

         if (this.name.equals(this.displayName)) {
            this.displayName = null;
         }
      }

      if (var2) {
         if (this.cromType != null) {
            this.cromExtension = this.cromType.createExtension();
         } else {
            this.cromExtension = NullExtension.INSTANCE;
         }
      } else {
         this.setExtension(new CROM_Attr());
      }
   }

   private boolean isNameUniqueAmongSiblings(String var1) {
      if (this.parent == null) {
         return true;
      }

      for (CROM var3 : this.parent.children) {
         if (var3 != this && var3.getName().equals(var1)) {
            return false;
         }
      }

      return true;
   }

   public CROM(CROM var1, Attributes var2, Map<Integer, String> var3, CromType var4) {
      this.parent = var1;
      if (var1 != null) {
         var1.addChild(this);
      }

      this.setType(0L);
      String var5 = var2.getValue("Type");
      this.addType(CromUtils.getTypeCodeFromString(var5));
      this.name = JtrXmlFormatUtils.getString(var2, "Name");
      if (this.name == null) {
         this.name = "";
      }

      this.displayName = JtrXmlFormatUtils.lookupIndexedString(var2, "Display", var3);
      if (this.name.equals(this.displayName)) {
         this.displayName = null;
      }

      this.comment = var2.getValue("Comment");
      this.cromType = var4;
      this.setExtension(null);
   }

   public CROM(CROM var1, CROM var2, CROM var3) {
      this.parent = var1;
      if (var2 == null) {
         throw new RuntimeException("cloning null CROM");
      }

      if (var1 != null) {
         int var4 = 0;
         if (var3 != null) {
            var4 = var1.children.indexOf(var3) + 1;
         }

         var1.insertChild(var4, this);
      }

      this.setType(var2.getType());
      this.setName(var2.getName());
      this.cromType = var2.cromType;
      this.setDisplayName(var2.getDisplayName());
      this.comment = var2.comment;
      this.setExtension(var2.getExtension().clone());
      this.isNillable = var2.isNillable;
      if (this.isNilAttributeCrom() && var1 != null) {
         var1.isNillable = true;
      }
   }

   public CROM(CROM var1, CROM var2, boolean var3, CROM_Tree var4, Stack<String> var5, String var6) {
      String var7 = var6 + "  ";
      this.parent = var1;
      this.setType(var2.getType());
      if (var1 != null) {
         var1.addChild(this);
      }

      this.setName(var2.getName());
      if (var2.isNilAttributeCrom()) {
         var1.isNillable = true;
      }

      this.setDisplayName(var2.getDisplayName());
      this.comment = var2.comment;
      this.cromType = var2.cromType;
      this.setExtension(var2.getExtension().clone());
      if (!var2.isAbstract()) {
         for (CROM var9 : var2.children) {
            new CROM(this, var9, var3, var4, var5, var7);
         }
      }

      Map var11 = var4.getElementTypeMap();
      String var12 = var2.getSchemaTypeExpandedName();
      if (var12 != null && var11 != null) {
         CROM var10 = (CROM)var11.get(var12);
         if (var10 != null && !isSameTypeDefinition(var2, var10)) {
            this.explodeXmlComplexType(var10, var3, var4, var5, var7);
         }
      }
   }

   public CROM getParent() {
      return this.parent;
   }

   private static boolean isSameTypeDefinition(CROM var0, CROM var1) {
      return var0.isTypeDefinition() && var1.isTypeDefinition() && StringUtils.equals(var0.getSchemaTypeExpandedName(), var1.getSchemaTypeExpandedName());
   }

   private String explode_id() {
      return this.getName() + ",schecma_type=" + this.getSchemaTypeExpandedName();
   }

   public CROM createValueCrom(String var1) {
      boolean var2 = false;
      byte var3 = 0;
      CROM var4 = new CROM(this, var2, "", var3, this.cromType);
      var4.getExtension().setValueType(var1);
      var4.addType(2L);
      return var4;
   }

   public String getSchemaType() {
      return this.isLeaf() ? null : this.getExtension().getSchemaType();
   }

   private String getSchemaTypeNamespace() {
      return this.isLeaf() ? null : this.getExtension().getSchemaTypeNamespace();
   }

   private int count_node() {
      int var1 = 1;

      for (CROM var3 : this.getChildren()) {
         var1 += var3.count_node();
      }

      return var1;
   }

   public boolean isArray() {
      return (this.getType() & 1048576L) != 0L;
   }

   private void setArrayFlag() {
      this.addType(1048576L);
   }

   private void setTypeExpanded(boolean var1) {
      if (this.isElementNode()) {
         if (var1) {
            this.type |= 2147483648L;
         } else {
            this.type &= -2147483649L;
         }
      }
   }

   private boolean selectedDerivedTypeIsBaseType() {
      if (this.parent == null) {
         return false;
      }

      if (!this.parent.isDerivedTypeHolder()) {
         return false;
      }

      String var1 = this.parent.getSchemaTypeExpandedName();
      String var2 = this.getSchemaTypeExpandedName();
      return var1.equals(var2);
   }

   void checkDerivedTypeHolder(Map<String, CROM> var1) {
      if (!this.isLeaf()) {
         if (var1 != null && !var1.isEmpty()) {
            if (!this.selectedDerivedTypeIsBaseType()) {
               String var2 = this.getSchemaTypeExpandedName();
               CROM var3 = (CROM)var1.get(var2);
               if (var3 != null && var3.hasDerivedType()) {
                  this.setDerivedTypeHolder();
               }
            }
         }
      }
   }

   public boolean hasDerivedType() {
      return this.getExtension().hasDerivedTypes();
   }

   public boolean hasSubstitutionGroup() {
      return this.getExtension().hasSubstitutionGroup();
   }

   public void resetSubstitutionGroupHead() {
      this.setType(this.getType() & -268435457L);
      this.setType(this.getType() & -67108865L);
      this.getExtension().clearSubstitutionGroupContainer();
   }

   public Set<String> getTypeExtentions() {
      return this.isLeaf() ? null : this.getExtension().getTypeExtensions();
   }

   public Set<String> getTotalTypeExtentions(Map<String, CROM> var1) {
      if (this.isLeaf()) {
         return null;
      }

      Set var2 = this.getExtension().getTypeExtensions();
      if (var2.size() == 0) {
         return var2;
      }

      HashSet var3 = new HashSet();
      this.addTypeExtensions(var3, var2, var1);
      return var3;
   }

   public Set<String> getSubstitutionGroup() {
      return this.isLeaf() ? null : this.getExtension().getSubsitutionGroup();
   }

   public boolean isAtLeastOneNode() {
      return (this.getType() & 64L) != 0L;
   }

   public void setAtLeastOneNode() {
      this.addType(65600L);
   }

   public boolean isAttributeNode() {
      return (this.getType() & 4L) != 0L;
   }

   public void setAttributeNode() {
      this.addType(4L);
   }

   public boolean isCollectionNode() {
      return (this.getType() & 8L) != 0L;
   }

   public void setCollectionNode() {
      this.addType(65544L);
   }

   public boolean isDerivedTypeHolder() {
      return (this.getType() & 67108864L) != 0L;
   }

   public boolean isElementNode() {
      return (this.getType() & 1L) != 0L;
   }

   public boolean isFlat() {
      return (this.getType() & 536870912L) != 0L;
   }

   public void setElementNode() {
      this.addType(1L);
   }

   public boolean isForeignKey() {
      return (this.getType() & 512L) != 0L;
   }

   public int getDbColSize() {
      return this.getExtension().getDbColSize();
   }

   public boolean isMapped() {
      return this.getExtension().isMapped();
   }

   public void setLinkKey() {
      this.addType(512L);
   }

   public String getForeignKeyValue() {
      if (this.isForeignKey() && this.parent != null) {
         CROM var1 = this.parent.parent;
         if (var1 == null) {
            return null;
         }

         String var2 = this.getLinkToPrimaryKey();
         String var3 = null;

         for (CROM var5 : var1.children) {
            if (var5.isElementNode()) {
               break;
            }

            if (var5.getDisplayName().equals(var2)) {
               var3 = var5.getValue();
               if (var3 == null && var5.isForeignKey()) {
                  var3 = var5.getForeignKeyValue();
               }
               break;
            }
         }

         return var3;
      } else {
         return null;
      }
   }

   public boolean isLeaf() {
      return (this.getType() & 2054L) != 0L;
   }

   public boolean isLoopNode() {
      return (this.getType() & 524360L) != 0L;
   }

   public boolean isRootLoopNode() {
      return (this.getType() & 557128L) != 0L;
   }

   public boolean isOneOfNode() {
      return (this.getType() & 16L) != 0L;
   }

   public boolean isOneToOne() {
      return this.isElementNode() && !this.isLoopNode() && !this.isOptionalNode();
   }

   public boolean isUpdateKey() {
      return this.cromType == CromType.CROM_t_DB && (this.getType() & 262144L) != 0L;
   }

   public boolean isInnerJoin() {
      assert this.cromType == CromType.CROM_t_DB;
      return this.isOneToOne() || this.isAtLeastOneNode();
   }

   public void setOneToOneNode() {
      this.addType(262144L);
   }

   public boolean isParentOptionalNode() {
      return this.parent != null && (this.parent.getType() & 32L) != 0L;
   }

   public boolean isOptionalNode() {
      return (this.getType() & 32L) != 0L;
   }

   public boolean isMultipleMapping() {
      return this.getName().indexOf(35) >= 0;
   }

   public void setOptionalNode() {
      this.addType(32L);
   }

   public boolean isOuterJoin() {
      return (this.getType() & 524288L) != 0L || (this.getType() & 8L) != 0L && (this.getType() & 64L) == 0L;
   }

   public void setOuterJoin() {
      this.addType(524288L);
   }

   public boolean isPrecondition() {
      return (this.getType() & 2048L) != 0L;
   }

   public void setPrecondition() {
      this.addType(2048L);
   }

   public boolean isPrimaryKey() {
      return this.isAttributeNode() && (this.getType() & 256L) != 0L;
   }

   public void setPrimaryKey() {
      assert this.isAttributeNode() && this.cromType == CromType.CROM_t_DB;
      this.addType(256L);
   }

   public void setUpdateKey() {
      assert this.isAttributeNode() && this.cromType == CromType.CROM_t_DB;
      this.addType(262144L);
   }

   public boolean isReadOnly() {
      return (this.getType() & 1073741824L) != 0L;
   }

   public boolean isFixedValueAttribute() {
      long var1 = this.getType();
      long var3 = 2147483652L;
      return (var1 & var3) == var3;
   }

   public boolean isAllGroup() {
      return (this.type & 2097152L) != 0L;
   }

   public boolean isAllGroupConverted() {
      if (this.isAllGroup()) {
         return true;
      }

      int var1 = 0;

      for (CROM var3 : this.getChildren()) {
         if (!var3.isLeaf()) {
            if (!var3.isElementNode() || var3.isLoopNode()) {
               return false;
            }

            var1++;
         }
      }

      return var1 > 1;
   }

   public boolean isAnyElement() {
      return (this.type & 4194304L) != 0L;
   }

   public CROM getChildAnyCrom() {
      if (this.isLeaf()) {
         return null;
      }

      for (CROM var2 : this.getChildren()) {
         if (var2.isAnyElement()) {
            return var2;
         }
      }

      return null;
   }

   public boolean isTypeExpanded() {
      long var1 = 2147483649L;
      return (this.getType() & var1) == var1;
   }

   public boolean isSourceUpdateColumn() {
      return (this.getType() & 8192L) != 0L;
   }

   public boolean isTypeDefinition() {
      return (this.getType() & 134217728L) != 0L;
   }

   public boolean isSubstitutionGroup() {
      return this.parent != null && this.parent.isSubstitutionGroupHead();
   }

   public boolean isSubstitutionGroupHead() {
      return (this.getType() & 268435456L) != 0L;
   }

   public boolean isValueLeaf() {
      return (this.getType() & 2L) != 0L;
   }

   public boolean isXml() {
      return this.isLeaf() && this.parent != null ? this.parent.isXml() : this.getExtension().isXml();
   }

   public boolean isXmlAnyType() {
      return (this.getType() & 128L) == 0L ? false : this.getExtension().isXml();
   }

   public void setDerivedTypeHolder() {
      this.addType(67108864L);
   }

   public void setSubstitutionGroupHead() {
      this.addType(268435456L);
      if (this.children != null && this.children.size() > 0) {
         this.children.clear();
      }
   }

   public boolean isAbstract() {
      return this.getExtension().isAbstract();
   }

   public String getValueType() {
      return this.getExtension().getValueType();
   }

   public int getSqlDataType() {
      return this.getExtension().getDbColType();
   }

   public String getLinkToPrimaryKey() {
      return this.getExtension().getLinkToPrimaryKey();
   }

   public void setValue(String var1) {
      this.getExtension().setValue(var1);
   }

   public void setSqlDataType(int var1) {
      this.getExtension().setDbColType(var1);
   }

   public void setLinkToPrimaryKey(String var1) {
      this.getExtension().setLinkToPrimaryKey(var1);
   }

   public void setSqlFilter(String var1) {
      this.getExtension().setSqlFilter(var1);
   }

   public void setSqlOrderBy(String var1) {
      this.getExtension().setSqlOrderBy(var1);
   }

   public void setDBSchema(String var1) {
      this.getExtension().setDbSchema(var1);
   }

   public void setSqlTableName(String var1) {
      this.getExtension().setSqlTableName(var1);
   }

   public String getSqlTableName() {
      return this.getExtension().getSqlTableName();
   }

   public String getSqlFilter() {
      return this.getExtension().getSqlFilter();
   }

   public String getSqlOrderBy() {
      return this.getExtension().getSqlOrderBy();
   }

   public String getValue() {
      return this.getExtension().getValue();
   }

   public void setName(String var1) {
      if (this.displayName == null && this.name != null) {
         this.displayName = this.name;
      }

      this.name = var1;
   }

   public void addType(long var1) {
      this.type |= var1;
   }

   public void clearType(long var1) {
      this.setType(this.getType() & ~var1);
   }

   @Override
   public String getName() {
      return this.name == null ? "" : this.name;
   }

   public boolean isAncestorOf(CROM var1) {
      if (var1 == null) {
         return false;
      }

      for (CROM var2 = var1.getParent(); var2 != null; var2 = var2.getParent()) {
         if (var2 == this) {
            return true;
         }
      }

      return false;
   }

   public String getPath(boolean var1, boolean var2) {
      if (this.isLeaf()) {
         if (var1) {
            if (!var2) {
               return this.getName();
            }

            String var8 = this.parent.getName();
            String var10 = this.getName();
            int var5 = var8.indexOf(35);
            int var6 = var8.indexOf(93);
            int var7 = var10.indexOf(35);
            if (var5 >= 0 && var7 < 0) {
               if (var6 < 0) {
                  var6 = var8.length();
               }

               return var10 + var8.substring(var5, var6);
            } else {
               return var10;
            }
         } else {
            return this.parent == null ? this.name : this.parent.getPath_(var1) + this.name;
         }
      } else {
         String var3 = this.getPath_(var1);
         if (var1 && var3 != null) {
            int var4 = var3.length();
            if (var4 > 0) {
               if (var3.charAt(--var4) == '.') {
                  var3 = var3.substring(0, var4);
               }
            }
         }

         return "[" + var3 + "]";
      }
   }

   public String getPath_(boolean var1) {
      if (this.path != null) {
         return this.path;
      }

      if (var1) {
         return this.getName();
      }

      this.buildPath();
      return this.path;
   }

   private void buildPath() {
      Stack var1 = new Stack();
      var1.add(this);

      for (CROM var2 = this.parent; var2 != null && var2.name != null && var2.name.length() > 0; var2 = var2.parent) {
         var1.add(var2);
      }

      StringBuilder var3 = new StringBuilder(100);

      while (var1.size() > 0) {
         CROM var5 = (CROM)var1.pop();
         String var4 = var5.name;
         if (!StringUtils.isEmpty(var4)) {
            var3.append(var4).append(var5.isLoopNode() ? "." : "$");
         }
      }

      this.path = var3.toString();
   }

   public void rebuildPaths() {
      this.buildPath();

      for (CROM var2 : this.children) {
         var2.buildPath();
      }
   }

   public String getDomain() {
      return this.getExtension().getDomain();
   }

   public String getDefaultValue() {
      return this.getExtension().getDefaultValue();
   }

   public String getFormat() {
      return this.getExtension().getFormat();
   }

   public void setDefaultValue(String var1) {
      this.getExtension().setDefaultValue(var1);
      this.addType(16777216L);
   }

   public void setValueType(String var1) {
      this.getExtension().setValueType(var1);
   }

   public String getDisplayName() {
      if (this.isOneOfNode() || this.isAnonymousGroup()) {
         return "";
      } else {
         return this.displayName == null ? this.getName() : this.displayName;
      }
   }

   public void setDisplayName(String var1) {
      if (var1 != null && var1.equals(this.name)) {
         this.displayName = null;
      } else {
         this.displayName = var1;
      }
   }

   public void setDomain(String var1) {
      this.getExtension().setDomain(var1);
   }

   String getUniversalName() {
      if (this.getExtension() == NullExtension.INSTANCE) {
         return this.name;
      }

      if (StringUtils.isEmpty(this.name)) {
         return null;
      }

      ExpandedName var1 = XML_NAME_FACTORY.newExpandedName(this.getExtension().getNamespace(), this.name);
      return var1.toString();
   }

   private void setSchemaTypeExpandedName(String var1, String var2) {
      this.getExtension().setSchemaType(var1);
      this.getExtension().setSchemaTypeNamespace(var2);
   }

   private void setSchemaTypeExpandedName(CROM var1) {
      this.setSchemaTypeExpandedName(var1.getExtension().getSchemaType(), var1.getExtension().getSchemaTypeNamespace());
   }

   public String getSchemaTypeExpandedName() {
      return this.getExtension().getSchemaTypeExpandedName();
   }

   public String getNamespace() {
      return this.getExtension().getNamespace();
   }

   public boolean isElementReference() {
      if (this.isLeaf()) {
         return false;
      }

      String var1 = this.getSchemaType();
      return var1 == null || var1.isEmpty();
   }

   public static String getLocalName(String var0) {
      ExpandedName var1 = XML_NAME_FACTORY.newExpandedName(var0);
      return var1.getLocalName();
   }

   public String getElementReferenceExpandedName() {
      if (this.isLeaf()) {
         return "";
      } else if (this.getName() != null && !this.getName().isEmpty()) {
         ExpandedName var1 = XML_NAME_FACTORY.newExpandedName(this.getSchemaTypeNamespace(), this.getName());
         return var1.toString();
      } else {
         return "";
      }
   }

   public void addChild(CROM var1) {
      if (this.children == NO_CHILDREN) {
         this.children = new ArrayList<>();
      }

      int var2 = 0;
      if (this.children.size() > 0 && !var1.isPrecondition()) {
         if (var1.isValueLeaf()) {
            if (this.children.get(0).isPrecondition()) {
               var2 = 1;
            }
         } else if (var1.isAttributeNode()) {
            for (CROM var4 : this.children) {
               if (!var4.isLeaf()) {
                  break;
               }

               var2++;
            }
         } else {
            var2 = this.children.size();
         }
      }

      this.children.add(var2, var1);
   }

   public void insertChild(int var1, CROM var2) {
      if (this.children == NO_CHILDREN) {
         this.children = new ArrayList<>();
      }

      if (var1 >= 0 && var1 <= this.children.size()) {
         this.children.add(var1, var2);
      } else {
         this.children.add(var2);
      }

      var2.parent = this;
   }

   private void addTypeExtensions(Set<String> var1) {
      if (!this.isLeaf() && var1 != null && var1.size() != 0) {
         boolean var2 = this.hasDerivedType();

         for (String var4 : var1) {
            this.getExtension().addTypeExtension(var4);
         }

         if (!var2 && this.hasDerivedType()) {
            this.setDerivedTypeHolder();
         }
      }
   }

   private void addTypeExtensions(Set<String> var1, Set<String> var2, Map<String, CROM> var3) {
      for (String var5 : var2) {
         CROM var6 = (CROM)var3.get(var5);
         if (!var6.isAbstract()) {
            var1.add(var5);
         }

         if (var6.hasDerivedType() && var6 != this) {
            this.addTypeExtensions(var1, var6.getTypeExtentions(), var3);
         }
      }
   }

   private void addSubstitutionGroup(Set<String> var1) {
      if (!this.isLeaf() && var1 != null && var1.size() != 0) {
         boolean var2 = this.hasSubstitutionGroup();

         for (String var4 : var1) {
            this.getExtension().addSubstitutionGroup(var4);
         }

         if (!var2 && this.hasSubstitutionGroup()) {
            this.setSubstitutionGroupHead();
         }
      }
   }

   public String getNodeTypeMarker() {
      switch (this.cromType) {
         case CROM_t_Xml:
         case CROM_t_Ldap:
            return this.getMarkerForXmlAndLdap();
         case CROM_t_Text:
            return this.getMarkerForText();
         default:
            return "";
      }
   }

   private String getMarkerForText() {
      return this.getName().equals("") ? "_flat_" : "";
   }

   private String getMarkerForXmlAndLdap() {
      String var1 = "[";
      if (this.isLeaf()) {
         if (this.isValueLeaf()) {
            var1 = var1 + "V";
         }

         if (this.isAttributeNode()) {
            var1 = var1 + "A";
         }

         if (this.isOptionalNode()) {
            var1 = var1 + "?";
         }
      } else {
         if (this.isElementNode()) {
            var1 = var1 + "E";
         }

         if (this.isAnonymousGroup()) {
            var1 = var1 + "Group";
         }

         if (this.isTypeDefinition()) {
            var1 = var1 + "T";
         }

         if (this.isOneOfNode()) {
            var1 = var1 + "OneOf";
         }

         if (this.isCollectionNode()) {
            var1 = var1 + "*";
         }

         if (this.isOptionalNode()) {
            var1 = var1 + "?";
         }

         if (this.isAtLeastOneNode()) {
            var1 = var1 + "+";
         }
      }

      return var1 + "]";
   }

   private boolean isAnonymousGroup() {
      return this.getType() == 0L || this.getType() == 8L || (this.getType() & 33554432L) != 0L;
   }

   public boolean isNumericName() {
      if (this.name != null && !this.name.isEmpty()) {
         int var1 = this.name.length();

         for (int var2 = 0; var2 < var1; var2++) {
            if (!Character.isDigit(this.name.charAt(var2))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public String getDisplayText() {
      String var1 = this.getNodeTypeMarker();
      String var2;
      if (this.isLeaf()) {
         if (this.isPrecondition()) {
            if (this.name != null && this.name.endsWith("/PRESCRIPT/")) {
               var2 = "[P] Script";
            } else {
               var2 = "[P] Condition";
            }
         } else {
            String var3 = this.getDisplayName();
            if (this.isOptionalNode() && !this.isXml()) {
               var3 = var3 + "?";
            }

            String var4 = this.getDataTypeForDisplay();
            var2 = var1 + " " + (var3 != null ? var3 : "") + var4;
         }
      } else {
         String var5 = "";
         if (this.isDerivedTypeHolder()) {
            var5 = " (Derived types ...)";
         }

         if (this.isSubstitutionGroupHead()) {
            var5 = " (Substitution group ...)";
         }

         String var6 = this.getDisplayName();
         var2 = var1 + " " + var6 + var5;
      }

      if (StringUtils.isNotEmpty(this.comment)) {
         var2 = var2 + " - " + this.comment;
      }

      return var2;
   }

   private String getDataTypeForDisplay() {
      return CromUtils.getDataTypeForDisplay(this.getValueType(), this.getSqlDataType());
   }

   public void writeXml(Writer var1, StringIndexMap var2) throws IOException {
      StringBuilder var3 = new StringBuilder();
      var3.append('<').append("CROM");
      JtrXmlFormatUtils.addHexAttribute(var3, "Type", this.getType());
      JtrXmlFormatUtils.addAttribute(var3, "Name", this.name);
      if (this.displayName != null && !this.name.equals(this.displayName)) {
         JtrXmlFormatUtils.addAttribute(var3, "Display", var2.store(this.displayName));
      }

      JtrXmlFormatUtils.addAttribute(var3, "Comment", this.comment);
      var1.write(var3.toString());
      if (this.hasSubElements()) {
         var1.write(">\n");
         this.getExtension().writeXml(var1, var2);

         for (CROM var5 : this.getChildren()) {
            var5.writeXml(var1, var2);
         }

         var1.write("</" + "CROM" + ">\n");
      } else {
         var1.write("/>\n");
      }
   }

   private boolean hasSubElements() {
      return this.getExtension() != NullExtension.INSTANCE || this.hasChildren();
   }

   public void setType(long var1) {
      this.type = var1;
   }

   public long getType() {
      return this.type;
   }

   public void setExtension(CROM_ext var1) {
      this.cromExtension = var1 != null ? var1 : NullExtension.INSTANCE;
   }

   public CROM_ext getExtension() {
      return this.cromExtension;
   }

   public Iterable<CROM> getChildren() {
      return this.children;
   }

   public boolean hasChildren() {
      return this.children.size() > 0;
   }

   public int getChildCount() {
      return this.children.size();
   }

   public int indexOfChild(CROM var1) {
      return this.children.indexOf(var1);
   }

   public boolean hasOnlyValueNode() {
      if (this.isLeaf()) {
         return this.parent.hasOnlyValueNode();
      }

      if (this.isLoopNode()) {
         return false;
      }

      if (this.children.size() == 1) {
         CROM var1 = this.children.get(0);
         if (var1.isValueLeaf()) {
            return true;
         }
      } else if (this.children.size() == 2 && this.children.get(0).isValueLeaf() && this.children.get(1).isNilAttributeCrom()) {
         CROM var2 = this.children.get(0);
         if (var2.isValueLeaf()) {
            return StringUtils.isEmpty(var2.getDefaultValue());
         }
      }

      return false;
   }

   public boolean hasMoreThanNilChildren() {
      int var1 = this.children.size();
      return var1 == 0 ? false : var1 > 1 || !this.children.get(0).isNilAttributeCrom();
   }

   public boolean hasOnlyXsiNilChild() {
      return this.children.size() == 1 ? this.children.get(0).isNilAttributeCrom() : false;
   }

   public CROM getNilCrom() {
      for (CROM var2 : this.children) {
         if (var2.isNilAttributeCrom()) {
            return var2;
         }
      }

      return null;
   }

   public boolean isNilAttributeCrom() {
      return this.getName().equals("xsi:nil");
   }

   public void removeChild(CROM var1) {
      this.children.remove(var1);
      if (this.children.isEmpty()) {
         this.children = NO_CHILDREN;
      }
   }

   public CROM getChild(int var1) {
      if (this.children.size() == 0) {
         return null;
      } else {
         return var1 < this.children.size() ? this.children.get(var1 < 0 ? 0 : var1) : this.getLastChild();
      }
   }

   public CROM getLastChild() {
      return this.children.isEmpty() ? null : this.children.get(this.children.size() - 1);
   }

   public boolean isIdentifier() {
      if (this.cromType != CromType.CROM_t_Text) {
         return false;
      } else {
         return !this.isLeaf() ? false : this.getValueType().equals("Identifier");
      }
   }

   public String getDelimiter() {
      if (this.isLeaf()) {
         return null;
      } else if (this.cromType == CromType.CROM_t_Text) {
         return this.getExtension() == null ? null : this.getExtension().getDelimiter();
      } else {
         return this.cromType != CromType.CROM_t_DB && this.cromType != CromType.CROM_t_Ldap ? null : ",";
      }
   }

   public String getStrQual() {
      if (this.isLeaf()) {
         return null;
      } else if (this.cromType == CromType.CROM_t_Text) {
         return this.getExtension() == null ? null : this.getExtension().getStrQual();
      } else {
         return this.cromType != CromType.CROM_t_DB && this.cromType != CromType.CROM_t_Ldap ? null : "\"";
      }
   }

   public int getFieldPos() {
      if (this.isLeaf()) {
         return this.getExtension().getFieldPos();
      } else if (this.cromType == CromType.CROM_t_Text) {
         return this.getExtension().getIdentPos();
      } else {
         return this.cromType != CromType.CROM_t_DB && this.cromType != CromType.CROM_t_Ldap ? 0 : 0;
      }
   }

   public int getFieldLength() {
      if (this.isLeaf()) {
         return this.getExtension().getFieldLength();
      } else if (this.cromType == CromType.CROM_t_Text) {
         return this.getExtension().getIdentLength();
      } else {
         return this.cromType != CromType.CROM_t_DB && this.cromType != CromType.CROM_t_Ldap ? 0 : 0;
      }
   }

   public boolean isDatabase() {
      return this.cromType == CromType.CROM_t_DB;
   }

   public boolean isLdap() {
      return this.cromType == CromType.CROM_t_Ldap;
   }

   public boolean foundSegmentIdentifier(String var1) {
      if (var1 == null) {
         return false;
      }

      String var2 = this.getSegmentIdentifier();
      return var2.equals(var1);
   }

   public String getSegmentIdentifier() {
      if (this.cromType == CromType.CROM_t_DB) {
         return this.getName();
      }

      if (this.cromType == CromType.CROM_t_Ldap) {
         return this.getName();
      }

      if ((this.type & 32768L) != 0L) {
         return "";
      }

      CROM var1 = this.getChild(0);
      if (var1.isPrecondition()) {
         var1 = this.getChild(1);
      }

      return var1 != null ? var1.getFormat() : "";
   }

   private String getSegmentInWrongPositionErrorMessage(String var1) {
      return "A record was encountered in an unexpected position. Please make sure the format of the source text file is correct, in particular that that all records appear under the correct parent. Details: undefined record identifier=\""
         + var1
         + "\", appeared under record \""
         + this.getName()
         + "\"";
   }

   public CROM findSegment(String var1) {
      if (this.foundSegmentIdentifier(var1)) {
         return this;
      }

      for (CROM var2 = this; var2 != null; var2 = var2.getParent()) {
         CROM var3 = var2.findSegmentInChildren(var1);
         if (var3 != null) {
            return var3;
         }
      }

      throw new RuntimeException(this.getSegmentInWrongPositionErrorMessage(var1));
   }

   private CROM findSegmentInChildren(String var1) {
      for (CROM var3 : this.children) {
         if (var3.isElementNode() && var3.foundSegmentIdentifier(var1)) {
            return var3;
         }
      }

      return null;
   }

   public void defineCromMatchingScheme() {
      if (this.isElementNode()) {
         if (!this.isXml()) {
            this.setSimpleCromMatching();
         } else if (this.hasComplexMatching()) {
            this.setComplexCromMatching();
         } else {
            this.setSimpleCromMatching();
         }

         for (CROM var2 : this.children) {
            if (!var2.getDisplayName().isEmpty() && var2.isElementNode()) {
               var2.defineCromMatchingScheme();
            }
         }
      }
   }

   private void setSimpleCromMatching() {
      this.type |= 256L;
      this.type &= -513L;
   }

   private void setComplexCromMatching() {
      this.type &= -257L;
      this.type |= 512L;
   }

   private boolean hasComplexMatching() {
      if (!this.isXml()) {
         return false;
      }

      HashSet var1 = Sets.newHashSet();

      for (CROM var3 : this.children) {
         if (var3.isElementNode()) {
            String var4 = var3.getDisplayName();
            if (var4.isEmpty()) {
               return true;
            }

            if (!var1.add(var4)) {
               return true;
            }
         }
      }

      return false;
   }

   public CROM findValueNode() {
      if (this.isValueLeaf()) {
         return this;
      }

      for (CROM var2 : this.children) {
         if (var2.isValueLeaf()) {
            return var2;
         }

         if (var2.isElementNode() && var2.isOneOfNode()) {
            CROM var3 = var2.findValueNode();
            if (var3 != null) {
               return var3;
            }
         }
      }

      return null;
   }

   public CROM findSubNode(String var1) {
      for (CROM var3 : this.children) {
         if (!var3.isLeaf()) {
            String var4 = var3.getDisplayName();
            if (var1.equals(var4)) {
               return var3;
            }

            if (var4.isEmpty()) {
               CROM var5 = this.findSubNode(var4);
               if (var5 != null) {
                  return var5;
               }
            }
         }
      }

      return null;
   }

   public CROM findDescendant(String... var1) {
      CROM var2 = this;

      for (String var6 : var1) {
         CROM var7 = var2.findSubNode(var6);
         if (var7 == null) {
            return null;
         }

         var2 = var7;
      }

      return var2;
   }

   public CROM findAttribute(String var1) {
      for (CROM var3 : this.children) {
         if (var3.isLeaf() && var1.equals(var3.getDisplayName())) {
            return var3;
         }
      }

      return null;
   }

   public static CROM getNextValueAttribute(Iterator<CROM> var0) {
      if (!var0.hasNext()) {
         return null;
      }

      CROM var1 = (CROM)var0.next();
      if (var1.isPrecondition()) {
         var1 = (CROM)var0.next();
      }

      while (var1 != null && !var1.isAttributeNode() && !var1.isValueLeaf()) {
         if (!var0.hasNext()) {
            return null;
         }

         var1 = (CROM)var0.next();
      }

      return var1;
   }

   public void print(PrintStream var1) {
      StringBuilder var2 = new StringBuilder();
      this.buildString("", var2);
      var1.println(var2 + "\n\n\n");
   }

   public void buildString(String var1, StringBuilder var2) {
      var2.append(var1);
      if (this.isPrecondition()) {
         var2.append("[P");
      } else if (this.isValueLeaf()) {
         var2.append("[V");
      } else if (this.isLeaf()) {
         var2.append("[A");
      } else if (!this.isSubstitutionGroup() && !this.isTypeDefinition()) {
         var2.append("[E");
      } else {
         var2.append("[T");
      }

      if (this.isOptionalNode() || this.isParentOptionalNode()) {
         var2.append('?');
      } else if (this.isAtLeastOneNode()) {
         var2.append('+');
      } else if (this.isCollectionNode()) {
         var2.append('*');
      }

      var2.append(String.format(" 0x%x] ", this.type));
      var2.append(this.getName());
      if (this.isSubstitutionGroupHead()) {
         var2.append(" -SubstitutionGroup head- ");
      }

      if (this.isDerivedTypeHolder()) {
         var2.append(" -Derived types- ");
      }

      var2.append('\n');

      for (CROM var4 : this.children) {
         var4.buildString(var1 + " ", var2);
      }
   }

   public String getXmlPath() {
      assert this.cromType == CromType.CROM_t_Xml;
      ArrayList var1 = new ArrayList();

      for (CROM var2 = this; var2 != null; var2 = var2.getParent()) {
         if (var2.isElementNode() && !var2.getDisplayName().isEmpty() && !var2.isSubstitutionGroupHead() && !var2.isTypeDefinition()) {
            var1.add(var2);
         }
      }

      StringBuilder var3 = new StringBuilder();

      for (int var4 = var1.size() - 1; var4 >= 0; var4--) {
         CROM var5 = (CROM)var1.get(var4);
         var3.append('/');
         var3.append(var5.getDisplayName());
      }

      return var3.toString();
   }

   @Override
   public String toString() {
      return this.getPath(false, false);
   }

   public String toXmlString() {
      String var1 = this.getName();
      String var2 = String.format("0x%x", this.getType());
      String var3 = "";
      if (this.isValueLeaf()) {
         var3 = "<V type=\"" + var2 + "\"/>\n";
      } else if (this.isAttributeNode()) {
         var3 = "<A name=\"" + var1 + "\" type=\"" + var2 + "\"/>\n";
      } else if (this.isElementNode()) {
         var3 = "<E name=\"" + var1 + "\" type=\"" + var2 + "\"";
         if (this.getChildCount() == 0) {
            var3 = var3 + "/>\n";
         } else {
            var3 = var3 + ">\n";

            for (CROM var5 : this.getChildren()) {
               var3 = var3 + var5.toXmlString();
            }

            var3 = var3 + "</E>\n";
         }
      } else {
         var3 = "<Group name=\"" + var1 + "\" type=\"" + var2 + "\"";
         if (this.getChildCount() == 0) {
            var3 = var3 + "/>\n";
         } else {
            var3 = var3 + ">\n";

            for (CROM var12 : this.getChildren()) {
               var3 = var3 + var12.toXmlString();
            }

            var3 = var3 + "</Group>\n";
         }
      }

      return var3;
   }

   public int getDataType() {
      int var1 = this.getSqlDataType();
      if (var1 != 0) {
         switch (var1) {
            case -7:
            case -6:
            case 4:
            case 5:
               return 0;
            case -5:
               return 1;
            case -4:
            case -3:
            case -2:
               return 9;
            case -1:
            case 12:
               return 5;
            case 1:
               return 5;
            case 2:
            case 3:
            case 8:
               return 3;
            case 6:
            case 7:
               return 3;
            case 91:
            case 92:
            case 93:
               return 4;
         }
      }

      String var2 = this.getValueType();
      if (StringUtils.isEmpty(var2)) {
         return 5;
      }

      boolean var3 = false;
      int var4 = var2.length();
      if (var4 > 99) {
         throw new RuntimeException("Unexpected long data type string: \"" + var2 + "\"");
      }

      var2 = var2.toLowerCase();

      for (int var7 = var4 - 1; var7 > 0 && var2.charAt(var7) == ' '; var7--) {
         var4--;
      }

      var2 = var2.substring(0, var4);
      return ValueTypeMap.get(var2);
   }

   public void writeDbRecord(OutputStreamWriter var1) throws IOException {
      if (this.isDatabase()) {
         boolean var2 = false;
         if (!this.isFlat()) {
            var1.write(this.getName());
            var2 = true;
         }

         for (CROM var4 : this.children) {
            if (!var4.isLeaf()) {
               break;
            }

            if (!var4.isPrecondition()) {
               String var5 = var4.getValue();
               if (var5 != null && KongaStringUtils.CSV_RequireQualifier(var5, "\n\r,")) {
                  var5 = "\"" + KongaStringUtils.escapeDoubleQuotes(var5) + "\"";
               }

               if (var2) {
                  var1.write(44);
               } else {
                  var2 = true;
               }

               if (var5 != null) {
                  var1.write(var5);
               }
            }
         }

         var1.write(10);
      }
   }

   private String calcTreeIndent(CROM var1) {
      Stack var2 = new Stack();

      for (CROM var3 = this; var3 != null && var3 != var1; var3 = var3.parent) {
         var2.push(var3);
      }

      StringBuilder var6 = new StringBuilder();
      CROM var4 = var1;

      while (var2.size() > 0) {
         CROM var5 = (CROM)var2.pop();
         if (var5 == this) {
            var6.append("|__");
         } else {
            if (var4 != null && var5 != var4.getLastChild()) {
               var6.append('|');
            }

            var6.append('\t');
         }

         var4 = var5;
      }

      return var6.toString();
   }

   private String getCardinalityMarker() {
      if (this.isCollectionNode()) {
         return "*";
      } else if (this.isAtLeastOneNode()) {
         return "+";
      } else {
         return this.isOptionalNode() ? "?" : "";
      }
   }

   public String getTreeString(StringBuilder var1, CROM var2) {
      boolean var3 = var1 == null;
      if (var1 == null) {
         var1 = new StringBuilder();
      }

      var1.append(this.calcTreeIndent(var2));
      if (this.getDisplayName().isEmpty()) {
         if (this.isOneOfNode()) {
            var1.append("[OneOf");
         } else {
            var1.append("[Group");
         }

         var1.append(this.getCardinalityMarker());
         var1.append(' ').append(this.getName()).append(']');
      } else {
         var1.append(this.getName());
         var1.append(this.getCardinalityMarker());
      }

      var1.append('\n');

      for (CROM var5 : this.children) {
         if (!var5.isLeaf()) {
            var5.getTreeString(var1, var2);
         }
      }

      return var3 ? var1.toString() : null;
   }

   public boolean isMixed() {
      boolean var1 = false;

      for (CROM var3 : this.children) {
         if (var3.isValueLeaf()) {
            var1 = true;
         }

         if (var3.isElementNode()) {
            if (var1) {
               return true;
            }

            return false;
         }
      }

      return false;
   }

   private CROM findSimilarCrom(CROM var1, String var2) {
      if (var1.getName().equals(var2)) {
         return var1.getChildCount() > 0 ? var1 : null;
      }

      for (CROM var4 : var1.getChildren()) {
         CROM var5 = this.findSimilarCrom(var4, var2);
         if (var5 != null) {
            return var5;
         }
      }

      return null;
   }

   public int explodeXmlComplexType(CROM var1, boolean var2, CROM_Tree var3, Stack<String> var4, String var5) {
      if (this.isTypeExpanded()) {
         return 0;
      }

      String var6 = var5 + "  ";
      this.handleDerivedType_SubstitionGroupDuringExploding(var1);
      if (!var1.isTypeDefinition()) {
         var4.pop();
         return 0;
      }

      if (var4 == null) {
         var4 = new Stack();
      }

      String var7 = this.explode_id();
      if ((!var2 || var4.size() <= 0) && !var4.contains(var7)) {
         int var8 = 0;
         boolean var9 = !this.isSubstitutionGroupHead() && !this.isDerivedTypeHolder() && !var1.isAbstract();
         if (var9) {
            var4.push(var7);
            if (var1.isArray()) {
               this.setArrayFlag();
            }

            for (CROM var11 : var1.getChildren()) {
               CROM var12 = new CROM(this, var11, var2, var3, var4, var6);
               var8 += var12.count_node();
               if (!var12.hasChildren()) {
                  int var13 = var12.explodeXmlNodeWithDictionary(var2, var3, var4, var6);
                  if (var13 >= 0) {
                     var8 += var13;
                  } else {
                     var12.createValueCrom(var11.getSchemaType());
                     var8++;
                  }
               }
            }

            var4.pop();
         }

         this.setTypeExpanded(true);
         return var8;
      } else {
         return 0;
      }
   }

   private int explodeXmlGlobalElementReference(CROM var1, boolean var2, CROM_Tree var3, Stack<String> var4, String var5) {
      if (this.isTypeExpanded()) {
         return 0;
      }

      String var6 = var5 + "  ";
      this.handleDerivedType_SubstitionGroupDuringExploding(var1);
      if (var4 != null && var4.size() > 0) {
         return 0;
      }

      int var7 = 0;
      boolean var8 = !this.isSubstitutionGroupHead() && !this.isDerivedTypeHolder() && !var1.isAbstract();
      if (var8) {
         boolean var9 = true;
         if (var1.hasChildren()) {
            for (CROM var11 : var1.getChildren()) {
               if (!var11.isAttributeNode()) {
                  var9 = false;
               }

               CROM var12 = new CROM(this, var11, var2, var3, var4, var6);
               var7 += var12.count_node();
               boolean var13 = var12.isElementNode();
               if (var13 && !var12.hasChildren() && !var12.isElementReference()) {
                  int var14 = var12.explodeXmlNodeWithDictionary(var2, var3, var4, var6);
                  var7 += var14;
               }
            }
         }

         if (var9) {
            String var15 = var1.getElementReferenceExpandedName();
            this.setSchemaTypeExpandedName(var1);
            boolean var16 = this.isElementReference();
            if (var16) {
               String var17 = this.getElementReferenceExpandedName();
               if (var15.equals(var17)) {
                  return 0;
               }
            }

            var7 = this.explodeXmlNodeWithDictionary(var2, var3, var4, var5);
         }
      }

      this.setTypeExpanded(true);
      return var7;
   }

   private void handleDerivedType_SubstitionGroupDuringExploding(CROM var1) {
      if (this.isSubstitutionGroup()) {
         this.setType(1L);
         this.resetSubstitutionGroupHead();
      } else {
         this.addTypeExtensions(var1.getTypeExtentions());
         this.addSubstitutionGroup(var1.getSubstitutionGroup());
      }
   }

   private boolean isNodeHolder() {
      return this.isSubstitutionGroupHead();
   }

   private void makeNillable() {
      CROM var1 = new CROM(CromType.CROM_t_Xml, this);
      var1.setExtension(new CROM_Attr());
      var1.setName("xsi:nil");
      var1.setType(16777252L);
      var1.setValueType("Boolean");
      var1.setDefaultValue("false");
      this.isNillable = true;
   }

   private int explodeXmlNodeWithDictionary(boolean var1, CROM_Tree var2, Stack<String> var3, String var4) {
      if (this.isNodeHolder()) {
         return 0;
      }

      boolean var5 = this.isElementReference();
      if (!var5 && var2.getElementTypeMap() != null) {
         String var6 = this.getSchemaTypeExpandedName();
         CROM var7 = var2.getElementTypeMap().get(var6);
         if (var7 != null) {
            int var8 = this.explodeXmlComplexType(var7, var1, var2, var3, var4);
            if (this.isNillable && var8 > 0 && this.getNilCrom() == null) {
               this.makeNillable();
               var8++;
            }

            return var8;
         }
      }

      if (var5 && var2.getElements() != null) {
         String var9 = this.getElementReferenceExpandedName();
         CROM var10 = var2.getElements().get(var9);
         if (var10 != null) {
            return this.explodeXmlGlobalElementReference(var10, var1, var2, var3, var4);
         }
      }

      return -1;
   }

   public boolean expandNode(CROM_Tree var1) {
      if (this.children != null) {
         if (this.children.size() > 1) {
            return false;
         }

         if (this.children.size() == 1 && !this.children.get(0).isNilAttributeCrom()) {
            return false;
         }
      }

      String var3 = this.getName();
      if (this.isXml()) {
         int var4 = this.explodeXmlNodeWithDictionary(true, var1, null, "");
         boolean var5 = var4 >= 0;
         if (var5) {
            return true;
         }

         CROM var2;
         if ((var2 = this.findRecursiveNode(var3)) != null) {
            this.createRecursiveNode(var2, this.getName());
         } else {
            String var6 = this.getSchemaType();
            if (var6 != null) {
               this.createValueCrom(var6);
            } else {
               CROM var7 = this.findSimilarCrom(var1.getRoot(), var3);
               if (var7 != null) {
                  this.createRecursiveNode(var7, "");
               }
            }
         }
      } else {
         CROM var8 = this.findRecursiveNode(var3);
         if (var8 != null) {
            this.createRecursiveNode(var8, var3);
         }
      }

      return true;
   }

   private CROM getRoot() {
      CROM var1 = this;

      for (CROM var2 = this.getParent(); var2 != null; var2 = var2.getParent()) {
         var1 = var2;
      }

      return var1;
   }

   private CROM findRecursiveNode(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         CROM var2 = this.getParent_recursive(var1);
         if (var2 == null) {
            CROM var3 = this.getRoot();
            var2 = var3.getChildFolder_recursive(var1);
         }

         return var2;
      } else {
         return null;
      }
   }

   public CROM getParent_recursive(String var1) {
      for (CROM var2 = this.getParent(); var2 != null; var2 = var2.getParent()) {
         if (var2.getName().equals(var1)) {
            return var2;
         }
      }

      return null;
   }

   public CROM getFirstChildFolder() {
      for (CROM var2 : this.getChildren()) {
         if (!var2.isLeaf()) {
            return var2;
         }
      }

      return null;
   }

   public CROM getChildFolder(String var1) {
      if (var1 == null) {
         return null;
      }

      for (CROM var3 : this.getChildren()) {
         if (!var3.isLeaf() && var1.equals(var3.getName())) {
            return var3;
         }
      }

      return null;
   }

   private CROM getChildFolder_recursive(String var1) {
      if (this.getName().equals(var1) && !this.isLeaf() && this.hasChildren()) {
         return this;
      }

      for (CROM var3 : this.children) {
         CROM var4 = var3.getChildFolder_recursive(var1);
         if (var4 != null) {
            return var4;
         }
      }

      return null;
   }

   private void createRecursiveNode(CROM var1, String var2) {
      CROM var3 = null;

      for (CROM var5 : var1.children) {
         var3 = new CROM(this, var5, var3);
         if (!var3.isLeaf() && !var3.getName().equals(var2)) {
            var3.createRecursiveNode(var5, var2);
         }
      }
   }

   public boolean isUDF() {
      return (this.getType() & 1024L) != 0L;
   }

   public static CROM getSubstitutionGroupType(CROM_Tree var0, String var1) {
      for (CROM var3 : var0.getElements().values()) {
         if (var3.getName().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public static CROM getDerivedType(CROM_Tree var0, String var1) {
      for (CROM var3 : var0.getElementTypeMap().values()) {
         if (var3.getName().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public boolean isOptionalValueElement() {
      if (this.isElementNode() && this.getChildCount() != 0) {
         for (CROM var2 : this.children) {
            boolean var3 = var2.isOptionalNode() && (var2.isValueLeaf() || var2.isAttributeNode());
            if (!var3) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean isEmptyDerivedTypeHolder() {
      return this.isDerivedTypeHolder() && !this.hasMoreThanNilChildren();
   }

   public boolean isEmptySubstitutionGroupHead() {
      return this.isSubstitutionGroupHead() && !this.hasMoreThanNilChildren();
   }

   public boolean isCsv() {
      if (this.isXml()) {
         return false;
      } else if (this.isDatabase()) {
         return true;
      } else {
         return this.getDelimiter() != null && this.getStrQual() != null ? this.getDelimiter().equals(",") && this.getStrQual().equals("\"") : false;
      }
   }

   public void setComment(String var1) {
      this.comment = var1;
   }

   public boolean isNonAutoMappableSfdcField() {
      return this.isXml() && (this.type & 1024L) != 0L;
   }

   public boolean isSfdcRelatedObject() {
      return this.isXml() && (this.type & 524288L) != 0L;
   }

   public boolean isAttributeValue() {
      return this.isValueLeaf() && this.parent != null && this.parent.isAttributeNode();
   }

   public int cleanUpUnnecessaryFolder(int var1) {
      if (this.children != null && !this.children.isEmpty()) {
         CROM[] var2 = this.children.toArray(new CROM[this.children.size()]);
         this.children.clear();

         for (CROM var6 : var2) {
            if (var6.isValueLeaf()) {
               this.children.add(var6);
            } else {
               if (var6.getChildCount() == 1 && var6.getName().isEmpty() && (var6.getType() & 1L) == 0L) {
                  var6 = var6.getChild(0);
               }

               this.children.add(var6);
               if (var6.getName().isEmpty()) {
                  var6.setName("" + ++var1);
               }

               var1 = var6.cleanUpUnnecessaryFolder(var1);
            }
         }

         this.removeExtraFolder();
         return var1;
      } else {
         return var1;
      }
   }

   public void removeExtraFolder() {
      if (this.getChildCount() == 1) {
         CROM var1 = this.getChild(0);
         if (var1.getType() == 0L) {
            this.children = var1.children;
         }
      }
   }
}
