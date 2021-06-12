package com.my.web.command;

import com.my.web.command.cashier.AddProductsIntoCurrentReceiptCommand;
import com.my.web.command.cashier.CreateReceiptCommand;
import com.my.web.command.cashier.EditReceiptProductsCommand;
import com.my.web.command.cashier.SetReceiptStatusClosedCommand;
import com.my.web.command.commodity_expert.CreateProductCommand;
import com.my.web.command.commodity_expert.EditProductCommand;
import com.my.web.command.common.*;
import com.my.web.command.common.setting.ChangePasswordCommand;
import com.my.web.command.common.setting.ChangeUserLocaleCommand;
import com.my.web.command.common.setting.SendConfirmationLinkCommand;
import com.my.web.command.senior_cashier.*;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;

/**
 * Holder for all commands
 */
public class CommandContainer {

    private static final Logger logger = Logger.getLogger(CommandContainer.class);
    private static final Map<String, Command> commands = new TreeMap<>();

    static {
        commands.put("login", new LoginCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("noCommand", new NoCommand());
        commands.put("viewMenu", new ViewMenuCommand());
        commands.put("changeLanguage", new ChangeLanguageCommand());
        commands.put("viewSettings", new ViewSettingsCommand());

        commands.put("createProduct", new CreateProductCommand());
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
        commands.put("viewReportsMenu", new ViewReportsMenuCommand());
        commands.put("searchCashier", new SearchCashierCommand());
        commands.put("viewSearchCashierResult", new ViewSearchCashierResultCommand());

        commands.put("changeUserLocale", new ChangeUserLocaleCommand());
        commands.put("sendConfirmationLink", new SendConfirmationLinkCommand());
        commands.put("changePassword", new ChangePasswordCommand());
    }

    /**
     * Return command object with given name
     *
     * @param commandName Name of the command
     * @return Command object
     */
    public static Command get(String commandName) {
        if (commandName == null || !commands.containsKey(commandName)) {
            logger.trace("Command not found, name --> " + commandName);
            return commands.get("noCommand");
        }
        return commands.get(commandName);
    }

}
