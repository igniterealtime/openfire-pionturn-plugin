/*
 * Copyright (C) 2005-2010 Jive Software. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ifsoft.turn.openfire;

import java.io.File;
import java.net.*;
import java.util.concurrent.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.http.HttpBindManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.database.DbConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.PropertyEventDispatcher;
import org.jivesoftware.util.PropertyEventListener;

import org.eclipse.jetty.util.security.*;
import org.eclipse.jetty.security.*;
import org.eclipse.jetty.security.authentication.*;

import java.lang.reflect.*;
import java.util.*;

import org.jitsi.util.OSUtils;
import de.mxro.process.*;
import org.igniterealtime.openfire.plugins.externalservicediscovery.Service;


public class PionTurn implements Plugin, PropertyEventListener, ProcessListener
{
    private static final Logger Log = LoggerFactory.getLogger(PionTurn.class);
    private XProcess pionTurnThread = null;
    private String pionTurnExePath = null;
    private String pionTurnHomePath = null;
    private ExecutorService executor;
	

    public void destroyPlugin()
    {
        PropertyEventDispatcher.removeListener(this);

        try {
            if (executor != null)  executor.shutdown();
            if (pionTurnThread != null) pionTurnThread.destory();
        }
        catch (Exception e) {
            Log.error("PionTurn destroyPlugin", e);
        }
    }

    public void initializePlugin(final PluginManager manager, final File pluginDirectory)
    {
        PropertyEventDispatcher.addListener(this);
        checkNatives(pluginDirectory);

        boolean pionTurnEnabled = JiveGlobals.getBooleanProperty("pionturn.enabled", true);

        if (pionTurnExePath != null && pionTurnEnabled)
        {
            executor = Executors.newCachedThreadPool();

			String ipAddress = JiveGlobals.getProperty("pionturn.ipaddr", getIpAddress(null));
			String hostName = JiveGlobals.getProperty("pionturn.host", ipAddress);			
			
			try {
				ipAddress = InetAddress.getByName(hostName).getHostAddress();
			} catch (Exception e) {	}			

            String ipaddr = " -public-ip " + ipAddress;
            String hostname = " -host-name " + hostName;			
            String port = " -port " + JiveGlobals.getProperty("pionturn.port", getPort());
            String minPort = " -min_port " + JiveGlobals.getProperty("pionturn.min.port", getMinPort());			
            String maxPort = " -max_port " + JiveGlobals.getProperty("pionturn.max.port", getMaxPort());			
            String realm = " -realm " + XMPPServer.getInstance().getServerInfo().getXMPPDomain();
            String username = JiveGlobals.getProperty("pionturn.username", "admin");
            String password = JiveGlobals.getProperty("pionturn.password", "admin");
			String authSecret = JiveGlobals.getProperty("pionturn.secret", "");

			String authentication = null;
			
			if (authSecret == null || "".equals(authSecret)) 
			{
				if ("".equals(username) || "".equals(password)) {
					Log.warn("PionTurn not enabled, secret or (password and username is missing)");
				} else {
					authentication = " -users " + username + "=" + password;					
				}
			} else {
				authentication = " -authSecret " + authSecret;
			}
			
			if (authentication != null) {
				String cmd = pionTurnExePath + hostname + ipaddr + port + minPort + maxPort + realm + authentication;
				pionTurnThread = Spawn.startProcess(cmd, new File(pionTurnHomePath), this);
				
				Log.info("PionTurn enabled " + cmd);				
			}

        } else {
            Log.info("PionTurn disabled");
        }
    }

    public void sendLine(String command)
    {
        if (pionTurnThread != null) pionTurnThread.sendLine(command);
    }

    public String getPort() {
        return "3478";
    }
	
    public String getMinPort() {
        return "50000";
    }
	
    public String getMaxPort() {
        return "55000";
    }

    public String getIpAddress(String hostname)
    {
        if (hostname == null) hostname = XMPPServer.getInstance().getServerInfo().getHostname();
        String ourIpAddress = "127.0.0.1";

        try {
            ourIpAddress = InetAddress.getByName(hostname).getHostAddress();
        } catch (Exception e) {

        }

        return ourIpAddress;
    }

    public void onOutputLine(final String line)
    {
        Log.info("PionTurn onOutputLine " + line);
    }

    public void onProcessQuit(int code)
    {
        Log.info("PionTurn onProcessQuit " + code);
    }

    public void onOutputClosed() {
        Log.error("PionTurn onOutputClosed");
    }

    public void onErrorLine(final String line)
    {
        Log.debug(line);
    }

    public void onError(final Throwable t)
    {
        Log.error("PionTurnThread error", t);
    }

    private void checkNatives(File pluginDirectory)
    {
        try
        {
            String suffix = null;

            if(OSUtils.IS_LINUX64)
            {
                suffix = "linux-64" + File.separator + "turn-server-log";
            }

            else if(OSUtils.IS_WINDOWS64)
            {
                suffix = "win-64" + File.separator + "turn-server-log.exe";
            }

            if (suffix != null)
            {
                pionTurnHomePath = pluginDirectory.getAbsolutePath() + File.separator + "classes";
                pionTurnExePath = pionTurnHomePath + File.separator + suffix;

                File file = new File(pionTurnExePath);
                file.setReadable(true, true);
                file.setWritable(true, true);
                file.setExecutable(true, true);

                Log.info("checkNatives pionTurn executable path " + pionTurnExePath);

            } else {
                Log.error("checkNatives unknown OS " + pluginDirectory.getAbsolutePath());
            }
        }
        catch (Exception e)
        {
            Log.error("checkNatives error", e);
        }
    }

//-------------------------------------------------------
//
//	Property management
//
//-------------------------------------------------------


    public void propertySet(String property, Map params)
    {

    }

    public void propertyDeleted(String property, Map<String, Object> params)
    {

    }

    public void xmlPropertySet(String property, Map<String, Object> params) {

    }

    public void xmlPropertyDeleted(String property, Map<String, Object> params) {

    }
	
//-------------------------------------------------------
//
//	External Services Discovery 
//
//-------------------------------------------------------
	
	public Map<String, Service> getTurnServices() {
		Map<String, Service> services = new HashMap<>();	

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try
        {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement( "SELECT * FROM ofExternalServices " );
            resultSet = pstmt.executeQuery();
            while ( resultSet.next() )
            {
                final long databaseId = resultSet.getLong( "serviceID" );

                String name = resultSet.getString( "name" );
                if ( resultSet.wasNull() || name == null || name.isEmpty() ) name = null;

                String host = resultSet.getString( "host" );
                if ( resultSet.wasNull() || host == null || host.isEmpty() ) host = null;
				
                Integer port = resultSet.getInt( "port" );
                if ( resultSet.wasNull() ) port = null;

                Boolean restricted = resultSet.getBoolean( "restricted" );
                if ( resultSet.wasNull() ) restricted = null;

                String transport = resultSet.getString( "transport" );
                if ( resultSet.wasNull() || transport == null || transport.isEmpty() ) transport = null;

                String type = resultSet.getString( "type" );
                if ( resultSet.wasNull() || type == null || type.isEmpty() ) type = null;

                String username = resultSet.getString( "username" );
                if ( resultSet.wasNull() || username == null || username.isEmpty() )  username = null;
				
                String password = resultSet.getString( "password" );
                if ( resultSet.wasNull() || password == null || password.isEmpty() ) password = null;

                String sharedSecret = resultSet.getString( "sharedSecret" );
                if ( resultSet.wasNull() || sharedSecret == null || sharedSecret.isEmpty() ) sharedSecret = null;

				if (type != null && "turn".equals(type)) {
					final Service service = new Service( databaseId, name, host, port, restricted, transport, type, username, password, sharedSecret );
					services.put(String.valueOf(databaseId), service );                
					Log.debug( "Selected Turn {} service at {} from database.", service.getType(), service.getHost() );
				}
            }
        }
        catch ( Exception e ) {
            Log.error( "Unable to load services from database!", e );
        }
        finally {
            DbConnectionManager.closeConnection( resultSet, pstmt, con );
        }
		
        return services;		
	}

}