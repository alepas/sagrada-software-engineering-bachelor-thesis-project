package it.polimi.ingsw.model.usersdb;

public class Test {

    public static void main ( String args[] ) {
        System.out.println ( Number.x );
        Number.x=34; // accessing static variable through class name

        Number num1 =new Number ();
        num1.y=3;
        System.out.println ( num1.x );
        System.out.println( num1.y );
        num1.x = 347; // accessing static variable through an object
        System.out.println(Number.x);

        Number num2 = new Number ();
        num2.y=4;
        System.out.println ( num2.x );
        System.out.println( num2.y );
        num2.y=3479;

// The three statements below print the same value as there is only one copy of a static variable which is shared by all the objects
        System.out.println ( Number.x );
        System.out.println ( num1.x );
        System.out.println ( num2.x );
    }
}