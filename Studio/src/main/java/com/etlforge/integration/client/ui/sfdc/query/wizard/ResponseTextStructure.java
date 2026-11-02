package com.etlforge.integration.client.ui.sfdc.query.wizard;

import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.Transformation;

public interface ResponseTextStructure {
   TextDocument getTextDocument();

   void applyTo(Transformation var1);
}
