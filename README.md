# Openfire Pion TURN Plugin

This plugin provides a TURN/STUN Server for Openfire.

## CI Build Status

[![Build Status](https://github.com/igniterealtime/openfire-pionturn-plugin/workflows/Java%20CI/badge.svg)](https://github.com/igniterealtime/openfire-pionturn-plugin/actions)

## Overview

This plugin uses the Pion Turn Project (https://github.com/pion/turn) to create a TURN/STUN server for Openfire. This plugin is used to help users who are on uncooperative networks connect their calls or video calls.
For this plugin to work, you will also need to install the plugin "External Service Discovery" in order to expose this service to the clients.

## Known Issues
This version has embedded binaries for only Linux 64 and Windows 64.
<p>

</p>
<h2>Installation</h2>

<ol>
    <li>Copy the pionturn.jar file to the OPENFIRE_HOME/plugins directory.</li>
    <li>Configure the admin properties page.</li>
</ol>

<h2>Configuration of the External Service Discovery</h2>
<img src="" />
 Head to 
'Server>Media Services>External Service Discovery' and set the fields:
<li>Host*: With your server's external IP address if you have a static IP (highly recommended) or with your domain name (if you are running with dynamicIP)</li>
<li>Port: Here choose a port number (you will have to allow this port in your firewall as UDP only and also port forward in case you are behind a NAT).</li> 
<li>Description: In this one you can write anything you want, but it is recommended to write something (it will help Pionturn identify the configuration, otherwise you will see it as "null").</li>
<li>Transport: "UDP"</li>
<li>Type*: "TURN"</li>
<li>Credentials: Here you have 2 options, but it is highly recommended that you go with "Shared Secret (for generating ephemeral passwords)" and then set a secret. But be aware some characters might cause some issues. So it's better to stick with alphanumericals.</li>
After that, just click on the button "Add Service".
<h2>Configuration of the Pion turn Plugin</h2>
<img src="" />
Head to 'Server>Media Services>Pion Turn/Stun' and set the fields:
<li>Check the box for "Enabled"</li>
<li>Service: Here you select the field which matches with your External Service Discovery Description(Pionturn will then grab all the info you did set there).</li>
<li> UDP Port Range Min: this will be your lower port on the UDP range used by the TURN server. You will have to allow it on the firewall and also port forward (the whole range)in case you are behind NAT.</li>
<li> UDP Port Range Max:this will be your higher port on the UDP range used by the TURN server. You will have to allow it on the firewall and also port forward (the whole range)in case you are behind NAT.</li>
After that just click "Save" and don't forget to go to 'Plugins' and restart the Pionturn plugin.


## Reporting Issues

Issues may be reported to the [forums](https://discourse.igniterealtime.org) or via this repo's [Github Issues](https://github.com/igniterealtime/openfire-pionturn-plugin).
