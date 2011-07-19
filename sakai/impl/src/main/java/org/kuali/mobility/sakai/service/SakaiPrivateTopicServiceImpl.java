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
 
package org.kuali.mobility.sakai.service;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;
import org.kuali.mobility.configparams.service.ConfigParamService;
import org.kuali.mobility.sakai.entity.Forum;
import org.kuali.mobility.sakai.entity.ForumMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.iu.es.espd.oauth.OAuth2LegService;

@Service
public class SakaiPrivateTopicServiceImpl implements SakaiPrivateTopicService {

	private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SakaiPrivateTopicServiceImpl.class);
	
	@Autowired
	private ConfigParamService configParamService;
	
	@Autowired
	private OAuth2LegService oncourseOAuthService;
	
	public List<Forum> findPrivateTopics(String siteId, String userId) {
		List<Forum> forums = null;
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "forum_topic/private/" + siteId + ".json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(userId, url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");
			
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("forum_topic_collection");

            forums = new ArrayList<Forum>();
            for (int i = 0; i < itemArray.size(); i++) {
                String id = itemArray.getJSONObject(i).getString("forumId");
                String title = itemArray.getJSONObject(i).getString("forumTitle");
                Forum item = new Forum();
                item.setId(id);
                item.setTitle(title);
                item.setIsForumHeader(true);
                forums.add(item);
//	            JSONObject topicsObj = new JSONObject(itemArray.getJSONObject(i).getJSONArray("topics"));
                JSONArray topicsArray = itemArray.getJSONObject(i).getJSONArray("topics");
//	            List<ForumTopics> ftList = new ArrayList<ForumTopics>();
                for (int j = 0; j < topicsArray.size(); j++) {
                	String topicId = topicsArray.getJSONObject(j).getString("topicId");
                    String topicTitle = topicsArray.getJSONObject(j).getString("topicTitle");
                    String topicDescription = topicsArray.getJSONObject(j).
                    	getString("messagesCount") + " messages, " 
                    	+ topicsArray.getJSONObject(j).getString("unreadMessagesCount")
                    	+ " unread";
                    String typeUuid = topicsArray.getJSONObject(j).getString("typeUuid");
                    Forum fTopic = new Forum();
                    fTopic.setId(topicId);
                    fTopic.setTitle(topicTitle);
                    fTopic.setDescription(topicDescription);
                    fTopic.setIsForumHeader(false);
                    fTopic.setTypeUuid(typeUuid);
                    forums.add(fTopic);
//	                ftList.add(fTopic);
                }
//	            item.setTopics(ftList);
//	            item.setDescription(description);
            }
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		
		return forums;
	}
	
	public List<ForumMessage> findPrivateMessages(String json) {
		SakaiPrivateTopicParser parser = new SakaiPrivateTopicParser();
		List<ForumMessage> forums = parser.parsePrivateMessages(json);
		return forums;
	}
	
	public ForumMessage findPrivateMessageDetails(String userId, String siteId, String typeUuid, String messageId) {
		try {
			String url = configParamService.findValueByName("Sakai.Url.Base") + "forum_message/private/" + typeUuid + "/site/" + siteId + ".json";
			ResponseEntity<InputStream> is = oncourseOAuthService.oAuthGetRequest(userId, url, "text/html");
			String json = IOUtils.toString(is.getBody(), "UTF-8");
			
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(json);
            JSONArray itemArray = jsonObj.getJSONArray("forum_message_collection");
            ForumMessage item = new ForumMessage();
            
            for (int i = 0; i < itemArray.size(); i++) {
                String mId = itemArray.getJSONObject(i).getString("messageId");
                if(!messageId.equalsIgnoreCase(mId)) {
                	continue;
                }
                String messageTitle = itemArray.getJSONObject(i).getString("title");
                String messageBody = itemArray.getJSONObject(i).getString("body");
                Boolean isRead = itemArray.getJSONObject(i).getBoolean("read");
                
                String messageAuthor = itemArray.getJSONObject(i).getString("authoredBy");
//                String messageAuthorName = messageAuthor[0] + " " + messageAuthor[1];
//                String messageAuthorRole = messageAuthor[2];
                Date cDate = new Date(Long.parseLong(itemArray.getJSONObject(i).getString("createdOn")));
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String createdDate = df.format(cDate);
                
                item.setId(messageId);
                item.setTitle(messageTitle);
                item.setBody(messageBody);
                item.setCreatedBy(messageAuthor);
                item.setRole(messageAuthor);
                item.setCreatedDate(createdDate);
                item.setMessageHeader(false);
                item.setIsRead(isRead);
            }
            return item;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	public void setConfigParamService(ConfigParamService configParamService) {
		this.configParamService = configParamService;
	}

	public void setOncourseOAuthService(OAuth2LegService oncourseOAuthService) {
		this.oncourseOAuthService = oncourseOAuthService;
	}

}
