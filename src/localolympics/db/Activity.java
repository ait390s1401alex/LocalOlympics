package localolympics.db;
/**
 * Copyright 2014 -
 * Licensed under the Academic Free License version 3.0
 * http://opensource.org/licenses/AFL-3.0
 * 
 * Authors: Karen Bacon, Alex Verkhovtsev
 */



import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;


/**
 * GAE ENTITY UTIL CLASS: "Activity" <br>
 * PARENT: NONE <br>
 * KEY: A long Id generated by GAE <br>
 * FEATURES: <br>
 * - "name" a {@link String} with the name record for the activity<br>
 * - "description" a {@link String} with the description record for the activity<br>
 * - "location" a {@link String} with the address record for the activity<br>
 * - "type" a {@link String} with the type record for the activity<br>
 */

public final class Activity {

	//
	// SECURITY
	//

	/**
	 * Private constructor to avoid instantiation.
	 */
	private Activity() {
	}

	//
	// KIND
	//

	/**
	 * The name of the Activity ENTITY KIND used in GAE.
	 */
	private static final String ENTITY_KIND = "Activity";

	//
	// KEY
	//

	/**
	 * Return the Key for a given Activity id given as String.
	 * 
	 * @param ActivityId
	 *            A string with the Activity ID (a long).
	 * @return the Key for this ActivityID.
	 */
	public static Key getKey(String ActivityId) {
		long id = Long.parseLong(ActivityId);
		Key ActivityKey = KeyFactory.createKey(ENTITY_KIND, id);
		return ActivityKey;
	}

	/**
	 * Return the string ID corresponding to the key for the Activity.
	 * 
	 * @param Activity
	 *            The GAE Entity storing the Activity.
	 * @return A string with the Activity ID (a long).
	 */
	public static String getStringID(Entity Activity) {
		return Long.toString(Activity.getKey().getId());
	}

	//
	// ATTRIBUTES
	//

	/**
	 * The property name for the name of the Activity.
	 */
	private static final String NAME_PROPERTY = "name";
	
	/**
	 * The property description for the description of the Activity.
	 */
	private static final String DESCRIPTION_PROPERTY = "description";
	
	/**
	 * The property type for the type of the Activity.
	 */
	private static final String TYPE_PROPERTY = "type";
	
	/**
	 * The property name for the location of the activity
	 */
	private static final String LOCATION_PROPERTY = "location";

	
	private static final String TIME_LIMIT = "limit";
	
	
	
	
	//
	// GETTERS
	//
	
	
	
	/**
	 * Return the name of the Activity.
	 * 
	 * @param Activity The GAE Entity storing the Activity.
	 * @return the name of the Activity.
	 */
	public static String getLimit(Entity Activity)
	{
		Object timeLimit = Activity.getProperty(TIME_LIMIT);

		return (String) timeLimit;
	}
	public static String getName(Entity Activity) {
		Object nameofActivity = Activity.getProperty(NAME_PROPERTY);

		return (String) nameofActivity;
	}
	
	/**
	 * Return the description of the Activity.
	 * 
	 * @param Activity The GAE Entity storing the Activity.
	 * @return the description of the Activity.
	 */
	public static String getDescription(Entity Activity) {
		Object description = Activity.getProperty(DESCRIPTION_PROPERTY);

		return (String) description;
	}
	
	/**
	 * Return the type of the Activity.
	 * 
	 * @param Activity The GAE Entity storing the Activity.
	 * @return the type of the Activity.
	 */
	public static String getType(Entity Activity) {
		Object type = Activity.getProperty(TYPE_PROPERTY);

		return (String) type;
	}
	
	/**
	 * Return the address of the campus.
	 * 
	 * @param Activity The Entity storing the activity
	 * @return a String with the address.
	 */
	public static String getLocation(Entity Activity) {
		Object val = Activity.getProperty(LOCATION_PROPERTY);
		if (val == null)
			return "";
		return (String) val;
	}
	
	
	
	
	
	
	//
	// CREATE ACTIVITY
	//
	

	/**
	 * Create a new Activity if the name is correct and none exists with this
	 * name.
	 * 
	 * @param ActivityName The name for the Activity.
	 * @param Description The description for the Activity.
	 * @param Type The type for the Activity.
	 * @return the Entity created with this name or null if error
	 */
	public static Entity createActivity(String ActivityName, String description, String type, 
			String limithour, String limitminute, String limitsecond, String address) {

		Entity Activity = null;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		try {

			if (!checkName(ActivityName)) {
				return null;
			}

			Activity = getActivity(ActivityName);
			if (Activity != null) {
				return null;
			}
			long seconds = secondCalc(limithour, limitminute, limitsecond);
			String totalSecond = Long.toString(seconds);
			Activity = new Entity(ENTITY_KIND);
			Activity.setProperty(NAME_PROPERTY, ActivityName);
			Activity.setProperty(DESCRIPTION_PROPERTY, description);
			Activity.setProperty(TYPE_PROPERTY, type);
			Activity.setProperty(TIME_LIMIT, totalSecond);
			Activity.setProperty(LOCATION_PROPERTY, address);
			datastore.put(Activity);

			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}

		}
		return Activity;
	}
	private static int secondCalc(String hour, String minute, String second)
	{
		int totalSecond;
		int limithour;
		int limitminute;
		int limitsecond;
		
		limithour = Integer.parseInt(hour);
		limitminute = Integer.parseInt(minute);
		limitsecond = Integer.parseInt(second);
		totalSecond = (limithour * 3600) + (limitminute * 60) + limitsecond;
		return totalSecond;
	}
	//
	// GET Activity
	//

	/**
	 * Get a Activity based on a string containing its long ID.
	 * 
	 * @param id A {@link String} containing the ID key (a <code>long</code> number)
	 * @return A GAE {@link Entity} for the Activity or <code>null</code> if none or error.
	 */
	public static Entity getActivity(String ActivityId) {
		Entity Activity = null;
		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			long id = Long.parseLong(ActivityId);
			Key ActivityKey = KeyFactory.createKey(ENTITY_KIND, id);
			Activity = datastore.get(ActivityKey);
		} catch (Exception e) {
			// TODO log the error
		}
		return Activity;
	}
	


	
	      
	      
      //
      // DELETE ACTIVITY
      //

      /**
      * Delete the activity if not linked to anything else.
      *
      * @param activityID A string with the activity ID (a long).
      * @return True if succeed, false otherwise.
      */
      public static boolean deleteActivity(String activityID) {
	try {
	  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	  datastore.delete(getKey(activityID));
	} catch (Exception e) {
	return false;
	}
	return true;
      }


      
      
      //
      // UPDATE ACTIVITY
      //

	public static boolean UpdateActivity(String ActivityID, String name, String description, String type, String address) {
		Entity activityInput = null;
		try {
			activityInput = getActivity(ActivityID);
			activityInput.setProperty(NAME_PROPERTY, name);
			activityInput.setProperty(DESCRIPTION_PROPERTY, description);
			activityInput.setProperty(TYPE_PROPERTY, type);
			activityInput.setProperty(LOCATION_PROPERTY, address);
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			datastore.put(activityInput);
		} catch (Exception e) {
			return false;
		}
		return true;
	}


	
	
	
	//
    // NAME DUPLICATE CHECK
    //

	/**
	 * checks to see if activity was entered previously
	 * @param String name
	 * @return boolean
	 */
	public static boolean checkName(String name) {

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query(ENTITY_KIND);
		Iterable<Entity> result = datastore.prepare(query).asIterable(
				FetchOptions.Builder.withLimit(1000));

		for (Entity Activity : result) {
			if (name.equals(Activity.getProperty(NAME_PROPERTY))) {
				return false;
			}

			else {
				continue;

			}

		}
		return true;
	}
	
	
	//
	// QUERY Activity
	//

	/**
	 * Return the requested number of Activity (e.g. 100).
	 * 
	 * @param limit The number of Activity to be returned.
	 * @return A list of GAE {@link Entity entities}.
	 */
	public static List<Entity> getFirstActivity(int limit) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query(ENTITY_KIND);
		List<Entity> result = datastore.prepare(query).asList(
				FetchOptions.Builder.withLimit(limit));
		return result;
	}

}
