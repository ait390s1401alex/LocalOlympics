<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="localolympics.db.Activity" %>
<%@ page import="localolympics.db.Participant" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<!--  
   Copyright 2014 - 
   Licensed under the Academic Free License version 3.0
   http://opensource.org/licenses/AFL-3.0

   Authors: Alex Verkhovtsev
   
   Version 0.1 - Spring 2014
-->



<html>

  <head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main1.css" />
    
    <title>Local Olympics - Home</title>
    
    <script src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>
    
    <script>

	  
	    function initializeMap(address){   
	    	var geocoder = new google.maps.Geocoder();
	    	var latlng = new google.maps.LatLng(-34.397, 150.644);
	        var map_options = {
	                center: latlng,
	                zoom: 11,
	                mapTypeId: google.maps.MapTypeId.ROADMAP
	              }
	        var map = new google.maps.Map(document.getElementById("map-canvas"), map_options);

	        geocoder.geocode( { 'address': address}, function(results, status) {
		          if (status == google.maps.GeocoderStatus.OK) {
		            map.setCenter(results[0].geometry.location);
		            var marker = new google.maps.Marker({
		            	map: map,
		                position: results[0].geometry.location
		            });
		          } else {
		        	  map.setCenter(new google.maps.LatLng(38.849468, -77.306355));
		          }
		        });


	      }

	    window.onload = function () {
	        
	    } 
    
    </script>
  </head>
	
	  <body>
	  <div class="background">
	  
	  	
	  
	   
  	


  
  <%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
      	pageContext.setAttribute("user", user);
	%>
		<div>
		<div class="top" style="float:left"><a href="/index.jsp">INDEX</a></div><div class="top"></div>
		<div class="top" style="float:right"><a href="profile.jsp">${fn:escapeXml(user.nickname)}</a> <a href="/logout">SIGN OUT</a></div>
		</div>
	<%
	    } else {
	%>
		<c:redirect url="/index.jsp" />
	<%
	    }
    %>
    
    <%
    
       
    Entity participant = Participant.getParticipantWithLoginID(user.getNickname());
    
    if(participant == null){
    	
    	%>
    	
    	<jsp:forward page="editProfile.jsp" />
    	
    	<%
    	
    	
    }else{
    	
    	
    	%>
    	<br />
    	<br />
    	<div class="main">
		<h2>Welcome <%=Participant.getFirstName(participant) %>.</h2>
			
			
			<br/>
			
			<%
			List<Entity> allActivity = Activity.getFirstActivity(100);
			if (allActivity.isEmpty()) {
			%>
				<h1>No Activities available at this time</h1>
			<%
			}else{	
			%>
			
			
			
			
			<table>
				<tr>
					<td><h3>Click on an activity to join in the fun!</h3></td>
					<td></td>
				</tr>
				<tr>
					<table class="activitylist">
						<tr>
							<th>Activity</th><th>Type</th><th>Location</th>
						</tr>
						<%
						for (Entity activity : allActivity) {
							String activityName = Activity.getName(activity);
							String activityID = Activity.getStringID(activity);
							String activityType = Activity.getType(activity);
							String activityLocation = Activity.getLocation(activity);
						%>
						
						<tr>
							<td><a href="activity.jsp?activityID=<%=activityID%>"><%=activityName%></a></td>
							<td><%=activityType%></td>
							<td><%=activityLocation%></td>
						</tr>
						
						<%
    	
    	
						}
						%>
					</table>
				</tr>
			</table>
			</div>
			
			
			

			<div id="map-canvas" class="map-canvas"></div>

	<%
    
    
    }
    }
    
    
	%>
  </div>

  </body>
 
</html>