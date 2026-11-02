package org.etlforge.integration.data.structure.text;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.KongaListUtils;

final class SegmentValidatorImpl implements TextStructureValidator {
   private final ImmutableList<Segment> segments;

   public SegmentValidatorImpl(Iterable<Segment> var1) {
      this.segments = ImmutableList.copyOf(var1);
   }

   @Override
   public Collection<? extends ValidationIssue> validate() {
      Collection var1 = null;
      if (this.segments.isEmpty()) {
         ValidationIssue var2 = ValidationIssue.FACTORY.createFatal("No segments have been defined.");
         var1 = KongaListUtils.asList(var2);
      } else {
         var1 = this.validateSegments();
      }

      return var1;
   }

   private List<ValidationIssue> validateSegments() {
      SegmentValidatorImpl.Runner var1 = new SegmentValidatorImpl.Runner();
      var1.run();
      return var1.getIssues();
   }

   private class Runner {
      private final Map<String, Set<String>> usedNames = Maps.newHashMap();
      private final Set<String> parentsWithDuplicateChildNames = Sets.newHashSet();
      private final List<Integer> missingNames = Lists.newArrayList();
      private final List<String> invalidNames = Lists.newArrayList();
      private final List<String> duplicateIdentifiers = Lists.newArrayList();
      private boolean identifierPositionsConflict;
      private final Map<Segment, List<Segment>> childSegments_map = Maps.newHashMap();

      private Runner() {
      }

      public void run() {
         int var1 = SegmentValidatorImpl.this.segments.size();

         for (int var2 = 0; var2 < var1; var2++) {
            Segment var3 = SegmentValidatorImpl.this.segments.get(var2);
            this.checkSegment(var3, var2, var1);
            Segment var4 = this.findParent(var3);
            if (var4 != null) {
               this.addChildSegment(var4, var3);
            }
         }

         this.checkParentsWithDuplicateChildNames();
      }

      private void checkSegment(Segment var1, int var2, int var3) {
         if (this.checkName(var1, var2)) {
            this.checkIdentifiers(var1, var2, var3);
            this.checkParentChildNames(var1);
         }
      }

      private boolean checkName(Segment var1, int var2) {
         String var3 = var1.getName();
         return this.checkMissingName(var3, var2) && this.checkInvalidName(var3);
      }

      private void checkIdentifiers(Segment var1, int var2, int var3) {
         if (var1.alias == null) {
            this.checkUniqueIdentifier(var1, var2, var3);
            this.checkIdentifierPosition(var1, var2, var3);
         }
      }

      private void checkParentChildNames(Segment var1) {
         String var2 = var1.getName();
         String var3 = var1.getParentName();
         Set var4 = (Set)this.usedNames.get(var3);
         if (var4 == null) {
            var4 = Sets.newHashSet();
            var4.add(var2);
            this.usedNames.put(var3, var4);
         } else {
            if (!var4.contains(var2)) {
               var4.add(var2);
            }
         }
      }

      private Segment findParent(Segment var1) {
         Segment var2 = null;
         String var3 = var1.getParentName();
         if (StringUtils.isEmpty(var3)) {
            return var2;
         }

         for (Segment var5 : SegmentValidatorImpl.this.segments) {
            if (var3.equals(var5.getName())) {
               var2 = var5;
            }

            if (var5 == var1) {
               break;
            }
         }

         return var2;
      }

      public List<ValidationIssue> getIssues() {
         ArrayList var1 = Lists.newArrayList();

         for (Integer var3 : this.missingNames) {
            var1.add(ValidationIssue.FACTORY.createFatal("Field without name: " + var3));
         }

         for (String var7 : this.invalidNames) {
            var1.add(ValidationIssue.FACTORY.createFatal("Invalid name: " + var7));
         }

         for (String var8 : this.parentsWithDuplicateChildNames) {
            var1.add(ValidationIssue.FACTORY.createWarning("Parent with duplicate child name: " + var8));
         }

         for (String var9 : this.duplicateIdentifiers) {
            var1.add(ValidationIssue.FACTORY.createWarning(var9));
         }

         if (this.identifierPositionsConflict) {
            var1.add(ValidationIssue.FACTORY.createWarning("   All identifiers must have the same start position"));
         }

         return var1;
      }

      private boolean checkMissingName(String var1, int var2) {
         if (StringUtils.isBlank(var1)) {
            this.missingNames.add(var2 + 1);
            return false;
         } else {
            return true;
         }
      }

      private boolean checkInvalidName(String var1) {
         if (TextStructureNameUtils.isFieldNameIllegal(var1)) {
            this.invalidNames.add(var1);
            return false;
         } else {
            return true;
         }
      }

      private void checkUniqueIdentifier(Segment var1, int var2, int var3) {
         Field var4 = var1.findIdentField();
         if (var4 != null) {
            String var5 = var4.format;

            for (int var6 = var2 + 1; var6 < var3; var6++) {
               Segment var7 = SegmentValidatorImpl.this.segments.get(var6);
               if (var7.alias != var1) {
                  Field var8 = var7.findIdentField();
                  if (var8 != null) {
                     String var9 = var8.format;
                     if (var5.equals(var9)) {
                        this.duplicateIdentifiers.add("   Segments " + var7.getName() + " and " + var1.getName() + " have same identifer \"" + var5 + "\"");
                     }
                  }
               }
            }
         }
      }

      private void checkIdentifierPosition(Segment var1, int var2, int var3) {
         Field var4 = var1.findIdentField();
         if (var4 != null) {
            int var5 = var4.getBeginPosition();

            for (int var6 = var2 + 1; var6 < var3; var6++) {
               Segment var7 = SegmentValidatorImpl.this.segments.get(var6);
               Field var8 = var7.findIdentField();
               if (var8 != null && var5 != var8.getBeginPosition()) {
                  this.identifierPositionsConflict = true;
                  return;
               }
            }
         }
      }

      private void addChildSegment(Segment var1, Segment var2) {
         assert var1 != null;
         List var3 = this.childSegments_map.get(var1);
         if (var3 == null) {
            var3 = Lists.newArrayList();
            this.childSegments_map.put(var1, var3);
         }

         var3.add(var2);
      }

      private void checkParentsWithDuplicateChildNames() {
         for (Entry var2 : this.childSegments_map.entrySet()) {
            String var3 = ((Segment)var2.getKey()).getName();
            List var4 = (List)var2.getValue();

            for (Object __etlItem220 : var4) {
               Segment var6 = (Segment) __etlItem220;
               for (Object __etlItem221 : var4) {
                  Segment var8 = (Segment) __etlItem221;
                  if (var6 == var8) {
                     break;
                  }

                  if (var6.getName().equals(var8.getName())) {
                     this.parentsWithDuplicateChildNames.add(var3);
                  }
               }
            }
         }
      }
   }
}
