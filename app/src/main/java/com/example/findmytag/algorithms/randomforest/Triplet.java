package com.example.findmytag.algorithms.randomforest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Triplet class
class Triplet<U, V, T>
{
    public final U first;       // the first field of a triplet
    public final V second;      // the second field of a triplet
    public final T third;       // the third field of a triplet

    // Constructs a new triplet with the given values
    Triplet(U first, V second, T third)
    {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public boolean equals(Object o)
    {
        /* Checks specified object is "equal to" the current object or not */

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Triplet triplet = (Triplet) o;

        // call `equals()` method of the underlying objects
        if (!first.equals(triplet.first) ||
                !second.equals(triplet.second) ||
                !third.equals(triplet.third)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        /* Computes hash code for an object by using hash codes of
        the underlying objects */

        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        result = 31 * result + third.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ")";
    }

    // Factory method for creating a typed immutable instance of triplet
    public static <U, V, T> Triplet <U, V, T> of(U a, V b, T c) {
        return new Triplet <>(a, b, c);
    }
}

