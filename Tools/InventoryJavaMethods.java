import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreePathScanner;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public final class InventoryJavaMethods {
    private static String csv(Object value) {
        return "\"" + String.valueOf(value).replace("\"", "\"\"") + "\"";
    }
    public static void main(String[] args) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<File> files = new ArrayList<>();
        for (String root : Arrays.asList("C:/ETL/Studio/src", "C:/ETL/Server/src")) {
            try (java.util.stream.Stream<Path> paths = Files.walk(Paths.get(root))) {
                paths.filter(p -> p.toString().endsWith(".java")).forEach(p -> files.add(p.toFile()));
            }
        }
        try (StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
             PrintWriter output = new PrintWriter(args[0], "UTF-8")) {
            output.println("Path,Class,Method,Modifiers,Annotations,Disposition");
            JavacTask task = (JavacTask) compiler.getTask(null, manager, null,
                Arrays.asList("-proc:none", "-encoding", "UTF-8", "-source", "8"), null,
                manager.getJavaFileObjectsFromFiles(files));
            for (CompilationUnitTree unit : task.parse()) {
                new TreePathScanner<Void, String>() {
                    @Override public Void visitClass(ClassTree tree, String owner) {
                        String name = (owner == null ? "" : owner + ".") + tree.getSimpleName();
                        return super.visitClass(tree, name);
                    }
                    @Override public Void visitMethod(MethodTree tree, String owner) {
                        String name = tree.getName().toString();
                        if (!name.equals("<init>")) {
                            String annotations = tree.getModifiers().getAnnotations().toString();
                            String disposition = annotations.contains("Override") ? "Resolve override contract" :
                                Arrays.asList("readObject", "writeObject", "readResolve", "writeReplace", "main", "finalize").contains(name)
                                ? "Preserve required runtime entry point" : "Resolve callers and reflective references before rename";
                            output.println(csv(Paths.get(unit.getSourceFile().toUri())) + "," + csv(owner) + "," +
                                csv(name) + "," + csv(tree.getModifiers().getFlags()) + "," + csv(annotations) + "," + csv(disposition));
                        }
                        return super.visitMethod(tree, owner);
                    }
                }.scan(unit, unit.getPackageName() == null ? "" : unit.getPackageName().toString());
            }
        }
    }
}
