Author: James Calcagni
Created for: Rutgers CS-111 Summer 2019



--How to Run:
To run the game  download the 3 files (UserInterface.java, ChessGame.java, and ChessPieces.jpg) to the same folder.  
Navigate to that folder in your command line and then run by typing:
	javac ChessGame.java && javac UserInterface.java && java ChessGame 

OR

Simply double click the JAR executable file named CalcagniChess.jar 


--How to play:
White moves first to begin the game.  To move, select the piece you would like to move and while keeping your mouse button
held down, drag and drop the piece onto the tile you intend to move to.  Can also use touchscreen if available.  If it is 
a valid move then your piece will disappear from the tile it was on and re-inflate where you dropped it.  If it is an 
invalid move nothing will happen and you will have to try another move.   Once you make your move it becomes black's turn 
and you will only be able to move the black pieces.


--About my project:

I decided for my project to create a two player Chess game.  I did think this was very ambitious, but I really love chess
and thought that I would be able to do it and was up for a challenge. Once I realized just how hard it was I was already
committed and doubled down.  I am extremely happy I did so, and really like how my project turned out. As I have never
done anything like this before (with the exception of the rock, paper, scissors, and tic-tac-toe games I coded in prepping
for the midterms for this class) which were much smaller scale, I did not originally know where to start and spent many
 hours watching youtube videos and pouring through StackOverflow and what help I could find from various websites online.
 I would say I spent AT LEAST 30% of my time in this planning stage.  As I learned more about the process that goes into
 creating a chess game in Java I realized right away that there are a large variety of ways to go about this.

Initially I started with the actual chess logic and figured out how I could go about making the rules for each piece.  I
started by using a hierarchical inheritance structure; making  a separate subclass for each piece extending from a larger
class "Piece" which held all of the pieces and contained their common characteristics (i.g. can capture, can be captured,
can move, etc).  I do not regret this initial attempt as I was easily able to keep the logic separate and learn the
process of coding a chess game.I then began to try and create the interface that would appear on the screen.

Before this project I had essentially never programmed a graphical user interface, but I determined for a game like chess
it was absolutely necessary.  Despite being a novice to GUI, I found the concepts of JLabel and JPanel to be pretty
straightforward and after a few youtube videos and some practice I got the hang of it.  I began by creating an array of
64 Jbuttons, but later discovered that it was easier for me to just use a switch to sort through an array of images and
place them on a painted Jframe which i designed to look like a chess board.  While this was new to me I found the concept behind gui to
be pretty simple and found it very fun to mess around with once I got my board up and running, and even when my program
wasn't doing what I wanted it to, at least it looked nice.  Once  I had a basic graphical interface and  was  able to put
the chess pieces on the board where they should initially go, I began to try and implement movement of pieces.

I soon discovered that it was very difficult the way my code was set up to use the separate classes, for me I simply
had too much going on and abandoned this initial attempt.  I started from scratch and found that by creating a board
representation using a string array of individual characters (example below) as opposed to the grid layout I was trying to use
before was much simpler for being able to debug my code.  Using this format lowercase letters represented the Black
 Player's pieces and uppercase letters represent the White Player's pieces.

{"r", "n", "b", "q", "k", "b", "n", "r"},
{"p", "p", "p", "p", "p", "p", "p", "p"},
{" ", " ", " ", " ", " ", " ", " ", " "},
{" ", " ", " ", " ", " ", " ", " ", " "},
{" ", " ", " ", " ", " ", " ", " ", " "},
{" ", " ", " ", " ", " ", " ", " ", " "},
{"P", "P", "P", "P", "P", "P", "P", "P"},
{"R", "N", "B", "Q", "K", "B", "N", "R"}


Once I had this string array basic format I was able to create move generation.  Each move is made by reading a 5 character string
with the first char being an integer holding the row the piece is moving from, the second char the column the piece is moving
from, the third char is the row it is moving to, the fourth the column it is moving to, and finally the fifth returns the
letter of the captured piece (if there is one).  At this point I was able to use my earlier chess logic from the classes
I had initially tried to program and I used them to create a strings called possMoves which returns a string of every
possible move available iterating through each piece per its specific rules (i.g. bishop only moves diagonally, king moves
all direction but only one space, pawn moves forward once and can go twice on first move, pawn attack diagonaletc, etc).  It
does this by calling a switch statement which iterates through every tile and then if it has the given piece calls a new
individual string dependant on each piece (possBishop, possPawn, possRook, possKing, etc).


Since the goal of chess is to protect your king, I created a boolean noKingThreat (actually two, one for white and one
for black)  which determines whether the king is under a direct threat from an opponents piece.  It does this by
incorporating a little bit from all  the other pieces.  (I have noticed before that in rare instances the game might allow you to 
make a move that is not legal as your king is in Check, so be vigilant when playing.  As of this date I have not been able to 
recreate this scenario, so if you find such a situation and can help me diagnose them please let me know!)

noKingThreat checks the diagonals searching for bishops or queens of the opposite color, the vertical and  horizontal for rooks 
and queens, all 8 possible knight positions, the two diagonal spots which would put it under threat by pawn, and finally the rare  
case of the other king being nearby (only really comes up in endgame scenarios). I then incorporated this boolean into the other 
movements with if statements such that you can only move so long as the move you are making does not put your king is  under threat 
and so that if the king is under threat you have to move so that you no longer in check.  My code does not determine if there is a 
checkmate, however if you are in checkmate it will not allow you to make any more moves so coding this would be semi-redundant as 
this is the end of the game.

Likewise, my code does not check the case for a stalemate but with the same logic if given a stalemate the player whose
turn it is will not be able to make any moves and therefore they should be able to determine that for themselves and
exit the game.  Also, I did not account for certain advanced special chess moves like "castling" or "en pessent", (I believe I could
have coded this I just did not have the time/motivation). However, I did code the special case for a "pawn promotion" which deals
with the case when a pawn reaches the other end of the board and is promoted to a new status- both for when a pawn captures a piece on
the last row or when it simply walks straight onto the last row.  NOTE: normally in pawn promotion you can choose any piece besides a
king, but my program just automatically changes you to a queen when you reach  the other side of the board which is likely what you
would want anyway except in rare cases where a knight could be more useful, but I decided just to do it this way.


At this point in my coding I went back to my UserInterface class and added Mouse Click events so that I could move the
pieces on the board.  The way the interface works is you click the piece you want to move and drag it to the tile you
want to move it to and then release on that tile.  If your mouse release is on a tile that is a valid move, then the
program executes the move. If a piece of the opposite color is on the tile you dragged to and it is a valid move then
the opponents piece is captured, and if a piece of the same color is on the tile you dragged to or it is an invalid
move nothing happens.  I spent a while trying to make it so you could see the piece you selected being dragged with your
mouse drag, but with my limited experience in GUI I decided it is fine the way it is.

I feel that I only spent about 20% of the time writing my code and at least 50% of my time debugging(probably more).  I did
not particularly enjoy the debugging process (particularly the 2 hours I spent searching the same 3 lines of code for an
error when it turned out to be a single space " "..) but after solving my problems the rewarding feeling was very worth it.

I went from having over 10 classes to just two, and though I know I could have used separate classes as I initially intended,
 the way my code is written is much easier for me to understand.  This is my first ever computer science/programming class
 although I have tried coding on my own before, and I feel very  relieved to have finished this complex project.  I
have never taken on a project of this size before and the many hours I put into it definitely paid off.  I believe that
I can also further adapt this chess game to create a larger multiplayer game with a larger playing field and various components
like obstacles, weapons, etc.  I can imagine many different ways to build upon this program, and to me that is the most
exciting part.  I am a mechanical engineering major but this class has caused me to strongly consider a minor in CS!!

I hope you enjoy!

:)

--Chess Engine:
I spent a while toying with the idea of creating a computer chess engine to play against the player.  While at first that
sounded daunting, after looking into it I do think that given more time I would be able to pretty easily implement a
basic chess engine into my program using a minmax algorithm and  the concepts we learned in class.  However,
I simply did not have the time nor motivation to implement this.  I do plan to do so in the future.


--Sources:
Array of Chess Pieces:  https://commons.wikimedia.org/wiki/Category:PNG_chess_pieces/Standard_transparent#/media/File:ChessPiecesArray.png
{this is the first result that comes up when you search "chess pieces jpg download" and as an array is very useful so I don't need to use 12 different image files which is what I did at first}

Youtube channels I found  helpful:
-> Software Architecture & Design: https://www.youtube.com/channel/UCwfB9D8Q4DtyMr2POU9yGNQ
->Logic Crazy Chess:  https://www.youtube.com/user/jonathanwarkentin/playlists
      -  His playlist "Programming a Simple Java Chess Engine"  was the inspiration for my decision to use a string array as opposed to the grid layout I had been using prior
      - easily my most used/most helpful resource
-> The Happie Cat:  https://www.youtube.com/user/TheHappieCat/
-> Almas Baimagambetov: https://www.youtube.com/channel/UCmjXvUa36DjqCJ1zktXVbUA/
-> Mr. Soderquist: https://www.youtube.com/channel/UCoL-30L7NMRNzwiHUqSYCVQ/featured
-> Derrf Seitz:  https://www.youtube.com/channel/UCjPXMVwp9ms9qBMFgpzjXgQ
       - Great video for GUI
->TechQuerry: https://www.youtube.com/channel/UC9y4E0KEY7IbEPN9HfpFxrQ
->Just Nobody: https://www.youtube.com/user/AhmadIftikhar17

Other websites I stumbled across:
-> Git Hub: https://github.com/psoder3/Program-Your-Own-Chess , many more
-> Stack exchange: (heres at least one link i looked over, may be more) https://codereview.stackexchange.com/questions/71790/design-a-chess-game-using-object-oriented-principles
-> Chess.com: https://www.chess.com/forum/view/general/lets-write-a-chess-gui-from-scratch-in-java
-> Harmonia Philosiphica: https://harmoniaphilosophica.com/2018/07/23/how-to-code-a-chess-program-in-one-day-c-and-java-examples/
-> Coderanch.com
-> probably many more...


--Built using IntelliJ
