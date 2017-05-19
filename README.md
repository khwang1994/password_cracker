Password Cracker

Introduction:
The program takes in a dictionary and a file analogous to a Unix /etc/passwd file. Using various password mangling techniques, the program deciphers the passwords that have been encrypted using JCrypt. It prints each password to the console once it has been cracked. 

To compile:
javac *.java

To run:
java PasswordCrack [dictionary] [passwd file]

Code Description:
1) PasswordCrack.java
  This is the main portion of the program. It takes two
input files from the command line (a "/etc/passwd" file and a file containing a
dictionary). From the passwd file, the program goes line-by-line, creates a User 
object and stores each User in an ArrayList userList. From the dictionary file,
the program adds each word to an ArrayList<String> wordList.

  The mangling process occurs in the function mangle(). There are also 9 helper 
functions that assist in the mangling process, each performing a "mangling"
task (e.g. reverse, reflect, capitalize, ncapitalize, etc). The function mangles
a given ArrayList<String> of words for a specified # of rounds through recursive
calls.

  Some mangling procedures were not utilized (such as appending an ASCII char to the
front or end of a word) because it took too long to run.

  The guessPW() function determines if a guessed password is correct (using jcrypt). 
If so, it prints the user and the password to the console.

2) User.java
  Represents a user in the system. Stores their name, salt, encrypted
password, actual password and whether or not we have "cracked" their pw.

3) jcrypt.java 
  This code is taken from the assignment webpage. It is used to check a password.
