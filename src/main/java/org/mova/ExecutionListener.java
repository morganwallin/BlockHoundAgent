package org.mova;


import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import reactor.blockhound.BlockingOperationError;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExecutionListener extends RunListener {
    /**
     * Called before any tests have been run.
     * */
    String fileName;

    public void sayHi() {
        System.out.println("Hi");
    }

    public void testRunStarted(Description description) throws java.lang.Exception
    {
        fileName = description.toString();
        writeLogFile(fileName, false, "Number of tests to execute: " + description.testCount() + "\n");
        System.out.println("Number of tests to execute: " + description.testCount());
    }

    /**
     *  Called when all tests have finished
     * */
    public void testRunFinished(Result result) throws java.lang.Exception
    {
        int successfulTests = result.getRunCount() - result.getIgnoreCount() - result.getFailureCount();
        writeLogFile(fileName, true, "Number of tests executed: " + result.getRunCount() + "\n" +
                "Number of tests successful: " + successfulTests + "\n" +
                "Number of tests ignored: " + result.getIgnoreCount() + "\n" +
                "Failures: " + result.getFailureCount() + "\n" +
                "Successful: " + result.wasSuccessful() + "\n");
        System.out.println("Number of tests executed : " + result.getRunCount() + "\n");
        System.out.println("Number of tests successful:" + successfulTests + "\n");
        System.out.println("Number of tests ignored: " + result.getIgnoreCount() + "\n");
        System.out.println("Failures: " + result.getFailureCount() + "\n");
        System.out.println("Successful: " + result.wasSuccessful() + "\n");
        System.exit(0);
    }

    /**
     *  Called when an atomic test is about to be started.
     * */
    public void testStarted(Description description) throws java.lang.Exception
    {
        writeLogFile(fileName, true, "Starting execution of test case: "+ description.getMethodName() + "\n");
        System.out.println("Starting execution of test case : "+ description.getMethodName());
    }

    /**
     *  Called when an atomic test has finished, whether the test succeeds or fails.
     * */
    public void testFinished(Description description) throws java.lang.Exception
    {
        writeLogFile(fileName, true, "Finished execution of test case: "+ description.getMethodName() + "\n");
        System.out.println("Finished execution of test case : "+ description.getMethodName());
    }

    /**
     *  Called when an atomic test fails.
     * */
    public void testFailure(Failure failure) throws java.lang.Exception
    {
        if(failure.getTrace().contains("BlockingOperationError") || failure.getMessage().contains("BlockingOperationError") || failure.getException() instanceof BlockingOperationError)  {
            writeLogFile(fileName, true, "\nTest failed because of blocking call: " + failure.getDescription().getMethodName() + "\n" +
                    failure.getTestHeader() + "\n" + failure.getMessage() + "\n" + failure.getException().toString() + "\n\n\n" + failure.getTrace() + "\n");
        }

        System.out.println("Execution of test case failed : " + failure.getMessage());
    }

    /**
     *  Called when a test will not be run, generally because a test method is annotated with Ignore.
     * */
    public void testIgnored(Description description) throws java.lang.Exception
    {
        writeLogFile(fileName, true, "Execution of test case ignored: "+ description.getMethodName() + "\n");
        System.out.println("Execution of test case ignored : "+ description.getMethodName());
    }

    public static void writeLogFile(String fileName, boolean append, String errorMsg) throws IOException
    {
        File file = new File("." + File.separator + fileName + ".log");
        if(!file.exists()){
            file.createNewFile();
        }

        BufferedWriter writer;
        if(append) {
            writer = new BufferedWriter(new FileWriter(file.getName(), true));
        } else {
            writer = new BufferedWriter(new FileWriter(file.getName(), false));
        }

        writer.write(errorMsg);
        writer.close();
    }
}
