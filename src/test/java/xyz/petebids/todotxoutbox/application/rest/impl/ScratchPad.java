package xyz.petebids.todotxoutbox.application.rest.impl;

public class ScratchPad {


    void swapStringsNoNewVariables(String left, String right){

        left = left + right;


        right = left.substring(0, left.length() - right.length());


        left = left.substring(right.length());


    }


    void pay() {
        final Employee employee = getEmployee();


        double salary = employee.salary();


    }


    private Employee getEmployee() {
        return null;
    }


    static abstract class Employee {

        abstract String type();

        abstract double salary();

    }


    static class Contractor extends Employee {

        @Override
        String type() {
            return null;
        }

        @Override
        double salary() {
            return 0;
        }
    }


    static class Perm extends Employee {
        @Override
        String type() {
            return null;
        }

        @Override
        double salary() {
            return 0;
        }
    }


    interface Printer {
        void print();


    }

    interface FaxMachine {

        void fax();

    }

    interface Scanner {
        void scan();
    }


    interface MultiFunctionPrinter extends Printer, FaxMachine, Scanner {

    }

}
