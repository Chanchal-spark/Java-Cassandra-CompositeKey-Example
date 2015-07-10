package Chanchal.Cassendra;

import com.netflix.astyanax.annotations.Component;

public class Population {

    @Component(ordinal=0) String state;
    @Component(ordinal=1) String city;
    @Component(ordinal=2) Integer zipcode;

    public Population() {
    }

    public Population(String state, String city, Integer zipcode) {
        this.state = state;
        this.city = city;
        this.zipcode = zipcode;
    }

    public String toString() {
        return "Population [" + state + ", " + city + ", " + zipcode + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((zipcode == null) ? 0 : zipcode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null)return false;
        if (getClass() != obj.getClass()) return false;
        Population other = (Population) obj;
        boolean equal = true;
        equal &= (state != null) ? (state.equals(other.state)) : other.state == null;
        equal &= (city != null) ? (city.equals(other.city)) : other.city == null;
        equal &= (zipcode != null) ? (zipcode.equals(other.zipcode)) : other.zipcode == null;
        return equal;
    }

    public Population clone() {
        return new Population(state, city, zipcode);
    }
}