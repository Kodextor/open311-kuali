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

package org.kuali.mobility.news.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.kuali.mobility.configparams.service.ConfigParamService;
import org.kuali.mobility.news.entity.NewsSource;
import org.kuali.mobility.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * A controller for handling requests for the News tool.
 * 
 * @author Kuali Mobility Team (moblitiy.collab@kuali.org)
 */
@Controller 
@RequestMapping("/news")
public class NewsController {
	public static final Logger LOG = Logger.getLogger( NewsController.class );
    
	private final Boolean ACTIVE = new Boolean( true );
	
	@Autowired
    private NewsService newsService;
    public void setNewsService(NewsService newsService) {
        this.newsService = newsService;
    }
    
    @Autowired
	ConfigParamService configParamService;
	public void setConfigParamService(ConfigParamService configParamService) {
		this.configParamService = configParamService;
	}
    
	/**
	 * The main entry point for the News tool.  Sets an ordered list of active NewsFeed objects to the view.
	 * 
	 * @param uiModel
	 * @param request
	 * @return the path to the home page
	 */
    @RequestMapping(method = RequestMethod.GET)
    public String newsHome(Model uiModel, HttpServletRequest request) {	
    	List<NewsSource> sources = newsService.getNewsSources( Long.valueOf(0), ACTIVE );
    	int sampleSize = 2;
    	try {
    		String configSampleSize = configParamService.findValueByName("News.Sample.Size");
    		sampleSize = Integer.parseInt(configSampleSize);
    	} catch (Exception e) {
    		
    	}
    	if( sources == null || sources.size() == 0 ) {
    		LOG.debug( "======= No NewsFeed found for parent 0. ========" );
    	}
    	for( NewsSource s : sources ) {
    		LOG.debug( "Feed for News Source: "+s.getId() );
    	}
    	
    	uiModel.addAttribute("newsSources", sources);
    	uiModel.addAttribute("sampleSize", sampleSize);
    	return "news/newsHome";
    }
    
    /**
     * Handles requests for feeds and articles.
     * 
     * @param request
     * @param sourceId the id of the NewsSource for the feed/article to retrieve
     * @param articleId (optional) the id of an article to retrieve. If this is not present, the whole feed is returned.
     * @param uiModel
     * @return the path to the feed page or the article page, depending on the presence of articleId
     */
    @RequestMapping(value = "/{sourceId}", method = RequestMethod.GET)
    public String getNewsArticle(HttpServletRequest request, @PathVariable("sourceId") long sourceId, @RequestParam(value = "articleId", required = false) String articleId, Model uiModel) {
    	String destination = null;
    	LOG.debug( "getNewsArticle() : sourceId = "+sourceId+" articleId = "+articleId );
    	if (articleId != null && articleId != "") {
    		uiModel.addAttribute("article", newsService.getNewsArticle(articleId, sourceId));
    		uiModel.addAttribute("feedTitle", newsService.getNewsSourceById(sourceId).getTitle());
    		LOG.debug( "Forwarding to news/newsArticle" );
        	destination = "news/newsArticle";
    	} else {
    		List<NewsSource> feeds = newsService.getNewsSources( Long.valueOf( sourceId ), ACTIVE );
    		if( feeds == null || feeds.isEmpty() ) {
	    		uiModel.addAttribute("feed", newsService.getNewsSourceById(sourceId));
	    		LOG.debug( "Forwarding to news/newsStream" );
	        	destination = "news/newsStream";
    		} else {
    	    	uiModel.addAttribute("newsSources", feeds);
    	    	int sampleSize = 2;
    	    	try {
    	    		String configSampleSize = configParamService.findValueByName("News.Sample.Size");
    	    		sampleSize = Integer.parseInt(configSampleSize);
    	    	} catch (Exception e) {}
    	    	uiModel.addAttribute("sampleSize", sampleSize);
	        	destination = "news/newsHome";
    		}
    	}
    	return destination;
    }
}
