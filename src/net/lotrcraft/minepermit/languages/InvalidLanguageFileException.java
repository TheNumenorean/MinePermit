package net.lotrcraft.minepermit.languages;

public class InvalidLanguageFileException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5407560092645274575L;
	
	public InvalidLanguageFileException(){
		super();
	}
	
	public InvalidLanguageFileException(String s){
		super(s);
	}

}
