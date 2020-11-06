public class BombSquare extends GameSquare
{
    private GameBoard board;                            // Object reference to the GameBoard this square is part of.
    private boolean hasBomb;                            // True if this squre contains a bomb. False otherwise.
    /**Used to check if square has been revealed or not*/
    private boolean revealed;
    /**Used to check if a flag has been set on a square*/
    private boolean flagSet;
	public static final int MINE_PROBABILITY = 7;

	public BombSquare(int x, int y, GameBoard board)
	{
		super(x, y, "images/blank.png");

        this.board = board;
        this.hasBomb = ((int) (Math.random() * MINE_PROBABILITY)) == 0;
    }
    /**
     * Different scenarios for when left click is pressed
     */
    public void leftClicked() {
        if (!flagSet) {
            revealed = true; //Ignores left clicks on flagged squares
            if (hasBomb){
                setImage("images/bomb.png");
                Expand(true); //Call expansion algorithm to reveal bombs
            } else if (!hasBomb) {
                int bombCount = countSurroundings(); //bombCount set to number of bombs in surrounding squares
                setImage(toString(bombCount)); //Sets image to bombCount
                if(bombCount == 0)
                {
                    Expand(false); //Call expansion algorithm to reveal nearby non-bomb squares
                }
            }
        }
    }
    /**
     * Right click for setting & unsetting flag
     */
    public void rightClicked() {
	    if(!revealed && !flagSet){ //Set flag on non revealed right-clicked square
            setImage("images/flag.png");
            flagSet = true;
        }
	    else if(!revealed && flagSet) //Remove flag
        {
            setImage("images/blank.png");
            flagSet = false;
        }
    }

    /**
     * This method counts the number of bombs in the surrounding squares
     * @return Number of bombs in surrounding squares
     */
    private int countSurroundings()
    {
        int bombCount = 0;
        int x = getXLocation();
        int y = getYLocation();
        for (int i = -1; i <= 1; i++) {
            for (int k = -1; k <= 1; k++) //Double for loop checks surrounding squares
            {
                if (board.getSquareAt(x + i, y + k) == null) {} //Error handling -> do nothing
                else {
                    BombSquare surrounding = (BombSquare) board.getSquareAt(x + i, y + k); //Gets the surrounding squares
                    if (surrounding.hasBomb) {
                        bombCount++;
                    }
                }
            }
        }
        return bombCount;
    }

    /**
     * This is the recursively called method which contains the expansion algorithm which serves 2 functions depending on the parameter state:
     * 1. Reveal all squares adjacent without a bomb, and all of their adjacent squares etc. until a bomb is adjacent
     * 2. Reveal all bombs on the board
     * @param bombTriggered True if bomb has been triggered (function 1), False if used for revealing adjacent squares (Function 2)
     */
    private void Expand(Boolean bombTriggered) {
        int bombCount = countSurroundings();
        int x = getXLocation();
        int y = getYLocation();
        for (int i = -1; i <= 1; i++) {
            for (int k = -1; k <= 1; k++) //Double for loop gets all surrounding squares
            {
                if (board.getSquareAt(x + i, y + k) == null) {} //Error handling -> do nothing
                else {
                    BombSquare surrounding = (BombSquare) board.getSquareAt(x + i, y + k);
                    if (!bombTriggered) { //Function 1: Box with 0 surrounding bombs clicked -> Expand
                        if (!surrounding.revealed && !surrounding.hasBomb && !surrounding.flagSet) {
                            setImage(toString(bombCount)); //Reveal square
                            revealed = true;
                            if (bombCount == 0) { //If square has no bombs adjacent to it
                                surrounding.Expand(false); //Recursive call to continue expansion
                            }
                        }
                    }
                    else if(bombTriggered) //Function 2: Bomb clicked -> Expand to reveal bombs
                    {
                        if(!surrounding.revealed && surrounding.hasBomb) //If adjacent has a bomb
                        {
                            surrounding.setImage("images/bomb.png"); //Reveal bomb
                            revealed = true;
                            surrounding.Expand(true); //Recursive call to continue searching board for bombs
                        }
                        else if(!surrounding.revealed && !surrounding.hasBomb){ //If adjacent does not have a bomb
                            revealed = true;
                            surrounding.Expand(true);

                        }
                    }
                }
            }
        }
    }

    /**
     * This method takes the number of surrounding bombs and converts it into a string to be used
     * in the GameSquare.setImage() method.
     * @param bombCount Number of surrounding bombs to be set as icon
     * @return String for icon in the images folder corresponding to the surrounding bomb count
     */
    private String toString(int bombCount)
    {
        return "images/"+bombCount+".png";
    }
}
