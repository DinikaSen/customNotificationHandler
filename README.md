This custom event handler is developed to oveeride the default email template used for password recovery with a service provider specific custom email template. This is useful if you need to use different email templates in the passoword recovery flow based on the application the user initiated password recovery from.

Note :  In this specific sample code, the SP name and the specific email template name are hardcoded. If you have more than one service provider, you may implement a suitable mechanism to handle that. For an example, you can maintain the sp name to template name mappings in a properties file, and fetch the data from there.

This is tested with WSO2 IS 5.10.0.

**Steps to deploy**

1. Change the service provider name and email template name as required.

2. Build the component using `mvn clean install`

2. Copy the `org.wso2.custom.notification.handler.templateoverride-1.0.0.jar` file into `<IS_HOME>/repository/components/dropins`

3. Add following configuration to `<IS_HOME>/repository/conf/deployment.toml` file.
```
[[event_handler]]
name= "emailSend"
subscriptions =[]

[[event_handler]]
name= "custom-notification-handler"
subscriptions =["TRIGGER_NOTIFICATION"]
```

4. Restart WSO2 IS

**Troubleshooting**

Add following line into `log4j2.properties` file and add the logger `org-wso2-custom-notification-handler-templateoverride` at the end of the line starting with `loggers =`.
```
logger.org-wso2-custom-notification-handler-templateoverride.name=org.wso2.custom.notification.handler.templateoverride
logger.org-wso2-custom-notification-handler-templateoverride.level=DEBUG
```

Following log will be written at server startup.
```
DEBUG {org.wso2.custom.notification.handler.templateoverride.internal.TemplateOverrideServiceComponent} - CustomNotificationHandler is activated.
```

Following lines will be written when a user initiates password recovery.
```
DEBUG {org.wso2.custom.notification.handler.templateoverride.CustomNotificationHandler} - Password reset email template is set to myserviceproviderpasswordreset for service provider my_service_provider
```