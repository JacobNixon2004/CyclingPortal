package cycling;

class Rider extends mainParent {

    private Integer yearOfBirth;

    public Rider(Integer myYearOfBirth, String myName, Integer myTeamID, Integer myRiderID){
        yearOfBirth = myYearOfBirth;
        name = myName;
        teamID = myTeamID;
        riderID = myRiderID;
    }

    public Integer getYearOfBirth(){
        return yearOfBirth;
    }
    public String getName(){
        return name;
    }
    public Integer getTeamID(){
        return teamID;
    }
    public Integer getRiderID(){
        return riderID;
    }
    
}
