package com.example.bhargav.customalertdialog;

import java.util.Random;
import java.util.ResourceBundle;

public class utility {

	private String smallChar = "deijxyzklmnopqrsabctuvwfgh";
	private String largeChar = "AMNOBCGHIJKWXYLPQRSTUDEFVZ";
	private String number = "1084237596";
	private String specialChar = "!@#$%^&*()-=+_~`?/[]{},.;:";

	private Random rand = new Random();

	private String generatePartialPassword(String password, String type) {
		password += type.charAt(rand.nextInt(type.length()));
		return password;
	}

	private int countPronounce(String password, String type) {
		int total = 0;

		for(int i = 0; i<password.length(); i++) {
			if(type.indexOf(password.charAt(i)) != -1) {
				total += 1;
			}
		}

		return total;
	}

	private void repeatComputation(int temp, int[] strengthVariables, int val) {
		if(temp == 0) {
			strengthVariables[val] = 0;
		} else if(temp == 1) {
			strengthVariables[val] = 1;
		} else {
			strengthVariables[val] = 2;
		}
	}

	public String passwordStrenght(String password) {
		int totalScore = 0;
		int len = password.length();
		int temp = 0;
		int strengthVariables[] = new int[6];

        if (len < 6) {
            return "#FF0000";
        } else if (len >=6 && len < 8) {
            return "#FFA000";
        }

		temp = (len*4);

		if(temp >= 32 && temp < 36) {
			strengthVariables[0] = 1;
		} else if(temp > 36) {
			strengthVariables[0] = 2;
		} else {
			strengthVariables[0] = 0;
		}

		repeatComputation(countPronounce(password, smallChar), strengthVariables, 1);
		repeatComputation(countPronounce(password, largeChar), strengthVariables, 2);
		repeatComputation(countPronounce(password, number), strengthVariables, 3);
		repeatComputation(countPronounce(password, specialChar), strengthVariables, 4);

		password = password.substring(1, (password.length()-1));

		repeatComputation(countPronounce(password, number)+countPronounce(password, specialChar), strengthVariables, 5);

		for(int i=0; i<strengthVariables.length; i++) {
			totalScore += strengthVariables[i];
		}

		if(totalScore >=0 && totalScore < 4) {
			return "#FF0000"; // Red
		} else if(totalScore >=4 && totalScore <= 6) {
			return "#FFA000"; // Orange
		} else if(totalScore > 6 && totalScore <= 9) {
			return "#009400"; // Green
		} else if(totalScore > 9 && totalScore < 12) {
			return "#00AAFF"; // Blue
		} else if(totalScore == 12) {
			return "#303F9F"; // Darkblue
		} else {
			return "#000000";
		}
	}

	public String generatePassword(boolean smaChar, boolean larChar, boolean num, boolean speChar, int len, String special) {
		String password = "";
		int ran = 0;
		Boolean status = true;

		if(special != null && !special.isEmpty()) {
			specialChar = special;
		}

		if(speChar) {
			password = generatePartialPassword(password, specialChar);
		}

		if (num) {
			password = generatePartialPassword(password, number);
		}

		if (larChar) {
			password = generatePartialPassword(password, largeChar);
		}

		if (smaChar) {
			password = generatePartialPassword(password, smallChar);
		}

		int condition = (len - password.length());

		for(int i = 0; i < condition; i++) {

			int max = 7;
			int min = 2;

			status = true;

			while(status) {

				ran = rand.nextInt((max-min)+1)+min;

				if((ran%5==0 || ran%7==0) && speChar) {
					password = generatePartialPassword(password, specialChar);
					status = false;
				} else if (ran%4==0 && num) {
					password = generatePartialPassword(password, number);
					status = false;
				} else if (ran%3==0 && larChar) {
					password = generatePartialPassword(password, largeChar);
					status = false;
				} else if (ran%2==0 && smaChar) {
					password = generatePartialPassword(password, smallChar);
					status = false;
				}

			}
		}

		return password;
	}
}
