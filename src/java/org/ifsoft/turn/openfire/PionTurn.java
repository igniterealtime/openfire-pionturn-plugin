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

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.http.HttpBindManager;
import org.jivesoftware.openfire.XMPPServer;

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

            String ipaddr = " -public-ip " + JiveGlobals.getProperty("pionturn.ipaddr", getIpAddress());
            String port = " -port " + JiveGlobals.getProperty("pionturn.port", getPort());
            String realm = " -realm " + XMPPServer.getInstance().getServerInfo().getXMPPDomain();
            String username = JiveGlobals.getProperty("pionturn.username", "admin");
            String password = JiveGlobals.getProperty("pionturn.password", "admin");

            if (!"".equals(username) && !"".equals(password))
            {
                String authentication = " -users " + username + "=" + password;
                String cmd = pionTurnExePath + ipaddr + port + realm + authentication;
                pionTurnThread = Spawn.startProcess(cmd, new File(pionTurnHomePath), this);

                Log.info("PionTurn enabled " + cmd);
            }
            else {
                Log.warn("PionTurn not enabled, password or username is missing");
            }

        } else {
            Log.info("PionTurn disabled");
        }
    }

    public void sendLine(String command)
    {
        if (pionTurnThread != null) pionTurnThread.sendLine(command);
    }

    public String getPort()
    {
        return "3478";
    }

    public String getIpAddress()
    {
        String ourHostname = XMPPServer.getInstance().getServerInfo().getHostname();
        String ourIpAddress = "127.0.0.1";

        try {
            ourIpAddress = InetAddress.getByName(ourHostname).getHostAddress();
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
//
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

}