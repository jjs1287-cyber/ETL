package org.etlforge.util.file;

import org.etlforge.util.name.IllegalNameException;

public interface FileNameValidator {
   void validate(String var1) throws IllegalNameException;
}
