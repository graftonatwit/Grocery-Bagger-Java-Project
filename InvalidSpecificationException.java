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
 * Exception indicating that a specification is invalid. Applies to grocery bags and grocery items.
 *
 * @author David M Rosenberg
 *
 * @version 1.0.0 2017-05-23 Initial implementation
 * @version 1.1.0 2020-02-09 update to coding standard
 * @version 1.2.0 2024-02-10 change from checked to unchecked exception
 */
public class InvalidSpecificationException extends RuntimeException
    {

    // make this exception safely serializable
    private static final long serialVersionUID = 1L ;


    /**
     * Generic message
     */
    public InvalidSpecificationException()
        {
        super( "Invalid specification" ) ;

        } // end no-arg constructor


    /**
     * Specified message
     *
     * @param message
     *     application message to include with exception
     */
    public InvalidSpecificationException( final String message )
        {
        super( "Invalid specification: " + message ) ;

        } // end 1-arg constructor


    /**
     * Test driver
     *
     * @param args
     *     -unused-
     */
    public static void main( final String[] args )
        {
        // default message
        InvalidSpecificationException testException = new InvalidSpecificationException() ;
        System.out.println( testException.getMessage() ) ;

        // specified message
        testException = new InvalidSpecificationException( "SNAFU" ) ;
        System.out.println( testException.getMessage() ) ;

        }   // end main()

    }   // end class InvalidSpecificationException