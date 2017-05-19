import java.util.*;
import java.io.*;

public class PasswordCrack{

	public static ArrayList<String> wordList;
	public static ArrayList<User> userList;

	public static void main(String[] args) throws IOException{
		String dict = args[0];
		Scanner pwlist = new Scanner(new File(args[1]));
		double start = 0;
		double end = 0;

		wordList = new ArrayList<String>();
		userList = new ArrayList<User>();

		start = System.nanoTime();

		while(pwlist.hasNextLine()){
			Scanner line = new Scanner(pwlist.nextLine());

			line.useDelimiter(":");

			String salt = "";
			String ePassword = "";
			String gcos = "";
			int entryCnt = 0;

			while(line.hasNext()){
				String entry = line.next();
				switch(entryCnt){
					case 0:
						break;
					case 1: 
						if(entry.length() != 13){
							System.out.println("Encrypted password field is invalid." +
									"Must be 13 char long");
							return;
						}
						salt = entry.substring(0, 2);
						ePassword = entry;
						break;
					case 2:
						break;
					case 3:
						break;
					case 4:
						gcos = entry;
						name_to_wdlist(gcos);
						break;
					case 5:
						break;
					case 6:
						break;
					default:
						System.out.println("Invalid line");
						return;
				}
				entryCnt++;
			}
			entryCnt = 0;
			userList.add(new User(salt, ePassword, gcos));
			line.close();
		}
		pwlist.close();
		init_wordlist(dict);

		for(int i = 0; i < wordList.size(); i++){
			String word = wordList.get(i);
			guessPW(word);
		}
		mangle(wordList, 2);
		end = System.nanoTime();
		double duration = (end - start)/1000000000;
		System.out.println("\n---Duration = " + duration + " seconds---");
	}

	public static void name_to_wdlist(String name){
		Scanner scan = new Scanner(name);
		while(scan.hasNext()){
			wordList.add(scan.next().toLowerCase());
		}

	}

	public static void init_wordlist(String dictFile) throws IOException{
		Scanner scan = new Scanner(new File(dictFile));
		while(scan.hasNext()){
			wordList.add(scan.next().toLowerCase());
		}
	}

	public static void mangle(ArrayList<String> words, int rounds){
		ArrayList<String> mangleList = new ArrayList<String>();
		if(rounds > 0){
			for(int i = 0; i < words.size(); i++){
				mangleList.add(delFirstChar(words.get(i)));
				mangleList.add(delLastChar(words.get(i)));
				mangleList.add(reverse(words.get(i)));
				mangleList.add(duplicate(words.get(i)));
				mangleList.add(reflect1(words.get(i)));
				mangleList.add(reflect2(words.get(i)));
				mangleList.add(toUpper(words.get(i)));
				mangleList.add(toLower(words.get(i)));
				mangleList.add(capitalize(words.get(i)));
				mangleList.add(ncapitalize(words.get(i)));
			}
			mangle(mangleList, rounds - 1);
		}
		else{
			return;
		}
	}

	/*  Checks to see if a guessed password is correct.
		Prints out a correct password with its given user name.
		Returns TRUE if a guess is correct, FALSE otherwise. */
	public static boolean guessPW(String pw){
		for(int j = 0; j < userList.size(); j++){
				User user = userList.get(j);
				if(!user.cracked){
					String jc = jcrypt.crypt(user.salt, pw);
					if(user.ePassword.equals(jc)){
						System.out.println("Password for user " + user.name + " is: " + pw);
						user.setCracked(true);
						user.setPW(pw);
						return true;
					}
				}
			}
		return false;
	}

	/*  Appends a valid ASCII character to word; in one instance to the front and
		in another to the back, then sends the result to guessPW(). Goes through
		all valid ASCII keyboard characters */
	public static boolean append(String word){
		boolean x = false;
		boolean y = false;
		for(int i = 33; i <= 126; i++){
			char c = (char)i;
			x = guessPW(word.concat(Character.toString(c)));
			y = guessPW(Character.toString(c).concat(word));
		}

		if(x | y){
			return true;
		}
		else{
			return false;
		}
	}

	/*  Deletes the first character in word and tests the result in guessPW(). 
		Returns the new word */
	public static String delFirstChar(String word){
		if(word.length() <= 1){
			return word;
		}
		word = word.substring(1, word.length());
		guessPW(word);
		return word;

	}

	/*  Deletes the last character in word and tests the result in guessPW(). 
		Returns the new word */
	public static String delLastChar(String word){
		if(word.length() <= 1){
			return word;
		}
		word = word.substring(0, word.length() - 1);
		guessPW(word);
		return word;

	}

	/*  Reverses the given word and tests the result in guessPW(). 
		Returns the new word */
	public static String reverse(String word){
		StringBuilder result = new StringBuilder();
		if(word.length() <= 1){
			return word;
		}

		for(int i = word.length() - 1; i >= 0; i--){
			result.append(word.charAt(i));
		}
		guessPW(result.toString());
		return result.toString();
	}

	/*  Duplicates word and appends it to the end of word. Tests result in 
		guessPW(). Returns the new word */
	public static String duplicate(String word){
		StringBuilder result = new StringBuilder(word);
		guessPW(result.append(word).toString());
		return result.toString();
	}
	/*  Flips word and appends it to the end of word. Tests result in guessPW().
		Returns the new word. */
	public static String reflect1(String word){
		if(word.length() <= 1){
			return word;
		}
		StringBuilder result = new StringBuilder(word);
		result.append(reverse(word)).toString();
		return result.toString();
	}

	/*  Flips word and appends it to the front of word. Tests result in guessPW().
		Returns the new word. */
	public static String reflect2(String word){
		if(word.length() <= 1){
			return word;
		}
		StringBuilder result = new StringBuilder(reverse(word));
		guessPW(result.append(word).toString());
		return result.toString();
	}

	public static String toUpper(String word){
		String result = word.toUpperCase();
		guessPW(result);
		return result;
	}


	public static String toLower(String word){
		String result = word.toLowerCase();
		guessPW(result);
		return result;
	}

	public static String capitalize(String word){
		int index = 0;

		for(int i = 0; i < word.length(); i++){
			if(word.charAt(i) > (char)96 && word.charAt(i) < (char)123){
				index = i;
				break;
			}
		}


		String preLetter = word.substring(0, index).toLowerCase();
		String letter = word.substring(index, (index + 1)).toUpperCase();
		String postLetter = word.substring((index + 1), word.length()).toLowerCase();
		String result = preLetter.concat(letter).concat(postLetter);
		guessPW(result);
		return result;
	}


	// /* 	Capitalizes the first letter in word. Tests result in guessPW(). Returns
	// 	the new word. */
	// public static String capitalize(String word){
	// 	if(word.charAt(0) > (char)64 && word.charAt(0) < (char)91){
	// 		return word;
	// 	}
	// 	String firstL = word.substring(0, 1).toUpperCase();
	// 	String result = firstL.concat(word.substring(1, word.length()).toLowerCase());
	// 	guessPW(result);
	// 	return result;
	// }

	/*  Capitalizes all but the first letter in word. Tests result in guessPW().
		Returns the new word. */
	public static String ncapitalize(String word){
		StringBuilder firstLetter = new StringBuilder(word.substring(0, 1).toLowerCase());
		firstLetter.append(word.substring(1, word.length()).toUpperCase());
		guessPW(firstLetter.toString());
		return firstLetter.toString();
	}
}
