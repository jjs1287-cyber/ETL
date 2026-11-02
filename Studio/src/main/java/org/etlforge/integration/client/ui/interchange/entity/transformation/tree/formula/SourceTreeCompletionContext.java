package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.Icon;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.NodeCollector;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperIcons;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperSourceTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.structure.mapping.PathSeparator;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.text.contentassist.CompletionToken;
import org.etlforge.ui.text.contentassist.DefaultCompletionToken;

public final class SourceTreeCompletionContext implements CompletionContext {
   private static final Comparator<Node> NODE_ORDER = new Comparator<Node>() {
      public int compare(Node var1, Node var2) {
         return var1.m_deName.compareToIgnoreCase(var2.m_deName);
      }
   };
   private final FormulaBuilderSourceTree tree;
   private final NodeCollector nodeCollector;
   private boolean requiresTransTags;
   private boolean includeTokenInfo;

   public SourceTreeCompletionContext(FormulaBuilderSourceTree var1) {
      this.tree = var1;
      this.nodeCollector = new NodeCollector(var1);
      this.requiresTransTags = true;
      this.includeTokenInfo = true;
   }

   public void setRequiresTransTags(boolean var1) {
      this.requiresTransTags = var1;
   }

   public void setIncludeTokenInfo(boolean var1) {
      this.includeTokenInfo = var1;
   }

   @Override
   public CompletionToken[] getAvailableTokens(Document var1, int var2) {
      int var3 = this.getStartOfCurrentToken(var1, var2);
      if (var3 >= 0) {
         List var4 = this.collectNodes(var1, var3, var2);
         if (org.etlforge.integration.data.script.JavaScriptFunctions.isInside(DocumentUtils.getText(var1,0,var1.getLength()),var2)) {
            CompletionToken[] tokens=new CompletionToken[var4.size()];int index=0;
            for(Object object:var4){Node node=(Node)object;String path=KongaStringUtils.removeStartAndEnd(node.m_deName,"[","]");tokens[index++]=new DefaultCompletionToken("ETLForge.GetSource('"+org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(path)+"')",var3,var2,node.getDisplay());}
            return tokens;
         }
         return this.createTokensFromNodes(var2, var3, var4);
      } else {
         return new CompletionToken[0];
      }
   }

   private List<Node> collectNodes(Document var1, int var2, int var3) {
      String var4 = DocumentUtils.getText(var1, var2, var3 - var2);
      return this.nodeCollector.findMatchingNodes(var4);
   }

   private int getStartOfCurrentToken(Document var1, int var2) {
      String var3 = getPreContext(var1, var2);
      if (var3.isEmpty()) {
         return this.requiresTransTags ? -1 : 0;
      }

      for (int var4 = var2 - 1; var4 >= 0; var4--) {
         char var5 = var3.charAt(var4);
         if (isBoundary(var5)) {
            if (var4 < var2 - 1) {
               char var6 = var3.charAt(var4 + 1);
               if (PathSeparator.isSeparator(var6) || Character.isDigit(var5)) {
                  return -1;
               }
            }

            return var4 + 1;
         }
      }

      return this.requiresTransTags ? -1 : 0;
   }

   private static String getPreContext(Document var0, int var1) {
      try {
         return var0.getText(0, var1);
      } catch (BadLocationException var3) {
         return "";
      }
   }

   private static boolean isBoundary(char var0) {
      return !PathSeparator.isSeparator(var0) && var0 != '_' && !Character.isLetterOrDigit(var0);
   }

   private CompletionToken[] createTokensFromNodes(int var1, int var2, List<Node> var3) {
      Collections.sort(var3, NODE_ORDER);
      CompletionToken[] var4 = new CompletionToken[var3.size()];
      int var5 = 0;

      for (Node var7 : var3) {
         CompletionToken var8 = this.createToken(var1, var2, var7);
         var4[var5] = var8;
         var5++;
      }

      return var4;
   }

   private CompletionToken createToken(int var1, int var2, Node var3) {
      String var4 = KongaStringUtils.removeStartAndEnd(var3.m_deName, "[", "]");
      String var5 = var3.getDisplay();
      DefaultCompletionToken var6 = new DefaultCompletionToken(var4, var2, var1, var5);
      if (this.includeTokenInfo) {
         var6.setInfo(var3.m_deName);
      }

      Icon var7 = this.getIcon(var3);
      var6.setIcon(var7);
      return var6;
   }

   private Icon getIcon(Node var1) {
      if (var1.isFolder()) {
         return TreeMapperIcons.CLOSED_FOLDER_NODE;
      }

      Node var2 = this.findNodeInSourceTree(var1);
      return var2 != null ? var2.getNodeIcon() : var1.getNodeIcon();
   }

   private Node findNodeInSourceTree(Node var1) {
      TreeMapperSourceTree var2 = this.tree.getTreeMapper().m_sourceTreeComponent;
      return var2.findNode(var1.m_deName);
   }

   @Override
   public boolean delete(CompletionToken var1) {
      return false;
   }
}
