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


package edu.wit.scds.ds.list.unordered.app ;


import static edu.wit.scds.ds.list.unordered.app.GroceryBagLimits.GROCERY_BAG_MAX_ITEM_COUNT ;
import static edu.wit.scds.ds.list.unordered.app.GroceryBagLimits.GROCERY_BAG_MAX_VOLUME ;
import static edu.wit.scds.ds.list.unordered.app.GroceryBagLimits.GROCERY_BAG_MAX_WEIGHT ;
import static edu.wit.scds.ds.list.unordered.app.GroceryBaggerCompatibilityChecker.* ;

import edu.wit.scds.ds.list.unordered.UnorderedListInterface ;
import edu.wit.scds.ds.list.unordered.adt.ArrayBag ;

import java.util.Arrays ;
import java.util.Objects ;

/**
 * A collection of {@code GroceryItem}s using an unordered list (Bag) as the backing store.
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
public class GroceryBag implements Comparable<GroceryBag>
    {

    /*
     * symbolic constants
     */

    // none


    /*
     * static variables
     */

    private static int bagCount = 0 ;     // used to assign each bag a unique id


    /*
     * data fields
     */

    /** the GroceryBag's unique id for display via toString() and testing */
    public final int bagId ;

    
    // capacity fields - accessible via getters

    private int remainingItemsAvailable ;
    private int remainingWeightAvailable ;
    private int remainingSpaceAvailable ;

    
    // compatibility fields - accessible via getters

    private int breakableItemCount ;
    private int perishableItemCount ;
    private int rigidItemCount ;
    
    // sizes
    private int smallItemCount ;
    private int largeItemCount ;
    
    // weights
    private int lightItemCount ;
    private int heavyItemCount ;
    
    // firmnesses
    private int softItemCount ;
    private int hardItemCount ;

    
    // backing store

    private final UnorderedListInterface<GroceryItem> groceryBag ;

    private boolean integrityOk = false ;


    /**
     * initialize a grocery bag to a valid empty state with specified limits
     */
    public GroceryBag()
        {

        this.bagId = ++bagCount ;   // set unique (next) bag id

        this.integrityOk = false ;  // not usable yet

        // validate configuration parameters
        validateLimits() ;

        initializeCounters() ;

        // set the capacity according to specification
        // TODO add argument to the instantiation
        this.groceryBag = new ArrayBag<>() ;    // STUB invocation

        this.integrityOk = true ;   // now we're usable

        }   // end no-arg constructor


    /*
     * API methods
     */


    /**
     * add a grocery item to the grocery bag assuming:
     * <ol>
     * <li>there is adequate capacity to hold the grocery item
     * <li>the grocery item is compatible with any items already in the bag
     * </ol>
     *
     * @param groceryItem
     *     the grocery item to add
     *
     * @return {@code true} if the grocery item was added, {@code false} otherwise
     */
    public boolean add( final GroceryItem groceryItem )
        {

        checkIntegrity() ;

        if ( !canAddItem( this, groceryItem ) )
            {
            // the bag has inadequate capacity for, or is incompatible with, the grocery item
            return false ;
            }

        // we can take this grocery item

        if ( !this.groceryBag.add( groceryItem ) )
            {
            throw new GroceryItemNotBaggableException( String.format( "failed to save the grocery item: '%s' in bag %,d%n",
                                                                      groceryItem,
                                                                      this.bagId ) ) ;
            }

        accountForAddedItem( groceryItem ) ;

        return true ;   // successfully added the grocery item

        }   // end add()


    @Override
    public int compareTo( final GroceryBag otherBag )
        {
        // this enables bags to be sorted according to their ids

        checkIntegrity() ;

        if ( otherBag == null )
            {
            throw new NullPointerException() ;
            }

        return ( this.bagId - otherBag.bagId ) ;

        }   // end compareTo()


    /**
     * determine if we're holding at least one matching grocery item
     *
     * @param groceryItem
     *     the item to check for
     *
     * @return {@code true} if there's at least one matching item, {@code false} otherwise
     */
    public boolean contains( final GroceryItem groceryItem )
        {

        checkIntegrity() ;

        return this.groceryBag.contains( groceryItem ) ;

        }   // end contain()


    @Override
    public boolean equals( final Object otherObject )
        {

        checkIntegrity() ;

        if ( otherObject instanceof final GroceryBag otherGroceryBag )
            {
            // compares the grocery bag's id - must be consistent with compareTo()
            return this.bagId == otherGroceryBag.bagId ;
            }

        return false ;  // not a match

        }   // end equals()


    /**
     * determine the number of grocery items currently in the bag
     *
     * @return the number of grocery items
     */
    public int getItemCount()
        {

        checkIntegrity() ;

        return this.groceryBag.getCurrentSize() ;

        }   // end getItemCount()
    
    
    /**
     * determine the remaining number of grocery items this grocery bag can still accept 
     * 
     * @return the number of remaining capacity (items)
     *
     * @since 3.0
     */
    public int getRemainingItemsAvailable()
        {

        checkIntegrity() ;

        return this.remainingItemsAvailable ;
        
        }   // end getRemainingItemsAvailable()
    

    /**
     * determine the remaining amount of space available in this grocery bag
     * 
     * @return the amount of space in 'units'
     *
     * @since 3.0
     */
    public int getRemainingSpaceAvailable()
        {

        checkIntegrity() ;

        return this.remainingSpaceAvailable ;
        
        }   // end getRemainingSpaceAvailable()
    

    /**
     * determine the remaining amount of weight available in this grocery bag
     * 
     * @return the amount of weight in 'units'
     *
     * @since 3.0
     */
    public int getRemainingWeightAvailable()
        {

        checkIntegrity() ;

        return this.remainingWeightAvailable ;
        
        }   // end getRemainingWeightAvailable()


    /**
     * determine the number of times a grocery item appears in the bag
     *
     * @param groceryItem
     *     the item to count
     *
     * @return the number of times it occurs in the bag
     */
    public int getNumberOf( final GroceryItem groceryItem )
        {

        checkIntegrity() ;

        return this.groceryBag.getFrequencyOf( groceryItem ) ;

        }   // end getNumberOf()


    @Override
    public int hashCode()
        {

        checkIntegrity() ;

        return Objects.hashCode( this.bagId ) ;

        }   // end hashCode()


    /**
     * determine if there are any breakable items in the bag
     *
     * @return {@code true} if there are any breakable items in the bag, {@code false} otherwise
     */
    public boolean hasBreakableItems()
        {

        checkIntegrity() ;

        return this.breakableItemCount > 0 ;

        }   // end hasBreakableItems()


    /**
     * determine if there are any hard (or harder) items in the bag
     *
     * @return {@code true} if there are any hard items in the bag, {@code false} otherwise
     */
    public boolean hasHardItems()
        {

        checkIntegrity() ;

        return this.hardItemCount > 0 ;

        }   // end hasHardItems()


    /**
     * determine if there are any heavy (or heavier) items in the bag
     *
     * @return {@code true} if there are any heavy items in the bag, {@code false} otherwise
     */
    public boolean hasHeavyItems()
        {

        checkIntegrity() ;

        return this.heavyItemCount > 0 ;

        }   // end hasHeavyItems()


    /**
     * determine if there are any large (or bigger) items in the bag
     *
     * @return {@code true} if there are any large items in the bag, {@code false} otherwise
     */
    public boolean hasLargeItems()
        {

        checkIntegrity() ;

        return this.largeItemCount > 0 ;

        }   // end hasLargeItems()


    /**
     * determine if there are any light (or lighter) items in the bag
     *
     * @return {@code true} if there are any light items in the bag, {@code false} otherwise
     */
    public boolean hasLightItems()
        {

        checkIntegrity() ;

        return this.lightItemCount > 0 ;

        }   // end hasLightItems()


    /**
     * determine if there are any perishable items in the bag
     *
     * @return {@code true} if there are any perishable items in the bag, {@code false} otherwise
     */
    public boolean hasPerishableItems()
        {

        checkIntegrity() ;

        return this.perishableItemCount > 0 ;

        }   // end hasPerishableItems()


    /**
     * determine if there are any rigid items in the bag
     *
     * @return {@code true} if there are any rigid items in the bag, {@code false} otherwise
     */
    public boolean hasRigidItems()
        {

        checkIntegrity() ;

        return this.rigidItemCount > 0 ;

        }   // end hasRigidItems()


    /**
     * determine if there are any small (or smaller) items in the bag
     *
     * @return {@code true} if there are any small items in the bag, {@code false} otherwise
     */
    public boolean hasSmallItems()
        {

        checkIntegrity() ;

        return this.smallItemCount > 0 ;

        }   // end hasSmallItems()


    /**
     * determine if there are any soft (or softer) items in the bag
     *
     * @return {@code true} if there are any soft items in the bag, {@code false} otherwise
     */
    public boolean hasSoftItems()
        {

        checkIntegrity() ;

        return this.softItemCount > 0 ;

        }   // end hasSoftItems()


    /**
     * determine if the grocery bag is empty
     *
     * @return {@code true} if there are no grocery items in the bag, {@code false} otherwise
     */
    public boolean isEmpty()
        {

        checkIntegrity() ;

        // TODO implement this
        return false ;  // STUB value

        }   // end isEmpty()


    /**
     * determine if the grocery bag has reached one or more capacity limits
     *
     * @return {@code true} if the bag is full (can't accept any more grocery items), {@code false}
     *     otherwise
     */
    public boolean isFull()
        {

        checkIntegrity() ;

        // TODO implement this
        return false ;  // STUB value

        }   // end isFull()


    /**
     * remove an unspecified grocery item from the grocery bag
     *
     * @return a reference to the removed item if successful, {@code null} otherwise
     */
    public GroceryItem remove()
        {

        checkIntegrity() ;

        final GroceryItem groceryItem = this.groceryBag.remove() ;

        if ( groceryItem != null )  // we actually removed something from the bag so update the
                                    // grocery bag's state
            {
            accountForRemovedItem( groceryItem ) ;
            }

        return groceryItem ;

        }   // end remove()


    /**
     * remove the specified item from the grocery bag
     *
     * @param groceryItem
     *     item the item to remove
     *
     * @return {@code true} if the item was in the grocery bag, {@code false} otherwise
     */
    public boolean remove( final GroceryItem groceryItem )
        {

        checkIntegrity() ;

        // if we can remove the item, update the grocery bag's status
        final boolean wasInBag = this.groceryBag.remove( groceryItem ) ;

        if ( wasInBag )     // the item was in the bag so adjust all grocery bag metrics
            {
            accountForRemovedItem( groceryItem ) ;
            }   // end if

        return wasInBag ;

        }   // end remove()


    /**
     * retrieve all grocery items from the grocery bag<br>
     * the resulting grocery bag is empty
     *
     * @return references to all the grocery items in the bag
     */
    public GroceryItem[] removeAll()
        {

        checkIntegrity() ;

        final GroceryItem[] allGroceryItems = toArray() ;  // we'll return an array of the items

        // reset all grocery bag state - empty the actual Bag contents
        // this.groceryBag. ;  // TODO uncomment then invoke the appropriate method to do the work

        // reset state to empty
        // TODO invoke the appropriate method to do the work

        return allGroceryItems ;

        }   // end removeAll()


    /**
     * return an inventory of grocery items to the caller<br>
     * the grocery bag's contents are unchanged
     *
     * @return the grocery items currently in the grocery bag
     */
    public GroceryItem[] toArray()
        {

        checkIntegrity() ;

        final int itemCount = 0 ;   // TODO replace 0 with a meaningful value

        final GroceryItem[] groceryItems = new GroceryItem[ itemCount ] ;

        System.arraycopy( this.groceryBag.toArray(),
                          0,
                          groceryItems,
                          0,
                          itemCount ) ;

        return groceryItems ;

        }   // end toArray()


    @Override
    public String toString()
        {

        checkIntegrity() ;

        final StringBuilder bagContents = new StringBuilder( toStringHeader() +
                                                             " [" ) ;

        final GroceryItem[] baggedGroceryItems = toArray() ;
        GroceryItem previousItem = null ;

        // put the grocery items in alphabetic order for display - enables folding if duplicates
        Arrays.sort( baggedGroceryItems ) ;

        for ( final GroceryItem item : baggedGroceryItems )
            {

            if ( ( previousItem != null ) && ( item.equals( previousItem ) ) )
                {
                // skip first item because there's no previous item to check against
                // and skip items already displayed
                }
            else
                {
                bagContents.append( ( previousItem == null
                                            ? ""
                                            : ", " ) + "(" + this.groceryBag.getFrequencyOf( item ) +
                                                            ") " + item ) ;
                }   // end if/else

            previousItem = item ;
            }   // end for
        
        bagContents.append( "]" ) ;

        return bagContents.toString() ;

        }   // end toString()


    /**
     * format the header/summary for the grocery bag
     *
     * @return the formatted info about this bag
     */
    private String toStringHeader()
        {

        return String.format( """
                              Bag %,d: \
                              %,d of %s items; \
                              %,d of %,d units size; \
                              %,d of %,d units weight; \
                              %s breakables; \
                              %s perishables; \
                              %s rigid items; \
                              %s light items; \
                              %s heavy items; \
                              %s small items; \
                              %s large items; \
                              %s soft items; \
                              %s hard items; \
                              %s empty; \
                              %s full:""",
                              
                              this.bagId,
                              
                              GROCERY_BAG_MAX_ITEM_COUNT - this.remainingItemsAvailable,
                              GROCERY_BAG_MAX_ITEM_COUNT,
                              
                              GROCERY_BAG_MAX_VOLUME - this.remainingSpaceAvailable,
                              GROCERY_BAG_MAX_VOLUME,
                              
                              GROCERY_BAG_MAX_WEIGHT - this.remainingWeightAvailable,
                              GROCERY_BAG_MAX_WEIGHT,
                              
                              ( hasBreakableItems()
                                  ? "has"
                                  : "no" ),
                              ( hasPerishableItems()
                                  ? "has"
                                  : "no" ),
                              ( hasRigidItems()
                                  ? String.format( "has %,d",
                                                   this.rigidItemCount )
                                  : "no" ),

                              ( hasLightItems()
                                  ? String.format( "has %,d",
                                                   this.lightItemCount )
                                  : "no" ),
                              ( hasHeavyItems()
                                  ? String.format( "has %,d",
                                                   this.heavyItemCount )
                                  : "no" ),

                              ( hasSmallItems()
                                  ? String.format( "has %,d",
                                                   this.smallItemCount )
                                  : "no" ),
                              ( hasLargeItems()
                                  ? String.format( "has %,d",
                                                   this.largeItemCount )
                                  : "no" ),


                              ( hasSoftItems()
                                  ? String.format( "has %,d",
                                                   this.softItemCount )
                                  : "no" ),
                              ( hasHardItems()
                                  ? String.format( "has %,d",
                                                   this.hardItemCount )
                                  : "no" ),

                              ( isEmpty()
                                  ? "is"
                                  : "not" ),
                              ( isFull()
                                  ? "is"
                                  : "not" ) ) ;

        }   // end toStringHeader()


    /*
     * private utility methods
     */


    /**
     * update the grocery bag state to reflect the addition of a specific grocery item
     *
     * @param groceryItem
     *     the item being added
     */
    private void accountForAddedItem( final GroceryItem groceryItem )
        {

        // TODO implement this - HINT copy the code from accountForRemovedItem() then modify

        }   // end accountForAddedItem()


    /**
     * update the grocery bag state to reflect the removal of a specific grocery item
     *
     * @param groceryItem
     *     the item being removed
     */
    private void accountForRemovedItem( final GroceryItem groceryItem )
        {
        
        // capacity fields

        this.remainingItemsAvailable++ ;    // there's room for one less item in the grocery bag
        this.remainingSpaceAvailable += groceryItem.size.sizeValue ;
        this.remainingWeightAvailable += groceryItem.weight.weightValue ;

        
        // compatibility fields
        
        if ( groceryItem.isBreakable )
            {
            this.breakableItemCount-- ;
            }
        
        if ( groceryItem.isPerishable )
            {
            this.perishableItemCount-- ;
            }

        if ( groceryItem.isRigid )
            {
            this.rigidItemCount-- ;
            }

        
        // sizes
        
        if ( groceryItem.isSmall )
            {
            this.smallItemCount-- ;
            }
        
        if ( groceryItem.isLarge )
            {
            
            this.largeItemCount-- ;
            }
        
        
        // weights
        
        if ( groceryItem.isLight )
            {
            this.lightItemCount-- ;
            }

        if ( groceryItem.isHeavy )
            {
            this.heavyItemCount-- ;
            }

        
        // firmnesses
        
        if ( groceryItem.isSoft )
            {
            this.softItemCount-- ;
            }

        if ( groceryItem.isHard )
            {
            this.hardItemCount-- ;
            }

        }   // end accountForRemovedItem()


    /**
     * prevent continued execution unless the grocery bag's state is valid
     *
     * @throws SecurityException
     *     indicates the state is invalid
     */
    private void checkIntegrity() throws SecurityException
        {

        if ( !this.integrityOk )
            {
            throw new SecurityException( "GroceryBag state is invalid" ) ;
            }

        }   // end checkIntegrity()


    /**
     * set all counters and limits to reflect an empty grocery bag
     * <p>
     * note: this doesn't affect the contents of the {@code groceryBag}
     */
    private void initializeCounters()
        {
        // capacity fields

        // per specifications
        // TODO set to appropriate limits
        this.remainingItemsAvailable = 0 ;  // STUB
        this.remainingSpaceAvailable = 0 ;  // STUB
        this.remainingWeightAvailable = 0 ; // STUB

        // compatibility fields
        this.breakableItemCount = 0 ;
        this.perishableItemCount = 0 ;
        this.rigidItemCount = 0 ;
        
        // sizes
        this.smallItemCount = 0 ;
        this.largeItemCount = 0 ;
        
        // weights
        this.lightItemCount = 0 ;
        this.heavyItemCount = 0 ;
        
        // firmnesses
        this.softItemCount = 0 ;
        this.hardItemCount = 0 ;

        }   // end initializeCounters()


    /**
     * ensure the configuration file specifies usable limits
     *
     * @throws InvalidSpecificationException
     *     if any limit is too small
     */
    private static void validateLimits() throws InvalidSpecificationException
        {

        // NOTE limit variable is needed to test the constants from GroceryBagLimits
        // - avoids an 'unused' compiler warning
        int limit ;
        
        limit = GROCERY_BAG_MAX_ITEM_COUNT ;
        if ( limit <= 0 )
            {
            throw new InvalidSpecificationException( "maximum number of items" ) ;
            }

        limit = GROCERY_BAG_MAX_VOLUME ;
        if ( limit <= 0 )
            {
            throw new InvalidSpecificationException( "maximum total size/volume" ) ;
            }

        limit = GROCERY_BAG_MAX_WEIGHT ;
        if ( limit <= 0 )
            {
            throw new InvalidSpecificationException( "maximum total weight" ) ;
            }

        }   // end validateLimits()


    /*
     * for testing/debugging
     */


    /**
     * (optional) test driver
     *
     * @param args
     *     -unused-
     */
    public static void main( final String[] args )
        {
        
        final GroceryBag testGroceryBag = new GroceryBag() ;
        System.out.printf( "testBag contents: %s%n", testGroceryBag ) ;

        }	// end main()

    }   // end class GroceryBag