package debug.client.injection;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
Util.getProcesses().forEach(System.out::println);

Scanner scanner = new Scanner(System.in);

String pid = scanner.nextLine();

File file = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());

Util.attach(pid,file);


    }
}
