package e.joepassanante.tictactoeassignment;
/**
 * TicTacToe class implements the interface
 * @author relkharboutly and Joe Passanante
 * @date 1/5/2017
 */
/*
NOTES:

Had to change the class a bit to support loading foreign boards into the game, in comparison to the console version.


 */
public class TicTacToeLogic implements ITicTacToe {

    // The game board and the game status
    private static final int ROWS = 3, COLS = 3; // number of rows and columns
    private int[][] board;

    /**
     * clear board and set current player
     */
    public TicTacToeLogic(int[][]board){
        this.board = board;
    }
    public TicTacToeLogic(){
        board = new int[ROWS][COLS]; // game board in 2D array
    }
    @Override
    public void clearBoard() {
        board = new int[ROWS][COLS]; // game board in 2D array
    }

    @Override
    public void setMove(int player, int location) {
        int row = location/3;
        int col = location%3;
        if(!checkOpenSpot(location))
            return;
        board[row][col] = (player==0) ? ITicTacToe.NOUGHT : ITicTacToe.CROSS;
    }

    @Override
    public int getComputerMove() {

        int[][] Fakeboard = new int[ROWS][COLS];
        for(int r=0;r<Fakeboard.length;r++){
            for(int c=0; c<Fakeboard[r].length;c++){
                Fakeboard[r][c] = this.board[r][c];
            }
        }
        //run through and check if there is somewhere to win for me
        for(int r = 0; r<Fakeboard.length;r++){
            for(int c=0; c<Fakeboard[r].length;c++){
                if(Fakeboard[r][c]==ITicTacToe.EMPTY){
                    Fakeboard[r][c] = ITicTacToe.CROSS;
                    //if we win by placing a point here... we win or Tie!!
                    if(this.checkWinner(Fakeboard)!=0){
                        for(int lazy = 0;lazy<Fakeboard.length*Fakeboard.length;lazy++){
                            int row = (lazy)/3;
                            int col = (lazy)%3;
                            if(r==row && c==col){
                                return lazy;
                            }
                        }
                    }
                    Fakeboard[r][c] = ITicTacToe.EMPTY;
                }
            }
        }
        //check to see if there is somewhere the other player can win.
        for(int r = 0; r<Fakeboard.length;r++){
            for(int c=0; c<Fakeboard[r].length;c++){
                if(Fakeboard[r][c]==ITicTacToe.EMPTY){
                    Fakeboard[r][c] = ITicTacToe.NOUGHT;
                    //if we are not playing after placing the enemy point here... it means thats dangerous!!
                    if(this.checkWinner(Fakeboard)!=0){
                        for(int lazy = 0;lazy<Fakeboard.length*Fakeboard.length;lazy++){
                            int row = (lazy)/3;
                            int col = (lazy)%3;
                            if(r==row && c==col){
                                return lazy;
                            }
                        }
                    }
                    Fakeboard[r][c] = ITicTacToe.EMPTY;
                }
            }
        }

        //randomly place if all of the above is false.
        int spot = (int)(Math.random()*8);
        while(!this.checkOpenSpot(spot)){
            spot = (int)(Math.random()*8);
        }
        return spot;
    }

    @Override
    public int checkForWinner(){
        return this.checkWinner(this.board);
    }


    private int checkWinner(int[][]b) {
        int winner = 0;
        //check across
        for(int r = 0; r<b.length;r++){
            boolean allSame = true;
            int last = -1;
            for(int c = 0; c<b[r].length;c++){
                if(c==0){
                    last = b[r][c];
                }else{
                    if(b[r][c]!=last){
                        allSame = false;
                    }
                }
            }
            if(allSame && last!=0)
                winner = last+1;
        }
        if(winner!=0)
            return winner;
        //check down
        for(int r = 0; r<b.length;r++){
            boolean allSame = true;
            int last = -1;
            for(int c = 0; c<b.length;c++){
                if(c==0){
                    last = b[c][r];
                }else{
                    if(b[c][r]!=last ){
                        allSame = false;
                    }
                }
            }
            if(allSame && last!=0)
                winner = last+1;
        }

        //check dag 1.
        boolean allSame = true;
        int last = -1;
        for(int r = 0; r<b.length;r++){
            if(r==0){
                last = b[r][r];
            }else{
                if(b[r][r]!=last){
                    allSame = false;
                }
            }
        }
        if(allSame && last!=0)
            winner = last+1;
        if(winner!=0)
            return winner;

        //check dag2
        allSame = true;
        last = -1;
        for(int r = b.length-1; r>=0;r--){
            int c = ((b.length-1)-r);
            if(r==b.length-1){
                last = b[r][c];
            }else{
                if(b[r][c]!=last){
                    allSame = false;
                }
            }
        }
        if(allSame && last!=0)
            winner = last+1;
        if(winner!=0)
            return winner;

        //check Tie
        winner = 1;
        for(int r = 0; r<b.length;r++){
            for(int c = 0; c<b[r].length;c++){
                if(b[r][c]==ITicTacToe.EMPTY){
                    winner = 0;
                }
            }
        }
        return winner;
    }

    //check if spot is open.
    public boolean checkOpenSpot(int location){
        int row = location/3;
        int col = location%3;
        if(board[row][col] == ITicTacToe.EMPTY){
            return true;
        }else{
            return false;
        }
    }
    public int[][] getBoard(){
        return board;
    }
    public int getLocationValue(int location){
        int row = location/3;
        int col = location%3;

        return board[row][col];
    }
}
