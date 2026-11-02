package org.etlforge.integration.data.script;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.Map.Entry;
import org.etlforge.integration.data.script.de.AccessType;
import org.etlforge.integration.data.script.de.DataElementReference;
import org.etlforge.integration.dataelement.GlobalDE;
import org.etlforge.lang.KongaStringUtils;

final class DataElementExtractor {
   private final Map<String, EnumSet<AccessType>> globalDEs = Maps.newHashMap();
   private final SortedSet<String> sourceDEs = Sets.newTreeSet(String.CASE_INSENSITIVE_ORDER);
   private final SortedSet<String> localDEs = Sets.newTreeSet(String.CASE_INSENSITIVE_ORDER);
   private boolean sourceDataElementAware;

   public void setSourceDataElementAware(boolean var1) {
      this.sourceDataElementAware = var1;
   }

   public void parse(List<Transform.Token> var1) {
      DataElementExtractor.Parser var2 = new DataElementExtractor.Parser();
      var2.parse(var1);
      for (Transform.Token token : var1) {
         if (token.m_id == 401 && token.m_str.startsWith("<javascript>")) {
            for (String reference : NativeJavaScript.references(token.m_str)) {
               if (reference.startsWith("S:")) this.sourceDEs.add(reference.substring(2));
               else if (reference.startsWith("G:")) this.globalDEs.put(reference.substring(3), EnumSet.of(AccessType.GET, AccessType.SET));
            }
         }
      }
   }

   public ImmutableSet<DataElementReference> getGlobalDataElementReferences(ExpressionSource var1) {
      Builder var2 = ImmutableSet.builder();

      for (Entry var4 : this.globalDEs.entrySet()) {
         var2.add(new DataElementReference((String)var4.getKey(), var1, (EnumSet<AccessType>)var4.getValue()));
      }

      return var2.build();
   }

   public ImmutableSortedSet<String> getSourceDataElements() {
      return ImmutableSortedSet.copyOf(this.sourceDEs);
   }

   public ImmutableSortedSet<String> getLocalDataElements() {
      return ImmutableSortedSet.copyOf(this.localDEs);
   }

   private final class Parser {
      private boolean isRead;
      private int index;
      private List<Transform.Token> tokens;

      private Parser() {
      }

      public void parse(List<Transform.Token> var1) {
         this.tokens = var1;

         for (Transform.Token var3 : var1) {
            this.processToken(var3);
            this.index++;
         }
      }

      private void processToken(Transform.Token var1) {
         switch (var1.m_id) {
            case 101:
               this.checkSetAndGet(var1);
               break;
            case 320:
               this.isRead = true;
               break;
            case 405:
               this.processSourceDataElement(var1);
               break;
            case 406:
               this.processGlobalDataElement(var1);
               break;
            case 408:
               this.processLocalDataElement(var1);
            case 1100:
            case 1101:
               break;
            default:
               this.isRead = false;
         }
      }

      private void processGlobalDataElement(Transform.Token var1) {
         if (var1.m_str.length() > 1) {
            AccessType var2 = this.getAccessType();
            String var3 = GlobalDE.nameOf(var1.m_str);
            this.addAccess(var3, var2);
         }
      }

      private AccessType getAccessType() {
         AccessType var1 = AccessType.UNKNOWN;
         if (this.isRead) {
            var1 = AccessType.GET;
         } else {
            var1 = this.isAssignment(this.tokens, this.index + 1) ? AccessType.SET : AccessType.GET;
         }

         return var1;
      }

      private boolean isAssignment(List<Transform.Token> var1, int var2) {
         int var3 = 0;
         int var4 = var2;

         for (int var5 = var1.size(); var4 < var5; var4++) {
            Transform.Token var6 = (Transform.Token)var1.get(var4);
            switch (var6.m_id) {
               case 320:
                  return true;
               case 1004:
                  var3++;
                  break;
               case 1005:
                  var3--;
               case 1100:
               case 1101:
                  break;
               default:
                  if (var3 <= 0) {
                     return false;
                  }
            }
         }

         return false;
      }

      private void checkSetAndGet(Transform.Token var1) {
         if ("set(".equalsIgnoreCase(var1.m_str) || "get(".equalsIgnoreCase(var1.m_str)) {
            String var2 = this.getFirstStringArgument(this.tokens, this.index + 1);
            if (var2 != null) {
               AccessType var3 = "set(".equalsIgnoreCase(var1.m_str) ? AccessType.SET : AccessType.GET;
               this.addAccess(var2, var3);
            }
         }
      }

      private void processSourceDataElement(Transform.Token var1) {
         if (DataElementExtractor.this.sourceDataElementAware) {
            DataElementExtractor.this.sourceDEs.add(var1.m_str);
         }

         this.isRead = false;
      }

      private void processLocalDataElement(Transform.Token var1) {
         DataElementExtractor.this.localDEs.add(var1.m_str);
         this.isRead = false;
      }

      private void addAccess(String var1, AccessType var2) {
         EnumSet var3 = DataElementExtractor.this.globalDEs.get(var1);
         if (var3 == null) {
            var3 = var2 != null ? EnumSet.of(var2) : EnumSet.noneOf(AccessType.class);
            DataElementExtractor.this.globalDEs.put(var1, var3);
         } else if (var2 != null) {
            var3.add(var2);
         }
      }

      private String getFirstStringArgument(List<Transform.Token> var1, int var2) {
         String var3 = null;
         int var4 = var2;

         for (int var5 = var1.size(); var4 < var5; var4++) {
            Transform.Token var6 = (Transform.Token)var1.get(var4);
            switch (var6.m_id) {
               case 200:
               case 1000:
                  return var3;
               case 401:
                  var3 = this.removeBeginAndEndQuotes(var6);
                  break;
               case 1100:
               case 1101:
               default:
                  return null;
            }
         }

         return null;
      }

      private String removeBeginAndEndQuotes(Transform.Token var1) {
         return KongaStringUtils.unwrap(var1.m_str);
      }
   }
}
