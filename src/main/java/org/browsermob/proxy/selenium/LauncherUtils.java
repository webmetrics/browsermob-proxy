package org.browsermob.proxy.selenium;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.types.FileSet;

import java.io.File;
import java.io.InputStream;

public class LauncherUtils {
    public static void copyDirectory(File source, File dest) {
        Project p = new Project();
        Copy c = new Copy();
        c.setProject(p);
        c.setTodir(dest);
        FileSet fs = new FileSet();
        fs.setDir(source);
        c.addFileset(fs);
        c.execute();
    }

    public static InputStream getSeleniumResourceAsStream(String resourceFile) {
        Class clazz = ClassPathResource.class;
        InputStream input = clazz.getResourceAsStream(resourceFile);
        if (input == null) {
            try {
                // This is hack for the OneJar version of Selenium-Server.
                // Examine the contents of the jar made by
                // https://svn.openqa.org/svn/selenium-rc/trunk/selenium-server-onejar/build.xml
                clazz = Class.forName("OneJar");
                input = clazz.getResourceAsStream(resourceFile);
            } catch (ClassNotFoundException e) {
            }
        }
        return input;
    }

    /**
	 * Delete a directory and all subdirectories
	 */
	public static void recursivelyDeleteDir(File customProfileDir) {
		if (customProfileDir == null || !customProfileDir.exists()) {
			return;
		}
		Delete delete = new Delete();
		delete.setProject(new Project());
		delete.setDir(customProfileDir);
		delete.setFailOnError(true);
		delete.execute();
	}
}
