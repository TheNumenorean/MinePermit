package net.lotrcraft.minepermit.languages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import net.lotrcraft.minepermit.MinePermit;

import org.bukkit.ChatColor;

public enum TextManager {
	GENERIC_ERROR("An Unknown Error occcured"), 
	NOT_ENOUGH_MONEY(ChatColor.DARK_RED + "You dont have enough money!"), 
	PURCHASE_SUCCESS(ChatColor.DARK_GREEN + "Permit purchased!"),
	UNIVERSAL_PERMIT_ALREADY_OWNED(ChatColor.YELLOW + "You already own the Universal Permit!"),
	NO_PERMITS_MESSAGE(ChatColor.YELLOW + "You don't own any permits!"),
	PERMIT_NOT_REQUIRED(ChatColor.GRAY + "A permit is not required for this item."),
	MISSING_PERMIT_ERROR(ChatColor.DARK_RED + "You may not mine these blocks! Use /permit buy <id> to buy a permit."),
	PERMIT_ALREADY_OWNED(ChatColor.YELLOW + "You already have a permit for this!");

	private String text;

	private TextManager(String s) {
		text = s;
	}

	protected void setText(String s) {
		text = s;
	}

	public String toString() {
		return text;
	}

	public static void loadTextFromFile(File f) {

		Map<String, String> vals = new TreeMap<String, String>();
		FileInputStream input;
		String end;

		try {
			input = new FileInputStream(f);
			
			int data = input.read();
			end = "";

			while (data > -1) {
				end = end + (char) data;
				data = input.read();
			}

		} catch (FileNotFoundException e) {
			throw new InvalidLanguageFileException("File doesn't exist!");
		} catch (IOException e) {
			throw new InvalidLanguageFileException("Can't read File!");
		}

		for (String s : end.trim().split("\n")) {
			String[] tmp = s.split(":", 2);
			if (tmp.length < 2)
				continue;
			vals.put(tmp[0], tmp[1]);
		}

		for (TextManager t : TextManager.values()) {
			String tmp = vals.get(t.name());
			if (tmp == null)
				continue;
			t.setText(ChatColor.translateAlternateColorCodes('&', tmp));
		}
	}
}
