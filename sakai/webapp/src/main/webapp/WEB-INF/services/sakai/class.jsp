<%--
  Copyright 2011 The Kuali Foundation Licensed under the Educational Community
  License, Version 2.0 (the "License"); you may not use this file except in
  compliance with the License. You may obtain a copy of the License at
  http://www.osedu.org/licenses/ECL-2.0 Unless required by applicable law or
  agreed to in writing, software distributed under the License is distributed
  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
  express or implied. See the License for the specific language governing
  permissions and limitations under the License.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="kme" uri="http://kuali.org/mobility" %>
	
<kme:page title="${course.courseTitle}" id="class_details">
	<kme:content>
		<h3>${course.courseDesc}</h3>
		<div>
		  	<img src="http://localhost:9090/direct/profile/${course.instructorId}/image/thumb?sakai.session=${sessionId}" width="70" height="70" alt="pic">
		</div>
		<div>
			<h3>Instructor</h3>
			<a href="${pageContext.request.contextPath}/sakaiparticipantdetails?siteId=${course.courseId}&siteTitle=${course.courseTitle}&displayId=${course.instructorId}">
				<p>${course.instructorName}</p>
			</a>
		</div>
		<ul data-role="listview" data-theme="c" data-dividertheme="b" data-inset="false">
			<li>
				<a href="${pageContext.request.contextPath}/myclasses/${course.courseId}/announcements?siteTitle=${course.courseTitle}">
					<h3>Announcements</h3>
				</a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/sakaiassignments?siteId=${course.courseId}&siteTitle=${course.courseTitle}&userId=${userId}">
					<h3>Assignments</h3>
				</a>
			</li>
			<li>					
				<a href="${pageContext.request.contextPath}/sakaigradebook?siteId=${course.courseId}&siteTitle=${course.courseTitle}">
					<h3>Gradebook</h3>
				</a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/roster?siteId=${course.courseId}&siteTitle=${course.courseTitle}">
					<h3>Roster</h3>
				</a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/forums?siteId=${course.courseId}&siteTitle=${course.courseTitle}">
					<h3>Forums</h3>
				</a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/resources?siteId=${course.courseId}&siteTitle=${course.courseTitle}">
					<h3>Resources</h3>
				</a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/sakaiprivatetopics?siteId=${course.courseId}&siteTitle=${course.courseTitle}">
					<h3>Messages</h3>
				</a>
			</li>
		</ul>
	</kme:content>
</kme:page>

</body>
</html>