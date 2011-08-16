/**
 * Copyright 2011 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.kuali.mobility.admin.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.kuali.mobility.admin.service.AdminService;
import org.springframework.context.ApplicationContext;

public class AdminListener implements ServletContextListener {
	
	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(AdminListener.class);

	private AdminService adminService;

	public void contextInitialized(final ServletContextEvent event) {
		ApplicationContext ctx = org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		adminService = (AdminService) ctx.getBean("AdminService");
		
		LOG.info("Starting the Admin cache thread");
		adminService.startCache();
		LOG.info("Admin cache thread started");
	}

	public void contextDestroyed(final ServletContextEvent event) {
		if (adminService != null) {
			LOG.info("Stopping the Admin cache thread");
			adminService.stopCache();
			LOG.info("Admin cache thread should be completely dead");
		}
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}
	
}
