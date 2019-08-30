import javax.swing.*;
import java.util.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class ChessGame {                                                                                // Main class

    static String board[][] = {                                                                       // This string array represents the chessboard.  Each time we make a move this board is updated
            {"r", "n", "b", "q", "k", "b", "n", "r"},
            {"p", "p", "p", "p", "p", "p", "p", "p"},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {" ", " ", " ", " ", " ", " ", " ", " "},
            {"P", "P", "P", "P", "P", "P", "P", "P"},
            {"R", "N", "B", "Q", "K", "B", "N", "R"}
    };

    static int whiteKing, blackKing, moves;                                     // the king ints record where our king is after each move and moves counts our moves


    public static void main(String[] args) {
        UserInterface gui = new UserInterface();                                            //initiate the class UserInterface which hosts our GUI

        JFrame f = new JFrame("James Calcagni   Chess Project   CS-111");                    // builds the window where our screen opens
        f.add(gui);                                                                               //this adds our user interface "gui" to the frame
        f.setSize(475, 500);                                                         // sets the size of the frame to the size of the chessboard  **note to look into later.. find out why i needed to make width 25pixel less to make a perfect square ((maybe title bar is 25??))
        f.setVisible(true);                                                                       // makes our frame visible
        f.setResizable(false);                                                                     // doesn't allow user to resize the board..  Not entirely necessary but i like it better this way
        f.setLocationRelativeTo(null);                                                              // centers the gui on the screen
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while (!"K".equals(board[whiteKing / 8][whiteKing % 8])) {
            whiteKing++;                                                             //updates position of the white King, i believe using this is not entirely necessary but i found it useful for determining if the king was in check and is easier to use than relying solely on the k on board
        }
        while (!"k".equals(board[blackKing/8][blackKing%8])) {
            blackKing++;                                                              //updates position of the black king
        }
    }


    private static void moveMade() {                             //counter to determine whose move it is
        moves++;
    }


    public static void movePiece(String move) {   // moves pieces on board by changing x and y coordinates of go to position and removing them from previous position (unless pawn promotion which is special case) *** Note: if later adding other special cases i.g. En Pessent or castling include those special cases here also!
        if (moves%2 != 0) {                      // moves piece only for the black pieces ... the charAt() pulls the numerical character from string out of the following format:  row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
            if (move.charAt(4) != 'p') {
                board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] = board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
                board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] = " ";
                if ("k".equals(board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))])) {   // if the black king is the piece that is moved this updates our location of the black King
                    blackKing = 8 * Character.getNumericValue(move.charAt(0)) + Character.getNumericValue(move.charAt(1));
                }
            } else {                                // moves piece only for the black pieces..        in the case of pawn promotion must carry extra values so it can change to queen we do not need the columns because we know it is the end column so we are able to account for that case by using the format: col from (0),col to (1), capped piece (2), queen "q" (3), "p" (4)
                board[6][Character.getNumericValue(move.charAt(0))] = " ";                                          //going from rank 6 to 7 for all pawn promotions so we use [6] and [7] for our rows
                board[7][Character.getNumericValue(move.charAt(1))] = String.valueOf(move.charAt(3));                // could just put a "q" here but already coded it into pawn promotion so left this way.  logically flows better also
            }
        } else {
            if (move.charAt(4) != 'P') {                                    // moves only for white pieces  with format: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] = board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
                board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] = " ";
                if ("K".equals(board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))])) {    // if the white king is the piece that is moved this updates our location of the whiteKing
                    whiteKing = 8 * Character.getNumericValue(move.charAt(0)) + Character.getNumericValue(move.charAt(1));
                }
            } else {
                board[1][Character.getNumericValue(move.charAt(0))] = " ";                                                      //case of pawn promotion for white pieces... going from rank 1 to 0 for all pawn promotions so we use [1] and [0]  as our rows
                board[0][Character.getNumericValue(move.charAt(1))] = String.valueOf(move.charAt(3));                   //using format: col from (0),col to (1), capped piece (2), queen "q" (3), "p" (4)
            }
        }
        moveMade();                                                                           //updates the counter to see whose turn it is



        // These are very useful for debugging and being able to see whats going on
        // System.out.println(possMoves());                                                          // uncomment (delete //)  to return all of the possible moves for next turn after each move to the cmd line
        // System.out.println(move);                                                                // uncomment to print the last move made after each move
        // for (int i = 0; i < 8; i++) {System.out.println(Arrays.toString(board[i]));}             // uncomment  to reprint the board array to the cmd line after each move
    }

    public static String possMoves() {                          //returns a string which holds all of the possible moves
        String out = " ";                                       // out is the output string returned when a move is made.  can be of the form [row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4) ] for regular moves or [col from (0),col to (1), capped piece (2), queen "q" (3), "p" (4)] for pawn promotion
        for (int i = 0; i < 64; i++) {                          // there are 64 tiles on our board, this loop sorts through all of them
            if (moves % 2 == 0) {                          // when we perform %2 on our number of moves we get a 0 or a 1.  If it is a 0 that means it is whites turn because white starts first and then it changes back and forth
                switch (board[i / 8][i % 8]) {            // switch statement is very simply  determines which piece we are attempting to move and returning the out holding its potential moves to be executed by the movePiece above

                    case "P":                                                //case for White Pawns
                        out += possWhitePawn(i);
                        break;
                    case "K":
                        out += possWhiteKing(i);                              //case for White King
                        break;
                    case "Q":
                        out += possWhiteQueen(i);                            //case for White Queen
                        break;
                    case "N":
                        out += possWhiteKnight(i);                           //case for White knight     NOTE*** I use "N" for knight. i later found out common convention is to use A for king and K for knight but I prefer mine
                        break;
                    case "R":
                        out += possWhiteRook(i);                            // case for white rook
                        break;
                    case "B":
                        out += possWhiteBishop(i);                          //case for white bishop
                        break;
                }
            } else {                                              // switch case for Black pieces determines which black piece we are attempting to move and returning the out holding its potential moves to be executed by the movePiece
                switch (board[i / 8][i % 8]) {
                    case "p":                                                   //case for black pawns
                        out += possBlackPawn(i);
                        break;
                    case "n":                                                   //case for black knights   NOTE** I Used "n" for the black knight.
                        out += possBlackKnight(i);
                        break;
                    case "r":                                                   //case for black rooks
                        out += possBlackRook(i);
                        break;
                    case "k":                                                   //case for black king
                        out += possBlackKing(i);
                        break;
                    case "q":                                                   //case for black queen
                        out += possBlackQueen(i);
                        break;
                    case "b":                                                      //case for black bishop
                        out += possBlackBishop(i);
                        break;
                }
            }
        }
        return out;
    }





    public static String possWhitePawn(int i) {        // checks all of the possible moves for white pawns.  Pawns are easily the most complex as they have so many different scenarios
        String out = " ", previous;
        int row = i / 8, col = i % 8;
        try {                                                                                            //CAPTURE --possible capture moves for a pawn (2 diagonals in direction of movement)
            for (int j = -1; j <= 1; j += 2) {                                                   // j  = -1 and 1    used to check columns to left and right of the pawn
                if (Character.isLowerCase(board[row - 1][col + j].charAt(0)) && i >= 16) {          //if diagonal spot for capture is occupied by a black piece (lower case letter) which can be captured then it executes
                    previous = board[row - 1][col + j];                                             // sets temporary variable  to be  whatever is the spot going to
                    board[row][col] = " ";                                                          // makes current loc an empty string
                    board[row - 1][col + j] = "P";                                                  // inflates new loc with pawn
                    if (noWhiteKingThreat() == true) {
                        out = out + row + col + (row - 1) + (col + j) + previous;               // if it is a legal move (meaning the king is not put into check when the move is made) outputs this string to the makeMove function in format:: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                    }
                    board[row][col] = "P";                                                          // resets the board with new piece location  (if the king is under threat)
                    board[row - 1][col + j] = previous;                                             //resets spot to the previous (if the king is under threat)
                }
            }
        } catch (Exception e) { }

        try {                                                                                 // MOVE UP 1 --sees if possible to move up 1, only will do so if space in front of it is empty
            if (" ".equals(board[row - 1][col]) && i >= 16) {                                   //if forwards spot is occupied by an empty string " "  then it executes
                previous = board[row - 1][col];                                                     // sets temporary variable  to be  whatever is the spot going to
                board[row][col] = " ";                                                                // makes current loc an empty string
                board[row - 1][col] = "P";                                                               // inflates new loc with pawn
                if (noWhiteKingThreat() == true) {
                    out = out + row + col + (row - 1) + col + previous;                           // if it is a legal move  outputs this string to the makeMove function: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                }
                board[row][col] = "P";                                                          // resets the board with new piece location  (if the king is under threat)
                board[row - 1][col] = previous;                                             //resets spot to the previous (if the king is under threat)
            }
        } catch (Exception e) { }
        try {                                                                                 //MOVE UP 2 --sees if possible for pawn to move up 2 ****only allowed on first move and if two squares in front are both empty
            if (" ".equals(board[row - 1][col]) && " ".equals(board[row - 2][col]) && i >= 48) { //if 2 forwards spot is occupied by an empty string " "  then it executes
                previous = board[row - 2][col];                                               // sets temporary variable  to be  whatever is the spot going to
                board[row][col] = " ";                                                              // makes current loc an empty string
                board[row - 2][col] = "P";
                if (noWhiteKingThreat() == true) {
                    out = out + row + col + (row - 2) + col + previous;                                         // if it is a legal move  outputs this string "out" to the makeMove function: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                }
                board[row][col] = "P";                                                      // resets the board with new piece location  (if the king is under threat)
                board[row - 2][col] = previous;                                             //resets spot to the previous (if the king is under threat)
            }
        } catch (Exception e) { }
        try {                                                                                 //PROMOTE NO CAP --case for a pawn promotion walking directly onto final rank with no capture
            if (" ".equals(board[row - 1][col]) && i < 16) {                                //if forwards spot on final rank is occupied by an empty string " "  then it executes
                previous = board[row - 1][col];                                               // sets temporary variable  to be  whatever is the spot going to
                board[row][col] = "";                                                          // makes current loc an empty string
                board[row - 1][col] = "Q";                                                             // inflates new loc with pawn
                if (noWhiteKingThreat() == true) {
                    out = out + col + col + previous + "Q" + "P";        // in order to do the pawn promotion we must change the piece and therefor must create a different type of output string for our move.  this special case is then specifically dealt with in the movePiece method
                }                                                        // format for out string for promotion is: col from (0),col to (1), capped piece (2), queen "q" (3), "p" (4)   **note we do not need to include row because it always promotes on the final row.  if last letter is P then we know it is a promotion
                board[row][col] = "P";                                                     // resets the board with new piece location  (if the king is under threat)
                board[row - 1][col] = previous;                                             //resets spot to the previous (if the king is under threat)
            }
        } catch (Exception e) { }

        try {                                                                                //PROMOTE AND CAPTURE --case for a pawn promotion where it captures a piece on the final rank
            for (int j = -1; j <= 1; j += 2) {                                           // j  = -1 and 1  used to check columns to left and right of the pawn
                if (Character.isLowerCase(board[row - 1][col + j].charAt(0)) && i < 16) {       //if diagonal spot for capture is occupied by a black piece (lower case letter) which can be captured then it executes
                    previous = board[row - 1][col + j];                                           // sets temporary variable  to be  whatever is the spot going to
                    board[row][col] = "";                                                             // makes current loc an empty string
                    board[row - 1][col + j] = "Q";                                                         // inflates new loc with pawn
                    if (noWhiteKingThreat() == true) {
                        out = out + col + (col + j) + previous + "Q" + "P";                  // if it is a legal move  outputs this string "out" to the makeMove function  in format: col from (0),col to (1), capped piece (2), queen "q" (3), "p" (4)
                    }
                    board[row][col] = "P";                                                  // resets the board with new piece location  (if the king is under threat)
                    board[row - 1][col + j] = previous;                                     //resets spot to the previous (if the king is under threat)
                }
            }
        } catch (Exception e) {}

        try {                                                                                //PROMOTE AND CAPTURE on the first file ... this should be included in the above promote and cap logic but for some reason this specific move was unable to execute on the first file, I could not figure out the error so this is my fix
            int j = 1;                                        // j  =  1  used to check columns to left of the pawn
            if (Character.isLowerCase(board[row-1][col + j].charAt(0)) && i == 8) {       //if diagonal spot for capture is occupied by a black piece (lower case letter) which can be captured then it executes
                previous = board[row][col];                                           // sets temporary variable  to be  whatever is the spot going to
                board[row][col] = " ";                                                             // makes current loc an empty string
                board[0][col + j] = "Q";                                                         // inflates new loc with pawn
                if (noWhiteKingThreat() == true) {
                    out = out + "0" + "1" + previous + "Q" + "P";                  // if it is a legal move  outputs this string "out" to the makeMove function  in format: col from (0),col to (1), capped piece (2), queen "q" (3), "p" (4)
                }
                board[row][col] = "P";                                                  // resets the board with new piece location  (if the king is under threat)
                board[0][col + j] = previous;                                     //resets spot to the previous (if the king is under threat)
            }
        } catch (Exception e) {}
        return out;
    }


    public static String possBlackPawn(int i) {         // checks all of the possible moves for black pawns.
        String out = " ", previous;
        int row = i / 8, col = i % 8;
        try {                                                                             //CAPTURE --possible capture moves for a pawn (2 diagonals in direction of movement)
            for (int j = -1; j <= 1; j += 2) {                                                // j  = -1 and 1
                if (Character.isUpperCase(board[row + 1][col - j].charAt(0)) && i <= 47) {            //if diagonal spot for capture is occupied by a white piece (upper case letter) which can be captured then it executes
                    previous = board[row + 1][col - j];                                           // sets temporary variable  to be  whatever is the spot going to
                    board[row][col] = " ";                                                          // makes current loc an empty string
                    board[row + 1][col - j] = "p";                                                     // inflates new loc with pawn
                    if (noBlackKingThreat() == true) {
                        out = out + row + col + (row + 1) + (col - j) + previous;
                    }
                    board[row][col] = "p";                                                        // resets the board with new piece location  (if the king is under threat)
                    board[row + 1][col - j] = previous;                                         //resets spot to the previous (if the king is under threat)
                }
            }
        } catch (Exception e) { }

        try {                                                                                 // MOVE UP 1 --sees if possible to move up 1, only will do so if space in front of it is empty
            if (" ".equals(board[row + 1][col]) && i <= 47) {                                       //if forwards spot is occupied by an empty string " "  then it executes
                previous = board[row + 1][col];                                                   // sets temporary variable  to be  whatever is the spot going to
                board[row][col] = " ";                                                              // makes current loc an empty string
                board[row + 1][col] = "p";                                                                 // inflates new loc with pawn
                if (noBlackKingThreat() == true) {
                    out = out + row + col + (row + 1) + col + previous;             // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                }
                board[row][col] = "p";                                                          // resets the board with new piece location  (if the king is under threat)
                board[row + 1][col] = previous;                                                 //resets spot to the previous (if the king is under threat)
            }
        } catch (Exception e) { }

        try {                                                                                //MOVE UP 2 --sees if possible for pawn to move up 2 ****only allowed on first move and if two squares in front are both empty
            if (" ".equals(board[row + 1][col]) && " ".equals(board[row + 2][col]) && i <= 15) {        //if forwards 2 spots are occupied by an empty string " "  then it executes
                previous = board[row + 2][col];                                                   // sets temporary variable  to be  whatever is the spot going to
                board[row][col] = " ";                                                              // makes current loc an empty string
                board[row + 2][col] = "p";                                                                      // inflates new loc with pawn
                if (noBlackKingThreat() == true) {
                    out = out + row + col + (row + 2) + col + previous;           // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                }
                board[row][col] = "p";                                                            // resets the board with new piece location  (if the king is under threat)
                board[row + 2][col] = previous;                                                 //resets spot to the previous (if the king is under threat)
            }
        } catch (Exception e) { }
        try {                                                                               //PROMOTE NO CAP --case for a pawn promotion walking directly onto final rank with no capture
            if (" ".equals(board[7][col]) && i >= 47) {                                   //if forwards spot on final rank is occupied by an empty string " "  then it promotes
                previous = board[7][col];                                               // sets temporary variable  to be  whatever is the spot going to
                board[row][col] = " ";                                                          // makes current loc an empty string
                board[7][col] = "q";                                                         // inflates new loc with pawn
                if (noBlackKingThreat() == true) {
                    out = out + col + col + previous + "q" + "p";         // if legal: col from (0),col to (1), capped piece (2), queen "q" (3), "p" (4)
                }
                board[row][col] = "p";                                                  // resets the board with new piece location  (if the king is under threat)
                board[7][col] = previous;                                         //resets end spot to the previous (if the king is under threat)
            }
        } catch (Exception e) { }

        try {                                                                 //PROMOTE AND CAPTURE --case for a pawn promotion where it captures a piece on the final rank
            for (int j = -1; j <= 1; j += 2) {                                    // j  = -1 and 1
                if (Character.isUpperCase(board[7][col + j].charAt(0)) && i >= 47) {      //if 2 diagonals spots are occupied by white player pieces (upper case) it executes
                    previous = board[7][col + j];                                   // sets temporary variable  to be  whatever is the spot going to
                    board[row][col] = " ";                                                   // makes current loc an empty string
                    board[row + 1][col + j] = "q";                                                 // inflates new loc with pawn
                    if (noBlackKingThreat() == true) {
                        out = out + col + (col + j) + previous + "q" + "p";        // if legal move outputs this string "out" to the makeMove function  in format: col from (0),col to (1), capped piece (2), queen "q" (3), "p" (4)
                    }
                    board[row][col] = "p";                                              // resets the board with new piece location  (if the king is under threat)
                    board[row + 1][col + j] = previous;                             //resets spot to the previous (if the king is under threat)
                }
            }
        } catch (Exception e) { }
        try {                                                                                //PROMOTE AND CAPTURE on the first file ... this should be included in the above promote and cap logic but for some reason this specific move was unable to execute on the first file, I could not figure out the error so this is my fix
            int j = 1;                                        // j  =  -1  used to check columns to left and right of the pawn
            if (Character.isUpperCase(board[row+1][col + j].charAt(0)) && i == 48) {       //if diagonal spot for capture is occupied by a black piece (lower case letter) which can be captured then it executes
                previous = board[row + 1][1];                                           // sets temporary variable  to be  whatever is the spot going to
                board[row][col] = " ";                                                             // makes current loc an empty string
                board[row + 1][col + j] = "p";                                                         // inflates new loc with pawn
                if (noBlackKingThreat() == true) {
                    out = out + "0" + "1" + previous + "q" + "p";                  // if it is a legal move  outputs this string "out" to the makeMove function  in format: col from (0),col to (1), capped piece (2), queen "q" (3), "p" (4)
                }
                board[row][col] = "p";                                                  // resets the board with new piece location  (if the king is under threat)
                board[row + 1][col + j] = previous;                                     //resets spot to the previous (if the king is under threat)
            }
        } catch (Exception e) {}
        return out;
    }


    public static String possWhiteKnight(int i) {                    // checks all of the possible moves for white knights.
        String out = " ", previous;
        int row = i/8, col = i%8;
        for (int j = -1; j <= 1; j += 2) {                                                       // j is -1 or 1
            for (int k = -1; k <= 1; k += 2) {                                                        // k is -1 or 1
                try {
                    if (Character.isLowerCase(board[row + j][col + k*2].charAt(0)) || " ".equals(board[row + j][col + k*2])) {   //if row increse/decrease by 1 and col increase/decrease by 2 and spot goes to is black (lowercase) or empty " " it executes
                        previous = board[row + j][col + k*2];                                         // sets temporary variable  to be  whatever is the spot going to
                        board[row][col] = " ";                                                          // makes current loc an empty string
                        board[row + j][col + k*2] = "N";                                                   // inflates new loc with knight
                        if (noWhiteKingThreat() == true) {
                            out = out + row + col + (row + j) + (col + k*2) + previous;           // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                        }
                        board[row][col] = "N";                                              // resets the board with new piece location  (if the king is under threat)
                        board[row + j][col + k*2] = previous;                           //resets spot to the previous (if the king is under threat)
                    }
                } catch (Exception e) { }
                try {
                    if (Character.isLowerCase(board[row + j*2][col + k].charAt(0)) || " ".equals(board[row + j*2][col + k])) {           //if row increse/decrease by 2 and col increase/decrease by 1 and spot goes to is black (lowercase) or empty " " it executes
                        previous = board[row + j*2][col + k];                                     // sets temporary variable  to be  whatever is the spot going to
                        board[row][col] = " ";                                                      // makes current loc an empty string
                        board[row + j*2][col + k] = "N";                                             // inflates new loc with knight
                        if (noWhiteKingThreat() == true) {
                            out = out + row + col + (row + j*2) + (col + k) + previous;               // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                        }
                        board[row][col] = "N";                                                                  // resets the board with new piece location  (if the king is under threat)
                        board[row + j*2][col + k] = previous;                                                   //resets spot to the previous (if the king is under threat)
                    }
                } catch (Exception e) { }

            }
        }
        return out;
    }




    public static String possBlackKnight(int i) {                              //Checks all possible moves for the black knight
        String out = " ", previous;
        int row = i / 8, col = i % 8;
        for (int j = -1; j <= 1; j += 2) {                                                          // j is -1 or 1
            for (int k = -1; k <= 1; k += 2) {                                                       // K is -1 or 1
                try {
                    if (Character.isUpperCase(board[row + j][col + k*2].charAt(0)) || " ".equals(board[row + j][col + k*2])) {           //if row increse/decrease by 1 and col increase/decrease by 2 and spot goes to is white (uppercase) or empty " " it executes
                        previous = board[row + j][col + k*2];                                                             // sets temporary variable  to be  whatever is the spot going to
                        board[row][col] = " ";                                                                              // makes current loc an empty string
                        board[row + j][col + k*2] = "n";                                                                 // inflates new loc with knight
                        if (noBlackKingThreat() == true) {
                            out = out + row + col + (row + j) + (col + k*2) + previous;           // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                        }
                        board[row][col] = "n";                                                          // resets the board with new piece location  (if the king is under threat)
                        board[row + j][col + k*2] = previous;                                       //resets spot to the previous (if the king is under threat)
                    }
                } catch (Exception e) {}
                try {
                    if (Character.isUpperCase(board[row + j*2][col + k].charAt(0)) || " ".equals(board[row + j*2][col + k])) {                //if row increse/decrease by 2 and col increase/decrease by 1 and spot goes to is white (uppercase) or empty " " it executes
                        previous = board[row + j*2][col + k];                                                     // sets temporary variable  to be  whatever is the spot going to
                        board[row][col] = " ";                                                                      // makes current loc an empty string
                        board[row + j*2][col + k] = "n";                                                             // inflates new loc with knight
                        if (noBlackKingThreat() == true) {
                            out = out + row + col + (row + j*2) + (col + k) + previous;           // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                        }
                        board[row][col] = "n";                                                          // resets the board with new piece location  (if the king is under threat)
                        board[row + j*2][col + k] = previous;                                           //resets spot to the previous (if the king is under threat)
                    }
                } catch (Exception e) {}
            }
        }
        return out;
    }


    public static String possWhiteRook(int i) {                   // checks all of the possible moves for white rooks.
        String out = " ", previous;
        int row = i / 8, col = i % 8, foo = 1;
        for (int j = -1; j <= 1; j += 2) {                                              //j is = -1 or 1
            try {
                while (" ".equals(board[row][col + foo*j])) {                                                   // if vertical spot is empty it executes
                    previous = board[row][col + foo*j];                                                        // sets temporary variable  to be  whatever is the spot going to
                    board[row][col] = " ";                                                                          // makes current loc an empty string
                    board[row][col + foo*j] = "R";                                                                   // inflates new loc with Rook
                    if (noWhiteKingThreat() == true) {
                        out = out + row + col + row + (col + foo*j) + previous;                                          // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                    }
                    board[row][col] = "R";                                                                  // resets the board with new piece location  (if the king is under threat)
                    board[row][col + foo*j] = previous;                                                         //resets spot to the previous (if the king is under threat)
                    foo++;                                                                                      //keeps increasing spot until it hits a piece or reaches end of board
                }
                if (Character.isLowerCase(board[row][col + foo*j].charAt(0))) {                                         // if vertical spot is black it executes
                    previous = board[row][col + foo*j];                                                              // sets temporary variable  to be  whatever is the spot going to
                    board[row][col] = " ";                                                                              // makes current loc an empty string
                    board[row][col + foo*j] = "R";                                                                      // inflates new loc with Rook
                    if (noWhiteKingThreat() == true) {
                        out = out + row + col + row + (col + foo*j) + previous;                                          // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                    }
                    board[row][col] = "R";                                                                      // resets the board with new piece location  (if the king is under threat)
                    board[row][col + foo*j] = previous;                                                         //resets spot to the previous (if the king is under threat)
                }
            } catch (Exception e) {}
            foo = 1;                                                                                        ///resets our temporary variable that counts spots
            try {
                while (" ".equals(board[row + foo*j][col])) {                                               // if horizontal spot is empty it executes
                    previous = board[row + foo*j][col];                                                           // sets temporary variable  to be  whatever is the spot going to
                    board[row][col] = " ";                                                                                  // makes current loc an empty string
                    board[row + foo*j][col] = "R";                                                                          // inflates new loc with Rook
                    if (noWhiteKingThreat() == true) {
                        out = out + row + col + (row + foo*j) + col + previous;                                          // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                    }
                    board[row][col] = "R";                                                                       // resets the board with new piece location  (if the king is under threat)
                    board[row + foo*j][col] = previous;                                                         //resets spot to the previous (if the king is under threat)
                    foo++;                                                                                        //keeps increasing spot until it hits a piece or reaches end of board
                }
                if (Character.isLowerCase(board[row + foo*j][col].charAt(0))) {                                          // if horizontal spot is black piece  it executes
                    previous = board[row + foo*j][col];                                                          // sets the temporary variable  to be the spot going to
                    board[row][col] = " ";                                                                          // makes current loc an empty string
                    board[row + foo*j][col] = "R";                                                                  // inflates new loc with Rook
                    if (noWhiteKingThreat() == true) {
                        out = out + row + col + (row + foo*j) + col + previous;                                          // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                    }
                    board[row][col] = "R";                                                                          // resets the board with new piece location  (if the king is under threat)
                    board[row + foo*j][col] = previous;                                                         //resets spot to the previous (if the king is under threat)
                }
            } catch (Exception e) { }
            foo = 1;
        }
        return out;
    }

    public static String possBlackRook(int i) {                                              // checks all of the possible moves for black rooks.
        String out = " ", previous;
        int row = i / 8, col = i % 8, foo = 1;
        for (int j = -1; j <= 1; j += 2) {                                                       // j is -1 or 1
            try {
                while (" ".equals(board[row][col + foo*j])) {                                                    // if vert spot is empty it executes
                    previous = board[row][col + foo*j];                                                       // sets temporary variable  to be  whatever is the spot going to
                    board[row][col] = " ";                                                                  // makes current loc an empty string
                    board[row][col + foo*j] = "r";                                                          // inflates new loc with Rook
                    if (noBlackKingThreat() == true) {
                        out = out + row + col + row + (col + foo*j) + previous;                                          // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                    }
                    board[row][col] = "r";                                                                  // resets the board with new piece location  (if the king is under threat)
                    board[row][col + foo*j] = previous;                                                     //resets spot to the previous (if the king is under threat)
                    foo++;                                                                                        //keeps increasing spot until it hits a piece or reaches end of board
                }
                if (Character.isUpperCase(board[row][col + foo*j].charAt(0))) {                                  // if vert spot is white it executes
                    previous = board[row][col + foo*j];                                               // sets temporary variable  to be  whatever is the spot going to
                    board[row][col] = " ";                                                                  // makes current loc an empty string
                    board[row][col + foo*j] = "r";                                                                  // inflates new loc with Rook
                    if (noBlackKingThreat() == true) {
                        out = out + row + col + row + (col + foo*j) + previous;                                          // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                    }
                    board[row][col] = "r";                                                      // resets the board with new piece location  (if the king is under threat)
                    board[row][col + foo*j] = previous;                                          //resets spot to the previous (if the king is under threat)
                }
            } catch (Exception e) {}
            foo = 1;                                                                            //resets our temporary variable that counts spots
            try {
                while (" ".equals(board[row + foo*j][col])) {                                        // if horizontal spot is empty it executes
                    previous = board[row + foo*j][col];                                               // sets temporary variable  to be  whatever is the spot going to
                    board[row][col] = " ";                                                              // makes current loc an empty string
                    board[row + foo*j][col] = "r";                                                      // inflates new loc with Rook
                    if (noBlackKingThreat() == true) {
                        out = out + row + col + (row + foo*j) + col + previous;                                          // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                    }
                    board[row][col] = "r";                                                                  // resets the board with new piece location  (if the king is under threat)
                    board[row + foo*j][col] = previous;                                                     //resets spot to the previous (if the king is under threat)
                    foo++;                                                                                    //keeps increasing spot until it hits a piece or reaches end of board
                }
                if (Character.isUpperCase(board[row + foo*j][col].charAt(0))) {                                 // if horizontal spot is white it executes
                    previous = board[row + foo*j][col];                                                       // sets temporary variable  to be  whatever is the spot going to
                    board[row][col] = " ";                                                                      // makes current loc an empty string
                    board[row + foo*j][col] = "r";                                                                      // inflates new loc with Rook
                    if (noBlackKingThreat() == true) {
                        out = out + row + col + (row + foo*j) + col + previous;                                          // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                    }
                    board[row][col] = "r";                                                                  // resets the board with new piece location  (if the king is under threat)
                    board[row + foo*j][col] = previous;                                                     //resets spot to the previous (if the king is under threat)
                }
            } catch (Exception e) { }
        }
        return out;
    }

    public static String possWhiteBishop(int i) {                                                    // checks all of the possible moves for white bishops.
        String out = " ", previous;
        int row = i / 8, col = i % 8, foo = 1;
        for (int j = -1; j <= 1; j += 2) {                                                      //j = -1 or 1
            for (int k = -1; k <= 1; k += 2) {                                                      //k is = -1 or 1
                if (j != 0 && k != 0) {                                                             //ensures move is a bishop move.. not entirely necessary
                    try {
                        while (" ".equals(board[row + foo*j][col + foo*k])) {                               //if diagonal location increase/decrease is empty it executes
                            previous = board[row + foo*j][col + foo*k];                                   // sets temporary variable  to be  whatever is the spot going to
                            board[row][col] = " ";                                                          // makes current loc an empty string
                            board[row + foo*j][col + foo*k] = "B";                                          // inflates new loc with Bishop
                            if (noWhiteKingThreat() == true) {
                                out = out + row + col + (row + foo*j) + (col + foo*k) + previous;                                          // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                            }
                            board[row][col] = "B";                                                          // resets the board with new piece location  (if the king is under threat)
                            board[row + foo*j][col + foo*k] = previous;                                     //resets spot to the previous (if the king is under threat)
                            foo++;                                                                                    //keeps increasing spot until it hits a piece or reaches end of board
                        }
                        if (Character.isLowerCase(board[row + foo*j][col + foo*k].charAt(0))) {                    //if diagonal location increase/decrease is black it executes
                            previous = board[row + foo*j][col + foo*k];                                       // sets temporary variable  to be  whatever is the spot going to
                            board[row][col] = " ";                                                              // makes current loc an empty string
                            board[row + foo*j][col + foo*k] = "B";                                                  // inflates new loc with Bishop
                            if (noWhiteKingThreat() == true) {
                                out = out + row + col + (row + foo*j) + (col + foo*k) + previous;                                          // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                            }
                            board[row][col] = "B";                                                           // resets the board with new piece location  (if the king is under threat)
                            board[row + foo*j][col + foo*k] = previous;                                      //resets spot to the previous (if the king is under threat)
                        }
                    } catch (Exception e) {     }
                    foo = 1;                                                                    //resets our temporary variable that counts spots
                }
            }
        }
        return out;
    }

    public static String possBlackBishop(int i) {                                        // checks all of the possible moves for black bishops
        String out = " ", previous;
        int row = i / 8, col = i % 8, foo = 1;
        for (int j = -1; j <= 1; j += 2) {                                           //j = -1 or 1
            for (int k = -1; k <= 1; k += 2) {                                       //k = -1 or 1
                if (j != 0 && k != 0) {
                    try {
                        while (" ".equals(board[row + foo*j][col + foo*k])) {                       //if diagonal location increase/decrease is empty it executes
                            previous = board[row + foo*j][col + foo*k];                                  // sets temporary variable  to be  whatever is the spot going to
                            board[row][col] = " ";                                                  // makes current loc an empty string
                            board[row + foo*j][col + foo*k] = "b";                                      // inflates new loc with Bishop
                            if (noBlackKingThreat() == true) {
                                out = out + row + col + (row + foo*j) + (col + foo*k) + previous;                                          // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                            }
                            board[row][col] = "b";                                              // resets the board with new piece location  (if the king is under threat)
                            board[row + foo*j][col + foo*k] = previous;                         //resets spot to the previous (if the king is under threat)
                            foo++;                                                                    //keeps increasing spot until it hits a piece or reaches end of board
                        }
                        if (Character.isUpperCase(board[row + foo*j][col + foo*k].charAt(0))) {                //if diagonal location increase/decrease is white it executes
                            previous = board[row + foo*j][col + foo*k];                                   // sets temporary variable  to be  whatever is the spot going to
                            board[row][col] = " ";                                                          // makes current loc an empty string
                            board[row + foo*j][col + foo*k] = "b";                                              // inflates new loc with Bishop
                            if (noBlackKingThreat() == true) {
                                out = out + row + col + (row + foo*j) + (col + foo*k) + previous;                                          // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                            }
                            board[row][col] = "b";                                                  // resets the board with new piece location  (if the king is under threat)
                            board[row + foo*j][col + foo*k] = previous;                             //resets spot to the previous (if the king is under threat)
                        }
                    } catch (Exception e) { }
                    foo = 1;                                                        //resets our temporary variable that counts spots
                }
            }
        }
        return out;
    }


    public static String possWhiteQueen(int i) {                                          // checks all of the possible moves for white queen
        String out = " ", previous;
        int row = i / 8, col = i % 8, foo = 1;
        for (int j = -1; j <= 1; j++) {                                                     // j = -1
            for (int k = -1; k <= 1; k++) {                                                   // k= -1
                if (j != 0 || k != 0) {                                                         // if one of these is zero queen move
                    try {
                        while (" ".equals(board[row + foo*j][col + foo*k])) {                                   //if empty tile in any direction it executes
                            previous = board[row + foo*j][col + foo*k];                                       // sets temporary variable  to be  whatever is the spot going to
                            board[row][col] = " ";                                                             // makes current loc an empty string
                            board[row + foo*j][col + foo*k] = "Q";                                          // inflates new loc with Queen
                            if (noWhiteKingThreat() == true) {
                                out = out + row + col + (row + foo*j) + (col + foo*k) + previous;    // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                            }
                            board[row][col] = "Q";                                                          // resets the board with new piece location  (if the king is under threat)
                            board[row + foo*j][col + foo*k] = previous;                                     //resets spot to the previous (if the king is under threat)
                            foo++;                                                                                    //keeps increasing spot until it hits a piece or reaches end of board
                        }
                        if (Character.isLowerCase(board[row + foo*j][col + foo*k].charAt(0))) {              //if black piece  in any direction it executes
                            previous = board[row + foo*j][col + foo*k];                                   // sets temporary variable  to be  whatever is the spot going to
                            board[row][col] = " ";                                                          // makes current loc an empty string
                            board[row + foo*j][col + foo*k] = "Q";                                          // inflates new loc with Queen
                            if (noWhiteKingThreat() == true) {
                                out = out + row + col + (row + foo*j) + (col + foo*k) + previous;       // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                            }
                            board[row][col] = "Q";                                                  // resets the board with new piece location  (if the king is under threat)
                            board[row + foo*j][col + foo*k] = previous;                             //resets spot to the previous (if the king is under threat)
                        }
                    } catch (Exception e) { }
                    foo = 1;                                                            //resets our temporary variable that counts spots
                }
            }
        }
        return out;
    }

    public static String possBlackQueen(int i) {                              // checks all of the possible moves for black queen
        String out = " ", previous;
        int row = i / 8, col = i % 8, foo = 1;
        for (int j = -1; j <= 1; j++) {                         //j is -1 or 1
            for (int k = -1; k <= 1; k++) {                      // k is -1 or 1
                if (j != 0 || k != 0) {                             //queen move ensured
                    try {
                        while (" ".equals(board[row + foo*j][col + foo*k])) {                                //if empty string in any direction it executes
                            previous = board[row + foo*j][col + foo*k];                                   // sets temporary variable  to be  whatever is the spot going to
                            board[row][col] = " ";                                                          // makes current loc an empty string
                            board[row + foo*j][col + foo*k] = "q";                                              // inflates new loc with Queen
                            if (noBlackKingThreat() == true) {
                                out = out + row + col + (row + foo*j) + (col + foo*k) + previous;                                          // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                            }
                            board[row][col] = "q";                                                          // resets the board with new piece location  (if the king is under threat)
                            board[row + foo*j][col + foo*k] = previous;                                     //resets spot to the previous (if the king is under threat)
                            foo++;                                                                            //keeps increasing spot until it hits a piece or reaches end of board
                        }
                        if (Character.isUpperCase(board[row + foo*j][col + foo*k].charAt(0))) {                  //if white piece in any direction it executes
                            previous = board[row + foo*j][col + foo*k];                                       // sets temporary variable  to be  whatever is the spot going to
                            board[row][col] = " ";                                                              // makes current loc an empty string
                            board[row + foo*j][col + foo*k] = "q";                                              // inflates new loc with Queen
                            if (noBlackKingThreat() == true) {
                                out = out + row + col + (row + foo*j) + (col + foo*k) + previous;                                          // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                            }
                            board[row][col] = "q";                                                          // resets the board with new piece location  (if the king is under threat)
                            board[row + foo*j][col + foo*k] = previous;                                     //resets spot to the previous (if the king is under threat)
                        }
                    } catch (Exception e) { }
                    foo = 1;                                                                        //resets our temporary variable that counts spots
                }
            }
        }
        return out;
    }


    public static String possWhiteKing(int i) {                                       // checks all of the possible moves for white king
        String out = " ", previous;
        int row = i / 8, col = i % 8;
        for (int j = 0; j < 9; j++) {                                       // checks the 9 squares that surround and include the king
            if (j != 4) {                                       // excludes the tile the king is standing on
                try {
                    if (Character.isLowerCase(board[row - 1 + j / 3][col - 1 + j % 3].charAt(0)) || " ".equals(board[row - 1 + j / 3][col - 1 + j % 3])) {      // if space around king is back or empty it executes
                        previous = board[row - 1 + j / 3][col - 1 + j % 3];                                       // sets temporary variable  to be  whatever is the spot going to
                        board[row][col] = " ";                                                                          // makes current loc an empty string
                        board[row - 1 + j / 3][col - 1 + j % 3] = "K";                                                  // inflates new loc with King
                        int kingFoo = whiteKing;                                                                    //pulls the loc of the white king
                        whiteKing = i + (j / 3) * 8 + j % 3 - 9;                                                    //determines the poss king moves
                        if (noWhiteKingThreat() == true) {
                            out = out + row + col + (row - 1 + j / 3) + (col - 1 + j % 3) + previous;                                          // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                        }
                        board[row][col] = "K";                                                                  // resets the board with new piece location  (if the king is under threat)
                        board[row - 1 + j / 3][col - 1 + j % 3] = previous;                                     //resets spot to the previous (if the king is under threat)
                        whiteKing = kingFoo;                                                               //resets our temp variable
                    }

                } catch (Exception e) {  }
            }

        }
        return out;
    }


    public static String possBlackKing(int i) {                                                   // checks all of the possible moves for black king
        String out = " ", previous;
        int row = i / 8, col = i % 8;
        for (int j = 0; j < 9; j++) {               // checks the 9 squares that surround and include the king
            if (j != 4) {                               // excludes the tile the king is standing on
                try {
                    if (Character.isUpperCase(board[row - 1 + j / 3][col - 1 + j % 3].charAt(0)) || " ".equals(board[row - 1 + j / 3][col - 1 + j % 3])) {         // if space around king is black or empty it executes
                        previous = board[row - 1 + j / 3][col - 1 + j % 3];                                       // sets temporary variable  to be  whatever is the spot going to
                        board[row][col] = " ";                                                                      // makes current loc an empty string
                        board[row - 1 + j / 3][col - 1 + j % 3] = "k";                                              // inflates new loc with King
                        int kingFoo = blackKing;                                                                //pulls the location of the black king
                        blackKing = i + (j / 3) * 8 + j % 3 - 9;                                                          //determines the poss king moves
                        if (noBlackKingThreat() == true) {
                            out = out + row + col + (row - 1 + j / 3) + (col - 1 + j % 3) + previous;                                          // if legal: row from (0), col from (1) , row to (2) , col to (3) , captured piece or empty string(4)
                        }
                        board[row][col] = "k";                                                                      // resets the board with new piece location  (if the king is under threat)
                        board[row - 1 + j / 3][col - 1 + j % 3] = previous;                                         //resets spot to the previous (if the king is under threat)
                        blackKing = kingFoo;                                                       // resets our temp variable
                    }

                } catch (Exception e) { }
            }

        }
        return out;
    }

    public static boolean noWhiteKingThreat() {

        int foo = 1;               // checks diagonals from king position for threat from BISHOP OR QUEEN
        for (int i = -1; i <= 1; i += 2) {                                                // i  = -1 and 1
            for (int j = -1; j <= 1; j += 2) {                                            // j  = -1 and 1
                try {
                    while (" ".equals(board[whiteKing / 8 + foo*i][whiteKing % 8 + foo*j])) {
                        foo++;                                                                //keeps increasing spot until it finds a piece or reaches end of board
                    }
                    if ("b".equals(board[whiteKing / 8 + foo*i][whiteKing % 8 + foo*j]) || "q".equals(board[whiteKing/8 + foo*i][whiteKing%8 + foo*j])) {    // if a black bishop or a queen is found on diagonal king is in check
                        return false;
                    }
                } catch (Exception e) { }
                foo = 1;                                                                    //resets our temporary variable that counts spots
            }
        }

        for (int i = -1; i <= 1; i += 2) {            // checks horiz/vert from king position for threat from ROOK OR QUEEN
            try {
                while (" ".equals(board[whiteKing / 8][whiteKing%8 + foo*i])) {
                    foo++;                                                                                     //keeps increasing spot until it finds a piece or reaches end of board
                }
                if ("r".equals(board[whiteKing/8][whiteKing%8 + foo*i]) || "q".equals(board[whiteKing/8][whiteKing%8 + foo*i])) {
                    return false;
                }
            } catch (Exception e) {}
            foo = 1;                                                                                //resets our temporary variable that counts spots
            try {
                while (" ".equals(board[whiteKing/8 + foo*i][whiteKing%8])) {
                    foo++;                                                                                                     //keeps increasing spot until it finds a piece or reaches end of board
                }
                if ("r".equals(board[whiteKing/8 + foo*i][whiteKing%8]) || "q".equals(board[whiteKing/8 + foo*i][whiteKing%8])) {
                    return false;
                }
            } catch (Exception e) {}
            foo = 1;                                                                               //resets our temporary variable that counts spots
        }

        //checks if king is threatened by opponents knight
        for (int i = -1; i <= 1; i += 2) {                                                    // i  = -1 and 1
            for (int j = -1; j <= 1; j += 2) {                                            // j  = -1 and 1
                try {
                    if ("n".equals(board[whiteKing/8 + i][whiteKing%8 + j*2])) {            // if a black knight is found in a threatening position kings in check (first four knight cases row move 1 col move 2)
                        return false;
                    }
                } catch (Exception e) {}
                try {
                    if ("n".equals(board[whiteKing/8 + i*2][whiteKing%8 + j])) {                   // if a black knight is found in a threatening position kings in check (second four cases row move 2 col move 1)
                        return false;
                    }
                } catch (Exception e) {}
            }
        }
        //checks if pawns threaten king
        if (whiteKing >= 16) {
            try {
                if ("p".equals(board[whiteKing/8 - 1][whiteKing%8 - 1])) {                      //checks for a black pawn on its left diagonal
                    return false;
                }
            } catch (Exception e) { }
            try {
                if ("p".equals(board[whiteKing/8 - 1][whiteKing%8 + 1])) {                       //checks for a black pawn on its right diagonal
                    return false;
                }
            } catch (Exception e) {}
        }
        //checks if the opponents king is poss threat
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {                                           //for loop sorts through every space around the king except where the king actually is
                    try {
                        if ("k".equals(board[whiteKing/8 + i][whiteKing%8 + j])) {                          //won't let other king get close
                            return false;
                        }
                    } catch (Exception e) {}
                }
            }
        }
        return true;
    }


    public static boolean noBlackKingThreat() {
        //checks threats in diagonal directions (BISHOP/QUEEN )
        int foo = 1;
        for (int i = -1; i <= 1; i += 2) {                                                        // i  = -1 and 1
            for (int j = -1; j <= 1; j += 2) {                                                        // j  = -1 and 1
                try {
                    while (" ".equals(board[blackKing/8 + foo*i][blackKing%8 + foo*j])) {
                        foo++;                                                                                     //keeps increasing spot until it finds a piece or reaches end of board
                    }
                    if ("B".equals(board[blackKing/8 + foo*i][blackKing%8 + foo*j]) || "Q".equals(board[blackKing/8 + foo*i][blackKing%8 + foo*j])) {               //if a white bishop or a queen is found on diagonal king is in check
                        return false;
                    }
                } catch (Exception e) {}
                foo = 1;                                                                    //resets our temporary variable that counts spots
            }
        }
        //checks threats in horizontal/vertical directions (ROOK / QUEEN)
        for (int i = -1; i <= 1; i += 2) {
            try {
                while (" ".equals(board[blackKing/8][blackKing%8 + foo*i])) {
                    foo++;                                                                     //keeps increasing spot until it finds a piece or reaches end of board
                }
                if ("R".equals(board[blackKing/8][blackKing%8 + foo*i])
                        || "Q".equals(board[blackKing/8][blackKing%8 + foo*i])) {                       //if a white rook or a queen is found on diagonal king is in check
                    return false;
                }
            } catch (Exception e) {}
            foo = 1;                                                                    //resets our temporary variable that counts spots
            try {
                while (" ".equals(board[blackKing/8 + foo*i][blackKing%8])) {
                    foo++;                                                                                         //keeps increasing spot until it finds a piece or reaches end of board
                }
                if ("R".equals(board[blackKing/8 + foo*i][blackKing%8]) || "Q".equals(board[blackKing/8 + foo*i][blackKing%8])) {
                    return false;
                }
            } catch (Exception e) {}
            foo = 1;                                                                        //resets our temporary variable that counts spots
        }

        //checks if king threatened by KNIGHT
        for (int i = -1; i <= 1; i += 2) {                                    // i  = -1 and 1
            for (int j = -1; j <= 1; j += 2) {                                    // j  = -1 and 1
                try {
                    if ("N".equals(board[blackKing/8 + i][blackKing%8 + j*2])) {      //if knight on row +- 1 , col +-2  , kings in check
                        return false;
                    }
                } catch (Exception e) {}
                try {
                    if ("N".equals(board[blackKing/8 + i*2][blackKing%8 + j])) {                  //if knight on row +- 2 , col +-1  , kings in check
                        return false;
                    }
                } catch (Exception e) { }
            }
        }
        //checks if PAWNS threaten king
        if (blackKing < 48) {
            try {
                if ("P".equals(board[blackKing/8 + 1][blackKing%8 + 1])) {                  // king left up diag for a white pawn, if so in check
                    return false;
                }
            } catch (Exception e) {
            }
            try {
                if ("P".equals(board[blackKing/8 + 1][blackKing%8 - 1])) {                           // king right up diag for a white pawn, if so in check
                    return false;
                }
            } catch (Exception e) {}
        }
        //checks if the opponents king is poss threat
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {                                                                                  //for loop sorts through every space around the king except where the king actually is
                    try {
                        if ("K".equals(board[blackKing/8 + i][blackKing%8 + j])) {                                       //won't let other king get close
                            return false;
                        }
                    } catch (Exception e) { }
                }
            }
        }
        return true;
    }
}
