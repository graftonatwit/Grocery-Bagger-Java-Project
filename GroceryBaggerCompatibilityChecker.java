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

/**
 * utility methods for determining compatibility between a grocery bag and a grocery item
 * <p>
 * Note: all methods in this class are static
 *
 * @author David M Rosenberg
 *
 * @version 0.1 2024-10-17 Initial skeleton
 * 
 * @author Trevor Grafton 
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
public class GroceryBaggerCompatibilityChecker
    {
    /*
     * symbolic constants
     */
    // none
    

    /*
     * static variables
     */
    // none
    
    
    
    
    
    

    /*
     * data fields
     */
    // not applicable


    /*
     * constructors
     */


    // prevent instantiation
    private GroceryBaggerCompatibilityChecker()
        {
        
        }


    /*
     * API methods
     */


    /**
     * enable application to determine if a grocery item can be added to a grocery bag
     * <p>
     * this is the only method the {@code GroceryBagger} will use
     * 
     * @param groceryBag
     *     the grocery bag to check
     * @param groceryItem
     *     the grocery item to check
     *
     * @return {@code true} if we can accept the item, {@code false} otherwise
     */
    public static boolean canAddItem( final GroceryBag groceryBag,
                                      final GroceryItem groceryItem )
        {

        // is there adequate capacity and is the item compatible with the current contents of the
        // bag?

        // there can't be any compatibility issues with an empty grocery bag

        // TODO implement this - HINT use other methods to do the work - use a single compound boolean expression
        return hasCapacityFor(groceryBag, groceryItem) && isCompatibleWith(groceryBag, groceryItem);   // STUB value

        }   // end canAddItem()


    /**
     * determine if there is adequate capacity (# of items, weight, space) in the grocery bag for
     * the specified item
     * 
     * @param groceryBag
     *     the grocery bag to test for fit
     * @param groceryItem
     *     the grocery item to test for fit
     *
     * @return {@code true} if there's room for this grocery item, {@code false} otherwise
     */
    private static boolean hasCapacityFor( final GroceryBag groceryBag,
                                           final GroceryItem groceryItem )
        {
        if (groceryBag.getRemainingItemsAvailable() <= 0) {
        return false; // No capacity for more items
        }
        int itemVolume = groceryItem.size.sizeValue; 
        int itemWeight = groceryItem.weight.weightValue; 

        // Check if there's enough space in terms of volume
        if (groceryBag.getRemainingSpaceAvailable() < itemVolume) {
            return false; 
        }

        // Check if there's enough weight capacity
        if (groceryBag.getRemainingWeightAvailable() < itemWeight) {
            return false; 
        }

    return true;
    }

    
          // STUB value
        

           // end hasCapacityFor()


    /**
     * determine if a grocery bag can hold a given grocery item
     * <p>
     * note: this check is bi-directional
     *
     * @param groceryBag
     *     the grocery bag to test for compatibility with the given grocery item
     * @param groceryItem
     *     the grocery item to test for compatibility with the given grocery bag
     *
     * @return {@code true} if the grocery bag and grocery item are compatible with each other,
     *     {@code false} otherwise
     */
    private static boolean isCompatibleWith( final GroceryBag groceryBag,
                                             final GroceryItem groceryItem )
        {
        // compare the attributes of the item against what's already in the bag and vice versa
        // - evaluate the bag's attributes via provided methods
        // - do not (re-)evaluate the items already in the bag

        // there can't be any compatibility issues with an empty grocery bag
        // TODO implement this
        
        // there's at least one item in the bag so we have potential incompatibility

        // TODO implement compatibility checks per your heuristics
        

        // Apply the heuristics
        if (groceryBag.hasHeavyItems() && groceryItem.isSoft) {
            return false; // Rule: No heavy item with soft items
        }
        
        if (groceryBag.hasRigidItems() && groceryItem.isPerishable) {
            return false; // Rule: No rigid items with perishable items
        }

        if (groceryBag.hasBreakableItems() && groceryItem.isHeavy) {
            return false; // Rule: No breakable items with heavy items
        }

        if (groceryBag.hasBreakableItems() && groceryItem.isHard) {
            return false; // Rule: No breakable items with hard items
        }

        if (groceryBag.hasRigidItems() && groceryItem.isSoft) {
            return false; // Rule: No rigid items with soft items
        }

        if (groceryBag.hasHardItems() && groceryItem.isSoft) {
            return false; // Rule: No hard items with soft items
        }
        
        if (groceryBag.hasLightItems() && ((groceryItem.isHard) && (groceryItem.isHeavy))) {
        return false; // Rule: No light items with hard, heavy items
    }
        
        if (groceryBag.hasSoftItems() && groceryItem.isHeavy) {
        return false; // Rule: No heavy item with soft items
    }
        
        if (groceryBag.hasPerishableItems() && groceryItem.isRigid) {
        return false; // Rule: No rigid items with perishable items
    }

        if (groceryBag.hasHeavyItems() && groceryItem.isBreakable) {
        return false; // Rule: No breakable items with heavy items
    }
        if (groceryBag.hasBreakableItems() && groceryItem.isHard) {
        return false; // Rule: No breakable items with hard items
    } 
        
        if (groceryBag.hasSoftItems() && groceryItem.isRigid) {
        return false; // Rule: No rigid items with soft items
    }
        
        if (groceryBag.hasSoftItems() && groceryItem.isHard) {
        return false; // Rule: No hard items with soft items
    }
       
        if (((groceryBag.hasHardItems()) && (groceryBag.hasHeavyItems())) && groceryItem.isLight) {
        return false; // Rule: No light items with hard, heavy items
    }

                // If none of the rules are violated, the item can be added
        
        
        return true ;   // STUB value

        }   // end isCompatibleWith()

    }   // end class GroceryBaggerCompatibilityChecker