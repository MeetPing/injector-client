package debug.client.injection;

import java.lang.instrument.Instrumentation;

public class Agent {



    public static void agentmain(String args, Instrumentation instrumentation) {
        System.out.println("Agent loaded!");
        System.out.println(1123123);
    }

}
