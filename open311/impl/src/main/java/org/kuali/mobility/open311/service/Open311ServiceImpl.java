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

package org.kuali.mobility.open311.service;

import java.util.List;

import org.kuali.mobility.open311.dao.Open311Dao;
import org.kuali.mobility.open311.dao.Open311DaoImpl;
import org.kuali.mobility.open311.entity.Submission;
import org.kuali.mobility.open311.service.Open311Service;
//import org.kuali.mobility.open311.entity.File;
import org.kuali.mobility.file.entity.File;
import org.kuali.mobility.open311.entity.ServiceEntity;
import org.kuali.mobility.open311.entity.Attributes;
import org.kuali.mobility.open311.entity.AttributesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

@Service
public class Open311ServiceImpl implements Open311Service {

	public Open311ServiceImpl()
	{}
	
    @Autowired
    private Open311Dao dao;
	
	public Open311Dao getDao() {
		return dao;
	}
	
	public void setDao(Open311Dao dao) {
		this.dao = dao;
	}
	
	private static ApplicationContext applicationContext;
	
    
    public static void createApplicationContext() {
    	Open311ServiceImpl.setApplicationContext(new FileSystemXmlApplicationContext(getConfigLocations()));
    }

    private static String[] getConfigLocations() {
        return new String[] { "classpath:/Open311SpringBeans.xml" };
    }
    
	public static ApplicationContext getApplicationContext() {
		Open311ServiceImpl.createApplicationContext();
		return applicationContext;
	}

	public static void setApplicationContext(ApplicationContext applicationContext) {
		Open311ServiceImpl.applicationContext = applicationContext;
	}
	
	
	@Override
	public List<ServiceEntity> getService() {
		
		dao = (Open311DaoImpl) getApplicationContext().getBean("open311Dao");

		return dao.getServiceList();
	}

	@Override
	public Attributes getServiceAttributes(final String serviceCode) {
		
		dao = (Open311DaoImpl) getApplicationContext().getBean("open311Dao");

		return dao.getServiceAttributes(serviceCode);
	}
	
	@Override
	public String getServiceJson( final String serviceCode) {
		
		return dao.getServiceJson(serviceCode);
	}
	
	
	@Override
	@Transactional
    public Submission findSubmissionById(Long id) {
		return dao.findSubmissionById(id);    	
    }
    
	@Override
	@Transactional
    public List<Submission> findAllSubmissions() {
		return dao.findAllSubmissions();
    }
    
	@Override
	@Transactional
	public Long saveSubmission(Submission submission) {		
		return dao.saveSubmission(submission);
	} 
    
	@Override
	@Transactional
    public Long saveAttachment(File file) {
		return dao.saveAttachment(file);
    }
    
	@Override
	@Transactional
    public List<Submission> findAllSubmissionsByParentId(Long id) {
		return dao.findAllSubmissionsByParentId(id);
	}

}
