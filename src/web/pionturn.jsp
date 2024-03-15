<%@ page import="java.util.*" %>
<%@ page import="org.ifsoft.turn.openfire.*" %>
<%@ page import="org.igniterealtime.openfire.plugins.externalservicediscovery.Service" %>
<%@ page import="org.jivesoftware.openfire.*" %>
<%@ page import="org.jivesoftware.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%

    boolean update = request.getParameter("update") != null;
    String errorMessage = null;

    // Get handle on the plugin
    PionTurn plugin = (PionTurn) XMPPServer.getInstance().getPluginManager().getPlugin("pionturn");
	boolean isServiceDiscoveryAvailable = XMPPServer.getInstance().getPluginManager().getPlugin("externalservicediscovery") != null;

    if (update) {   
        String service = request.getParameter("service");      

		if (service != null) {		
			JiveGlobals.setProperty("pionturn.service", service); 
			Map<String, Service> services = plugin.getTurnServices();   

			if (services != null && services.containsKey(service)) {
				Service turn = services.get(service);							
				
				JiveGlobals.setProperty("pionturn.secret", turn.getSharedSecret()); 
				JiveGlobals.setProperty("pionturn.username", turn.getRawUsername()); 		    
				JiveGlobals.setProperty("pionturn.password", turn.getRawPassword());
				JiveGlobals.setProperty("pionturn.host", turn.getHost());  				
				JiveGlobals.setProperty("pionturn.ipaddr", plugin.getIpAddress(turn.getHost()));  		    
				JiveGlobals.setProperty("pionturn.port", String.valueOf(turn.getPort()));
			}
			
		} else {    
			String username = request.getParameter("username");     
			JiveGlobals.setProperty("pionturn.username", username); 		

			String password = request.getParameter("password");     
			JiveGlobals.setProperty("pionturn.password", password); 

			String ipaddr = request.getParameter("ipaddr");     
			JiveGlobals.setProperty("pionturn.ipaddr", ipaddr);  		
			
			String port = request.getParameter("port");     
			JiveGlobals.setProperty("pionturn.port", port);
		}			
        
        String min_port = request.getParameter("min_port");     
        JiveGlobals.setProperty("pionturn.min.port", min_port); 
        
        String max_port = request.getParameter("max_port");     
        JiveGlobals.setProperty("pionturn.max.port", max_port);  
        
        String enabled = request.getParameter("enabled");
        JiveGlobals.setProperty("pionturn.enabled", (enabled != null && enabled.equals("on")) ? "true": "false");        
    }

%>
<html>
<head>
   <title><fmt:message key="plugin.title.description" /></title>
   <meta name="pageID" content="pionturn-settings"/>
</head>
<body>
<% if (errorMessage != null) { %>
<div class="error">
    <%= errorMessage%>
</div>
<br/>
<% } else if (update) {%>
<div class="success">
	<fmt:message key="config.page.settings.success"/>
</div>
<% }%>

<div class="jive-table">
<form action="pionturn.jsp" method="post">
    <p>
        <table class="jive-table" cellpadding="0" cellspacing="0" border="0" width="100%">
            <thead> 
            <tr>
                <th colspan="2"><fmt:message key="config.page.settings.description"/></th>
            </tr>
            </thead>
            <tbody>  
            <tr>
                <td nowrap  colspan="2">
                    <input type="checkbox" name="enabled"<%= (JiveGlobals.getProperty("pionturn.enabled", "true").equals("true")) ? " checked" : "" %>>
                    <fmt:message key="config.page.configuration.enabled" />       
                </td>  
            </tr>
<% 
			if (isServiceDiscoveryAvailable) {
				Map<String, Service> services = plugin.getTurnServices();
%>	
            <tr>
                <td width="200"><fmt:message key="config.page.configuration.service" /></td>
                <td>
                    <select id="service" name="service" required>
<%
					for (Service service : services.values()) {
%>					
                         <option value="<%= service.getDatabaseId() %>" id="<%= service.getDatabaseId()%>" <%= (JiveGlobals.getIntProperty("pionturn.service", 0) == service.getDatabaseId() ? "selected" : "")%> ><%= service.getName()%></option>
<%
					}
%>						
                </select>
                </td>
            </tr> 
<% 
			} else {
%>		
            <tr>
                <td align="left" width="150">
                    <fmt:message key="config.page.configuration.secret"/>
                </td>
                <td><input type="password" size="50" maxlength="100" name="secret" value="<%= JiveGlobals.getProperty("pionturn.secret", "") %>">
                </td>
            </tr> 			
            <tr>
                <td align="left" width="150">
                    <fmt:message key="config.page.configuration.username"/>
                </td>
                <td><input type="text" size="50" maxlength="100" name="username" value="<%= JiveGlobals.getProperty("pionturn.username", "admin") %>">
                </td>
            </tr>   
            <tr>
                <td align="left" width="150">
                    <fmt:message key="config.page.configuration.password"/>
                </td>
                <td><input type="password" size="50" maxlength="100" name="password" value="<%= JiveGlobals.getProperty("pionturn.password", "admin") %>">
                </td>
            </tr>              
            <tr>
                <td align="left" width="150">
                    <fmt:message key="config.page.configuration.ipaddr"/>
                </td>
                <td><input type="text" size="50" maxlength="100" name="ipaddr" required
                       value="<%= JiveGlobals.getProperty("pionturn.ipaddr", plugin.getIpAddress(null)) %>">
                </td>                               
            </tr>   
            <tr>
                <td align="left" width="150">
                    <fmt:message key="config.page.configuration.port"/>
                </td>
                <td><input type="text" size="50" maxlength="100" name="port" required
                       value="<%= JiveGlobals.getProperty("pionturn.port", plugin.getPort()) %>">
                </td>                               
            </tr> 	
<% 
			}
%>				
            <tr>
                <td align="left" width="150">
                    <fmt:message key="config.page.configuration.min.port"/>
                </td>
                <td><input type="text" size="50" maxlength="100" name="min_port" required
                       value="<%= JiveGlobals.getProperty("pionturn.min.port", plugin.getMinPort()) %>">
                </td>                               
            </tr> 			
            <tr>
                <td align="left" width="150">
                    <fmt:message key="config.page.configuration.max.port"/>
                </td>
                <td><input type="text" size="50" maxlength="100" name="max_port" required
                       value="<%= JiveGlobals.getProperty("pionturn.max.port", plugin.getMaxPort()) %>">
                </td>                               
            </tr>             
            </tbody>
        </table>
    </p>
   <p>
        <table class="jive-table" cellpadding="0" cellspacing="0" border="0" width="100%">
            <thead> 
            <tr>
                <th colspan="2"><fmt:message key="config.page.configuration.save.title"/></th>
            </tr>
            </thead>
            <tbody>         
            <tr>
                <th colspan="2"><input type="submit" name="update" value="<fmt:message key="config.page.configuration.submit" />">&nbsp;<fmt:message key="config.page.configuration.restart.warning"/></th>
            </tr>       
            </tbody>            
        </table> 
    </p>
</form>
</div>
</body>
</html>
