package simkit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import simkit.random.RandomNumber;
import simkit.random.RandomVariate;

/**
 * <p>
 * An INSTANCE of this class scans the classpath and creates a Map with keys the
 * unqualified class name and values the corresponding Class object. The first
 * found Class object is the one stored.</p>
 * <p>
 * Both jar files and directories on the classpath are scanned. If the user
 * wishes to skip certain jar files, they are listed in the
 * <code>config/ClassFinder.properties</code> file. Also in that file is a
 * directory specified by the key <code>first</code>. This directory is expected
 * to contain jar files that are scanned before any others on the classpath.
 * This allows the user to add classes that will be found and instantiated by
 * the <code>ObjectMaker</code>.</p>
 *
 * @author ahbuss
 */
public class ClassFinder {

    private static final Logger LOGGER = Logger.getLogger(ClassFinder.class.getName());

    private static final String DEFAULT_EXT_DIR = "ext";

    private static final ClassFinder INSTANCE;
    
    private Locale locale;

    static {
        INSTANCE = new ClassFinder();
    }

    /**
     * @return the INSTANCE
     */
    public static ClassFinder getINSTANCE() {
        return INSTANCE;
    }

    private final Map<String, Class<?>> foundByQualifiedName;

    private final Map<String, Class<? extends RandomVariate>> randomVariateClasses;

    private final Map<String, Class<? extends RandomNumber>> randomNumberClasses;

    private final List<URL> jarFileURLs;

    private final List<URL> dirURLs;

    private final List<String> skippedJars;

    private URLClassLoader urlClassLoader;

    private String firstDirectory;

    protected ClassFinder() {
        locale = Locale.getDefault();
        this.firstDirectory = DEFAULT_EXT_DIR;
        foundByQualifiedName = new HashMap<>();
        randomVariateClasses = new HashMap<>();
        randomNumberClasses = new HashMap<>();
        jarFileURLs = new ArrayList<>();
        dirURLs = new ArrayList<>();
        skippedJars = new ArrayList<>();
        urlClassLoader = null;
        loadConfigFile();
        findJarFiles();
        loadClasses();
    }

    /**
     * Load the skipped jar names from config/skippedJars.properties into
     * skippedJars List
     */
    private void loadConfigFile() {
        FileInputStream fileInputStream = null;
        try {
            File configFile = new File("config/ClassFinder.properties");
            if (configFile.exists()) {
                Properties properties = new Properties();
                fileInputStream = new FileInputStream(configFile);
                properties.load(fileInputStream);
                for (Object obj : properties.keySet()) {
                    if (obj.equals("first")) {
                        firstDirectory = properties.get(obj).toString();
                    } else if (obj.toString().toLowerCase(locale).endsWith(".jar")
                            && properties.getProperty(obj.toString()).trim().equals("skip")) {
                        skippedJars.add(obj.toString());
                    }
                }
            } else {
                LOGGER.log(Level.FINE, "No config file found: {0}", configFile.getAbsolutePath());
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Finds all the jar files on the class path - first the ones in the "first"
     * directory, then the ones not listed in skippedJars.properties
     */
    private void findJarFiles() {
        File firstDirFile = new File(firstDirectory);
        if (firstDirFile.exists()) {
            File[] firstDirJars = firstDirFile.listFiles(new JarFilter());
            for (File jarFile : firstDirJars) {
                try {
                    jarFileURLs.add(jarFile.toURI().toURL());
                } catch (MalformedURLException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        } else {
            LOGGER.log(Level.FINE, "No extension directory named {0} found", firstDirFile.getAbsolutePath());
        }
        String[] classPathElements
                = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
        for (String classpathItem : classPathElements) {
            File classPathFile = new File(classpathItem);
            try {
                if (!classPathFile.getName().equalsIgnoreCase(firstDirectory)
                        && !skippedJars.contains(classPathFile.getName())) {
                    if (classPathFile.isFile() && classPathFile.getName().toLowerCase(locale).endsWith(".jar")) {
                        jarFileURLs.add(classPathFile.toURI().toURL());
                    } else if (classPathFile.isDirectory()) {
                        dirURLs.add(classPathFile.toURI().toURL()); // TODO: process directories with class files
                    }
                }
            } catch (MalformedURLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        List<URL> allURLS = new ArrayList<>(jarFileURLs);
        allURLS.addAll(dirURLs);
        urlClassLoader = new URLClassLoader(allURLS.toArray(new URL[0]));
    }

    /**
     * Finds and loads all classes on classpath. Class objects are stored in
     * foundByQualifiedName Map keyed by their unqualified name. Jar files are
     * loaded from the ones in jarFileURLs and compiled class files from the
     * dirURLs Lists.
     */
    @SuppressWarnings("unchecked")
    private void loadClasses() {
        for (URL url : jarFileURLs) {
            JarFile jarFile = null;
            try {
                String decodedFileName = URLDecoder.decode(url.getFile(), "UTF-8");
                 jarFile = new JarFile(decodedFileName);
                for (Enumeration<JarEntry> entryEnum = jarFile.entries();
                        entryEnum.hasMoreElements();) {
                    JarEntry nextEntry = entryEnum.nextElement();
                    if (nextEntry.getName().endsWith(".class")) {
                        Class<?> theClass = null;
                        try {
                            theClass = loadClassFromJar(nextEntry);
                            String unqualifiedName = findUnqualifiedNameFor(theClass);
                            if (!foundByQualifiedName.containsKey(unqualifiedName)) {
                                foundByQualifiedName.put(unqualifiedName, theClass);
                            }
                            foundByQualifiedName.put(theClass.getName(), theClass);
                            if (RandomVariate.class.isAssignableFrom(theClass)) {
                                randomVariateClasses.put(theClass.getName(), (Class<? extends RandomVariate>) theClass);
                                randomVariateClasses.put(theClass.getName().replace("Variate", ""), (Class<? extends RandomVariate>) theClass);
                                randomVariateClasses.put(theClass.getSimpleName().replace("Variate", ""), (Class<? extends RandomVariate>) theClass);
                                randomVariateClasses.put(findUnqualifiedNameFor(theClass), (Class<? extends RandomVariate>) theClass);
                            }
                            if (RandomNumber.class.isAssignableFrom(theClass)) {
                                randomNumberClasses.put(theClass.getName(), (Class<? extends RandomNumber>) theClass);
                                randomNumberClasses.put(theClass.getSimpleName(), (Class<? extends RandomNumber>) theClass);
                            }

                        } catch (ClassNotFoundException | NoClassDefFoundError ex) {
                            LOGGER.log(Level.FINE, "Jarfile {0} can''t load class {1}",
                                    new Object[]{jarFile.getName(), nextEntry});
                        }
                    }
                }
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            } finally {
                if (jarFile != null) {
                    try {
                        jarFile.close();
                    } catch (IOException ex) {
                        LOGGER.log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        for (URL url : dirURLs) {
            try {
                String decodedFile = URLDecoder.decode(url.getFile(), "UTF-8");
                File dirFile = new File(decodedFile);
                loadClassesFromDirectory(dirFile, dirFile);
            } catch (UnsupportedEncodingException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Attempt to load the given file in the class loader, or all the files in
     * the subdirectory if given file (second argument) is a directory. This is
     * a recursive method: it loads found .class files and adds them to the
     * foundByQualifiedName Map, and recursively calls itself with its children
     * if a directory. The first argument (dirFile) should be the directory that
     * is on the classpath. This is used to trimmed the leading part of the path
     * so as to get the fully qualified class name when the ".class" extension
     * is stripped off and the path separators are replaced by ".". The initial
     * call should be <code>loadClassesFromDirectory(dirFile, dirFile);</code>
     * where <code>dirFile</code> is the directory on the classpath.
     *
     * @param dirFile Parent directory from classpath
     * @param file directory or file; if file, attempt to load it if it is a
     * .class file
     */
    @SuppressWarnings("unchecked")
    private void loadClassesFromDirectory(File dirFile, File file) {
        if (file.isFile()) {
            if (file.getName().endsWith(".class") && !file.getName().contains("$")) {
                try {
                    String className = fixFullPathName(dirFile, file);
                    Class<?> theClass = urlClassLoader.loadClass(className);
                    foundByQualifiedName.put(findUnqualifiedNameFor(theClass), theClass);
                    foundByQualifiedName.put(theClass.getName(), theClass);
                    if (RandomVariate.class.isAssignableFrom(theClass)) {
                        randomVariateClasses.put(theClass.getName(), (Class<? extends RandomVariate>) theClass);
                        randomVariateClasses.put(theClass.getName().replace("Variate", ""), (Class<? extends RandomVariate>) theClass);
                        randomVariateClasses.put(theClass.getSimpleName().replace("Variate", ""), (Class<? extends RandomVariate>) theClass);
                        randomVariateClasses.put(findUnqualifiedNameFor(theClass), (Class<? extends RandomVariate>) theClass);
                    }
                    if (RandomNumber.class.isAssignableFrom(theClass)) {
                        randomNumberClasses.put(theClass.getName(), (Class<? extends RandomNumber>) theClass);
                        randomNumberClasses.put(theClass.getSimpleName(), (Class<? extends RandomNumber>) theClass);
                    }
                } catch (ClassNotFoundException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        } else {
            for (File child : file.listFiles()) {
                loadClassesFromDirectory(dirFile, child);
            }
        }

    }

    /**
     * Trims off ".class" from the end of the file name and substitutes "." for
     * file separators ("/" on Mac and *nix, "\" on Windows)
     *
     * @param parent directory given file is in
     * @param file Given file to fix name
     * @return fully qualified name of class based on its name and directory
     * location
     */
    protected String fixFullPathName(File parent, File file) {
        String trimmed = file.getAbsolutePath().substring(parent.getAbsolutePath().length() + 1,
                file.getAbsolutePath().lastIndexOf("."));
        return trimmed.replace(System.getProperty("file.separator"), ".");
    }

    /**
     * Loads a class corresponding to a JarEntry into the urlClassLoader. The
     * entry should corresponding to a compiled class file (as opposed to, say,
     * the Manifest file).
     *
     * @param jarEntry Given JarEntry
     * @return Class file that was loaded into urlClassLoader
     * @throws java.lang.ClassNotFoundException if can't load the class
     */
    protected Class<?> loadClassFromJar(JarEntry jarEntry) throws ClassNotFoundException {
        String fullyQualifiedName
                = jarEntry.getName().replace("/", ".");
        fullyQualifiedName = fullyQualifiedName.replaceAll("\\.class$", "");
        Class<?> theClass = null;
        theClass = urlClassLoader.loadClass(fullyQualifiedName);
        return theClass;
    }

    /**
     *
     * @param theClass Given Class
     * @return unqualified name of class (i.e. without package name)
     */
    protected String findUnqualifiedNameFor(Class<?> theClass) {
        String name = theClass.getName();
        name = name.substring(name.lastIndexOf(".") + 1);
        return name;
    }

    /**
     *
     * @return a copy of the Map from unqualified class names to the
     * corresponding classes.
     */
    public Map<String, Class<?>> getFoundByQualifiedName() {
        return new TreeMap<>(foundByQualifiedName);
    }

    /**
     * This returns the first Class found on the classpath. If more than one
     * unqualified name is used, then subsequent ones must use the corresponding
     * fully qualified name.
     *
     * @param unqualifiedName Name of unqualified class
     * @return Class object corresponding to given unqualified name
     */
    public Class<?> findClassByUnqualifiedName(String unqualifiedName) {
        return foundByQualifiedName.get(unqualifiedName);
    }

    /**
     * @return the randomVariateClasses
     */
    public Map<String, Class<? extends RandomVariate>> getRandomVariateClasses() {
        return new HashMap<>(randomVariateClasses);
    }

    /**
     * @return the randomNumberClasses
     */
    public Map<String, Class<? extends RandomNumber>> getRandomNumberClasses() {
        return randomNumberClasses;
    }

    /**
     * Filter to only accept files that end with ".jar"
     */
    private class JarFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase(locale).endsWith(".jar");
        }

    }

}
