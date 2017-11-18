package com.ctosb.study.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuatzManager {

    private SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    public void addJob(String name, String group, String time, Class<? extends Job> clazz) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(name, group).build();
            CronTrigger trigger = (CronTrigger) TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(CronScheduleBuilder.cronSchedule(time)).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void modifyCron(String name, String group, String time) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(CronScheduleBuilder.cronSchedule(time)).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void removeJob(String name, String group) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = JobKey.jobKey(name, group);
            TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void pauseJob(String name, String group) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = JobKey.jobKey(name, group);
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void resumeJob(String name, String group) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = JobKey.jobKey(name, group);
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void startJob(String name, String group) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = JobKey.jobKey(name, group);
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


    public void startJobs() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
//			if(scheduler.isShutdown()){
            scheduler.start();
            System.out.println("started");
//			}
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }

    public void shutdownJobs() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
                System.out.println("shutdown");
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
