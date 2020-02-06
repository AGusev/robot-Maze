import becker.robots.*;

/**
 * CS1A - Assignment 3 - "The Maze" <br>
 * Quarter: Winter<br>
 * THE SUMMARY GOES HERE <br>
 * 
 * @author Aleksandr Gusev
 * @author Victor Troncoso
 */

class MazeBot extends RobotSE
{
   // Instance Variables will be declared and initialized here
   // one each for totalMoves, movesWest, movesEast, movesSouth, and MovesNorth

   public MazeBot(City theCity, int str, int ave, Direction dir, int numThings)
   {
      super(theCity, str, ave, dir, numThings);
   }

   // <-- BIG HINT: You might want to create a new method here called
   // movesCounted() that will count everything it is supposed to by adding to
   // the instance variables before moving, and then use that instead of move()
   // in the NavigateMaze() method

   public void printEverything(int count_total, int count_east, int count_south, int count_west, int count_north)
   {
      System.out.println("You won !!!\n");
      System.out.print("TOTAL NUMBER OF MOVES MADE: " + count_total);
      System.out.println("NUMBER OF TIMES MOVED EAST: " + count_east);
      System.out.println("NUMBER OF TIMES MOVED SOUTH: " + count_south);
      System.out.println("NUMBER OF TIMES MOVED WEST: " + count_west);
      System.out.println("NUMBER OF TIMES MOVED NORTH: " + count_north);
   }

   public void countMoves(int count_total, int count_east, int count_south, int count_west, int count_north)
   {
      if (this.isFacingWest())
         count_west++;
      if (this.isFacingEast())
         count_east++;
      if (this.isFacingSouth())
         count_south++;
      if (this.isFacingNorth())
         count_north++;
      count_total++;
   }

   // The isAtEndSpot() method below is what's called a 'helper method' It
   // exists just to make another command (in this case, NavigateMaze) easier
   // to understand. It does this by replacing some code that otherwise would
   // be in NavigateMaze with it's name, and doing that work here, instead.
   // Declaring it "private" means that only the MazeBot is allowed to call
   // upon it.
   private boolean isAtEndSpot()
   {
      return getAvenue() == 9 && getStreet() == 10;
   }

   // THIS IS THE ONE MAIN METHOD WILL USE TO DO EVERYTHING (ALTHOUGH IT CAN USE
   // OTHER METHODS LIKE isAtEndSpot, ETC)
   public void navigateMaze()
   {
      // While your robot hasn't yet reached the 'end spot', keep navigating
      // through the Maze and doing its thing

      int count_total = 0;
      int count_west = 0;
      int count_east = 0;
      int count_south = 0;
      int count_north = 0;
      int flag = 0; // in case if robot doesn't make a move

      while (!isAtEndSpot())
      {
         flag = 0;
         if (!this.canPickThing())
            if (this.countThingsInBackpack() > 0)
               this.putThing();
         this.turnRight();
         if (this.frontIsClear())
            this.move();
         else
         {
            this.turnLeft();
            if (this.frontIsClear())
               this.move();
            else
            {
               this.turnLeft();
               if (this.frontIsClear())
                  this.move();
               else
                  flag = 1;
            }
         }
         if (flag == 0)
            this.countMoves(count_total, count_east, count_south, count_west, count_north);
      }
      if (!this.canPickThing())
         if (this.countThingsInBackpack() > 0)
            this.putThing();
      this.printEverything(count_total, count_east, count_south, count_west, count_north);
   }
}

   // ###################################################################################################
   // NO NEED TO TOUCH ANYTHING FROM HERE ON DOWN, EXCEPT TO CHANGE NUMBER OF
   // THINGS IN BACKPACK IN MAIN AND ADDING JavaDoc
   // The NavigateMaze() method is already set up and called by don the robot down
   // in main
   // ###################################################################################################
   public class Maze extends Object
   {
       private static void makeMaze(City theCity)
       {
           for (int i = 1; i < 11; i++)
           {
               // north wall
               new Wall(theCity, 1, i, Direction.NORTH);

               // Second to north wall
               if (i <= 9)
                   new Wall(theCity, 1, i, Direction.SOUTH);

               // Third to north wall
               if (i >= 4)
                   new Wall(theCity, 4, i, Direction.SOUTH);

               // south wall
               if (i != 9) // (9, 10, SOUTH), is where the 'exit' is
                   new Wall(theCity, 10, i, Direction.SOUTH);

               // west wall
               if (i != 1) // (1, 1, WEST) is where the 'entrance' is
                   new Wall(theCity, i, 1, Direction.WEST);

               // second to most western wall
               if (i >= 3 && i < 6)
                   new Wall(theCity, i, 6, Direction.WEST);

               // east wall
               new Wall(theCity, i, 10, Direction.EAST);
           }

           // Cul de Sac
           new Wall(theCity, 3, 10, Direction.WEST);
           new Wall(theCity, 3, 10, Direction.SOUTH);

           new Wall(theCity, 2, 8, Direction.WEST);
           new Wall(theCity, 2, 8, Direction.SOUTH);

           new Wall(theCity, 10, 8, Direction.NORTH);
           new Wall(theCity, 10, 9, Direction.EAST);
           new Wall(theCity, 10, 9, Direction.NORTH);
           makeSpiral(theCity, 8, 9, 3);
           new Wall(theCity, 8, 10, Direction.SOUTH);

           makeSpiral(theCity, 10, 5, 4);
       }

       public static void makeSpiral(City theCity, int st, int ave, int size)
       {
           // We start out building the wall northward
           // the walls will be built on the east face of the current
           // intersection
           Direction facing = Direction.EAST;

           while (size > 0)
           {
               int spacesLeft = size;
               int aveChange = 0;
               int stChange = 0;
               switch (facing)
               {
               case EAST:
                   stChange = -1;
                   break;
               case NORTH:
                   aveChange = -1;
                   break;
               case WEST:
                   stChange = 1;
                   break;
               case SOUTH:
                   aveChange = 1;
                   break;
               }

               while (spacesLeft > 0)
               {
                   new Wall(theCity, st, ave, facing);
                   ave += aveChange;
                   st += stChange;
                   --spacesLeft;
               }
               // back up one space
               ave -= aveChange;
               st -= stChange;

               switch (facing)
               {
               case EAST:
                   facing = Direction.NORTH;
                   break;
               case NORTH:
                   facing = Direction.WEST;
                   size--;
                   break;
               case WEST:
                   facing = Direction.SOUTH;
                   break;
               case SOUTH:
                   facing = Direction.EAST;
                   size--;
                   break;
               }
           }
       }

       // ###########################################################################################
       // Main Method
   // ###########################################################################################
   public static void main(String[] args)
   {
      City calgary = new City(12, 12);
      MazeBot don = new MazeBot(calgary, 1, 1, Direction.EAST, 1000); // added 1000 things

      Maze.makeMaze(calgary);

      don.navigateMaze(); // <-- HERE'S WHERE THE NavigateMaze() method is
      // called. NO NEED TO TOUCH AT ALL

   }

}
