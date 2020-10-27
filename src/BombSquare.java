public class BombSquare extends GameSquare
{

    private GameBoard board;                            // Object reference to the GameBoard this square is part of.
    private boolean hasBomb;                            // True if this squre contains a bomb. False otherwise.
    private boolean revealed;
    private boolean flagSet;

	public static final int MINE_PROBABILITY = 7;

	public BombSquare(int x, int y, GameBoard board)
	{
		super(x, y, "images/blank.png");

        this.board = board;
        this.hasBomb = ((int) (Math.random() * MINE_PROBABILITY)) == 0;
    }

    public void leftClicked() {
        if (!flagSet) {
            revealed = true;
            if (hasBomb){
                setImage("images/bomb.png");
                revealBombs();
            } else if (!hasBomb) {
                int bombCount = countSurroundings();
                setImage(toString(bombCount));
                if(bombCount == 0)
                {
                    Expand();
                }
            }
        }
    }
    public void rightClicked() {
	    if(!revealed && !flagSet){
            setImage("images/flag.png");
            flagSet = true;
        }
	    else if(!revealed && flagSet)
        {
            setImage("images/blank.png");
            flagSet = false;
        }
    }

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
                    BombSquare surrounding = (BombSquare) board.getSquareAt(x + i, y + k);
                    if (surrounding.hasBomb) {
                        bombCount++;
                    }
                }
            }
        }
        return bombCount;
    }
    private void Expand()
    {
        int bombCount = countSurroundings();
        int x = getXLocation();
        int y = getYLocation();
        for (int i = -1; i <= 1; i++) {
            for (int k = -1; k <= 1; k++) //Double for loop checks surrounding squares
            {
                if (board.getSquareAt(x + i, y + k) == null) {} //Error handling -> do nothing
                else {
                    BombSquare surrounding = (BombSquare) board.getSquareAt(x + i, y + k);
                    if (!surrounding.revealed && !surrounding.hasBomb && !surrounding.flagSet) {
                        System.out.println(bombCount);
                        setImage(toString(bombCount));
                        revealed = true;
                        if(bombCount == 0){
                            surrounding.Expand(); //Recursive call
                        }
                    }
                }
            }
        }
    }

    private void revealBombs() {
        int x = getXLocation();
        int y = getYLocation();
        for (int i = -1; i <= 1; i++) {
            for (int k = -1; k <= 1; k++) //Double for loop checks surrounding squares
            {
                if (board.getSquareAt(x + i, y + k) == null) {
                } //Error handling -> do nothing
                else {
                    BombSquare surrounding = (BombSquare) board.getSquareAt(x + i, y + k);
                    if(!surrounding.revealed && surrounding.hasBomb)
                    {
                        surrounding.setImage("images/bomb.png");
                        revealed = true;
                        surrounding.revealBombs();
                            }
                    else if(!surrounding.revealed && !surrounding.hasBomb){
                            revealed = true;
                            surrounding.revealBombs();

                        }
                    }
                }
            }
        }
    private String toString(int bombCount)
    {
        return "images/"+bombCount+".png";
    }
}
