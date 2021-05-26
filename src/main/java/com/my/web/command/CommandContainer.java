package com.my.web.command;

import com.my.web.command.cashier.AddProductsIntoCurrentReceiptCommand;
import com.my.web.command.cashier.CreateReceiptCommand;
import com.my.web.command.cashier.EditReceiptProductsCommand;
import com.my.web.command.cashier.SetReceiptStatusClosedCommand;
import com.my.web.command.commodity_expert.AddProductCommand;
import com.my.web.command.senior_cashier.RemoveProductFromReceiptCommand;
import com.my.web.command.senior_cashier.SetReceiptStatusCanceledCommand;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;

public class CommandContainer {

    private static final Logger logger = Logger.getLogger(CommandContainer.class);
    private static final Map<String, Command> commands = new TreeMap<String, Command>();

    static {
        commands.put("login", new LoginCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("noCommand", new NoCommand());
        commands.put("viewMenu", new ViewMenuCommand());
        commands.put("changeLocaleToRu", new ChangeLangToRuCommand());
        commands.put("changeLocaleToEn", new ChangeLangToEnCommand());
        commands.put("viewSettings", new ViewSettingsCommand());

        commands.put("addProduct", new AddProductCommand());
        commands.put("viewProduct", new ViewProductCommand());
        commands.put("viewSearchProductPage", new ViewSearchProductPageCommand());
        commands.put("searchProduct", new SearchProductsCommand());
        commands.put("editProduct", new EditProductCommand());

        commands.put("createReceipt", new CreateReceiptCommand());
        commands.put("viewCurrentReceipt", new ViewCurrentReceiptCommand());
        commands.put("addProductsIntoCurrentReceipt", new AddProductsIntoCurrentReceiptCommand());
        commands.put("searchReceipt", new SearchReceiptCommand());
        commands.put("viewSearchReceiptPage", new ViewSearchReceiptPageCommand());
        commands.put("chooseReceipt", new ChooseReceiptCommand());
        commands.put("editReceiptProducts", new EditReceiptProductsCommand());
        commands.put("viewReceiptProducts", new ViewReceiptProductsPageCommand());
        commands.put("setReceiptStatusClosed", new SetReceiptStatusClosedCommand());

        commands.put("setReceiptStatusCanceled", new SetReceiptStatusCanceledCommand());
        commands.put("removeProductFromReceipt", new RemoveProductFromReceiptCommand());
    }

    public static Command get(String commandName) {
        if (commandName == null || !commands.containsKey(commandName)) {
            logger.trace("Command not found, name --> " + commandName);
            return commands.get("noCommand");
        }
        return commands.get(commandName);
    }

}
