import javax.swing.WindowConstants;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Team Foxtrot - JavaBall Referees
 * The controller class
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
 * @version 1.1
 * @since   01-02-2015
 */
public class JavaBallController {
	
    private final Season season;
    private final RefereeList refList;
    private ChartFrame chart;

    private final String REFEREE_FILE = "RefereesOut.txt";
    private final String MATCH_FILE   = "MatchAllocs.txt";
    private final int TABLE_FIELDS = 7;
	
    /** TODO */
    public enum Location {
        NORTH("North"), CENTRAL("Central"), SOUTH("South");
        private final String LocationString;
        private Location(final String s) { 
            LocationString = s; 
        }
        @Override
        public String toString() { return LocationString; }
    }
	
    /**
     *
     * @param season
     * @param refList
     */
    public JavaBallController(Season season, RefereeList refList) {
		this.season = season;
		this.refList = refList;
	}

    /**
     *
     */
    public void openChart() {
		chart = new ChartFrame(refList);
		chart.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		chart.setVisible(true);
	}

    /**
     * 
     */
    public void saveExit() {
    	// Exit programme if data has been successfully saved
        if (writeOutputFile())
        	System.exit(0);
    }
        
    /**
     *
     * @param id
     * @return
     */
	public Referee getReferee(String id) {
		return refList.getReferee(id);
	}

	/**
	 *
	 * @param fname
	 * @param sname
     * @param qualification
     * @param qualLevel
	 * @param allocations
	 * @param home
	 * @param travel
	 */
	public void addReferee(String fname, String sname,
			Referee.Qualifications qualification, int qualLevel,
			int allocations, Location home, String travel) 
        {
            refList.add(new Referee(refList.createID(fname, sname),fname, sname, 
                    qualification.toString()+qualLevel,
			 allocations,  home.toString(), travel));
	}

    /**
     *
     */
    public void addRow()
        {
            
        }
        
	/**
	 * Method to edit fields of Referee
	 * 
     * @param referee
     * @param travel
     * @param qualification
     * @param qualLevel
     * @param home
	 */
	public void editReferee(Referee referee,
			Referee.Qualifications qualification, int qualLevel, Location home,
			String travel) {

	}
        
    /**
     *
     * @param ref
     */
	public void removeReferee(Referee ref) {
		refList.remove(ref);
	}
	
	/**
	 * 
	 * @param week
	 * @param level
	 * @param location
         * @return 
	 */
	public ArrayList<Referee> allocateReferees(int week, Match.Level level,
			Location location) {
		// Create new match without referees
		Match match = new Match(week, level, location);

		// Check if match ID is already in use
		if (season.getMatch(week) == null) {
			//  Return indication of unsuccessful referee allocations
			return null;
		} else {
			// Retrieve all suitable Referees for that match
			ArrayList<Referee> availableReferees = refList
					.getSuitableReferees(match);

			// Select the two most suitable referees and pass them to the match
			Referee[] suitableReferees = { availableReferees.get(0),
					availableReferees.get(1) };
			match.setReferees(suitableReferees);

			// Add the fully filled in match to the current season
			season.addMatch(match);
			
			// Return indication of successful referee allocation
			return availableReferees;
		}
	}
	
    /**
     *
     * @return
     */
    public TableModel getTableData()
        {
            TableModel tableData = new RefereeTableModel(refList.getReferees());
            return tableData;
        }
        
    /**
     *
     * @return
     */
    public int indexCounter()
        {
            return refList.size();
        }
        
    //Old updateTable methods that are no longer used.
	/**
	 * 
        * @return 
	 */
//	public String[][] updateTable() {
//		// Retrieve all referees ordered by ID
//		refList.sort();
//		ArrayList<Referee> refereesByID = refList.getReferees();
//		// Update table with ID sorting
//		return updateTable(refereesByID);
//	}
//	
//    /**
//     *
//     * @param refereeList
//     * @return
//     */
//    public String[][] updateTable(ArrayList<Referee> refereeList) {
//	String[][] table = new String[RefereeList.MAX_REFEREES][TABLE_FIELDS];
//	int row = 0;
//	
//	for (Referee ref : refereeList) {
//	    System.arraycopy(ref.report(), 0, table[row], 0, TABLE_FIELDS);
//	    row++;
//	}
//	return table;
//    }
    
	/**
	 * Write report Files
	 */
	private boolean writeOutputFile() {
            try {
                FileWriter matchFile;
                try (FileWriter refereeFile = new FileWriter(REFEREE_FILE)) {

                    matchFile = new FileWriter(MATCH_FILE);
                    String[] referees = new String[refList.size()];
                    String[] matches = new String[season.getNumMatches()];
                    int refCounter = 0;
                    for (Referee ref : refList) {
                        String details = String.format("%s %s %s %s%d %d %s %s\n",
                                    ref.getID(), ref.getForename(), ref.getSurname(),
                                    ref.getQualifications(),
                                    ref.getQualificationLevel(), ref.getAllocations(),
                                    ref.getHomeLocation(), ref.getTravelLocations());
                        referees[refCounter] = details;
                        refCounter++;
                    }
                    int counter = 0;
                    for (Match match : season) {
                            matches[counter] = (match.report());
                    }
                    for (String s : referees) {
                            refereeFile.write(s);                                        
                    }
                    for (String s : matches) {
                            matchFile.write(s + "\n");
                    }
                }
                matchFile.close();
                return true;
            } catch (IOException ex) {
                    // TODO
                    return false;
            }
	}
        
        private class RefereeTableModel extends AbstractTableModel implements TableModelListener {
            
            private final static int COLUMN_ID     = 0;
            private final static int COLUMN_FNAME  = 1;
            private final static int COLUMN_SNAME  = 2;
            private final static int COLUMN_QUAL   = 3;
            private final static int COLUMN_ALLOC  = 4;
            private final static int COLUMN_HOME   = 5;
            private final static int COLUMN_TRAVEL = 6;
            
            private final String[] columnNames;
            
            private final ArrayList<Referee> listReferees;
            
            public RefereeTableModel(ArrayList<Referee> referees) {
                this.listReferees = referees;
                this.columnNames  = new String[]{"ID", "Forename", "Surname", 
                    "Qualification", "Allocations", "Home", "Travel Areas"};     

                int index = 1;
                for (Referee referee : listReferees)
                {
                    referee.setIndex(index++);
                }
            
            }
            
            @Override
            public int getRowCount() {
                return listReferees.size();
            }

            @Override
            public int getColumnCount() {
                return columnNames.length;
            }

            @Override
            public String getColumnName(int columnIndex) {
                return columnNames[columnIndex];
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (listReferees.isEmpty())
                {
                    return Object.class;
                }
                return getValueAt(0, columnIndex).getClass();
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Referee referee = listReferees.get(rowIndex);
                Object returnValue = null;
                
                switch (columnIndex) {
                case COLUMN_ID:
                    returnValue = referee.getID();
                    break;
                case COLUMN_FNAME:
                    returnValue = referee.getForename();
                    break;
                case COLUMN_SNAME:
                    returnValue = referee.getSurname();
                    break;
                case COLUMN_QUAL:
                    returnValue = referee.getQualifications()+referee.getQualificationLevel();
                    break;
                case COLUMN_ALLOC:
                    returnValue = referee.getAllocations();
                    break;
                case COLUMN_HOME:
                    returnValue = referee.getHomeLocation();
                    break;
                case COLUMN_TRAVEL:
                    returnValue = referee.getTravelLocations();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid column index");
                }

                return returnValue;
                
            }

            @Override
            public void setValueAt(Object value, int rowIndex, int columnIndex) {
                Referee referee = listReferees.get(rowIndex);
                if (columnIndex == COLUMN_ID)
                {
                    referee.setIndex((int) value);
                }
            }

        @Override
        public void tableChanged(TableModelEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        }
        
}