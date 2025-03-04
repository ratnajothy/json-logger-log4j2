# json-logger-log4j2

1. Place the JAR in the '**dropins**' directory 
2. Create the **logrhythm** folder for wso2carbon.log
3. Apply **log4j2.properties** configuration below
```
appenders = ..., LOGRHYTHM_LOGFILE

appender.LOGRHYTHM_LOGFILE.type = RollingFile
appender.LOGRHYTHM_LOGFILE.name = LOGRHYTHM_LOGFILE
appender.LOGRHYTHM_LOGFILE.fileName = ${sys:carbon.home}/repository/logs/logrhythm/wso2carbon.log
appender.LOGRHYTHM_LOGFILE.filePattern = ${sys:carbon.home}/repository/logs/logrhythm/wso2carbon-%d{yyyy-MM-dd}.log
appender.LOGRHYTHM_LOGFILE.append = true

appender.LOGRHYTHM_LOGFILE.layout.type = LogRhythmLayout
appender.LOGRHYTHM_LOGFILE.layout.MachineName = 10.83.80.32

appender.LOGRHYTHM_LOGFILE.policies.type = Policies
appender.LOGRHYTHM_LOGFILE.policies.time.type = TimeBasedTriggeringPolicy
appender.LOGRHYTHM_LOGFILE.policies.time.interval = 1
appender.LOGRHYTHM_LOGFILE.policies.time.modulate = true

appender.LOGRHYTHM_LOGFILE.policies.size.type = SizeBasedTriggeringPolicy
appender.LOGRHYTHM_LOGFILE.policies.size.size = 50MB

appender.LOGRHYTHM_LOGFILE.strategy.type = DefaultRolloverStrategy
appender.LOGRHYTHM_LOGFILE.strategy.max = 50

appender.LOGRHYTHM_LOGFILE.threshold.type = ThresholdFilter
appender.LOGRHYTHM_LOGFILE.threshold.level = INFO

rootLogger.appenderRef.LOGRHYTHM_LOGFILE.ref = LOGRHYTHM_LOGFILE
```
