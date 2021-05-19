package com.my.web.command;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;

public class CommandContainer {

    private static final Logger logger = Logger.getLogger(CommandContainer.class);
    private static Map<String, Command> commands = new TreeMap<String, Command>();

    static {
        commands.put("login", new LoginCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("noCommand", new NoCommand());
        commands.put("menu", new MenuCommand());
        commands.put("changeLocaleToRu", new ChangeLangToRuCommand());
        commands.put("changeLocaleToEn", new ChangeLangToEnCommand());
        commands.put("viewSettings", new SettingsCommand());
        commands.put("addProduct", new AddProductCommand());
    }

    public static Command get(String commandName) {
        if (commandName == null || !commands.containsKey(commandName)) {
            logger.trace("Command not found, name --> " + commandName);
            return commands.get("noCommand");
        }
        return commands.get(commandName);
    }

}
