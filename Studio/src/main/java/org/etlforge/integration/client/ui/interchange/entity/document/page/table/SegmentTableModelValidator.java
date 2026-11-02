package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import java.util.Iterator;
import org.etlforge.integration.data.structure.text.Segment;
import org.etlforge.integration.data.structure.text.TextStructureValidator;
import org.etlforge.integration.data.structure.text.TextValidatorFactory;

final class SegmentTableModelValidator extends AbstractModelValidator {
   private final SegmentTableModel model;

   SegmentTableModelValidator(SegmentTableModel var1) {
      this.model = var1;
   }

   @Override
   protected TextStructureValidator getValidator() {
      TextValidatorFactory var1 = new TextValidatorFactory();
      return var1.createSegmentValidator(new Iterable<Segment>() {
         @Override
         public Iterator<Segment> iterator() {
            return SegmentTableModelValidator.this.model.getSegmentIterator();
         }
      });
   }
}
