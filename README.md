# Accurate**Readings**
#### Ever wondered what your actual resource usage is on your server? **This plugin is for you!**

## What's this plugin?
AccurateReadings aims to provide accurate readings on resource usage by communicating with a Pterodactyl panel using the
[Pterodactyl4J](https://github.com/stanjg/Ptero4J) wrapper made by mattmalec, and of course using the Pterodactyl panel
API. Furthermore, if PlaceholderAPI is present, the plugin will also register a few placeholders that you can use to
display your resource usage statistics on any other plugin that supports PAPI placeholders: want to display RAM usage in
your server's playerlist/tab using TitleManager? No problem!    

## Requirements
- **A Pterodactyl panel.** If you're using a shared hosting plan and the panel is powered by Pterodactyl, then you are
able to use this plugin. If you're self-hosting your server (home server, your PC, a VPS or dedicated server...), make
sure you use Pterodactyl, not only is it really easy to install, but it is an excellent panel software. Please read the
wiki for instructions on how to check if the panel you're using is powered by Pterodactyl.
- **A Spigot server.** This plugin has been coded against Spigot 1.12.2, but it has been tested on Pufferfish 1.17.1 and 
Purpur 1.18.1. It should work on any server running Spigot, or one of its forks: Paper, Purpur...
While it is only expected to work on 1.12.2 through 1.18.2, this plugin has been coded with simplicity in mind, so it
should work on 1.8.8 and newer versions of the game.

## How to install
1. Go to your Pterodactyl panel and, on the left sidebar, click on 'Account API'. If you're using a Sparked Host server,
[click here](https://control.sparkedhost.us/account/api).
2. In the top right corner, click on Create New. Add a description (e.g. "AccurateReadings") and leave the 'Allowed
Connection IPs' empty unless you're **100% sure** what IP will your server be using to connect to the API (this is hard
to determine on shared hosting environments as it may use the node's main IP address to communicate, which is not always
obvious).
3. Copy the API key for later. You won't be able to see it again, so make sure you save it!
4. Upload the plugin JAR (you can grab it from the [releases page](https://github.com/SparkedHost/AccurateReadings/releases/))
to the plugins directory on your server.
5. Restart your server in order to generate the config file. The plugin will then disable itself, please stop the server
after it has fully loaded and navigate to `plugins/AccurateReadings`, then open the config.yml file.
6. Then fill in the rest of the options: `panel-url` is the URL of your Pterodactyl panel; `server-id` is your server ID
and you can find it on the main page of your panel, at the very left of the server list; `api-key` is the API key we
copied earlier. Make sure you don't remove the quotation marks from any of the options.
7. Save the changes and start the server.

## How to use
**The main command is `/perf`**, although there are aliases: `/performance` and `/lag`. This may override Essentials
`/lag` command, but you can fix this in the `commands.yml` file on your server, or you can just use `/elag`.

This plugin also registers a few PlaceholderAPI placeholders, you can find the complete list in the wiki.

## Known issues
None at this moment :)

## Credits
- [OkHttp](https://github.com/square/okhttp) by Square
- [Okio](https://github.com/square/okio) by Square
- [Pterodactyl4J](https://github.com/mattmalec/Pterodactyl4J) by mattmalec
- Thanks to the Sparked Host LLC community for providing feedback, suggestions and bug reports during initial
development.

## Version history
- **1.0.0-dev01**. First version of the plugin that actually worked. It made the main thread hang while the plugin
- communicated with the panel.
- **1.0.0-dev02**. Fixed the main thread freezing issue. Added new options to the config.yml
- **1.0.0-pre1**. First public version of the plugin
- **1.1.0**. Removed Sparked Host lock, added proper configuration checks and cooldown, and fixed a few bugs.
- **1.2.0**. Tons of code quality improvements, restructured the config file, added PlaceholderAPI support, switched
to a better API wrapper.

## Support
As this project has been officially released by Sparked Host, our support team may provide assistance with this plugin.
For any other inquiries, you may reach out to Raúl via email: raul.m@sparkedhost.com
