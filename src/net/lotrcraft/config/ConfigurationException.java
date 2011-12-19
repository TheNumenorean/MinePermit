/**
 * This code is all originally borrowed from Bukkit. 
 * This version of the configuration was abandoned, and i have decided to
 * take the old code and fix it for my personal use.
 * 
 * Let it be known, the original author of all of this is the Bukkit Team!
 * 
 * P.S. The new version of the bukkit configuration is awful.
 */

package net.lotrcraft.config;

public class ConfigurationException extends Exception {
    private static final long serialVersionUID = -2442886939908724203L;

    public ConfigurationException() {
        super();
    }

    public ConfigurationException(String msg) {
        super(msg);
    }
}
