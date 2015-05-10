/**
 * Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 * <p/>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.gnu.org/licenses/gpl.html
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.template.compiler;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.tinygroup.commons.file.IOUtils;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.impl.ClassName;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MemorySourceCompiler {
    private String outputDir = TEMP_DIR + (TEMP_DIR.endsWith(File.separator) ? "" : File.separatorChar) + "ttl" + File.separatorChar;


    public <T> Class<T> loadClass(String engineId, MemorySource source) throws TemplateException {
        return loadClass(MemorySourceCompiler.class.getClassLoader(), engineId, source);
    }

    public <T> T loadClass(ClassLoader classLoader, String engineId, MemorySource source) throws TemplateException {
        compile(engineId, source);
        return (T) loadInstance(classLoader, engineId, source.getQualifiedClassName());
    }

    public <T> Class<T> getClass(ClassLoader classLoader, String engineId, String className) throws TemplateException {
        try {
            URL[] urls = new URL[1];
            File file = new File(getOutputDir()+engineId+File.separator);
            urls[0] = file.toURI().toURL();
            if (classLoader == null) {
                return (Class<T>) new URLClassLoader(urls).loadClass(className);
            } else {
                return (Class<T>) new URLClassLoader(urls, classLoader).loadClass(className);
            }
        } catch (Exception e) {
            throw new TemplateException(e);
        }
    }

    public <T> T loadInstance(String engineId, MemorySource source) throws TemplateException, InstantiationException, IllegalAccessException {
        return (T) loadInstance(MemorySourceCompiler.class.getClassLoader(), engineId, source);
    }

    public <T> T loadInstance(ClassLoader classLoader, String engineId, MemorySource source) throws TemplateException {
        return (T) loadInstance(classLoader, engineId, source.getQualifiedClassName());
    }

    public static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public <T> T loadInstance(ClassLoader classLoader, String engineId, String className) throws TemplateException {
        try {
            return (T) getClass(classLoader, engineId, className).newInstance();
        } catch (InstantiationException e) {
            throw new TemplateException(e);
        } catch (IllegalAccessException e) {
            throw new TemplateException(e);
        }
    }

    public boolean isModified(ClassName className, String engineId, String content) {
        String classFileName = className.getClassName().replaceAll("[.]", "\\/") + ".class";
        String javaFileName = className.getClassName().replaceAll("[.]", "\\/") + ".java";
        File classFile = new File(outputDir + engineId + File.separator, classFileName);
        File javaFile = new File(outputDir + engineId + File.separator, javaFileName);
        try {
            if (javaFile.exists()) {
                String contentInDisk = IOUtils.readFromInputStream(new FileInputStream(javaFile), "UTF-8");
                if (contentInDisk.equals(content) && classFile.exists()) {
                    return false;
                }
            }
        } catch (Throwable e) {
            //只要出错就认为不存在
        }
        return true;
    }


    class NameEnvironment implements INameEnvironment {

        private final MemorySource[] sources;
        String engineId;

        public NameEnvironment(String engineId, MemorySource[] sources) {
            this.sources = sources.clone();
            this.engineId = engineId;
        }

        /**
         * @param compoundTypeName {{'j','a','v','a'}, {'l','a','n','g'}}
         */
        public NameEnvironmentAnswer findType(final char[][] compoundTypeName) {
            return findType(join(compoundTypeName));
        }

        public NameEnvironmentAnswer findType(final char[] typeName, final char[][] packageName) {
            return findType(join(packageName) + "." + new String(typeName));
        }

        /**
         * @param name like `aaa`,`aaa.BBB`,`java.lang`,`java.lang.String`
         */
        private NameEnvironmentAnswer findType(final String name) {

            // check data dir first
            for (MemorySource source : sources) {
                if (name.equals(source.getQualifiedClassName())) {
                    return new NameEnvironmentAnswer(new CompilationUnit(source), null);
                }
            }
            // find by system
            try {
                InputStream input = this.getClass().getClassLoader().getResourceAsStream(name.replace(".", "/") + ".class");
                if (input != null) {

                    byte[] bytes = readByteArray(input);
                    if (bytes != null) {
                        ClassFileReader classFileReader = new ClassFileReader(bytes, name.toCharArray(), true);
                        return new NameEnvironmentAnswer(classFileReader, null);
                    }
                }
            } catch (ClassFormatException e) {
                // Something very very bad
                throw new RuntimeException(e);
            }

            return null;
        }

        public boolean isPackage(char[][] parentPackageName, char[] packageName) {
            String name = new String(packageName);
            if (parentPackageName != null) {
                name = join(parentPackageName) + "." + name;
            }

            File target = new File(outputDir, name.replace('.', '/'));

            // only return false if it's a file
            // return true even if it doesn't exist
            return !target.isFile();
        }

        public void cleanup() {
        }
    }

    public void compile(String engineId, Collection<MemorySource> source) {
        compile(engineId, source.toArray(new MemorySource[0]));
    }

    public void compile(String engineId, MemorySource source) {
        MemorySource[] sources = new MemorySource[1];
        sources[0] = source;
        compile(engineId, sources);
    }

    public void compile(String engineId, final MemorySource[] sources) {
        for (MemorySource source : sources) {
            String javaFileName = source.getQualifiedClassName().replaceAll("[.]", "/") + ".java";
            try {
                File file = new File(outputDir + engineId + File.separator, javaFileName);
                File path = file.getParentFile();
                if (!path.exists()) {
                    path.mkdirs();
                }
                IOUtils.writeToOutputStream(new FileOutputStream(file), source.getContent(), "UTF-8");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * To find types ...
         */
        INameEnvironment nameEnvironment = new NameEnvironment(engineId, sources);
        /**
         * Compilation result
         */
        ICompilerRequestor compilerRequestor = new CompilerRequestor(sources[0],engineId);

        IProblemFactory problemFactory = new DefaultProblemFactory(Locale.CHINA);
        IErrorHandlingPolicy policy = DefaultErrorHandlingPolicies.exitOnFirstError();

        /**
         * The JDT compiler
         */
        Compiler jdtCompiler = new Compiler(
                nameEnvironment, policy, getCompilerOptions(), compilerRequestor, problemFactory);

        // Go !

        ICompilationUnit[] units = new ICompilationUnit[sources.length];
        for (int i = 0; i < sources.length; i++) {
            units[i] = new CompilationUnit(sources[i]);
        }
        jdtCompiler.compile(units);
    }

    public static CharSequence getPrettyError(String[] sourceLines, int line, int inputColumn, int start, int stop, int showLines) {
        int column = inputColumn;
        StringBuilder sb = new StringBuilder(128);
        for (int i = line - showLines; i < line; i++) {
            if (i >= 0) {
                String sourceLine = sourceLines[i];

                // 1 个 Tab 变成 4 个空格
                if (i == line - 1) {
                    int originColumn = Math.min(column, sourceLine.length() - 1);
                    for (int j = 0; j < originColumn; j++) {
                        char c = sourceLine.charAt(j);
                        if (c == '\t') {
                            column += 3;
                        } else if (c >= '\u2E80' && c <= '\uFE4F') {
                            column++;
                        }
                    }
                }
                sourceLine = sourceLine.replaceAll("\\t", "    ");
                sb.append(String.format("%4d: %s%n", i + 1, sourceLine));
            }
        }
        if (start > stop) {
            // <EOF>
            sb.append("      <EOF>\n");
            sb.append("      ^^^^^");
        } else {
            sb.append("      ");
            for (int i = 0; i < column - 1; i++) {
                sb.append(' ');
            }
            for (int i = start; i <= stop; i++) {
                sb.append('^');
            }
        }
        sb.append('\n');
        return sb;
    }

    private static byte[] readByteArray(InputStream input) {
        BufferedInputStream stream = new BufferedInputStream(input);
        try {
            byte[] buf = new byte[stream.available()];
            stream.read(buf);
            stream.close();
            input.close();
            return buf;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveClassFile(String engineId, ClassFile[] classFiles) {
        if (classFiles == null) {
            return;
        }

        for (int i = 0; i < classFiles.length; i++) {
            try {
                String fileName = new String(classFiles[i].fileName()) + ".class";
                File javaClassFile = new File(outputDir + engineId, fileName);
                if (javaClassFile.exists()) {
                    javaClassFile.delete();
                }
                File pa = javaClassFile.getParentFile();
                if (!pa.exists()) {
                    pa.mkdirs();
                }
                FileOutputStream fout = new FileOutputStream(javaClassFile);
                byte[] bytes = classFiles[i].getBytes();
                fout.write(bytes);
                fout.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static CompilerOptions getCompilerOptions() {
        Map settings = new HashMap();
        settings.put(CompilerOptions.OPTION_ReportMissingSerialVersion, CompilerOptions.IGNORE);
        settings.put(CompilerOptions.OPTION_LineNumberAttribute, CompilerOptions.GENERATE);
        settings.put(CompilerOptions.OPTION_SourceFileAttribute, CompilerOptions.GENERATE);
        settings.put(CompilerOptions.OPTION_ReportDeprecation, CompilerOptions.IGNORE);
        settings.put(CompilerOptions.OPTION_ReportUnusedImport, CompilerOptions.IGNORE);
        settings.put(CompilerOptions.OPTION_Encoding, "UTF-8");
        settings.put(CompilerOptions.OPTION_LocalVariableAttribute, CompilerOptions.GENERATE);
        String javaVersion = CompilerOptions.VERSION_1_5;
        if (System.getProperty("java.version").startsWith("1.6")) {
            javaVersion = CompilerOptions.VERSION_1_6;
        } else if (System.getProperty("java.version").startsWith("1.7")) {
            javaVersion = CompilerOptions.VERSION_1_7;
        }
        settings.put(CompilerOptions.OPTION_Source, javaVersion);
        settings.put(CompilerOptions.OPTION_TargetPlatform, javaVersion);
        settings.put(CompilerOptions.OPTION_PreserveUnusedLocal, CompilerOptions.PRESERVE);
        settings.put(CompilerOptions.OPTION_Compliance, javaVersion);
        return new CompilerOptions(settings);
    }

    private static class CompilationUnit implements ICompilationUnit {


        private final MemorySource source;

        public CompilationUnit(MemorySource source) {
            this.source = source;
        }


        public char[] getContents() {
            return source.getContent().toCharArray();
        }


        public char[] getMainTypeName() {
            return source.getSimpleName().toCharArray();
        }


        public char[][] getPackageName() {
            String[] names = source.getQualifiedClassName().split("[.]");
            char[][] result = new char[names.length - 1][];
            for (int i = 0; i < names.length - 1; i++) {
                result[i] = names[i].toCharArray();
            }
            return result;
        }


        public boolean ignoreOptionalProblems() {
            return false;
        }


        public char[] getFileName() {
            return (source.getSimpleName() + ".java").toCharArray();
        }
    }

    private static String join(char[][] chars) {
        StringBuilder sb = new StringBuilder();
        for (char[] item : chars) {
            if (sb.length() > 0) {
                sb.append(".");
            }
            sb.append(item);
        }
        return sb.toString();
    }
    class CompilerRequestor implements ICompilerRequestor {
        private final String engineId;
        private final MemorySource source;

        CompilerRequestor(MemorySource source, String engineId){
            this.source=source;
            this.engineId=engineId;
        }
        public void acceptResult(CompilationResult result) {
            if (result.hasErrors()) {
                String sourceCode = source.getContent();
                String[] sourceCodeLines = sourceCode.split("(\r\n|\r|\n)", -1);
                StringBuilder sb = new StringBuilder();
                sb.append("Compilation failed.");
                sb.append('\n');
                for (IProblem p : result.getErrors()) {
                    sb.append(p.getMessage()).append('\n');
                    int start = p.getSourceStart();
                    int column = start;
                    for (int i = start; i >= 0; i--) {
                        char c = sourceCode.charAt(i);
                        if (c == '\n' || c == '\r') {
                            column = start - i;
                            break;
                        }
                    }
                    sb.append(MemorySourceCompiler.getPrettyError(sourceCodeLines, p.getSourceLineNumber(), column, p.getSourceStart(), p.getSourceEnd(), 3));
                }
                sb.append(result.getErrors().length);
                sb.append(" error(s)\n");
                throw new RuntimeException(sb.toString());
            } else {
                saveClassFile(engineId, result.getClassFiles());
            }

        }
    }
}