import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The class for all players in this very real video game
 */
public class Player implements Contract {

    /**
     * @random a random number generator
     * @inventory an arraylist of strings that contains the players inventory
     * @lastItemDropped a string of the last item the player dropped to they can get it back when they use undo()
     * @inventorySpace an int that represents the max number of items the player can carry
     * @xPos the players current x position
     * @yPos the players current y position
     * @edibleItems a string array of all of the possible items the player can eat without powering down
     */
    static Random random;
    ArrayList<String> inventory;
    String lastItemDropped;
    int inventorySpace;
    int currentPower;
    int xPos;
    int yPos;
    String[] edibleItems;


    /**
     * The constructor for the player class
     * @param inventorySpace an int that represents how many items can be kept in the players inventory
     * @param edibleItems a string[] of all of the items the player can eat and gain power from
     */
    public Player(int inventorySpace, String[] edibleItems){
        random = new Random();
        this.inventory = new ArrayList<String>();
        this.inventorySpace = inventorySpace;
        this.currentPower = 2;
        this.xPos = 0;
        this.yPos = 0;
        this.edibleItems = edibleItems;
        lastItemDropped = "";
    }

    /**
     * A method to grab items off of the ground
     * Throws a runtime exception if the inventory is full
     * @param item a string of the item that will be added
     */
    public void grab(String item){
        if(this.inventory.size() >= this.inventorySpace){
            throw new RuntimeException("You cant pick up any more items since your inventory is full");
        }
        System.out.println("You picked up the "+item+" and added it to your inventory");
        this.inventory.add(item);
    }

    /**
     * A method to drop items from the inventory
     * Throws a runtime exception if the item is not in the inventory
     * @param item the item that will be dropped
     * @return the item that was dropped
     */
    public String drop(String item){
        if(!this.inventory.contains(item)){
            throw new RuntimeException("You tried to drop "+item+" but it isn't in your inventory");
        }
        System.out.println("The "+item+" was discarded");
        this.lastItemDropped = item;
        this.inventory.remove(item);
        return item;
    }

    /**
     * A method to look for an item in the inventory
     * @param item the item that will be looked for
     * Doesn't return anything, only prints whether or not the item was found to the player
     */
    public void examine(String item){
        System.out.println("You searched your inventory for "+item);
        if(this.inventory.contains(item)){
            System.out.println("You found it!");
        }
        else{
            System.out.println("But it wasn't there");
        }
    }
    
    /**
     * A method to use an item from the inventory
     * Throws a runtime exception if the item is not in the inventory
     * @param item the item that will be used
     * it the item is edible, the player will grow, if not, the player will shrink
     * the item is then removed from the players inventory
     */
    public void use(String item){
        if(!this.inventory.contains(item)){
            throw new RuntimeException("You tried to use the "+item+", but it wasn't in your inventory");
        }
        System.out.println("You ate the "+item);
        if(Arrays.asList(this.edibleItems).contains(item)){
            System.out.println("It tastes great!");
            this.grow();
        }
        else{
            System.out.println("It tastes rancid!");
            this.shrink();
        }
        this.inventory.remove(item);
    }

    /**
     * A method to move the player
     * Has a chance of the player tripping and falling, so they will not move
     * @param direction the direction the player will move, can be any string but the move will only be successful if Up, Down, Left, or Right is passed in
     * if one of the valid moves is passed in, the x and y position will be updated accordingly
     * @return returns whether or not the player moved successfully
     */
    public boolean walk(String direction){
        boolean canWalk = random.nextBoolean();
        if(!canWalk){
            System.out.println("You tried walking "+direction+" but you tripped and fell");
        }
        else if(!direction.equals("Left") && !direction.equals("Right") && direction.equals("Up") && direction.equals("Down")){
            System.out.println("You tried moving "+direction+", but then realized you don't know what direction that is");
            canWalk = false;
        }
        else{
            System.out.println("You successfully walked "+direction);
            switch (direction) {
                case "Left":
                    xPos--;
                    break;
                case "Right":
                    xPos++;
                    break;
                case "Up":
                    yPos++;
                    break;
                case "Down":
                    yPos--;
                    break;
            }
            System.out.println("You are now at X:"+xPos+" Y:"+yPos);
        }
        return canWalk;
    }

    /**
     * A method to teleport the player
     * @param x the x position that the player will be teleported to
     * @param y the y position that the player will be teleported to
     * @return returns true if the player teleported, returns false if the player was already at the point they tried to teleport to
     */
    public boolean fly(int x, int y){
        if(this.xPos == x && this.yPos == y){
            System.out.println("You wanted to fly to X:"+x+" Y:"+y+"... but you are already here!");
            return false;
        }
        this.xPos = x;
        this.yPos = y;
        System.out.println("You flew to the position X:"+x+" Y:"+y);
        return true;
    }

    /**
     * A method to power down the player
     * Reduces the players power by one
     * Throws a runtime exception if the player is at or below 0 power
     * @return the current power of the player
     */
    public Number shrink(){
        this.currentPower--;
        System.out.println("You powered down! \nYou now have "+this.currentPower+" power");
        if(this.currentPower <= 0){
            throw new RuntimeException("You died! Game over");
        }
        return this.currentPower;
    }

    /**
     * A method to power up the player
     * Increases the players power by one
     * @return the current power of the player
     */
    public Number grow(){
        this.currentPower++;
        System.out.println("You powered up! \nYou now have "+this.currentPower+" power");
        return this.currentPower;
    }
    
    /**A method to restore the players health'
     * Returns the player to 2 power if they are below 2 power 
     */
    public void rest(){
        System.out.println("You rested at a campfire for the night");
        if(this.currentPower < 2){
            System.out.println("Your power was restored to 2");
            this.currentPower = 2;
        }
    }

    /**Allows the player to get back the last item they dropped
     */
    public void undo(){
        if(this.lastItemDropped.isEmpty()){
            throw new RuntimeException("You have not yet dropped an item");
        }
        System.out.println("You decided "+this.lastItemDropped+" was actually worth keeping");
        this.grab(this.lastItemDropped);
    }


    public static void main(String[] args) {
        Player player = new Player(3, new String[]{"Brownies", "Butterscotch Pie", "Pizza"});
        // player.undo();
        // player.drop("TTTTTT");
        // player.grab("Sword");
        // player.examine("Sword");
        // player.drop("Sword");
        // player.examine("Sword");
        // player.undo();
        // player.examine("Sword");
        // player.walk("Left");
        // player.walk("Left");
        // player.walk("Up");
        // player.walk("Up");
        // player.walk("Down");
        // player.walk("Down");
        // player.walk("Right");
        // player.walk("Right");
        // player.fly(10, 10);
        // player.fly(10, 10);
        // player.grab("Sword");
        // player.grab("Sword");
        // player.grab("Sword");
        // player.use("Sword");
        // player.use("Sword");
        // player.use("Sword");
        // player.rest();
        // player.grab("null");
        // player.use("null");

    }
}
