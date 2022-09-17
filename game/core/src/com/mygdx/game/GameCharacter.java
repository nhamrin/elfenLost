/**
 * This class defines the structure for the game's characters and contains the methods used in
 * the `MyGdxGame` class.
 * 
 * @author Nils Hamrin
 * @version 2022-05-10
 */

package com.mygdx.game;

public class GameCharacter {
	private int health, strength, amountFish, amountMoney;
	
	public GameCharacter(int startHealth, int startStrength, int startFish, int startMoney) {
		health = startHealth;
		strength = startStrength;
		amountFish = startFish;
		amountMoney = startMoney;
	}
	
	/**
	 * Used to decrease the character's health
	 */
	public void takeDamage(int damage) {
		health -= damage;
	}
	
	public int getHealth() {
		return health;
	}
	
	/**
	 * Used to increase the character's fish count
	 */
	public void aquireFish() {
		amountFish++;
	}
	
	/**
	 * Used to decrease the character's fish count and increase the character's money
	 */
	public void sellFish() {
		if (amountFish > 0) {
			amountFish--;
			amountMoney++;
		}
	}
	
	public int getAmountFish() {
		return amountFish;
	}
	
	/**
	 * Used to increase the character's money
	 * @param money
	 */
	public void aquireMoney(int money) {
		amountMoney += money;
	}
	
	public int getAmountMoney() {
		return amountMoney;
	}
	
	/**
	 * Used to increase the character's strength
	 * @param strength
	 */
	public void gainStrength(int strength) {
		this.strength += strength;
	}
	
	public int getStrength() {
		return strength;
	}
}
