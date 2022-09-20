# Accurate**Readings**
#### **WARNING:** This version of the plugin will **NOT** work with Pterodactyl 1.x and newer versions of Spigot.
A new version (v1.2.0) is being worked on in a separate branch, it is not production-ready yet but it is, to our knowledge, fully functional so far. Please take a look at the ``v1.2.0`` branch if you'd like to compile the code yourself, or check out the following pull request to stay updated: [#1 AccurateReadings v1.2.0](https://github.com/SparkedHost/AccurateReadings/pull/1)

**Alternatively, you may download the latest stable v1.2.0 build by clicking [here](https://builds.bettd.me/java/bukkit/accuratereadings/AccurateReadings-1.2.0-pre1.jar).** This build has been in use in a live environment for over 4 months without major issues. If you have an old config file, **it will not be compatible** and you should remake it. Before v1.2.0 is released, we will attempt to create a system that automatically converts your old configuration to the new format.

[There's also a CI server with freshly-baked builds](https://ci.bettd.me/job/SparkedHost/job/AccurateReadings/), but at the time of writing (September 20th, 2022) they currently contain unfinished features which may break the plugin's functionality entirely during runtime.

Here's a few of the new version's highlights:

- Code quality has been greatly improved
- Switched to a better & newer API wrapper for Pterodactyl
- Added PlaceholderAPI support: this plugin now registers a number of placeholders that may be used with any other plugin that supports the use of PAPI placeholders.
- Multi-version support: even though this is considered a "legacy" plugin and was compiled against Java 8, it should still work with anything from Spigot 1.8.8 all the way to the latest Purpur build for **1.19.2**.

**ANYTHING BELOW THIS LINE IS PART OF v1.1.0's README!** [You can find an up-to-date README here](https://github.com/SparkedHost/AccurateReadings/tree/v1.2.0#readme).

---

## What's this plugin?
AccurateReadings aims to provide accurate readings on resource usage by communicating with a Pterodactyl panel using the [Ptero4J](https://github.com/stanjg/Ptero4J) wrapper made by stanjg, and of course using the Pterodactyl panel API.
## Requirements
- **A Pterodactyl panel.** If you're using a shared hosting plan and the panel is powered by Pterodactyl, then you are able to use this plugin. If you're selfhosting your server (home server, your PC, a VPS or dedicated server...), make sure you use the Pterodactyl panel, not only it's really easy to install, but is an excellent panel software. Please read the wiki for more information on how to check if your panel is powered by Pterodactyl.
- **A CraftBukkit server.** This plugin is coded using the Spigot 1.8.8 API, but I have tested it on Paper 1.8.8, 1.12.2 and 1.15.2, and have been told it also works on CraftBukkit 1.14.4 due to the use of simple API calls. Whatever you use, make sure it's CraftBukkit or one of its forks (e.g. Spigot, or one of its forks: Paper, BeerSpigot...)
## How to install
1. Go to your Pterodactyl panel and, on the left sidebar, click on 'Account API'. If you're using a Sparked Host server, [click here](https://control.sparkedhost.us/account/api).
2. In the top right corner, click on Create New. Add a description (e.g. "AccurateReadings") and leave the 'Allowed Connection IPs' empty unless you're **100% sure** what IP will your server be using to connect to the API (this is hard to determine on shared hosting environments as it may use the node's main IP address to communicate, which is not always obvious).
3. Copy the API key for later.
4. Upload the plugin JAR (you can grab it from the [releases page](https://github.com/BetTD/AccurateReadings/releases/)) to the plugins directory on your server.
5. Restart your server in order to generate the config file. The plugin will then disable itself, please stop the server after it has fully loaded and navigate to `plugins/AccurateReadings`, then open the config.yml file.
6. Then fill in the rest of the options: `panelUrl` is the URL of your Pterodactyl panel; `serverId` is your server ID and you can find it on the main page of your panel, at the very left of the server list; `apiKey` is the API key we copied earlier. Make sure you don't remove the quotation marks from any of the options.
7. Save the changes and start the server. Take a look at the console and carefully read through the output. If you see this: `Ptero4J loaded! Successfully made contact with the panel.` It means you're completely good to go and the plugin is ready to go. Otherwise, double-check the config.yml file. If it still doesn't work, please read "Contact me" at the end of this document.
## How to use
**The main command is `/perf`**, although there are aliases: `/performance` and `/lag`. This may override the Essentials' `/lag` command, but you can fix this in the `commands.yml` file on your server or by using `/elag`.
## Known issues
- `Command Dispatched Async: /spigot:tps`. This will be fixed in the full release.
- `[00:20:31 WARN]: [okhttp3.OkHttpClient] A connection to <insert panel link here> was leaked. Did you forget to close a response body? To see where this was allocated, set the OkHttpClient logger level to FINE: Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);`. Unfortunately I cannot find a solution to this problem no matter what I try. I will contact the Ptero4J author in order to hopefully find a solution to this.
- **The restart feature kills the server completely.** I have looked at the entire code and couldn't find a reason as to why would this happen.
## Credits
A big thank you to Square for providing the [OkHttp](https://github.com/square/okhttp) and [Okio](https://github.com/square/okio) libraries used by [Ptero4J](https://github.com/stanjg/Ptero4J), which is also a huge part of AccurateReadings by providing a wrapper to easily communicate with any Pterodactyl panel API. Thanks to the Sparked Host LLC community for giving ideas, suggestions and bug reports.
## Version history
- **1.0.0-dev01**. First version of the plugin that actually worked. It made the main thread hang while the plugin communicated with the panel.
- **1.0.0-dev02**. Fixed the main thread freezing issue. Added new options to the config.yml
- **1.0.0-pre1**. First public version of the plugin
## Contact us
- Through Discord: `The [Sparked Host Discord server](https://discord.gg/sparked)
- Through e-mail: `raul.m@sparkedhost.com`
