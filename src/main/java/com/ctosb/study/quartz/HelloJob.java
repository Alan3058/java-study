package com.ctosb.study.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloJob implements Job {

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("helloworld");
    }

    private static String group = "default";

    public static void main(String[] args) {
        QuatzManager quatzManager = new QuatzManager();
        quatzManager.addJob("helloJob", group, "0/10 * * * * ?", HelloJob.class);
        quatzManager.startJobs();
        quatzManager.startJobs();
        quatzManager.shutdownJobs();
        quatzManager.shutdownJobs();
        quatzManager.startJobs();
    }

}
