package org.etlforge.integration.data.structure.text;

import java.util.Collection;

public interface TextStructureValidator {
   Collection<? extends ValidationIssue> validate();
}
