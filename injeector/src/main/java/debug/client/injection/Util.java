package debug.client.injection;


import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import org.apache.commons.io.IOUtils;
import sun.jvmstat.monitor.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;


public class Util {

    public static void attach(String pid,File input){
        System.setProperty("java.library.path", System.getProperty("java.home").replace("jre", "jdk") + "\\jre\\bin");
       try {
           Field field = ClassLoader.class.getDeclaredField("sys_paths");
           field.setAccessible(true);
           field.set(null, null);
       }catch (NoSuchFieldException | IllegalAccessException e) {
        e.printStackTrace();
        return;
       }
       File agentFile = new File(System.getProperty("user.home") + "/Desktop", "agent.jar");

       buildAgent(input, agentFile);
try {
    VirtualMachine virtualMachine = VirtualMachine.attach(pid);

    virtualMachine.loadAgent(agentFile.getAbsolutePath());
System.out.println("Agent loaded");


virtualMachine.detach();
System.out.println("Agent detached!");
}catch (IOException| AttachNotSupportedException | AgentInitializationException | AgentLoadException e){
    e.printStackTrace();
}
}


    private static void buildAgent(File input, File output) {
        try {
            JarFile jarFile = new JarFile(input);

            Manifest manifest = new Manifest(jarFile.getManifest());

            manifest.getMainAttributes().putValue( "Agent-Class", Agent.class.getName());
            manifest.getMainAttributes().putValue( "Can-Retransform-Classes", "true");
            manifest.getMainAttributes().putValue( "Can-Redifine-Classes",  "true");
            manifest.getMainAttributes().putValue( "Can-Set-Native-Prefix", "true");

            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(output), manifest);

            Enumeration<JarEntry> entries = jarFile.entries();

            while(entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                if(!jarEntry.getName().equals("META-INF/MANIFEST.MF")) {
                    jarOutputStream.putNextEntry(jarEntry);

                    if (!jarEntry.isDirectory()){
                        jarOutputStream.write(IOUtils.toByteArray(jarFile.getInputStream(jarEntry)));

                    jarOutputStream.closeEntry();
                    }
                }
            }

            jarFile.close();
            jarOutputStream.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    public static ArrayList<String> getProcesses() {
        ArrayList<String> list = new ArrayList<>();

        try {
            MonitoredHost monitoredHost  =  MonitoredHost.getMonitoredHost("localhost");
            for (int id: monitoredHost.activeVms()) {
                MonitoredVm monitoredVm = monitoredHost.getMonitoredVm(new VmIdentifier("//" + id));
                
                String mainClass = MonitoredVmUtil.mainClass(monitoredVm, true);

                if(!mainClass.isEmpty())
                    list.add(id + ": "+mainClass);

            }
        } catch (MonitorException | URISyntaxException e) {
            e.printStackTrace();
        } 
        return list;
    }
}
