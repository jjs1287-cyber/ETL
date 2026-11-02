package org.etlforge.integration.client.ui.interchange.entity.document.wizard;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeElement;
import org.etlforge.integration.data.structure.text.SegmentOccurrence;

final class ConversionUtils {
   private ConversionUtils() {
   }

   public static SegmentOccurrence segmentOccurrenceFromElement(TreeElement var0) {
      switch (var0.cardinality()) {
         case ONCE_ONLY:
            return SegmentOccurrence.OnlyOnce;
         case ZERO_OR_ONE:
            return SegmentOccurrence.ZeroOrOne;
         case ONE_OR_MORE:
            return SegmentOccurrence.OneOrMore;
         case ZERO_OR_MORE:
            return SegmentOccurrence.ZeroOrMore;
         default:
            assert false : var0.cardinality();
            return null;
      }
   }
}
