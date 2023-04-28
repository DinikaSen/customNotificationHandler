/*
 * Copyright (c) 2023, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.custom.notification.handler.templateoverride;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.event.IdentityEventException;
import org.wso2.carbon.identity.event.event.Event;
import org.wso2.carbon.identity.event.handler.notification.NotificationConstants;
import org.wso2.carbon.identity.event.handler.notification.NotificationHandler;

import java.util.Map;

public class CustomNotificationHandler extends NotificationHandler {

    private static final Log log = LogFactory.getLog(CustomNotificationHandler.class);

    @Override
    public String getName() {
        return "custom-notification-handler";
    }

    /**
     * This overrides the notification handler behavior and replaces the default email template of a specific scenario
     * with a custom template specified for a service provider. In order to do so, the service provider name should be
     * passed to the event as a property.
     *
     * NOTE : In this specific sample code, the SP name and the specific email template name are hardcoded. If you have
     * more than one service provider, you may implement a suitable mechanism to handle that. For an example, you can
     * maintain the sp name to template name mappings in a properties file, and fetch the data from there.
     *
     * @param event
     * @throws IdentityEventException
     */
    @Override
    public void handleEvent(Event event) throws IdentityEventException {

        String serviceProvider = (String) event.getEventProperties().get("sp");
        // Only proceed to override the email template if service provider is available as a property,
        // and it matches the required service provider name(s).
        if (StringUtils.equals(serviceProvider,"my_service_provider")) {
            Map<String, Object> eventProperties = event.getEventProperties();
            // Verify if the recovery scenario is correct and notification type is email.
            String recoveryScenario = (String) eventProperties.get("RECOVERY_SCENARIO");
            String recoveryChannel = (String) eventProperties.get("notification-channel");
            if (StringUtils.isNotEmpty(serviceProvider) && recoveryScenario.equals("NOTIFICATION_BASED_PW_RECOVERY")
                    && recoveryChannel.equals("EMAIL") ) {
                String newEmailTemplate = "myserviceproviderpasswordreset";
                if (log.isDebugEnabled()) {
                    log.debug("Password reset email template is set to " + newEmailTemplate +
                            " for service provider " + serviceProvider);
                }
                // Set the value of the 'TEMPLATE_TYPE' property to the new email template name.
                event.getEventProperties().put(NotificationConstants.EmailNotification.EMAIL_TEMPLATE_TYPE, newEmailTemplate);
            }
        }

        // Pass to the super method to continue.
        super.handleEvent(event);
    }
}
