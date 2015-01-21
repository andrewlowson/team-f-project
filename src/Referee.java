
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Team Foxtrot
 * JavaBall Referees
 * XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
 * <p>
 * University of Glasgow
 * MSc/PGDip Information Technology/Software Development
 * Team Project 2014/15
 *
 * @author  Miroslav Pashov, 1005139P
 * @author  Andrew Lowson, 0800685L
 * @author  Marco Cook, 2152599C
 * @author  Raoul Rothfeld, 2164502R
 * 
 * @version 1.0
 * @since   14-01-2015
 * 
 */

public class Referee implements Comparable<Referee> {
	// TODO Andrew (Helped by Marco)
    /**
     * @author Andrew Lowson & Marco Cook
     * This early version of the Referee Class tries to account for 
     * as many possible implementations of other classes as possible.
     * 
     * There will be many methods here that end up becoming redundant
     * but we wanted to try and accommodate different thoughts initially
     * before deciding on final implementation.
     */
    
    //-------------------------------------------------------//
    
    //Basic information about Referee from the input file/user.
    private String forename;
    private String surname;
    private String fullname; // Decide later: delete -rr
    private String uniqueID;
    private String qualification; //maybe as int (without NJB/IJB)? -rr
    private String homeLocality;
    
    /* 
     * boolean values that represent whether or not Referee 
     * will visit this location
     */
    private boolean visitNorth;
    private boolean visitCentre;
    private boolean visitSouth;
    
    // Alternate method of storing visit areas
    private boolean[] visitArea;
    
    // Count of amount of matches ref has been allocated to
    private int allocatedCount; 
    private int qualificationLevel; 
    
    // List of matches by matchID referee has been allocated to
    private ArrayList<Integer> allocatedMatchesList;
    
    private final int MAXMATCHES = 52;
    
    //-------------------------------------------------------//
    
    /**
     * Default Constructor.
     */
    public Referee()
    {
        
        this.uniqueID       = "";
        this.forename       = "";
        this.surname        = "";
        this.fullname       = "";
        this.qualification  = "";
        this.homeLocality   = "";
        this.allocatedCount = 0;
        
    }
    /**
     * Constructor to be used if passed either all referee information from 
     * GUI when adding new ref, or if ReadLine splits details up before
     * creating a Referee object. 
     * 
     * @param id - Referee ID, format XY1
     * @param forename - Referee forename
     * @param surname - Referee surname
     * @param qual 
     * @param allocCount - amount of matches allocated to referee
     * @param homeLocality - home area for referee
     * @param travel - string Y/N for areas Referee will travel too
     */
    public Referee(String id, String forename, String surname, 
            String qual, String homeLocality, int allocCount, String travel) 
    {
       
        //Full name 
        String name = forename + " " + surname;
        
        //Convert travel parameter to boolean values for area.
        setTravelLocations(travel); 
             
        this.uniqueID        = id;
        this.forename        = forename;
        this.surname         = surname;
        this.fullname        = name;
        this.qualification   = qual;
        this.homeLocality    = homeLocality;
        this.allocatedCount  = allocCount;
        
        allocatedMatchesList = new ArrayList(MAXMATCHES);
                   
    }
    
    //-------------------------------------------------------//
    
    /**
     * Constructor to be used if ReadLine has not been split before 
     * instantiating new Referee object.
     * String is split, verified for validity and values assigned.
     * @param fileLine - Long line with all Referee Information
     */
    public Referee(String fileLine)
    {
        
        String [] refereeDetails = fileLine.split(" ");
        
        //Check to make sure line split properly and has adequate items
        if (refereeDetails != null && refereeDetails.length == 7)
        {
            this.uniqueID        = refereeDetails[0];
            this.forename        = refereeDetails[1];
            this.surname         = refereeDetails[2];
            this.qualification   = refereeDetails[3];
            this.allocatedCount  = Integer.parseInt(refereeDetails[4]);
            this.homeLocality    = refereeDetails[5];
            
            allocatedMatchesList = new ArrayList(52);
            //convert travel locations to boolean
            setTravelLocations(refereeDetails[6]);
        }
    }
    
    //-------------------------------------------------------//
    
    /**
     * Method to convert Referee Area Options to boolean
     * @param travel - the three character String eg. 'YYY'
     */   
    private void setTravelLocations(String travel)
    {
        //remove any possible whitespace
        travel = travel.trim();
        
        // if we go with boolean array.
        for (int i=0;i<travel.length();i++)
        {
            visitArea[i] = travel.charAt(i)=='Y';
        }   
        
        // Other implementation
        this.visitNorth  = visitArea[0];
        this.visitCentre = visitArea[1];
        this.visitSouth  = visitArea[2];
        
    }
    
    //-------------------------------------------------------//
    
    /**
     * Set RefereeID
     * @param ID 
     */        
    public void setID(String ID)
    {
        this.uniqueID = ID;
    }
    
    //-------------------------------------------------------//
    
    /**
     * Pass back refereeID
     * @return
     */
    public String getID()
    {
        return uniqueID;
    }
    
    //-------------------------------------------------------//

    /**
     * Set forename and surname if UI collects full name "First Second" 
     * from user.
     * @param fullname - Full String format "Forename Surname"
     */
        public void setName(String fullname)
    {
        String [] names = fullname.split(" ");
        
        this.forename = names[0];
        this.surname  = names[1];
    }
    
    //-------------------------------------------------------//
    
    /**
     * Likely won't be used, concatenates fore and surnames.
     * @param forename
     * @param surname
     */
        
    public void setFullName(String forename, String surname)
    {
        this.fullname = forename+" "+surname;   
    }
    
    //-------------------------------------------------------//
    
    /**
     * Returns Fullname, likely won't be used if fullname is not kept.
     * @return
     */
    public String getFullName()
    {
        return fullname;
    }
    
    //-------------------------------------------------------//
    
    /**
     * Set forename of referee.
     * @param forename
     */
    public void setForename(String forename)
    {
        this.forename = forename;
    }
    
    //-------------------------------------------------------//
    
    /**
     * Return forename
     * @return
     */
        
    public String getForename()
    {
        return forename;
    }
    
    //-------------------------------------------------------//
    /**
     * Set surname for Referee
     * @param surname
     */
    public void setSurname(String surname)
    {
        this.surname = surname;
    }
    
    //-------------------------------------------------------//
    
    /**
     *
     * @return
     */
    public String getSurname()
    {
        return surname;
    }
    
    
    //-------------------------------------------------------//

    /**
     * 
     * @param qualifications
     */
    public void setQualifications(String qualifications)
    {
        this.qualification = qualifications;
    }
    
    //-------------------------------------------------------//
    /**
     * Integer value for qualification level independent of other info.
     * @param qualification - full qualification String eg. IJB1
     */
    public void setQualificationLevel(String qualification)
    {
        int length         = qualification.length()-1;
        qualificationLevel = (int) qualification.charAt(length);
    }
        
    //-------------------------------------------------------//
    /**
     *
     * @return
     */ 
    public String getQualifications()
    {
        return qualification;
    }
    
    //-------------------------------------------------------//
    
    /**
     *
     * @param location
     */
    public void setHomeLocation(String location)
    {
        this.homeLocality = location;
    }
    //-------------------------------------------------------//
    
    /**
     *
     * @return
     */
    public String getHomeLocation()
    {
        return homeLocality;
    }
    
    //-------------------------------------------------------//
    
    /**
     *
     * @return
     */
    public int getAllocations()
    {
        return allocatedCount;
    }
    
    //-------------------------------------------------------//
    
    /**
     * 
     * @param week
     */
    public void increaseMatchesRefereed(int week)
    {
        allocatedCount++;
        allocatedMatchesList.add(week);
    }
    
    //-------------------------------------------------------//
    
    /**
     *
     * @param travel
     */
        
    public void setTravelAreas(String travel)
    {
        setTravelLocations(travel);
    }
    
    //-------------------------------------------------------//
    
    /**
     *
     * @param matches
     */
    public void setMatchesRefereed(int matches)
    {
        this.allocatedCount = matches;
    }
    
    //-------------------------------------------------------//
    
    /**
     *
     * @return
     */
        
    public boolean[] getTravelLocations()
    {
        return visitArea;
    }
    //-------------------------------------------------------//
    
    /**
     * Get boolean for location directly. 
     * 0,1,2 = N,C,S respectively.
     * @param index - value 0,1,2
     * @return
     */
    public boolean getTravelLocationAtIndex(int index)
    {
        return visitArea[index];
    }
    
    //-------------------------------------------------------//
    
    /**
     * Returns a boolean value referring to whether the referee will travel
     * to that area or not.
     * @param location - location required.
     * @return 
     */  
    public boolean getTravelLocation(String location)
    {
        if (location.equals("North"))
        {
            return visitNorth;
        } 
        else if (location.equals("Centre")) 
        {
            return visitCentre;
        } 
        else 
        {
            return visitSouth;
        }
    }
    
    //-------------------------------------------------------//
    
     /**
     * Method to change a particular travel preference
     * @param index - index for area. 0:North 1:Centre 2:South
     * @param willTravel - true or false pertaining to that area.
     */
        
    public void changeLocationAtIndex(int index, boolean willTravel)
    {
        visitArea[index] = willTravel;
    }
    
    //-------------------------------------------------------//
    
    /**
     * Method to update all three locations in one step
     * Referee can change travel preferences. 
     * Takes in one string, same format as initial preference input.
     * @param travel - is the three character string eg. "YYN"
     * 
     */        
    public void updateTravelLocations(String travel)
    {
        //remove any possible whitespace
        travel = travel.trim();
        
        // if we go with boolean array.
        for (int i=0;i<travel.length();i++)
        {
            visitArea[i] = travel.charAt(i)=='Y';
        }   
    }
    
    //-------------------------------------------------------//

    @Override
    public int compareTo(Referee ref) 
    {
        // this has not been tested but StackOverflow said it would work
        // Will test in a separate programme.
        return this.getID().compareTo(ref.getID());
    }

    //-------------------------------------------------------//
}
