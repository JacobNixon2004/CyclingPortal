package cycling;

import java.time.temporal.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.*;


/**
 * BadCyclingPortal is a minimally compiling, but non-functioning implementor
 * of the CyclingPortal interface.
 * 
 * @author Diogo Pacheco
 * @version 2.0
 *
 */
public class CyclingPortalImpl implements CyclingPortal {
	private int numRaces = 0;
	private int numRiders = 0;
	private int numStages = 0;
	private int numTeams = 0;
	//Hash maps that contain the RaceIDs as the index and the RaceNames as the values, and vice versa 
	private HashMap<String, Integer> raceNameIds = new HashMap<String, Integer>();
	private HashMap<Integer, String> raceIdNames = new HashMap<Integer, String>();
	private HashMap<String, Race> raceNames = new HashMap<String, Race>();
	//Integer = raceID, int[] is a list of stage IDs, used to list stages in a race
	private HashMap<Integer, ArrayList<Integer>> raceStages = new HashMap<Integer, ArrayList<Integer>>();
	private HashMap<Integer, Integer> stageRaces = new HashMap<Integer, Integer>();
	//Hash maps using stage details to allow access to the stage itself, as well as access name and ID attributes
	private HashMap<String, Integer> stageNameIds = new HashMap<String, Integer>();
	private HashMap<Integer, String> stageIdNames = new HashMap<Integer, String>();
	private HashMap<String, Stage> stageNames = new HashMap<String, Stage>();
	//HashMap containing teamID and a list of racerIDs
	private HashMap<Integer, Rider> riderIdObj = new HashMap<Integer, Rider>();
	private HashMap<Integer, ArrayList<Integer>> teamRiders = new HashMap<Integer, ArrayList<Integer>>(); 
	private HashMap<Integer, Integer> riderTeam = new HashMap<Integer, Integer>();
	//HashMaps for teams
	private HashMap<String, Integer> teamNameIds = new HashMap<String, Integer>();
	private HashMap<Integer, String> teamIdNames = new HashMap<Integer, String>();
	private HashMap<String, Team> teamNames = new HashMap<String, Team>();
	//Hashmaps for Checkpoints
	private HashMap<Integer, ArrayList<Integer>> stageCheckpoint = new HashMap<Integer, ArrayList<Integer>>();
	private HashMap<Integer, HashMap<Integer, Checkpoint>> stageCheckpointIds = new HashMap<Integer, HashMap<Integer, Checkpoint>>();
	private HashMap<Integer, Integer> checkpointStage = new HashMap<Integer, Integer>();
	private HashMap<Integer, String> stagePreparation = new HashMap<Integer, String>();
	//Hashmaps for the Rider's results
	private HashMap<Integer, HashMap<Integer, LocalTime[]>> stageRiders = new HashMap<Integer, HashMap<Integer, LocalTime[]>>();
	private HashMap<Integer, HashMap<Integer, HashMap<Integer, LocalTime[]>>> ridersStageResults = new HashMap<Integer, HashMap<Integer, HashMap<Integer, LocalTime[]>>>();
	private HashMap<Integer, HashMap<Integer, LocalTime>> stageRiderElapsed = new HashMap<Integer, HashMap<Integer, LocalTime>>();
	private HashMap<Integer, LocalTime> idElapse = new HashMap<Integer, LocalTime>();
	private HashMap<LocalTime, LocalTime> adjustedElapse = new HashMap<LocalTime, LocalTime>();
	//Arrays for points
	private int[] flatPoints = {50, 30, 20, 18, 16, 14, 12, 10, 8, 7, 6, 5, 4, 3, 3}; 
	private int[] hillyPoints = {30, 25, 22, 19, 17, 15, 13, 11, 9, 7, 6, 5, 4, 3, 2};
	private int[] highPoints = {20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
	private int[] c1Points = {10, 8, 6, 4, 2, 1};
	private int[] c2Points = {5, 3, 2, 1};
	private int[] c3Points = {2, 1};
	private int[] c4Points = {1};
	private int[] hcPoints = {20, 15, 12, 10, 8, 6, 4, 2};

	@Override
	public int[] getRaceIds() {
		//Getting all the key values from raceIdNames and returning them 
		int[] listRaceIds = new int[raceIdNames.size()];
		int j = 0;
		for (String i : raceNameIds.keySet()){
  			listRaceIds[j] = raceNameIds.get(i);
			j++;
		}
		return listRaceIds;
	}

	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		int newNumRaces = numRaces;
		//Checks if the name of the race is already in the HashMap
		if (raceNameIds.get(name) != null){
			throw new IllegalNameException("Race name taken");
		}	
		//Checks if the name is in valid form
		else if (name == null || name == "" || name.length() > 30 || name.contains(" ")){
			throw new InvalidNameException("Race name invalid");
		}
		//Create a new class with the names and id, also adds to the HashMap
		else{
			Race race = new Race(numRaces, 0, name, description);
			ArrayList<Integer> stageList = new ArrayList<Integer>();
			raceNameIds.put(name, numRaces);
			raceIdNames.put(numRaces, name);
			raceNames.put(name, race);
			raceStages.put(numRaces, stageList);
			numRaces = numRaces + 1;
		}	
		return newNumRaces;
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		//Checks if a RaceID is attached to a race
		if(raceIdNames.get(raceId) == null){
			throw new IDNotRecognisedException("ID not attached to a race");
		}
		else{
		//Gets the details of the race based off the RaceID	
			String raceName = raceIdNames.get(raceId);
			Race thisRace = raceNames.get(raceName);
			String raceDetails = thisRace.getDetails();
			Double length = 0;
			for(Integer i : raceStages.get(raceId)){
				length = length + ((stageNames.get(stageIdNames.get(i))).getLength());
			}
			raceDetails = String.format(raceDetails " Length of the race "+ length);
			return raceDetails;
		}
			
	}


	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		//Checks if the RaceId is attached to a race
		if(raceIdNames.get(raceId) == null){
			throw new IDNotRecognisedException("ID not attached to a race");
		}
		else{
			//Removes the race based of the RaceId in all the HashMaps
			String raceName = raceIdNames.get(raceId);
			raceIdNames.remove(raceId);
			raceNameIds.remove(raceName);
			raceNames.remove(raceName);
			ArrayList<Integer> thisStages = raceStages.get(raceId);
			for(int i = 0; i < thisStages.size(); i++){
				stageRaces.remove(i);
			}
			raceStages.remove(raceId);
		}
	}


	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		//Checks if the RaceId is attached to a race
		if(raceIdNames.get(raceId) == null){
			throw new IDNotRecognisedException("ID not attached to a race");
		}
		else{
			//Gets the number of stages in the race based of the Id given
			String raceName = raceIdNames.get(raceId);
			Race thisRace = raceNames.get(raceName);
			Integer numberStages = thisRace.getNumberOfStages();
			return numberStages;
		}
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,
			StageType type)
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		//Checks and throws the exceptions
		int newStageId = numStages;
		if(raceIdNames.get(raceId) == null){
			throw new IDNotRecognisedException("ID not attached to a race");
		}
		else if(stageNameIds.get(stageName) != null){
			throw new IllegalNameException("Stage name taken");
		}
		else if (stageName == null || stageName == "" || stageName.length() > 30 || stageName.contains(" ")){
			throw new InvalidNameException("Stage name invalid");
		}
		else if (length < 5.0){
			throw new InvalidLengthException("Stage too short");
		}
		else{
			//Creates the stage
			Stage stage = new Stage(stageName, length, numStages, description, type, raceId, null, null, startTime, 0); //New stage
			ArrayList<Integer> checkpointList = new ArrayList<Integer>();
			stageCheckpoint.put(numStages, checkpointList);
			stagePreparation.put(numStages , "Preparing");
			ArrayList<Integer> currentStages = raceStages.get(raceId);//Get the list of stages currently in the race and the current length
			currentStages.add(numStages); //Add this stage to the next spot in the list
			raceStages.remove(raceId); //Remove the race from the HashMap then add it back with updated list
			raceStages.put(raceId, currentStages);
			//Puts all the details into Hashmaps
			stageRaces.remove(numStages);
			stageRaces.put(numStages, raceId);
			stageNameIds.put(stageName, numStages);
			stageIdNames.put(numStages, stageName);
			stageNames.put(stageName, stage);
			HashMap<Integer, LocalTime[]> riderResults = new HashMap<Integer, LocalTime[]>();
			//riderResults.put(null, null);
			//riderElapsed.put(null, null);
			HashMap<Integer, LocalTime>riderElapsed = new HashMap<Integer, LocalTime>();
			HashMap<Integer, Checkpoint>checkpointIdName = new HashMap<Integer, Checkpoint>();
			stageCheckpointIds.put(numStages, checkpointIdName);
			stageRiderElapsed.put(numStages, riderElapsed);
			stageRiders.put(numStages, riderResults);
			(raceNames.get((raceIdNames.get(raceId)))).setNumberOfStages();
			numStages = numStages + 1;
			
		}
		return newStageId;
	}

	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		//Checks if the raceId is valid
		if(raceIdNames.get(raceId) == null){
			throw new IDNotRecognisedException("ID not attached to a race");
		}
		else{
			//Gets the list of the stages' IDs based off the race Id and returns it 
			int[] listStageIds = new int[stageIdNames.size()];
			int j = 0;
			for (String i : stageNameIds.keySet()){
  				listStageIds[j] = stageNameIds.get(i);
				j++;
			}
			return listStageIds;
		}

	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		//Checks if the stage Id is valid
		if(raceIdNames.get(stageId) == null){
			throw new IDNotRecognisedException("ID not attached to a stage");
		}
		else{
			//Gets the stage Length from the object based off the stage Id and returns it
			String thisName = stageIdNames.get(stageId);
			Stage thisStage = stageNames.get(thisName);
			Double stageLength = thisStage.getLength();
			return stageLength;
		}

	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		//Checks if the stage Id is valid
		if(raceIdNames.get(stageId) == null){
			throw new IDNotRecognisedException("ID not attached to a stage");
		}
		else{
			//Gets the stage object name
			String raceName = stageIdNames.get(stageId);
			int thisId = stageRaces.get(stageId);
			ArrayList<Integer> removeStages = raceStages.get(thisId);
			//Removes the stage Ids from the array
				for (Integer j : removeStages){
					if(removeStages.get(j) == stageId){
						removeStages.set(j, -1);
					}
				}
				raceStages.remove(thisId);
				raceStages.put(thisId, removeStages);
			//Remove the stage data from all the Hashmaps 
			stageIdNames.remove(stageId);
			stageNameIds.remove(raceName);
			stageNames.remove(raceName);
			}	
	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {
		String stageName = stageIdNames.get(stageId);
		Stage stageObj = stageNames.get(stageName);
		int numCheckpoint = stageObj.getNumOfCheckpoints();
		int newCheckpointId = numCheckpoint;
		if(stageIdNames.get(stageId) == null){
			throw new IDNotRecognisedException("ID not attached to a stage");
		}
		else if(stagePreparation.get(stageId) == "waiting for results"){
			throw new InvalidStageStateException("Stage is waiting for results");
		}
		else if(location > (stageNames.get((stageIdNames.get(stageId)))).getLength()){
			throw new InvalidLocationException("Checkpoint out of range of the stage");
		}
		else if((stageNames.get((stageIdNames.get(stageId)))).getType() == StageType.TT){
			throw new InvalidStageTypeException("Time-trial stages cannot contain any checkpoints");
		}
		else{
			//Creates a new checkpoint
			Checkpoint checkpoint = new Checkpoint(type, location, averageGradient, numCheckpoint, length);
			ArrayList<Integer> currentCheckpoints = stageCheckpoint.get(stageId);//Get the list of checkpoint currently in the stage and the current length
			currentCheckpoints.add(numCheckpoint); //Add this checkpoint to the next spot in the list
			stageCheckpoint.remove(stageId); //Remove the stage from the HashMap then add it back with updated list
			stageCheckpoint.put(stageId, currentCheckpoints);
			checkpointStage.put(numCheckpoint, stageId);
			HashMap<Integer, Checkpoint> checkpointIdName = stageCheckpointIds.get(stageId);
			checkpointIdName.put(numCheckpoint, checkpoint);
			stageCheckpointIds.remove(stageId);
			stageCheckpointIds.put(stageId,checkpointIdName);
			stageObj.setNumOfCheckpoints();
		}
		return newCheckpointId;
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		//Get stage details and checkpoints
		String stageName = stageIdNames.get(stageId);
		Stage stageObj = stageNames.get(stageName);
		int numCheckpoint = stageObj.getNumOfCheckpoints();
		int newCheckpointId = numCheckpoint;
		if(stageIdNames.get(stageId) == null){
			throw new IDNotRecognisedException("ID not attached to a stage");
		}
		else if(stagePreparation.get(stageId) == "waiting for results"){
			throw new InvalidStageStateException("Stage is waiting for results");
		}
		else if(location > (stageNames.get((stageIdNames.get(stageId)))).getLength()){
			throw new InvalidLocationException("Checkpoint out of range of the stage");
		}
		else if((stageNames.get((stageIdNames.get(stageId)))).getType() == StageType.TT){
			throw new InvalidStageTypeException("Time-trial stages cannot contain any checkpoints");
		}
		else{
			Checkpoint checkpoint = new Checkpoint(location, numCheckpoint);
			ArrayList<Integer> currentCheckpoints = stageCheckpoint.get(stageId);//Get the list of checkpoint currently in the stage and the current length
			currentCheckpoints.add(numCheckpoint); //Add this checkpoint to the next spot in the list
			stageCheckpoint.remove(stageId); //Remove the stage from the HashMap then add it back with updated list
			stageCheckpoint.put(stageId, currentCheckpoints);
			checkpointStage.put(numCheckpoint, stageId);
			HashMap<Integer, Checkpoint> checkpointIdName = stageCheckpointIds.get(stageId);
			checkpointIdName.put(numCheckpoint, checkpoint);
			stageCheckpointIds.remove(stageId);
			stageCheckpointIds.put(stageId,checkpointIdName);
			stageObj.setNumOfCheckpoints();
		}
		return newCheckpointId;
	}

	@Override
	public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
		if(checkpointStage.get(checkpointId) == null){
			throw new IDNotRecognisedException("ID not attached to a checkpoint");
		}
		else if(stagePreparation.get(checkpointStage.get(checkpointId)) == "waiting for results"){
			throw new InvalidStageStateException("Stage is waiting for results");
		}
		else{
			//Gets the checkpoint object name
			int removeCheckpoint = checkpointStage.get(checkpointId);
			ArrayList<Integer> checkpointArray = stageCheckpoint.get(removeCheckpoint);
			//Removes the checkpoint Ids from the array
			for (Integer j : checkpointArray){
				if(checkpointArray.get(j) == checkpointId){
				checkpointArray.set(j, -1);
					}
				}
			//Remove the checkpoint data from all the Hashmaps 
			stageCheckpoint.remove(removeCheckpoint);
			stageCheckpoint.put(removeCheckpoint, checkpointArray);
			checkpointStage.remove(checkpointId);
		}

	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		if(stageIdNames.get(stageId) == null){
			throw new IDNotRecognisedException("ID not attached to a stage");
		}
		else if(stagePreparation.get(stageId) == "waiting for results"){
			throw new InvalidStageStateException("Stage is waiting for results");
		}
		else{
			//Remove the stage from the hashmap and adds it back with "waiting for results"
			stagePreparation.remove(stageId);
			stagePreparation.put(stageId, "waiting for results");
		}
	}

	@Override
	public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {
		//Checks if a stageId is attached to a race
		if(stageIdNames.get(stageId) == null){
			throw new IDNotRecognisedException("ID not attached to a stage");
		}
		else{
			//Gets the list of the ids from the stageId
			ArrayList<Integer> idList = stageCheckpoint.get(stageId);
			//Bubblesort through the the Ids comparing their location
			for (int i = 0; i < idList.size(); i++) {
            			for (int j = i + 1; j < idList.size(); j++) {
							Checkpoint tempObj1 = (stageCheckpointIds.get(stageId)).get(i);
							Checkpoint tempObj2 = (stageCheckpointIds.get(stageId)).get(j);
							Double tempLocation1 = tempObj1.getLocation();
							Double tempLocation2 = tempObj2.getLocation();
                			if (tempLocation1 > tempLocation2) {
								var temp = idList.get(i);
								idList.set(i, idList.get(j));
								idList.set(j, temp);
                			}
           			 	}
       			 	}
					int[] newIdList = idList.stream().mapToInt(i -> i).toArray();
		return newIdList;
		}
		
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		int newNumTeams = numTeams;
		//Checks if the name of the race is already in the HashMap
		if (teamNameIds.get(name) != null){
			throw new IllegalNameException("Team name taken");
		}	
		//Checks if the name is in valid form
		else if (name == null || name == "" || name.length() > 30 || name.contains(" ")){
			throw new InvalidNameException("Team name invalid");
		}
		else{
			//Create a new class with the names ,id , and description, also adds to the HashMap
			ArrayList<Integer>riderList = new ArrayList<Integer>();
			Team team = new Team(name, description, numTeams, null);
			teamNameIds.put(name, numTeams);
			teamIdNames.put(numTeams, name);
			teamNames.put(name, team);
			teamRiders.put(numTeams, riderList);
			numTeams = numTeams + 1;
		}
		return newNumTeams;
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		if(teamIdNames.get(teamId) == null){
			throw new IDNotRecognisedException("ID not attached to a team");
		}
		else{
			//Removes the race based of the RaceId in all the HashMaps
			String teamName = teamIdNames.get(teamId);
			teamIdNames.remove(teamId);
			teamNameIds.remove(teamName);
			teamNames.remove(teamName);
			teamRiders.remove(teamId);
		}

	}
	@Override
	public int[] getTeams() {
		//Creates an array for the Ids
		int[] listTeamNames = new int[teamIdNames.size()];
		int j = 0;
		//Loops through the dictionary and adds the Ids to the array
		for (String i : teamNameIds.keySet()){
  			listTeamNames[j] = teamNameIds.get(i);
			j= j + 1;
		}
		//Returns the array
		return listTeamNames;
	}

	
	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		if(teamIdNames.get(teamId) == null){
			throw new IDNotRecognisedException("ID not attached to a team");
		}
		else{
		//Gets the list of the rider's IDs based off the team Id and returns it 
		//Get list containing all riders names
		ArrayList<Integer> riders = new ArrayList<Integer>();
		riders = teamRiders.get(teamId);
		int[] teamRider = new int[riders.size()];
		int j = 0;
		for(Integer i: riders){
			teamRider[j] = i;
			j++;
		}
		return teamRider;
		}
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		int newRiderId = numRiders; 
		if(teamIdNames.get(teamID) == null){
			throw new IDNotRecognisedException("ID not attached to a team");
		}
		else if(yearOfBirth < 1900){
			throw new IllegalArgumentException("Riders year of birth cannot be less than 1900");
		}
		else if(name == null || name == ""){
			throw new IllegalArgumentException("Rider name invalid");
		}
		else{
			//Creates new Rider object
			Rider rider = new Rider(yearOfBirth, name, teamID, numRiders);
			//Adds new rider object to all the HashMaps
			ArrayList<Integer> teamRiderList = new ArrayList<Integer>();
			teamRiderList = teamRiders.get(teamID);
			teamRiderList.add(numRiders);
			teamRiders.remove(teamID);
			teamRiders.put(teamID, teamRiderList);
			riderTeam.put(numRiders, teamID);
			riderIdObj.put(numRiders, rider);
			ridersStageResults.put(numRiders, null);
			numRiders++;			
		}				
		return newRiderId;
	}


	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		if(riderIdObj.get(riderId) == null){
			throw new IDNotRecognisedException("ID not attached to a rider");
		}
		else{
			//Gets the riders team id
			ArrayList<Integer> riders = new ArrayList<Integer>();
			int removeRider = riderTeam.get(riderId);
			riders = teamRiders.get(removeRider);
			//Removes the rider Ids from the array
			for (Integer j : riders){
				if(riders.get(j) == riderId){
				riders.set(j, -1);
					}
				}
			//Remove the checkpoint data from all the Hashmaps 
			teamRiders.remove(removeRider);
			teamRiders.put(removeRider, riders);
			riderTeam.remove(riderId);
			riderIdObj.remove(riderId);
		}

	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException,
			InvalidStageStateException {
		if(stageIdNames.get(stageId) == null){
			throw new IDNotRecognisedException("ID not attached to a stage");
		}
		else if(riderIdObj.get(riderId) == null){
			throw new IDNotRecognisedException("ID not attached to a rider");
		}
		else if(stagePreparation.get(stageId) == "Preparing"){
			throw new InvalidStageStateException("Stage is not prepared");
		}
		else if((stageRiders.get(stageId)).get(riderId) != null){
			throw new DuplicatedResultException("This rider already has a result in this stage");
		}
		else if(checkpoints.length > (stageNames.get(stageIdNames.get(stageId)).getNumOfCheckpoints() + 2)){
			throw new InvalidCheckpointTimesException("Too many checkpoints given");
		}
		else{
			//Put all relative pointers into hashMaps
			HashMap<Integer, LocalTime[]> riders = stageRiders.get(stageId);
			riders.put(riderId, checkpoints);
			stageRiders.put(stageId, riders);
			ridersStageResults.put(riderId, stageRiders);
			HashMap<Integer, HashMap<Integer, LocalTime[]>> stage = ridersStageResults.get(riderId);
			HashMap<Integer, LocalTime[]> riderInStage = stage.get(stageId);
			LocalTime[] resultsInStage = riderInStage.get(riderId);
			Integer finish = resultsInStage.length;
			LocalTime finishTime = resultsInStage[finish - 1];
			LocalTime startTime = ((stageNames.get(stageIdNames.get(stageId))).getStartTime()).toLocalTime();
			//Calculate elapsed time
			int elapseTimeMillis = (int) startTime.until(finishTime, ChronoUnit.MILLIS);
			int elapseTimeHours= elapseTimeMillis/(1000*60*24);
			int elapseTimeMins = (elapseTimeMillis%(1000*60*24))/(1000*60);
			int elapseTimeSecs = ((elapseTimeMillis%(1000*60*24))%(1000*60))/1000;
			int actElapseTimeMillis = (((elapseTimeMillis%(1000*60*24))%(1000*60))%1000)*1000000;
			//Add elapsed time to the HashMap
			LocalTime elapseTime = LocalTime.of(elapseTimeHours, elapseTimeMins, elapseTimeSecs, actElapseTimeMillis);
			HashMap<Integer, LocalTime> riderEl = stageRiderElapsed.get(stageId);
			riderEl.put(riderId, elapseTime);
		
		}

	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		if(stageIdNames.get(stageId) == null){
			throw new IDNotRecognisedException("ID not attached to a stage");
		}
		else if(riderIdObj.get(riderId) == null){
			throw new IDNotRecognisedException("ID not attached to a rider");
		}
		else if((stageRiders.get(stageId)).get(riderId) == null){
			LocalTime[] empty = new LocalTime[0];
			return empty;
		}
		else{
			//Get stage, riders, and results from HashMaps
			HashMap<Integer, HashMap<Integer, LocalTime[]>> stage = ridersStageResults.get(riderId);
			HashMap<Integer, LocalTime[]> riderInStage = stage.get(stageId);
			LocalTime[] resultsInStage = riderInStage.get(riderId);
			Integer finish = resultsInStage.length;
			LocalTime finishTime = resultsInStage[finish-1];
			LocalTime startTime = ((stageNames.get(stageIdNames.get(stageId))).getStartTime()).toLocalTime();
			//Calculate the elapse time 
			int elapseTimeMillis = (int) startTime.until(finishTime, ChronoUnit.MILLIS);
			int elapseTimeHours= elapseTimeMillis/(1000*60*24);
			int elapseTimeMins = (elapseTimeMillis%(1000*60*24))/(1000*60);
			int elapseTimeSecs = ((elapseTimeMillis%(1000*60*24))%(1000*60))/1000;
			int actElapseTimeMillis = (((elapseTimeMillis%(1000*60*24))%(1000*60))%1000)*1000000;
			LocalTime elapseTime = LocalTime.of(elapseTimeHours, elapseTimeMins, elapseTimeSecs, actElapseTimeMillis);
			LocalTime[] riderResults = new LocalTime[resultsInStage.length - 1];
			//Put the riders results into a list
			int j = 0;
			for (int i = 1; i + 1 < resultsInStage.length + 1; i++){
				 riderResults[j] = resultsInStage[i];
				 j++;
			}
			riderResults[riderResults.length - 1] = elapseTime;
			return riderResults;
		}
		
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		if(stageIdNames.get(stageId) == null){
			throw new IDNotRecognisedException("ID not attached to a stage");
		}
		else if(riderIdObj.get(riderId) == null){
			throw new IDNotRecognisedException("ID not attached to a rider");
		}
		else if((stageRiders.get(stageId)).get(riderId) == null){
			return null;
		}
		else{
			HashMap<Integer, LocalTime> ridersEl = stageRiderElapsed.get(stageId);
			int[] riders = new int[ridersEl.size()];
			LocalTime[] times = new LocalTime[ridersEl.size()];
			int j = 0;
			//Get list of rider's Ids and times
			for (Integer i : ridersEl.keySet()) {
				riders[j] = i;
				j++;
			  }
			int k = 0;  
			for (LocalTime l : ridersEl.values()){
				times[k] = l;
				k++;
			}
			//Bubblesort both lists
			for (int p = 0; p < times.length; p++) {
				for (int o = p + 1; o < times.length; o++) {
					LocalTime time1 = times[p];
					LocalTime time2 = times[o];
					int value = time1.compareTo(time2); 
					if (value > 0) {
						var temp = times[p];
						times[p] = times[o];
						times[o] = temp;
						var temp2 = riders[p];
						riders[p] = riders[o];
						riders[o] = temp2;
						}
				}
			}
			//Check the difference between the two times and put in a hashmap if they are meant to change with the correct times
			LocalTime pointer = times[0];
			for(int u = 1; u < times.length; u++){
				if((times[u - 1]).until(times[u], ChronoUnit.MILLIS) < 1000){
					adjustedElapse.put(times[u], pointer);
				}
				else{
					pointer = times[u];
				}
			}
			//Go through both the lists and the hashmap changing the times and adding the ids and the elapsed times to the new hashmap
			for(int y = 0; y < times.length ; y++){
				if(adjustedElapse.get((times[y])) == null){
					idElapse.put(riders[y], times[y]);
				}
				else{
					idElapse.put(riders[y], adjustedElapse.get(times[y]));
				}
			}
		}
		LocalTime ridersElapsed = idElapse.get(riderId);
		return ridersElapsed;
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		if(stageIdNames.get(stageId) == null){
			throw new IDNotRecognisedException("ID not attached to a stage");
		}
		else if(riderIdObj.get(riderId) == null){
			throw new IDNotRecognisedException("ID not attached to a rider");
		}
		else{
			//Remove all pointers for the riders
			HashMap<Integer, HashMap<Integer, LocalTime[]>> stage = ridersStageResults.get(riderId);
			HashMap<Integer, LocalTime[]> riderInStage = stage.get(stageId);
			riderInStage.remove(riderId);
			ridersStageResults.remove(riderId);
		}
	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		HashMap<Integer, LocalTime> ridersEl = stageRiderElapsed.get(stageId);
		int[] riders = new int[ridersEl.size()];
		if(stageIdNames.get(stageId) == null){
			throw new IDNotRecognisedException("ID not attached to a stage");
		}
		else{
			LocalTime[] times = new LocalTime[ridersEl.size()];
			int j = 0;
			//Get list of rider's Ids and times
			for (Integer i : ridersEl.keySet()) {
				riders[j] = i;
				j++;
			  }
			int k = 0;  
			for (LocalTime l : ridersEl.values()){
				times[k] = l;
				k++;
			}
			//Bubblesort both lists
			for (int p = 0; p < times.length; p++) {
				for (int o = p + 1; o < times.length; o++) {
					LocalTime time1 = times[p];
					LocalTime time2 = times[o];
					int value = time1.compareTo(time2); 
					if (value > 0) {
						var temp = times[p];
						times[p] = times[o];
						times[o] = temp;
						var temp2 = riders[p];
						riders[p] = riders[o];
						riders[o] = temp2;
						}
				}
			}
		}
		return riders;
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		HashMap<Integer, LocalTime> ridersEl = stageRiderElapsed.get(stageId);
		LocalTime[] times = new LocalTime[ridersEl.size()];
		LocalTime[] newTimes = new LocalTime[ridersEl.size()];
		if(stageIdNames.get(stageId) == null){
			throw new IDNotRecognisedException("ID not attached to a stage");
		}
		else if((stageRiders.get(stageId)).size() == 0){
			LocalTime[] empty = new LocalTime[0];
			return empty;
		}
		else{
			//Get list of rider's times
			int k = 0;  
			for (LocalTime l : ridersEl.values()){
				times[k] = l;
				k++;
			}
			//Bubblesort the list
			for (int p = 0; p < times.length; p++) {
				for (int o = p + 1; o < times.length; o++) {
					LocalTime time1 = times[p];
					LocalTime time2 = times[o];
					int value = time1.compareTo(time2); 
					if (value > 0) {
						var temp = times[p];
						times[p] = times[o];
						times[o] = temp;
						}
				}
			}
			//Check the difference between the two times and put in a hashmap if they are meant to change with the correct times
			LocalTime pointer = times[0];
			for(int u = 1; u < times.length; u++){
				if((times[u - 1]).until(times[u], ChronoUnit.MILLIS) < 1000){
					adjustedElapse.put(times[u], pointer);
				}
				else{
					pointer = times[u];
				}
			}
			//Go through both the lists and the hashmap changing the times and adding the elapsed times to the new list
			for(int y = 0; y < times.length ; y++){
				if(adjustedElapse.get((times[y])) == null){
					newTimes[y] =  times[y];
				}
				else{
					newTimes[y] = adjustedElapse.get(times[y]);
				}
			}
		}
		return newTimes;
	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		//Get the riders elapse times in the stage from the HashMaps 
		HashMap<Integer, LocalTime[]> ridersInStage = stageRiders.get(stageId);
		HashMap<Integer, LocalTime> ridersEl = stageRiderElapsed.get(stageId);
		LocalTime[] times = new LocalTime[ridersEl.size()];
		LocalTime[] newTimes = new LocalTime[ridersEl.size()];
		int[] riders = new int[ridersEl.size()];
		int[] ridersPoints = new int[ridersEl.size()];
		if(stageIdNames.get(stageId) == null){
			throw new IDNotRecognisedException("ID not attached to a stage");
		}
		else if((stageRiders.get(stageId)).size() == 0){
			int[] empty = new int[0];
			return empty;
		}
		else{
			int j = 0;
			//Get list of rider's Ids and times
			for (Integer i : ridersEl.keySet()) {
				riders[j] = i;
				j++;
			  }
			int k = 0;  
			for (LocalTime l : ridersEl.values()){
				times[k] = l;
				k++;
			}
			//Bubblesort both lists
			for (int p = 0; p < times.length; p++) {
				for (int o = p + 1; o < times.length; o++) {
					LocalTime time1 = times[p];
					LocalTime time2 = times[o];
					int value = time1.compareTo(time2); 
					if (value > 0) {
						var temp = times[p];
						times[p] = times[o];
						times[o] = temp;
						var temp2 = riders[p];
						riders[p] = riders[o];
						riders[o] = temp2;
						}
				}
			}
			//Check the difference between the two times and put in a hashmap if they are meant to change with the correct times
			LocalTime pointer = times[0];
			for(int u = 1; u < times.length; u++){
				if((times[u - 1]).until(times[u], ChronoUnit.MILLIS) < 1000){
					adjustedElapse.put(times[u], pointer);
				}
				else{
					pointer = times[u];
				}
			}
			//Go through both the lists and the hashmap changing the times and adding the elapsed times to the new list
			for(int y = 0; y < times.length ; y++){
				if(adjustedElapse.get((times[y])) == null){
					newTimes[y] =  times[y];
				}
				else{
					newTimes[y] = adjustedElapse.get(times[y]);
				}
			}
		}
		//Check the stage type if its a time trial and add the points accordingly
		if (stageNames.get(stageIdNames.get(stageId)).getType() == StageType.TT){
			for(int t = 0; t < times.length ; t++){
				ridersPoints[t] = highPoints[t];
			}
		}
		LocalTime rankPointer = newTimes[0];
		int point = 0;
		//Check the stage type if its a flat stage and add the points accordingly
		if (stageNames.get(stageIdNames.get(stageId)).getType() == StageType.FLAT){
			for(int r = 0; r < newTimes.length ; r++){
				if(newTimes[r] == rankPointer){
					ridersPoints[r] = flatPoints[point];
				}
				else{
					point = point + 1;
					rankPointer = newTimes[r];
					ridersPoints[r] = flatPoints[point];
				}
			}
		}
		//Check the stage type if its a medium mountain stage and add the points accordingly
		else if (stageNames.get(stageIdNames.get(stageId)).getType() == StageType.MEDIUM_MOUNTAIN){
			for(int r = 0; r < newTimes.length ; r++){
				if(newTimes[r] == rankPointer){
					ridersPoints[r] = hillyPoints[point];
				}
				else{
					point = point + 1;
					rankPointer = newTimes[r];
					ridersPoints[r] = hillyPoints[point];
				}
			}
		}
		//Add the points accordingly for the stage
		else{
			for(int r = 0; r < newTimes.length ; r++){
				if(newTimes[r] == rankPointer){
					ridersPoints[r] = highPoints[point];
				}
				else{
					point = point + 1;
					rankPointer = newTimes[r];
					ridersPoints[r] = highPoints[point];
				}
			}
		}
		//Goes through each of the stage Checkpoints to check if they are a sprint checkpoint
		for(int i : stageCheckpoint.get(stageId)){
			ArrayList<Integer> ridersCheckpointResults = new ArrayList<Integer>();
			if (((stageCheckpointIds.get(stageId).get(i)).getCheckpointType()) == CheckpointType.SPRINT){
				int[] riderCheckpointList = new int[ridersInStage.size()];
				int m = 0; 
				//Gets a list of the times
				for(Integer j: ridersInStage.keySet()){
					LocalTime[] timeList = ridersInStage.get(j);
					LocalTime time1 = timeList[i+1];
					LocalTime time2 = timeList[i];
					int checkTime = (int) time2.until(time1, ChronoUnit.MILLIS);
					ridersCheckpointResults.add(checkTime);
					riderCheckpointList[m] = j;
					m = m + 1;
				}
				for (int ii = 0; ii < ridersCheckpointResults.size(); ii++) {
					for (int jj = ii + 1; jj < ridersCheckpointResults.size(); jj++) {
						//Compares the two list of times together and stores them accordingly
						Integer time1 = ridersCheckpointResults.get(ii);
						Integer time2 = ridersCheckpointResults.get(jj);
						int value = time1.compareTo(time2); 
						if (value > 0) {
							var temp = ridersCheckpointResults.get(ii);
							ridersCheckpointResults.set(ii, ridersCheckpointResults.get(jj));
							ridersCheckpointResults.set(jj, temp);
							var temp2 = riderCheckpointList[ii];
							riderCheckpointList[ii] = riderCheckpointList[jj];
							riderCheckpointList[jj] = temp2;
						}
					}
				}
				//Adds the Sprint checkpoint points
				for(int iii = 0; iii < riderCheckpointList.length ; iii++){
					for(int jjj = 0; jjj < riderCheckpointList.length ; jjj++){
						if (riderCheckpointList[iii] == riders[jjj]){
							if(iii < highPoints.length - 1){
								ridersPoints[jjj] = ridersPoints[jjj] + highPoints[iii];
							}
						}
					}
				}
			}
		}
		return ridersPoints;
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		HashMap<Integer, LocalTime[]> ridersInStage = stageRiders.get(stageId);
		HashMap<Integer, LocalTime> ridersEl = stageRiderElapsed.get(stageId);
		LocalTime[] times = new LocalTime[ridersEl.size()];
		LocalTime[] newTimes = new LocalTime[ridersEl.size()];
		int[] riders = new int[ridersEl.size()];
		int[] ridersPoints = new int[ridersEl.size()];
		if(stageIdNames.get(stageId) == null){
			throw new IDNotRecognisedException("ID not attached to a stage");
		}
		else if((stageRiders.get(stageId)).size() == 0){
			int[] empty = new int[0];
			return empty;
		}
		else{
			int j = 0;
			//Get list of rider's Ids and times
			for (Integer i : ridersEl.keySet()) {
				riders[j] = i;
				j++;
			  }
			int k = 0;  
			for (LocalTime l : ridersEl.values()){
				times[k] = l;
				k++;
			}
			//Bubblesort both lists
			for (int p = 0; p < times.length; p++) {
				for (int o = p + 1; o < times.length; o++) {
					LocalTime time1 = times[p];
					LocalTime time2 = times[o];
					int value = time1.compareTo(time2); 
					if (value > 0) {
						var temp = times[p];
						times[p] = times[o];
						times[o] = temp;
						var temp2 = riders[p];
						riders[p] = riders[o];
						riders[o] = temp2;
						}
				}
			}
			//Check the difference between the two times and put in a hashmap if they are meant to change with the correct times
			LocalTime pointer = times[0];
			for(int u = 1; u < times.length; u++){
				if((times[u - 1]).until(times[u], ChronoUnit.MILLIS) < 1000){
					adjustedElapse.put(times[u], pointer);
				}
				else{
					pointer = times[u];
				}
			}
			//Go through both the lists and the hashmap changing the times and adding the elapsed times to the new list
			for(int y = 0; y < times.length ; y++){
				if(adjustedElapse.get((times[y])) == null){
					newTimes[y] =  times[y];
				}
				else{
					newTimes[y] = adjustedElapse.get(times[y]);
				}
			}
		}
		//Goes through the checkpoints to check them for what type they are 
		for(int i : stageCheckpoint.get(stageId)){
			ArrayList<Integer> ridersCheckpointResults = new ArrayList<Integer>();
			//Checks if the checkpoint is a C4
			if (((stageCheckpointIds.get(stageId).get(i)).getCheckpointType()) == CheckpointType.C4){
				int[] riderCheckpointList = new int[ridersInStage.size()];
				int m = 0; 
				//Gets a list of the times
				for(Integer j: ridersInStage.keySet()){
					LocalTime[] timeList = ridersInStage.get(j);
					LocalTime time1 = timeList[i+1];
					LocalTime time2 = timeList[i];
					int checkTime = (int) time2.until(time1, ChronoUnit.MILLIS);
					ridersCheckpointResults.add(checkTime);
					riderCheckpointList[m] = j;
					m = m + 1;
				}
				//Compares and sorts the lists
				for (int ii = 0; ii < ridersCheckpointResults.size(); ii++) {
					for (int jj = ii + 1; jj < ridersCheckpointResults.size(); jj++) {
						Integer time1 = ridersCheckpointResults.get(ii);
						Integer time2 = ridersCheckpointResults.get(jj);
						int value = time1.compareTo(time2); 
						if (value > 0) {
							var temp = ridersCheckpointResults.get(ii);
							ridersCheckpointResults.set(ii, ridersCheckpointResults.get(jj));
							ridersCheckpointResults.set(jj, temp);
							var temp2 = riderCheckpointList[ii];
							riderCheckpointList[ii] = riderCheckpointList[jj];
							riderCheckpointList[jj] = temp2;
						}
					}
				}
				//Adds the points 
				for(int iii = 0; iii < riderCheckpointList.length ; iii++){
					for(int jjj = 0; jjj < riderCheckpointList.length ; jjj++){
						if (riderCheckpointList[iii] == riders[jjj]){
							if(iii < c4Points.length - 1){
								ridersPoints[jjj] = ridersPoints[jjj] + c4Points[iii];
							}
						}
					}
				}
			}
			//Checks if the checkpoint is a C3
			else if (((stageCheckpointIds.get(stageId).get(i)).getCheckpointType()) == CheckpointType.C3){
				int[] riderCheckpointList = new int[ridersInStage.size()];
				int m = 0; 
				//Creates the two lists
				for(Integer j: ridersInStage.keySet()){
					LocalTime[] timeList = ridersInStage.get(j);
					LocalTime time1 = timeList[i+1];
					LocalTime time2 = timeList[i];
					int checkTime = (int) time2.until(time1, ChronoUnit.MILLIS);
					ridersCheckpointResults.add(checkTime);
					riderCheckpointList[m] = j;
					m = m + 1;
				}
				//Compares the list and sorts them
				for (int ii = 0; ii < ridersCheckpointResults.size(); ii++) {
					for (int jj = ii + 1; jj < ridersCheckpointResults.size(); jj++) {
						Integer time1 = ridersCheckpointResults.get(ii);
						Integer time2 = ridersCheckpointResults.get(jj);
						int value = time1.compareTo(time2); 
						if (value > 0) {
							var temp = ridersCheckpointResults.get(ii);
							ridersCheckpointResults.set(ii, ridersCheckpointResults.get(jj));
							ridersCheckpointResults.set(jj, temp);
							var temp2 = riderCheckpointList[ii];
							riderCheckpointList[ii] = riderCheckpointList[jj];
							riderCheckpointList[jj] = temp2;
						}
					}
				}
				//Adds the points
				for(int iii = 0; iii < riderCheckpointList.length ; iii++){
					for(int jjj = 0; jjj < riderCheckpointList.length ; jjj++){
						if (riderCheckpointList[iii] == riders[jjj]){
							if(iii < c3Points.length - 1){
								ridersPoints[jjj] = ridersPoints[jjj] + c3Points[iii];
							}
						}
					}
				}
			}
			//Checks if the checkpoint is a C2
			else if (((stageCheckpointIds.get(stageId).get(i)).getCheckpointType()) == CheckpointType.C2){
				int[] riderCheckpointList = new int[ridersInStage.size()];
				int m = 0; 
				//Gets the list of times
				for(Integer j: ridersInStage.keySet()){
					LocalTime[] timeList = ridersInStage.get(j);
					LocalTime time1 = timeList[i+1];
					LocalTime time2 = timeList[i];
					int checkTime = (int) time2.until(time1, ChronoUnit.MILLIS);
					ridersCheckpointResults.add(checkTime);
					riderCheckpointList[m] = j;
					m = m + 1;
				}
				//Compares the lists and sorts them
				for (int ii = 0; ii < ridersCheckpointResults.size(); ii++) {
					for (int jj = ii + 1; jj < ridersCheckpointResults.size(); jj++) {
						Integer time1 = ridersCheckpointResults.get(ii);
						Integer time2 = ridersCheckpointResults.get(jj);
						int value = time1.compareTo(time2); 
						if (value > 0) {
							var temp = ridersCheckpointResults.get(ii);
							ridersCheckpointResults.set(ii, ridersCheckpointResults.get(jj));
							ridersCheckpointResults.set(jj, temp);
							var temp2 = riderCheckpointList[ii];
							riderCheckpointList[ii] = riderCheckpointList[jj];
							riderCheckpointList[jj] = temp2;
						}
					}
				}
				//Adds the points
				for(int iii = 0; iii < riderCheckpointList.length ; iii++){
					for(int jjj = 0; jjj < riderCheckpointList.length ; jjj++){
						if (riderCheckpointList[iii] == riders[jjj]){
							if(iii < c2Points.length - 1){
								ridersPoints[jjj] = ridersPoints[jjj] + c2Points[iii];
							}
						}
					}
				}
			}
			//Checks if the checkpoints is a C1
			else if (((stageCheckpointIds.get(stageId).get(i)).getCheckpointType()) == CheckpointType.C1){
				int[] riderCheckpointList = new int[ridersInStage.size()];
				int m = 0; 
				//Gets the list of times
				for(Integer j: ridersInStage.keySet()){
					LocalTime[] timeList = ridersInStage.get(j);
					LocalTime time1 = timeList[i+1];
					LocalTime time2 = timeList[i];
					int checkTime = (int) time2.until(time1, ChronoUnit.MILLIS);
					ridersCheckpointResults.add(checkTime);
					riderCheckpointList[m] = j;
					m = m + 1;
				}
				//Compares the list and sorts them
				for (int ii = 0; ii < ridersCheckpointResults.size(); ii++) {
					for (int jj = ii + 1; jj < ridersCheckpointResults.size(); jj++) {
						Integer time1 = ridersCheckpointResults.get(ii);
						Integer time2 = ridersCheckpointResults.get(jj);
						int value = time1.compareTo(time2); 
						if (value > 0) {
							var temp = ridersCheckpointResults.get(ii);
							ridersCheckpointResults.set(ii, ridersCheckpointResults.get(jj));
							ridersCheckpointResults.set(jj, temp);
							var temp2 = riderCheckpointList[ii];
							riderCheckpointList[ii] = riderCheckpointList[jj];
							riderCheckpointList[jj] = temp2;
						}
					}
				}
				//Adds the points
				for(int iii = 0; iii < riderCheckpointList.length ; iii++){
					for(int jjj = 0; jjj < riderCheckpointList.length ; jjj++){
						if (riderCheckpointList[iii] == riders[jjj]){
							if(iii < c1Points.length - 1){
								ridersPoints[jjj] = ridersPoints[jjj] + c1Points[iii];
							}
						}
					}
				}
			}
			//Checks if the checkpoint is a HC
			else if (((stageCheckpointIds.get(stageId).get(i)).getCheckpointType()) == CheckpointType.HC){
				int[] riderCheckpointList = new int[ridersInStage.size()];
				int m = 0; 
				//Gets the list of times
				for(Integer j: ridersInStage.keySet()){
					LocalTime[] timeList = ridersInStage.get(j);
					LocalTime time1 = timeList[i+1];
					LocalTime time2 = timeList[i];
					int checkTime = (int) time2.until(time1, ChronoUnit.MILLIS);
					ridersCheckpointResults.add(checkTime);
					riderCheckpointList[m] = j;
					m = m + 1;
				}
				//Compares the list and sorts them
				for (int ii = 0; ii < ridersCheckpointResults.size(); ii++) {
					for (int jj = ii + 1; jj < ridersCheckpointResults.size(); jj++) {
						Integer time1 = ridersCheckpointResults.get(ii);
						Integer time2 = ridersCheckpointResults.get(jj);
						int value = time1.compareTo(time2); 
						if (value > 0) {
							var temp = ridersCheckpointResults.get(ii);
							ridersCheckpointResults.set(ii, ridersCheckpointResults.get(jj));
							ridersCheckpointResults.set(jj, temp);
							var temp2 = riderCheckpointList[ii];
							riderCheckpointList[ii] = riderCheckpointList[jj];
							riderCheckpointList[jj] = temp2;
						}
					}
				}
				//Adds the points
				for(int iii = 0; iii < riderCheckpointList.length ; iii++){
					for(int jjj = 0; jjj < riderCheckpointList.length ; jjj++){
						if (riderCheckpointList[iii] == riders[jjj]){
							if(iii < hcPoints.length - 1){
								ridersPoints[jjj] = ridersPoints[jjj] + hcPoints[iii];
							}
						}
					}
				}
			}
		}
		
		return ridersPoints;
	}

	@Override
	public void eraseCyclingPortal() {
		//Removes all the pointers and clears every hashmap
		numRaces = 0;
		numRiders = 0;
		numStages = 0;
		numTeams = 0;
		raceNameIds.clear();
		raceIdNames.clear();
		raceNames.clear();
		raceStages.clear();
		stageRaces.clear();
		stageNameIds.clear();
		stageIdNames.clear();
		stageNames.clear();
		riderIdObj.clear();
		teamRiders.clear();
		riderTeam.clear();
		teamNameIds.clear();
		teamIdNames.clear();
		teamNames.clear();
		stageCheckpoint.clear();
		checkpointStage.clear();
		stagePreparation.clear();
		stageRiders.clear();
		ridersStageResults.clear();
		stageRiderElapsed.clear();
		idElapse.clear();
		adjustedElapse.clear();

	}


	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		try { //Non functioning attemtp to make a copy of the current state of the CyclingPortalImpl that is running
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
			transient private CyclingPortalImpl selfCopy = new CyclingPortalImpl;
			selfCopy = this;
			out.writeObject(selfCopy);
		out.close();
		} catch (Exception e) {
		// TODO: handle exception
		}
	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		//Non functioning attempt to take a state of a CyclingPortalImpl and replace this running versions values with those of the loaded state
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
			CyclingPortalImpl newObj = new CyclingPortalImpl();
			newObj = in.readObject(filename);
			
			this.numRaces = newObj.numRaces;
			this.numRiders = newObj.numRiders;
			this.numStages = newObj.numStages;
			this.numTeams = newObj.numTeams;
			this.raceNameIds = newObj.raceNameIds;
			this.raceIdNames = newObj.raceIdNames;
			this.raceNames = newObj.raceNames;
			this.raceStages = newObj.raceStages;
			this.stageRaces = newObj.stageRaces;
			this.stageNameIds = newObj.stageNameIds;
			this.stageIdNames = newObj.stageIdNames;
			this.stageNames = newObj.stageNames;
			this.riderIdObj = newObj.riderIdObj;
			this.teamRiders = newObj.teamRiders;
			this.riderTeam = newObj.riderTeam;
			this.teamNameIds = newObj.teamNameIds;
			this.teamIdNames = newObj.teamIdNames;
			this.teamNames = newObj.teamNames;
			this.stageCheckpoint = newObj.stageCheckpoint;
			this.checkpointStage = newObj.checkpointStage;
			this.stagePreparation = newObj.stagePreparation;
			this.stageRiders = newObj.stageRiders;
			this.ridersStageResults = newObj.ridersStageResults;
			this.stageRiderElapsed = newObj.stageRiderElapsed;
			this.idElapse = newObj.idElapse;
			this.adjustedElapse = newObj.adjustedElapse;
	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		if(raceNameIds.get(name) == null){
			throw new NameNotRecognisedException("Name not attached to a race");
		}
		else{
			//Gets raceId using raceName
			String raceName = name;
			int raceId = raceNameIds.get(raceName);
			//Removes all pointers to/from the race
			raceIdNames.remove(raceId);
			raceNameIds.remove(raceName);
			raceNames.remove(raceName);
			//Removes all the stages pointers to the race
			ArrayList<Integer> thisStages = raceStages.get(raceId);
			for(int i = 0; i < thisStages.size(); i++){
				stageRaces.remove(i);
			}
			raceStages.remove(raceId);
		}
		

	}

	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		if(raceIdNames.get(raceId) == null){
			throw new IDNotRecognisedException("ID not attached to a race");
		}
		else{
			//Gets the stages in the race
			ArrayList<Integer> theseStages = raceStages.get(raceId);
			//Creates an empty hashmap used to connect and add up all the riders times stored with their IDs
			HashMap<Integer, LocalTime> ridersTotalTime = new HashMap<Integer, LocalTime>();
			//Runs for every stage in the race
			for(int i = 0; i < theseStages.size(); i++){
				int currentStageId = theseStages.get(i);
				//If a sent value is empty, return a blank list. Repeated in later 2 methods for their respective data sets
				if((getRankedAdjustedElapsedTimesInStage(currentStageId)).length == 0){
					LocalTime[] blankReturn = new LocalTime[0];
					return blankReturn;
				}
				else{
					HashMap<Integer, LocalTime> currentHashMap = stageRiderElapsed.get(currentStageId);
					//If the rider is not already in the hashmap, put them in it with the first time you find
					for(int j: currentHashMap.keySet()){
						if(ridersTotalTime.get(j) == null){
							ridersTotalTime.put(j, currentHashMap.get(j));
						}
						//Otherwise add their time to the current stored time
						else{
							LocalTime totalTime = ridersTotalTime.get(j);
							LocalTime time2 = currentHashMap.get(j);
							totalTime = totalTime.plusHours(time2.getHour());
							totalTime = totalTime.plusMinutes(time2.getMinute());
							totalTime = totalTime.plusSeconds(time2.getSecond());
							totalTime = totalTime.plusNanos(time2.getNano());
							ridersTotalTime.put(j, totalTime);
						}
					}
				}
			}
				//Create a list of all the rider times
				LocalTime[] listRiderTimes = new LocalTime[ridersTotalTime.size()];
				int r = 0;
				for (int s : ridersTotalTime.keySet()){
					listRiderTimes[r] = ridersTotalTime.get(s);
					r++;
				}
				//Sort the list with a bubble sort
				for(int u = 0; u < listRiderTimes.length; u++){
					for(int v = u + 1; v < listRiderTimes.length; v++){
						LocalTime time1 = listRiderTimes[u];
						LocalTime time2 = listRiderTimes[v];
						int value = time1.compareTo(time2); 
						if (value > 0) {
							var temp1 = listRiderTimes[u];
							listRiderTimes[u] = listRiderTimes[v];
							listRiderTimes[v] = temp1;
						
						}
					}
				}
			return listRiderTimes;
			}
			
		}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		if(raceIdNames.get(raceId) == null){
			throw new IDNotRecognisedException("ID not attached to a race");
		}		
		else{
			//Get all stages in the race
			ArrayList<Integer> theseStages = raceStages.get(raceId);
			//Empty hashmap to store the points and connect them to riders ID
			HashMap<Integer, Integer> riderIdPoints  = new HashMap<Integer, Integer>();
			//For each stage in the race
			for(int i = 0; i < theseStages.size(); i++){
				int currentStageId = theseStages.get(i);
				if((getRidersPointsInStage(currentStageId)).length == 0){
					int[] blankReturn = new int[0];
					return blankReturn;
				}
				//Get a list of all the riders and a list of all their points in the stage (both are sorted to match)
				int[] currentStageRiders = getRidersRankInStage(theseStages.get(i));
				int[] currentStagePoints = getRidersPointsInStage(theseStages.get(i));
				for(int j = 0; j < currentStageRiders.length ; j++){
					//Same as before, if its already in add to the point total, otherwise put it in the hashmap
					int thisRider = currentStageRiders[j];
					int thisPoints = currentStagePoints[j];
					if(riderIdPoints.get(thisRider) == null){
						riderIdPoints.put(thisRider, thisPoints);
					}
					else{
						int totalPoints = riderIdPoints.get(thisRider);
						totalPoints = totalPoints + thisPoints;
						riderIdPoints.remove(thisRider);
						riderIdPoints.put(thisRider, totalPoints);
					}
				}
			}
			int[] listRiderPoints = new int[riderIdPoints.size()];
			int r = 0;
			for (int s : riderIdPoints.keySet()){
				listRiderPoints[r] = riderIdPoints.get(s);
				r++;
			}
			//Bubble sort the list of points
			for(int u = 0; u < listRiderPoints.length; u++){
				for(int v = u + 1; v < listRiderPoints.length; v++){
					if (listRiderPoints[u] < listRiderPoints[v]) {
						var temp1 = listRiderPoints[u];
						listRiderPoints[u] = listRiderPoints[v];
						listRiderPoints[v] = temp1;
					}
				}
			} return listRiderPoints;
		}
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		//Basically identical to getRidersPointsInRace just using getRidersMountainPointsInStage instead of getRidersPointsInStage
		if(raceIdNames.get(raceId) == null){
			throw new IDNotRecognisedException("ID not attached to a race");
		}
		else{
			ArrayList<Integer> theseStages = raceStages.get(raceId);
			HashMap<Integer, Integer> riderIdMountainPoints  = new HashMap<Integer, Integer>();
			for(int i = 0; i < theseStages.size(); i++){
				int currentStageId = theseStages.get(i);
				if((getRidersMountainPointsInStage(currentStageId)).length == 0){
					int[] blankReturn = new int[0];
					return blankReturn;
				}
				int[] currentStageRiders = getRidersRankInStage(theseStages.get(i));
				int[] currentStageMountainPoints = getRidersMountainPointsInStage(theseStages.get(i));
				for(int j = 0; j < currentStageRiders.length; j++){
					int thisRider = currentStageRiders[j];
					int thisPoints = currentStageMountainPoints[j];
					System.out.println(Arrays.toString(currentStageMountainPoints));
					if(riderIdMountainPoints.get(thisRider) == null){
						riderIdMountainPoints.put(thisRider, thisPoints);
					}
					else{
						int totalMountainPoints = riderIdMountainPoints.get(thisRider);
						totalMountainPoints = totalMountainPoints + thisPoints;
						riderIdMountainPoints.remove(thisRider);
						riderIdMountainPoints.put(thisRider, totalMountainPoints);
					}
				}
			}
			int[] listRiderMountainPoints = new int[riderIdMountainPoints.size()];
			int r = 0;
			for (int s : riderIdMountainPoints.keySet()){
				listRiderMountainPoints[r] = riderIdMountainPoints.get(s);
				r++;
			}
			for(int u = 0; u < listRiderMountainPoints.length; u++){
				for(int v = u + 1; v < listRiderMountainPoints.length; v++){
					if (listRiderMountainPoints[u] < listRiderMountainPoints[v]) {
						var temp1 = listRiderMountainPoints[u];
						listRiderMountainPoints[u] = listRiderMountainPoints[v];
						listRiderMountainPoints[v] = temp1;

					}
				}
			}return listRiderMountainPoints;
		} 
	}


	
	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		//Mostly the same as getRidersGeneralClassificationTimesInRace up until the creation of listRiderIds
		if(raceIdNames.get(raceId) == null){
			throw new IDNotRecognisedException("ID not attached to a race");
		}
		else{
			ArrayList<Integer> theseStages = raceStages.get(raceId);
			HashMap<Integer, LocalTime> ridersTotalTime = new HashMap<Integer, LocalTime>();
			for(int i = 0; i < theseStages.size(); i++){
				int currentStageId = theseStages.get(i);
				HashMap<Integer, LocalTime> currentHashMap = stageRiderElapsed.get(currentStageId);
				for(int j: currentHashMap.keySet()){
					if(ridersTotalTime.get(j) == null){
						ridersTotalTime.put(j, currentHashMap.get(j));
					}
					else{
						LocalTime totalTime = ridersTotalTime.get(j);
						LocalTime time2 = currentHashMap.get(j);
						totalTime = totalTime.plusHours(time2.getHour());
						totalTime = totalTime.plusMinutes(time2.getMinute());
						totalTime = totalTime.plusSeconds(time2.getSecond());
						totalTime = totalTime.plusNanos(time2.getNano());
						ridersTotalTime.remove(j);
						ridersTotalTime.put(j, totalTime);
					}
				}
				}
			
				//Create lists of rider IDs and rider times where indexes connect the rider's to their times
				int[] listRiderIds = new int[ridersTotalTime.size()];
				int p = 0;
				for (int q : ridersTotalTime.keySet()){
  					listRiderIds[p] = q;
					p++;
				}
				LocalTime[] listRiderTimes = new LocalTime[ridersTotalTime.size()];
				int r = 0;
				for (int s : ridersTotalTime.keySet()){
					listRiderTimes[r] = ridersTotalTime.get(s);
					r++;
				}
				//Perform a bubble sort on listRiderTimes and move listRiderIds to match movements in listRiderTimes
				for(int u = 0; u < listRiderTimes.length; u++){
					for(int v = u + 1; v < listRiderTimes.length; v++){
						LocalTime time1 = listRiderTimes[u];
						LocalTime time2 = listRiderTimes[v];
						int value = time1.compareTo(time2); 
						if (value > 0) {
							var temp1 = listRiderTimes[u];
							listRiderTimes[u] = listRiderTimes[v];
							listRiderTimes[v] = temp1;
							var temp2 = listRiderIds[u];
							listRiderIds[u] = listRiderIds[v];
							listRiderIds[v] = temp2;
						}
					}
				}
				//Return a list of the IDs sorted by elapsed time in race
				return listRiderIds;
		}
			
	}
	
	

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
		//Same relative changes as getRidersGeneralClassificationRank, just with points instead of times
		if(raceIdNames.get(raceId) == null){
			throw new IDNotRecognisedException("ID not attached to a race");
		}
		else{
			ArrayList<Integer> theseStages = raceStages.get(raceId);
			HashMap<Integer, Integer> riderIdPoints  = new HashMap<Integer, Integer>();
			for(int i = 0; i < theseStages.size(); i++){
				int[] currentStageRiders = getRidersRankInStage(theseStages.get(i));
				int[] currentStagePoints = getRidersPointsInStage(theseStages.get(i));
				for(int j = 0; j < currentStageRiders.length; j++){
					int thisRider = currentStageRiders[j];
					int thisPoints = currentStagePoints[j];
					if(riderIdPoints.get(thisRider) == null){
						riderIdPoints.put(thisRider, thisPoints);
					}
					else{
						int totalPoints = riderIdPoints.get(thisRider);
						totalPoints = totalPoints + thisPoints;
						riderIdPoints.remove(thisRider);
						riderIdPoints.put(thisRider, totalPoints);
					}
				}
			}
			int[] listRiderIds = new int[riderIdPoints.size()];
			int p = 0;
			for (int q : riderIdPoints.keySet()){
  				listRiderIds[p] = q;
				p++;
			}
			int[] listRiderPoints = new int[riderIdPoints.size()];
			int r = 0;
			for (int s : riderIdPoints.keySet()){
				listRiderPoints[r] = riderIdPoints.get(s);
				r++;
			}
			for(int u = 0; u < listRiderPoints.length; u++){
				for(int v = u + 1; v < listRiderPoints.length; v++){
					if (listRiderPoints[u] < listRiderPoints[v]) {
						var temp1 = listRiderPoints[u];
						listRiderPoints[u] = listRiderPoints[v];
						listRiderPoints[v] = temp1;
						var temp2 = listRiderIds[u];
						listRiderIds[u] = listRiderIds[v];
						listRiderIds[v] = temp2;
					}
				}
			}return listRiderIds;
		} 
	}
	

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
		//Same relative changes again
		if(raceIdNames.get(raceId) == null){
			throw new IDNotRecognisedException("ID not attached to a race");
		}
		else{
			ArrayList<Integer> theseStages = raceStages.get(raceId);
			HashMap<Integer, Integer> riderIdMountainPoints  = new HashMap<Integer, Integer>();
			for(int i = 0; i < theseStages.size(); i++){
				int[] currentStageRiders = getRidersRankInStage(theseStages.get(i));
				int[] currentStageMountainPoints = getRidersMountainPointsInStage(theseStages.get(i));
				for(int j = 0; j < currentStageRiders.length; j++){
					int thisRider = currentStageRiders[j];
					int thisPoints = currentStageMountainPoints[j];
					if(riderIdMountainPoints.get(thisRider) == null){
						riderIdMountainPoints.put(thisRider, thisPoints);
					}
					else{
						int totalMountainPoints = riderIdMountainPoints.get(thisRider);
						totalMountainPoints = totalMountainPoints + thisPoints;
						riderIdMountainPoints.remove(thisRider);
						riderIdMountainPoints.put(thisRider, totalMountainPoints);
					}
				}
			}
			int[] listRiderIds = new int[riderIdMountainPoints.size()];
			int p = 0;
			for (int q : riderIdMountainPoints.keySet()){
  				listRiderIds[p] = q;
				p++;
			}
			int[] listRiderMountainPoints = new int[riderIdMountainPoints.size()];
			int r = 0;
			for (int s : riderIdMountainPoints.keySet()){
				listRiderMountainPoints[r] = riderIdMountainPoints.get(s);
				r++;
			}
			for(int u = 0; u < listRiderMountainPoints.length; u++){
				for(int v = u + 1; v < listRiderMountainPoints.length; v++){
					if (listRiderMountainPoints[u] > listRiderMountainPoints[v]) {
						var temp1 = listRiderMountainPoints[u];
						listRiderMountainPoints[u] = listRiderMountainPoints[v];
						listRiderMountainPoints[v] = temp1;
						var temp2 = listRiderIds[u];
						listRiderIds[u] = listRiderIds[v];
						listRiderIds[v] = temp2;
					}
				}
			}return listRiderIds;
		} 
	}
}
