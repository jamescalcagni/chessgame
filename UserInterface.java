import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class UserInterface extends JPanel implements MouseListener, MouseMotionListener {       //This class contains our GUI and everything the user needs to interface with the chess board
    static int tile = 58, X, Y, newX, newY;

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);                        //need the paint component super class
        this.setBackground(Color.BLACK);                // sets the background to black. dont think its necessary because i made it non-resizable but jic
        this.addMouseListener(this);                //so user can use the mouse
        this.addMouseMotionListener(this);

        for ( int i  =0; i < 64; i += 2){
            setVisible(true);                                                               //makes visible
            setSize(900,900) ;                                                 // sets the size of the board
            g.setColor(new Color(52, 235, 183));                                            // sets the color for the lighter tiles ( a lightish green color)
            g.fillRect((i%8 + (i/8)%2)*tile, (i/8)*tile, tile, tile);                           //creates the light tiles (50% of the board)
            g.setColor(new Color(235, 64, 52));                                     //sets the color for the dark tiles ( reddish color)
            g.fillRect(((i+1)%8 - (i/8)%2)*tile, ((i+1)/8)*tile, tile, tile);                   //creates the dark tiles(other 50% of the board)
        }
        Image chessPieceArray = new ImageIcon("ChessPieces.jpg").getImage();    // Array of Chess icons from online

        for (int i = 0; i < 64; i ++){                        // This switch searches through Image Array of Chess Pieces downloaded from Wikipedia and returns coordinates of piece
            int j = -1, k = -1;
            switch (ChessGame.board[i / 8][i % 8]) {
                case "K":                           //White king piece image
                    j = 0;
                    k = 0;
                    break;
                case "k":                            //Black king piece image
                    j = 0;
                    k = 1;
                    break;
                case "Q":                            //White Queen piece image
                    j = 1;
                    k = 0;
                    break;
                case "q":                           //Black queen piece image
                    j = 1;
                    k = 1;
                    break;
                case "R":                            //White Rook piece image
                    j = 2;
                    k = 0;
                    break;
                case "r":                               //Black rook piece image
                    j = 2;
                    k = 1;
                    break;
                case "B":                                //White Bishop piece image
                    j = 3;
                    k = 0;
                    break;
                case "b":                               //Black bishop piece image
                    j = 3;
                    k = 1;
                    break;
                case "N":                                //White Knight piece image   **note: use N not K
                    j = 4;
                    k = 0;
                    break;
                case "n":                                   //Black Knight piece image
                    j = 4;
                    k = 1;
                    break;
                case "P":                                    //White pawn piece image
                    j = 5;
                    k = 0;
                    break;
                case "p":                                   ////Black pawn piece image
                    j = 5;
                    k = 1;
                    break;
            }
            if (j!= -1 && k!= -1){
                g.drawImage(chessPieceArray, (i%8)*tile, (i/8)*tile, (i%8 + 1)*tile,(i/8 + 1)*tile,j*64, k*64,(j+1)*64,(k+1)*64,this) ;    // draws the specific piece that is called upon from the array
            }

        }

    }


    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getX() < 8*tile && e.getY() < 8*tile){                             // gets the X and Y coordinates of where you click
            //if inside the board
            X = e.getX();
            Y = e.getY();
            repaint();                                                  //repaints the board
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {                       // You drag your piece to a tile and drop it on.  When your mouse releases this happens
        if(e.getX() < 8*tile && e.getY() < 8*tile){             //only allows if your mouse is within the frame of the board
            newX = e.getX();
            newY = e.getY();
            String dragpiece ;
            if(e.getButton() == MouseEvent.BUTTON1){                  //when the left mouse button is clicked down
                if(newY/tile == 0 && Y/tile == 1 && "P".equals(ChessGame.board[Y/tile][X/tile])) {                           // Case for white pawn promotion -> changes to a queen  (going from rank 1 to 0)
                    dragpiece = "" + X/tile + newX/tile + ChessGame.board[newY/tile][newX/tile] +"Q" +"P";
                }
                else if(newY/tile == 7 && Y/tile == 6 && "p".equals(ChessGame.board[Y/tile][X/tile])) {                      //case for black pawn promotion  ( going from rank 6 to 7)
                    dragpiece = "" + X/tile + newX/tile + ChessGame.board[newY/tile][newX/tile] +"q" +"p";
                }
                else{
                    dragpiece = "" + Y/tile + X/tile + newY/tile + newX/tile + ChessGame.board[newY/tile][newX/tile];               // case for all other moves
                }

                if(ChessGame.possMoves().replaceAll(dragpiece," ").length() < ChessGame.possMoves().length()){
                    ChessGame.movePiece(dragpiece);
                }

            }
            repaint();                                                      // repaints the board
        }
    }



    //even though we don't use these methods we still need them to avoid an error
    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) { }

    @Override
    public void mouseClicked(MouseEvent e) { }
}
