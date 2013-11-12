package ak.expensemanager.contacts;

import java.util.Map;

public interface IContactInfo {

	/**
	 * Add this contact name and number in db.
	 * This can be contact info of the banks from which
	 * expense SMSs are received 
	 * */
	boolean addContact(String name, String number);
	
	
	/**
	 * When new sms is received,check if sender is 
	 * one of the contact(bank) stored by user
	 * */
	boolean searchContact(String number);
	
	/**
	 * Remove this contact/bank info from
	 * contact list
	 * */
	boolean removeContact(String number);
	
	/**
	 * Get all the contacts stored in the db
	 * */
	Map<String , ? > getAllContacts();
	
	boolean	searchSubStringContact(String number);
	
}
