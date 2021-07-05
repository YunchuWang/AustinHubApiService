package com.austinhub.apiservice.cronjobs;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
public class QuartzConfig {

    // expiration email reminder job variables
    public static final String JOB_EXPIRATION_EMAIL_REMINDER = "expirationEmailReminderJobDetail";
    public static final String JOB_GROUP_EXPIRATION_EMAIL = "expirationEmailJobGroup";
    public static final String TRIGGER_EXPIRATION_EMAIL_REMINDER = "expirationEmailReminderTrigger";

    // transaction status scanner job variables
    public static final String JOB_TRAN_STATUS_SCANNER = "transactionStatusScannerJobDetail";
    public static final String JOB_GROUP_TRAN_STATUS_SCANNER = "transactionStatusScannerJobGroup";
    public static final String TRIGGER_TRAN_STATUS_SCANNER = "transactionStatusScannerTrigger";

    public static final String FILE_NAME_QUARTZ_PROPERTIES = "quartz.properties";

    private ApplicationContext applicationContext;

    public QuartzConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public JobDetail expirationEmailReminderJobDetail() {
        return JobBuilder.newJob(ExpirationEmailReminderJob.class)
                         .withIdentity(JOB_EXPIRATION_EMAIL_REMINDER, JOB_GROUP_EXPIRATION_EMAIL)
                         .storeDurably()
                         .build();
    }

    @Bean
    public JobDetail transactionStatusScannerJobDetail() {
        return JobBuilder.newJob(TransactionStatusScannerJob.class)
                         .withIdentity(JOB_TRAN_STATUS_SCANNER, JOB_GROUP_TRAN_STATUS_SCANNER)
                         .storeDurably()
                         .build();
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public Trigger expirationEmailReminderTrigger(JobDetail expirationEmailReminderJobDetail) {
        return TriggerBuilder.newTrigger().forJob(expirationEmailReminderJobDetail)
                             .withIdentity(TRIGGER_EXPIRATION_EMAIL_REMINDER,
                                           JOB_GROUP_EXPIRATION_EMAIL)
                             .withSchedule(dailyAtHourAndMinute(23, 59))
                             .build();
    }

    @Bean
    public Trigger transactionStatusScannerTrigger(JobDetail transactionStatusScannerJobDetail) {
        return TriggerBuilder.newTrigger().forJob(transactionStatusScannerJobDetail)
                             .withIdentity(TRIGGER_TRAN_STATUS_SCANNER,
                                           JOB_GROUP_TRAN_STATUS_SCANNER)
//                             .withSchedule(dailyAtHourAndMinute(22, 59))
                             .startNow()
                             .build();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactory(Trigger expirationEmailReminderTrigger,
            Trigger transactionStatusScannerTrigger,
            JobDetail expirationEmailReminderJobDetail,
            JobDetail transactionStatusScannerJobDetail) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setConfigLocation(new ClassPathResource(FILE_NAME_QUARTZ_PROPERTIES));

        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory
                .setJobDetails(expirationEmailReminderJobDetail, transactionStatusScannerJobDetail);
        schedulerFactory
                .setTriggers(expirationEmailReminderTrigger, transactionStatusScannerTrigger);

        return schedulerFactory;
    }
}