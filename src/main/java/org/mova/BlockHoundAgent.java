package org.mova;

import org.junit.runner.notification.RunListener;
import reactor.blockhound.BlockHound;

import java.lang.instrument.Instrumentation;
import java.security.Provider;

public class BlockHoundAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        BlockHound.install();
        System.out.println("Blockhound installed!");

    }

}