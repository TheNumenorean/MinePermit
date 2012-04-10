package net.lotrcraft.minepermit.languages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public enum TextManager {
	GENERIC_ERROR(""), NOT_ENOUGH_MONEY(""), PURCHASE_SUCCESS("");

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

			while (data != -1) {
				end += (char)data;
			}

		} catch (FileNotFoundException e) {
			throw new InvalidLanguageFileException("File doesn't exist!");
		} catch (IOException e) {
			throw new InvalidLanguageFileException("Can't read File!");
		}
		
		for(String s : end.trim().split("\n")){
			String[] tmp = s.split(":", 2);
			if(tmp.length < 2)
				continue;
			vals.put(tmp[0], tmp[1]);
		}
		
		for(TextManager t : TextManager.values()){
			String tmp = vals.get(t.name());
			if(tmp == null)
				continue;
			t.setText(tmp);
		}
	}
}
