/* @formatter:off
 *
 * Dave Rosenberg
 * Comp 2000 - Data Structures
 * Lab: Unordered List App - Grocery Bagger
 * Fall, 2024
 * 
 * Usage restrictions:
 * 
 * You may use this code for exploration, experimentation, and furthering your
 * learning for this course. You may not use this code for any other
 * assignments, in my course or elsewhere, without explicit permission, in
 * advance, from myself (and the instructor of any other course).
 * 
 * Further, you may not post (including in a public repository such as on github)
 * nor otherwise share this code with anyone other than current students in my 
 * sections of this course. Violation of these usage restrictions will be considered 
 * a violation of the Wentworth Institute of Technology Academic Honesty Policy.
 *
 * Do not remove this notice.
 *
 * @formatter:on
 */


package edu.wit.scds.ds.list.unordered.app;

import java.io.BufferedReader ;
import java.io.FileNotFoundException ;
import java.io.FileReader ;

import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.Iterator ;
import java.util.LinkedList ;
import java.util.List ;
import java.util.Scanner ;

/**
 * Driver for the Grocery Bagger application.
 *
 * @author David M Rosenberg
 *
 * @version 0.1 2024-10-17 Initial skeleton
 * 
 * @author Your Name // TODO replace with your full name
 * 
 * @version 1.0 2024-10-18 Initial implementation
 *     <ul>
 *     <li>the work you need to do is marked with TODO and STUB comments
 *     <li>once you complete a task, delete the entire comment (except this one 8~)
 *     <li>your first task is to replace 'Your Name' with your name (not username) in the
 *     second @author tag above (and delete the TODO comment 8~)
 *     <li>NOTE: there can only be one author per class
 *     </ul>
 * 
 * @since 1.0
 */
public class GroceryBagger
    {

    /* @formatter:off
     * 
     * overall algorithm
     * 
     * bagItems() will:
     * 
     * 1: take each grocery item one-by-one in the order they arrive from the conveyor belt and
     *    place it in a compatible grocery bag in the bagging area
     * 2: if all bags have been check and none accepted the item, open a new bag in the bagging
     *    area for it
     * 3: if the bag that accepted the item is now full, move that bag to the shopping cart
     * 4: once all items have been bagged, move all of the remaining bags from the bagging area to
     *    the shopping cart (it would be a shame if you left some of your nicely bagged groceries
     *    at the store 8~)
     * 
     * displayAllBagsContents will:
     * 
     * 1: display the contents of each of the bags in the shopping cart
     * 
     * @formatter:on
     */


    /**
     * bag all the grocery items on the conveyor belt and display the results
     * 
     * @param args
     *     -unused-
     */
    public static void main( String[] args )
        {
        // our groceries will be taken from the conveyor belt and placed in grocery bags in the
        // shopping cart
        
        List<GroceryBag> shoppingCart = null ;

        System.out.printf( "Bagging items:%n%n" ) ;

        // Load the bag(s) from the 'conveyor belt' (list of groceries)
        try // bagging items might cause exceptions
            {
            shoppingCart = bagItems() ;
            }
        catch ( FileNotFoundException e )
            {
            System.out.println( "Can't load groceries: " + e.getMessage() ) ;

            return ;    // get out
            }
        catch ( GroceryBaggerException |
                GroceryItemNotBaggableException |
                InvalidSpecificationException e )
            {
            // should never occur/reach here - indicates a bug
            System.out.println( "Apparently there's a bug here: " +
                                e.getMessage() ) ;
            e.printStackTrace() ;

            return ;    // get out
            }   // end try/catch

        System.out.println( "\n----------\n" ) ;

        // Display the contents of each grocery bag in the shopping cart
        displayAllBagsContents( shoppingCart ) ;


        System.out.println( "\n----------" ) ;

        System.out.println( "\nDone." ) ;

        }   // end main()


    /**
     * bag all the items on the conveyor belt
     * 
     * @return the shopping cart containing the bagged grocery items
     * 
     * @throws FileNotFoundException
     *     if the data file can't be accessed
     */
    private static List<GroceryBag> bagItems() 
                                throws FileNotFoundException
        {
        List<GroceryBag> baggingArea = new LinkedList<>() ;     // bags being filled
        List<GroceryBag> shoppingCart = new ArrayList<>() ;    // bags to return to the caller

        int itemCount = 0 ;
        
        try ( Scanner conveyorBelt = new Scanner( new BufferedReader( new FileReader( "./data/conveyor-belt.data" ) ) ) ; )
            {
            // skip header line
            conveyorBelt.nextLine() ;
            
            // process the grocery items from the conveyor belt
            while ( conveyorBelt.hasNextLine() )
                {
                // get the next item description and use it to instantiate an item object
                String itemSpecifications = conveyorBelt.nextLine() ;
                GroceryItem groceryItem = new GroceryItem( itemSpecifications ) ;
                
                itemCount++ ;

                GroceryBag baggedInBag = null ; // reference to bag the new item was put into

                // find a bag to put it in and do so
                // baggedInBag will reference the bag the item was bagged in or null if not yet bagged
                // TODO implement this
                
                // if we couldn't add the item to an already open bag add it to a new one
                if ( baggedInBag == null )
                    {
                    GroceryBag newBag = new GroceryBag() ;
                    System.out.println( "Opened bag " + newBag.bagId ) ;
                    if ( !baggingArea.add( newBag ) )
                        {
                        // failed to add the bag to the bagging area
                        throw new GroceryBaggerException( "Failed to add bag (" +
                                                          newBag.bagId +
                                                          ") to the bagging area - aborting" ) ;
                        }   // end if()

                    if ( !newBag.add( groceryItem ) )
                        {
                        throw new GroceryBaggerException( "Failed to add " + groceryItem +
                                                          " to an empty grocery bag - aborting" ) ;
                        }

                    // remember which bag the item was added to
                    baggedInBag = newBag ;
                    }

                // display a message that the item was added to the selected bag
                System.out.println( "Added " + groceryItem.name + " to bag " + baggedInBag.bagId ) ;

                // if the bag is now full, move it from the bagging area to the shopping cart
                if ( baggedInBag.isFull() )
                    {
                    System.out.println( "Bag " + baggedInBag.bagId + " is full, moving it from bagging area to shopping cart" ) ;

                    // move the bag from the bagging area to the shopping cart so we stop
                    // checking it
                    
                    // note that we're emulating the real world process but it would be safer to
                    // perform the add() then the remove() so we don't inadvertently lose the bag
                    // TODO implement this - 2 lines of code

                    }

                }   // end while

            }   // end try

        // assertion: the conveyor belt is empty and all grocery items are in grocery bags in either
        // the bagging area or the shopping cart

        System.out.printf( "%nBagged %,d grocery items%n%n", itemCount ) ;

        // move the rest of the grocery bags from the bagging area into the shopping cart
        // TODO implement this
        
        return shoppingCart ;

        }   // end bagItems()
 
    
    /**
     * display the contents of a grocery bag
     * 
     * @param aGroceryBag
     *     the grocery bag to display
     */
    private static void displayGroceryBagContents( GroceryBag aGroceryBag )
        {
        ArrayList<String> bagDescription = new ArrayList<>() ;
        if ( aGroceryBag.hasBreakableItems() )
            {
            bagDescription.add( "breakables" ) ;
            }
        
        if ( aGroceryBag.hasPerishableItems() )
            {
            bagDescription.add( "perishables" ) ;
            }
        
        if ( aGroceryBag.hasRigidItems() )
            {
            bagDescription.add( "rigid items" ) ;
            }
        

        if ( aGroceryBag.hasLightItems() )
            {
            bagDescription.add( "light items" ) ;
            }
        if ( aGroceryBag.hasHeavyItems() )
            {
            bagDescription.add( "heavy items" ) ;
            }
        
        if ( aGroceryBag.hasSmallItems() )
            {
            bagDescription.add( "small items" ) ;
            }
        if ( aGroceryBag.hasLargeItems() )
            {
            bagDescription.add( "large items" ) ;
            }
        
        if ( aGroceryBag.hasSoftItems() )
            {
            bagDescription.add( "soft items" ) ;
            }
        if ( aGroceryBag.hasHardItems() )
            {
            bagDescription.add( "hard items" ) ;
            }
        
        if ( aGroceryBag.isFull() )
            {
            bagDescription.add( "is full" ) ;
            }
        
        System.out.printf( "Bag %,d contains %,d item%s (%s):%n",
                           aGroceryBag.bagId,
                           aGroceryBag.getItemCount(),
                           ( aGroceryBag.getItemCount() == 1 ? "" : "s" ),
                           String.join( ", ", bagDescription ) ) ;

        GroceryItem[] baggedGroceryItems = aGroceryBag.toArray() ;
        GroceryItem previousItem = null ;  // initially no previous item - used to skip duplicates

        Arrays.sort( baggedGroceryItems ) ;
        
        for( GroceryItem groceryItem : baggedGroceryItems )
            {
            if ( ( previousItem != null ) &&        // if this is either the first item or 
                 ( groceryItem.equals( previousItem ) ) )  // it is the same as the previous item
                {
                // skip duplicate items
                }
            else    // display the item and the quantity in the bag
                {
                System.out.printf( "(%d) %s%n",
                                   aGroceryBag.getNumberOf( groceryItem ),
                                   groceryItem ) ;
                }   // end if/else
            
            previousItem = groceryItem ;   // remember this item so we can skip duplicates
            
            }   // end for()
        
        }   // end method displayGroceryBagContents()
 
    
    /**
     * display the contents of all grocery bags
     * 
     * @param groceryBags
     *     a collect of grocery bags
     */
    private static void displayAllBagsContents( List<GroceryBag> groceryBags )
        {
        System.out.printf( "Loaded %,d bag%s:%n",
                           groceryBags.size(), 
                           ( groceryBags.size() == 1 ? "" : "s" ) ) ;

        // put the grocery bags in order by bag id for display
        groceryBags.sort( null ) ;
        
        int itemCount = 0 ;

        for ( GroceryBag aGroceryBag : groceryBags )
            {
            System.out.println() ;
            
            itemCount += aGroceryBag.getItemCount() ;

            displayGroceryBagContents( aGroceryBag ) ;
            }   // end for()
        
        System.out.printf( "%nBagged %,d grocery items%n", itemCount ) ;
        
        }   // end displayAllBagsContents()
    
    }   // end class GroceryBagger