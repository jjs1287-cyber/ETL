package org.etlforge.cli;

import java.util.Set;

public interface CommandCollection extends Command, Iterable<Command> {
   Set<String> collectCompletions(String var1);
}
